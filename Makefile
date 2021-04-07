
export APP_VERSION?=0.1

export SERVER_PORT?=8080

.PHONY: build
build: 
	docker-compose run --rm build-products-api ./gradlew build

.PHONY: build_image
build_image:
	docker-compose build build-products-api-container

.PHONY: run_image
run_image: 
	docker-compose up build-products-api-container
