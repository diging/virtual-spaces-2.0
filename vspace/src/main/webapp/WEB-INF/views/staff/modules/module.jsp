<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
//# sourceURL=click.js
var slideIdToDelete = 0;

//------------Deleting Slides-------------------
function deleteSlide(slideId, hasSequence) {
	console.log("hasSequence: ------>  "+hasSequence)
	if(hasSequence == 0) {
		$.ajax({
	        url: "<c:url value="/staff/module/${module.id}/slide/" />" + slideId +'?${_csrf.parameterName}=${_csrf.token}',
	        type: 'DELETE',
	        cache       : false,
	        contentType : false,
	        success: function(data) {
	        	$("#"+slideId).closest('.slide').remove();
	        },
	        error: function(data) {
	        	var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
	        	$('.error').append(alert);
	        }
	    });
	} else {
		slideIdToDelete = slideId;
		$("#deleteSlideAlert").show();
	}  
}

function deleteSlideFromSequence(slideId, hasSequence){
	$.ajax({
        url: "<c:url value="/staff/module/${module.id}/slide/" />" + slideId + "/" + hasSequence +'?${_csrf.parameterName}=${_csrf.token}',
        type: 'DELETE',
        cache       : false,
        contentType : false,
        success: function(data) {
        	$("#"+slideId).closest('.slide').remove();
        },
        error: function(data) {
        	var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
        	$('.error').append(alert);
        }
    });
}

$(document).ready(function($) {
	$("#addSlideButton").on("click", function (e) {
		$("#createSlideAlert").show();
	});
	$("#createSlideAlert").draggable();
	
	$("#closeSlide").click(function (){
		slideIdToDelete = 0;
		$("#deleteSlideAlert").hide();
	});
	
	$("#cancelSlideBtn").click(function () {
		$("#createSlideAlert").hide();
	});
		
	$("#cancelSlideDelButton").click(function () {
		slideIdToDelete = 0;
		$("#deleteSlideAlert").hide();
	});
	$("#deleteSlideAlert").draggable();
	
	$(".sequence").on("click", function(e) {
		$(".sequence").css({ 'border' : ''});
		$(this).css("border", "solid #c1bb88");
		var sequenceId = this.id;
		$('#selectedSequence').empty();
		
		$.ajax({
			type: "GET",
			url: "<c:url value="/staff/module/${module.id}/sequence/"/>" +sequenceId+  "/slides",
			async: false,
			success: function(response) {
				$.each(response, function (index, slide) {
					$('#selectedSequence').append(''+
							'<div class="card sequence" style="max-width: 18rem; margin-bottom:10px;">'+
							'<div align="left" class="card-body">'+
							'<a href="<c:url value="/staff/module/${module.id}/slide/"/>'+slide.id+'" >'+
							'<h5 class="card-title">'+slide.name+'</h5>'+
							'<p class="card-text">'+slide.description+'</p>'+'</a>'+
							'</div></div>');
					});
				}
		});
	});
	
	$("#deleteSlideFromSequence").on("click", function() {
		$("#deleteSlideAlert").hide();
		deleteSlideFromSequence(slideIdToDelete, 1);
	});
				
});	

</script>

<h1>Module: ${module.name}</h1>
<h3>Description: ${module.description}</h3>

<div class="alert alert-light" role="alert">
	Created on <span class="date">${module.creationDate}</span> by
	${module.createdBy}. <br> Modified on <span class="date">${module.modificationDate}</span>
	by ${module.modifiedBy}.
</div>

<div id="result"></div>
<div class="container" id="header" style="margin-bottom:10px;">
	<div class="row">
		<div class="col justify-content-center">
			<div class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1">
				<span style="float: left; font-size: medium; padding-top: 3px;">SLIDES</span>
				<a class="d-flex align-items-center text-muted"
					href="<c:url value="${module.id}/slide/add" />"> <span
					data-feather="plus-circle"></span></a>
			</div>
		</div>
		<div class="col">
			<div class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1">
				<span style="float: left; font-size: medium; padding-top: 3px;">SEQUENCES</span>
				<a class="d-flex align-items-center text-muted"
					href="<c:url value="${module.id}/sequence/add" />"> <span
					data-feather="plus-circle"></span></a>
			</div>
		</div>
		<div class="col">
			<div class="card-header sidebar-heading d-flex align-items-center px-3 mt-4 mb-1">
				<span style="float: left; font-size: medium; padding-top: 3px;">SLIDES IN
					SEQUENCE</span> <span id="startSequence"
					style="float: right; padding-top: 3px;"></span>
			</div>
		</div>
	</div>
	<div class="container" id="table">
		<div class="row">
			<div class="col justify-content-center" style="padding-left: 30px;">
				<c:forEach items="${slides}" var="slide">
					<div id="${slide.key.id}" class="card slide" style="max-width: 18rem; margin-bottom:10px;">
						<div align="left" class="card-body d-flex align-items-center" style="position:relative;">
							<a href="<c:url value="/staff/module/${module.id}/slide/${slide.key.id}" />">
							<h5 class="card-title">${slide.key.name}</h5><p class="card-text">${slide.key.description}</p></a>						
							<div class='block2' style="width: 40px; position: absolute; top: 6px; right:6px;">
                            <a id="${slide.key.id}" href="javascript:deleteSlide('${slide.key.id}','${slide.value}')" class="deleteSlide" style="float: right;"><span style="float: right;" data-feather="trash-2"></span></a>
							</div>
						</div>
					</div>		
				</c:forEach>
			</div>
			<div class="col justify-content-center" style="padding-left: 65px;">
				<c:forEach items="${sequences}" var="sequences">
					<div id=${sequences.id} var class="card sequence" style="max-width: 18rem; margin-bottom:10px;">
						<div align="left" class="card-body">
							<a href="<c:url value="/staff/module/${module.id}/sequence/${sequences.id}" />"><span style="float: right" data-feather="eye"></span></a>
							<font color="#796d05"><h5 class="card-title">${sequences.name}</h5>
							<p class="card-text">${sequences.description}</p></font>	
						</div>
					</div>
				</c:forEach>
			</div>
			<div id="selectedSequence" class="col justify-content-center" style="padding-left: 60px; padding-right:20px;"></div>
		</div>
	</div>
</div>

<div id="deleteSlideAlert" class="modal-dialog alert alert-secondary" role="alert"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="cursor: move; width: 400px; height: 250px; display: none; position: absolute; top: 300px; z-index: 999">
	<div class="modal-content">
	<div class="modal-header">
		<h4 class="modal-title" id="deleteModalTitle">Confirm Deletion?</h4>
		<button type="button" id="closeSlide" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	</div>
	<div class="modal-body">
		<p> This Slide is a part of another Sequence. Are you sure you want to delete it?</p>
	</div>
	<div class="modal-footer">
		<button type="button" id="cancelSlideDelButton" class="btn btn-default" data-dismiss="modal">Cancel</button>
		<button id="deleteSlideFromSequence" type="submit" class="btn btn-danger btn-ok deleteSlide">Delete</button>
	</div>
	</div>
</div>
