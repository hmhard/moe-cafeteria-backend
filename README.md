# MOE Cafeteria Management System - Backend

A Spring Boot backend API for the Ministry of Education (MOE) cafeteria management system with MySQL database support.

## Features

- **Employee Management**: CRUD operations for employees with card ID and short code support
- **Meal Types & Categories**: Manage meal types (breakfast, lunch) with fasting/non-fasting categories
- **Support Pricing**: Automatic pricing based on employee salary eligibility
- **Meal Recording**: Track meal transactions with support calculations
- **User Authentication**: Role-based access control (Admin, Manager, Operator)
- **RESTful API**: Complete REST endpoints for all operations
- **API Documentation**: Interactive Swagger UI for API exploration and testing

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Security**
- **Spring Data JPA**
- **MySQL Database**
- **Swagger UI / OpenAPI 3**
- **Lombok**
- **Gradle**

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Gradle (or use the included wrapper)

## Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE moe_cafeteria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Update database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/moe_cafeteria?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

### Using Gradle Wrapper
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

### Using IDE
Run the `MoeCateteriaBackendApplication` class directly from your IDE.

## API Documentation

### Swagger UI
Once the application is running, you can access the interactive API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

The Swagger UI provides:
- Interactive API documentation
- Request/response examples
- Authentication support
- Try-it-out functionality
- Schema definitions

### API Overview
The API is organized into the following sections in Swagger UI:

1. **Meal Types** - Manage meal types (breakfast, lunch, etc.)
2. **Meal Categories** - Manage meal categories with pricing
3. **Employees** - Employee management and support eligibility
4. **Meal Records** - Transaction recording and reporting
5. **Support Configuration** - Support pricing configuration

## Authentication

The application uses basic authentication. Default users are created on startup:

- **Admin**: `admin` / `admin123`
- **Manager**: `manager` / `manager123`
- **Operator**: `operator` / `operator123`

### Public Endpoints (No Authentication Required)
- `GET /api/meal-types/enabled` - Get enabled meal types
- `GET /api/meal-categories/enabled` - Get enabled meal categories
- `GET /api/meal-categories/by-type/{mealTypeId}` - Get categories by meal type
- `GET /api/employees/by-card/{cardId}` - Get employee by card ID
- `GET /api/employees/by-code/{shortCode}` - Get employee by short code
- `POST /api/meal-records/record` - Record a meal transaction
- `GET /api/support-config` - Get support configuration

### Protected Endpoints (Authentication Required)

#### Meal Types
- `GET /api/meal-types` - Get all meal types
- `GET /api/meal-types/{id}` - Get meal type by ID
- `POST /api/meal-types` - Create meal type
- `PUT /api/meal-types/{id}` - Update meal type
- `PATCH /api/meal-types/{id}/toggle` - Toggle meal type enabled status
- `DELETE /api/meal-types/{id}` - Delete meal type

#### Meal Categories
- `GET /api/meal-categories` - Get all meal categories
- `GET /api/meal-categories/{id}` - Get meal category by ID
- `POST /api/meal-categories` - Create meal category
- `PUT /api/meal-categories/{id}` - Update meal category
- `PATCH /api/meal-categories/{id}/toggle` - Toggle category enabled status
- `DELETE /api/meal-categories/{id}` - Delete meal category

#### Employees
- `GET /api/employees` - Get all active employees
- `GET /api/employees/{id}` - Get employee by ID
- `GET /api/employees/department/{department}` - Get employees by department
- `GET /api/employees/support-eligible` - Get employees eligible for support
- `POST /api/employees` - Create employee
- `PUT /api/employees/{id}` - Update employee
- `PATCH /api/employees/{id}/toggle` - Toggle employee status
- `DELETE /api/employees/{id}` - Delete employee

#### Meal Records
- `GET /api/meal-records` - Get all meal records
- `GET /api/meal-records/{id}` - Get meal record by ID
- `GET /api/meal-records/employee/{employeeId}` - Get records by employee
- `GET /api/meal-records/date-range?start=...&end=...` - Get records by date range
- `GET /api/meal-records/department/{department}/date-range?start=...&end=...` - Get records by department and date range

#### Support Configuration
- `POST /api/support-config` - Create support configuration
- `PUT /api/support-config/max-salary?maxSalary=...` - Update max salary for support

## Sample Data

The application automatically initializes with sample data on first run:

### Meal Types
- Breakfast (ቁርስ)
- Lunch (ምሳ)

### Meal Categories
- Breakfast - Fasting (ቁርስ - ጾም): Normal 30 ETB, Supported 20 ETB
- Breakfast - Non-Fasting (ቁርስ - የፍስግ): Normal 40 ETB, Supported 30 ETB
- Lunch - Fasting (ምሳ - ጾም): Normal 50 ETB, Supported 40 ETB
- Lunch - Non-Fasting (ምሳ - የፍስግ): Normal 60 ETB, Supported 50 ETB

### Sample Employees
- 5 employees with different departments and salary levels
- Card IDs and short codes for testing

### Support Configuration
- Maximum salary for support: 5,000 ETB

## Support Pricing Logic

Employees with salary below the configured threshold (default: 5,000 ETB) are eligible for supported pricing:
- **Supported Price**: Reduced price for eligible employees
- **Normal Price**: Full price for non-eligible employees
- **Support Amount**: Difference between normal and supported prices

## CORS Configuration

The application is configured to allow requests from `http://localhost:3000` for frontend integration.

## Database Schema

The application uses JPA/Hibernate with automatic schema generation. Key entities:

- **Users**: Authentication and authorization
- **Employees**: Employee information with salary and support eligibility
- **MealTypes**: Base meal types (breakfast, lunch)
- **MealCategories**: Fasting/non-fasting variants with pricing
- **MealRecords**: Transaction records with support calculations
- **SupportConfig**: Configuration for support eligibility

## Development

### Project Structure
```
src/main/java/et/moe/ethernet/cateteria/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data transfer objects
├── entity/         # JPA entities
├── repository/     # Data access repositories
└── service/        # Business logic services
```

### Adding New Features
1. Create entity classes in `entity/` package
2. Create repository interfaces in `repository/` package
3. Create service classes in `service/` package
4. Create DTOs in `dto/` package
5. Create controllers in `controller/` package
6. Add OpenAPI annotations for Swagger documentation
7. Update security configuration if needed

### API Documentation
The project uses OpenAPI 3 annotations for comprehensive API documentation:

- `@Tag` - Group related endpoints
- `@Operation` - Describe endpoint functionality
- `@Parameter` - Document parameters
- `@ApiResponses` - Document response codes
- `@Schema` - Define data models
- `@ExampleObject` - Provide request/response examples

## Testing the API

### Using Swagger UI
1. Start the application
2. Navigate to http://localhost:8080/swagger-ui.html
3. Click "Authorize" and enter credentials (e.g., admin/admin123)
4. Use the "Try it out" button to test endpoints

### Using curl
```bash
# Get enabled meal types (no auth required)
curl -X GET "http://localhost:8080/api/meal-types/enabled"

# Get all employees (auth required)
curl -X GET "http://localhost:8080/api/employees" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="

# Record a meal (no auth required)
curl -X POST "http://localhost:8080/api/meal-records/record?cardId=04A2B3C4D5&mealCategoryId=456e7890-e89b-12d3-a456-426614174000"
```

## Troubleshooting

### Common Issues

1. **Database Connection**: Ensure MySQL is running and credentials are correct
2. **Port Conflicts**: Default port is 8080, change in `application.properties` if needed
3. **CORS Issues**: Verify frontend URL is correctly configured
4. **Authentication**: Use correct username/password for protected endpoints
5. **Swagger UI Access**: Ensure the application is running and accessible

### Logs
Enable debug logging by setting in `application.properties`:
```properties
logging.level.et.moe.ethernet.cateteria=DEBUG
```

## License

This project is developed for the Ministry of Education, Ethiopia. 