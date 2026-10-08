package com.anurag.sms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anurag.sms.entity.Fee;
import com.anurag.sms.service.FeeService;
import com.anurag.sms.service.StudentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/fee")
public class FeeController {

    private final FeeService feeService;
    private final StudentService studentService;

    public FeeController(FeeService feeService,
            StudentService studentService) {
        this.feeService = feeService;
        this.studentService = studentService;
    }

    // Display Fee List
    @GetMapping
    public String listFees(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        if (keyword != null && !keyword.isBlank()) {
            java.util.List<Fee> searchResults = feeService.searchFee(keyword);
            model.addAttribute("fees", searchResults);
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", 1);
            model.addAttribute("totalItems", searchResults.size());
            model.addAttribute("totalFees", feeService.getTotalFees());
            return LayoutView.render(model, "fee/fee-list :: content", "fee", "Fees");
        }

        Page<Fee> feePage = feeService.getFeeByPage(page);
        model.addAttribute("fees", feePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", feePage.getTotalPages());
        model.addAttribute("totalItems", feePage.getTotalElements());
        model.addAttribute("totalFees", feeService.getTotalFees());
        model.addAttribute("keyword", "");

        return LayoutView.render(model, "fee/fee-list :: content", "fee", "Fees");
    }

    // Search Fee mapping to handle /fee/search
    @GetMapping("/search")
    public String searchFee(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        return listFees(keyword, page, model);
    }

    // Add New Fee Form
    @GetMapping("/new")
    public String showAddFeeForm(Model model) {

        model.addAttribute("fee", new Fee());
        model.addAttribute("students", studentService.getAllStudents());

        return LayoutView.render(model, "fee/fee-form :: content", "fee", "Add Fee");
    }

    // Save Fee
    @PostMapping("/save")
    public String saveFee(@Valid @ModelAttribute("fee") Fee fee,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("students", studentService.getAllStudents());

            return LayoutView.render(model, "fee/fee-form :: content", "fee", "Add Fee");
        }

        feeService.saveFee(fee);

        return "redirect:/fee";
    }

    // Edit Fee Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
            Model model) {

        model.addAttribute("fee", feeService.getFeeById(id));
        model.addAttribute("students", studentService.getAllStudents());

        return LayoutView.render(model, "fee/fee-form :: content", "fee", "Edit Fee");
    }

    // Update Fee
    @PostMapping("/update/{id}")
    public String updateFee(@PathVariable Long id,
            @Valid @ModelAttribute("fee") Fee fee,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("students", studentService.getAllStudents());

            return LayoutView.render(model, "fee/fee-form :: content", "fee", "Edit Fee");
        }

        fee.setId(id);

        feeService.updateFee(fee);

        return "redirect:/fee";
    }

    // Delete Fee
    @PostMapping("/delete/{id}")
    public String deleteFee(@PathVariable Long id) {

        feeService.deleteFee(id);

        return "redirect:/fee";
    }

}