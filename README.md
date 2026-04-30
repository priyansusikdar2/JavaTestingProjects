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
| **JMeter** | 5.6.3 | Performance testing |

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
- Right-click `src/test/java`
- Click **Run 'All Tests'**
- Or run individual test classes
- Use **Run with Coverage** for metrics

### **Using Maven**

```bash
# Run all tests
mvn clean test

# Run specific tests
mvn test -Dtest=UserServiceTest
mvn test -Dtest=ProductServiceTest
mvn test -Dtest=UserServiceIntegrationTest
mvn test -Dtest=UserServiceMockTest
```

### **Performance Tests 🚀**
```bash
mvn test -Pperformance

mvn test -Dtest=LoadTest#testConcurrentUserCreation
mvn test -Dtest=LoadTest#testConcurrentProductCreation
mvn test -Dtest=LoadTest#testConcurrentProductLookups

mvn clean test -Pfull-test
mvn test -Pquick
```

### **JMeter Tests 🚀**
```bash
.\run-jmeter-clean.ps1

jmeter -n -t src/test/jmeter/user-load-test.jmx \
-l target/jmeter-results.jtl \
-e -o target/jmeter-report \
-JTHREADS=50 -JRAMP_UP=10 -JLOOPS=5
```

---

## 📈 **PERFORMANCE METRICS**

| Test Scenario | Threads | Operations | Avg Duration | Throughput |
|--------------|--------|-----------|-------------|------------|
| Concurrent User Creation | 100 | 1000 | ~1250ms | 800 users/sec |
| Concurrent Product Creation | 50 | 500 | ~850ms | 588 products/sec |
| Product Lookups | 200 | 10000 | ~2100ms | 4762 ops/sec |

---

## 🔮 **FUTURE IMPROVEMENTS**

### ✅ COMPLETED
- Performance testing suite
- Concurrent testing
- JMeter integration

### 🚧 NEXT
- Spring Boot integration tests
- TestContainers support
- CI/CD with GitHub Actions
- Mutation testing (PITest)
- REST Assured API testing
- Performance monitoring

---

## 📊 **QUICK STATS CARD**

```
Total Tests Written:        121
Tests Passing:              121
Tests Failing:              0
Pass Rate:                  100%
Test Classes:               5
Lines of Test Code:         ~2800
Mock Objects Created:       100+
Assertions Written:         550+
Coverage Achieved:          95%+
Performance Tests:          3
Max Concurrent Users:       1000+
Hours of Learning:          100+
```

---

## 🚀 **RECENT UPGRADES (April 2026)**

### Performance Testing Suite
- Added load testing framework
- 1000 concurrent users testing
- 10,000+ operations testing

### JMeter Integration
- JMeter test plans
- HTML reports
- Java sampler integration

### Coverage Improvements
- Test count: 118 → 121
- Maintained 100% pass rate

---

## 📁 **NEW FILES ADDED**

```
performance/LoadTest.java
jmeter/user-load-test.jmx
UserServiceSampler.java
run-jmeter-clean.ps1
```

---

## 🤝 **CONTRIBUTING**

- Add edge test cases
- Improve performance tests
- Add database testing
- Try distributed load testing

---

## 📄 **LICENSE**

Educational project for learning software testing.

---

<div align="center">

⭐ IF YOU FIND THIS PROJECT USEFUL, PLEASE GIVE IT A STAR! ⭐  

"Every expert was once a beginner."  

Built with ☕ Java | 🧪 JUnit | 🎭 Mockito | 📦 Maven | ⚡ JMeter  

🚀 From Unit → Integration → Mock → Performance Testing  

</div>
