package com.example.demo.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Orders;
import com.example.demo.entities.User;
import com.example.demo.repositories.OrderRepository;

@Service
public class OrderServices {

    private static final Logger log = LoggerFactory.getLogger(OrderServices.class);

    @Autowired
    private OrderRepository orderRepository;

    public List<Orders> getOrders() {
        return this.orderRepository.findAll();
    }

    public void saveOrder(Orders order) {
        this.orderRepository.save(order);
        log.info("Saved order for product: {}", order.getoName());
    }

    public void updateOrder(int id, Orders order) {
        order.setoId(id);
        this.orderRepository.save(order);
    }

    public void deleteOrder(int id) {
        this.orderRepository.deleteById(id);
    }

    public List<Orders> getOrdersForUser(User user) {
        return this.orderRepository.findOrdersByUser(user);
    }
}