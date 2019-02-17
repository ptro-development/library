Scenario: Create a new book
Given a POST request with the url BOOK-RESOURCE and json {"title": "Test title"}
When a caller hits POST endpoint
Then an expected response code should be 201
Then save last response body under key JBEHAVE-BOOK-ONE 
Then a response should have strings authorId,id,publisherId,title,isbn10,isbn13

Scenario: Get an existing book
Given a loading of previous response body under key JBEHAVE-BOOK-ONE was successful
Given a previous resource was created at an url key BOOK-RESOURCE with an identifier id and is ready for GET operation
When a caller hits GET endpoint
Then an expected response code should be 200
Then a response should have strings authorId,id,publisherId,title,isbn10,isbn13

Scenario: Get all existing books
Given a previous resources were created at an url key BOOK-RESOURCE and are ready for GET operation
When a caller hits GET endpoint
Then an expected response code should be 200
Then a response should be an array where each element have strings authorId,id,publisherId,title

Scenario: Modify an existing book
Given a loading of previous response body under key JBEHAVE-BOOK-ONE was successful
Given a PUT request with an url key BOOK-RESOURCE identifier id and simple key-value json {"title": "NewTitle"}
When a caller hits PUT endpoint
Then an expected response code should be 200
Then save last response body under key JBEHAVE-BOOK-TWO 
Then a response should have strings authorId,id,publisherId,title,isbn10,isbn13
Then a response should have strings NewTitle

Scenario: Delete an existing book
Given a loading of previous response body under key JBEHAVE-BOOK-TWO was successful
Given a previous resource was created at an url key BOOK-RESOURCE with an identifier id and is ready for DELETE operation  
When a caller hits DELETE endpoint
Then an expected response code should be 204