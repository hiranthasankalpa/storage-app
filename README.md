# storage-app
### Provide users with a straightforward platform for storing and sharing files


## Code styling

For Intellij Idea, import Google code style scheme from
### ``intellij-java-google-style.xml``


To generate report, run
### ``mvn checkstyle:checkstyle``


## To deploy and run on docker
### ``docker-compose up -d --build``


## API Definition

Once deployed, Swagger UI can be accessed for API Definition at
### ``http://localhost:8080/api/v1/storage-app/swagger-ui/index.html``


## Postman collection provided at root for import
### ``Storage-App.postman_collection.json``


## API List

### Upload file

### Method: POST
### ``http://localhost:8080/api/v1/storage-app/files/upload``

#### HEADER
    X-User-Name - alphanumeric username, only name is required to identify owners for simplicity

#### FORM DATA
    file - Multipart file
    tags - comma seperated tags ( Eg: aa,bb,cc )
    file-name - string
    visibility - PRIVATE or PUBLIC


### List files with paging, sorting and filtering

### Method: POST
### ``http://localhost:8080/api/v1/storage-app/files/list``

#### HEADER
    X-User-Name - alphanumeric username, only name is required to identify owners for simplicity

#### REQUEST BODY
````
{
    "page": 0,                      // default 0
    "sizePerPage": 5,               // default 5
    "filterTags": ["aaa", "bbb"],   // optional
    "sortField": "FILE_SIZE",       // default UPLOADED_DATE: FILE_NAME, TAGS, FILE_TYPE, FILE_SIZE
    "sortDirection":  "DESC"        // default DESC: ASC
}
````


### Rename file

### Method: PUT
### ``http://localhost:8080/api/v1/storage-app/files/rename``

#### HEADER
    X-User-Name - alphanumeric username, only name is required to identify owners for simplicity

#### REQUEST BODY
````
{
    "id": "6653ad474cf6136eb475456f",
    "fileName": "new_name.txt"
}
````


### Delete file

### Method: DELETE
### ``http://localhost:8080/api/v1/storage-app/files/rename/6653ad474cf6136eb475456f``

#### HEADER
    X-User-Name - alphanumeric username, only name is required to identify owners for simplicity


### Download file
This is the generated download path for any file

### Method: GET
### ``http://localhost:8080/api/v1/storage-app/files/download/6653ad474cf6136eb475456f``