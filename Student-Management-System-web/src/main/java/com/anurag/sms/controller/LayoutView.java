package com.anurag.sms.controller;

import org.springframework.ui.Model;

/**
 * Renders a page inside the shared app shell (layout/layout.html), so the
 * sidebar, navbar and footer survive every navigation (#6).
 *
 * Every handler that returns a page, including the validation-error
 * branches of form handlers, must go through here. Returning a template
 * name directly renders the bare page with no shell.
 */
public final class LayoutView {

    private static final String APP_NAME = "Student Management System";

    private LayoutView() {
    }

    /**
     * @param content   fragment selector, e.g. "student/student-list :: content".
     *                  Without the ":: fragment" part Thymeleaf would inject the
     *                  entire document, html and body tags included.
     * @param activeNav sidebar key to highlight, e.g. "student"
     * @param title     page name shown in the browser tab, e.g. "Students"
     * @return the layout view name
     */
    public static String render(Model model, String content, String activeNav, String title) {

        model.addAttribute("content", content);
        model.addAttribute("activeNav", activeNav);
        model.addAttribute("title", title + " | " + APP_NAME);

        return "layout/layout";
    }
}
