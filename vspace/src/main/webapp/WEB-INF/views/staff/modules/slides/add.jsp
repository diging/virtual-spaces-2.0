<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
//# sourceURL=click.js
$(document).ready(function(){
		$('select[name="slideType"]').change(function(){
		    if ($(this).val() == "branchingPoint"){
		    	$('#choices').show();	
		    	
				$.ajax({
					type: "GET",
					url: "<c:url value="/staff/module/${moduleId}/sequences"/>",
					async: false,
					success: function(response) {
						console.log(response);
					    $.each(response, function (index, sequence) {
							$('#selectSequence').append(''+
									'<option value="${sequence.id}">'+sequence.name+'</option>'+'');
							});
						}
				});
		     }        
		});
});
</script>
<h1>Add new Slide</h1>

<c:url value="/staff/module/${moduleId}/slide/add" var="postUrl" />
<form:form method="POST"
          action="${postUrl}" modelAttribute="slide">
          
 <div class="form-group row">
 	<label for="name" class="col-md-2 col-form-label">Slide Name:</label>
    <form:input type="text" class="form-control col-md-10" id="name" path="name" />
 </div>
  
 <div class="form-group row">
 	 <label for="type" class="col-md-2 col-form-label">Type:</label>
         <div>
         <select id="slideType" name="slideType" class="form-control-xs target" style="height:50px;width:300px;">
             <option selected value="">Choose from existing</option>
             <option value="slide">Slide</option>
             <option value="branchingPoint">Branching Point</option>
         </select>   
         </div>
 </div>
  
 <div class="form-group row" id="choices" style="display:none;">
    <label for="choices" class="col-md-2 col-form-label">Add Choices:</label>
    <form id="selectSequenceForm" action="<c:url value="/staff/module/" />${moduleId}?${_csrf.parameterName}=${_csrf.token}" method="post">
    	<input type="hidden" name="imageID" value="${image.id}" id="imageID" />
    		<select id="selectSequence" name="selectSequence" class="form-control form-control-sm" style="width: 68px;">
    			<option>Select Sequences</option>				
            </select>
    </form>
 </div> 
   
 <div class="form-group row">
 	<label for="description" class="col-md-2 col-form-label">Description:</label>
    <form:textarea class="form-control col-md-10" rows="5" cols="30" id="description" path="description" />
 </div> 
          
  <button class="btn btn-primary btn-sm" type="submit" value="submit">Create Slide</button>
</form:form>