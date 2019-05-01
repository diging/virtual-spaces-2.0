<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
	$(document).ready(function() {
		$('#ordered-slides').multiSelect({
			keepOrder : true,
			afterSelect : function(value) {
				var get_val = $("#orderedSlideIds").val();
				var hidden_val = (get_val != "") ? get_val + "," : get_val;
				$("#orderedSlideIds").val(hidden_val + "" + value);
			},
			afterDeselect : function(value, text) {
				var get_val = $("#orderedSlideIds").val();
				var new_val = get_val.replace(value, "");
				$("#orderedSlideIds").val(new_val);
			}
		})
	});
</script>

<h1>Add New Sequence</h1>

<c:url value="/staff/module/${moduleId}/sequence/add" var="postUrl" />
<form:form method="POST" action="${postUrl}" modelAttribute="sequence">

	<div class="form-group row">
		<label for="name" class="col-md-2 col-form-label">Sequence
			Name:</label>
		<form:input type="text" class="form-control col-md-10" id="name"
			path="name" />
	</div>

	<div class="form-group row">
		<label for="description" class="col-md-2 col-form-label">Description:</label>
		<form:textarea class="form-control col-md-10" rows="5" cols="30"
			id="description" path="description" />
	</div>
	<div class="form-group row">
		<label for="Slides" class="col-md-2 col-form-label">Select
			Slides:</label> <select multiple="multiple" id="ordered-slides">
			<c:forEach items="${slides}" var="slide">
				<option value='${slide.id}'>${slide.name}</option>
			</c:forEach>
			<select>
	</div>
	<button class="btn btn-primary btn-sm" type="submit" value="submit">Create
		Sequence</button>
	<form:input type="hidden" id="orderedSlideIds" path="orderedSlideIds"></form:input>
</form:form>

</body>
</html>