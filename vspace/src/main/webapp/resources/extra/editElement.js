/*
 * Language tabs of the staff forms (staff/fragments/localizedFields.html).
 * Shows the panel of the clicked tab and opens the first tab on page load.
 */
(function () {
  "use strict";

  function openTab(button) {
    const container = button.closest(".localized-fields");
    container.querySelectorAll(".tabcontent").forEach(function (panel) {
      panel.style.display = "none";
    });
    container.querySelectorAll(".tablinks").forEach(function (tab) {
      tab.classList.remove("active");
      tab.setAttribute("aria-selected", "false");
    });
    const panel = document.getElementById(button.dataset.tab);
    if (panel) {
      panel.style.display = "block";
    }
    button.classList.add("active");
    button.setAttribute("aria-selected", "true");
  }

  document.addEventListener("click", function (evt) {
    const button = evt.target.closest(".localized-fields .tablinks");
    if (button) {
      evt.preventDefault();
      openTab(button);
    }
  });

  document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".localized-fields").forEach(function (container) {
      const first = container.querySelector(".tablinks");
      if (first) {
        openTab(first);
      }
    });
  });
})();
