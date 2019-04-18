<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1>Edit Image: ${imageForm.fileName}</h1>

<c:url value="/staff/image/${imageId}/edit" var="postUrl" />
<form:form method="POST" action="${postUrl}?${_csrf.parameterName}=${_csrf.token}" modelAttribute="imageForm" enctype="multipart/form-data">

	<div class="form-group row">
		<label for="fileName" class="col-md-2 col-form-label">Image Name:</label>
		<form:input type="text" class="form-control col-md-10" id="fileName"
			path="fileName" />
	</div>

	<div class="form-group row">
		<label for="description" class="col-md-2 col-form-label">Description:</label>
		<form:textarea class="form-control col-md-10" rows="5" cols="30"
			id="description" path="description" />
	</div>
	
	<button class="btn btn-primary btn-sm" type="submit" value="submit">Save
		Image</button>
	<a href="<c:url value="/staff/display/image/${imageId}" />" class="btn btn-light btn-sm">Cancel</a>
</form:form>