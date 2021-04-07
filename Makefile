
export APP_VERSION ?=$(shell jq -j '.version' package.json)
export SERVER_PORT ?=8080
export LOG_LEVEL ?= info

# Uses the atrifact url if provided else assumes ECR using the provided AWS account details
export AWS_REGION ?= $(AWS_DEFAULT_REGION)
export ARTIFACT_URL ?= $(AWS_ACCOUNT_ID).dkr.ecr.$(AWS_REGION).amazonaws.com

export IMAGE_NAME=products-api

# deploy env
export DEPLOY_ENV ?= dev

# Docker image: BUILD, RUN, TAG and PUSH

.PHONY: build
build: 
	docker-compose run --rm build-products-api ./gradlew build

.PHONY: build_image
build_image:
	docker-compose build build-products-api-container

.PHONY: run_image
run_image: 
	docker-compose up build-products-api-container

.PHONY: test
test: 
	@echo "run integration tests"
	docker-compose up -d --force-recreate build-products-api-container
	@sleep 10
	docker-compose run --rm tester yarn && yarn integration-test
	docker-compose down

.PHONY: tag_image
tag_image:
	@echo "tagging the image $(IMAGE_NAME):$(APP_VERSION)"
	docker tag $(IMAGE_NAME) $(ARTIFACT_URL)/$(IMAGE_NAME):$(APP_VERSION)
	@echo "tagging latest $(IMAGE_NAME):latest"
	docker tag $(IMAGE_NAME) $(ARTIFACT_URL)/$(IMAGE_NAME):latest

.PHONY: push_image
push_image:
	@echo "push the image $(IMAGE_NAME):$(APP_VERSION)"
	docker push $(ARTIFACT_URL)/$(IMAGE_NAME):$(APP_VERSION)
	docker push $(ARTIFACT_URL)/$(IMAGE_NAME):latest

# In case of ECR as artifact: Heplers to login to ECR
.PHONY: ecr_login
ecr_login:
	aws ecr get-login-password --region $(AWS_REGION) | docker login --username AWS --password-stdin $(ARTIFACT_URL)

.PHONY: ecr_create_repository
ecr_create_repository:
	aws ecr create-repository \
    --repository-name $(IMAGE_NAME) \
    --image-scanning-configuration scanOnPush=true \
    --region $(AWS_REGION)

# INFRA code
.PHONY: infra_init
infra_init:
	docker-compose run --rm infra init ./deploy

.PHONY: infra_format
infra_format:
	docker-compose run --rm infra fmt -recursive .

.PHONY: infra_plan
infra_plan: infra_init
	docker-compose run --rm infra plan -var-file="./env/${DEPLOY_ENV}-${AWS_REGION}.tfvars" -var "products_api_image_url=$(ARTIFACT_URL)/$(IMAGE_NAME):$(APP_VERSION)" -var "aws_region=$(AWS_REGION)" ./deploy 

.PHONY: infra_deploy
infra_deploy: infra_init
	docker-compose run --rm infra apply -auto-approve -var-file="./env/${DEPLOY_ENV}-${AWS_REGION}.tfvars" -var "products_api_image_url=$(ARTIFACT_URL)/$(IMAGE_NAME):$(APP_VERSION)"  -var "aws_region=$(AWS_REGION)" ./deploy 

.PHONY: infra_destroy
infra_destroy: infra_init
	docker-compose run --rm infra destroy -auto-approve -var-file="./env/${DEPLOY_ENV}-${AWS_REGION}.tfvars" -var "products_api_image_url=$(ARTIFACT_URL)/$(IMAGE_NAME):$(APP_VERSION)"  -var "aws_region=$(AWS_REGION)" ./deploy 

.PHONY: infra_shell
infra_shell:
	docker-compose run --rm --entrypoint sh infra 