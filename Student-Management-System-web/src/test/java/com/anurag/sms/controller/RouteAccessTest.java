package com.anurag.sms.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import jakarta.servlet.ServletException;

/**
 * Every route against every role (#42), so a reordered rule in
 * SecurityConfig, where the first match wins, fails here instead of
 * opening a page to the wrong people. The matrix below is the README's
 * Roles table written out per route; it does not read SecurityConfig.
 *
 * A route added without a line in the matrix fails
 * everyRouteHasALineInTheMatrix, which is how "every new route gets a 403
 * test" is enforced.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RouteAccessTest {

    enum Access { PUBLIC, SIGNED_IN, STAFF, ADMIN }

    // "METHOD /path", with every {variable} written as {}
    private static final Map<String, Access> MATRIX = new LinkedHashMap<>();

    static {
        allow(Access.PUBLIC, "GET /", "GET /login", "GET /register", "POST /register");

        // Viewing: every role, until Stage I limits students to their own records
        allow(Access.SIGNED_IN,
                "GET /dashboard", "GET /profile", "POST /profile/password",
                "GET /student", "GET /student/search", "GET /student/page/{}", "GET /student/view/{}",
                "GET /student/{}/marksheet", "GET /student/{}/marksheet.pdf",
                "GET /teacher", "GET /teacher/view/{}",
                "GET /course", "GET /course/view/{}",
                "GET /subject", "GET /subject/view/{}",
                "GET /enrollment", "GET /enrollment/view/{}",
                "GET /attendance", "GET /attendance/search", "GET /attendance/page/{}",
                "GET /attendance/view/{}", "GET /attendance/report",
                "GET /exam", "GET /exam/search", "GET /exam/page/{}", "GET /exam/view/{}", "GET /exam/schedule",
                "GET /result", "GET /result/search", "GET /result/page/{}", "GET /result/view/{}");

        // Attendance, exams and results: teachers may change them too
        allow(Access.STAFF,
                "GET /attendance/new", "GET /attendance/edit/{}", "GET /attendance/bulk",
                "POST /attendance/save", "POST /attendance/update/{}", "POST /attendance/delete/{}",
                "POST /attendance/bulk",
                "GET /exam/new", "GET /exam/edit/{}",
                "POST /exam/save", "POST /exam/update/{}", "POST /exam/delete/{}",
                "GET /result/new", "GET /result/edit/{}",
                "POST /result/save", "POST /result/update/{}", "POST /result/delete/{}");

        // Records, fees and accounts: admins only, viewing fees included
        allow(Access.ADMIN,
                "GET /admin/users", "POST /admin/users/{}/enabled", "POST /admin/users/{}/role",
                "POST /admin/users/{}/link", "POST /admin/users/{}/unlink",
                "GET /fee", "GET /fee/search", "GET /fee/view/{}", "GET /fee/{}/receipt.pdf", "GET /fee/export",
                "GET /fee/new", "POST /fee/save", "GET /fee/edit/{}", "POST /fee/update/{}", "POST /fee/delete/{}",
                "GET /student/new", "GET /student/edit/{}", "POST /student", "POST /student/delete/{}",
                "GET /student/import", "POST /student/import", "GET /student/export",
                "GET /teacher/new", "GET /teacher/edit/{}", "POST /teacher", "POST /teacher/delete/{}",
                "GET /course/new", "GET /course/edit/{}", "POST /course", "POST /course/delete/{}",
                "POST /course/{}/subjects", "POST /course/{}/subjects/{}/remove",
                "GET /subject/new", "GET /subject/edit/{}", "POST /subject", "POST /subject/delete/{}",
                "GET /enrollment/new", "GET /enrollment/edit/{}", "POST /enrollment", "POST /enrollment/delete/{}");
    }

    private static void allow(Access access, String... routes) {
        for (String route : routes) {
            MATRIX.put(route, access);
        }
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping mappings;

    @Test
    void everyRouteHasALineInTheMatrix() {
        Set<String> mapped = new TreeSet<>();

        for (Map.Entry<RequestMappingInfo, org.springframework.web.method.HandlerMethod> entry
                : mappings.getHandlerMethods().entrySet()) {

            // The app's own controllers, not Spring Boot's /error
            if (!entry.getValue().getBeanType().getPackageName().startsWith("com.anurag.sms")) {
                continue;
            }
            RequestMappingInfo info = entry.getKey();
            for (var method : info.getMethodsCondition().getMethods()) {
                for (String path : info.getPatternValues()) {
                    mapped.add(method.name() + " " + path.replaceAll("\\{[^}]+}", "{}"));
                }
            }
        }

        assertThat(mapped).as("routes the controllers map").containsExactlyInAnyOrderElementsOf(MATRIX.keySet());
        assertThat(MATRIX).hasSize(92);
    }

    @Test
    void eachRoleReachesWhatTheMatrixAllowsAndIsRefusedTheRest() throws Exception {
        SoftAssertions softly = new SoftAssertions();

        for (String role : new String[] {"ADMIN", "TEACHER", "STUDENT"}) {
            for (Map.Entry<String, Access> line : MATRIX.entrySet()) {

                Outcome outcome = send(line.getKey(), user("route_" + role.toLowerCase()).roles(role));

                if (allows(line.getValue(), role)) {
                    softly.assertThat(outcome.letThrough())
                            .as("%s should reach %s, got %s", role, line.getKey(), outcome).isTrue();
                } else {
                    softly.assertThat(outcome.status())
                            .as("%s should get 403 on %s", role, line.getKey()).isEqualTo(403);
                }
            }
        }

        softly.assertAll();
    }

    @Test
    void signedOutVisitorsAreSentToTheLoginPage() throws Exception {
        SoftAssertions softly = new SoftAssertions();

        for (Map.Entry<String, Access> line : MATRIX.entrySet()) {

            Outcome outcome = send(line.getKey(), null);

            if (line.getValue() == Access.PUBLIC) {
                softly.assertThat(outcome.letThrough())
                        .as("anyone should reach %s, got %s", line.getKey(), outcome).isTrue();
            } else {
                softly.assertThat(outcome.sentToLogin())
                        .as("%s should send a visitor to sign in, got %s", line.getKey(), outcome).isTrue();
            }
        }

        softly.assertAll();
    }

    private static boolean allows(Access access, String role) {
        return switch (access) {
            case PUBLIC, SIGNED_IN -> true;
            case STAFF -> role.equals("ADMIN") || role.equals("TEACHER");
            case ADMIN -> role.equals("ADMIN");
        };
    }

    // status -1: the handler threw an exception, see send()
    record Outcome(int status, String redirect) {

        boolean sentToLogin() {
            return redirect != null && redirect.endsWith("/login");
        }

        boolean letThrough() {
            return status != 403 && !sentToLogin();
        }
    }

    // Ids are 1 and the forms are empty, so an allowed request usually ends
    // in 404, 400 or a form with errors. All of that is fine here: only 403
    // and the login redirect mean security turned the request away.
    private Outcome send(String route, RequestPostProcessor who) throws Exception {

        String[] parts = route.split(" ");
        String url = parts[1].replace("{}", "1");

        MockHttpServletRequestBuilder request = parts[0].equals("GET") ? get(url) : post(url).with(csrf());
        if (who != null) {
            request.with(who);
        }

        try {
            MvcResult result = mvc.perform(request).andReturn();
            return new Outcome(result.getResponse().getStatus(), result.getResponse().getRedirectedUrl());
        } catch (ServletException e) {
            // The handler failed on the missing data, e.g. /profile for a
            // made-up user. Spring Security decides before any handler
            // runs, so the request was let through.
            return new Outcome(-1, null);
        }
    }
}
