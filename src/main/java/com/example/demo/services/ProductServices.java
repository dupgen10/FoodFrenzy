package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductServices {

    private static final Logger log = LoggerFactory.getLogger(ProductServices.class);

    @Autowired
    private ProductRepository productRepository;

    public void addProduct(Product p) {
        this.productRepository.save(p);
        log.info("Added product: {}", p.getPname());
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    /**
     * Returns product by ID, or throws EntityNotFoundException.
     * Previously called Optional.get() without checking isPresent().
     */
    public Product getProduct(int id) {
        Optional<Product> optional = this.productRepository.findById(id);
        return optional.orElseThrow(() ->
                new EntityNotFoundException("Product not found with id: " + id));
    }

    public void updateproduct(Product p, int id) {
        // Verify product exists before attempting update
        getProduct(id); // throws EntityNotFoundException if not found
        p.setPid(id);
        this.productRepository.save(p);
        log.info("Updated product with id: {}", id);
    }

    public void deleteProduct(int id) {
        this.productRepository.deleteById(id);
        log.info("Deleted product with id: {}", id);
    }

    public Product getProductByName(String name) {
        return this.productRepository.findByPname(name);
    }
}