# Library Management System - Version 3.0

## 📚 Overview
A comprehensive library management system for universities, built with Java and Maven. This system manages books, student registrations, borrow requests, and generates detailed reports.

## 🏗️ Architecture

### Clean Architecture Pattern
The project follows clean architecture principles with clear separation of concerns:

```
model/          → Domain entities (Book, User, BorrowRequest, etc.)
repository/     → Data access layer (in-memory storage)
service/        → Business logic layer
ui/             → Presentation layer (console menus)
util/           → Utility classes
```

### Key Design Patterns
- **Singleton Pattern**: Repositories use singleton pattern for centralized data management
- **Repository Pattern**: Abstracts data access logic
- **Service Layer Pattern**: Encapsulates business logic
- **Inheritance**: User hierarchy (Student, Staff, Manager extend User)

## 🚀 Features

### Guest Users
- View total registered students
- Search books by title
- View general statistics

### Students
- Register and login
- Search books (by title, author, year)
- Submit borrow requests
- View borrow history

### Staff Members
- Change password
- Register new books
- Edit book information
- Review and approve borrow requests
- View student borrow history
- Activate/deactivate students
- Process book returns

### Manager
- Create staff accounts
- View staff performance reports
- View borrow statistics
- View student statistics
- See top 10 students with most delays

## 🛠️ Technical Stack
- **Language**: Java 17
- **Build Tool**: Maven
- **Testing**: JUnit 5 + Mockito
- **Storage**: In-memory (HashMap-based repositories)

## 📦 Project Structure

```
library-management-system/
├── src/
│   ├── main/java/ir/university/library/
│   │   ├── Main.java
│   │   ├── model/
│   │   │   ├── User.java (abstract)
│   │   │   ├── Student.java
│   │   │   ├── Staff.java
│   │   │   ├── Manager.java
│   │   │   ├── Book.java
│   │   │   ├── BorrowRequest.java
│   │   │   └── BorrowRecord.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── BookRepository.java
│   │   │   └── BorrowRepository.java
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   ├── BookService.java
│   │   │   ├── BorrowService.java
│   │   │   ├── ReportService.java
│   │   │   ├── StudentService.java
│   │   │   └── StaffService.java
│   │   ├── ui/
│   │   │   ├── MenuHandler.java
│   │   │   ├── GuestMenu.java
│   │   │   ├── StudentMenu.java
│   │   │   ├── StaffMenu.java
│   │   │   └── ManagerMenu.java
│   │   └── util/
│   │       ├── ConsoleUtils.java
│   │       ├── DateUtils.java
│   │       └── InputValidator.java
│   └── test/java/ir/university/library/
│       ├── repository/
│       │   ├── UserRepositoryTest.java
│       │   ├── BookRepositoryTest.java
│       │   └── BorrowRepositoryTest.java
│       └── service/
│           ├── AuthServiceTest.java
│           ├── BookServiceTest.java
│           ├── BookSearchTest.java
│           ├── BorrowServiceTest.java
│           ├── BorrowManagementTest.java
│           └── ReportServiceTest.java
├── prompts/
│   └── SE1_P_3_prompts.txt
├── API_DESIGN.md
├── pom.xml
├── .gitignore
└── README.md
```

## 🔧 Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build the Project
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```

### Run the Application
```bash
mvn exec:java -Dexec.mainClass="ir.university.library.Main"
```

### Create JAR
```bash
mvn clean package
java -jar target/library-management-system-3.0.0.jar
```

## 👤 Default Credentials

### Manager
- Username: `admin`
- Password: `admin123`

### Staff Members
- Username: `staff1`, `staff2`, `staff3`
- Password: `staff123`

### Students
Students must register through the application.

## 📊 Testing

The project includes comprehensive unit tests covering:
- Repository operations
- Service layer business logic
- Edge cases and error handling
- Data validation

Run tests with coverage:
```bash
mvn test jacoco:report
```

## 🎯 Key Features Implementation

### Borrow Request Workflow
1. Student searches for available books
2. Student creates borrow request with date range
3. Staff reviews pending requests (for today/yesterday)
4. Staff approves request → Creates borrow record
5. Book marked as unavailable
6. Student returns book
7. Staff processes return
8. Book marked as available

### Statistics and Reports
- Student statistics: total borrows, unreturned books, late returns
- Staff performance: books registered, lent, received
- Borrow statistics: total requests, approvals, average duration
- Top 10 students with most delays

## 🔒 Security Features
- Password-based authentication
- Role-based access control
- Active/inactive student status
- Input validation

## 🧪 Code Quality
- Clean code principles
- SOLID principles
- Comprehensive JavaDoc
- Unit test coverage
- Error handling
- Input validation

## 📝 License
This project is created for educational purposes as part of Software Engineering course.

## 🚀 Future Enhancements (REST API)
The project includes a complete RESTful API design specification in `API_DESIGN.md` for future implementation. This includes:
- Complete CRUD operations for all entities
- Authentication with JWT tokens
- Pagination and filtering
- Advanced search capabilities
- Rate limiting and security features

See `API_DESIGN.md` for detailed API specifications.

## 👨‍💻 Author
Created by: [Your Name]
Course: Software Engineering 1
Version: 3.0.0
Date: December 2025