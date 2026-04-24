package com.testing.demo.repository;

import com.testing.demo.model.Product;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProductRepository {
    private final Map<Long, Product> productStore = new ConcurrentHashMap<>();
    private Long currentId = 1L;

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(currentId++);
        }
        productStore.put(product.getId(), product);
        return product;
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productStore.get(id));
    }

    public List<Product> findAll() {
        return new ArrayList<>(productStore.values());
    }

    public List<Product> findProductsInPriceRange(double min, double max) {
        return productStore.values().stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public void updateQuantity(Long id, int quantity) {
        Product product = productStore.get(id);
        if (product != null) {
            product.setQuantity(quantity);
        }
    }
}
