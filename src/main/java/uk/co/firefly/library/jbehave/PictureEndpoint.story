Scenario: Create a new book
Given a POST request with the url BOOK-RESOURCE and json { "title": "Test title two" }
When a caller hits POST endpoint
Then an expected response code should be 201
Then save last response body under key JBEHAVE-BOOK-TWO 
Then a response should have strings authorId,id,publisherId,title

Scenario: Create a new PICTURE
Given a loading of previous response body under key JBEHAVE-BOOK-TWO was successful
Given a POST request with the url PICTURE-RESOURCE and type FRONT_COVER and bookId of id
When a caller hits POST endpoint
Then an expected response code should be 201
Then save last response body under key JBEHAVE-PICTURE-ONE 
Then a response should have strings id,name,pictureType,mimetype,bookId

Scenario: Get an existing PICTURE
Given a loading of previous response body under key JBEHAVE-PICTURE-ONE was successful
Given a previous resource was created at an url key PICTURE-RESOURCE with an identifier id and is ready for GET operation
When a caller hits GET endpoint
Then an expected response code should be 200

Scenario: Delete an existing PICTURE
Given a loading of previous response body under key JBEHAVE-PICTURE-ONE was successful
Given a previous resource was created at an url key PICTURE-RESOURCE with an identifier id and is ready for DELETE operation  
When a caller hits DELETE endpoint
Then an expected response code should be 204

Scenario: Delete an existing book
Given a loading of previous response body under key JBEHAVE-BOOK-TWO was successful
Given a previous resource was created at an url key BOOK-RESOURCE with an identifier id and is ready for DELETE operation  
When a caller hits DELETE endpoint
Then an expected response code should be 204