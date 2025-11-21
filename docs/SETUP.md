# Development Setup Guide

## Prerequisites

### Required Software
- **JDK 17 or higher** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Android Studio** (for Android development) - [Download](https://developer.android.com/studio)
- **Git** - [Download](https://git-scm.com/)
- **Gradle** (bundled with projects)

### Optional Tools
- **PostgreSQL** (for production database) - [Download](https://www.postgresql.org/download/)
- **Postman** (for API testing) - [Download](https://www.postman.com/downloads/)

---

## Clone the Repository

\\\ash
git clone git@github.com-personal:SujwalBoppana/personal-finance-application.git
cd personal-finance-application
\\\

---

## Backend Setup

### 1. Navigate to Backend Directory
\\\ash
cd backend
\\\

### 2. Configure Database (Optional)
By default, the application uses H2 in-memory database. To use PostgreSQL:

Create \ackend/src/main/resources/application-dev.yml\:
\\\yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/finance_db
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
\\\

### 3. Build the Backend
\\\ash
# Windows
.\\gradlew.bat build

# Linux/Mac
./gradlew build
\\\

### 4. Run the Backend
\\\ash
# Windows
.\\gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
\\\

The backend will start on \http://localhost:8080\

### 5. Verify Backend is Running
\\\ash
curl http://localhost:8080/api/health
\\\

---

## Android App Setup

### 1. Open in Android Studio
- Launch Android Studio
- Select **Open an Existing Project**
- Navigate to \personal-finance-application/android\
- Click **Open**

### 2. Sync Gradle Dependencies
Android Studio will automatically prompt to sync Gradle. If not:
- Click **File**  **Sync Project with Gradle Files**

### 3. Configure Local Properties
Ensure \ndroid/local.properties\ has your SDK path:
\\\properties
sdk.dir=C:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
\\\

### 4. Update API Base URL
If running on an emulator, update the base URL in:
\ndroid/app/src/main/java/com/example/finance/network/ApiConfig.kt\

\\\kotlin
object ApiConfig {
    // For Android Emulator
    const val BASE_URL = "http://10.0.2.2:8080/api/"
    
    // For Physical Device (use your computer's IP)
    // const val BASE_URL = "http://192.168.1.100:8080/api/"
}
\\\

### 5. Build the App
\\\ash
# From android directory
# Windows
.\\gradlew.bat assembleDebug

# Linux/Mac
./gradlew assembleDebug
\\\

### 6. Run on Emulator/Device
- Create an AVD (Android Virtual Device) if you don't have one:
  - **Tools**  **Device Manager**  **Create Device**
  - Choose **Pixel 5** or similar
  - Select **API Level 33** or higher
  - Click **Finish**

- Click the **Run** button (green triangle) in Android Studio

---

## Running Both Projects Together

### Terminal 1 - Backend
\\\ash
cd backend
./gradlew bootRun
\\\

### Terminal 2 - Android Emulator
\\\ash
cd android
./gradlew installDebug
# Or use Android Studio Run button
\\\

---

## Testing

### Backend Tests
\\\ash
cd backend
./gradlew test
\\\

### Android Tests
\\\ash
cd android
# Unit tests
./gradlew test

# Instrumented tests (requires emulator/device)
./gradlew connectedAndroidTest
\\\

---

## Common Issues

### Issue: Backend won't start - Port 8080 already in use
**Solution:** Kill the process using port 8080
\\\ash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
\\\

### Issue: Android app can't connect to backend
**Solutions:**
1. Verify backend is running: \curl http://localhost:8080/api/health\
2. Check API URL in app (should be \10.0.2.2:8080\ for emulator)
3. Check network security config in \ndroid/app/src/main/res/xml/network_security_config.xml\

### Issue: Gradle sync failed
**Solutions:**
1. Invalidate caches: **File**  **Invalidate Caches**  **Invalidate and Restart**
2. Clean and rebuild: \./gradlew clean build\
3. Delete \.gradle\ and \uild\ directories, then sync again

### Issue: Android SDK not found
**Solution:** Set SDK path in \local.properties\:
\\\properties
sdk.dir=/path/to/Android/Sdk
\\\

---

## Development Workflow

### 1. Create a Feature Branch
\\\ash
git checkout -b feature/your-feature-name
\\\

### 2. Make Changes
- Follow clean architecture principles
- Write unit tests for new features
- Update API documentation if needed

### 3. Commit Changes
\\\ash
git add .
git commit -m "feat: add your feature description"
\\\

### 4. Push and Create PR
\\\ash
git push origin feature/your-feature-name
\\\

Then create a Pull Request on GitHub.

---

## Code Style

### Kotlin (Android & Backend)
- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use ktlint for formatting
- Maximum line length: 120 characters

### Android Specific
- Use Jetpack Compose for UI
- Follow MVVM architecture
- Use Kotlin Coroutines for async operations

### Backend Specific
- Follow Spring Boot best practices
- Use dependency injection
- Write meaningful API endpoint names

---

## Resources

- [Android Documentation](https://developer.android.com/docs)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)

---

## Support

For issues or questions:
1. Check existing issues on GitHub
2. Create a new issue with detailed description
3. Tag with appropriate labels (bug, enhancement, question)
