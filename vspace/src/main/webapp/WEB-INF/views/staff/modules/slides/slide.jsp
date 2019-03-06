<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
//# sourceURL=click.js

function changeClass() {
	alert("hi1");
	$("#addImgAlert").show();
}

$(document).ready(function() {
	$("#uploadImage").on("click", function(){
	    alert("The paragraph was clicked.");
		alert("hi");
		$("#addImgAlert").hide();
	  });
	$("#addImgAlert").draggable();
});





</script>
<h1>Slide: ${slide.name}</h1>
<h3>Description: ${slide.description}</h3>
	
<div class="alert alert-light" role="alert">
  Created on <span class="date">${slide.creationDate}</span> by ${slide.createdBy}.
  <br>
  Modified on <span class="date">${slide.modificationDate}</span> by ${slide.modifiedBy}.
</div>


<nav class="navbar navbar-expand-sm navbar-light bg-light">
  <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    Add
  </button>
  <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
  <ul>
    <li class="dropdown-item" id="addImage" onclick="changeClass()">Add Image</li>
    <li class="dropdown-item" id="addText">Add Text</li>
   </ul>
  </div>
</nav>


<div id="addImgAlert" class="alert alert-secondary" role="alert" style="cursor:move; width:340px; height: 130px; display:none; position: absolute; top: 100px; right: 50px; z-index:999">
	<h6><small>Upload Image: </small></h6>
	<input type="file" name="file" rows="5" cols="500" id="file" /><br><br>
        <p class="mb-0 text-right"><button type="submit" id="uploadImage" class="btn btn-primary btn-xs">Upload Image</button> &nbsp
	<button id="cancelBgImgBtn" type="button" class="btn light btn-xs">Cancel</button></p>
</div>

    
