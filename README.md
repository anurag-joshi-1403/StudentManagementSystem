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

![Build](https://img.shields.io/badge/Build-passing-brightgreen?style=flat-square&logo=apachemaven&logoColor=white)
![Modules](https://img.shields.io/badge/Modules-10-blue?style=flat-square)
![Endpoints](https://img.shields.io/badge/Endpoints-66-blueviolet?style=flat-square)
![Entities](https://img.shields.io/badge/JPA_Entities-10-orange?style=flat-square)
![Views](https://img.shields.io/badge/Thymeleaf_Views-36-green?style=flat-square)
![LOC](https://img.shields.io/badge/Java_LOC-4.4k-yellow?style=flat-square)

<br/>

### 🎓 A full-stack web app that runs the daily administration of a college — students, teachers, courses, attendance, exams, results and fees — from one dashboard.

<br/>

[🚀 Quick Start](#-getting-started) · [🧱 Architecture](#-architecture) · [💾 Database](#-database-schema) · [📖 Keywords Explained](#-keywords-explained) · [🗺️ Roadmap](PROJECT_ROADMAP.md)

</div>

---

## 📑 Table of Contents

| | Section | | Section |
|:---:|---|:---:|---|
| 🎯 | [About the Project](#-about-the-project) | 🧮 | [Grade Engine](#-grade-engine) |
| ✨ | [Features](#-features) | 🔗 | [Data Integrity](#-data-integrity) |
| 🧰 | [Tech Stack](#-tech-stack) | 📁 | [Project Structure](#-project-structure) |
| 🧱 | [Architecture](#-architecture) | 🌐 | [Routes](#-routes) |
| 🧩 | [Anatomy of a Module](#-anatomy-of-a-module) | 🚀 | [Getting Started](#-getting-started) |
| 🧭 | [User Journey](#-user-journey) | 📖 | [Keywords Explained](#-keywords-explained) |
| 🔐 | [Login Flow](#-login-flow) | 📊 | [Project Status](#-project-status) |
| 💾 | [Database Schema](#-database-schema) | 👤 | [Author](#-author) |

---

## 🎯 About the Project

> **Student Management System (SMS)** is a server-rendered **Spring Boot MVC** application.
> An administrator logs in, lands on a live dashboard, and manages every academic record of the
> institution through ten CRUD modules that share one layout, one security layer and one database.

<table>
<tr>
<td width="50%" valign="top">

**🧠 Why it exists**

Colleges still track admissions, attendance and fees across spreadsheets and paper. This project
puts all of it behind a single login with searchable, paginated screens and a dashboard that
reads real numbers from the database — not hardcoded placeholders.

</td>
<td width="50%" valign="top">

**📐 By the numbers**

| Metric | Count |
|---|:---:|
| Feature modules | `10` |
| HTTP endpoints | `66` |
| JPA entities / repositories | `10` / `10` |
| Service classes | `21` |
| Thymeleaf templates | `36` |
| Lines of Java | `4.4k` |

</td>
</tr>
</table>

---

## ✨ Features

<div align="center">

| | | |
|:---:|:---:|:---:|
| 🔐 **Secure Login**<br/><sub>BCrypt hashing · session auth · duplicate guards</sub> | 👨‍🎓 **Students**<br/><sub>CRUD · photo upload · search · pagination</sub> | 👨‍🏫 **Teachers**<br/><sub>CRUD · department · qualification · photo</sub> |
| 📚 **Courses & Subjects**<br/><sub>Catalogue with codes, credits, semesters</sub> | 📝 **Enrollment**<br/><sub>Links Student ↔ Course ↔ Subject</sub> | 🗓️ **Attendance**<br/><sub>Daily marking · keyword + date filter</sub> |
| 🧾 **Exams**<br/><sub>Schedule with total & passing marks</sub> | 🏆 **Results**<br/><sub>**Auto grade** & pass/fail computation</sub> | 💰 **Fees**<br/><sub>Paid / pending tracking · totals</sub> |
| 📊 **Live Dashboard**<br/><sub>Real stat cards · Chart.js analytics · live clock</sub> | 🔍 **Search Everywhere**<br/><sub>Multi-field case-insensitive search</sub> | 🔗 **Safe Deletes**<br/><sub>Transactional cascade — no orphan rows</sub> |

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
| ✔️ | ![Validation](https://img.shields.io/badge/Jakarta_Bean_Validation-F8B500?style=flat&logo=jakartaee&logoColor=black) | `@NotBlank`, `@Email`, `@Pattern` form checks |
| 🛠️ | ![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white) ![Lombok](https://img.shields.io/badge/Lombok-BC4521?style=flat&logo=lombok&logoColor=white) | Build & dependency management, boilerplate reduction |

</div>

---

## 🧱 Architecture

A classic **layered MVC** design. Every request passes through security first, then flows
strictly downward — controllers never touch the database, services never touch HTTP.

```mermaid
flowchart LR
    U["👤 Browser<br/><i>Bootstrap 5 · HTML</i>"]
    S["🔐 Spring Security<br/><i>filter chain · BCrypt</i>"]
    C["🎯 Controllers<br/><i>12 classes · 66 routes</i>"]
    V["⚙️ Services<br/><i>business rules</i>"]
    R["🗄️ Repositories<br/><i>Spring Data JPA</i>"]
    D[("🐬 MySQL<br/><i>sms_web</i>")]
    T["🎨 Thymeleaf<br/><i>36 templates</i>"]
    F["📁 uploads/<br/><i>student & teacher photos</i>"]

    U -->|HTTP| S -->|authenticated| C --> V --> R --> D
    C -.->|model data| T -.->|rendered HTML| U
    C -.->|multipart| F

    style U fill:#1e3a8a,stroke:#3b82f6,color:#fff
    style S fill:#7f1d1d,stroke:#ef4444,color:#fff
    style C fill:#065f46,stroke:#10b981,color:#fff
    style V fill:#78350f,stroke:#f59e0b,color:#fff
    style R fill:#4c1d95,stroke:#8b5cf6,color:#fff
    style D fill:#164e63,stroke:#06b6d4,color:#fff
    style T fill:#831843,stroke:#ec4899,color:#fff
    style F fill:#374151,stroke:#9ca3af,color:#fff
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

All ten modules follow the **same shape**, so once you understand one you understand them all.
Here is the **Student** module:

```mermaid
flowchart TB
    subgraph WEB["🎯 Web"]
        SC["StudentController<br/><i>8 endpoints</i>"]
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
    end

    SC --> SS
    SS -. implemented by .-> SI
    SI --> SR
    SR --> SE
    SC -.-> L & Fm & Vw

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
    B -- Yes --> D[📊 Dashboard<br/><i>stats · charts · recent activity</i>]

    D --> E[👨‍🎓 Students]
    D --> F[👨‍🏫 Teachers]
    D --> G[📚 Courses / Subjects]
    D --> H[📝 Enrollment]
    D --> I[🗓️ Attendance]
    D --> J[🧾 Exams → 🏆 Results]
    D --> K[💰 Fees]

    E & F & G & H & I & J & K --> L{{"📋 List · 🔍 Search · ➕ Add · ✏️ Edit · 🗑️ Delete"}}
    L -->|save| M[(🐬 MySQL)]
    M -->|redirect| L

    style A fill:#1e3a8a,stroke:#3b82f6,color:#fff
    style D fill:#065f46,stroke:#10b981,color:#fff
    style L fill:#78350f,stroke:#f59e0b,color:#fff
    style M fill:#164e63,stroke:#06b6d4,color:#fff
```

Every module exposes the same five actions — **list, search, add, edit, delete** — with pagination
on the list page and validation on the form.

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
    P->>P: BCrypt.matches(raw, hash)
    alt ✅ match
        P-->>SF: Authenticated
        SF-->>U: 302 → /dashboard  (session cookie set)
    else ❌ no match
        P-->>SF: BadCredentials
        SF-->>U: 302 → /login?error=true
    end
```

**Registration** (`POST /register`) checks that the username and email are unused, hashes the
password with **BCrypt**, assigns `ROLE_STUDENT`, and saves the user — the raw password is never stored.

---

## 💾 Database Schema

Ten tables, **eight foreign-key relationships**. Hibernate creates them automatically from the
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

<sub>`PK` primary key · `FK` foreign key · `UK` unique. `USER` and `TEACHER` are standalone tables.</sub>

---

## 🧮 Grade Engine

When a result is saved, `ResultServiceImpl` computes the **grade** and **pass/fail** status
automatically — the form only asks for the marks obtained.

```mermaid
flowchart TD
    IN[/"📥 obtainedMarks · exam.totalMarks · exam.passingMarks"/]
    IN --> PCT["percentage = obtained ÷ total × 100"]
    PCT --> PASS{obtained ≥ passingMarks?}
    PASS -- Yes --> P["✅ status = Pass"]
    PASS -- No --> F["❌ status = Fail"]
    P --> G{percentage}
    F --> G
    G -- "≥ 90" --> A1["🥇 A+"]
    G -- "≥ 80" --> A2["🥈 A"]
    G -- "≥ 70" --> B["🥉 B"]
    G -- "≥ 60" --> C["C"]
    G -- "≥ 50" --> D["D"]
    G -- "< 50" --> FF["F"]

    style IN fill:#1e3a8a,stroke:#3b82f6,color:#fff
    style P fill:#065f46,stroke:#10b981,color:#fff
    style F fill:#7f1d1d,stroke:#ef4444,color:#fff
```

| Percentage | 90–100 | 80–89 | 70–79 | 60–69 | 50–59 | < 50 |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| **Grade** | 🥇 A+ | 🥈 A | 🥉 B | C | D | F |

> Pass/fail uses **each exam's own** `passingMarks`, so a 40-mark quiz and a 100-mark final can
> have different thresholds.

---

## 🔗 Data Integrity

Deleting a parent record never leaves orphaned rows. `deleteStudent()` runs inside one
`@Transactional` boundary — if any step fails, **everything rolls back**.

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

Each child delete is a `@Modifying` JPQL bulk query in its repository — e.g.
`DELETE FROM Fee f WHERE f.student.id = :studentId`. The same pattern protects `deleteSubject()`.

---

## 📁 Project Structure

```
Student-Management-System-web/
├── 📄 pom.xml                          # Maven dependencies & build
├── 📁 uploads/                         # Uploaded photos (served at /student-images, /teacher-images)
└── 📁 src/main/
    ├── 📁 java/com/anurag/sms/
    │   ├── 🚀 SmswebApplication.java   # Entry point (@SpringBootApplication)
    │   ├── 📁 config/                  # SecurityConfig · webConfig (static resource mapping)
    │   ├── 📁 controller/              # 12 controllers — one per module + Auth, Home, Dashboard
    │   ├── 📁 service/                 # 10 interfaces + CustomUserDetailsService
    │   │   └── 📁 impl/                # 10 implementations (@Service)
    │   ├── 📁 repository/              # 10 Spring Data JPA repositories
    │   ├── 📁 entity/                  # 10 JPA entities (@Entity)
    │   ├── 📁 dto/                     # UserRegistrationDto (+ stubs)
    │   └── 📁 utility/                 # Helper stubs (FileUploadUtil, DateUtil, CsvHelper)
    └── 📁 resources/
        ├── ⚙️ application.properties   # DB, Hibernate, multipart, port
        ├── 📁 static/
        │   ├── 📁 css/                 # theme · sidebar · navbar · stats-cards · charts …
        │   └── 📁 js/                  # dashboard.js · charts.js · notification.js
        └── 📁 templates/
            ├── 📁 layout/              # layout.html — the app shell
            ├── 📁 common/              # navbar · sidebar · footer · notification fragments
            ├── 📁 dashboard/           # stats-cards · charts · recent-* · upcoming-exams
            ├── 📁 auth/                # login · register
            └── 📁 {student,teacher,course,subject,enrollment,attendance,exam,result,fee}/
                                        # list · form (· view) per module
```

---

## 🌐 Routes

<details>
<summary><b>All 66 endpoints, grouped by module</b> — click to expand</summary>

<br/>

| Module | Base path | Endpoints |
|:---:|---|---|
| 🏠 Home | `/` | `GET /` → redirect to dashboard |
| 🔐 Auth | `/login` `/register` | `GET /login` · `GET /register` · `POST /register` · `POST /login` & `/logout` handled by Spring Security |
| 📊 Dashboard | `/dashboard` | `GET /dashboard` |
| 👨‍🎓 Student | `/student` | `GET /` · `GET /view/{id}` · `GET /search` · `GET /new` · `POST /` · `GET /edit/{id}` · `GET /delete/{id}` · `GET /page/{n}` |
| 👨‍🏫 Teacher | `/teacher` | `GET /` · `GET /new` · `POST /` · `GET /edit/{id}` · `GET /view/{id}` · `GET /delete/{id}` |
| 📚 Course | `/course` | `GET /` · `GET /new` · `POST /` · `GET /edit/{id}` · `GET /view/{id}` · `GET /delete/{id}` |
| 📖 Subject | `/subject` | `GET /` · `GET /new` · `POST /` · `GET /edit/{id}` · `GET /delete/{id}` |
| 📝 Enrollment | `/enrollment` | `GET /` · `GET /new` · `POST /` · `GET /edit/{id}` · `GET /delete/{id}` |
| 🗓️ Attendance | `/attendance` | `GET /` · `GET /new` · `POST /save` · `GET /edit/{id}` · `POST /update/{id}` · `GET /delete/{id}` · `GET /search` · `GET /page/{n}` |
| 🧾 Exam | `/exam` | `GET /` · `GET /new` · `POST /save` · `GET /edit/{id}` · `POST /update/{id}` · `GET /delete/{id}` · `GET /search` · `GET /page/{n}` |
| 🏆 Result | `/result` | `GET /` · `GET /new` · `POST /save` · `GET /edit/{id}` · `POST /update/{id}` · `GET /delete/{id}` · `GET /search` · `GET /page/{n}` |
| 💰 Fee | `/fee` | `GET /` · `GET /search` · `GET /new` · `POST /save` · `GET /edit/{id}` · `POST /update/{id}` · `GET /delete/{id}` |

**Public** (no login needed): `/`, `/login`, `/register`, `/css/**`, `/js/**`, `/images/**`, `/uploads/**`.
**Everything else** requires an authenticated session.

</details>

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

# 3️⃣  Point the app at your MySQL — edit src/main/resources/application.properties
#     spring.datasource.username=root
#     spring.datasource.password=<your-password>

# 4️⃣  Build & run
./mvnw spring-boot:run          # macOS / Linux
mvnw.cmd spring-boot:run        # Windows
```

<div align="center">

🌐 Open **http://localhost:8080** → **Register** an account → **Sign in** → you're on the dashboard.

</div>

> 🔧 Change the port with `server.port` in `application.properties`. Uploaded photos land in
> `Student-Management-System-web/uploads/`.

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
| **Lombok** | Annotation library that generates getters/setters/constructors at compile time. |

</details>

---

## 📊 Project Status

<div align="center">

![Progress](https://img.shields.io/badge/Overall-75%25-success?style=for-the-badge)
![Backend](https://img.shields.io/badge/Backend-10%2F10_modules-brightgreen?style=for-the-badge)
![Frontend](https://img.shields.io/badge/Frontend-list_%2B_form_on_all_modules-blue?style=for-the-badge)

</div>

| Area | Status | Notes |
|---|:---:|---|
| 🔐 Auth · 👨‍🎓 Student · 👨‍🏫 Teacher · 📚 Course | 🟢 Complete | CRUD, search, pagination, detail views |
| 📖 Subject · 📝 Enrollment · 🧾 Exam | 🟢 Backend · 🟡 UI | Detail view page pending |
| 💰 Fee · 🗓️ Attendance · 🏆 Result | 🟢 Backend · 🟡 UI | Receipt / report views pending |
| 📊 Dashboard | 🟢 Complete | Real data, Chart.js — notifications still static |
| 🛡️ Role-based access | 🔴 Planned | All users currently share one role |
| 🧪 Tests | 🟡 Minimal | Context-load test only |

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

<sub>Built with ☕ Java 21 · 🍃 Spring Boot 3.5 · 🐬 MySQL 8 — last verified against source on 21 Sep 2026</sub>

</div>
