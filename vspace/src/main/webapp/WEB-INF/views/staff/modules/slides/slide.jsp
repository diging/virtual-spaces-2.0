<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
//# sourceURL=click.js
var contentCount = ${fn:length(slideContents)};
	
function uploadImage() {
	var file = document.getElementById('file').files[0];
	var reader  = new FileReader();
	
	reader.onload = function () {
		var imageblock = $('<img src="#" />');
		imageblock.attr('src', reader.result);
		imageblock.attr('width', '800px');
		$('#slideSpace').append(imageblock);        
	}
	++contentCount;
	reader.readAsDataURL(file);
	
	var file = document.getElementById('file').files[0];
	var formData = new FormData();
	formData.append('file', file);
	formData.append('contentOrder', contentCount);
	$.ajax({
		enctype: 'multipart/form-data',
		// ------------- creating image content blocks ------------
		url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/image?${_csrf.parameterName}=${_csrf.token}" />",
		type: 'POST',
		cache       : false,
		contentType : false,
		processData : false,
		data: formData,
		
		success: function(data) {
			// do nothing for now
		},
		error: function(data) {
			console.log(data);
		}
	});
} 
	
$(document).ready(function() {
	$("#addText").click(function() {
		$("#addTextAlert").show();
  	});
	
	$("#addImage").click(function() {
		$("#addImgAlert").show();
  	});
	
	$("#uploadImage").click(function(e) {
		e.preventDefault();
			$("#addImgAlert").hide();
			uploadImage();
  	});
	
	$("#cancelSubmitText").click(function() {
		$("#addTextAlert").hide();	
	});
	
	$("#cancelImageBtn").click(function() {
		$("#image1").remove();
		$("#addImgAlert").hide();	
	});
	
	$("#submitText").on("click", function(e) {
		$("#addTextAlert").hide();
		e.preventDefault();

		var payload = {};
		payload["content"] = $("#textBlockText").val();
		
		var textblock = $('<div class="card card-body">'+payload["content"]+'</div>');
		$(textblock).css({
			'margin': "10px"
		});
		$('#slideSpace').append(textblock);
		++contentCount;
		payload["contentOrder"] = contentCount;
		// ------------- creating text content blocks ------------
		$.post("<c:url value="/staff/module/${module.id}/slide/${slide.id}/textcontent?${_csrf.parameterName}=${_csrf.token}" />", payload, function(data) {
		});
		
		$("#textBlockText").val('')
	});
	$("#addImgAlert").draggable();
	$("#addTextAlert").draggable();

});
</script>

<ol class="breadcrumb">
  <li class="breadcrumb-item"><a href="<c:url value="/staff/dashboard" />">Dashboard</a></li>
  <li class="breadcrumb-item"><a href="<c:url value="/staff/module/list" />">Modules</a></li>
  <li class="breadcrumb-item"><a href="<c:url value="/staff/module/${module.id}" />">${module.name}</a></li>
  <li class="breadcrumb-item active">${slide.name}</li>
</ol>

<h1>Slide: ${slide.name}</h1>
<div class="alert alert-light" role="alert">
  Created on <span class="date">${slide.creationDate}</span> by ${slide.createdBy}.<br>
  Modified on <span class="date">${slide.modificationDate}</span> by ${slide.modifiedBy}.
</div>
<h5>Description:</h5>
<p>${slide.description}</p>
	


<nav class="navbar navbar-expand-sm navbar-light bg-light">
<div class="dropdown">
  <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    Add content
  </button>
  <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
    <a id="addText" class="dropdown-item" href="#">Add Text</a>
    <a id="addImage" class="dropdown-item" href="#">Add Image</a>
  </div>
</div>
</nav>

<div id="addTextAlert" class="modal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Add new Text Block</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <form name="textForm"  id="textUploadForm" enctype="multipart/form-data" method="post">
          <div class="modal-body">
            <h6><small>Enter Text: </small></h6>
            <textarea class="form-control" id="textBlockText" rows="3"></textarea>
	      </div>
	      <div class="modal-footer">
	        <button id="cancelSubmitText" type="reset" class="btn light">Cancel</button>
	        <button type="submit" id="submitText" class="btn btn-primary">Submit</button>
	      </div>
      </form>
    </div>
  </div>
</div>

<div id="addImgAlert" class="modal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Add new Image Block</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <form name="photoForm"  id="imageUploadForm" enctype="multipart/form-data" method="post">
        <div class="modal-body">
            <h6><small>Upload Image: </small></h6>
            <input class="form-control" type="file" name="file" rows="5" cols="500" id="file" />
          </div>
          <div class="modal-footer">
            <button id="cancelImageBtn" type="reset" class="btn light">Cancel</button>
            <button type="submit" id="uploadImage" class="btn btn-primary">Upload Image</button>
          </div>
      </form>
    </div>
  </div>
</div>
    
<div id="slideSpace">
	<c:forEach items="${slideContents}" var="contents">
		<c:if test="${contents['class'].simpleName ==  'ImageBlock'}">
			<div class="valueDiv"><img id="${contents.image.id}" width="800px" style="margin:10px;" src="<c:url value="/api/image/${contents.image.id}" />" /></div>
		</c:if>
		<c:if test="${contents['class'].simpleName ==  'TextBlock'}">
			<div class="valueDiv card card-body" style="margin:10px;"><p>${contents.text}</p></div>
		</c:if>
	</c:forEach>
</div>




