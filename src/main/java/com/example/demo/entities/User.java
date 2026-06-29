package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users") // "user" is a reserved keyword in PostgreSQL/MySQL — renamed to "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer u_id;

    @NotBlank(message = "Name is required")
    private String uname;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String uemail;

    @NotBlank(message = "Password is required")
    @Size(min = 4, max = 100)
    private String upassword;

    private Long unumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Orders> orders;

    public User() {
    }

    public User(String uemail, String upassword) {
        this.uemail = uemail;
        this.upassword = upassword;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public Integer getU_id() {
        return u_id;
    }

    public void setU_id(Integer u_id) {
        this.u_id = u_id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public Long getUnumber() {
        return unumber;
    }

    public void setUnumber(Long unumber) {
        this.unumber = unumber;
    }

    @Override
    public String toString() {
        // Password and orders intentionally excluded:
        //   - Password: prevents plaintext leakage in logs
        //   - Orders: prevents infinite recursion (User->Orders->User)
        return "User [u_id=" + u_id + ", uname=" + uname + ", uemail=" + uemail
                + ", unumber=" + unumber + "]";
    }
}