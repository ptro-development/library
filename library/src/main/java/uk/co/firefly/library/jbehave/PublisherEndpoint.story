Scenario: Create a new publisher
Given a POST request with the url PUBLISHER-RESOURCE and json {"address": "someAddress", "name": "someName", "webAddress": "webAddress"}
When a caller hits POST endpoint
Then an expected response code should be 201
Then save last response body under key JBEHAVE-PUBLISHER-ONE 
Then a response should have strings id,address,name,webAddress

Scenario: Get an existing publisher
Given a loading of previous response body under key JBEHAVE-PUBLISHER-ONE was successful
Given a previous resource was created at an url key PUBLISHER-RESOURCE with an identifier id and is ready for GET operation
When a caller hits GET endpoint
Then an expected response code should be 200
Then a response should have strings id,address,name,webAddress

Scenario: Get all existing publishers
Given a previous resources were created at an url key PUBLISHER-RESOURCE and are ready for GET operation
When a caller hits GET endpoint
Then an expected response code should be 200
Then a response should be an array where each element have strings id,address,name,webAddress

Scenario: Modify an existing publisher
Given a loading of previous response body under key JBEHAVE-PUBLISHER-ONE was successful
Given a PUT request with an url key PUBLISHER-RESOURCE identifier id and simple key-value json {"address": "newAddress", "name": "newName", "webAddress": "newWebAddress"}
When a caller hits PUT endpoint
Then an expected response code should be 200
Then save last response body under key JBEHAVE-PUBLISHER-TWO 
Then a response should have strings id,address,name,webAddress
Then a response should have strings newAddress,newName,newWebAddress

Scenario: Delete an existing publisher
Given a loading of previous response body under key JBEHAVE-PUBLISHER-TWO was successful
Given a previous resource was created at an url key PUBLISHER-RESOURCE with an identifier id and is ready for DELETE operation  
When a caller hits DELETE endpoint
Then an expected response code should be 204