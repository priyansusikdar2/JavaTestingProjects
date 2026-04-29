package com.testing.demo.jmeter1;

import com.testing.demo.model.Product;
import com.testing.demo.repository.ProductRepository;
import com.testing.demo.service.ProductService;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.util.UUID;

public class ProductServiceSampler extends AbstractJavaSamplerClient {
    private ProductService productService;
    private ProductRepository productRepository;

    @Override
    public void setupTest(JavaSamplerContext context) {
        productRepository = new ProductRepository();
        productService = new ProductService(productRepository);
        System.out.println("JMeter: ProductService initialized");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
        result.sampleStart();

        try {
            // Generate unique product
            String productName = "JMeter_Product_" + UUID.randomUUID().toString().substring(0, 8);
            double price = 10.0 + (Math.random() * 990);
            int quantity = (int)(Math.random() * 100);

            // Execute the service method
            Product product = productService.createProduct(productName, price, quantity);

            // Optionally do some other operations
            if (quantity > 50) {
                productService.updateProductQuantity(product.getId(), quantity / 2);
            }

            // Mark as successful
            result.setSuccessful(true);
            result.setResponseMessage("Product created successfully with ID: " + product.getId());
            result.setResponseCode("200");
            result.setResponseData("Product: " + productName + ", Price: $" + String.format("%.2f", price), "UTF-8");

        } catch (Exception e) {
            // Mark as failed
            result.setSuccessful(false);
            result.setResponseMessage("Failed: " + e.getMessage());
            result.setResponseCode("500");
            result.setResponseData("Error: " + e.getMessage(), "UTF-8");
        }

        result.sampleEnd();
        return result;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        // No cleanup needed - let GC handle it
        System.out.println("JMeter: ProductService test completed");
    }
}
