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
			data = JSON.parse(data);
			var imageBlock = $('<div class="valueDiv card card-body"><div class="row"><div class="col"><img class="img" src="#" /></div><div class="col"><input type="hidden" id="deleteTextId"><input class="btn btn-danger deleteImage" type="submit" value="Delete" style="float: right;"></div></div></div>');
			reader.onload = function () {
				imgTag = $('.img', imageBlock)
				imgTag.attr('src', reader.result);
				imgTag.attr('width', '800px');
				$('#slideSpace').append(imageBlock); 
				$(imageBlock).attr( 'id', data["imageBlock"]);
		        $('#'+data["imageBlock"]+ ' #deleteTextId').attr( 'value', data["imageBlock"]);
			}
			reader.readAsDataURL(file);
			// clear previous upload from form
			$( '#imageUploadForm' ).each(function(){
			    this.reset();
			});
		},
		error: function(data) {
			--contentCount;
			var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try to submit again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
			$('.error').append(alert); 
			$(".error").delay(4000).slideUp(500, function(){
			    $(".error").empty();
			});
			$( '#imageUploadForm' ).each(function(){
			    this.reset();
			});
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
	
	$(document).on("click", ".deleteText", function(e) {
		$("#confirmDeleteTextAlert").show();
		var alert = $('<input type="hidden" id="deleteTextId">');
		$(alert).attr( 'value', $(this).siblings('input').val());
		$('.modal-footer').append(alert); 
  	});
	
	$(document).on("click", ".deleteImage", function(e) {
		$("#confirmDeleteImageAlert").show();
		var alert = $('<input type="hidden" id="deleteImageId">');
		$(alert).attr( 'value', $(this).siblings('input').val());
		$('.modal-footer').append(alert); 
  	});
	
	$("#uploadImage").click(function(e) {
		e.preventDefault();
		$("#addImgAlert").hide();
		uploadImage();
  	});
	
	$("#cancelSubmitText").click(function() {
		$("#addTextAlert").hide();	
	});
	
	$("#cancelDeleteText").click(function() {
		$("#confirmDeleteTextAlert").hide();
	});
	
	$("#cancelDeleteImage").click(function() {
		$("#confirmDeleteImageAlert").hide();
	});
	
	$("#cancelDelete").click(function() {
		$("#confirmDeleteImageAlert").hide();	
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
		++contentCount;
		payload["contentOrder"] = contentCount;
		// ------------- creating text content blocks ------------
		$.ajax({
		    url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/textcontent?${_csrf.parameterName}=${_csrf.token}" />",
		    type: 'POST',
		    data: payload,
		    success: function(data) {
		    	var textBlock = $('<div class="valueDiv card card-body"><div class="row"><div class="col"><p>'+payload["content"]+'</p></div><div class="col"><input type="hidden" id="deleteTextId"><input class="btn btn-danger deleteText" type="submit" value="Delete" style="float: right;"></div></div></div>');
				$(textBlock).css({
					'margin': "10px"
				});
				$('#slideSpace').append(textBlock);
		    	$(textBlock).attr( 'id', data["textBlock"]);
		        $('#'+data["textBlock"]+ ' #deleteTextId').attr( 'value', data["textBlock"]);
		        
		    },
			error: function(data) {
				--contentCount;
				var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try to submit again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
				$('.error').append(alert); 
				$(".error").delay(4000).slideUp(500, function(){
				    $(".error").empty();
				});
				$("#textBlockText").val('');
			}
		});

		
		$("#textBlockText").val('');
	});
	
	// ---------- Delete Text Block -------------
	
		$("#deleteText").on("click", function(e) {
		e.preventDefault();
		$("#confirmDeleteTextAlert").hide();
		var blockId = $('#deleteTextId').attr('value');
		$('#deleteTextId').remove()
		
		// ------------- delete text content blocks ------------
		$.ajax({
		    url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/text/" />" + blockId + "?${_csrf.parameterName}=${_csrf.token}",
		    type: 'DELETE',
		    success: function(result) {
		        $('#' + blockId).remove();
		        
		    },
			error: function(data) {
				var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try to delete again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
				$('.error').append(alert); 
				$(".error").delay(4000).slideUp(500, function(){
				    $(".error").empty();
				});
			}
		});
	});
	
	// ---------- Delete Image Block -----------
	
	$("#deleteImage").on("click", function(e) {
		e.preventDefault();
		$("#confirmDeleteImageAlert").hide();
		var blockId = $('#deleteImageId').attr('value');
		console.log(blockId);
		if(blockId == null || blockId == ''){
			var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try to delete again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
			$('.error').append(alert); 
			$(".error").delay(4000).slideUp(500, function(){
			    $(".error").empty();
			});
		} else{
			$('#deleteImageId').remove()
			// ------------- delete image content blocks ------------
			$.ajax({
			    url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/image/" />" + blockId + "?${_csrf.parameterName}=${_csrf.token}",
			    type: 'DELETE',
			    success: function(result) {
			        $('#' + blockId).remove();
			    },
				error: function(data) {
					var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try to delete again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
					$('.error').append(alert); 
					$(".error").delay(4000).slideUp(500, function(){
					    $(".error").empty();
					});
				}
			});
		}
	});
	
	$("#addImgAlert").draggable();
	$("#addTextAlert").draggable();
	$("#confirmDeleteTextAlert").draggable();
	$("#confirmDeleteImageAlert").draggable();

});
</script>

<ol class="breadcrumb">
  <li class="breadcrumb-item"><a href="<c:url value="/staff/dashboard" />">Dashboard</a></li>
  <li class="breadcrumb-item"><a href="<c:url value="/staff/module/list" />">Modules</a></li>
  <li class="breadcrumb-item"><a href="<c:url value="/staff/module/${module.id}" />">${module.name}</a></li>
  <li class="breadcrumb-item active">${slide.name}</li>
</ol>

<div class="error">
</div>


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

<!-- Delete Text Modal -->
<div id="confirmDeleteTextAlert" class="modal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Confirm Delete</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
          <h6>Are you sure you want to delete this text block?</h6>
      </div>
      <div class="modal-footer">
        <button id="cancelDeleteText" type="reset" class="btn light">Cancel</button>
        <button type="submit" id="deleteText" class="btn btn-primary">Submit</button>
      </div>
    </div>
  </div>
</div>

<!-- Delete Image Modal -->
<div id="confirmDeleteImageAlert" class="modal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Confirm Delete</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
          <h6>Are you sure you want to delete this image block?</h6>
      </div>
      <div class="modal-footer">
        <button id="cancelDelete" type="reset" class="btn light">Cancel</button>
        <button type="submit" id="deleteImage" class="btn btn-primary">Submit</button>
      </div>
    </div>
  </div>
</div>

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
      
        <div class="modal-body">
        	<form name="photoForm"  id="imageUploadForm" enctype="multipart/form-data" method="post">
	            <h6><small>Upload Image: </small></h6>
	            <input class="form-control" type="file" name="file" rows="5" cols="500" id="file" />
            </form>
        </div>
      
      <div class="modal-footer">
        <button id="cancelImageBtn" type="reset" class="btn light">Cancel</button>
        <button type="submit" id="uploadImage" class="btn btn-primary">Upload Image</button>
      </div>
    </div>
  </div>
</div>
    
<div id="slideSpace">
	<c:forEach items="${slideContents}" var="contents">
		<c:if test="${contents['class'].simpleName ==  'ImageBlock'}">
			<div id="${contents.id}" class="valueDiv card card-body" style="margin:10px;">
				<div class="row">
					<div class="col">
						<img id="${contents.image.id}" width="800px" src="<c:url value="/api/image/${contents.image.id}" />" />
					</div>
					<div class="col">
						<input type="hidden" id="deleteImageId" value="${contents.id}">
						<input class="btn btn-danger deleteImage" type="submit" value="Delete" style="float: right;">
					</div>
				</div>
			</div>
		</c:if>
		<c:if test="${contents['class'].simpleName ==  'TextBlock'}">
			<div id="${contents.id}" class="valueDiv card card-body" style="margin:10px;">
				<div class="row">
					<div class="col">
						<p>${contents.text}</p>
					</div>
					<div class="col">
						<input type="hidden" id="deleteTextId" value="${contents.id}">
						<input class="btn btn-danger deleteText" type="submit" value="Delete" style="float: right;">
					</div>
				</div>
			</div>
		</c:if>
	</c:forEach>
</div>




