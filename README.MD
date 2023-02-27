# News Feed Engine app
***

This is the REST API for the News Engine app. It allows users to access news articles and  comments.

> **Used stack**: _Java 17, Spring boot 3, Jooq 8.1, Postgres 14, Lombok, Flyway 9.5.1_

***
**Required elements:**
* PostgreSQL 14
* JDK 17
* Gradle 7.*
* Created database (in case using CLI)
***
**Launch of the project**

Using CLI:

* create database with name ***newsfeed***
* change login/password in file src/main/resources/application.properties and flyway section in /build.gradle
* execute in console `gradle build`
* execute in console  `java -jar build/libs/news-0.0.1-SNAPSHOT.jar`

Using Docker:
* create docker images `docker-compose build`
* start the application `docker-compose up -d`

### Request and Response Formats
All requests and responses use JSON format.
## Endpoints
 ### `GET /api/v1/news`
Get a list of news articles with optional query parameters. For example:

`GET http://localhost:8080/api/v1/news?page=1&size=20&author=author&commentNumFrom=3&CommentNumTo=3&sort=commentNum,DESC&sort=author,ASC`

Pagination: 
*  The "**page**" parameter is used to specify the page number of results to retrieve, with a default value of 0
* The "**size**" parameter is used to specify the number of results per page, with a default value of 10

Filtering:
* The "**author**" parameter is used to filter results by the author name of the news articles
* The "**commentNumFrom**" and "**CommentNumTo**" parameters are used to filter results by the number of comments on the news articles
* The "**createdFrom**" and "**createdTo**" parameters fre used to filter results by the date of creation of the news articles

Sorting (`sort=`) is used to specify the sorting order of the results, with the ability to sort by multiple criteria:
*  "**author**" parameter is used to sort by author name of the news articles
*  "**created**" (default criteria with descending order) parameter is used to sort by date of publishing of the news articles 
*  "**commentNum**" (default criteria with descending order) parameter is used to sort by number of comments of the news articles 

### `POST /api/v1/news`

Create a new news article. The request body should include the article data. For example:
```
POST http://localhost:8080/api/v1/news
Content-Type: application/json

{
"title": "title",
"body":"body",
"author": "author name"
}
```
Where "title" is a title of creating article (it should be unique), "body" is a text of creating article, "author"
is a name of creating article author. The response will contain the representation of creating news article with short version of article text (50 characters).
### `PUT /api/v1/news`
Update an existing news article. The request body should include the new article data. For example:
```
PUT http://localhost:8080/api/v1/news
Content-Type: application/json

{
"id": "1",
"title": "new title",
"body":"new body",
"author": "author name",
"disable": "false"
}
```
Where "id" is an ID of updating article, "title" is a title of updating article (it should be unique), "body" is a text of updating article, "author" 
is a name of updating article author, "disable" is mark for disabling article (it will not return a GET request for a list of articles).
The response will contain the representation of updating news article with short version of article text (50 characters).


### `GET /api/v1/news/{itemId}`
Get a specific news article by ID. The response will return news article JSON with full article text.

### `GET /api/v1/news/{itemId}/comments`
Get a list of comments for a specific news article by ID.

### `POST /api/v1/comments`
Create a new comment. The request body should include the comment data. For example:
```
POST http://localhost:8080/api/v1/news/comments
Content-Type: application/json

{
"commentator": "commentator name",
"body":"comment text",
"itemId": "1"
}
```
Commentator name must be between 3 and 30 characters. Body of comment must be between 1 and 500 characters. ID of article "itemID"
could not be null. 

### `PUT /api/v1/comments`
Update an existing comment. The request body should include the new comment data and ID of updating comment. For example:
```
PUT http://localhost:8080/api/v1/news/comments
Content-Type: application/json

{
"id": "1",
"commentator": " new commentator name",
"body":"new comment text", 
"itemId": "1"
}
```

### `GET /api/v1/comments/{commentId}`
Get a specific comment by ID.

### `DELETE /api/v1/comments/{commentId}`
Delete a specific comment by ID.

### Error Handling
If an error occurs, the API will return an appropriate HTTP error code (e.g. 400 Bad Request, 404 Not Found) along with a JSON response containing an error message.

### Authentication
Authentication is not currently required to use this API.

### Credits
This app was created by Vlad Krotov.

email : geraldik1986@gmail.com




