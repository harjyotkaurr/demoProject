document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll(".archives a").forEach(link => {
        link.addEventListener("click", function(event) {
            event.preventDefault();
            const month = this.getAttribute("href").split("=")[1];
            history.pushState({}, "", "?month=" + month);
            location.reload();
        });
    });
});
