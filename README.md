# 🧪 Java Software Testing Mastery Project - My First Testing Journey

![Java](https://img.shields.io/badge/Java-23-orange?style=flat-square&logo=java)
![JUnit 5](https://img.shields.io/badge/JUnit-5.10.2-green?style=flat-square&logo=junit5)
![Mockito](https://img.shields.io/badge/Mockito-5.14.2-brightgreen?style=flat-square)
![AssertJ](https://img.shields.io/badge/AssertJ-3.26.3-blue?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.9.15-red?style=flat-square)
![JMeter](https://img.shields.io/badge/JMeter-5.6.3-orange?style=flat-square&logo=apachejmeter)
![Tests](https://img.shields.io/badge/Tests-121-yellow?style=flat-square)
![Pass Rate](https://img.shields.io/badge/Pass%20Rate-100%25-success?style=flat-square)

---

## 📊 **TEST RESULTS SUMMARY**

| Test Class | Total Tests | Passed | Failed | Pass Rate | Status |
|------------|-------------|--------|--------|-----------|--------|
| **UserServiceTest** | 24 | 24 | 0 | 100% | ✅ ALL PASSING |
| **UserServiceIntegrationTest** | 8 | 8 | 0 | 100% | ✅ ALL PASSING |
| **UserServiceMockTest** | 26 | 26 | 0 | 100% | ✅ ALL PASSING |
| **ProductServiceTest** | 60 | 60 | 0 | 100% | ✅ ALL PASSING |
| **LoadTest (Performance)** | 3 | 3 | 0 | 100% | ✅ ALL PASSING |
| **TOTAL** | **121** | **121** | **0** | **100%** | 🎯 0 TESTS FAILING |

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

### **✅ PERFORMANCE & LOAD TESTS (3 Tests - ALL PASSING) - NEW! 🚀**
- 📊 Concurrent user creation (1000 users simultaneously)
- 📊 Concurrent product creation (500 products simultaneously)
- 🔍 High-volume product lookup testing (10,000+ lookups)
- 📈 Throughput and response time analytics
- ⚡ Stress testing with configurable thread counts
- 🎯 Performance baseline establishment

---

## 🛠️ **TECHNOLOGY STACK**

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 23 | Primary programming language |
| **JUnit 5 (Jupiter)** | 5.10.2 | Testing framework |
| **Mockito** | 5.14.2 | Mocking framework |
| **AssertJ** | 3.26.3 | Fluent assertions |
| **Maven** | 3.9+ | Build automation |
| **JaCoCo** | 0.8.12 | Code coverage |
| **Byte Buddy** | 1.15.11 | Mock generation |
| **JMeter** | 5.6.3 | Performance testing (extensible) |

---

## 📁 **PROJECT STRUCTURE**

```
software-testing-demo/
│
├── src/main/java/com/testing/demo/
│   ├── model/
│   │   ├── User.java
│   │   └── Product.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── ProductService.java
│   │   └── EmailService.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   └── ProductRepository.java
│   └── exception/
│       └── UserNotFoundException.java
│
└── src/test/java/com/testing/demo/
    ├── unit/
    │   ├── UserServiceTest.java
    │   └── ProductServiceTest.java
    ├── integration/
    │   └── UserServiceIntegrationTest.java
    ├── mock/
    │   └── UserServiceMockTest.java
    └── performance/
        └── LoadTest.java
```

---

## 💻 **HOW TO RUN TESTS**

### **Using IntelliJ IDEA**
- Right-click on `src/test/java`
- Click **Run 'All Tests'**
- Or run individual classes
- Use **Run with Coverage** for metrics

---

### **Using Maven Command Line**

#### **Run All Tests**
```bash
mvn clean test
```

#### **Run Specific Tests**
```bash
mvn test -Dtest=UserServiceTest
mvn test -Dtest=ProductServiceTest
mvn test -Dtest=UserServiceIntegrationTest
mvn test -Dtest=UserServiceMockTest
```

---

### **Performance & Load Tests**
```bash
mvn test -Pperformance
mvn test -Dtest=LoadTest#testConcurrentUserCreation
mvn test -Dtest=LoadTest#testConcurrentProductCreation
mvn test -Dtest=LoadTest#testConcurrentProductLookups
mvn clean test -Pfull-test
mvn test -Pquick
```

---

### **Coverage Reports**
```bash
mvn clean test jacoco:report
start target/site/jacoco/index.html
```

---

## 📈 PERFORMANCE METRICS

| Scenario | Threads | Operations | Avg Duration | Throughput |
|----------|--------|-----------|-------------|-----------|
| User Creation | 100 | 1000 | ~1250ms | 800/sec |
| Product Creation | 50 | 500 | ~850ms | 588/sec |
| Lookups | 200 | 10000 | ~2100ms | 4762/sec |

---

## 🔮 FUTURE IMPROVEMENTS

### ✅ Completed
- Performance testing framework
- Concurrent operations testing
- Stress testing

### 🚧 Next
- Spring Boot tests
- TestContainers
- JMeter plans

### 🚀 Long Term
- CI/CD (GitHub Actions)
- Mutation testing (PITest)
- BDD (Cucumber)
- API testing (REST Assured)

---

## 📊 PROJECT STATS

```
Total Tests: 121
Pass Rate: 100%
Coverage: 95%+
Assertions: 550+
Lines of Test Code: ~2800
```

---

## 📄 LICENSE
Educational project for learning software testing.

---

<div align="center">

⭐ Star this repo if it helped you! ⭐

"Every expert was once a beginner."

Built with ☕ Java | 🧪 JUnit | 🎭 Mockito | 📦 Maven | ⚡ JMeter

</div>
