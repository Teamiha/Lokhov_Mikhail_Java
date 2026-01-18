# Vacation Pay Calculator

A Spring Boot microservice for calculating vacation pay according to Russian labor law conventions.

## Overview

This service provides a REST API endpoint to calculate vacation pay based on:
- Average monthly salary for the last 12 months
- Number of vacation days, OR
- Vacation start and end dates (with automatic exclusion of Russian non-working holidays)

## Features

- ✅ RESTful API with single GET endpoint
- ✅ **Web interface** - Beautiful, user-friendly HTML form (no Postman needed!)
- ✅ Automatic holiday exclusion when using date ranges
- ✅ Comprehensive input validation
- ✅ Detailed error handling with meaningful error messages
- ✅ Swagger/OpenAPI documentation
- ✅ Comprehensive unit tests (>90% coverage)
- ✅ Clean, maintainable code following SOLID principles

## Technology Stack

- **Java**: 11
- **Framework**: Spring Boot 2.7.18
- **Build Tool**: Maven
- **Key Dependencies**:
  - Spring Boot Web
  - Spring Boot Validation
  - Lombok
  - SpringDoc OpenAPI (Swagger)
  - JUnit 5 + AssertJ (testing)

## Prerequisites

- Java 11 or higher
- Maven 3.6+ (optional, wrapper can be used)

## Building the Project

### Using Maven Wrapper (Recommended):

```bash
./mvnw clean install
```

On Windows:
```bash
mvnw.cmd clean install
```

### Using Maven (if installed):

```bash
mvn clean install
```

## Running the Application

### Using Maven Wrapper (Recommended - No Maven installation needed):

```bash
./mvnw spring-boot:run
```

On Windows:
```bash
mvnw.cmd spring-boot:run
```

### Using Maven (if installed):

```bash
mvn spring-boot:run
```

### Using Java (after building):

First build the project:
```bash
./mvnw clean package
```

Then run:
```bash
java -jar target/vacation-calculator-1.0.0.jar
```

The application will start on port **8080** by default.

## Web Interface

The application includes a beautiful, user-friendly web interface accessible directly in your browser!

### Access the Web Interface

Simply navigate to: **http://localhost:8080/**

The web interface provides:
- 📝 Intuitive form for entering calculation parameters
- 🔄 Toggle between "by vacation days" or "by date range" calculation modes
- ✅ Frontend validation for better user experience
- 💰 Real-time calculation results with detailed breakdown
- 🎨 Modern, responsive design using Bootstrap 5
- ⚠️ Clear error messages for invalid inputs

**No need for Postman, Swagger, or cURL** - just open your browser and start calculating!

### Features of the Web Interface

- **Average Salary Input**: Enter your average monthly salary for the last 12 months
- **Calculation Mode Selection**: 
  - **By Days**: Simply enter the number of vacation days
  - **By Dates**: Enter start and end dates (holidays are automatically excluded)
- **Instant Results**: See vacation pay amount, payable days, and calculation details
- **Error Handling**: Clear error messages for validation failures

## API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## API Endpoint

### Calculate Vacation Pay

**Endpoint**: `GET /calculate`

**Query Parameters**:

| Parameter      | Type      | Required | Description                                      |
|----------------|-----------|----------|--------------------------------------------------|
| averageSalary  | BigDecimal | Yes      | Average monthly salary for the last 12 months    |
| vacationDays   | Integer   | No*      | Number of vacation days (used if dates not provided) |
| startDate      | LocalDate | No*      | Vacation start date (format: YYYY-MM-DD)         |
| endDate        | LocalDate | No*      | Vacation end date (format: YYYY-MM-DD)           |

*Either `vacationDays` or both `startDate` and `endDate` must be provided (mutually exclusive).

**Response**:

```json
{
  "vacationPay": 95563.16,
  "payableDays": 28,
  "calculationDetails": "Based on 28 vacation days"
}
```

## Example Requests

### Using vacationDays:

```bash
curl "http://localhost:8080/calculate?averageSalary=100000&vacationDays=28"
```

**Response**:
```json
{
  "vacationPay": 95563.16,
  "payableDays": 28,
  "calculationDetails": "Based on 28 vacation days"
}
```

### Using date range:

```bash
curl "http://localhost:8080/calculate?averageSalary=100000&startDate=2024-06-01&endDate=2024-06-28"
```

**Response**:
```json
{
  "vacationPay": 95563.16,
  "payableDays": 28,
  "calculationDetails": "Based on provided dates (2024-06-01 to 2024-06-28), 28 calendar days"
}
```

### Using date range with holidays:

```bash
curl "http://localhost:8080/calculate?averageSalary=100000&startDate=2024-05-01&endDate=2024-05-10"
```

**Response**:
```json
{
  "vacationPay": 27303.76,
  "payableDays": 8,
  "calculationDetails": "Based on provided dates (2024-05-01 to 2024-05-10), 10 calendar days excluding 2 holiday(s)"
}
```

## Business Logic

### Calculation Formula

1. **Average daily earnings** = `averageSalary / 29.3`
   - 29.3 is the official average monthly number of calendar days per Russian Ministry of Labor

2. **Payable vacation days**:
   - If `vacationDays` provided: use that number directly
   - If dates provided: calculate calendar days between `startDate` and `endDate` (inclusive), then subtract official non-working holidays

3. **Vacation pay** = `average daily earnings × payable vacation days`

### Russian Holidays

The service automatically excludes the following Russian non-working holidays:
- January 1-8: New Year holidays
- February 23: Defender of the Fatherland Day
- March 8: International Women's Day
- May 1: Spring and Labor Day
- May 9: Victory Day
- June 12: Russia Day
- November 4: Unity Day

## Error Handling

The API returns meaningful error messages in JSON format:

### Validation Error (400 Bad Request):

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "averageSalary": "Average salary must be greater than 0"
  },
  "timestamp": "2024-01-15T10:30:00",
  "path": "/calculate"
}
```

### Business Logic Error (400 Bad Request):

```json
{
  "status": 400,
  "message": "Either vacationDays or both startDate and endDate must be provided",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/calculate"
}
```

## Running Tests

### Using Maven Wrapper:

```bash
./mvnw test
```

On Windows:
```bash
mvnw.cmd test
```

### Using Maven (if installed):

```bash
mvn test
```

To run tests with coverage report:

```bash
./mvnw test jacoco:report
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/vacationcalculator/
│   │   ├── VacationCalculatorApplication.java
│   │   ├── controller/
│   │   │   └── VacationController.java
│   │   ├── service/
│   │   │   └── VacationPayService.java
│   │   ├── dto/
│   │   │   ├── VacationPayRequest.java
│   │   │   └── VacationPayResponse.java
│   │   ├── model/
│   │   │   └── HolidayCalendar.java
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── InvalidVacationRequestException.java
│   │   │   └── ErrorResponse.java
│   │   └── config/
│   │       └── SwaggerConfig.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/vacationcalculator/
        ├── service/
        │   └── VacationPayServiceTest.java
        └── model/
            └── HolidayCalendarTest.java
```

## Configuration

Application configuration is in `src/main/resources/application.properties`:

```properties
spring.application.name=vacation-calculator
server.port=8080
logging.level.com.example.vacationcalculator=INFO
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

## Code Quality

- Follows SOLID principles
- Comprehensive unit tests (>90% coverage)
- Input validation and error handling
- Clean, readable code with meaningful naming
- JavaDoc documentation
- Uses BigDecimal for precise monetary calculations

## License

This project is licensed under the Apache License 2.0.

## Author

Vacation Calculator Team
