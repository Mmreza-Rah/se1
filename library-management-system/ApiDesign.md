# Library Management System - RESTful API Design

## Base URL
```
/api
```

## Authentication
All authenticated endpoints require a valid session token or JWT token in the `Authorization` header:
```
Authorization: Bearer <token>
```

---

## 1. Authentication APIs

### 1.1 Student Registration
**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "username": "string (required, 3-20 chars)",
  "password": "string (required, min 6 chars)",
  "studentId": "string (required)",
  "fullName": "string (required)",
  "email": "string (required, valid email)"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "username": "student1",
    "studentId": "S001",
    "fullName": "John Doe",
    "email": "john@example.com",
    "active": true,
    "createdAt": "2024-12-30T10:00:00Z"
  }
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Username already exists",
  "errors": [
    {
      "field": "username",
      "message": "This username is already taken"
    }
  ]
}
```

### 1.2 Login
**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "string (required)",
  "password": "string (required)"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userType": "STUDENT",
    "username": "student1",
    "fullName": "John Doe"
  }
}
```

### 1.3 Change Password
**Endpoint:** `POST /api/auth/change-password`

**Headers:** `Authorization: Bearer <token>` (Required)

**Request Body:**
```json
{
  "oldPassword": "string (required)",
  "newPassword": "string (required, min 6 chars)"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Password changed successfully"
}
```

---

## 2. Books APIs

### 2.1 Get Books (with Search and Filters)
**Endpoint:** `GET /api/books`

**Query Parameters:**
- `title` (optional): Search by title (partial match, case insensitive)
- `author` (optional): Filter by author (partial match)
- `year` (optional): Filter by publication year
- `available` (optional): Filter by availability (true/false)
- `page` (optional, default: 1): Page number
- `limit` (optional, default: 20): Items per page

**Example Request:**
```
GET /api/books?title=Java&year=2020&available=true&page=1&limit=10
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "books": [
      {
        "bookId": "abc123",
        "title": "Java Programming",
        "author": "John Doe",
        "publicationYear": 2020,
        "isbn": "ISBN123456",
        "available": true,
        "registeredAt": "2024-01-15T09:30:00Z",
        "registeredBy": "staff1"
      }
    ],
    "pagination": {
      "currentPage": 1,
      "totalPages": 5,
      "totalItems": 48,
      "itemsPerPage": 10
    }
  }
}
```

### 2.2 Get Book Details
**Endpoint:** `GET /api/books/{id}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "bookId": "abc123",
    "title": "Java Programming",
    "author": "John Doe",
    "publicationYear": 2020,
    "isbn": "ISBN123456",
    "available": true,
    "registeredAt": "2024-01-15T09:30:00Z",
    "registeredBy": "staff1"
  }
}
```

**Error Response (404 Not Found):**
```json
{
  "success": false,
  "message": "Book not found"
}
```

### 2.3 Create Book
**Endpoint:** `POST /api/books`

**Headers:** `Authorization: Bearer <token>` (Staff only)

**Request Body:**
```json
{
  "title": "string (required)",
  "author": "string (required)",
  "publicationYear": "integer (required)",
  "isbn": "string (required)"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Book registered successfully",
  "data": {
    "bookId": "def456",
    "title": "Advanced Python",
    "author": "Jane Smith",
    "publicationYear": 2024,
    "isbn": "ISBN789012",
    "available": true,
    "registeredAt": "2024-12-30T10:15:00Z",
    "registeredBy": "staff1"
  }
}
```

### 2.4 Update Book
**Endpoint:** `PUT /api/books/{id}`

**Headers:** `Authorization: Bearer <token>` (Staff only)

**Request Body:**
```json
{
  "title": "string (optional)",
  "author": "string (optional)",
  "publicationYear": "integer (optional)",
  "isbn": "string (optional)"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Book updated successfully",
  "data": {
    "bookId": "abc123",
    "title": "Java Programming - 2nd Edition",
    "author": "John Doe",
    "publicationYear": 2021,
    "isbn": "ISBN123456",
    "available": true
  }
}
```

### 2.5 Advanced Book Search
**Endpoint:** `GET /api/books/search`

**Query Parameters:**
- `q` (required): Search query
- `searchFields` (optional): Comma-separated fields (title,author)
- `filters` (optional): JSON string of filters

**Example:**
```
GET /api/books/search?q=programming&searchFields=title,author&filters={"year":2020}
```

**Response:** Same format as Get Books endpoint

---

## 3. Borrowing APIs

### 3.1 Create Borrow Request
**Endpoint:** `POST /api/borrow/request`

**Headers:** `Authorization: Bearer <token>` (Student only)

**Request Body:**
```json
{
  "bookId": "string (required)",
  "startDate": "string (required, format: YYYY-MM-DD)",
  "endDate": "string (required, format: YYYY-MM-DD)"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Borrow request created successfully",
  "data": {
    "requestId": "req123",
    "studentUsername": "student1",
    "bookId": "abc123",
    "bookTitle": "Java Programming",
    "startDate": "2024-12-31",
    "endDate": "2025-01-07",
    "status": "PENDING",
    "requestedAt": "2024-12-30T10:30:00Z"
  }
}
```

### 3.2 Get Pending Requests
**Endpoint:** `GET /api/borrow/requests/pending`

**Headers:** `Authorization: Bearer <token>` (Staff only)

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "requests": [
      {
        "requestId": "req123",
        "studentUsername": "student1",
        "studentName": "John Doe",
        "bookId": "abc123",
        "bookTitle": "Java Programming",
        "startDate": "2024-12-31",
        "endDate": "2025-01-07",
        "status": "PENDING",
        "requestedAt": "2024-12-30T10:30:00Z"
      }
    ],
    "totalCount": 5
  }
}
```

### 3.3 Approve Borrow Request
**Endpoint:** `PUT /api/borrow/requests/{id}/approve`

**Alternative:** `POST /api/borrow/requests/{id}/approve`

**Headers:** `Authorization: Bearer <token>` (Staff only)

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Borrow request approved successfully",
  "data": {
    "requestId": "req123",
    "status": "APPROVED",
    "approvedBy": "staff1",
    "approvedAt": "2024-12-30T11:00:00Z",
    "borrowRecordId": "bor456"
  }
}
```

### 3.4 Reject Borrow Request
**Endpoint:** `PUT /api/borrow/requests/{id}/reject`

**Alternative:** `POST /api/borrow/requests/{id}/reject`

**Headers:** `Authorization: Bearer <token>` (Staff only)

**Request Body (Optional):**
```json
{
  "reason": "string (optional)"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Borrow request rejected",
  "data": {
    "requestId": "req123",
    "status": "REJECTED",
    "rejectedBy": "staff1",
    "rejectedAt": "2024-12-30T11:00:00Z"
  }
}
```

### 3.5 Return Book
**Endpoint:** `PUT /api/borrow/{id}/return`

**Alternative:** `POST /api/borrow/{id}/return`

**Headers:** `Authorization: Bearer <token>` (Staff only)

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Book returned successfully",
  "data": {
    "recordId": "bor456",
    "bookId": "abc123",
    "studentUsername": "student1",
    "returnedAt": "2025-01-05T14:30:00Z",
    "wasLate": false,
    "delayDays": 0
  }
}
```

---

## 4. Student Management APIs

### 4.1 Get Student Profile
**Endpoint:** `GET /api/students/{id}`

**Headers:** `Authorization: Bearer <token>` (Staff/Manager or own profile)

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "username": "student1",
    "studentId": "S001",
    "fullName": "John Doe",
    "email": "john@example.com",
    "active": true,
    "createdAt": "2024-01-10T08:00:00Z"
  }
}
```

### 4.2 Update Student Status
**Endpoint:** `PUT /api/students/{id}/status`

**Alternative:** `POST /api/students/{id}/status`

**Headers:** `Authorization: Bearer <token>` (Staff only)

**Request Body:**
```json
{
  "active": "boolean (required)"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Student status updated successfully",
  "data": {
    "username": "student1",
    "active": false
  }
}
```

### 4.3 Get Student Borrow History
**Endpoint:** `GET /api/students/{id}/borrow-history`

**Headers:** `Authorization: Bearer <token>` (Staff or own history)

**Query Parameters:**
- `status` (optional): Filter by status (active, returned, late)
- `page` (optional): Page number
- `limit` (optional): Items per page

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "history": [
      {
        "recordId": "bor456",
        "bookId": "abc123",
        "bookTitle": "Java Programming",
        "startDate": "2024-12-20",
        "expectedReturnDate": "2024-12-27",
        "actualReturnDate": "2024-12-26T15:00:00Z",
        "status": "RETURNED",
        "wasLate": false,
        "lentBy": "staff1",
        "receivedBy": "staff2"
      }
    ],
    "statistics": {
      "totalBorrows": 15,
      "notReturned": 2,
      "lateReturns": 3
    },
    "pagination": {
      "currentPage": 1,
      "totalPages": 2,
      "totalItems": 15
    }
  }
}
```

---

## 5. Reports and Statistics APIs

### 5.1 Get Summary Statistics
**Endpoint:** `GET /api/stats/summary`

**Headers:** `Authorization: Bearer <token>` (Optional for guests)

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "totalStudents": 120,
    "totalBooks": 450,
    "totalBorrows": 890,
    "activeBorrows": 45
  }
}
```

### 5.2 Get Borrow Statistics
**Endpoint:** `GET /api/stats/borrows`

**Headers:** `Authorization: Bearer <token>` (Manager only)

**Query Parameters:**
- `startDate` (optional): Start date for stats
- `endDate` (optional): End date for stats

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "totalRequests": 200,
    "approvedBorrows": 180,
    "rejectedBorrows": 20,
    "averageBorrowDuration": 6.5,
    "onTimeReturns": 150,
    "lateReturns": 30
  }
}
```

### 5.3 Get Staff Performance
**Endpoint:** `GET /api/stats/employees/{id}/performance`

**Headers:** `Authorization: Bearer <token>` (Manager only)

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "username": "staff1",
    "fullName": "Jane Staff",
    "booksRegistered": 45,
    "booksLent": 120,
    "booksReceived": 115,
    "period": {
      "startDate": "2024-01-01",
      "endDate": "2024-12-30"
    }
  }
}
```

### 5.4 Get Top Delayed Students
**Endpoint:** `GET /api/stats/top-delayed`

**Headers:** `Authorization: Bearer <token>` (Manager only)

**Query Parameters:**
- `limit` (optional, default: 10): Number of students to return

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "students": [
      {
        "username": "student5",
        "fullName": "Alice Johnson",
        "totalDelayDays": 25,
        "lateReturns": 5
      },
      {
        "username": "student12",
        "fullName": "Bob Smith",
        "totalDelayDays": 18,
        "lateReturns": 3
      }
    ]
  }
}
```

---

## 6. Employee Management APIs

### 6.1 Create Employee
**Endpoint:** `POST /api/admin/employees`

**Headers:** `Authorization: Bearer <token>` (Manager only)

**Request Body:**
```json
{
  "username": "string (required)",
  "password": "string (required)",
  "staffId": "string (required)",
  "fullName": "string (required)"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Staff account created successfully",
  "data": {
    "username": "staff4",
    "staffId": "ST004",
    "fullName": "New Staff Member",
    "active": true,
    "createdAt": "2024-12-30T12:00:00Z"
  }
}
```

### 6.2 Get All Employees
**Endpoint:** `GET /api/admin/employees`

**Headers:** `Authorization: Bearer <token>` (Manager only)

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "employees": [
      {
        "username": "staff1",
        "staffId": "ST001",
        "fullName": "Jane Staff",
        "active": true,
        "performance": {
          "booksRegistered": 45,
          "booksLent": 120,
          "booksReceived": 115
        }
      }
    ],
    "totalCount": 3
  }
}
```

---

## Common Response Codes

- `200 OK`: Request successful
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Authentication required
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource conflict (e.g., duplicate username)
- `422 Unprocessable Entity`: Validation error
- `500 Internal Server Error`: Server error

## Error Response Format

```json
{
  "success": false,
  "message": "Error description",
  "errors": [
    {
      "field": "fieldName",
      "message": "Specific error message"
    }
  ],
  "timestamp": "2024-12-30T12:00:00Z"
}
```

## Notes

1. All dates are in ISO 8601 format
2. All timestamps are in UTC
3. Pagination uses 1-based indexing
4. Search is case-insensitive by default
5. Rate limiting: 100 requests per minute per user
6. API versioning can be done via URL (`/api/v1/`) or header (`API-Version: 1`)
