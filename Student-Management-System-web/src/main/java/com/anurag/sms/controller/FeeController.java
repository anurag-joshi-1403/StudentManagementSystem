package com.anurag.sms.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
import com.anurag.sms.service.PdfService;
import com.anurag.sms.service.impl.PdfServiceImpl;
import com.anurag.sms.service.StudentService;
import com.anurag.sms.utility.CsvHelper;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/fee")
public class FeeController {

    private final FeeService feeService;
    private final StudentService studentService;
    private final PdfService pdfService;

    // Printed on the receipt; see application.properties
    private final String institutionName;

    public FeeController(FeeService feeService,
            StudentService studentService,
            PdfService pdfService,
            @Value("${sms.institution-name}") String institutionName) {
        this.feeService = feeService;
        this.studentService = studentService;
        this.pdfService = pdfService;
        this.institutionName = institutionName;
    }

    // Display Fee List
    @GetMapping
    public String listFees(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        // The list and the search are one paged query: a blank keyword lists
        // every fee. Search used to report a fixed totalPages = 1 (#11).
        Page<Fee> feePage = feeService.searchFee(keyword, page);
        model.addAttribute("fees", feePage.getContent());
        model.addAttribute("currentPage", feePage.getNumber() + 1);
        model.addAttribute("totalPages", feePage.getTotalPages());
        model.addAttribute("totalItems", feePage.getTotalElements());
        model.addAttribute("totalFees", feeService.getTotalFees());
        model.addAttribute("keyword", keyword);

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

    // View Fee as a printable receipt (admins only, like every /fee page)
    @GetMapping("/view/{id}")
    public String viewFee(@PathVariable Long id,
            Model model) {

        model.addAttribute("fee", feeService.getFeeById(id));
        model.addAttribute("institutionName", institutionName);
        model.addAttribute("issuedOn", LocalDate.now());

        return LayoutView.render(model, "fee/fee-view :: content", "fee", "Fee Receipt");
    }

    // The receipt as a PDF (F8), with the same facts as the receipt page
    @GetMapping("/{id}/receipt.pdf")
    public ResponseEntity<byte[]> receiptPdf(@PathVariable Long id) {

        Fee fee = feeService.getFeeById(id);
        byte[] pdf = pdfService.feeReceipt(fee, LocalDate.now());

        return FileDownload.pdf("receipt-" + PdfServiceImpl.receiptNumber(fee) + ".pdf", pdf);
    }

    // CSV export for a spreadsheet (E20), admins only like every /fee page
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportFees() {

        List<Fee> fees = new ArrayList<>(feeService.getAllFees());
        fees.sort(Comparator.comparing(Fee::getId));

        return FileDownload.csv("fees.csv", CsvHelper.feesToCsv(fees));
    }

    // Delete Fee
    @PostMapping("/delete/{id}")
    public String deleteFee(@PathVariable Long id) {

        feeService.deleteFee(id);

        return "redirect:/fee";
    }

}