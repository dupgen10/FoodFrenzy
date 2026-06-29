package com.example.demo.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oId;

    @NotBlank(message = "Product name is required")
    private String oName;

    private double oPrice;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int oQuantity;

    private LocalDateTime orderDate;

    // Fixed typo: totalAmmout -> totalAmount
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "user_u_id")
    private User user;

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public String getoName() {
        return oName;
    }

    public void setoName(String oName) {
        this.oName = oName;
    }

    public double getoPrice() {
        return oPrice;
    }

    public void setoPrice(double oPrice) {
        this.oPrice = oPrice;
    }

    public int getoQuantity() {
        return oQuantity;
    }

    public void setoQuantity(int oQuantity) {
        this.oQuantity = oQuantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Kept for backward compat with any existing column references
    /** @deprecated Use {@link #getTotalAmount()} */
    @Deprecated
    public double getTotalAmmout() {
        return totalAmount;
    }

    /** @deprecated Use {@link #setTotalAmount(double)} */
    @Deprecated
    public void setTotalAmmout(double totalAmmout) {
        this.totalAmount = totalAmmout;
    }

    @Override
    public String toString() {
        // 'user' intentionally excluded to prevent infinite recursion
        // (Orders->User->Orders->...)
        return "Orders [oId=" + oId + ", oName=" + oName + ", oPrice=" + oPrice
                + ", oQuantity=" + oQuantity + ", orderDate=" + orderDate
                + ", totalAmount=" + totalAmount + "]";
    }
}