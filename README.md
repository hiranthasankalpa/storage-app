# storage-app
Provide users with a straightforward platform for storing and sharing files


# Code styling

For Intellij Idea, import Google code style scheme from ``intellij-java-google-style.xml`` 

Run ``mvn checkstyle:checkstyle`` to generate report

docker build -t storage-app .
docker run -d -p 8080:8080 --name storage-app storage-app