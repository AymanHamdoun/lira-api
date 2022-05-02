<img src="https://user-images.githubusercontent.com/21179129/164029239-2dec81e1-c6a1-4b4a-b5ae-2cc328e26286.png" width="200"/>

# LBP Rate Api
LBP Rate API **practice project** to learn Kotlin

## Main Tasks To Practice
- ✅ scrape LBP Rate using [Jsoup](https://jsoup.org/)
- ✅ Store & retrieve the data from a json file (by serializing data classes to json using [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization))
- ✅ Implement an HTTP server using [Ktor](https://ktor.io/docs/welcome.html)
- ✅ Serve the content of the JSON file to the `/rate` route
- ✅ Have the scraping job run every 5 minutes
- ✅ Implement logging using [Logback](https://logback.qos.ch/) through [SLF4J](https://www.slf4j.org/) (which comes already with Ktor)
- ✅ Dockerize the project
- ✅ Implement Basic Database functionality (With Repositories & Models)
- Return Updated at for /rates based on timezone of the client
- Add a cache layer (preferably Redis) to the Repository Getters 
- Allow User Registration and give them Auth Keys
- Only Accept GET requests if a valid Auth key exists in a certain header
- Include a rate limit of 10 requests per minute for each auth key
- Run the project on kubernetes 
