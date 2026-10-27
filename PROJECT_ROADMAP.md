<div align="center">

<img src="docs/banner.svg" alt="Student Management System" width="100%"/>

<br/>

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.16-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

<br/>

[![CI](https://github.com/anurag-joshi-1403/StudentManagementSystem/actions/workflows/ci.yml/badge.svg)](https://github.com/anurag-joshi-1403/StudentManagementSystem/actions/workflows/ci.yml)
![Tests](https://img.shields.io/badge/Tests-117_passing-brightgreen?style=flat-square&logo=junit5&logoColor=white)
![Roles](https://img.shields.io/badge/Roles-3-red?style=flat-square)
![Endpoints](https://img.shields.io/badge/Endpoints-92-blueviolet?style=flat-square)
![Entities](https://img.shields.io/badge/JPA_Entities-11-orange?style=flat-square)
![Templates](https://img.shields.io/badge/Thymeleaf_Templates-57-green?style=flat-square)
![LOC](https://img.shields.io/badge/Java_LOC-8.7k-yellow?style=flat-square)
![Progress](https://img.shields.io/badge/Phases_0–4-complete-success?style=flat-square)
[![Next](https://img.shields.io/badge/Next-Roadmap_v2_·_99_tasks-blueviolet?style=flat-square)](#-roadmap-v2)

### 🎓 A full-stack academic ERP for managing students, faculty, academics and finance.

**Project Roadmap & Engineering Plan** · *Last verified against source: 11 Oct 2026*

</div>

---

## 📑 Table of Contents

| | Section | | Section |
|---|---|---|---|
| 🎯 | [Project Overview](#-project-overview) | 🗺️ | [Development Roadmap](#️-development-roadmap) |
| 🧰 | [Tech Stack](#-tech-stack) | 🌟 | [**Roadmap v2**](#-roadmap-v2) |
| 🏛️ | [System Architecture](#️-system-architecture) | 🧪 | [Engineering Backlog](#-engineering-backlog) |
| 🗃️ | [Database Design](#️-database-design) | 🚀 | [Getting Started](#-getting-started) |
| 🔄 | [Request Lifecycle](#-request-lifecycle) | 🔮 | [Future Scope](#-future-scope) |
| ✅ | [Feature Matrix](#-feature-matrix) | 🎓 | [What This Project Demonstrates](#-what-this-project-demonstrates) |
| | | 👤 | [Author](#-author) |

---

## 🎯 Project Overview

> A **Spring Boot MVC** web application that digitises the day-to-day administration of an educational
> institution — from student records and faculty management through to enrollment, attendance,
> examinations, results and fee collection.

<table>
<tr>
<td width="50%" valign="top">

**📦 What's inside**

- 🔐 BCrypt login with **Admin / Teacher / Student** roles and user admin
- 👨‍🎓 Student & faculty records with validated photo upload
- 📚 Course and subject catalogue with unique codes
- 📝 Enrollment linking students ↔ courses ↔ subjects
- 🗓️ Attendance, daily or **bulk** for a whole class, with a 75% report
- 💰 Fees with printable receipts, **PDF** and overdue flags
- 🧾 Exam schedule, results with pass rates
- 🏆 **Automatic grading** and a marksheet with **PDF**
- 📥 CSV import and export · 🔔 live notifications · 📰 activity log
- 🗄️ Flyway migrations and a 🌱 one-command demo college
- 🧪 117 tests, every route checked against every role, and GitHub Actions CI

</td>
<td width="50%" valign="top">

**📐 By the numbers**

| Metric | Count |
|---|:---:|
| Record modules + auth | `9` + `1` |
| HTTP endpoints | `92` |
| JPA entities | `11` |
| Repositories | `11` |
| Service interfaces / impls | `14` / `14` |
| Thymeleaf templates | `57` |
| Automated tests | `117` |
| Lines of Java | `8,704` |

</td>
</tr>
</table>

---

## 🧰 Tech Stack

<div align="center">

| Layer | Technology | Purpose |
|:---:|---|---|
| ☕ **Language** | `Java 21` | Modern LTS runtime |
| 🍃 **Framework** | `Spring Boot 3.5.16` | Application backbone, auto-configuration |
| 🔐 **Security** | `Spring Security 6` | Authentication, BCrypt, session management |
| 🗄️ **Persistence** | `Spring Data JPA` + `Hibernate` | ORM and derived query methods |
| 🐬 **Database** | `MySQL 8.0` | Relational data store (`sms_web`) |
| 🎨 **View Layer** | `Thymeleaf` + `Bootstrap 5` | Server-rendered UI with fragment composition |
| ✔️ **Validation** | `Jakarta Bean Validation` | Declarative constraint enforcement, forms and CSV rows |
| 📄 **Documents** | `OpenPDF 3` · `Apache Commons CSV` | Marksheet and receipt PDFs · CSV import and export |
| 🧪 **Testing** | `JUnit 5` · `Mockito` · `H2` | Unit, repository and security tests without MySQL |
| ⚙️ **CI** | `GitHub Actions` | Runs the tests on every push |
| 🛠️ **Build** | `Maven` | Dependency management (wrapper included) |

</div>

---

## 🏛️ System Architecture

A textbook **layered MVC architecture** — each layer talks only to the one beneath it, keeping
persistence concerns out of the controllers and HTTP concerns out of the services.

```mermaid
flowchart TD
    subgraph CL["🌐 CLIENT LAYER"]
        BR["Browser<br/><i>Bootstrap 5 · Responsive UI</i>"]
    end

    subgraph SEC["🔐 SECURITY LAYER"]
        SF["Spring Security Filter Chain<br/><i>BCrypt · Roles · CSRF · Session</i>"]
    end

    subgraph WEB["🎯 PRESENTATION LAYER"]
        CT["14 Controllers<br/><i>92 endpoints · PDF & CSV downloads</i>"]
        TH["Thymeleaf Engine<br/><i>57 templates · fragment composition</i>"]
    end

    subgraph BIZ["⚙️ BUSINESS LAYER"]
        SV["13 Service Interfaces<br/>+ 13 Implementations"]
        RL["Domain Rules<br/><i>GradeCalculator · cascade delete · notifications</i>"]
    end

    subgraph DAT["🗄️ PERSISTENCE LAYER"]
        RP["11 JPA Repositories<br/><i>derived queries · @Modifying JPQL · paging</i>"]
        HB["Hibernate ORM"]
    end

    subgraph DB["🐬 DATABASE"]
        MY[("MySQL<br/>sms_web")]
        FS["File Storage<br/><i>/uploads</i>"]
    end

    BR -->|HTTP| SF
    SF -->|allowed for the role| CT
    CT --> SV
    CT -.->|render| TH
    TH -.->|HTML| BR
    SV --> RL
    SV --> RP
    RP --> HB
    HB --> MY
    CT -.->|multipart| FS

    style CL fill:#1e3a8a,stroke:#3b82f6,color:#fff
    style SEC fill:#7f1d1d,stroke:#ef4444,color:#fff
    style WEB fill:#065f46,stroke:#10b981,color:#fff
    style BIZ fill:#78350f,stroke:#f59e0b,color:#fff
    style DAT fill:#4c1d95,stroke:#8b5cf6,color:#fff
    style DB fill:#164e63,stroke:#06b6d4,color:#fff
```

---

## 🗃️ Database Design

Eleven entities and a join table with **fourteen foreign keys**, modelling the full academic lifecycle from
admission through to results, plus a standalone activity log. The schema is versioned with **Flyway**
(V1 to V7) since v2 Stage G.

```mermaid
erDiagram
    STUDENT ||--o{ ENROLLMENT : "registers"
    STUDENT ||--o{ ATTENDANCE : "marked in"
    STUDENT ||--o{ FEE : "pays"
    STUDENT ||--o{ RESULT : "scores"
    COURSE  ||--o{ ENROLLMENT : "offered as"
    SUBJECT ||--o{ ENROLLMENT : "included in"
    SUBJECT ||--o{ ATTENDANCE : "tracked for"
    SUBJECT ||--o{ EXAM : "assessed by"
    EXAM    ||--o{ RESULT : "produces"
    COURSE  ||--o{ STUDENT : "admits"
    COURSE  }o--o{ SUBJECT : "teaches"
    USER    |o--o| STUDENT : "belongs to"
    USER    |o--o| TEACHER : "belongs to"

    USER {
        Long id PK
        String username UK
        String email UK
        String password "BCrypt hashed"
        String role
        boolean enabled
        LocalDateTime createdAt
        Long student_id FK "optional"
        Long teacher_id FK "optional"
    }
    ACTIVITY_LOG {
        Long id PK
        String action
        String entityType
        String description
        String username
        LocalDateTime createdAt
    }
    STUDENT {
        Long id PK
        String firstName
        String lastName
        String email UK
        String phone "10 digits"
        String gender
        Long course_id FK
        LocalDate dateOfBirth
        String address
        String photo
    }
    TEACHER {
        Long id PK
        String firstName
        String lastName
        String email UK
        String phone
        String department
        String qualification
        String gender
        String photo
    }
    COURSE {
        Long id PK
        String courseCode UK
        String courseName
        String duration
        Double fees
        String description
    }
    SUBJECT {
        Long id PK
        String subjectCode UK
        String subjectName
        String semester
        Integer credits
        String department
    }
    ENROLLMENT {
        Long id PK
        Long student_id FK
        Long course_id FK
        Long subject_id FK
        LocalDate enrollmentDate
    }
    ATTENDANCE {
        Long id PK
        Long student_id FK
        Long subject_id FK
        LocalDate attendanceDate
        String status
        String remarks
    }
    FEE {
        Long id PK
        Long student_id FK
        String feeType
        BigDecimal amount
        LocalDate dueDate
        LocalDate paymentDate
        String paymentStatus
        String paymentMethod
    }
    EXAM {
        Long id PK
        String examName
        Long subject_id FK
        LocalDate examDate
        Integer totalMarks
        Integer passingMarks
    }
    RESULT {
        Long id PK
        Long student_id FK
        Long exam_id FK
        Integer obtainedMarks
        String grade "auto-computed"
        String resultStatus "auto-computed"
    }
```

> 💡 **Referential integrity is handled in the service layer.** Deleting a Student transactionally clears
> its Attendance, Enrollment, Fee and Result rows before removing the parent — no orphaned foreign keys.
> Subject, Course and Exam deletes clear their own dependent rows the same way, a course that still has
> students is refused, and `DeleteCascadeTest` checks every path. Codes, emails, enrollments (student,
> course, subject) and attendance (student, subject, day) are unique in the database.

---

## 🔄 Request Lifecycle

How a single request — creating a student with a photo — travels through the stack:

```mermaid
sequenceDiagram
    autonumber
    actor U as 👤 User
    participant SF as 🔐 Security Filter
    participant C as 🎯 StudentController
    participant V as ✔️ Bean Validator
    participant S as ⚙️ StudentService
    participant R as 🗄️ StudentRepository
    participant DB as 🐬 MySQL
    participant FS as 📁 File Storage

    U->>SF: POST /student (form + photo)
    SF->>SF: Verify session, ADMIN role & CSRF token
    SF->>C: Forward the request
    C->>V: @Valid Student
    alt ❌ Validation fails
        V-->>C: BindingResult errors
        C-->>U: Re-render form with messages
    else ✅ Validation passes
        V-->>C: Object is valid
        C->>S: existsByEmail(email)
        S->>R: Derived query
        R->>DB: SELECT ... WHERE email = ?
        DB-->>R: Result set
        R-->>S: boolean
        S-->>C: Duplicate check outcome
        C->>FS: Decode as JPEG/PNG, store under a generated name
        FS-->>C: Saved filename
        C->>S: saveStudent(student)
        S->>R: save(entity)
        R->>DB: INSERT INTO students
        DB-->>R: Generated ID
        R-->>S: Persisted entity
        S-->>C: Persisted entity
        C-->>U: 302 Redirect → /student
    end
```

---

## ✅ Feature Matrix

<div align="center">

| # | Module | Backend | UI | Highlights | Status |
|:---:|---|:---:|:---:|---|:---:|
| 1 | 🔐 **Authentication** | ✅ | ✅ | BCrypt · 3 roles · seeded admin · profile & password · user admin | `████████████` **100%** |
| 2 | 👨‍🎓 **Student** | ✅ | ✅ | CRUD · paged search · photo · marksheet + PDF · attendance % · CSV | `████████████` **100%** |
| 3 | 👨‍🏫 **Teacher** | ✅ | ✅ | CRUD · 4-field search · photo · unique email | `████████████` **100%** |
| 4 | 📚 **Course** | ✅ | ✅ | CRUD · detail · unique code | `████████████` **100%** |
| 5 | 📖 **Subject** | ✅ | ✅ | CRUD · detail with its exams · unique code | `████████████` **100%** |
| 6 | 📝 **Enrollment** | ✅ | ✅ | 3-way mapping · detail · no duplicates | `████████████` **100%** |
| 7 | 💰 **Fee** | ✅ | ✅ | Printable receipt · PDF · overdue flags · CSV export | `████████████` **100%** |
| 8 | 🗓️ **Attendance** | ✅ | ✅ | Keyword + date filter · bulk entry · 75% report | `████████████` **100%** |
| 9 | 🧾 **Exam** | ✅ | ✅ | Schedule by month · results with pass rate | `████████████` **100%** |
| 10 | 🏆 **Result** | ✅ | ✅ | **Auto grade + pass/fail engine** · marks ≤ total | `████████████` **100%** |
| 11 | 📊 **Dashboard** | ✅ | ✅ | Real-data stat cards · Chart.js · live notifications · recent activity | `████████████` **100%** |

</div>

### 🏅 Engineering highlights already shipped

<table>
<tr>
<td width="33%" valign="top">

**🔐 Security Foundation**

`SecurityConfig` with BCrypt, a custom `UserDetailsService`, Admin / Teacher / Student URL rules, POST-only changes with CSRF, and `sec:authorize` in every template.

</td>
<td width="33%" valign="top">

**🔗 Transactional Integrity**

Cascade deletes using `@Transactional` + `@Modifying` bulk JPQL, so removing a parent record never leaves orphaned foreign keys behind.

</td>
<td width="33%" valign="top">

**🧮 Business Logic Engine**

`GradeCalculator` derives pass/fail from each exam's own threshold and a six-band grade that can't contradict it; the marksheet reuses it for the overall grade.

</td>
</tr>
<tr>
<td valign="top">

**🎨 Component-Based UI**

Every page renders through `layout.html`, composing `sidebar`, `navbar`, `footer` and `notification` fragments, with 11 hand-written CSS files and print styles.

</td>
<td valign="top">

**🔍 Search & Pagination**

One paged query per module: multi-field `ContainingIgnoreCase` search or an AND-semantics `@Query`, 5 per page, sorted so rows can't shift.

</td>
<td valign="top">

**📤 File Upload Pipeline**

Photos decoded as real JPEG/PNG, capped at 2 MB, stored under generated names, and served through a `WebMvcConfigurer` handler behind login.

</td>
</tr>
</table>

---

## 🗺️ Development Roadmap

<div align="center">

```mermaid
gantt
    title Roadmap as delivered
    dateFormat YYYY-MM-DD
    axisFormat %b %d

    section ✅ Phase 0 · Stabilise
    Repository query fixes          :done, p0, 2026-09-21, 1d
    Dashboard charts                :done, p0b, 2026-09-21, 1d

    section ✅ Quick wins
    Search, logout, deletes, dates  :done, qw, 2026-10-06, 1d

    section ✅ Phase 1 · Unify UI
    Shared layout & error pages     :done, p1, 2026-10-07, 1d

    section ✅ Phase 2 · Secure
    Roles, POST deletes, uploads    :done, p2, 2026-10-08, 1d

    section ✅ Phase 3 · Complete
    Correctness & paging            :done, p3a, 2026-10-08, 1d
    Detail views, reports, CSV      :done, p3b, 2026-10-08, 1d

    section ✅ Phase 4 · Showcase
    Notifications, PDF, profile     :done, p4a, 2026-10-08, 1d
    Tests, CI, screenshots, docs    :done, p4b, 2026-10-09, 1d
```

</div>

### ✅ Phase 0 — Stabilise the Build · **COMPLETE**

> **Completed 21 Sep 2026** · *Two repository defects were aborting application startup.*

Both defects lived in one file — [`EnrollmentRepository.java`](Student-Management-System-web/src/main/java/com/anurag/sms/repository/EnrollmentRepository.java)

**1️⃣ Invalid derived query** — *resolved*

`findTop5ByOrderByAttendanceDateDesc()` declared `List<Attendance>` on an **Enrollment** repository and
sorted by a property `Enrollment` doesn't own. Spring Data validates method names eagerly at bootstrap, so
this one unused method aborted the entire application context. Removed, along with its now-unused import.

**2️⃣ JPQL targeting the wrong entity** — *resolved*

```diff
- @Query("DELETE FROM Attendance a WHERE a.subject.id = :subjectId")
+ @Query("DELETE FROM Enrollment e WHERE e.subject.id = :subjectId")
```

As written, subject deletion cleared attendance twice and never cleared enrollments — a guaranteed
foreign-key violation.

**✔️ Verified** — `mvnw clean test` → `Tests run: 1, Failures: 0, Errors: 0` · application boots on `:8080`
with 68 request mappings registered · `/login` returns `200` · unauthenticated `/student` correctly
redirects with `302`.

### ✅ Phase 1 — Make It Feel Like One Application · **COMPLETE**

> **Completed 7 Oct 2026** ([Task Board](TASKS.md) Stage B) · *The single biggest visual payoff available.*

Only `DashboardController` used to render through `layout/layout.html`, so clicking **Students** from the
dashboard made the whole sidebar and navbar disappear. Every page now goes through `LayoutView`.

- [x] 🎨 **Adopt the global layout everywhere** — each view is a `th:fragment="content"` rendered into
      `"layout/layout"`, all 9 modules included.
- [x] 📌 **Pin a single Bootstrap version** — `5.3.8` everywhere.
- [x] 👤 **Bind the navbar to the real session user** via `sec:authentication="name"` instead of the
      hardcoded *"Admin User"*.
- [x] 🧭 **Highlight the active sidebar link** — `class="active"` is currently pinned to Dashboard.
- [x] 🛡️ **Add `@ControllerAdvice`** plus styled `403`, `404`, `409`, `413` and `500` pages, replacing Whitelabel error output.

**✔️ Definition of done** — the shell survives every navigation · bad IDs render a designed 404.

### ✅ Phase 2 — Security & Data Integrity · **COMPLETE** *(one chore deferred)*

> **Completed 8 Oct 2026** (Stage C) · *What turns a demo into something defensible in an interview.*

- [x] 🔑 **Role-based access control** — `ADMIN` / `TEACHER` / `STUDENT` with `.hasRole(...)` route rules,
      `sec:authorize` on every button, and the first admin seeded from environment variables.
- [x] 🚫 **Convert deletes from GET to POST** — all 9 lists use POST forms with the CSRF token; a GET
      delete gets `405`.
- [x] 📎 **Harden file upload** — images decoded server-side, 2 MB cap, generated names, all in
      `FileUploadUtil`; a replaced photo is deleted only after the save succeeds.
- [x] 🔒 **Externalise database credentials** to `${DB_PASSWORD}` *(6 Oct 2026)*
- [ ] 🔑 **Rotate the old password**, which is still in git history *(deferred: other local projects share the MySQL `root` account)*
- [x] 🔇 **Move `show-sql` and `DEBUG` logging** into an `application-dev.properties` profile.
- [x] 🧹 **Untrack `uploads/`** — in `.gitignore`, removed with `git rm --cached`.

**✔️ Definition of done** — a student account receives `403` on admin routes · disguised executables are
rejected on upload · no credentials in source control.

### ✅ Phase 3 — Complete the Feature Set · **COMPLETE**

> **Completed 8 Oct 2026** (Stages D and E)

<table>
<tr>
<td width="50%" valign="top">

**🔧 Correctness**

- [x] ~~`getTotalFees()` returns a record count, not a money sum~~ — `getTotalFeeAmount()` now sums `amount`
- [x] ~~`getUpcomingExams()` has no date filter~~ — now filters on `examDate >= today`
- [x] ~~Search bypasses pagination in all 9 modules~~ — one paged query per module
- [x] ~~Uniqueness only on create~~ — database constraints, checked on edit too
- [x] ~~Marks above the total, negative fees, "Pass · F"~~ — cross-field checks and one `GradeCalculator`
- [x] ~~Add `@Valid` to Exam and Result controllers~~ — both forms now re-render with field errors instead of a 500 page
- [x] ~~Replace the `LocalDate.of(1900,1,1)` null-date sentinel~~ — attendance and exam search use a nullable `@Query` that ANDs keyword and date

</td>
<td width="50%" valign="top">

**✨ New capability**

- [x] 📄 Detail views for the 6 remaining modules
- [x] 🎓 **Student marksheet** — all results, percentage, overall grade
- [x] 🧾 **Printable fee receipt** and overdue flags
- [x] 📈 **Attendance percentage** per student, plus a 75% report
- [x] 📅 **Exam schedule** by month
- [x] ⚡ **Bulk attendance entry** — mark an entire class in one form
- [x] 📥 **CSV import** — validated row by row, with a report
- [x] 📤 **CSV export** for student and fee lists

</td>
</tr>
</table>

### ✅ Phase 4 — Showcase Polish · **COMPLETE**

> **Completed 8–9 Oct 2026** (Stage F) · *The layer recruiters actually see first.*

- [x] 📊 **Chart.js dashboard** — start with the gender split; `maleStudents` and `femaleStudents` are
      **already computed and on the model**, just never rendered. Then fee collection trends and
      enrollments per course.
- [x] 🔔 **Live notifications** — overdue fees, students under 75% and exams this week, built from real data.
- [x] 📰 **Recent-activity feed** — an audit log of creates, updates, deletes and imports.
- [x] 🖨️ **PDF export** for marksheets and receipts (OpenPDF).
- [x] 👤 **User profile page** with change-password, and **admin user management**.
- [x] 🧪 **Test suite** — 55 tests: grade boundaries, cascade deletes, `@WebMvcTest` security, `@DataJpaTest` search.
- [x] ⚙️ **CI** — GitHub Actions runs the tests on every push.
- [x] 📸 **README with screenshots** and setup instructions.

> ➡️ **Next:** [Roadmap v2](#-roadmap-v2) takes the project from an admin tool to a campus platform in ten more stages.

---

## 🌟 Roadmap v2

> **v1 turned a failing prototype into a solid admin tool. v2 turns it into a campus platform.** Every role gets
> its own portal, the college's real processes (timetables, coursework, fees in instalments) are modelled, the
> system reaches people by email, exposes a documented API and uses AI where it saves a teacher time. All of it
> runs at a public URL.
>
> 📋 The step-by-step plan is the [Task Board](TASKS.md): **99 tasks in 10 stages (G–P), about 150 hours**.

### 🔭 Where v2 Takes the Project

| | Today (v1) | After v2 |
|:---:|---|---|
| 👥 **Users** | Admin staff; students and teachers browse the same pages | Three portals: students see only their own records, teachers only their own classes |
| 🗄️ **Schema** | `ddl-auto=update`, a student's course stored as text | Flyway migrations, real foreign keys, academic terms |
| 🎓 **Academics** | Exams, results, attendance | + timetable, assignments, leave, holidays, SGPA/CGPA, report cards, merit lists |
| 💰 **Fees** | One row per fee, paid or not | Structures, invoices, instalments, late fines, scholarships, online payment |
| 📣 **Reach** | An in-app bell | Email, scheduled reminders, an inbox, live updates, self-service password reset |
| 🔌 **Integration** | Web pages only | A JWT-secured REST API with OpenAPI docs |
| 🤖 **Intelligence** | Rule-based alerts | Explainable at-risk scores, plus Claude-drafted remarks, a student helpdesk and plain-English analytics |
| 🚢 **Delivery** | Runs locally; CI runs the tests | Docker, a live demo, MySQL integration tests, coverage, CodeQL, metrics, backups |

### 🗓️ Timeline

Planned at **about 12 hours a week from Monday 12 Oct 2026**. Each release is usable on its own, so the plan
can stop after any of them.

<div align="center">

```mermaid
gantt
    title Roadmap v2 at about 12 hours a week
    dateFormat YYYY-MM-DD
    axisFormat %d %b

    section Foundation
    G · Solid ground            :g, 2026-10-12, 7d
    H · Ship it                 :h, after g, 7d
    v1.2 Live demo              :milestone, m1, after h, 0d

    section Product
    I · Portals                 :i, after h, 9d
    J · Academics               :j, after i, 13d
    K · Finance                 :k, after j, 9d
    L · Communication           :l, after k, 8d
    M · Insight and documents   :m, after l, 9d

    section Platform
    N · REST API                :n, after m, 7d
    v2.0 Platform               :milestone, m2, after n, 0d
    O · AI assistant            :o, after n, 9d
    v2.1 AI                     :milestone, m3, after o, 0d
    P · Hardening               :p, after o, 12d
```

</div>

> 🎯 **Placement fast track.** If interviews come first, do **G → H → I1–I5 → O1, O2, O4** (~35 h, about three
> weeks). That gives a live URL, three logins that each show a different portal, and a safe AI helpdesk.

### 📦 Release Plan

#### 🧱 `v1.1` Solid Ground · Stage G · ~11.5 h · ✅ shipped 11 Oct 2026

*Close the backlog and make the schema safe to change.*

- **Ships:** Flyway migrations · `Student → Course` and `Subject ↔ Course` foreign keys · accounts linked to students and teachers · instant sign-out on disable · a demo-data seeder · backlog #33–#37 closed
- **You'll learn:** versioned migrations and data backfills, method security with `@PreAuthorize`, Spring Security's session registry
- **📝 Résumé line:** *Introduced Flyway versioned migrations and moved free-text course data into real foreign keys without data loss.*

#### 🚀 `v1.2` Ship It · Stage H · ~10 h · 🎯 live demo

*A recruiter can try the app from a link in under a minute.*

- **Ships:** multi-stage Dockerfile · Compose with MySQL and Mailpit · Testcontainers integration tests · JaCoCo coverage gate · CodeQL and Dependabot · a cloud deployment with self-resetting demo accounts
- **You'll learn:** containers, 12-factor configuration, integration testing on a real database, CI-gated delivery
- **📝 Résumé line:** *Containerised and deployed a Spring Boot app with CI-gated delivery and Testcontainers-based MySQL integration tests.*

#### 🎒 `v1.3` Self-Service Portals · Stage I · ~15.5 h

*Three roles, three different homes, and no one sees another person's records.*

- **Ships:** student and teacher dashboards · "My" marksheet, attendance and fees · object-level authorization · a gradebook for a whole exam · self-service contact details
- **You'll learn:** object-level (ownership) authorization, preventing mass assignment with DTOs, security testing per role
- **📝 Résumé line:** *Designed role-specific portals with object-level authorization, verified by an automated route × role test matrix.*

#### 🏫 `v1.4` Academic Operations · Stage J · ~22 h

*The app models how a college actually runs a term.*

- **Ships:** academic terms · a weekly timetable with clash detection · announcements · assignments with PDF submissions · leave requests · a holiday calendar · SGPA/CGPA · report-card PDFs · merit lists
- **You'll learn:** interval-overlap queries, secure file handling beyond images, credit-weighted calculations, ranking with ties
- **📝 Résumé line:** *Built timetable scheduling with teacher and room clash detection, and credit-weighted SGPA/CGPA report cards.*

#### 💰 `v1.5` Finance · Stage K · ~14.5 h

*Fees behave like a small accounts module.*

- **Ships:** fee structures per course and term · bulk invoice generation · partial payments · capped late fines · scholarships · Razorpay test-mode checkout · collection and defaulter reports
- **You'll learn:** money with `BigDecimal`, payment-gateway integration, HMAC signature checks, idempotent webhooks
- **📝 Résumé line:** *Integrated Razorpay with server-side signature verification and idempotent webhooks for instalment-based fee payments.*

#### 📣 `v1.6` Communication & Automation · Stage L · ~13 h

*The system reaches people outside the browser.*

- **Ships:** templated HTML email · forgot/reset password · email verification · a notification inbox · domain events · scheduled reminders · per-user preferences · a live bell over Server-Sent Events
- **You'll learn:** event-driven design, `@Async` after commit, `@Scheduled` with ShedLock, secure token flows, SSE
- **📝 Résumé line:** *Decoupled notifications with domain events, adding scheduled email reminders and real-time updates over Server-Sent Events.*

#### 📊 `v1.7` Insight & Documents · Stage M · ~15 h

*The data turns into decisions and documents someone can check.*

- **Ships:** an analytics page · explainable at-risk scores · Excel export · QR-verified ID cards and certificates · QR attendance · a Ctrl+K command palette
- **You'll learn:** aggregate queries, explainable scoring, streaming Excel generation, QR codes, tamper-evident documents
- **📝 Résumé line:** *Added an explainable at-risk student score and QR-verifiable ID cards and certificates.*

#### 🔌 `v2.0` REST API · Stage N · ~12.5 h · 🎯 platform

*Everything the web app does is available as a documented API.*

- **Ships:** `/api/v1` resources with DTOs · RFC 9457 problem details · JWT authentication on a second filter chain · OpenAPI and Swagger UI · rate limiting · a contract snapshot test · an API collection
- **You'll learn:** REST design, stateless security beside session security, API documentation, contract testing
- **📝 Résumé line:** *Exposed a JWT-secured, OpenAPI-documented REST API alongside the server-rendered app, with rate limiting and contract tests.*

#### 🤖 `v2.1` AI Assistant · Stage O · ~15 h · 🎯 AI

*AI that saves teachers time and stays safe: drafts a person approves, minimum data, no write access.*

- **Ships:** report-card remarks (structured output, batch generation) · a student helpdesk using tool calls limited to the signed-in student's own data · plain-English questions answered by read-only report tools · smart CSV column mapping · an evaluation set and a cost page
- **Built on:** the official Anthropic Java SDK with `claude-opus-5-5` by default (configurable), switched off unless an admin enables it and an API key is set
- **You'll learn:** LLM tool use, structured outputs, prompt-injection-resistant design, PII redaction, evaluations, cost tracking
- **📝 Résumé line:** *Integrated the Claude API for human-approved report remarks and a student helpdesk whose tools are limited to the signed-in user's data.*

#### 🔒 `v2.2` Production Hardening · Stage P · ~20.5 h

*The operational polish that separates a project from a product.*

- **Ships:** Prometheus and Grafana · structured JSON logs · CSP and security headers · login lockout and admin 2FA · change history with Envers · an N+1 query fix pass with a load test · English and Hindi · dark mode and accessibility · an installable PWA · tested backups
- **You'll learn:** observability, web security headers, TOTP, auditing, performance profiling, internationalisation, accessibility
- **📝 Résumé line:** *Hardened the app for production with metrics dashboards, a strict CSP, admin 2FA, audit history and a load-tested query pass.*

### 🧩 Keystone Tasks

Ten tasks unlock most of the plan. Getting these right early saves rework later.

```mermaid
flowchart LR
    G7["🗄️ G7<br/>Flyway"]
    G8["G8<br/>Course FK"]
    G9["G9<br/>Curriculum"]
    G10["👤 G10<br/>Account links"]
    G12["🌱 G12<br/>Demo data"]
    H7["🚀 H7<br/>Live deploy"]
    I1["I1<br/>Teaching assignments"]
    I5["🔐 I5<br/>Ownership rules"]
    J1["📅 J1<br/>Terms"]
    J2["J2<br/>Timetable"]
    K3["💳 K3<br/>Payments"]
    K6["K6<br/>Razorpay"]
    L5["📣 L5<br/>Domain events"]
    M1["M1<br/>Analytics"]
    N4["N4<br/>JWT API"]
    O2["🛡️ O2<br/>AI guardrails"]
    O4["O4<br/>AI helpdesk"]
    O5["O5<br/>Plain-English questions"]

    G7 --> G8
    G7 --> G9
    G7 --> G10
    G7 --> J1
    G12 --> H7
    G9 --> I1 --> J2
    G10 --> I5
    J1 --> J2
    J1 --> K3 --> K6
    K3 --> L5
    K3 --> M1 --> O5
    I5 --> N4
    I5 --> O4
    O2 --> O4
    O2 --> O5

    style G7 fill:#7f1d1d,stroke:#ef4444,color:#fff
    style G10 fill:#7f1d1d,stroke:#ef4444,color:#fff
    style G12 fill:#7f1d1d,stroke:#ef4444,color:#fff
    style I5 fill:#7c2d12,stroke:#f97316,color:#fff
    style J1 fill:#713f12,stroke:#eab308,color:#fff
    style K3 fill:#065f46,stroke:#10b981,color:#fff
    style L5 fill:#065f46,stroke:#10b981,color:#fff
    style H7 fill:#1e3a8a,stroke:#3b82f6,color:#fff
    style O2 fill:#4c1d95,stroke:#8b5cf6,color:#fff
```

### 🏛️ Target Architecture

```mermaid
flowchart TD
    subgraph CL["🌐 CLIENTS"]
        BR["Browser<br/><i>3 portals · installable PWA</i>"]
        AC["API clients<br/><i>Bruno · mobile · SPA</i>"]
    end

    subgraph APP["🍃 SPRING BOOT APP · Docker"]
        WEB["Session filter chain<br/><i>form login · CSRF · admin 2FA</i>"]
        API["JWT filter chain<br/><i>/api/v1 · rate limits</i>"]
        CT["MVC + REST controllers"]
        SV["Services<br/><i>@access ownership rules</i>"]
        EV["Domain events<br/><i>after commit · @Async</i>"]
        JOB["Scheduler<br/><i>reminders · ShedLock</i>"]
        AI["AiService<br/><i>redaction · read-only tools</i>"]
        MET["Actuator metrics"]
    end

    subgraph DATA["🗄️ DATA"]
        MY[("MySQL 8.4<br/><i>Flyway-managed</i>")]
        FS["Uploads volume<br/><i>photos · submissions</i>"]
    end

    subgraph EXT["☁️ OPTIONAL SERVICES · each has an on/off switch"]
        SMTP["SMTP<br/><i>Mailpit locally</i>"]
        RZP["Razorpay<br/><i>test mode</i>"]
        CLAUDE["Claude API"]
    end

    PROM["📈 Prometheus + Grafana"]

    BR --> WEB --> CT
    AC --> API --> CT
    CT --> SV
    SV --> MY
    SV --> FS
    SV --> EV --> SMTP
    JOB --> SV
    SV --> AI --> CLAUDE
    SV --> RZP
    RZP -.->|signed webhook| CT
    PROM -.->|scrapes| MET

    style CL fill:#1e3a8a,stroke:#3b82f6,color:#fff
    style APP fill:#065f46,stroke:#10b981,color:#fff
    style DATA fill:#164e63,stroke:#06b6d4,color:#fff
    style EXT fill:#4c1d95,stroke:#8b5cf6,color:#fff
```

### 🗃️ Data Model Additions

Roughly 20 new tables, all created by Flyway migrations. Existing entities are shown only where a new
relationship attaches to them.

```mermaid
erDiagram
    USER |o--o| STUDENT : "linked to (G10)"
    USER |o--o| TEACHER : "linked to (G10)"
    COURSE ||--o{ STUDENT : "admits (G8)"
    COURSE }o--o{ SUBJECT : "curriculum (G9)"
    TEACHER ||--o{ TEACHING_ASSIGNMENT : "teaches"
    SUBJECT ||--o{ TEACHING_ASSIGNMENT : "taught by"
    ACADEMIC_TERM ||--o{ TIMETABLE_SLOT : "schedules"
    TEACHING_ASSIGNMENT ||--o{ TIMETABLE_SLOT : "held as"
    SUBJECT ||--o{ ASSIGNMENT : "sets"
    ASSIGNMENT ||--o{ SUBMISSION : "receives"
    STUDENT ||--o{ SUBMISSION : "uploads"
    STUDENT ||--o{ LEAVE_REQUEST : "applies for"
    COURSE ||--o{ FEE_STRUCTURE : "priced by"
    ACADEMIC_TERM ||--o{ FEE_STRUCTURE : "per term"
    FEE_STRUCTURE ||--|{ FEE_COMPONENT : "made of"
    FEE ||--o{ PAYMENT : "settled by"
    STUDENT ||--o{ CONCESSION : "granted"
    USER ||--o{ NOTIFICATION : "receives"
    USER ||--o{ PASSWORD_RESET_TOKEN : "requests"

    PAYMENT {
        Long id PK
        Long fee_id FK
        BigDecimal amount
        LocalDate paidOn
        String method
        String reference UK "gateway id, idempotency"
    }
    TIMETABLE_SLOT {
        Long id PK
        Long term_id FK
        Long teaching_assignment_id FK
        String dayOfWeek
        LocalTime startTime
        LocalTime endTime
        String room
    }
    PASSWORD_RESET_TOKEN {
        Long id PK
        Long user_id FK
        String tokenHash "SHA-256, never the token"
        LocalDateTime expiresAt
        LocalDateTime usedAt
    }
```

<sub>Also new, with no foreign keys worth drawing: `ANNOUNCEMENT`, `HOLIDAY`, `DOCUMENT_VERIFICATION`, `AI_USAGE` and ShedLock's `shedlock` table.</sub>

### 📐 Prioritisation

Where the headline features sit on impact for a reviewer against build effort. The fast track takes the top-left
quadrant first.

<div align="center">

```mermaid
quadrantChart
    title Impact against effort
    x-axis Low effort --> High effort
    y-axis Low impact --> High impact
    quadrant-1 Big bets
    quadrant-2 Quick wins
    quadrant-3 Nice touches
    quadrant-4 Only if time allows
    Live demo: [0.25, 0.95]
    Flyway: [0.2, 0.7]
    Student portal: [0.4, 0.88]
    AI helpdesk: [0.58, 0.85]
    REST API with JWT: [0.62, 0.74]
    Razorpay: [0.6, 0.66]
    Timetable: [0.66, 0.58]
    Password reset: [0.35, 0.56]
    Analytics: [0.45, 0.62]
    Excel export: [0.2, 0.35]
    Dark mode: [0.28, 0.3]
    Change history: [0.3, 0.42]
    Admin 2FA: [0.56, 0.4]
    QR attendance: [0.64, 0.34]
    Hindi UI: [0.7, 0.28]
```

</div>

### 🎓 What v2 Adds to the Résumé

| Competency | Evidence after v2 |
|---|---|
| 🗄️ **Schema evolution** | Flyway migrations with data backfills (G7–G9, K3) |
| 🐳 **Containers & delivery** | Docker, Compose, CI-gated deploys, a live demo (Stage H) |
| 🧪 **Integration testing** | Testcontainers on MySQL 8.4, GreenMail, an OpenAPI contract snapshot (H3, L9, N7) |
| 🔐 **Advanced security** | Object-level authorization, JWT, rate limits, CSP, login lockout, TOTP 2FA (I5, N4, N6, P3–P5) |
| 💳 **Payments** | Gateway orders, HMAC signature checks, idempotent webhooks (K6) |
| ⚙️ **Async & scheduling** | Domain events, `@Async` after commit, `@Scheduled` with ShedLock, SSE (Stage L) |
| 📊 **Data & reporting** | Aggregate analytics, ranks with ties, SGPA/CGPA, streaming Excel (J11–J13, M1–M3) |
| 🤖 **Applied AI** | Tool use scoped to the user, structured outputs, batches, PII redaction, evaluations, cost tracking (Stage O) |
| 📈 **Observability** | Prometheus, Grafana, structured logs with request ids (P1, P2) |
| ♿ **Product polish** | Hindi, dark mode, accessibility score ≥ 95, installable PWA (P8–P10) |

### 📏 Engineering Principles for v2

1. **Every release ships.** `main` stays green and deployable; a half-built feature stays on its branch.
2. **The schema changes only through Flyway.** A merged migration is never edited.
3. **Authorization is tested, not assumed.** Every route, every role, and every "own records only" rule has a test.
4. **Outside services are optional.** Mail, payments and AI each have an on/off switch and a fake for tests, so the app runs with none of them set up.
5. **Personal data stays home.** Nothing personal leaves the app without a documented reason (`docs/ai-policy.md`).
6. **Decisions are written down.** Each major choice gets a short ADR in `docs/adr/`.

### ⚠️ Risks and Mitigations

| Risk | Mitigation |
|---|---|
| ⏳ **150 hours competes with studies and interviews** | Fast track first (~35 h); each release stands alone, so stopping early still leaves a finished product |
| 🗄️ **A migration damages data** (G8, K3) | Back up before migrating, run migrations on Testcontainers MySQL (H3) before production, keep merged migrations immutable |
| 🌐 **Free hosting sleeps or changes its limits** | Say so in the README, keep `docker compose up` as the offline fallback, reset the demo nightly |
| 💳 **Payment fraud or double charges** | Test mode only, signatures checked on the server, payment ids recorded once, no card data touches the app |
| 🤖 **AI cost, privacy or wrong answers** | Off by default, allow-listed fields only, a person approves every draft, read-only tools, an evaluation set and a cost page |
| 🧩 **Scope creep inside a stage** | One task, one commit; anything new goes into the backlog, not into the task being worked on |

---

## 🧪 Engineering Backlog

> Every non-trivial codebase carries debt. What matters professionally is whether it's **tracked and
> triaged** — this register is maintained deliberately, with file references and an owning phase for each item.
> Issues found after 21 Sep (#23 onward, now up to #47) are tracked in [project_analysis.md](project_analysis.md#-engineering-backlog).
> Of this register's 22 items, **21 are resolved**; only the password rotation (#5) is left.

<details>
<summary><b>✅ Resolved (21 items)</b></summary>

<br/>

| # | Issue | Location | Resolved |
|:---:|---|---|:---:|
| 1 | Invalid derived query aborted Spring context at startup | `EnrollmentRepository.java:22` | `21 Sep 2026` |
| 2 | `deleteBySubjectId` targeted the wrong entity → FK violation | `EnrollmentRepository.java:29-32` | `21 Sep 2026` |
| 9 | "Total Fees" displayed a record count, not a currency sum | `FeeServiceImpl.java` | `21 Sep 2026` |
| 10 | "Upcoming Exams" listed the 5 oldest exams — no date filter | `ExamServiceImpl.java` | `21 Sep 2026` |
| 12 | Exam and Result forms lacked `@Valid`, so invalid input reached Hibernate and showed a 500 page | `ExamController.java` · `ResultController.java` | `6 Oct 2026` |
| 13 | Magic-date sentinel `1900-01-01` stood in for "no date" in search | `AttendanceServiceImpl.java` · `ExamServiceImpl.java` | `6 Oct 2026` |
| 14 | Navbar hardcoded "Admin User" regardless of session | `common/navbar.html` | `21 Sep 2026` |
| 16 | Gender statistics computed but never rendered | `stats-cards.html` · `charts.html` | `21 Sep 2026` |
| 21 | `footer.css` contained a copy of `footer.html` — footer rendered unstyled | `static/css/footer.css` | `21 Sep 2026` |
| 22 | `welcome.css` targeted markup that did not exist; live clock in `dashboard.js` had no elements to update | `welcome.css` · `welcome.html` | `21 Sep 2026` |
| 3 | No role enforcement — every user had full destructive access | Admin / Teacher / Student rules, seeded admin | `8 Oct 2026` |
| 4 | Deletes exposed as GET links — prefetch/CSRF exposure | POST forms on all 9 lists | `8 Oct 2026` |
| 6 | Only the dashboard used the shared layout | Every page through `LayoutView` | `7 Oct 2026` |
| 7 | File upload lacked type/size/filename validation; orphaned old files | `FileUploadUtil` | `8 Oct 2026` |
| 8 | No global exception handler — Whitelabel error pages | `GlobalExceptionHandler` + styled pages | `7 Oct 2026` |
| 11 | Search results weren't paginated | One paged query per module | `8 Oct 2026` |
| 15 | Notifications were three hardcoded placeholder items | `NotificationService` + `NotificationAdvice` | `8 Oct 2026` |
| 17 | Bootstrap version drift (`5.3.3` vs `5.3.8`) | `5.3.8` everywhere | `7 Oct 2026` |
| 18 | Six empty stub classes (3 DTOs + 3 utilities) | Implemented or deleted | `8 Oct 2026` |
| 19 | `uploads/` directory committed to version control | `.gitignore` + `git rm --cached` | `8 Oct 2026` |
| 20 | `webConfig` broke Java class naming convention | Renamed `WebConfig` | `7 Oct 2026` |

</details>

<details open>
<summary><b>🔴 Critical, partly done (1 item)</b></summary>

<br/>

| # | Issue | Location | Status |
|:---:|---|---|:---:|
| 5 | Database password committed in plaintext. Read from `${DB_PASSWORD}` since 6 Oct 2026; since 11 Oct the app signs in as its own `sms_app` user, limited to `sms_web` (G6). The leaked `root` password is still in git history and not yet rotated (task A4), which now only concerns other local projects | `application.properties` history | 🟡 App off `root` |

</details>

<div align="center">

```mermaid
pie showData
    title Issues 1–22 by status
    "✅ Resolved" : 21
    "🟡 Partly done" : 1
```

</div>

---

## 🚀 Getting Started

```bash
# 1️⃣  Clone the repository
git clone https://github.com/anurag-joshi-1403/StudentManagementSystem.git
cd StudentManagementSystem/Student-Management-System-web

# 2️⃣  Create the database
mysql -u root -p -e "CREATE DATABASE sms_web;"

# 3️⃣  Give the app its own MySQL user, limited to sms_web, and pass it in (never committed)
mysql -u root -p -e "CREATE USER 'sms_app'@'localhost' IDENTIFIED BY 'app_password';
                     GRANT ALL PRIVILEGES ON sms_web.* TO 'sms_app'@'localhost';"
export DB_USERNAME=sms_app
export DB_PASSWORD=app_password
export ADMIN_USERNAME=admin ADMIN_PASSWORD=choose_one   # first admin, created once at startup

# 4️⃣  Build and run
./mvnw clean install      # also runs the 117 tests, which need no MySQL
./mvnw spring-boot:run
```

<div align="center">

🌐 **`http://localhost:8080`** → sign in as the admin → dashboard

</div>

> ✅ **Verified working** — boots against MySQL 8.0 on Java 21+ with 92 endpoints; `./mvnw test` passes on H2.
> 🌱 Add `-Dspring-boot.run.profiles=demo` on an empty database for a sample college and three demo logins.

---

## 🔮 Future Scope

Most of v1's deliberately parked items are now scheduled in [Roadmap v2](#-roadmap-v2):

| Was parked | Now scheduled |
|---|---|
| 🗄️ Flyway / Liquibase migrations | **G7**, the first schema task, because v2 adds about 20 tables |
| 🔌 REST API + OpenAPI/Swagger | **Stage N** |
| 🐳 Docker | **H1–H2**, with Compose for MySQL and Mailpit |
| 🚢 Continuous delivery | **H7, H9**: deploys only after a green CI run |
| 📡 Monitoring & metrics | **P1–P2**: Prometheus, Grafana, structured logs |
| 📧 Email notifications · 🔑 forgot-password | **L1, L2, L6** |

What remains out of scope, and why:

| Area | Item | Rationale for deferring |
|:---:|---|---|
| ☸️ | Kubernetes or microservices | One app with Docker Compose fits this scale; splitting it adds network failure modes without a scaling need |
| 📱 | Native mobile app or SPA front end | Stage N builds the API either would use; a second front end is a separate product |
| 🏢 | Several institutions in one install | Multi-tenancy needs tenant isolation in every query, and there is no second institution to serve |
| 🔐 | Google/Microsoft single sign-on | Needs an OAuth app registration per institution; local accounts with 2FA (P5) cover the demo |
| ☁️ | Object storage for uploads | Matters only once the app runs as more than one instance |
| 🧠 | Machine-learning risk prediction | Explainable rules first (M2); a model needs a few terms of real outcomes to train and check against |

<details>
<summary><b>📐 Former schema debt (fixed in v2 Stage G, 11 Oct 2026)</b></summary>

<br/>

- ~~**`Student.course` is a `String`, not a foreign key** to `Course`~~ — **G8** moved it to `course_id`. The
  migration matched each student's text to a course name and stops with an error if any match is missing,
  so no student could silently lose their course.
- ~~**`Subject` has no relationship to `Course`**~~ — **G9** added the `course_subjects` curriculum, filled from
  the enrollments that already existed, and enrollments must now use it.

v1 recorded both rather than scheduling them, because each needed a data migration plus form and query
rework across several modules. Flyway (**G7**) provided the migration.

</details>

---

## 🎓 What This Project Demonstrates

<div align="center">

| Competency | Evidence in this codebase |
|---|---|
| 🏛️ **Layered architecture** | Strict controller → service → repository separation across 9 record modules |
| 🔗 **Relational modelling** | 11 entities and a join table, 14 foreign keys, 11 unique constraints, Flyway migrations with data backfills |
| 🔐 **Application security** | Three roles, URL rules plus `sec:authorize`, CSRF on every change, hardened uploads |
| 🗄️ **Data access** | Derived queries, `@Modifying` JPQL, `SELECT new` summaries, `Pageable` paging |
| ✔️ **Validation** | Jakarta constraints, cross-field checks, the same rules on every CSV row |
| 🎨 **Frontend composition** | Thymeleaf fragments, one layout, print styles, hand-authored responsive CSS |
| 🧮 **Business logic** | Grading, marksheets, attendance %, overdue fees, notifications from live data |
| 📄 **Documents** | PDF marksheets and receipts, Excel-safe CSV export, validated import |
| 🧪 **Testing & CI** | 117 tests across unit, `@DataJpaTest`, `@SpringBootTest` and `@WebMvcTest`, run by GitHub Actions |
| 🔍 **Code review skill** | A maintained 47-item backlog with severity triage, 44 resolved, and a [written audit](PROJECT_AUDIT_REPORT.md) |

</div>

---

## 👤 Author

<div align="center">

**Anurag Joshi**

[![GitHub](https://img.shields.io/badge/GitHub-anurag--joshi--1403-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/anurag-joshi-1403)

<sub>💼 Add your LinkedIn, portfolio and email badges here before sharing this repository.</sub>

<br/><br/>

⭐ *If this project is useful to you, consider starring the repository.*

<br/>

**📘 Companion document:** [project_analysis.md](project_analysis.md) — detailed module-by-module inventory

</div>
