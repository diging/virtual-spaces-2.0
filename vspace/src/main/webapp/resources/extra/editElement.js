/**
 * Shows the form fields of the selected language tab and hides the others.
 */
function openForm(evt, label) {
  const tabcontent = document.getElementsByClassName("tabcontent");
  for (const content of tabcontent) {
    content.style.display = "none";
  }
  const tablinks = document.getElementsByClassName("tablinks");
  for (const link of tablinks) {
    link.classList.remove("active");
  }
  const selected = document.getElementById(label);
  if (selected) {
    selected.style.display = "block";
  }
  evt.currentTarget.classList.add("active");
  evt.preventDefault();
}
