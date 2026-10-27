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
import com.anurag.sms.service.StudentService;
import com.anurag.sms.service.TeacherService;
import com.anurag.sms.service.UserService;

/**
 * Account management for admins (F11, F12). /admin/** is admin-only in
 * SecurityConfig. Changes are POSTs, so they carry the CSRF token.
 *
 * Disabling an account or changing its role signs it out at once (#37):
 * UserService ends its sessions, and its next request goes to the login
 * page.
 */
@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public AdminUserController(UserService userService,
                               StudentService studentService,
                               TeacherService teacherService) {
        this.userService = userService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String listUsers(Principal principal, Model model) {

        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", UserService.ROLES);
        model.addAttribute("me", principal.getName());

        // The people an account could still be linked to (G10)
        model.addAttribute("unlinkedStudents", studentService.getStudentsWithoutAccount());
        model.addAttribute("unlinkedTeachers", teacherService.getTeachersWithoutAccount());

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

    // Link an account to its student or teacher record (G10). The form
    // sends studentId for a STUDENT account, teacherId for a TEACHER one.
    @PostMapping("/{id}/link")
    public String link(@PathVariable Long id,
                       @RequestParam(required = false) Long studentId,
                       @RequestParam(required = false) Long teacherId,
                       RedirectAttributes redirect) {
        try {
            if (studentId != null) {
                User user = userService.linkStudent(id, studentId);
                redirect.addFlashAttribute("message", "Linked " + user.getUsername() + " to "
                        + user.getStudent().getFirstName() + " " + user.getStudent().getLastName() + ".");
            } else if (teacherId != null) {
                User user = userService.linkTeacher(id, teacherId);
                redirect.addFlashAttribute("message", "Linked " + user.getUsername() + " to "
                        + user.getTeacher().getFirstName() + " " + user.getTeacher().getLastName() + ".");
            } else {
                redirect.addFlashAttribute("error", "Pick a student or teacher to link.");
            }
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/unlink")
    public String unlink(@PathVariable Long id, RedirectAttributes redirect) {

        User user = userService.unlink(id);
        redirect.addFlashAttribute("message", "Unlinked " + user.getUsername() + ".");
        return "redirect:/admin/users";
    }
}
