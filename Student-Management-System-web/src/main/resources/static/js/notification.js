// Opens and closes the navbar's notification panel.
// The "show" class goes on the panel itself (.notification-dropdown), which
// is what notification.css shows; it used to go on the wrapper around it,
// so clicking the bell did nothing.
const bellButton = document.getElementById("notificationBtn");
const panel = document.getElementById("notificationPanel");

function setOpen(open) {
    panel.classList.toggle("show", open);
    bellButton.setAttribute("aria-expanded", String(open));
}

if (bellButton && panel) {

    bellButton.addEventListener("click", function (e) {
        e.stopPropagation();
        setOpen(!panel.classList.contains("show"));
    });

    // Clicks inside the panel follow their link without closing it first
    panel.addEventListener("click", function (e) {
        e.stopPropagation();
    });

    window.addEventListener("click", function () {
        setOpen(false);
    });

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape" && panel.classList.contains("show")) {
            setOpen(false);
            bellButton.focus();
        }
    });
}
