package com.testing.demo.performance;

import com.testing.demo.model.Product;
import com.testing.demo.model.User;
import com.testing.demo.repository.ProductRepository;
import com.testing.demo.repository.UserRepository;
import com.testing.demo.service.EmailService;
import com.testing.demo.service.ProductService;
import com.testing.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("performance")
@DisplayName("Performance & Load Tests")
public class LoadTest {

    private UserService userService;
    private ProductService productService;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        productRepository = new ProductRepository();
        emailService = new EmailService();
        userService = new UserService(userRepository, emailService);
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("Load Test: Create 1000 users concurrently")
    void testConcurrentUserCreation() throws InterruptedException {
        int threadCount = 100;
        int usersPerThread = 10;
        int totalUsers = threadCount * usersPerThread;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        Instant start = Instant.now();

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < usersPerThread; j++) {
                        try {
                            userService.createUser(
                                    "user_" + threadNum + "_" + j,
                                    "user" + threadNum + "_" + j + "@test.com",
                                    20 + (j % 50)
                            );
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            failureCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(60, TimeUnit.SECONDS);
        executorService.shutdown();
        Instant end = Instant.now();

        long duration = Duration.between(start, end).toMillis();

        System.out.println("✅ Created " + successCount.get() + " users in " + duration + "ms");

        assertThat(successCount.get()).isEqualTo(totalUsers);
        assertThat(failureCount.get()).isEqualTo(0);
    }

    @Test
    @DisplayName("Load Test: Create 500 products concurrently")
    void testConcurrentProductCreation() throws InterruptedException {
        int threadCount = 50;
        int productsPerThread = 10;
        int totalProducts = threadCount * productsPerThread;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        Instant start = Instant.now();

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < productsPerThread; j++) {
                        try {
                            productService.createProduct(
                                    "Product_" + threadNum + "_" + j,
                                    50.0 + (Math.random() * 100),
                                    10 + (int)(Math.random() * 90)
                            );
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            failureCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(60, TimeUnit.SECONDS);
        executorService.shutdown();
        Instant end = Instant.now();

        long duration = Duration.between(start, end).toMillis();

        System.out.println("✅ Created " + successCount.get() + " products in " + duration + "ms");

        assertThat(successCount.get()).isEqualTo(totalProducts);
        assertThat(failureCount.get()).isEqualTo(0);
    }

    @Test
    @DisplayName("Load Test: Product lookups")
    void testConcurrentProductLookups() throws InterruptedException {
        // First create products
        List<Long> productIds = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Product product = productService.createProduct("Product_" + i, 100.0, 10);
            productIds.add(product.getId());
        }

        int threadCount = 200;
        int lookupsPerThread = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        Instant start = Instant.now();

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < lookupsPerThread; j++) {
                        try {
                            Long productId = productIds.get(threadNum % productIds.size());
                            Product product = productService.getProductById(productId);
                            if (product != null) {
                                successCount.incrementAndGet();
                            } else {
                                failureCount.incrementAndGet();
                            }
                        } catch (Exception e) {
                            failureCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(60, TimeUnit.SECONDS);
        executorService.shutdown();
        Instant end = Instant.now();

        long duration = Duration.between(start, end).toMillis();
        int totalOperations = threadCount * lookupsPerThread;

        System.out.println("✅ Completed " + successCount.get() + " lookups in " + duration + "ms");

        assertThat(successCount.get()).isEqualTo(totalOperations);
        assertThat(failureCount.get()).isEqualTo(0);
    }
}
