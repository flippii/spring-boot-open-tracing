# Getting started

You need Java 11 installed.

    ./mvn spring-boot:run
    
To test that it works, open a browser tab at http://localhost:8080/api/v1/names/random. 
Alternatively, you can run

    curl -H "user_name: JC" http://localhost:8080/api/v1/names/random 

# Run Zipkin with UI in [Docker](https://www.docker.com/)

You need Docker installed.
	
	docker-compose up -d
