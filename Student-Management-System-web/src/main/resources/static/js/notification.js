const bellButton = document.getElementById("notificationBtn");

const dropdown = document.getElementById("notificationDropdown");

if(bellButton){

    bellButton.addEventListener("click",function(e){

        e.stopPropagation();

        dropdown.classList.toggle("show");

    });

}

window.addEventListener("click",function(){

    dropdown.classList.remove("show");

});