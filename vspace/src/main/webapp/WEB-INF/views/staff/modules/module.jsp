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
	<div class="container" id="header" style="margin-bottom:10px;">
		<div class="row">
			<div class="col">
				<div
					class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1">
					<span style="float: left; font-size: medium; padding-top: 3px;">SLIDES</span>
					<a class="d-flex align-items-center text-muted"
						href="<c:url value="${module.id}/slide/add" />"> <span
						data-feather="plus-circle"></span></a>
				</div>
			</div>
			<div class="col">
				<div
					class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1">
					<span style="float: left; font-size: medium; padding-top: 3px;">SEQUENCES</span>
					<a class="d-flex align-items-center text-muted"
						href="<c:url value="${module.id}/sequence/add" />"> <span
						data-feather="plus-circle"></span></a>
				</div>
			</div>
			<div class="col">
				<div
					class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1">
					<span style="float: left; font-size: medium; padding-top: 3px;">START
						SEQUENCE</span> <span id="startSequence"
						style="float: right; padding-top: 3px;"></span>
				</div>
			</div>
		</div>
	</div>
	<div class="container" id="table">
		<div class="row">
			<div class="col">
				<c:forEach items="${slides}" var="slide">
					<div class="card" style="max-width: 18rem; margin-bottom:10px;">
						<div align="left" class="card-body">
							<a
								href="<c:url value="/staff/module/${module.id}/slide/${slide.id}" />">
								<h5 class="card-title">${slide.name}</h5>
								<p class="card-text">${slide.description}</p>
							</a>
						</div>
					</div>
				</c:forEach>
			</div>
			
			<div class="col">
				<c:forEach items="${sequences}" var="sequences">
					<div class="card" style="max-width: 18rem; margin-bottom:10px;">
						<div align="left" class="card-body">
							<a
								href="<c:url value="/staff/module/${module.id}/sequences/${sequences.id}" />">
								<h5 class="card-title">${sequences.name}</h5>
								<p class="card-text">${sequences.description}</p>
							</a>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>

</body>