package com.testing.demo.unit;

import com.testing.demo.model.Product;
import com.testing.demo.repository.ProductRepository;
import com.testing.demo.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("Product Service Comprehensive Unit Tests")
class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    // ==================== PRODUCT CREATION TESTS ====================

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProductSuccessfully() {
        Product savedProduct = new Product(1L, "Laptop", 999.99, 10);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct("Laptop", 999.99, 10);

        assertThat(result)
                .isNotNull()
                .satisfies(product -> {
                    assertThat(product.getName()).isEqualTo("Laptop");
                    assertThat(product.getPrice()).isEqualTo(999.99);
                    assertThat(product.getQuantity()).isEqualTo(10);
                    assertThat(product.getTotalValue()).isEqualTo(9999.9);
                });

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when product name is empty")
    void shouldThrowExceptionWhenProductNameIsEmpty() {
        assertThatThrownBy(() -> productService.createProduct("", 100.0, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name cannot be empty");

        assertThatThrownBy(() -> productService.createProduct("   ", 100.0, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name cannot be empty");

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when product name is null")
    void shouldThrowExceptionWhenProductNameIsNull() {
        assertThatThrownBy(() -> productService.createProduct(null, 100.0, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name cannot be empty");
    }

    @ParameterizedTest
    @ValueSource(doubles = {-10.0, 0.0, -0.01, -100.0})
    @DisplayName("Should throw exception when price is invalid")
    void shouldThrowExceptionWhenPriceIsInvalid(double invalidPrice) {
        assertThatThrownBy(() -> productService.createProduct("Mouse", invalidPrice, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Price must be positive");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -10, -100})
    @DisplayName("Should throw exception when quantity is negative")
    void shouldThrowExceptionWhenQuantityIsNegative(int invalidQuantity) {
        assertThatThrownBy(() -> productService.createProduct("Mouse", 25.0, invalidQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity cannot be negative");
    }

    @Test
    @DisplayName("Should handle product with zero quantity")
    void shouldHandleProductWithZeroQuantity() {
        Product savedProduct = new Product(1L, "OutOfStock", 50.0, 0);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct("OutOfStock", 50.0, 0);

        assertThat(result).isNotNull();
        assertThat(result.getQuantity()).isZero();
        assertThat(result.getTotalValue()).isZero();

        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should handle product with decimal price")
    void shouldHandleProductWithDecimalPrice() {
        Product savedProduct = new Product(1L, "Precision Item", 99.99, 3);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct("Precision Item", 99.99, 3);

        assertThat(result.getPrice()).isEqualTo(99.99);
        assertThat(result.getTotalValue()).isCloseTo(299.97, within(0.001));
    }

    @Test
    @DisplayName("Should create product with maximum allowed values")
    void shouldCreateProductWithMaximumAllowedValues() {
        Product savedProduct = new Product(1L, "Max Product", Double.MAX_VALUE, Integer.MAX_VALUE);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct("Max Product", Double.MAX_VALUE, Integer.MAX_VALUE);

        assertThat(result.getPrice()).isEqualTo(Double.MAX_VALUE);
        assertThat(result.getQuantity()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should capture created product object")
    void shouldCaptureCreatedProductObject() {
        Product savedProduct = new Product(1L, "Captured", 100.0, 5);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        productService.createProduct("Captured", 100.0, 5);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());

        Product capturedProduct = productCaptor.getValue();
        assertThat(capturedProduct.getName()).isEqualTo("Captured");
        assertThat(capturedProduct.getPrice()).isEqualTo(100.0);
        assertThat(capturedProduct.getQuantity()).isEqualTo(5);
        assertThat(capturedProduct.getId()).isNull();
    }

    @Test
    @DisplayName("Should handle repository save exception")
    void shouldHandleRepositorySaveException() {
        when(productRepository.save(any(Product.class)))
                .thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> productService.createProduct("Test", 100.0, 5))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
    }

    // ==================== PRODUCT RETRIEVAL TESTS ====================

    @Test
    @DisplayName("Should get product by id successfully")
    void shouldGetProductById() {
        Product expectedProduct = new Product(1L, "Keyboard", 49.99, 20);
        when(productRepository.findById(1L)).thenReturn(Optional.of(expectedProduct));

        Product result = productService.getProductById(1L);

        assertThat(result)
                .isNotNull()
                .isEqualTo(expectedProduct)
                .matches(product -> product.getPrice() == 49.99)
                .matches(product -> product.getQuantity() == 20);

        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when product not found")
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found with id: 999");

        verify(productRepository).findById(999L);
    }

    @Test
    @DisplayName("Should throw exception when product id is invalid")
    void shouldThrowExceptionWhenProductIdIsInvalid() {
        assertThatThrownBy(() -> productService.getProductById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid product id");

        assertThatThrownBy(() -> productService.getProductById(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid product id");

        assertThatThrownBy(() -> productService.getProductById(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid product id");
    }

    @Test
    @DisplayName("Should get all products")
    void shouldGetAllProducts() {
        List<Product> expectedProducts = Arrays.asList(
                new Product(1L, "Product1", 10.0, 5),
                new Product(2L, "Product2", 20.0, 3)
        );
        when(productRepository.findAll()).thenReturn(expectedProducts);

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expectedProducts);
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void shouldReturnEmptyListWhenNoProductsExist() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<Product> result = productService.getAllProducts();

        assertThat(result).isEmpty();
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Should verify findById called with correct argument")
    void shouldVerifyFindByIdCalledWithCorrectArgument() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product(1L, "Test", 10.0, 1)));

        productService.getProductById(1L);

        verify(productRepository).findById(eq(1L));
    }

    @Test
    @DisplayName("Should capture ID argument in findById")
    void shouldCaptureIdArgumentInFindById() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            productService.getProductById(123L);
        } catch (RuntimeException e) {
            // Expected
        }

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(productRepository).findById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(123L);
    }

    @Test
    @DisplayName("Should handle repository findById exception")
    void shouldHandleRepositoryFindByIdException() {
        when(productRepository.findById(1L)).thenThrow(new RuntimeException("Repository error"));

        assertThatThrownBy(() -> productService.getProductById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Repository error");
    }

    // ==================== INVENTORY VALUE TESTS ====================

    @Test
    @DisplayName("Should calculate total inventory value correctly")
    void shouldCalculateTotalInventoryValue() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product1", 100.0, 2),
                new Product(2L, "Product2", 50.0, 3),
                new Product(3L, "Product3", 25.0, 4),
                new Product(4L, "Product4", 10.0, 10)
        );
        when(productRepository.findAll()).thenReturn(products);

        double totalValue = productService.getTotalInventoryValue();

        assertThat(totalValue).isEqualTo(550.0);
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Should return zero for empty inventory")
    void shouldReturnZeroForEmptyInventory() {
        when(productRepository.findAll()).thenReturn(List.of());

        double totalValue = productService.getTotalInventoryValue();

        assertThat(totalValue).isZero();
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Should calculate total value for single product")
    void shouldCalculateTotalValueForSingleProduct() {
        List<Product> products = List.of(new Product(1L, "Single", 50.0, 10));
        when(productRepository.findAll()).thenReturn(products);

        double totalValue = productService.getTotalInventoryValue();

        assertThat(totalValue).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Should handle large quantity values")
    void shouldHandleLargeQuantityValues() {
        List<Product> products = List.of(new Product(1L, "High Quantity", 1.0, 1_000_000));
        when(productRepository.findAll()).thenReturn(products);

        double totalValue = productService.getTotalInventoryValue();

        assertThat(totalValue).isEqualTo(1_000_000.0);
    }

    @Test
    @DisplayName("Should handle floating point precision correctly")
    void shouldHandleFloatingPointPrecisionCorrectly() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Item1", 0.1, 3),
                new Product(2L, "Item2", 0.2, 3)
        );
        when(productRepository.findAll()).thenReturn(products);

        double totalValue = productService.getTotalInventoryValue();

        assertThat(totalValue).isCloseTo(0.9, within(0.0001));
    }

    @Test
    @DisplayName("Should verify findAll called once for inventory calculation")
    void shouldVerifyFindAllCalledOnceForInventoryCalculation() {
        when(productRepository.findAll()).thenReturn(List.of());

        productService.getTotalInventoryValue();

        verify(productRepository, times(1)).findAll();
    }

    // ==================== PRICE RANGE TESTS (FIXED) ====================

    @ParameterizedTest
    @CsvSource({
            "10.0, 50.0",
            "0.0, 100.0",
            "75.0, 200.0",
            "100.0, 100.0"
    })
    @DisplayName("Should get products by valid price range")
    void shouldGetProductsByValidPriceRange(double min, double max) {
        List<Product> expectedProducts = List.of(
                new Product(1L, "Product", 30.0, 5)
        );
        when(productRepository.findProductsInPriceRange(min, max)).thenReturn(expectedProducts);

        List<Product> result = productService.getProductsByPriceRange(min, max);

        assertThat(result).hasSize(1);
        verify(productRepository).findProductsInPriceRange(min, max);
    }

    @Test
    @DisplayName("Should return empty list for price range with no matches")
    void shouldReturnEmptyListForPriceRangeWithNoMatches() {
        when(productRepository.findProductsInPriceRange(1000.0, 2000.0))
                .thenReturn(List.of());

        List<Product> result = productService.getProductsByPriceRange(1000.0, 2000.0);

        assertThat(result).isEmpty();
        verify(productRepository).findProductsInPriceRange(1000.0, 2000.0);
    }

    @Test
    @DisplayName("Should throw exception for negative minimum price")
    void shouldThrowExceptionForNegativeMinimumPrice() {
        assertThatThrownBy(() -> productService.getProductsByPriceRange(-10.0, 50.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Minimum price cannot be negative");
    }

    @Test
    @DisplayName("Should throw exception when max price less than min price")
    void shouldThrowExceptionWhenMaxPriceLessThanMinPrice() {
        assertThatThrownBy(() -> productService.getProductsByPriceRange(100.0, 50.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Maximum price cannot be less than minimum price");
    }

    @Test
    @DisplayName("Should throw exception for negative maximum price - FIXED")
    void shouldThrowExceptionForNegativeMaximumPrice() {
        // FIXED: Use min=0 so max negative is detected first
        assertThatThrownBy(() -> productService.getProductsByPriceRange(0.0, -5.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Maximum price cannot be negative");

        // Also test with negative min (should fail on min check first)
        assertThatThrownBy(() -> productService.getProductsByPriceRange(-10.0, -5.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Minimum price cannot be negative");
    }

    @Test
    @DisplayName("Should handle multiple price range queries")
    void shouldHandleMultiplePriceRangeQueries() {
        List<Product> cheapProducts = List.of(new Product(1L, "Cheap", 10.0, 5));
        List<Product> expensiveProducts = List.of(new Product(2L, "Expensive", 1000.0, 2));

        when(productRepository.findProductsInPriceRange(0.0, 50.0)).thenReturn(cheapProducts);
        when(productRepository.findProductsInPriceRange(500.0, 2000.0)).thenReturn(expensiveProducts);

        List<Product> result1 = productService.getProductsByPriceRange(0.0, 50.0);
        List<Product> result2 = productService.getProductsByPriceRange(500.0, 2000.0);

        assertThat(result1).hasSize(1);
        assertThat(result2).hasSize(1);
        verify(productRepository).findProductsInPriceRange(0.0, 50.0);
        verify(productRepository).findProductsInPriceRange(500.0, 2000.0);
    }

    @Test
    @DisplayName("Should verify price range parameters are passed correctly")
    void shouldVerifyPriceRangeParametersArePassedCorrectly() {
        when(productRepository.findProductsInPriceRange(anyDouble(), anyDouble()))
                .thenReturn(List.of());

        productService.getProductsByPriceRange(25.0, 75.0);

        ArgumentCaptor<Double> minCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Double> maxCaptor = ArgumentCaptor.forClass(Double.class);
        verify(productRepository).findProductsInPriceRange(minCaptor.capture(), maxCaptor.capture());

        assertThat(minCaptor.getValue()).isEqualTo(25.0);
        assertThat(maxCaptor.getValue()).isEqualTo(75.0);
    }

    @Test
    @DisplayName("Should handle exact match price range")
    void shouldHandleExactMatchPriceRange() {
        Product exactProduct = new Product(1L, "Exact", 100.0, 1);
        when(productRepository.findProductsInPriceRange(100.0, 100.0))
                .thenReturn(List.of(exactProduct));

        List<Product> result = productService.getProductsByPriceRange(100.0, 100.0);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPrice()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Should handle zero to positive price range")
    void shouldHandleZeroToPositivePriceRange() {
        List<Product> products = List.of(new Product(1L, "Free", 0.0, 5));
        when(productRepository.findProductsInPriceRange(0.0, 50.0))
                .thenReturn(products);

        List<Product> result = productService.getProductsByPriceRange(0.0, 50.0);

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Should verify price range order independence")
    void shouldVerifyPriceRangeOrderIndependence() {
        when(productRepository.findProductsInPriceRange(10.0, 20.0))
                .thenReturn(List.of());

        productService.getProductsByPriceRange(10.0, 20.0);

        InOrder inOrder = inOrder(productRepository);
        inOrder.verify(productRepository).findProductsInPriceRange(10.0, 20.0);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Should handle repository exception in price range search")
    void shouldHandleRepositoryExceptionInPriceRangeSearch() {
        when(productRepository.findProductsInPriceRange(anyDouble(), anyDouble()))
                .thenThrow(new RuntimeException("Repository query failed"));

        assertThatThrownBy(() -> productService.getProductsByPriceRange(10.0, 20.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Repository query failed");
    }

    @Test
    @DisplayName("Should verify price range validation happens before repository call")
    void shouldVerifyPriceRangeValidationHappensBeforeRepositoryCall() {
        try {
            productService.getProductsByPriceRange(-10.0, 50.0);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        verify(productRepository, never()).findProductsInPriceRange(anyDouble(), anyDouble());
    }

    // ==================== PRODUCT UPDATE TESTS ====================

    @Test
    @DisplayName("Should update product quantity successfully")
    void shouldUpdateProductQuantity() {
        Product existingProduct = new Product(1L, "Mouse", 25.0, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).updateQuantity(1L, 15);

        productService.updateProductQuantity(1L, 15);

        assertThat(existingProduct.getQuantity()).isEqualTo(15);
        verify(productRepository).findById(1L);
        verify(productRepository).updateQuantity(1L, 15);
    }

    @Test
    @DisplayName("Should throw exception when updating to negative quantity")
    void shouldThrowExceptionWhenUpdatingToNegativeQuantity() {
        assertThatThrownBy(() -> productService.updateProductQuantity(1L, -5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity cannot be negative");

        verify(productRepository, never()).findById(anyLong());
        verify(productRepository, never()).updateQuantity(anyLong(), anyInt());
    }

    @Test
    @DisplayName("Should throw exception when updating quantity for non-existent product")
    void shouldThrowExceptionWhenUpdatingQuantityForNonExistentProduct() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProductQuantity(999L, 10))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found with id: 999");
    }

    @Test
    @DisplayName("Should throw exception when updating with invalid product id")
    void shouldThrowExceptionWhenUpdatingWithInvalidProductId() {
        assertThatThrownBy(() -> productService.updateProductQuantity(null, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid product id");

        assertThatThrownBy(() -> productService.updateProductQuantity(0L, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid product id");

        assertThatThrownBy(() -> productService.updateProductQuantity(-1L, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid product id");
    }

    @Test
    @DisplayName("Should update quantity to zero")
    void shouldUpdateQuantityToZero() {
        Product existingProduct = new Product(1L, "Item", 10.0, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).updateQuantity(1L, 0);

        productService.updateProductQuantity(1L, 0);

        assertThat(existingProduct.getQuantity()).isZero();
        verify(productRepository).updateQuantity(1L, 0);
    }

    @Test
    @DisplayName("Should verify update quantity parameters")
    void shouldVerifyUpdateQuantityParameters() {
        Product existingProduct = new Product(1L, "Item", 10.0, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).updateQuantity(anyLong(), anyInt());

        productService.updateProductQuantity(1L, 25);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> quantityCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(productRepository).updateQuantity(idCaptor.capture(), quantityCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(1L);
        assertThat(quantityCaptor.getValue()).isEqualTo(25);
    }

    @Test
    @DisplayName("Should verify findById and updateQuantity order")
    void shouldVerifyFindByIdAndUpdateQuantityOrder() {
        Product existingProduct = new Product(1L, "Item", 10.0, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).updateQuantity(1L, 10);

        productService.updateProductQuantity(1L, 10);

        InOrder inOrder = inOrder(productRepository);
        inOrder.verify(productRepository).findById(1L);
        inOrder.verify(productRepository).updateQuantity(1L, 10);
    }

    @Test
    @DisplayName("Should handle repository update exception")
    void shouldHandleRepositoryUpdateException() {
        Product existingProduct = new Product(1L, "Item", 10.0, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        doThrow(new RuntimeException("Update failed")).when(productRepository).updateQuantity(1L, 10);

        assertThatThrownBy(() -> productService.updateProductQuantity(1L, 10))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Update failed");
    }

    @Test
    @DisplayName("Should preserve price when updating quantity")
    void shouldPreservePriceWhenUpdatingQuantity() {
        Product existingProduct = new Product(1L, "Item", 99.99, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).updateQuantity(1L, 20);

        productService.updateProductQuantity(1L, 20);

        assertThat(existingProduct.getPrice()).isEqualTo(99.99);
        assertThat(existingProduct.getQuantity()).isEqualTo(20);
    }

    // ==================== ADVANCED SCENARIO TESTS ====================

    @Test
    @DisplayName("Should create multiple products and maintain state")
    void shouldCreateMultipleProductsAndMaintainState() {
        Product product1 = new Product(1L, "Product1", 100.0, 5);
        Product product2 = new Product(2L, "Product2", 200.0, 3);

        when(productRepository.save(any(Product.class)))
                .thenReturn(product1)
                .thenReturn(product2);

        Product result1 = productService.createProduct("Product1", 100.0, 5);
        Product result2 = productService.createProduct("Product2", 200.0, 3);

        assertThat(result1).isEqualTo(product1);
        assertThat(result2).isEqualTo(product2);
        verify(productRepository, times(2)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should handle product with maximum price value")
    void shouldHandleProductWithMaximumPriceValue() {
        Product savedProduct = new Product(1L, "Max Price", Double.MAX_VALUE, 1);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct("Max Price", Double.MAX_VALUE, 1);

        assertThat(result.getPrice()).isEqualTo(Double.MAX_VALUE);
        assertThat(result.getTotalValue()).isEqualTo(Double.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle product name with special characters")
    void shouldHandleProductNameWithSpecialCharacters() {
        String specialName = "Product!@#$%^&*()_+";
        Product savedProduct = new Product(1L, specialName, 100.0, 5);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct(specialName, 100.0, 5);

        assertThat(result.getName()).isEqualTo(specialName);
    }

    @Test
    @DisplayName("Should verify product total value calculation")
    void shouldVerifyProductTotalValueCalculation() {
        Product product = new Product(1L, "Test", 25.50, 4);

        double totalValue = product.getTotalValue();

        assertThat(totalValue).isEqualTo(102.0);
    }

    @Test
    @DisplayName("Should handle concurrent product updates")
    void shouldHandleConcurrentProductUpdates() throws InterruptedException {
        Product existingProduct = new Product(1L, "Concurrent", 100.0, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).updateQuantity(anyLong(), anyInt());

        Thread thread1 = new Thread(() -> productService.updateProductQuantity(1L, 5));
        Thread thread2 = new Thread(() -> productService.updateProductQuantity(1L, 15));

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        verify(productRepository, times(2)).updateQuantity(eq(1L), anyInt());
    }

    @Test
    @DisplayName("Should verify all repository method calls")
    void shouldVerifyAllRepositoryMethodCalls() {
        when(productRepository.findAll()).thenReturn(List.of());
        when(productRepository.findProductsInPriceRange(anyDouble(), anyDouble()))
                .thenReturn(List.of());

        productService.getAllProducts();
        productService.getProductsByPriceRange(10.0, 20.0);
        productService.getTotalInventoryValue();

        verify(productRepository, times(2)).findAll();
        verify(productRepository, times(1)).findProductsInPriceRange(10.0, 20.0);
    }
}
