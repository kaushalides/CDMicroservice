# 🏥 Patient Microservice

A Spring Boot microservice for managing patient data with full CRUD operations. Built with MongoDB for data persistence and includes comprehensive testing with unit, integration, and API testing capabilities.

## 🚀 Technologies Used

- Java 17
- Spring Boot
- Spring Data MongoDB
- MongoDB
- Maven
- Docker & Docker Compose
- Testcontainers
- JUnit 5 & Mockito
- Lombok
- Bean Validation

## ✨ Features

### 👤 Patient Management
- Fields:
    - `id` (String)
    - `firstName` (String)
    - `lastName` (String)
    - `dateOfBirth` (LocalDate)
    - `contactNumber` (String)
    - `emailAddress` (String)
    - `gender` (String)

### 🔧 Core Functionality
- **Full CRUD Operations**: Create, Read, Update, Delete patients
- **Data Validation**: Comprehensive input validation with custom error messages
- **Exception Handling**: Global exception handling with structured error responses
- **Health Check**: Service health monitoring endpoint
- **REST Client**: PatientServiceClient for external service communication

### 🧪 Testing
- **Unit Tests**: Service and client layer testing with Mockito
- **Integration Tests**: End-to-end testing with Testcontainers
- **Controller Tests**: MockMvc testing for REST endpoints
- **Docker Integration**: Containerized testing environment

### 🐳 Containerization
- **Docker Support**: Multi-stage Docker builds
- **Docker Compose**: Full stack deployment with MongoDB
- **CI/CD Ready**: GitLab CI pipeline configuration

## 📁 Project Structure

```
CDPatientMicroservice/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── learnjavawithkaushi/
│   │   │           └── cdpatientmicroservice/
│   │   │               ├── client/
│   │   │               │   └── PatientServiceClient.java
│   │   │               ├── config/
│   │   │               │   └── RestTemplateConfig.java
│   │   │               ├── controller/
│   │   │               │   └── PatientController.java
│   │   │               ├── exception/
│   │   │               │   ├── ErrorResponse.java
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── PatientNotFoundException.java
│   │   │               │   └── PatientValidationException.java
│   │   │               ├── model/
│   │   │               │   └── Patient.java
│   │   │               ├── repository/
│   │   │               │   └── PatientRepository.java
│   │   │               ├── service/
│   │   │               │   └── PatientService.java
│   │   │               └── CdPatientMicroserviceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── learnjavawithkaushi/
│                   └── cdpatientmicroservice/
│                       ├── client/
│                       │   └── PatientServiceClientTest.java
│                       ├── controller/
│                       │   └── PatientControllerTest.java
│                       ├── integration/
│                       │   └── PatientIntegrationTest.java
│                       └── service/
│                           └── PatientServiceTest.java
├── docker-compose.yml
├── Dockerfile
├── .gitlab-ci.yml
└── pom.xml
```

## ✅ Prerequisites

- Java 17
- Maven 3.6+
- Docker & Docker Compose
- MongoDB (if running locally without Docker)
- IDE (IntelliJ IDEA)

## ▶️ Running the Application

### 🐳 Using Docker Compose (Recommended)

1. **Clone the Repository**
   ```bash
   git clone https://git01lab.cs.univie.ac.at/vu-advanced-software-engineering/students/2025s/ASE_12413702.git
   cd CDPatientMicroservice
   ```

2. **Start All Services**
   ```bash
   docker-compose up -d
   ```

3. **Access the Application**
   Open your browser and go to:  
   👉 [http://localhost:8080/api/patients/health](http://localhost:8080/api/patients/health)

### 🏃 Running Locally

1. **Start MongoDB**
   ```bash
   docker run -d -p 27017:27017 --name patient-mongo mongo:6.0
   ```

2. **Build the Project**
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

## 🌐 API Endpoints

### Patient Operations
- `GET /api/patients` - Get all patients
- `GET /api/patients/{id}` - Get patient by ID
- `POST /api/patients` - Create new patient
- `PUT /api/patients/{id}` - Update patient
- `DELETE /api/patients/{id}` - Delete patient

### Health Check
- `GET /api/patients/health` - Service health status

### Example Patient JSON
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-01-01",
  "contactNumber": "1234567890",
  "emailAddress": "john.doe@gmail.com",
  "gender": "Male"
}
```

## 🗃️ Database Configuration

### MongoDB Connection
- **URL**: `mongodb://localhost:27017/patientdb`
- **Database**: `patientdb`
- **Collection**: `patients`

### Docker MongoDB Access
Access MongoDB shell when running with Docker:
```bash
docker exec -it patient-mongo mongosh patientdb
```

## 🧪 Testing

The project includes comprehensive testing at multiple levels:

### ✅ Test Types
- **Unit Tests**: Service and client layer testing
- **Integration Tests**: Full application testing with Testcontainers
- **Controller Tests**: REST endpoint testing with MockMvc

### 📊 Test Coverage
- `PatientServiceTest` - Service layer business logic
- `PatientServiceClientTest` - REST client functionality
- `PatientControllerTest` - REST endpoint behavior
- `PatientIntegrationTest` - End-to-end integration testing

### ▶️ Running Tests

**Run All Tests**
```bash
mvn test
```

**Run Specific Test Class**
```bash
mvn test -Dtest=PatientServiceTest
```

**Run Integration Tests**
```bash
mvn test -Dtest=PatientIntegrationTest
```

## 🔧 Configuration

### Application Properties
```properties
# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/patientdb

# Logging Configuration
logging.level.com.learnjavawithkaushi=INFO
logging.level.org.springframework.web=DEBUG

# Server Configuration
server.port=8080
```

### Docker Environment
The application supports different profiles:
- `default` - Local development
- `docker` - Docker container deployment
- `test` - Testing environment

## 🚀 CI/CD Pipeline

The project includes a GitLab CI pipeline with:

### 📋 Stages
1. **Build Stage**
    - Maven build and packaging
    - Docker image creation
    - Artifact generation

2. **Test Stage**
    - Unit test execution
    - Integration test with Docker services
    - API health checks

### ▶️ Pipeline Commands
```bash
# Local pipeline testing
docker run --rm -v "$PWD":/usr/src/app -w /usr/src/app maven:3.8.4-openjdk-17 mvn clean test
```

## 🛠️ Development

### Adding New Features
1. Create/modify model classes in `model/` package
2. Update repository interfaces in `repository/` package
3. Implement business logic in `service/` package
4. Add REST endpoints in `controller/` package
5. Write comprehensive tests for all layers

### Code Quality
- Use Lombok annotations for boilerplate code
- Follow Spring Boot best practices
- Implement proper exception handling
- Add validation annotations to models
- Write meaningful log messages

## 🚧 Future Improvements

- Add Spring Security for authentication and authorization
- Implement caching with Redis
- Add API documentation with OpenAPI/Swagger


## 🐛 Troubleshooting

### Common Issues

**MongoDB Connection Issues**
```bash
# Check if MongoDB is running
docker ps | grep mongo

# Check MongoDB logs
docker logs patient-mongo
```

**Application Startup Issues**
```bash
# Check application logs
docker logs patient-service

# Verify health endpoint
curl http://localhost:8080/api/patients/health
```

## 👩‍💻 Author

**Name**: Hithandura Gedara Kaushali Shanika De Silva  
