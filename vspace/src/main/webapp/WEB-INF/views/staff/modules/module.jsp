<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="t"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
//# sourceURL=click.js
$(document).ready(function($) {

	$("#addSlideButton").on("click", function(e) {
		$("#createSlideAlert").show();
	});

	$("#cancelSlideBtn").click(function() {
		$("#createSlideAlert").hide();
	});
	
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
});
</script>

<h1>Module: ${module.name}</h1>
<h3>Description: ${module.description}</h3>
<div id="message" style="text-align:center; color:#008000"></div>
<div class="alert alert-light" role="alert">
	Created on <span class="date">${module.creationDate}</span> by
	${module.createdBy}. <br> Modified on <span class="date">${module.modificationDate}</span>
	by ${module.modifiedBy}.
</div>

<c:set var="sequences" value="${sequences}"></c:set>
<c:if test="${sequences.size() > 0}">
	<div style="padding-bottom: 10px;">
		<c:url value="/staff/module/${module.id}/sequence/start" var="postUrl" />
		<form:form method="POST"
			action="${postUrl}?${_csrf.parameterName}=${_csrf.token}">
			<label for="sequences" style="width: 200px">Select the start
				sequence of the Module:</label>
			<select class="form-control" name="sequenceParam"
				style="width: 200px; display: inline; padding-bottom: 110px">
				<option id="Select" value="Select">Select</option>
				<c:forEach items="${sequences}" var="sequences">
					<option id=${sequences.id } value=${sequences.id
						} <c:if test="${sequences==module.startSequence}">selected</c:if>>${sequences.name}</option>
				</c:forEach>
			</select>
			<p style="display: inline; padding-left: 10px; padding-top: 1000px;">
				<input class="btn btn-primary" type="submit" value="submit" />
			</p>
		</form:form>
	</div>
</c:if>

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
						<div class="card" style="max-width: 18rem; margin-bottom:10px;">
							<div align="left" class="card-body">
								<a href="<c:url value="/staff/module/${module.id}/slide/${slide.id}" />">
									<h5 class="card-title">${slide.name}</h5>
									<p class="card-text">${slide.description}</p>
								</a>						
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
