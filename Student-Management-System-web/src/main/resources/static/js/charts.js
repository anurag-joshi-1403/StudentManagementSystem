/* ===========================================================
   Dashboard charts

   Data arrives on data-* attributes rendered by Thymeleaf, so
   this file stays static and needs no server-side templating.
   Every guard below is deliberate: this script also loads on
   pages that have no charts on them.
   =========================================================== */

(function () {
  "use strict";

  if (typeof Chart === "undefined") {
    return; // Chart.js CDN unavailable — leave the page untouched.
  }

  // Pull a token from the stylesheet so charts and CSS cannot drift.
  const token = (name, fallback) => {
    const value = getComputedStyle(document.documentElement)
      .getPropertyValue(name)
      .trim();
    return value || fallback;
  };

  const num = (el, key) => Number(el.dataset[key] || 0);

  Chart.defaults.font.family = token("--font-sans", "system-ui, sans-serif");
  Chart.defaults.font.size = 12;
  Chart.defaults.color = token("--text-secondary", "#566074");

  /* --------------------------------------------------------
     Gender doughnut

     Three slices, not two. The student form offers Male,
     Female and Other, so a two-slice chart would not add up
     to the headline student count.
     -------------------------------------------------------- */
  const genderHost = document.getElementById("genderChartData");
  const genderCanvas = document.getElementById("genderChart");

  if (genderHost && genderCanvas) {
    const male = num(genderHost, "male");
    const female = num(genderHost, "female");
    const other = num(genderHost, "other");

    if (male + female + other === 0) {
      // Nothing to plot; a doughnut of zeros renders as an empty ring.
      genderCanvas.closest(".chart-body")?.classList.add("chart-no-data");
    } else {
      new Chart(genderCanvas, {
        type: "doughnut",
        data: {
          labels: ["Male", "Female", "Other"],
          datasets: [
            {
              data: [male, female, other],
              backgroundColor: [
                token("--c-blue", "#2563eb"),
                token("--c-pink", "#be185d"),
                token("--border-strong", "#d4dae8"),
              ],
              borderColor: token("--bg-surface", "#ffffff"),
              borderWidth: 3,
              hoverOffset: 6,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          cutout: "68%",
          plugins: {
            legend: { display: false },
            tooltip: {
              padding: 10,
              boxPadding: 4,
              callbacks: {
                label: (ctx) => {
                  const total = ctx.dataset.data.reduce((a, b) => a + b, 0);
                  const pct = total ? Math.round((ctx.parsed / total) * 100) : 0;
                  return ` ${ctx.label}: ${ctx.parsed} (${pct}%)`;
                },
              },
            },
          },
        },
      });
    }
  }

  /* --------------------------------------------------------
     Records-per-module bar chart
     -------------------------------------------------------- */
  const moduleHost = document.getElementById("moduleChartData");
  const moduleCanvas = document.getElementById("moduleChart");

  if (moduleHost && moduleCanvas) {
    const values = [
      num(moduleHost, "students"),
      num(moduleHost, "teachers"),
      num(moduleHost, "courses"),
      num(moduleHost, "subjects"),
      num(moduleHost, "enrollments"),
      num(moduleHost, "attendance"),
      num(moduleHost, "exams"),
    ];

    new Chart(moduleCanvas, {
      type: "bar",
      data: {
        labels: [
          "Students",
          "Teachers",
          "Courses",
          "Subjects",
          "Enrollments",
          "Attendance",
          "Exams",
        ],
        datasets: [
          {
            data: values,
            backgroundColor: token("--accent", "#4f46e5"),
            hoverBackgroundColor: token("--accent-hover", "#4338ca"),
            borderRadius: 6,
            maxBarThickness: 44,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: { padding: 10, displayColors: false },
        },
        scales: {
          x: {
            grid: { display: false },
            border: { display: false },
          },
          y: {
            beginAtZero: true,
            // Counts are whole rows; fractional ticks would be nonsense.
            ticks: { precision: 0 },
            grid: { color: token("--border", "#e5e9f2") },
            border: { display: false },
          },
        },
      },
    });
  }
})();
