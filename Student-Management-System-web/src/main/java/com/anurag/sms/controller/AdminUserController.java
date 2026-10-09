package com.anurag.sms.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anurag.sms.entity.User;
import com.anurag.sms.service.UserService;

/**
 * Account management for admins (F11, F12). /admin/** is admin-only in
 * SecurityConfig. Changes are POSTs, so they carry the CSRF token.
 *
 * A change applies from the account's next sign-in: someone signed in when
 * they are disabled or given a new role keeps their current session until
 * it ends.
 */
@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Principal principal, Model model) {

        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", UserService.ROLES);
        model.addAttribute("me", principal.getName());

        return LayoutView.render(model, "admin/users :: content", "users", "Users");
    }

    @PostMapping("/{id}/enabled")
    public String setEnabled(@PathVariable Long id,
                             @RequestParam boolean enabled,
                             Principal principal,
                             RedirectAttributes redirect) {
        try {
            User user = userService.setEnabled(id, enabled, principal.getName());
            redirect.addFlashAttribute("message",
                    (enabled ? "Enabled " : "Disabled ") + user.getUsername() + ".");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/role")
    public String changeRole(@PathVariable Long id,
                             @RequestParam String role,
                             Principal principal,
                             RedirectAttributes redirect) {
        try {
            User user = userService.changeRole(id, role, principal.getName());
            redirect.addFlashAttribute("message",
                    user.getUsername() + " is now " + role.replace("ROLE_", "") + ".");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
