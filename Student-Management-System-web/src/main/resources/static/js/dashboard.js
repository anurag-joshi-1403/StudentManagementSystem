/* ===========================================
   Dashboard JS
=========================================== */

function updateDateTime() {

    const now = new Date();

    const dateOptions = {

        weekday: 'long',
        day: 'numeric',
        month: 'long',
        year: 'numeric'

    };

    const timeOptions = {

        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'

    };

    const dateElement = document.getElementById("currentDate");
    const timeElement = document.getElementById("currentTime");

    if (dateElement) {

        dateElement.innerHTML =
            now.toLocaleDateString('en-IN', dateOptions);

    }

    if (timeElement) {

        timeElement.innerHTML =
            now.toLocaleTimeString('en-IN', timeOptions);

    }

}

updateDateTime();

setInterval(updateDateTime,1000);