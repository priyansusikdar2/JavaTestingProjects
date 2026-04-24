package com.testing.demo.service;

import com.testing.demo.model.Product;
import com.testing.demo.repository.ProductRepository;
import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String name, double price, int quantity) {
        validateProductData(name, price, quantity);

        Product product = new Product(null, name, price, quantity);
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid product id");
        }

        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public double getTotalInventoryValue() {
        return productRepository.findAll().stream()
                .mapToDouble(Product::getTotalValue)
                .sum();
    }

    public List<Product> getProductsByPriceRange(double min, double max) {
        if (min < 0) {
            throw new IllegalArgumentException("Minimum price cannot be negative");
        }
        if (max < min) {
            throw new IllegalArgumentException("Maximum price cannot be less than minimum price");
        }
        if (max < 0) {
            throw new IllegalArgumentException("Maximum price cannot be negative");
        }
        return productRepository.findProductsInPriceRange(min, max);
    }

    public void updateProductQuantity(Long id, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid product id");
        }

        Product product = getProductById(id);
        product.setQuantity(quantity);
        productRepository.updateQuantity(id, quantity);
    }

    private void validateProductData(String name, double price, int quantity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }
}
