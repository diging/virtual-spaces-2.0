<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<script>
	$(document).ready(function () {

		$("#addSlideButton").on("click", function (e) {
			$("#createSlideAlert").show();
		});
		$("#createSlideAlert").draggable();

		$("#cancelSlideBtn").click(function () {
			$("#createSlideAlert").hide();
		});
	});
</script>

<h1>Module: ${module.name}</h1>
<div class="alert alert-light" role="alert">
	Created on <span class="date">${module.creationDate}</span> by ${module.createdBy}.
	<br>
	Modified on <span class="date">${module.modificationDate}</span> by ${module.modifiedBy}.
</div>
<h5>Description:</h5>
<p>${module.description}</p>



<body>
	<div id="result"></div>
	<table border="0" height="50%" style=" margin-top:50px;">
		<tr>
			<td style="width: 20rem; padding-left: 15px; border:1;">
				<div
					class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1">
					<span style="float:left; font-size:medium; padding-top: 3px;">SLIDES</span>
					<a class="d-flex align-items-center text-muted" href="<c:url value=" ${module.id}/slide/add" />">
					<span data-feather="plus-circle"></span></a></div>
			</td>
			<td>&nbsp;</td>
			<td style="width: 20rem; padding-left: 15px; border:1;">
				<div
					class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1">
					<span style="float:left; font-size:medium; padding-top: 3px;">SEQUENCES</span>
					<span data-feather="plus-circle" style="float:right; padding-top: 3px;"></span></div>
			</td>
			<td>&nbsp;</td>
			<td style="width: 20rem; padding-left: 15px; border:1;">
				<div
					class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1">
					<span style="float:left; font-size:medium; padding-top: 3px;">START SEQUENCE</span>
					<span id="startSequence" style="float:right; padding-top: 3px;"></span></div>
			</td>
		</tr>
		<c:forEach items="${slides}" var="slide">
			<tr>
				<td style="padding-left: 22px; border-style:hidden; padding-top: 6px; padding-bottom: 6px;">
					<div class="card" style="max-width: 18rem;">
						<div align="left" class="card-body d-flex align-items-center" style="position:relative;">
							 <a href="<c:url value="/staff/module/${module.id}/slide/${slide.id}" />">
								<h5 class="card-title">${slide.name}</h5>
								<p class="card-text">${slide.description}</p></a>
							<div class='block2' style="width: 40px; position: absolute; top: 6px; right:6px;">
								<a href="#" data-record-id="${slide.id}"
								data-url="<c:url value="/staff/module/${module.id}/slide/remove/${slide.id}?${_csrf.parameterName}=${_csrf.token}"/>"
								data-call-on-success="<c:url value="/staff/module/${module.id}"/>"
								data-call-on-error="<c:url value="/staff/module/${module.id}"/>"
								data-toggle="modal" data-target="#confirm-delete"><span style="float: right;" data-feather="trash-2"></span></a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>

<jsp:include page="../../deleteModal.jsp">
	<jsp:param name="elementType" value="Slide" />
</jsp:include>