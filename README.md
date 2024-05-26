# storage-app
Provide users with a straightforward platform for storing and sharing files

# To deploy and run on docker

``docker-compose up -d --build``

# API Definition

Once deployed, Swagger UI can be accessed for API Definition at 

``http://localhost:8080/api/v1/storage-app/swagger-ui/index.html``

# Code styling

For Intellij Idea, import Google code style scheme from 

``intellij-java-google-style.xml`` 

To generate report, run

``mvn checkstyle:checkstyle``