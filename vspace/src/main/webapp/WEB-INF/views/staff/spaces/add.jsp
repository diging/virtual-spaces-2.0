<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="en">
<head>

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/js/select2.min.js"></script>

<script>
$(document).ready(function(){
	$("#imageID").select2({
		templateResult: formatState,
		});
	});
	function formatState (state) {
		if (!state.id) {
			return state.text;
		}
		var tbaseUrl = "/vspace/api/image";
		var $state = $(
				'<span><img src="' + tbaseUrl + '/' + state.id + '" class="img-thumbnail" /> ' + state.text + '</span>'
		);
		return $state;
	};
</script>
</head>

<body>
<h1>Add new Space</h1>

<c:url value="/staff/space/add" var="postUrl" />
<form:form method="POST" action="${postUrl}?${_csrf.parameterName}=${_csrf.token}" modelAttribute="space" enctype="multipart/form-data">
	<div class="form-group row">
		<label for="name" class="col-md-2 col-form-label">Space Name:</label>
		<form:input type="text" class="form-control col-md-10" id="name"
			path="name" />
	</div>

	<div class="form-group row">
		<label for="description" class="col-md-2 col-form-label">Description:</label>
		<form:textarea class="form-control col-md-10" rows="5" cols="30"
			id="description" path="description" />
	</div>

	<div class="form-group row">
		<label for="description" class="col-md-2 col-form-label">Background image:</label>
		<div class="form-group row">
			<input type="file" name="file" class="form-control col-md-10" rows="5" cols="30" id="file" />
			<select id="imageID" name="imageID" class="form-control-xs target" style="height:50px;width:300px;">
		        <option selected value="">Choose from existing</option>
		        <c:forEach items="${images}" var="image">
		        	<option value="${image.id}">${image.filename}</option>
		        </c:forEach>
			</select>	
		</div>
	</div>

	<button class="btn btn-primary btn-sm" type="submit" value="submit">Create
		Space</button>
</form:form>
</body>
</html>