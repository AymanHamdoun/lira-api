FROM arm64v8/openjdk:19

WORKDIR /app

COPY . .

CMD ./gradlew run