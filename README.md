# customers-app

Simple Customers app.

## Requirements

For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/downloads/#jdk17)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.customer.data.DataApplication` class from your IDE.

## Deploying the application to Azure

The easiest way to run the application from internet is to deploy the application to Azure and use this url: https://customers-app.azurewebsites.net/customers:

This will expose the following endpoints to be called:

### How to run the application.

```
http://localhost:8080/customers  - for running locally
https://customers-app.azurewebsites.net/customers  - for running from azure
```

### GET: endpoint to retrieve all customers from application database:
```
https://customers-app.azurewebsites.net/customers

GET
Accept: application/json
Content-Type: application/json

Response: HTTP 200
Content: list of customers
```


### POST: endpoint to add a customer to application database:
```
https://customers-app.azurewebsites.net/customers

POST 
Accept: application/json
Content-Type: application/json

{
    "id": 1,
    "firstName": "First", 
    "lastName": "Last", 
    "email": "name@email.com", 
    "age": 18,
    "currentLivingAddress": {
            "country": "Country", 
            "city": "City", 
            "street": "Street", 
            "houseNumber": 1, 
            "postalCode": "code"
        }
}

Response: HTTP 200
Content: the created customer
```


### PUT: endpoint to update only the email or address for a customer from application database:
```
https://customers-app.azurewebsites.net/customers/update

PUT 
Accept: application/json
Content-Type: application/json

{
    "id": 1,
    "firstName": "First", 
    "lastName": "Last", 
    "email": "name@email.com", 
    "age": 18,
    "currentLivingAddress": {
            "country": "Country", 
            "city": "City", 
            "street": "Street", 
            "houseNumber": 1, 
            "postalCode": "code"
        }
}

Response: HTTP 200
Content: the updates customer
```


### GET: endpoint to retrieve a customer by its id from application database:
```
https://customers-app.azurewebsites.net/customers/{id}

GET
Accept: application/json
Content-Type: application/json

Response: HTTP 200
Content: the searched customer by id
```
The {id} will be replaced with the id number of desired customer to be retrieved.


### GET: endpoint to search a customer by first name or last name from application database:
```
https://customers-app.azurewebsites.net/customers/name/{name}

GET
Accept: application/json
Content-Type: application/json

Response: HTTP 200
Content: the searched customers by first name or last name
```
The {name} will be replaced with the first name or last name text of desired customers to be searched.


### To view your H2 in-memory datbase

The 'test' profile runs on H2 in-memory database. To view and query the database you can browse to http://localhost:8090/h2-console. Default username is 'sa' with a blank password.

