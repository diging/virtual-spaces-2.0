<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script>
	$(document).ready(function($) {

		$("#addSlideButton").on("click", function(e) {
			$("#createSlideAlert").show();
		});
		//$("#createSlideAlert").draggable();

		$("#cancelSlideBtn").click(function() {
			$("#createSlideAlert").hide();
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

<body>
	<div id="result"></div>
	<table border="0" width="100%" height="50%" style="margin-top: 50px;">
		<tr>
			<td style="width: 20rem; padding-left: 15px; border: 1;">
				<div
					class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1">
					<span style="float: left; font-size: medium; padding-top: 3px;">SLIDES</span>
					<a class="d-flex align-items-center text-muted"
						href="<c:url value="${module.id}/slide/add" />"> <span
						data-feather="plus-circle"></span></a>
				</div>
			</td>
			<td>&nbsp;</td>
			<td style="width: 20rem; padding-left: 15px; border: 1;">
				<div
					class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1">
					<span style="float: left; font-size: medium; padding-top: 3px;">SEQUENCES</span>
					<a class="d-flex align-items-center text-muted"
						href="<c:url value="${module.id}/sequence/add" />"> <span
						data-feather="plus-circle"></span></a>
				</div>
			</td>
			<td>&nbsp;</td>
			<td style="width: 20rem; padding-left: 15px; border: 1;">
				<div
					class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1">
					<span style="float: left; font-size: medium; padding-top: 3px;">START
						SEQUENCE</span> <span id="startSequence"
						style="float: right; padding-top: 3px;"></span>
				</div>
			</td>
		</tr>
		<tr>
		<td>
				<table width="100%" height="100%" style="margin-top: 50px;">
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
		
		</td>
		<td>&nbsp;</td>
		<td>
		<table  width="100%" height="100%" style="margin-top: 50px;" align="top">
		<c:forEach items="${sequences}" var="sequence">
			<tr>
				<td
					style="padding-left: 22px; border-style: hidden; padding-top: 6px; padding-bottom: 6px;">
					<div class="card" style="max-width: 18rem;">
						<div align="left" class="card-body">
							<a
								href="<c:url value="/staff/module/${module.id}/sequence/${sequence.id}" />">
								<h5 class="card-title">${sequence.name}</h5>
								<p class="card-text">${sequence.description}</p>
							</a>
						</div>
					</div>
				</td>
			</tr>
		</c:forEach>
					</table>
		</td>
		</tr>
	</table>
</body>