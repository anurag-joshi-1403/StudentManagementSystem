package com.anurag.sms.config;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Attendance;
import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Enrollment;
import com.anurag.sms.entity.Exam;
import com.anurag.sms.entity.Fee;
import com.anurag.sms.entity.Result;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.Subject;
import com.anurag.sms.entity.Teacher;
import com.anurag.sms.entity.User;
import com.anurag.sms.repository.AttendanceRepository;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.EnrollmentRepository;
import com.anurag.sms.repository.ExamRepository;
import com.anurag.sms.repository.FeeRepository;
import com.anurag.sms.repository.ResultRepository;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.SubjectRepository;
import com.anurag.sms.repository.TeacherRepository;
import com.anurag.sms.repository.UserRepository;
import com.anurag.sms.utility.GradeCalculator;
import com.anurag.sms.utility.PasswordPolicy;

/**
 * Fills an empty database with a believable college (G12), for the live
 * demo, screenshots and trying features by hand:
 *
 * <pre>
 * ./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
 * </pre>
 *
 * Runs only with the "demo" profile and only when there are no courses,
 * subjects or students yet, so it never mixes with real records. A fixed
 * random seed gives the same people, marks and payments on every run; the
 * dates are counted back from today, so attendance always covers the last
 * six weeks and an exam is always a few days away.
 *
 * Everyone is invented and every email is on example.com. Three accounts
 * share the password in sms.demo.password: demo_admin, demo_teacher (linked
 * to a teacher) and demo_student (linked to a student).
 */
@Component
@Profile("demo")
public class DemoDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);

    private static final String PRESENT = "Present";
    private static final String ABSENT = "Absent";
    private static final String LATE = "Late";
    private static final String PAID = "Paid";
    private static final String PENDING = "Pending";

    private static final String[][] COURSES = {
            // code, name, duration, yearly fee, description
            { "BTECH-CSE", "B.Tech Computer Science", "4 years", "120000", "Programming, algorithms and computer systems" },
            { "BSC-PHY", "B.Sc Physics", "3 years", "60000", "Classical and modern physics with laboratory work" },
            { "BCOM", "B.Com", "3 years", "50000", "Accounting, economics and business law" },
    };

    private static final String[][] SUBJECTS = {
            // code, name, semester, credits, department, courses that teach it
            { "CS101", "Programming in Java", "1", "4", "Computer Science", "BTECH-CSE" },
            { "CS102", "Data Structures", "2", "4", "Computer Science", "BTECH-CSE" },
            { "CS103", "Database Systems", "3", "3", "Computer Science", "BTECH-CSE" },
            { "MA101", "Mathematics I", "1", "4", "Mathematics", "BTECH-CSE BSC-PHY BCOM" },
            { "PH101", "Classical Mechanics", "1", "4", "Physics", "BSC-PHY" },
            { "PH102", "Electromagnetism", "2", "4", "Physics", "BSC-PHY" },
            { "CM101", "Financial Accounting", "1", "4", "Commerce", "BCOM" },
            { "CM102", "Business Economics", "1", "3", "Commerce", "BCOM" },
    };

    private static final String[][] TEACHERS = {
            // first, last, department, qualification, gender
            { "Rahul", "Verma", "Computer Science", "M.Tech", "Male" },
            { "Meera", "Iyer", "Physics", "PhD", "Female" },
            { "Sunita", "Rao", "Mathematics", "M.Sc", "Female" },
            { "Arjun", "Nair", "Commerce", "M.Com", "Male" },
            { "Kavya", "Menon", "Computer Science", "PhD", "Female" },
    };

    // 30 students: the first 12 study Computer Science, the next 10
    // Physics and the last 8 Commerce
    private static final String[][] STUDENTS = {
            { "Aarav", "Male" }, { "Vivaan", "Male" }, { "Aditya", "Male" }, { "Ananya", "Female" },
            { "Diya", "Female" }, { "Kabir", "Male" }, { "Saanvi", "Female" }, { "Ishaan", "Male" },
            { "Myra", "Female" }, { "Reyansh", "Male" }, { "Anika", "Female" }, { "Sam", "Other" },
            { "Rohan", "Male" }, { "Navya", "Female" }, { "Krishna", "Male" }, { "Kiara", "Female" },
            { "Dev", "Male" }, { "Ira", "Female" }, { "Arnav", "Male" }, { "Riya", "Female" },
            { "Vihaan", "Male" }, { "Tara", "Female" }, { "Sai", "Male" }, { "Neha", "Female" },
            { "Yash", "Male" }, { "Pooja", "Female" }, { "Zoya", "Female" }, { "Nikhil", "Male" },
            { "Aditi", "Female" }, { "Karan", "Male" },
    };

    private static final String[] SURNAMES = {
            "Sharma", "Patel", "Gupta", "Singh", "Reddy", "Mehta", "Joshi", "Kapoor",
            "Das", "Khan", "Bose", "Pillai", "Chopra", "Malhotra", "Kulkarni",
    };

    private static final String[] STREETS = {
            "MG Road", "Lake View Road", "Station Road", "Park Street", "Hill Road", "Nehru Nagar",
    };

    private static final String[] CITIES = { "Pune", "Mumbai", "Bengaluru", "Hyderabad", "Jaipur", "Kochi" };

    private static final String[] PAYMENT_METHODS = { "UPI", "Bank Transfer", "Debit Card", "Credit Card", "Cash" };

    private final CourseRepository courses;
    private final SubjectRepository subjects;
    private final TeacherRepository teachers;
    private final StudentRepository students;
    private final EnrollmentRepository enrollments;
    private final AttendanceRepository attendance;
    private final ExamRepository exams;
    private final ResultRepository results;
    private final FeeRepository fees;
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final String demoPassword;

    public DemoDataSeeder(CourseRepository courses, SubjectRepository subjects, TeacherRepository teachers,
                          StudentRepository students, EnrollmentRepository enrollments,
                          AttendanceRepository attendance, ExamRepository exams, ResultRepository results,
                          FeeRepository fees, UserRepository users, PasswordEncoder passwordEncoder,
                          @Value("${sms.demo.password:demo12345}") String demoPassword) {
        this.courses = courses;
        this.subjects = subjects;
        this.teachers = teachers;
        this.students = students;
        this.enrollments = enrollments;
        this.attendance = attendance;
        this.exams = exams;
        this.results = results;
        this.fees = fees;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.demoPassword = demoPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        if (courses.count() > 0 || subjects.count() > 0 || students.count() > 0) {
            log.info("Demo data not loaded: the database already has courses, subjects or students.");
            return;
        }
        if (!PasswordPolicy.isAcceptable(demoPassword)) {
            log.warn("Demo data not loaded: sms.demo.password must be {} to {} characters.",
                    PasswordPolicy.MIN_LENGTH, PasswordPolicy.MAX_LENGTH);
            return;
        }

        Random random = new Random(42);
        LocalDate today = LocalDate.now();

        Map<String, Course> courseByCode = seedCourses();
        List<Subject> allSubjects = seedSubjects(courseByCode);
        List<Teacher> allTeachers = seedTeachers();
        List<Student> allStudents = seedStudents(courseByCode, random);

        // Each student's habits: how often they attend, and how well they
        // score. Three students fall under the 75% attendance line.
        double[] attendanceRate = new double[allStudents.size()];
        double[] ability = new double[allStudents.size()];
        for (int i = 0; i < allStudents.size(); i++) {
            boolean struggling = i == 5 || i == 16 || i == 26;
            attendanceRate[i] = struggling ? 0.55 + random.nextDouble() * 0.12 : 0.82 + random.nextDouble() * 0.16;
            ability[i] = struggling ? 0.32 + random.nextDouble() * 0.15 : 0.50 + random.nextDouble() * 0.42;
        }

        LocalDate termStart = today.minusWeeks(8);
        int enrolled = 0;
        int attendanceRows = 0;

        for (int i = 0; i < allStudents.size(); i++) {
            Student student = allStudents.get(i);

            for (Subject subject : student.getCourse().getSubjects()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setStudent(student);
                enrollment.setCourse(student.getCourse());
                enrollment.setSubject(subject);
                enrollment.setEnrollmentDate(termStart);
                enrollments.save(enrollment);
                enrolled++;

                // Six weeks of weekday classes, up to yesterday
                for (LocalDate day = today.minusWeeks(6); day.isBefore(today); day = day.plusDays(1)) {
                    if (day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        continue;
                    }
                    String status = random.nextDouble() < attendanceRate[i] ? PRESENT
                            : random.nextDouble() < 0.25 ? LATE : ABSENT;
                    attendance.save(new Attendance(null, student, subject, day, status, null));
                    attendanceRows++;
                }
            }
        }

        int resultRows = seedExamsAndResults(allSubjects, allStudents, ability, today, random);
        int feeRows = seedFees(allStudents, today, random);
        seedAccounts(allStudents.get(0), allTeachers.get(0));

        log.info("Loaded demo data: {} courses, {} subjects, {} teachers, {} students, {} enrollments, "
                + "{} attendance records, {} results, {} fees.",
                courseByCode.size(), allSubjects.size(), allTeachers.size(), allStudents.size(),
                enrolled, attendanceRows, resultRows, feeRows);
        // Names the setting, never its value (#44): a public demo sets its
        // own password, and the host keeps the logs
        log.info("Demo accounts: demo_admin, demo_teacher and demo_student. Their password is "
                + "sms.demo.password (demo12345 unless it is set).");
    }

    private Map<String, Course> seedCourses() {

        Map<String, Course> byCode = new LinkedHashMap<>();
        for (String[] c : COURSES) {
            Course course = new Course(null, c[0], c[1], c[2], Double.valueOf(c[3]), c[4]);
            byCode.put(c[0], courses.save(course));
        }
        return byCode;
    }

    // Subjects, each added to the curriculum of the courses that teach it (G9)
    private List<Subject> seedSubjects(Map<String, Course> courseByCode) {

        List<Subject> saved = new ArrayList<>();
        for (String[] s : SUBJECTS) {
            Subject subject = subjects.save(new Subject(null, s[0], s[1], s[2], Integer.valueOf(s[3]), s[4]));
            for (String code : s[5].split(" ")) {
                courseByCode.get(code).getSubjects().add(subject);
            }
            saved.add(subject);
        }
        return saved;
    }

    private List<Teacher> seedTeachers() {

        List<Teacher> saved = new ArrayList<>();
        for (int i = 0; i < TEACHERS.length; i++) {
            String[] t = TEACHERS[i];
            saved.add(teachers.save(new Teacher(null, t[0], t[1], email(t[0], t[1]),
                    String.format("98765%05d", 10000 + i), t[2], t[3], t[4], null)));
        }
        return saved;
    }

    private List<Student> seedStudents(Map<String, Course> courseByCode, Random random) {

        List<Course> byPosition = List.copyOf(courseByCode.values());
        List<Student> saved = new ArrayList<>();

        for (int i = 0; i < STUDENTS.length; i++) {
            String first = STUDENTS[i][0];
            String last = SURNAMES[(i * 7) % SURNAMES.length];
            Course course = byPosition.get(i < 12 ? 0 : i < 22 ? 1 : 2);

            Student student = new Student();
            student.setFirstName(first);
            student.setLastName(last);
            student.setEmail(email(first, last));
            student.setPhone(String.format("98765%05d", 20000 + i));
            student.setGender(STUDENTS[i][1]);
            student.setCourse(course);
            student.setDateOfBirth(LocalDate.of(2004, 1, 1).plusDays(random.nextInt(3 * 365)));
            student.setAddress((10 + random.nextInt(190)) + " " + STREETS[random.nextInt(STREETS.length)]
                    + ", " + CITIES[random.nextInt(CITIES.length)]);
            saved.add(students.save(student));
        }
        return saved;
    }

    // Per subject: a mid-semester exam three weeks ago (with results), a unit
    // test in a few days, which the notifications list, and the end-semester
    // exam later in the term
    private int seedExamsAndResults(List<Subject> allSubjects, List<Student> allStudents, double[] ability,
                                    LocalDate today, Random random) {

        int rows = 0;
        for (Subject subject : allSubjects) {
            Exam midSemester = exams.save(new Exam(null, "Mid Semester", subject, today.minusWeeks(3), 100, 40));
            exams.save(new Exam(null, "Unit Test 2", subject, today.plusDays(3), 50, 20));
            exams.save(new Exam(null, "End Semester", subject, today.plusWeeks(7), 100, 40));

            for (int i = 0; i < allStudents.size(); i++) {
                Student student = allStudents.get(i);
                if (!student.getCourse().getSubjects().contains(subject)) {
                    continue;
                }

                double score = ability[i] + (random.nextDouble() - 0.5) * 0.2;
                int marks = (int) Math.round(Math.max(0, Math.min(1, score)) * midSemester.getTotalMarks());
                boolean pass = GradeCalculator.isPass(marks, midSemester.getPassingMarks());
                String grade = GradeCalculator.grade(
                        GradeCalculator.percentage(marks, midSemester.getTotalMarks()), pass);

                results.save(new Result(null, student, midSemester, marks, grade, GradeCalculator.status(pass)));
                rows++;
            }
        }
        return rows;
    }

    // A semester's tuition for everyone, due two weeks ago: most paid, a few
    // overdue. An examination fee for everyone, due in three weeks.
    private int seedFees(List<Student> allStudents, LocalDate today, Random random) {

        int rows = 0;
        for (Student student : allStudents) {

            BigDecimal tuition = BigDecimal.valueOf(student.getCourse().getFees() / 2).setScale(2);
            LocalDate tuitionDue = today.minusWeeks(2);

            if (random.nextDouble() < 0.8) {
                fees.save(new Fee(null, student, "Tuition", tuition, tuitionDue,
                        tuitionDue.minusDays(random.nextInt(20)), PAID,
                        PAYMENT_METHODS[random.nextInt(PAYMENT_METHODS.length)]));
            } else {
                fees.save(new Fee(null, student, "Tuition", tuition, tuitionDue, null, PENDING, null));
            }

            fees.save(new Fee(null, student, "Examination", new BigDecimal("1500.00"), today.plusWeeks(3),
                    null, PENDING, null));
            rows += 2;
        }
        return rows;
    }

    // One account per role, each skipped if the username is already taken
    private void seedAccounts(Student student, Teacher teacher) {

        account("demo_admin", "Demo Admin", "ROLE_ADMIN", null, null);
        account("demo_teacher", teacher.getFirstName() + " " + teacher.getLastName(), "ROLE_TEACHER", null, teacher);
        account("demo_student", student.getFirstName() + " " + student.getLastName(), "ROLE_STUDENT", student, null);
    }

    private void account(String username, String fullName, String role, Student student, Teacher teacher) {

        if (users.existsByUsername(username)) {
            log.info("Demo account {} not created: the username is taken.", username);
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(username.replace('_', '.') + "@example.com");
        user.setPassword(passwordEncoder.encode(demoPassword));
        user.setRole(role);
        user.setEnabled(true);
        user.setStudent(student);
        user.setTeacher(teacher);
        users.save(user);
    }

    private static String email(String first, String last) {
        return (first + "." + last).toLowerCase() + "@example.com";
    }
}
