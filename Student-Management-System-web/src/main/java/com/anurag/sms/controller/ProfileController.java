package com.anurag.sms.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anurag.sms.dto.ChangePasswordDto;
import com.anurag.sms.service.UserService;

import jakarta.validation.Valid;

/**
 * The signed-in user's own account (F9) and password change (F10). Every
 * role has one, so these routes only need a login.
 */
@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {

        model.addAttribute("passwordForm", new ChangePasswordDto());
        return render(principal, model);
    }

    @PostMapping("/profile/password")
    public String changePassword(
            @Valid @ModelAttribute("passwordForm") ChangePasswordDto form,
            BindingResult result,
            Principal principal,
            Model model,
            RedirectAttributes redirect) {

        if (form.getNewPassword() != null && !form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "password.mismatch", "The two new passwords do not match");
        }

        if (!result.hasErrors()) {
            try {
                userService.changePassword(principal.getName(), form.getCurrentPassword(), form.getNewPassword());
                redirect.addFlashAttribute("passwordChanged", true);
                return "redirect:/profile";
            } catch (IllegalArgumentException e) {
                String field = UserService.WRONG_CURRENT_PASSWORD.equals(e.getMessage())
                        ? "currentPassword" : "newPassword";
                result.rejectValue(field, "password.rejected", e.getMessage());
            }
        }

        // Never echo passwords back into the form
        form.setCurrentPassword(null);
        form.setNewPassword(null);
        form.setConfirmPassword(null);
        return render(principal, model);
    }

    private String render(Principal principal, Model model) {

        model.addAttribute("account", userService.getByUsername(principal.getName()));
        return LayoutView.render(model, "profile/profile :: content", "profile", "My Profile");
    }
}
