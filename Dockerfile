FROM adoptopenjdk/openjdk14:alpine-jre
LABEL description="products-ap"


WORKDIR /app

COPY /build/libs/products-api-* /app/products-api.jar

CMD exec java -jar ./products-api.jar $@ 
