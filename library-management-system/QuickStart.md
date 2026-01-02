# 🚀 Quick Start Guide - Library Management System

## Prerequisites
```bash
# Check Java version (must be 17 or higher)
java -version

# Check Maven version
mvn -version
```

## Installation & Setup

### 1. Clone/Download the Project
```bash
# If using git
git clone <your-repo-url>
cd library-management-system

# Or extract the zip file
unzip library-management-system.zip
cd library-management-system
```

### 2. Build the Project
```bash
# Clean and compile
mvn clean compile

# This will:
# - Download dependencies
# - Compile all Java files
# - Run code validation
```

### 3. Run Tests (Optional but Recommended)
```bash
# Run all tests
mvn test

# You should see output like:
# [INFO] Tests run: 45, Failures: 0, Errors: 0, Skipped: 0
```

### 4. Run the Application

**Option A: Using Maven**
```bash
mvn exec:java -Dexec.mainClass="ir.university.library.Main"
```

**Option B: Create and Run JAR**
```bash
# Create executable JAR
mvn clean package

# Run the JAR
java -jar target/library-management-system-3.0.0.jar
```

## First Time Usage

### Default Accounts

#### Manager Account
```
Username: admin
Password: admin123
```

#### Staff Accounts
```
Username: staff1, staff2, staff3
Password: staff123
```

#### Students
No default students - you need to register!

## Quick Usage Guide

### 1. As a Guest
```
Main Menu → 1. Guest Access
- View registered students count
- Search books by title
- View library statistics
```

### 2. Register as Student
```
Main Menu → 3. Student Registration
- Choose username and password
- Enter your student ID, name, and email
- After registration, login to access features
```

### 3. Login as Student
```
Main Menu → 2. Student Login
- Enter your username and password
- Search for books
- Request book borrows
- View your borrow history
```

### 4. Login as Staff
```
Main Menu → 4. Staff Login
Username: staff1
Password: staff123

As staff you can:
- Register new books
- Edit book information
- Approve borrow requests
- Process book returns
- View student histories
- Activate/deactivate students
```

### 5. Login as Manager
```
Main Menu → 5. Manager Login
Username: admin
Password: admin123

As manager you can:
- Create new staff accounts
- View staff performance reports
- View borrow statistics
- View student statistics
- See top 10 students with delays
```

## Common Operations

### Adding a Book (Staff Only)
```
1. Login as staff
2. Select "2. Register new book"
3. Enter book details:
   - Title: Java Programming
   - Author: John Doe
   - Year: 2024
   - ISBN: ISBN123456789
4. Book is now in the system!
```

### Borrowing a Book (Student)
```
1. Login as student
2. Select "1. Search books"
3. Find your desired book
4. Note the Book ID
5. Select "2. Request borrow"
6. Enter:
   - Book ID
   - Start date (YYYY-MM-DD)
   - End date (YYYY-MM-DD)
7. Wait for staff approval
```

### Approving a Borrow Request (Staff)
```
1. Login as staff
2. Select "4. Review borrow requests"
3. View pending requests
4. Enter Request ID to approve
5. Book is now borrowed
```

### Returning a Book (Staff)
```
1. Login as staff
2. Select "7. Return borrowed book"
3. View active borrows
4. Enter Record ID
5. Book is returned and available again
```

## Testing Workflow

### Complete Test Flow
```bash
# 1. Create a staff account (as manager)
Login as: admin/admin123
Create staff: teststaff/pass123

# 2. Register a student
Main Menu → Student Registration
Username: teststudent
Password: pass123

# 3. Add books (as staff)
Login as: staff1/staff123
Register Book: Test Book / Test Author / 2024 / ISBN123

# 4. Request borrow (as student)
Login as: teststudent/pass123
Search books
Request borrow for Test Book

# 5. Approve request (as staff)
Login as: staff1/staff123
Review pending requests
Approve the request

# 6. View reports (as manager)
Login as: admin/admin123
View staff performance
View borrow statistics
```

## Troubleshooting

### Issue: "Command not found: mvn"
**Solution:** Install Maven
```bash
# macOS
brew install maven

# Ubuntu/Debian
sudo apt-get install maven

# Windows
# Download from: https://maven.apache.org/download.cgi
```

### Issue: "Java version mismatch"
**Solution:** Install Java 17+
```bash
# macOS
brew install openjdk@17

# Ubuntu/Debian
sudo apt-get install openjdk-17-jdk

# Windows
# Download from: https://adoptium.net/
```

### Issue: Tests failing
**Solution:** Clean and rebuild
```bash
mvn clean test
```

### Issue: "Port already in use" (Future REST API)
**Solution:** Not applicable yet - this is CLI only

## Project Structure Quick Reference
```
src/main/java/
├── model/          → Data models (Book, User, etc.)
├── repository/     → Data storage (in-memory)
├── service/        → Business logic
├── ui/            → Console menus
└── util/          → Helper utilities

src/test/java/
├── repository/     → Repository tests
└── service/       → Service tests
```

## Development Commands

```bash
# Compile only
mvn compile

# Run tests only
mvn test

# Clean build directory
mvn clean

# Package as JAR
mvn package

# Run specific test
mvn test -Dtest=AuthServiceTest

# Skip tests during build
mvn package -DskipTests

# Generate test coverage report
mvn test jacoco:report
# View report: target/site/jacoco/index.html
```

## Next Steps

1. ✅ Run the application
2. ✅ Create test accounts
3. ✅ Test all features
4. ✅ Run unit tests
5. ✅ Review API design for future REST API
6. ✅ Read code documentation

## Need Help?

- Check `README.md` for detailed documentation
- Check `API_DESIGN.md` for REST API specifications
- Review test files for usage examples
- Check JavaDoc in source code

## Git Commands for Submission

```bash
# Initialize git (if not done)
git init

# Add all files
git add .

# Commit with correct format
git commit -m "SE1_P_3 Complete implementation with all tests and API design"

# Push to repository
git push origin main
```

Remember: The commit message must start with `SE1_P_3` for grading!

---

Happy coding! 🎉
