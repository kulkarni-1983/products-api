FROM adoptopenjdk/openjdk14:alpine-jre
LABEL description="products-ap"

ARG ARG_APP_VERSION
ENV APP_VERSION=$ARG_APP_VERSION

WORKDIR /app

COPY /build/libs/products-api-* /app/products-api.jar

CMD exec java -jar ./products-api.jar $@ 
