FROM openjdk:19

WORKDIR /app

COPY . .

CMD ./gradlew run