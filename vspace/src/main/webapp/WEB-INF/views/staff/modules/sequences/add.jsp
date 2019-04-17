<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
$('#keep-order').multiSelect({ keepOrder: true });
</script>

<h1>Add new Sequence</h1>

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
    <form:textarea class="form-control col-md-10" rows="5" cols="30" id="description" path="description" />
    <select id='keep-order' multiple='multiple'>
  <option value='elem_1'>elem 1</option>
  <option value='elem_2'>elem 2</option>
  <option value='elem_3'>elem 3</option>
  <option value='elem_4'>elem 4</option>
  <option value='elem_100'>elem 100</option>
</select>
 </div> 
          
  <button class="btn btn-primary btn-sm" type="submit" value="submit">Create Sequence</button>
</form:form>



