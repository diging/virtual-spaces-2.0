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
  <li class="breadcrumb-item active">${sequence.name}</li>
</ol>

<h1>Sequence: ${sequence.name}</h1>
<div class="alert alert-light" role="alert">
  Created on <span class="date">${sequence.creationDate}</span> by ${sequence.createdBy}.<br>
  Modified on <span class="date">${sequence.modificationDate}</span> by ${sequence.modifiedBy}.
</div>
<h5>Description:</h5>
<p>${sequence.description}</p>
	


    
<div id="slideSpace">
	<table width="100%" height="50%" style="margin-top: 50px;">
		<c:forEach items="${slides}" var="slide">
			<tr>
				<td
					style="padding-left: 22px; border-style: hidden; padding-top: 6px; padding-bottom: 6px;">
					<div class="card" style="max-width: 18rem;">
						<div align="left" class="card-body">
							<a
								href="<c:url value="/staff/module/${module.id}/slide/${slide.id}" />">
								<h5 class="card-title">${slide.name}</h5>
								<p class="card-text">${slide.description}</p>
							</a>
						</div>
					</div>
				</td>
			</tr>
		</c:forEach>
					</table>
</div>




