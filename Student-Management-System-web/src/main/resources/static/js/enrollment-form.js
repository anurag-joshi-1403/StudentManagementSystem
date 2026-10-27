// Enrollment form (G9): the Subject list shows only the subjects of the
// chosen course. Each course option carries its subjects' ids in
// data-subjects. The server checks the same rule when the form is saved.
document.addEventListener("DOMContentLoaded", () => {
  const course = document.getElementById("course");
  const subject = document.getElementById("subject");
  if (!course || !subject) {
    return;
  }

  const showCourseSubjects = () => {
    const chosen = course.options[course.selectedIndex];
    const taught = new Set((chosen?.dataset.subjects || "").split(" ").filter(Boolean));

    for (const option of subject.options) {
      if (!option.value) {
        continue; // the "-- Select Subject --" prompt
      }
      const allowed = course.value === "" || taught.has(option.value);
      option.hidden = !allowed;
      option.disabled = !allowed;
    }

    // A subject picked for another course is no longer valid
    if (subject.selectedOptions[0]?.disabled) {
      subject.value = "";
    }
  };

  course.addEventListener("change", showCourseSubjects);
  showCourseSubjects();
});
