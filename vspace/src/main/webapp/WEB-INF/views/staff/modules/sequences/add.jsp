<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/multi-select/0.9.12/css/multi-select.css">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/multi-select/0.9.12/js/jquery.multi-select.min.js"></script>

<!-- https://cdnjs.cloudflare.com/ajax/libs/multi-select/0.9.12/js/jquery.multi-select.min.js -->

<script>
$('#keep-order').multiSelect({ keepOrder: true });
</script>

<h1>Add New Sequence</h1>

<c:url value="/staff/module/${moduleId}/sequence/add" var="postUrl" />
<form:form method="POST"
          action="${postUrl}" modelAttribute="sequence">
          
 <div class="form-group row">
 	<label for="name" class="col-md-2 col-form-label">Sequence Name:</label>
    <form:input type="text" class="form-control col-md-10" id="name" path="name" />
 </div> 
 
  <div class="form-group row">
 	<label for="description" class="col-md-2 col-form-label">Description:</label>
    <form:textarea class="form-control col-md-10" rows="5" cols="30" id="description" path="description" />
  </div>
 
	<div class="form-group row">
	 	<label for="Slides" class="col-md-2 col-form-label">Select Slides:</label>    
		<form:select id='keep-order' multiple='multiple' size="8" class="form-control" path="slides" data-right="#output">
		  <form:option value='elem_2'>elem 2</form:option>
		  <form:option value='elem_3'>elem 3</form:option>
		  <form:option value='elem_4'>elem 4</form:option>
		  <form:option value='elem_4'>elem 5</form:option>
		  <form:option value='elem_4'>elem 6</form:option>
		  <form:option value='elem_4'>elem 7</form:option>
		  <form:option value='elem_100'>elem 100</form:option>
		</form:select> 
	</div>

	<div class="form-group row">
        <form:select name="to[]" id="output" class="form-control" size="8" multiple="multiple" path="slides"></form:select>
    </div>
         
  <button class="btn btn-primary btn-sm" type="submit" value="submit">Create Sequence</button>
</form:form>

