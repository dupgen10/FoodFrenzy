package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.ContactMessage;
import com.example.demo.entities.Product;
import com.example.demo.loginCredentials.AdminLogin;
import com.example.demo.loginCredentials.UserLogin;
import com.example.demo.services.ContactMessageService;
import com.example.demo.services.ProductServices;

@Controller
public class HomeController {

    @Autowired
    private ProductServices productServices;

    @Autowired
    private ContactMessageService contactMessageService;

    @GetMapping(value = {"/home", "/"})
    public String home() {
        return "Home";
    }

    /**
     * Public product catalog — now dynamic from the database.
     */
    @GetMapping("/products")
    public String products(Model model) {
        List<Product> allProducts = this.productServices.getAllProducts();
        model.addAttribute("products", allProducts);
        return "Products";
    }

    @GetMapping("/location")
    public String location() {
        return "Locate_us";
    }

    @GetMapping("/about")
    public String about() {
        return "About";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("adminLogin", new AdminLogin());
        model.addAttribute("userLogin", new UserLogin());
        return "Login";
    }

    // ──────────────────────────────────────────────────────────────
    // CONTACT FORM — stores messages in the database
    // ──────────────────────────────────────────────────────────────

    @PostMapping("/contact/submit")
    public String handleContactForm(@ModelAttribute ContactMessage contactMessage) {
        contactMessageService.saveMessage(contactMessage);
        return "redirect:/location?success=true";
    }

    // ──────────────────────────────────────────────────────────────
    // ABOUT PAGE CONTACT FORM
    // ──────────────────────────────────────────────────────────────

    @PostMapping("/about/contact")
    public String handleAboutContactForm(@ModelAttribute ContactMessage contactMessage) {
        contactMessageService.saveMessage(contactMessage);
        return "redirect:/about?success=true";
    }

    // ──────────────────────────────────────────────────────────────
    // ADMIN: View contact messages (admin-only, secured by SecurityConfig)
    // ──────────────────────────────────────────────────────────────

    @GetMapping("/contact/messages")
    public String viewMessages(Model model) {
        List<ContactMessage> messages = contactMessageService.getAllMessages();
        long unread = contactMessageService.countUnread();
        model.addAttribute("messages", messages);
        model.addAttribute("unread", unread);
        return "Contact_Messages";
    }
}