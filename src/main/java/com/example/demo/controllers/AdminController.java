package com.example.demo.controllers;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Orders;
import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.loginCredentials.UserLogin;
import com.example.demo.services.OrderServices;
import com.example.demo.services.ProductServices;
import com.example.demo.services.UserServices;

import java.util.List;

/**
 * Handles user-facing product browsing, search, and order placement.
 *
 * SECURITY FIXES vs original:
 * - Removed thread-unsafe instance fields (email, user). Session is now used
 *   per-request, so concurrent users cannot corrupt each other's data.
 * - User object is fetched from HttpSession on each request.
 * - DELETE/UPDATE operations remain POST-only (as configured in SecurityConfig).
 */
@Controller
public class AdminController {

    @Autowired
    private UserServices services;
    @Autowired
    private com.example.demo.services.AdminServices adminServices;
    @Autowired
    private ProductServices productServices;
    @Autowired
    private OrderServices orderServices;

    // ──────────────────────────────────────────────────────────────
    // ADMIN LOGIN
    // ──────────────────────────────────────────────────────────────

    @PostMapping("/adminLogin")
    public String getAllData(@ModelAttribute("adminLogin") com.example.demo.loginCredentials.AdminLogin login,
                             Model model) {
        String email = login.getEmail();
        String password = login.getPassword();
        if (adminServices.validateAdminCredentials(email, password)) {
            return "redirect:/admin/services";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "Login";
        }
    }

    // ──────────────────────────────────────────────────────────────
    // USER LOGIN — stores authenticated user in session (thread-safe)
    // ──────────────────────────────────────────────────────────────

    @PostMapping("/userLogin")
    public String userLogin(@ModelAttribute("userLogin") UserLogin login,
                            Model model, HttpSession session) {
        String email = login.getUserEmail();
        String password = login.getUserPassword();

        if (services.validateLoginCredentials(email, password)) {
            User user = this.services.getUserByEmail(email);
            session.setAttribute("loggedInUser", user); // store per-session, not shared field
            List<Orders> orders = this.orderServices.getOrdersForUser(user);
            model.addAttribute("orders", orders);
            model.addAttribute("name", user.getUname());
            return "BuyProduct";
        } else {
            model.addAttribute("error2", "Invalid email or password");
            return "Login";
        }
    }

    // ──────────────────────────────────────────────────────────────
    // PRODUCT SEARCH & ORDER (user flows)
    // ──────────────────────────────────────────────────────────────

    @PostMapping("/product/search")
    public String searchHandler(
            @org.springframework.web.bind.annotation.RequestParam("productName") String name,
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Product product = this.productServices.getProductByName(name);
        List<Orders> orders = this.orderServices.getOrdersForUser(user);
        model.addAttribute("orders", orders);

        if (product == null) {
            model.addAttribute("message", "Sorry! Product Unavailable.");
        }
        model.addAttribute("product", product);
        model.addAttribute("name", user.getUname());
        return "BuyProduct";
    }

    @PostMapping("/product/order")
    public String orderHandler(@ModelAttribute Orders order, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        double totalAmount = com.example.demo.count.Logic.countTotal(
                order.getoPrice(), order.getoQuantity());
        order.setTotalAmount(totalAmount);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        this.orderServices.saveOrder(order);
        model.addAttribute("amount", totalAmount);
        return "Order_success";
    }

    @GetMapping("/product/back")
    public String back(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<Orders> orders = this.orderServices.getOrdersForUser(user);
        model.addAttribute("orders", orders);
        model.addAttribute("name", user.getUname());
        return "BuyProduct";
    }

    // ──────────────────────────────────────────────────────────────
    // ADMIN DASHBOARD
    // ──────────────────────────────────────────────────────────────

    @GetMapping("/admin/services")
    public String adminDashboard(Model model) {
        model.addAttribute("users", this.services.getAllUser());
        model.addAttribute("admins", this.adminServices.getAll());
        model.addAttribute("products", this.productServices.getAllProducts());
        model.addAttribute("orders", this.orderServices.getOrders());
        return "Admin_Page";
    }

    // ──────────────────────────────────────────────────────────────
    // ADMIN CRUD
    // ──────────────────────────────────────────────────────────────

    @GetMapping("/addAdmin")
    public String addAdminPage() {
        return "Add_Admin";
    }

    @PostMapping("/addingAdmin")
    public String addAdmin(@ModelAttribute com.example.demo.entities.Admin admin) {
        this.adminServices.addAdmin(admin);
        return "redirect:/admin/services";
    }

    @GetMapping("/updateAdmin/{adminId}")
    public String update(@PathVariable("adminId") int id, Model model) {
        com.example.demo.entities.Admin admin = this.adminServices.getAdmin(id);
        model.addAttribute("admin", admin);
        return "Update_Admin";
    }

    @PostMapping("/updatingAdmin/{id}")
    public String updateAdmin(@ModelAttribute com.example.demo.entities.Admin admin,
                              @PathVariable("id") int id) {
        this.adminServices.update(admin, id);
        return "redirect:/admin/services";
    }

    @PostMapping("/deleteAdmin/{id}")
    public String deleteAdmin(@PathVariable("id") int id) {
        this.adminServices.delete(id);
        return "redirect:/admin/services";
    }

    // ──────────────────────────────────────────────────────────────
    // PRODUCT CRUD (navigation to forms — actual CRUD in ProductController)
    // ──────────────────────────────────────────────────────────────

    @GetMapping("/addProduct")
    public String addProduct() {
        return "Add_Product";
    }

    @GetMapping("/updateProduct/{productId}")
    public String updateProduct(@PathVariable("productId") int id, Model model) {
        Product product = this.productServices.getProduct(id);
        model.addAttribute("product", product);
        return "Update_Product";
    }

    // ──────────────────────────────────────────────────────────────
    // USER CRUD (admin-side navigation to forms)
    // ──────────────────────────────────────────────────────────────

    @GetMapping("/addUser")
    public String addUser() {
        return "Add_User";
    }

    @GetMapping("/updateUser/{userId}")
    public String updateUserPage(@PathVariable("userId") int id, Model model) {
        User user = this.services.getUser(id);
        model.addAttribute("user", user);
        return "Update_User";
    }
}