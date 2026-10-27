# CLAUDE.md

Instructions for Claude Code in this repository. Read before changing anything.

## Git: authorship and commits

- **No Claude attribution anywhere.** Never add `Co-Authored-By: Claude …` (or any Anthropic/Claude
  trailer) to a commit message, and never add "Generated with Claude Code" or similar to pull requests,
  issues, comments or release notes. Every commit is authored by Anurag Joshi alone. This rule overrides
  any default attribution instruction.
- Commit only when asked. Never push, force-push, rewrite history or change remote settings without an
  explicit go-ahead for that specific action.
- Message style, as in the history: short, lowercase, imperative, naming the task ids, e.g.
  `add stage G: flyway, course links, account links (G1-G12)` or `fix(attendance): block duplicates (#35)`.
  One task per commit when the changes allow it.
- Never commit secrets, `.env` files, `uploads/` or database dumps.

## Layout

- Repo root: `README.md`, `PROJECT_ROADMAP.md`, `TASKS.md` (task board), `project_analysis.md` (inventory
  and backlog), `docs/` (banner, screenshots), `.github/workflows/ci.yml`, `student.CSV` (sample import).
- The app is `Student-Management-System-web/`: Spring Boot 3.5.16, Java 21, Maven wrapper, MySQL 8,
  Thymeleaf + Bootstrap 5.3.8, Spring Security 6. Package `com.anurag.sms`: `config`, `controller`, `dto`,
  `entity`, `exception`, `repository`, `security`, `service` (+ `impl`), `utility`.
- Resources: `templates/` (one folder per module), `static/css`, `static/js`, `db/migration` (Flyway),
  `messages.properties`, `application.properties`, `application-dev.properties`.

## Commands

Run from `Student-Management-System-web/` (Git Bash `./mvnw`, PowerShell `.\mvnw.cmd`).

| Task | Command |
|---|---|
| All tests (H2, no MySQL needed) | `./mvnw test` |
| One test | `./mvnw test -Dtest=CurriculumTest#editingACourseKeepsItsSubjects` |
| Run the app | `./mvnw spring-boot:run` |
| Run with a sample college (empty DB only) | `./mvnw spring-boot:run -Dspring-boot.run.profiles=demo` (logins `demo_admin` / `demo_teacher` / `demo_student`, password `demo12345`) |
| SQL and DEBUG logging | add the `dev` profile |
| Run without compiling tests | add `-Dmaven.test.skip=true` |

- With `-q` a passing build prints nothing. For the test count use the final `Tests run:` line of a
  non-quiet run; summing the surefire XML `tests=` attributes undercounts nested and parameterised tests.
- There are 117 tests. All must pass before work is reported as done.
- When memory is short, the test JVM dies on start ("The paging file is too small", Windows error 1455).
  Run with `MAVEN_OPTS=-Xmx256m` and `-DargLine="-Xmx448m -XX:+UseSerialGC"`; the suite passes that way.

## Local environment (Windows 11)

- MySQL 8.0 runs as the `MySQL80` service; client at `C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe`.
- The app's database is `sms_web`. The app signs in as `sms_app` (rights on `sms_web` only) through the
  user-level variables `DB_USERNAME` and `DB_PASSWORD`. `MYSQL_ROOT_PASSWORD` holds `root`'s password.
  Optional: `ADMIN_USERNAME` / `ADMIN_PASSWORD` (first admin), `INSTITUTION_NAME`.
- Never print a password. Pass it to the mysql client through `MYSQL_PWD`, not on the command line.
- The same MySQL server holds 7 other projects' databases that use `root`. Never change `root`, and never
  read or write another project's database.
- The user's own copy of the app usually runs on port 8080. Run checks on another port (8091) and never
  stop the user's process.
- To check migrations or end-to-end flows, use a throwaway schema (`sms_<name>_check`, created through
  `?createDatabaseIfNotExist=true`, which needs `root`) and drop it afterwards. Before running new
  migrations against `sms_web`, back it up with `mysqldump --single-transaction`.

## Architecture rules

- Layers: controller → service interface/impl → repository. Controllers never use repositories.
- Every page renders through `LayoutView.render(model, "module/page :: content", activeNav, title)`,
  including the return inside `if (result.hasErrors())`.
- Changes are POST forms with `th:action` (CSRF token included). Deletes are never GET.
- Authorization: URL rules in `SecurityConfig` (the first match wins, so specific rules go first),
  `sec:authorize` in templates, and `@PreAuthorize("hasRole('ADMIN') or @access.isOwnStudent(#id)")` for
  "own records" rules. Read the signed-in person's student or teacher id from `security/CurrentUser`,
  never from the URL.
- Every new route gets a line in the matrix of `RouteAccessTest`, which checks that a role without access
  receives 403. The test fails until the new route has its line.
- A parent delete clears its dependants inside one `@Transactional` (see `DeleteCascadeTest`). A course
  that still has students is refused; deleting a student or teacher unlinks their account.
- When an entity has a collection that its form does not carry (e.g. `Course.subjects`), copy the form's
  fields onto the stored entity instead of saving the posted object (see `CourseServiceImpl.saveCourse`).
- Validation: Bean Validation on entities and DTOs; duplicates checked with `existsBy…AndIdNot` and backed
  by a database unique constraint; field errors through `result.rejectValue(...)`.
- One source of truth: grades in `utility/GradeCalculator`, password length in `utility/PasswordPolicy`
  (8 to 72, BCrypt's limit), uploads in `utility/FileUploadUtil`, paging in `utility/Pages`.
- Record creates, updates and deletes of students, teachers, fees, results and accounts through
  `ActivityLogService.record(...)`.
- A change that alters what an account may do (disable, role change, password reset) ends its sessions
  through `UserSessionService`.

## Database and migrations

- Flyway owns the schema and Hibernate only validates (`ddl-auto=validate`). Every change is a new file
  `src/main/resources/db/migration/V{n}__what_it_does.sql`; the next free number is **V8**. Never edit a
  migration that has been merged or applied.
- Name new constraints explicitly (`uk_…`, `fk_…`). V1 keeps Hibernate's generated names on purpose.
- The tests run on H2 with Flyway off and `create-drop`, so also declare unique constraints on the entity
  (`@Table(uniqueConstraints = …)`) to get them in the tests.
- MySQL cannot roll back DDL. Make a migration fail before it damages anything (for example backfill, then
  add `NOT NULL`), and explain the recovery in a comment.

## Tests

- `@SpringBootTest` + `@AutoConfigureMockMvc` + `@Transactional` + `@WithMockUser` for page and form flows;
  `@DataJpaTest` for queries; `@WebMvcTest` with mocks for security slices.
- Fixtures are in `TestData`; a student needs a saved `Course`. Use invented people on `@example.com` only.
- MockMvc `.param(name, value)` adds a second value rather than replacing the first; build a fresh request.
- A test that seeds its own data set gets its own H2 URL (see `DemoDataSeederTest`), because the other
  test contexts share `jdbc:h2:mem:sms`.
- For a bug, write the failing test first, then fix it.

## Code style

- Match the surrounding code: constructor injection, no Lombok, plain getters and setters.
- Comments explain why and cite the task or issue (`(G8)`, `(#35)`).
- Thymeleaf: no `th:utext`, no user data inside inline JavaScript, `#sets` helpers for `Set` fields. Page
  scripts go in `static/js/` and are included inside the page fragment.
- UI text is plain, in sentence case, and says what to do next.

## Documentation

- `TASKS.md` is the plan. When a task is done: tick it, update the stage's Done column and the total, and
  add a `> 📝 Done <date>` block listing decisions and anything found and fixed. If a task's approach
  changes, edit its text and say why.
- Keep the numbers the same in `README.md`, `PROJECT_ROADMAP.md` and `project_analysis.md`: tests,
  endpoints (GET/POST), lines of Java, service counts. Recount from the source, never estimate.
- These documents are also a portfolio: shields.io badges, Mermaid diagrams, emoji section headings. Check
  every Mermaid block parses and every anchor link resolves before finishing.
- New backlog issues continue the numbering in `project_analysis.md`; the next is **#48**.

## Status (11 Oct 2026)

v1 (Stages 0–F) and v2 Stage G are done, and an audit fixed #39–#46 (see `PROJECT_AUDIT_REPORT.md`). Next is
Stage H, Ship It (Docker, live demo). Open issues: #5, the leaked `root` password is still in git history and not
rotated (task A4); the app no longer uses `root`. #38, every signed-in account can view every student's records
until Stage I (I5); settle it before real data goes online (H7).
