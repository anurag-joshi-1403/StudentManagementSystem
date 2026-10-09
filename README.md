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
![Tests](https://img.shields.io/badge/Tests-55_passing-brightgreen?style=flat-square&logo=junit5&logoColor=white)
![Roles](https://img.shields.io/badge/Roles-Admin_·_Teacher_·_Student-red?style=flat-square)
![Endpoints](https://img.shields.io/badge/Endpoints-88-blueviolet?style=flat-square)
![Entities](https://img.shields.io/badge/JPA_Entities-11-orange?style=flat-square)
![Templates](https://img.shields.io/badge/Thymeleaf_Templates-57-green?style=flat-square)
![LOC](https://img.shields.io/badge/Java_LOC-7.4k-yellow?style=flat-square)

<br/>

### 🎓 A full-stack web app that runs the daily administration of a college — students, teachers, courses, attendance, exams, results and fees — from one dashboard.

<br/>

[📸 Screenshots](#-screenshots) · [🚀 Quick Start](#-getting-started) · [🧱 Architecture](#-architecture) · [💾 Database](#-database-schema) · [🧪 Tests](#-tests--ci) · [📖 Keywords Explained](#-keywords-explained) · [🗺️ Roadmap](PROJECT_ROADMAP.md)

</div>

---

## 📑 Table of Contents

| | Section | | Section |
|:---:|---|:---:|---|
| 🎯 | [About the Project](#-about-the-project) | 🧮 | [Grade Engine](#-grade-engine) |
| 📸 | [Screenshots](#-screenshots) | 🔗 | [Data Integrity](#-data-integrity) |
| ✨ | [Features](#-features) | 📁 | [Project Structure](#-project-structure) |
| 🧰 | [Tech Stack](#-tech-stack) | 🌐 | [Routes](#-routes) |
| 🧱 | [Architecture](#-architecture) | 🧪 | [Tests & CI](#-tests--ci) |
| 🧩 | [Anatomy of a Module](#-anatomy-of-a-module) | 🚀 | [Getting Started](#-getting-started) |
| 🧭 | [User Journey](#-user-journey) | 📖 | [Keywords Explained](#-keywords-explained) |
| 🔐 | [Login Flow](#-login-flow) & [Roles](#-roles) | 📊 | [Project Status](#-project-status) |
| 💾 | [Database Schema](#-database-schema) | 👤 | [Author](#-author) |

---

## 🎯 About the Project

> **Student Management System (SMS)** is a server-rendered **Spring Boot MVC** application.
> Admins, teachers and students sign in to a live dashboard and work with every academic record of
> the institution through nine record modules that share one layout, one role-based security layer
> and one database, plus marksheets, receipts, reports, CSV and PDF export.

<table>
<tr>
<td width="50%" valign="top">

**🧠 Why it exists**

Colleges still track admissions, attendance and fees across spreadsheets and paper. This project
puts all of it behind a single login with searchable, paginated screens, a dashboard that reads
real numbers from the database, and alerts for what needs attention: overdue fees, low attendance
and exams this week.

</td>
<td width="50%" valign="top">

**📐 By the numbers**

| Metric | Count |
|---|:---:|
| Record modules + auth | `9` + `1` |
| HTTP endpoints | `88` |
| JPA entities / repositories | `11` / `11` |
| Service interfaces / implementations | `13` / `13` |
| Thymeleaf templates | `57` |
| Automated tests | `55` |
| Lines of Java | `7.4k` |

</td>
</tr>
</table>

---

## 📸 Screenshots

<div align="center">

| 📊 Dashboard | 👨‍🎓 Student list |
|:---:|:---:|
| <img src="docs/screenshots/dashboard.png" alt="Dashboard with stat cards, charts and the notification bell" width="100%"/> | <img src="docs/screenshots/student-list.png" alt="Student list with search, photos and actions" width="100%"/> |
| **🎓 Marksheet** | **🧾 Fee receipt** |
| <img src="docs/screenshots/marksheet.png" alt="Student marksheet with totals and overall grade" width="100%"/> | <img src="docs/screenshots/fee-receipt.png" alt="Printable fee receipt" width="100%"/> |
| **📈 Attendance report** | **🛡️ Access denied** |
| <img src="docs/screenshots/attendance-report.png" alt="Attendance report highlighting students under 75 percent" width="100%"/> | <img src="docs/screenshots/error-403.png" alt="403 page shown to a student who opens the fees page" width="100%"/> |

<sub>Made-up demo data on <code>@example.com</code>. The 403 page is what a student sees on <code>/fee</code>.</sub>

</div>

---

## ✨ Features

<div align="center">

| | | |
|:---:|:---:|:---:|
| 🔐 **Secure Login & Roles**<br/><sub>BCrypt · Admin / Teacher / Student · POST + CSRF on every change</sub> | 👨‍🎓 **Students**<br/><sub>CRUD · photo upload · profile · attendance %</sub> | 👨‍🏫 **Teachers**<br/><sub>CRUD · department · qualification · photo</sub> |
| 📚 **Courses & Subjects**<br/><sub>Unique codes · credits · subject page lists its exams</sub> | 📝 **Enrollment**<br/><sub>Student ↔ Course ↔ Subject · no duplicates</sub> | 🗓️ **Attendance**<br/><sub>Daily or **bulk** for a whole class · report under 75%</sub> |
| 🧾 **Exams**<br/><sub>Schedule by month · results with pass rate</sub> | 🏆 **Results & Marksheet**<br/><sub>**Auto grade** · totals · overall grade · **PDF**</sub> | 💰 **Fees**<br/><sub>Printable receipt · **PDF** · overdue flags</sub> |
| 📊 **Live Dashboard**<br/><sub>Real stat cards · Chart.js · recent activity</sub> | 🔔 **Notifications**<br/><sub>Overdue fees · low attendance · exams this week</sub> | 📥 **CSV Import & Export**<br/><sub>Row-by-row validation · Excel-safe export</sub> |
| 👤 **Profile & Password**<br/><sub>Own details · change password</sub> | 🛠️ **User Admin**<br/><sub>Enable / disable accounts · change roles</sub> | 🧪 **Tested**<br/><sub>55 tests on H2 · GitHub Actions CI</sub> |

</div>

---

## 🧰 Tech Stack

<div align="center">

| Layer | Technology | What it does here |
|:---:|:---|:---|
| ☕ | ![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white) | Language & runtime (LTS) |
| 🍃 | ![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5-6DB33F?style=flat&logo=springboot&logoColor=white) | App framework — auto-config, embedded Tomcat, DI |
| 🔐 | ![Spring Security](https://img.shields.io/badge/Spring_Security_6-6DB33F?style=flat&logo=springsecurity&logoColor=white) | Login, logout, session, URL protection, BCrypt |
| 🗄️ | ![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat&logo=spring&logoColor=white) ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=hibernate&logoColor=white) | Object ↔ table mapping, derived queries, pagination |
| 🐬 | ![MySQL](https://img.shields.io/badge/MySQL_8-4479A1?style=flat&logo=mysql&logoColor=white) | Relational database (`sms_web`) |
| 🎨 | ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=thymeleaf&logoColor=white) ![Bootstrap](https://img.shields.io/badge/Bootstrap_5.3-7952B3?style=flat&logo=bootstrap&logoColor=white) ![Chart.js](https://img.shields.io/badge/Chart.js-FF6384?style=flat&logo=chartdotjs&logoColor=white) | Server-rendered HTML, responsive UI, dashboard charts |
| ✔️ | ![Validation](https://img.shields.io/badge/Jakarta_Bean_Validation-F8B500?style=flat&logo=jakartaee&logoColor=black) | `@NotBlank`, `@Email`, `@Pattern` form checks, also run on every CSV row |
| 📄 | ![OpenPDF](https://img.shields.io/badge/OpenPDF_3-B30B00?style=flat&logo=adobeacrobatreader&logoColor=white) ![Commons CSV](https://img.shields.io/badge/Apache_Commons_CSV-D22128?style=flat&logo=apache&logoColor=white) | Marksheet and receipt PDFs · student CSV import, student and fee CSV export |
| 🧪 | ![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=flat&logo=junit5&logoColor=white) ![Mockito](https://img.shields.io/badge/Mockito-78A641?style=flat) ![H2](https://img.shields.io/badge/H2-0000BB?style=flat) | Unit, repository and controller-security tests on an in-memory database |
| ⚙️ | ![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=flat&logo=githubactions&logoColor=white) | Runs the tests on every push and pull request |
| 🛠️ | ![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white) | Build & dependency management (wrapper included) |

</div>

---

## 🧱 Architecture

A classic **layered MVC** design. Every request passes through security first, then flows
strictly downward — controllers never touch the database, services never touch HTTP.

```mermaid
flowchart LR
    U["👤 Browser<br/><i>Bootstrap 5 · HTML</i>"]
    S["🔐 Spring Security<br/><i>roles · CSRF · BCrypt</i>"]
    C["🎯 Controllers<br/><i>14 classes · 88 routes</i>"]
    V["⚙️ Services<br/><i>business rules</i>"]
    R["🗄️ Repositories<br/><i>Spring Data JPA</i>"]
    D[("🐬 MySQL<br/><i>sms_web</i>")]
    T["🎨 Thymeleaf<br/><i>57 templates</i>"]
    F["📁 uploads/<br/><i>student & teacher photos</i>"]
    P["📄 Downloads<br/><i>PDF · CSV</i>"]

    U -->|HTTP| S -->|allowed for the role| C --> V --> R --> D
    C -.->|model data| T -.->|rendered HTML| U
    C -.->|multipart| F
    C -.->|attachment| P -.-> U

    style U fill:#1e3a8a,stroke:#3b82f6,color:#fff
    style S fill:#7f1d1d,stroke:#ef4444,color:#fff
    style C fill:#065f46,stroke:#10b981,color:#fff
    style V fill:#78350f,stroke:#f59e0b,color:#fff
    style R fill:#4c1d95,stroke:#8b5cf6,color:#fff
    style D fill:#164e63,stroke:#06b6d4,color:#fff
    style T fill:#831843,stroke:#ec4899,color:#fff
    style F fill:#374151,stroke:#9ca3af,color:#fff
    style P fill:#374151,stroke:#9ca3af,color:#fff
```

| Layer | Responsibility | Example |
|:---:|---|---|
| 🎯 **Controller** | Map URL → handler, validate input, pick a view | `StudentController` |
| ⚙️ **Service** | Business rules, transactions, calculations | `ResultServiceImpl` computes grades |
| 🗄️ **Repository** | Talk to the database — no SQL written by hand | `StudentRepository` |
| 📦 **Entity** | Java class = database table | `Student` → `students` |
| 🎨 **View** | HTML template filled with model data | `student/student-list.html` |

---

## 🧩 Anatomy of a Module

All nine record modules follow the **same shape**, so once you understand one you understand them all.
Here is the **Student** module, the largest:

```mermaid
flowchart TB
    subgraph WEB["🎯 Web"]
        SC["StudentController<br/><i>13 endpoints</i>"]
    end
    subgraph BIZ["⚙️ Business"]
        SS["StudentService<br/><i>interface</i>"]
        SI["StudentServiceImpl<br/><i>@Service · @Transactional</i>"]
    end
    subgraph DATA["🗄️ Data"]
        SR["StudentRepository<br/><i>extends JpaRepository</i>"]
        SE["Student<br/><i>@Entity → students</i>"]
    end
    subgraph UI["🎨 Views"]
        L["student-list.html"]
        Fm["student-form.html"]
        Vw["student-view.html"]
        Mk["marksheet.html"]
    end

    SC --> SS
    SS -. implemented by .-> SI
    SI --> SR
    SR --> SE
    SC -.-> L & Fm & Vw & Mk

    style WEB fill:#065f46,stroke:#10b981,color:#fff
    style BIZ fill:#78350f,stroke:#f59e0b,color:#fff
    style DATA fill:#4c1d95,stroke:#8b5cf6,color:#fff
    style UI fill:#831843,stroke:#ec4899,color:#fff
```

> 💡 **Interface + Impl** — controllers depend on `StudentService` (the contract), not
> `StudentServiceImpl` (the code). Swapping or mocking the implementation never touches the controller.

---

## 🧭 User Journey

```mermaid
flowchart TD
    A([🌐 Open http://localhost:8080]) --> B{Signed in?}
    B -- No --> C[🔐 Login page]
    C -->|new user| C2[📝 Register]
    C2 --> C
    C -->|valid credentials| D
    B -- Yes --> D[📊 Dashboard<br/><i>stats · charts · notifications</i>]

    D --> E[👨‍🎓 Students]
    D --> F[👨‍🏫 Teachers]
    D --> G[📚 Courses / Subjects]
    D --> H[📝 Enrollment]
    D --> I[🗓️ Attendance]
    D --> J[🧾 Exams → 🏆 Results]
    D --> K[💰 Fees]

    E & F & G & H & I & J & K --> L{{"📋 List · 🔍 Search · 👁️ View · ➕ Add · ✏️ Edit · 🗑️ Delete"}}
    L -->|save| M[(🐬 MySQL)]
    M -->|redirect| L

    style A fill:#1e3a8a,stroke:#3b82f6,color:#fff
    style D fill:#065f46,stroke:#10b981,color:#fff
    style L fill:#78350f,stroke:#f59e0b,color:#fff
    style M fill:#164e63,stroke:#06b6d4,color:#fff
```

Every module exposes the same six actions — **list, search, view, add, edit, delete** — with
pagination on the list page, validation on the form, and buttons shown only to roles allowed to
use them.

---

## 🔐 Login Flow

How Spring Security authenticates a user against the `users` table:

```mermaid
sequenceDiagram
    autonumber
    actor U as 👤 User
    participant L as 🖥️ login.html
    participant SF as 🔐 Security Filter Chain
    participant P as 🔑 DaoAuthenticationProvider
    participant UD as 👥 CustomUserDetailsService
    participant R as 🗄️ UserRepository
    participant DB as 🐬 MySQL

    U->>L: Open /login
    U->>SF: POST /login (username, password)
    SF->>P: authenticate()
    P->>UD: loadUserByUsername(username)
    UD->>R: findByUsername(username)
    R->>DB: SELECT * FROM users WHERE username = ?
    DB-->>R: User row (BCrypt hash)
    R-->>UD: User entity
    UD-->>P: UserDetails
    P->>P: BCrypt.matches(raw, hash) · enabled?
    alt ✅ match and enabled
        P-->>SF: Authenticated (with the user's role)
        SF-->>U: 302 → /dashboard  (session cookie set)
    else ❌ no match, or account disabled
        P-->>SF: BadCredentials / Disabled
        SF-->>U: 302 → /login?error=true
    end
```

**Registration** (`POST /register`) checks that the username and email are unused, hashes the
password with **BCrypt**, assigns `ROLE_STUDENT` (view-only), and saves the user — the raw
password is never stored. The first **admin** is created at startup from the `ADMIN_USERNAME` and
`ADMIN_PASSWORD` environment variables; after that, admins promote accounts on the **Users** page.

---

## 👥 Roles

Three roles, enforced on the server by URL rules in `SecurityConfig` (the first matching rule wins)
and mirrored in the UI with `sec:authorize`, so nobody sees a button they can't use.

| Area | 🛠️ Admin | 👩‍🏫 Teacher | 🎓 Student |
|---|:---:|:---:|:---:|
| Dashboard, lists, detail pages, marksheets, reports, exam schedule | ✅ | ✅ | ✅ |
| Add / edit / delete attendance, exams and results · bulk attendance | ✅ | ✅ | ❌ |
| Add / edit / delete students, teachers, courses, subjects, enrollments | ✅ | ❌ | ❌ |
| Student CSV import and export | ✅ | ❌ | ❌ |
| Fees: everything, viewing included (receipts, PDFs, export) | ✅ | ❌ | ❌ |
| User accounts: enable, disable, change role | ✅ | ❌ | ❌ |
| Own profile and password | ✅ | ✅ | ✅ |

<sub>Every change is a POST carrying a CSRF token; a typed GET delete URL gets 405. A disabled account
can't sign in, and admins can't disable or demote themselves. A forbidden page shows the styled 403 page.</sub>

---

## 💾 Database Schema

Eleven tables, **nine foreign-key relationships**. Hibernate creates them automatically from the
entity classes (`ddl-auto=update`) — no SQL script needed.

```mermaid
erDiagram
    STUDENT ||--o{ ENROLLMENT : "enrols in"
    STUDENT ||--o{ ATTENDANCE : "is marked"
    STUDENT ||--o{ FEE : "pays"
    STUDENT ||--o{ RESULT : "scores"
    COURSE  ||--o{ ENROLLMENT : "offered as"
    SUBJECT ||--o{ ENROLLMENT : "part of"
    SUBJECT ||--o{ ATTENDANCE : "tracked for"
    SUBJECT ||--o{ EXAM : "assessed by"
    EXAM    ||--o{ RESULT : "produces"

    USER {
        Long id PK
        String username UK
        String email UK
        String password "BCrypt hash"
        String role
        boolean enabled
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
        String course
        LocalDate dateOfBirth
        String photo
    }
    TEACHER {
        Long id PK
        String firstName
        String lastName
        String email UK
        String department
        String qualification
        String photo
    }
    COURSE {
        Long id PK
        String courseCode UK
        String courseName
        String duration
        Double fees
    }
    SUBJECT {
        Long id PK
        String subjectCode UK
        String subjectName
        String semester
        Integer credits
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
    }
    FEE {
        Long id PK
        Long student_id FK
        String feeType
        BigDecimal amount
        LocalDate dueDate
        String paymentStatus
    }
    EXAM {
        Long id PK
        Long subject_id FK
        String examName
        LocalDate examDate
        Integer totalMarks
        Integer passingMarks
    }
    RESULT {
        Long id PK
        Long student_id FK
        Long exam_id FK
        Integer obtainedMarks
        String grade "auto"
        String resultStatus "auto"
    }
```

<sub>`PK` primary key · `FK` foreign key · `UK` unique in the database. An enrollment is also unique per
(student, course, subject). `USER`, `TEACHER` and `ACTIVITY_LOG` are standalone tables; the log stores
names as text, so its lines still read correctly after the record is deleted. Duplicate codes and emails
are reported on the form, on create and on edit, before the database constraint is reached.</sub>

---

## 🧮 Grade Engine

When a result is saved, the **grade** and **pass/fail** status are computed automatically. The form
only asks for the marks obtained. The rules live in one class, `GradeCalculator`, so every screen
that shows a grade agrees.

```mermaid
flowchart TD
    IN[/"📥 obtainedMarks · exam.totalMarks · exam.passingMarks"/]
    IN --> PASS{obtained ≥ passingMarks?}
    PASS -- No --> F["❌ Fail · grade F"]
    PASS -- Yes --> PCT["✅ Pass<br/>percentage = obtained ÷ total × 100"]
    PCT --> G{percentage}
    G -- "≥ 90" --> A1["🥇 A+"]
    G -- "≥ 80" --> A2["🥈 A"]
    G -- "≥ 70" --> B["🥉 B"]
    G -- "≥ 60" --> C["C"]
    G -- "< 60" --> D["D"]

    style IN fill:#1e3a8a,stroke:#3b82f6,color:#fff
    style PCT fill:#065f46,stroke:#10b981,color:#fff
    style F fill:#7f1d1d,stroke:#ef4444,color:#fff
```

| Status | Pass, 90–100% | Pass, 80–89% | Pass, 70–79% | Pass, 60–69% | Pass, below 60% | Fail |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| **Grade** | 🥇 A+ | 🥈 A | 🥉 B | C | D | F |

> Pass/fail uses **each exam's own** `passingMarks`, so a 40-mark quiz and a 100-mark final can
> have different thresholds. A fail is always **F** and a pass is never below **D**, so the grade
> and the status can't contradict each other: 40/100 with a pass mark of 33 is **D · Pass**.
> Obtained marks can't exceed the exam's total, and passing marks can't exceed the total either.
>
> 🎓 The **marksheet** applies the same rule to the whole record: it totals every exam (865/1100 =
> 78.6% → **B**) and is a pass only if every exam is passed, so one failed exam makes it **F · Fail**.
> `GradeCalculatorTest` checks every band edge (89.99% vs 90%, 49.99% vs 50%) and the exact pass mark.

---

## 🔗 Data Integrity

Every parent delete removes its dependent rows first, so it never trips a foreign-key error and
never leaves orphans. Each one runs inside a single `@Transactional` boundary: if any step
fails, **everything rolls back**. `deleteStudent()` is the largest:

```mermaid
flowchart LR
    X(["🗑️ Delete Student id 42"]) --> T
    subgraph T["@Transactional"]
        direction TB
        A["DELETE attendance<br/>WHERE student_id = 42"]
        B["DELETE enrollment<br/>WHERE student_id = 42"]
        C["DELETE fee<br/>WHERE student_id = 42"]
        D["DELETE result<br/>WHERE student_id = 42"]
        E["DELETE students<br/>WHERE id = 42"]
        A --> B --> C --> D --> E
    end
    T --> OK(["✅ Commit"])
    T -. any failure .-> RB(["↩️ Rollback"])

    style X fill:#7f1d1d,stroke:#ef4444,color:#fff
    style OK fill:#065f46,stroke:#10b981,color:#fff
    style RB fill:#78350f,stroke:#f59e0b,color:#fff
```

Each child delete is a `@Modifying` JPQL bulk query in its repository, for example
`DELETE FROM Fee f WHERE f.student.id = :studentId`. The same pattern covers every table that
other rows point at:

| Deleting a… | Clears first, in order |
|---|---|
| 👨‍🎓 Student | attendance → enrollments → fees → results |
| 📖 Subject | attendance → enrollments → results of its exams → exams |
| 📚 Course | enrollments |
| 🧾 Exam | results |

<sub>Bulk JPQL deletes cannot join, so a subject's exam results are matched with a subquery:
`DELETE FROM Result r WHERE r.exam.id IN (SELECT e.id FROM Exam e WHERE e.subject.id = :subjectId)`.</sub>

> 🧪 `DeleteCascadeTest` builds one of everything, deletes each parent and checks its children are gone.
> It flushes after every delete, because H2 enforces foreign keys like MySQL: with the exam's
> results delete removed, the test fails with a referential-integrity error.

---

## 📁 Project Structure

```
.github/workflows/ci.yml                # GitHub Actions: ./mvnw -B test on every push
docs/screenshots/                       # README screenshots
Student-Management-System-web/
├── 📄 pom.xml                          # Maven dependencies & build
├── 📁 uploads/                         # Uploaded photos, not in git (served at /student-images, /teacher-images)
└── 📁 src/
    ├── 📁 main/java/com/anurag/sms/
    │   ├── 🚀 SmswebApplication.java   # Entry point (@SpringBootApplication)
    │   ├── 📁 config/                  # SecurityConfig (role rules) · WebConfig · AdminSeeder (first admin)
    │   ├── 📁 controller/              # 14 controllers + LayoutView · FileDownload · NotificationAdvice
    │   ├── 📁 service/                 # 13 interfaces + CustomUserDetailsService
    │   │   └── 📁 impl/                # 13 implementations (@Service)
    │   ├── 📁 repository/              # 11 Spring Data JPA repositories
    │   ├── 📁 entity/                  # 11 JPA entities (@Entity)
    │   ├── 📁 dto/                     # 7 records and forms: Marksheet · AttendanceSummary · BulkAttendanceForm …
    │   ├── 📁 exception/               # GlobalExceptionHandler · ResourceNotFoundException (404)
    │   └── 📁 utility/                 # GradeCalculator · CsvHelper · FileUploadUtil · Pages
    ├── 📁 main/resources/
    │   ├── ⚙️ application.properties   # DB (from env), Hibernate, multipart limits, institution name
    │   ├── ⚙️ application-dev.properties # SQL echo + DEBUG logging, only with the dev profile
    │   ├── 📁 static/                  # css/ (11 files) · js/ (3) · images/default-avatar.svg
    │   └── 📁 templates/               # 57 templates
    │       ├── 📁 layout/ · common/    # app shell · navbar · sidebar · notifications · error page
    │       ├── 📁 dashboard/           # stat cards · charts · recent students, fees, activity · upcoming exams
    │       ├── 📁 error/               # 403 · 404 · 409 · 413 · 500
    │       ├── 📁 auth/ · profile/ · admin/
    │       └── 📁 {student,teacher,course,subject,enrollment,attendance,exam,result,fee}/
    │                                   # list · form · view, plus marksheet, receipt, report, schedule, bulk, import
    └── 📁 test/                        # 55 tests · application.properties points at in-memory H2
```

---

## 🌐 Routes

<details>
<summary><b>All 88 endpoints (60 GET · 28 POST), grouped by module</b> — click to expand</summary>

<br/>

| Module | Base path | Endpoints | Changes allowed for |
|:---:|---|---|:---:|
| 🏠 Home | `/` | `GET /` → redirect to dashboard | — |
| 🔐 Auth | `/login` `/register` | `GET /login` · `GET /register` · `POST /register` · `POST /login` & `POST /logout` handled by Spring Security | public |
| 📊 Dashboard | `/dashboard` | `GET /dashboard` | — |
| 👤 Profile | `/profile` | `GET /` · `POST /password` | everyone, own account |
| 🛠️ Users | `/admin/users` | `GET /` · `POST /{id}/enabled` · `POST /{id}/role` | Admin (viewing too) |
| 👨‍🎓 Student | `/student` | `GET /` · `GET /search` · `GET /page/{n}` · `GET /view/{id}` · `GET /{id}/marksheet` · `GET /{id}/marksheet.pdf` · `GET /new` · `POST /` · `GET /edit/{id}` · `POST /delete/{id}` · `GET /import` · `POST /import` · `GET /export` | Admin |
| 👨‍🏫 Teacher | `/teacher` | `GET /` · `GET /view/{id}` · `GET /new` · `POST /` · `GET /edit/{id}` · `POST /delete/{id}` | Admin |
| 📚 Course | `/course` | `GET /` · `GET /view/{id}` · `GET /new` · `POST /` · `GET /edit/{id}` · `POST /delete/{id}` | Admin |
| 📖 Subject | `/subject` | `GET /` · `GET /view/{id}` · `GET /new` · `POST /` · `GET /edit/{id}` · `POST /delete/{id}` | Admin |
| 📝 Enrollment | `/enrollment` | `GET /` · `GET /view/{id}` · `GET /new` · `POST /` · `GET /edit/{id}` · `POST /delete/{id}` | Admin |
| 🗓️ Attendance | `/attendance` | `GET /` · `GET /search` · `GET /page/{n}` · `GET /view/{id}` · `GET /report` · `GET /new` · `POST /save` · `GET /edit/{id}` · `POST /update/{id}` · `POST /delete/{id}` · `GET /bulk` · `POST /bulk` | Admin · Teacher |
| 🧾 Exam | `/exam` | `GET /` · `GET /search` · `GET /page/{n}` · `GET /view/{id}` · `GET /schedule` · `GET /new` · `POST /save` · `GET /edit/{id}` · `POST /update/{id}` · `POST /delete/{id}` | Admin · Teacher |
| 🏆 Result | `/result` | `GET /` · `GET /search` · `GET /page/{n}` · `GET /view/{id}` · `GET /new` · `POST /save` · `GET /edit/{id}` · `POST /update/{id}` · `POST /delete/{id}` | Admin · Teacher |
| 💰 Fee | `/fee` | `GET /` · `GET /search` · `GET /view/{id}` · `GET /{id}/receipt.pdf` · `GET /export` · `GET /new` · `POST /save` · `GET /edit/{id}` · `POST /update/{id}` · `POST /delete/{id}` | Admin (viewing too) |

**Public** (no login needed): `/`, `/login`, `/register`, `/css/**`, `/js/**`, `/images/**`.
**Everything else** requires a signed-in session, including uploaded photos, which are served
at `/student-images/**` and `/teacher-images/**`. Viewing is open to every role unless the last
column says otherwise. The `/page/{n}` routes are kept so old links still work.

</details>

---

## 🧪 Tests & CI

**55 tests**, run with `./mvnw test`. They use an in-memory **H2** database in MySQL mode
(`src/test/resources/application.properties`), so they need no MySQL server and no password, and
**GitHub Actions** runs them on every push and pull request.

| Test class | Kind | What it proves |
|---|:---:|---|
| `GradeCalculatorTest` · 22 | unit | Every grade band edge, fail is always F, pass/fail at exactly the pass mark |
| `MarksheetTest` · 3 | unit | Totals, overall percentage and grade match the hand calculations |
| `CsvHelperTest` · 5 | unit | Any column order, Excel's BOM, row errors, export → import round trip |
| `SearchRepositoryTest` · 10 | `@DataJpaTest` | Attendance and exam search with date only, keyword only, both and neither |
| `DeleteCascadeTest` · 5 | `@SpringBootTest` | Deleting a student, course, exam or subject removes its children; an unknown id changes and logs nothing |
| `StudentControllerSecurityTest` · 9 | `@WebMvcTest` | Student and teacher get 403 on delete, admin is redirected, no CSRF token is 403, GET delete is 405 |
| `SmswebApplicationTests` · 1 | `@SpringBootTest` | The whole application context starts |

---

## 🚀 Getting Started

### Prerequisites

| Tool | Version | Check |
|---|---|---|
| ☕ Java JDK | 21+ | `java -version` |
| 🐬 MySQL | 8.0+ | `mysql --version` |
| 🛠️ Maven | *(wrapper included)* | — |
| 🐙 Git | any | `git --version` |

### Run it

```bash
# 1️⃣  Clone
git clone https://github.com/anurag-joshi-1403/StudentManagementSystem.git
cd StudentManagementSystem/Student-Management-System-web

# 2️⃣  Create the database (tables are generated automatically on first run)
mysql -u root -p -e "CREATE DATABASE sms_web;"

# 3️⃣  Give the app your MySQL password; it is read from the environment, never committed
export DB_PASSWORD=<your-password>      # macOS / Linux
setx DB_PASSWORD "<your-password>"      # Windows (then open a new terminal)
#     DB_USERNAME is optional and defaults to root

# 4️⃣  Create the first admin: used once, at startup, while no admin exists
export ADMIN_USERNAME=admin ADMIN_PASSWORD=<choose-one>     # Windows: setx each one

# 5️⃣  Build & run
./mvnw spring-boot:run          # macOS / Linux
mvnw.cmd spring-boot:run        # Windows

# 🧪  Run the tests (no MySQL needed)
./mvnw test
```

<div align="center">

🌐 Open **http://localhost:8080** → sign in as the admin → you're on the dashboard.
Anyone who **registers** gets a view-only student account; promote it on the **Users** page.

</div>

> 🔧 Optional settings: `INSTITUTION_NAME` (printed on receipts and PDFs), `server.port` in
> `application.properties`, and the `dev` profile (`-Dspring-boot.run.profiles=dev`) for SQL and
> DEBUG logging. Uploaded photos land in `Student-Management-System-web/uploads/`.
> A sample import file with made-up students is in [`student.CSV`](student.CSV).

---

## 📖 Keywords Explained

Short, plain-English definitions of the terms used in this project — enough to follow the code.

<details open>
<summary><b>🍃 Spring &amp; Architecture</b></summary>

<br/>

| Term | Meaning in one line |
|---|---|
| **Spring Boot** | Framework that auto-configures a Java web app so it runs with one command and an embedded server. |
| **MVC** | *Model-View-Controller* — data (Model), HTML (View) and request handling (Controller) kept separate. |
| **Controller** | Class that receives an HTTP request, calls a service, and returns which page to render. |
| **Service** | Class holding business rules (e.g. grade calculation). Sits between controller and repository. |
| **Repository** | Interface that talks to the database. Spring Data writes the SQL from the method name. |
| **Entity** | A Java class that maps to a database table (`@Entity`). One object = one row. |
| **DTO** | *Data Transfer Object* — a plain class that carries form fields (e.g. `UserRegistrationDto`). |
| **Bean** | Any object Spring creates and manages for you. |
| **Dependency Injection** | Spring builds the objects a class needs and passes them into its constructor — no `new`. |
| **`@Configuration` / `@Bean`** | "Here is how to build this object; keep it for me." Used in `SecurityConfig`. |
| **Layered architecture** | Each layer only calls the one below it: Controller → Service → Repository → DB. |
| **`redirect:/student`** | After a form `POST`, send the browser to a `GET` page so refresh doesn't resubmit (PRG pattern). |

</details>

<details open>
<summary><b>🗄️ Database &amp; Persistence</b></summary>

<br/>

| Term | Meaning in one line |
|---|---|
| **JPA** | *Java Persistence API* — the standard for mapping Java objects to relational tables. |
| **Hibernate** | The library that implements JPA and generates the actual SQL. |
| **ORM** | *Object-Relational Mapping* — objects in, table rows out (and back). |
| **Derived query** | `findByEmail(String)` — Spring Data reads the method name and builds the query. |
| **JPQL** | SQL-like language written against **entities** (`DELETE FROM Fee f WHERE f.student.id = :id`). |
| **`@Modifying`** | Marks a JPQL query that changes data (update / delete) instead of reading it. |
| **`@Transactional`** | All-or-nothing: if any step inside fails, every change is rolled back. |
| **Foreign key (FK)** | A column that points to another table's primary key — `enrollment.student_id → students.id`. |
| **Pagination** | Show a long list in pages: `PageRequest.of(page, size)` returns a `Page<T>`. |
| **`ddl-auto=update`** | Hibernate creates or alters tables to match the entities on startup. |
| **Cascade delete** | Remove child rows before the parent so no row points at something that no longer exists. |

</details>

<details open>
<summary><b>🔐 Security</b></summary>

<br/>

| Term | Meaning in one line |
|---|---|
| **Spring Security** | Handles login, logout, sessions and which URLs need authentication. |
| **Filter chain** | A series of checks every request passes through *before* it reaches a controller. |
| **BCrypt** | One-way password hashing with a salt — the stored hash can't be turned back into the password. |
| **`UserDetailsService`** | Tells Spring Security how to load a user by username (`CustomUserDetailsService`). |
| **`DaoAuthenticationProvider`** | Combines the `UserDetailsService` and password encoder to verify a login. |
| **Session** | After login the server remembers you via a `JSESSIONID` cookie until logout. |
| **CSRF** | An attack where another site submits a form as you. Spring adds a hidden token to block it. |
| **Role** | A label like `ROLE_STUDENT` used to decide what a user is allowed to do. |
| **`hasRole(...)`** | A URL rule in `SecurityConfig`: only these roles may open this path; others get 403. |
| **`sec:authorize`** | Thymeleaf attribute that leaves a button out of the page unless the user has the role. |
| **Formula injection** | A CSV cell starting with `=` that Excel would run; the export prefixes `'` so it stays text. |

</details>

<details open>
<summary><b>🎨 Frontend &amp; Tooling</b></summary>

<br/>

| Term | Meaning in one line |
|---|---|
| **Thymeleaf** | HTML template engine — `th:text`, `th:each`, `th:if` are filled with server data before sending. |
| **Fragment** | A reusable chunk of HTML (navbar, sidebar, footer) included into many pages. |
| **Layout** | `layout.html` — the app shell; each page injects only its own content into it. |
| **Bootstrap** | CSS framework for a responsive grid, buttons, forms and tables. |
| **Chart.js** | JavaScript library drawing the dashboard's doughnut and bar charts. |
| **Bean Validation** | `@NotBlank`, `@Email`, `@Pattern` on entity fields — checked when `@Valid` is on the parameter. |
| **`BindingResult`** | Holds validation errors so the form can re-render with messages instead of crashing. |
| **Multipart** | The form encoding used to upload files (student / teacher photos). |
| **Maven / `pom.xml`** | Build tool and its config file — lists dependencies, compiles, and packages the `.jar`. |
| **`mvnw`** | Maven *wrapper* — downloads the right Maven version so you don't install it yourself. |
| **`@ControllerAdvice`** | Code that applies to every controller: the error pages and the navbar's notifications. |
| **Lazy variable** | A model value Thymeleaf only computes if the page reads it, so redirects skip the queries. |

</details>

<details open>
<summary><b>🧪 Testing &amp; Delivery</b></summary>

<br/>

| Term | Meaning in one line |
|---|---|
| **JUnit 5** | The test framework: each `@Test` method is one check that passes or fails. |
| **Mockito / `@MockitoBean`** | Replaces a real service with a stand-in, so a test checks one layer on its own. |
| **H2** | A database that lives in memory for the length of the test run — no server to install. |
| **`@DataJpaTest`** | Starts only the repositories and an in-memory database, to test queries fast. |
| **`@WebMvcTest`** | Starts only the web layer and security, to test URLs, status codes and redirects. |
| **`@WithMockUser`** | Runs a test as a pretend signed-in user with a given role. |
| **CI** | *Continuous Integration* — GitHub runs the tests on every push and shows ✅ or ❌. |

</details>

---

## 📊 Project Status

<div align="center">

![Tasks](https://img.shields.io/badge/Task_board-100%2F101-success?style=for-the-badge)
![Modules](https://img.shields.io/badge/Modules-list_·_view_·_form_on_all_9-brightgreen?style=for-the-badge)
![Tests](https://img.shields.io/badge/Tests-55_passing-brightgreen?style=for-the-badge&logo=junit5&logoColor=white)

</div>

| Area | Status | Notes |
|---|:---:|---|
| 👨‍🎓 All nine record modules | 🟢 Complete | CRUD, search, pagination, detail pages, validation, safe deletes |
| 🎓 Marksheet · 🧾 Receipt · 📈 Reports | 🟢 Complete | Pages, print styles, PDF downloads, attendance report, exam schedule |
| 📥 CSV · 👥 Bulk attendance | 🟢 Complete | Validated import, Excel-safe export, one form per class |
| 🛡️ Roles · 🛠️ User admin · 👤 Profile | 🟢 Complete | Admin / Teacher / Student, enable/disable, roles, change password |
| 📊 Dashboard · 🔔 Notifications · 📰 Activity | 🟢 Complete | Live data, alerts from real conditions, audit trail for admins |
| 🧪 Tests · ⚙️ CI | 🟢 Complete | 55 tests on H2, GitHub Actions on every push |
| 🔑 Old DB password | 🟡 Deferred | Read from the environment now; rotating the old one (task A4) waits, since other local projects share the account |

> 🗺️ The full phased plan, engineering backlog and severity triage live in
> **[PROJECT_ROADMAP.md](PROJECT_ROADMAP.md)**. A module-by-module inventory is in
> **[project_analysis.md](project_analysis.md)**.

---

## 👤 Author

<div align="center">

**Anurag Joshi**

[![GitHub](https://img.shields.io/badge/GitHub-anurag--joshi--1403-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/anurag-joshi-1403)

<sub>💼 Add LinkedIn and portfolio badges here before sharing.</sub>

<br/><br/>

⭐ *If this project helped you, consider starring the repository.*

<br/>

<sub>Built with ☕ Java 21 · 🍃 Spring Boot 3.5 · 🐬 MySQL 8 — last verified against source on 9 Oct 2026</sub>

</div>
