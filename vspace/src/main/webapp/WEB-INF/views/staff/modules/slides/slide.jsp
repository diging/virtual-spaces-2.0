<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
//# sourceURL=click.js

function addImage() {
	$("#addImgAlert").show();
}

function addText() {
	alert("inside add text");
	$("#addTextAlert").show();
}

function postData(formData) {
alert("in ajax call");
	$.ajax({
    	enctype: 'multipart/form-data',
        url: "<c:url value="/staff/module/slide/${slide.id}/content?${_csrf.parameterName}=${_csrf.token}" />",
        type: 'POST',
        data: formData,
        processData: false, 
        contentType: false,
        
        success: function(data) {
            console.log("success");
            console.log(data);
        },
        error: function(data) {
            console.log("error");
            console.log(data);
        }
    });    
}
$(document).ready(function() {

	$("#uploadImage").on("click", function(e) {
		$("#addImgAlert").hide();
		e.preventDefault();

        var payload = {};
		payload["content"] = document.getElementById('file').value;
		payload["type"] = "Image"

		$.post("<c:url value="/staff/module/slide/${slide.id}/content?${_csrf.parameterName}=${_csrf.token}" />", payload, function(data) {
				// TODO: show success/error message
			});  
  });
	$("#submitText").on("click", function(e) {
		$("#addTextAlert").hide();
		e.preventDefault();
	
		var payload = {};
		payload["content"] = document.getElementById('textarea').value;
		payload["type"] = "String"
		console.log(payload["content"]);
		
		$.post("<c:url value="/staff/module/slide/${slide.id}/content?${_csrf.parameterName}=${_csrf.token}" />", payload, function(data) {
				// TODO: show success/error message
			});  
  });
    $("#addImgAlert").draggable();
    $("#addTextAlert").draggable();
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
    <li class="dropdown-item" id="addImage" onclick="addImage()">Add Image</li>
    <li class="dropdown-item" id="addText" onclick="addText()">Add Text</li>
   </ul>
  </div>
</nav>

<form name="photoForm"  id="imageUploadForm" enctype="multipart/form-data" method="post">
<div id="addImgAlert" class="alert alert-secondary" role="alert" style="cursor:move; width:340px; height: 130px; display:none; position: absolute; top: 100px; right: 50px; z-index:999">
	<h6><small>Upload Image: </small></h6>
	<input type="file" name="file" rows="5" cols="500" id="file" /><br><br>
        <p class="mb-0 text-right"><button type="submit" id="uploadImage" class="btn btn-primary btn-xs">Upload Image</button> &nbsp
	<button id="cancelBgImgBtn" type="button" class="btn light btn-xs">Cancel</button></p>
</div>
</form>

<form name="textForm"  id="textUploadForm" enctype="multipart/form-data" method="post">
<div id="addTextAlert" class="alert alert-secondary form-group" role="alert" style="cursor:move; width:340px; height: 180px; display:none; position: absolute; top: 100px; right: 50px; z-index:999">
	<h6><small>Enter Text: </small></h6>
    <textarea class="form-control" id="textarea" rows="3"></textarea>
	<p class="mb-0 text-right"><button type="submit" id="submitText" class="btn btn-primary btn-xs">Submit</button> &nbsp
	<button id="cancelSubmitText" type="button" class="btn light btn-xs">Cancel</button></p>
</div>
</form>
    
