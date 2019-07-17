<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1>Add new Slide</h1>

<c:url value="/staff/module/${moduleId}/slide/add" var="postUrl" />
<form:form method="POST"
          action="${postUrl}" modelAttribute="slide">
          
 <div class="form-group row">
 	<label for="name" class="col-md-2 col-form-label">Slide Name:</label>
    <form:input type="text" class="form-control col-md-10" id="name" path="name" />
 </div>
  
 <div class="form-group row">
 	 <label for="type" class="col-md-2 col-form-label">Slide Type:</label>
         <div>
         <select id="type" name="type" class="form-control-xs target" style="height:50px;width:300px;" path="type">
             <option selected value="">Choose from existing</option>
             <option value="slide">Slide</option>
             <option value="branchingPoint">Branching Point</option>
         </select>   
         </div>
 </div>
 
 <div class="form-group row" id="cardioyes" style="display:none;">
    <label for="How Long?">Add Choices:</label>
    <form:input type="text" class="form-control col-md-10" id="choices" path="choices" />
  </div> 
  
 <div class="form-group row">
 	<label for="description" class="col-md-2 col-form-label">Description:</label>
    <form:textarea class="form-control col-md-10" rows="5" cols="30" id="description" path="description" />
 </div> 
          
  <button class="btn btn-primary btn-sm" type="submit" value="submit">Create Slide</button>
</form:form>