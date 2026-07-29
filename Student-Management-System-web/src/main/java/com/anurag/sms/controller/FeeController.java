package com.anurag.sms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String listFees(Model model) {

        model.addAttribute("fees", feeService.getAllFees());
        model.addAttribute("totalFees", feeService.getTotalFees());

        return "fee/fee-list";
    }

    // Add New Fee Form
    @GetMapping("/new")
    public String showAddFeeForm(Model model) {

        model.addAttribute("fee", new Fee());
        model.addAttribute("students", studentService.getAllStudents());

        return "fee/fee-form";
    }

    // Save Fee
    @PostMapping("/save")
    public String saveFee(@Valid @ModelAttribute("fee") Fee fee,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("students", studentService.getAllStudents());

            return "fee/fee-form";
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

        return "fee/fee-form";
    }

    // Update Fee
    @PostMapping("/update/{id}")
    public String updateFee(@PathVariable Long id,
            @Valid @ModelAttribute("fee") Fee fee,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("students", studentService.getAllStudents());

            return "fee/fee-form";
        }

        fee.setId(id);

        feeService.updateFee(fee);

        return "redirect:/fee";
    }

    // Delete Fee
    @GetMapping("/delete/{id}")
    public String deleteFee(@PathVariable Long id) {

        feeService.deleteFee(id);

        return "redirect:/fee";
    }

    // Search Fee
    @GetMapping("/search")
    public String searchFee(@RequestParam("keyword") String keyword,
            Model model) {

        model.addAttribute("fees", feeService.searchFee(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalFees", feeService.getTotalFees());

        return "fee/fee-list";
    }

    // Pagination
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable int pageNo,
            Model model) {

        Page<Fee> page = feeService.getFeeByPage(pageNo);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("fees", page.getContent());
        model.addAttribute("totalFees", feeService.getTotalFees());

        return "fee/fee-list";
    }
}