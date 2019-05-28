<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
	//-------- edit description --------
	$("#submitDescription").hide()
	$("#editDescription").click(function() {
		var val = $("#description").val();
		$("#description").html(function (i, val) {
	        return '<textarea id="newDescription" style="width:250%; height:200%;" class="form-control" type="text">'+val+'</textarea>';
	    });
		$("#editDescription").hide()
		$("#submitDescription").show()
		
	});
	
	$("#submitDescription").click(function() {
		var formData = new FormData();
		formData.append('description', $("#newDescription").val());
		$.ajax({
		url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/edit/description?${_csrf.parameterName}=${_csrf.token}" />",
		type: 'POST',
		cache       : false,
		contentType : false,
		processData : false,
		data: formData,
		enctype: 'multipart/form-data',
		success: function(data) {
			// replace text box with new description
			$("#submitDescription").hide()
			$("#editDescription").show()
			var val = $("#newDescription").val();
			$("#newDescription").remove()
			$("#description").text(val)
			
		},
		error: function(data) {
			console.log(data);
			var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
			$('.error').append(alert); 
			$(".error").delay(4000).slideUp(500, function(){
			    $(".error").empty();
			});
		}
	});
		
  	});
	
	//------- edit title --------
	$("#submitTitle").hide()
	$("#editTitle").click(function() {
		$('<div class="col-4"><input id="newTitle" class="form-control" type="text"></div>').insertAfter( "#title" );
		var val = $("#title").text()
		var val2 = val.split(": ")[1] 
		console.log(val2);
		$('#title').text('Slide: ')
		$("#newTitle").val(val2)
		$("#editTitle").hide()
		$("#submitTitle").show()
		
	});
	
	$("#submitTitle").click(function() {
		var formData = new FormData();
		formData.append('title', $("#newTitle").val());
		$.ajax({
		url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/edit/title?${_csrf.parameterName}=${_csrf.token}" />",
		type: 'POST',
		cache       : false,
		contentType : false,
		processData : false,
		data: formData,
		enctype: 'multipart/form-data',
		success: function(data) {
			// replace text box with new description
			$("#submitTitle").hide()
			$("#editTitle").show()
			var val = $("#newTitle").val();
			console.log(val)
			$("#newTitle").closest('div').remove()
			$("#title").text("Silde: " + val)
			
		},
		error: function(data) {
			console.log(data);
			var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
			$('.error').append(alert); 
			$(".error").delay(4000).slideUp(500, function(){
			    $(".error").empty();
			});
		}
	});
		
  	});
	
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
	<li class="breadcrumb-item"><a
		href="<c:url value="/staff/dashboard" />">Dashboard</a></li>
	<li class="breadcrumb-item"><a
		href="<c:url value="/staff/module/list" />">Modules</a></li>
	<li class="breadcrumb-item"><a
		href="<c:url value="/staff/module/${module.id}" />">${module.name}</a></li>
	<li class="breadcrumb-item active">${slide.name}</li>
</ol>
<div class="row align-items-center">

	<button id="editTitle" type="button"
		class="btn btn-outline-primary btn-sm"
		style="float: left; margin-left: 1.25%; margin-right: 1%;">edit</button>
	<button id="submitTitle" type="button"
		class="btn btn-outline-success btn-sm"
		style="float: left; margin-left: 1.25%; margin-right: 1%;">
		Save</span>
	</button>
	<h1 id="title" style="margin-bottom: 0%;">Slide: ${slide.name}</h1>

</div>
<div class="alert alert-light" role="alert">
	Created on <span class="date">${slide.creationDate}</span> by
	${slide.createdBy}.<br> Modified on <span class="date">${slide.modificationDate}</span>
	by ${slide.modifiedBy}.
</div>
<div style="margin-left: .1%;" class="row align-items-center">
	<button id="editDescription" type="button"
		class="btn btn-outline-primary btn-sm"
		style="margin-right: 1%; padding: .3rem .38rem; font-size: .66rem; line-height: .38; border-radius: .15rem; margin-left: .5%">edit</button>
	<button id="submitDescription" type="button"
		class="btn btn-outline-success btn-sm"
		style="padding: .3rem .38rem; font-size: .66rem; line-height: .38; border-radius: .15rem; margin-left: .5%;">
		Save</span>
	</button>
	<h5 style="margin-bottom: 0px;">Description:</h5>
	<p id="description"
		style="margin-top: 1%; margin-left: 1%; margin-bottom: .5rem;">${slide.description}</p>
</div>


<nav class="navbar navbar-expand-sm navbar-light bg-light">
	<div class="dropdown">
		<button class="btn btn-primary dropdown-toggle" type="button"
			id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true"
			aria-expanded="false">Add content</button>
		<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
			<a id="addText" class="dropdown-item" href="#">Add Text</a> <a
				id="addImage" class="dropdown-item" href="#">Add Image</a>
		</div>
	</div>
</nav>

<div id="addTextAlert" class="modal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Add new Text Block</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form name="textForm" id="textUploadForm"
				enctype="multipart/form-data" method="post">
				<div class="modal-body">
					<h6>
						<small>Enter Text: </small>
					</h6>
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
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form name="photoForm" id="imageUploadForm"
				enctype="multipart/form-data" method="post">
				<div class="modal-body">
					<h6>
						<small>Upload Image: </small>
					</h6>
					<input class="form-control" type="file" name="file" rows="5"
						cols="500" id="file" />
				</div>
				<div class="modal-footer">
					<button id="cancelImageBtn" type="reset" class="btn light">Cancel</button>
					<button type="submit" id="uploadImage" class="btn btn-primary">Upload
						Image</button>
				</div>
			</form>
		</div>
	</div>
</div>

<div id="slideSpace">
	<c:forEach items="${slideContents}" var="contents">
		<c:if test="${contents['class'].simpleName ==  'ImageBlock'}">
			<div class="valueDiv">
				<img id="${contents.image.id}" width="800px" style="margin: 10px;"
					src="<c:url value="/api/image/${contents.image.id}" />" />
			</div>
		</c:if>
		<c:if test="${contents['class'].simpleName ==  'TextBlock'}">
			<div class="valueDiv card card-body" style="margin: 10px;">
				<p>${contents.text}</p>
			</div>
		</c:if>
	</c:forEach>
</div>




