# Personal Finance Application

A full-stack personal finance management application built with Android (Kotlin) and Spring Boot.

## Project Structure

`
personal-finance-application/
 android/          # Android mobile application (Kotlin + Jetpack Compose)
 backend/          # Spring Boot REST API backend
`

## Components

### Android App
- **Technology**: Kotlin, Jetpack Compose, MVVM Architecture
- **Features**: 
  - User authentication
  - Transaction management
  - Budget tracking
  - Analytics and insights
  - Category management
  
### Backend API
- **Technology**: Spring Boot, Kotlin
- **Features**:
  - RESTful API
  - JWT-based authentication
  - PostgreSQL/H2 database
  - Transaction processing
  - Budget calculations

## Getting Started

### Prerequisites
- JDK 17 or higher
- Android Studio (for Android development)
- Gradle

### Running the Backend
`ash
cd backend
./gradlew bootRun
`

The API will be available at http://localhost:8080

### Running the Android App
1. Open the ndroid directory in Android Studio
2. Sync Gradle dependencies
3. Run the application on an emulator or physical device

## API Documentation
The backend API runs on port 8080 by default. Key endpoints:
- /api/auth/login - User authentication
- /api/transactions - Transaction management
- /api/budgets - Budget operations
- /api/categories - Category management

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
Private repository - All rights reserved
