# Stock Exchange API

This project is a Spring Boot application that provides a REST API for managing stock exchanges and stocks. The application includes functionality for creating, retrieving, updating, and deleting stock exchanges and stocks, as well as adding and removing stocks from exchanges. It also includes configurations for JaCoCo, Spotless, and Swagger, and uses an H2 in-memory database for testing and development.

## Table of Contents
- [Endpoints](#endpoints)
- [Postman Collection](#postman-collection)
- [Code Quality and Coverage](#code-quality-and-coverage)
- [Swagger API Documentation](#swagger-api-documentation)
- [H2 Database](#h2-database)
- [Running the Application](#running-the-application)
- [Testing](#testing)

## Endpoints

### Stock Exchange Endpoints
- **Create Stock Exchange**
  - `POST /api/v1/stock-exchange`
  - Request Body: `{ "name": "BIST", "description": "Borsa Istanbul", "liveInMarket": true }`
  - Response: `201 Created`

- **Get Stock Exchange by Name**
  - `GET /api/v1/stock-exchange/{name}`
  - Response: `200 OK` with stock exchange details

### Stock Endpoints
- **Create Stock**
  - `POST /api/v1/stock`
  - Request Body: `{ "name": "AKBNK", "description": "Akbank", "currentPrice": 7.45 }`
  - Response: `201 Created`

- **Add Stock to Stock Exchange by ID**
  - `POST /api/v1/stock-exchange/{name}/add-stock-by-id/{stockId}`
  - Response: `204 No Content`

- **Add Stock to Stock Exchange by Name**
  - `POST /api/v1/stock-exchange/{name}/add-stock-by-name/{stockName}`
  - Response: `204 No Content`

- **Remove Stock from Stock Exchange by ID**
  - `POST /api/v1/stock-exchange/{name}/remove-stock-by-id/{stockId}`
  - Response: `204 No Content`

- **Remove Stock from Stock Exchange by Name**
  - `POST /api/v1/stock-exchange/{name}/remove-stock-by-name/{stockName}`
  - Response: `204 No Content`

- **Update Stock Price**
  - `PUT /api/v1/stock/{stockId}/update-price`
  - Request Body: `{ "newPrice": 8.00 }`
  - Response: `200 OK`

- **Delete Stock**
  - `DELETE /api/v1/stock/{stockId}`
  - Response: `204 No Content`

## Postman Collection

A Postman collection (`postman.json`) is included in the repository. This collection contains pre-configured requests for all the endpoints listed above. Import this file into Postman to quickly test and interact with the API.

## Code Quality and Coverage

### JaCoCo

JaCoCo is used for code coverage analysis. Run the following command to generate a coverage report:
```sh
./gradlew clean test jacocoTestReport

Current coverage report is shown below
![Screenshot 2024-07-05 at 01 10 45](https://github.com/gokhanakc7/stock-exchange-api/assets/162465880/4417ba4c-f056-42b1-8284-3ce15d51a425)

