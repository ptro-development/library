Scenario: Create a new author
Given a POST request with the url AUTHOR-RESOURCE and json {"about": "someAbout","firstName": "someFirstName","lastName": "someLastName","middleName": "someMiddleName"}
When a caller hits POST endpoint
Then an expected response code should be 201
Then save last response body under key JBEHAVE-AUTHOR-ONE 
Then a response should have strings id,about,firstName,middleName,lastName

Scenario: Get an existing author
Given a loading of previous response body under key JBEHAVE-AUTHOR-ONE was successful
Given a previous resource was created at an url key AUTHOR-RESOURCE with an identifier id and is ready for GET operation
When a caller hits GET endpoint
Then an expected response code should be 200
Then a response should have strings id,about,firstName,middleName,lastName

Scenario: Get all existing authors
Given a previous resources were created at an url key AUTHOR-RESOURCE and are ready for GET operation
When a caller hits GET endpoint
Then an expected response code should be 200
Then a response should be an array where each element have strings id,about,firstName,middleName,lastName

Scenario: Modify an existing author
Given a loading of previous response body under key JBEHAVE-AUTHOR-ONE was successful
Given a PUT request with an url key AUTHOR-RESOURCE identifier id and simple key-value json {"about": "someNewAbout","firstName": "someNewFirstName","lastName": "someNewLastName","middleName": "someNewMiddleName"}
When a caller hits PUT endpoint
Then an expected response code should be 200
Then a response should have strings id,about,firstName,middleName,lastName
Then a response should have strings someNewAbout,someNewFirstName,someNewLastName,someNewMiddleName

Scenario: Delete an existing author
Given a loading of previous response body under key JBEHAVE-AUTHOR-ONE was successful
Given a previous resource was created at an url key AUTHOR-RESOURCE with an identifier id and is ready for DELETE operation  
When a caller hits DELETE endpoint
Then an expected response code should be 204