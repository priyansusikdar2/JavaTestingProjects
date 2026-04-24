# 🧪 Java Software Testing Mastery Project - My First Testing Journey

![Java](https://img.shields.io/badge/Java-23-orange?style=flat-square&logo=java)
![JUnit 5](https://img.shields.io/badge/JUnit-5.10.2-green?style=flat-square&logo=junit5)
![Mockito](https://img.shields.io/badge/Mockito-5.14.2-brightgreen?style=flat-square)
![AssertJ](https://img.shields.io/badge/AssertJ-3.25.3-blue?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.9.0-red?style=flat-square)
![Tests](https://img.shields.io/badge/Tests-118-yellow?style=flat-square)
![Pass Rate](https://img.shields.io/badge/Pass%20Rate-98.3%25-success?style=flat-square)

---

## 📊 **TEST RESULTS SUMMARY**

| Test Class | Total Tests | Passed | Failed | Pass Rate | Status |
|------------|-------------|--------|--------|-----------|--------|
| **UserServiceTest** | 24 | 24 | 0 | 100% | ✅ ALL PASSING |
| **UserServiceIntegrationTest** | 8 | 8 | 0 | 100% | ✅ ALL PASSING |
| **UserServiceMockTest** | 26 | 26| 0|100%  |✅ ALL PASSING|
| **ProductServiceTest** | 60 | 60 | 0 | 100%  |  ✅ ALL PASSING|
| **TOTAL** | **118** | **118** | **0** | **100%** | 🎯 0 TESTS FAILING |


---

## 🎯 **WHAT THIS PROJECT TESTS**

### **✅ USER SERVICE (24 Tests - ALL PASSING)**
- ✨ User creation with validation (username, email, age)
- 🔍 User retrieval by ID and listing all users
- 📝 Email updates and user deletion
- 🎂 Adult age verification (age >= 18)
- ⚠️ Error handling for non-existent users
- 📧 Email service integration

### **✅ PRODUCT SERVICE (60 Tests - ALL PASSING)**
- 🏷️ Product creation with name, price, quantity validation
- 🔎 Product lookup by ID with error handling
- 📦 Inventory total value calculation
- 💰 Price range filtering (min/max price search)
- 🔄 Quantity updates with validation
- 🧮 Floating-point precision handling
- 🔒 Concurrent update handling

### **✅ INTEGRATION TESTS (8 Tests - ALL PASSING)**
- 🔗 End-to-end user workflows
- 💾 Real data persistence verification
- 🔄 Concurrent user operations (thread safety)
- 📊 Data integrity across multiple operations
- ⚡ Sequential update verification

### **✅ MOCK TESTS (26 Tests - ALL PASSING)**
- 📸 Argument capturing and verification
- 🔢 Call count verification (times, never, atLeast)
- 📋 Method call order verification
- 🎭 Spy operations for partial mocking
- ⚡ Dynamic stubbing with thenAnswer
- 🔄 Mock reset operations

---

## 🛠️ **TECHNOLOGY STACK**

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 23 | Primary programming language |
| **JUnit 5 (Jupiter)** | 5.10.2 | Testing framework |
| **Mockito** | 5.14.2 | Mocking framework |
| **AssertJ** | 3.25.3 | Fluent assertions |
| **Maven** | 3.9+ | Build automation |
| **JaCoCo** | 0.8.12 | Code coverage |
| **Byte Buddy** | 1.15.11 | Mock generation |

---

## 📁 **PROJECT STRUCTURE**

```
software-testing-demo/
│
├── src/main/java/com/testing/demo/
│   ├── model/
│   │   ├── User.java           # User entity
│   │   └── Product.java        # Product entity with calculations
│   ├── service/
│   │   ├── UserService.java    # User business logic (24 tests)
│   │   ├── ProductService.java # Product business logic (60 tests)
│   │   └── EmailService.java   # Email simulation
│   ├── repository/
│   │   ├── UserRepository.java # In-memory user storage
│   │   └── ProductRepository.java # In-memory product storage
│   └── exception/
│       └── UserNotFoundException.java
│
└── src/test/java/com/testing/demo/
    ├── unit/
    │   ├── UserServiceTest.java      # 24 ✅ Unit tests (ALL PASSING)
    │   └── ProductServiceTest.java   # 60 ✅ Unit tests (ALL PASSING)
    ├── integration/
    │   └── UserServiceIntegrationTest.java # 8 ✅ Integration tests (ALL PASSING)
    └── mock/
        └── UserServiceMockTest.java  # 26 ✅ Mock tests (ALL PASSING)
```

---

## 💻 **HOW TO RUN TESTS**

### **Using IntelliJ IDEA**
```
1. Right-click on src/test/java folder
2. Select "Run 'All Tests'"
3. OR right-click individual test class
4. Select "Run with Coverage" for detailed metrics
```

### **Using Maven Command Line**
```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=UserServiceTest
mvn test -Dtest=ProductServiceTest
mvn test -Dtest=UserServiceIntegrationTest
mvn test -Dtest=UserServiceMockTest

# Generate coverage report
mvn clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html   # macOS
start target/site/jacoco/index.html  # Windows
```

---

## 🔮 **FUTURE IMPROVEMENTS**

### **Medium Term (Next Month)**
1. Add Spring Boot integration tests
2. Implement TestContainers for database testing
3. Add performance/load tests with JMeter

### **Long Term (Next Quarter)**
4. Configure GitHub Actions CI/CD pipeline
5. Add mutation testing with PITest
6. Create BDD tests with Cucumber
7. Add API contract testing with REST Assured

---

## 📊 **QUICK STATS CARD**

```
┌──────────────────────────────────────────┐
│          PROJECT STATISTICS              │
├──────────────────────────────────────────┤
│ Total Tests Written:        118          │
│ Tests Passing:              118          │
│ Tests Failing:              0            │
│ Overall Pass Rate:          100%        │
│ Test Classes:               4            │
│ Lines of Test Code:         ~2500        │
│ Mock Objects Created:       100+         │
│ Assertions Written:         500+         │
│ Coverage Achieved:          95%+         │
│ Hours of Learning:          100+         │
└──────────────────────────────────────────┘
```

---

---

## 📄 **LICENSE**

This project is for **educational purposes** as a learning resource for software testing.

---

<div align="center">

### ⭐ **IF YOU FIND THIS PROJECT USEFUL, PLEASE GIVE IT A STAR!** ⭐

**This is my first testing project - 118 tests, countless lessons, and a journey into quality assurance**

*"Every expert was once a beginner. These 118 tests are just the beginning!"*

---

**Built with ☕ Java, 🧪 JUnit, 📦 Maven, 🎭 Mockito, and a passion for quality code**

</div>
