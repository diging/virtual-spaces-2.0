<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<link href="<c:url value="/resources/multiselect/css/multiselect.css" />" rel="stylesheet">
<script src="<c:url value="/resources/multiselect/js/multiselect.min.js" />" ></script>

<script>
//# sourceURL=click.js
$(document).ready(function(){
	var vals = []
	var choices = []
		
	$('select[id="type"]').change(function(){
	    if ($(this).val() == "branchingPoint"){
	    	$('#sequences').show();
	    	$('#selectSequence').multiSelect({		    		
	    		dblClick : true,
	    		afterInit : function(container) {
	    			$("#selectSequence").find("option").each(function() {
	    				vals.push($(this).val());
	    			});
	    			$(".ms-selection ul").find("li").each(function(index) {
	    				$(this).attr('value', vals[index]);
	    			});
	    		},
	    		afterSelect : function(value) {
	    			choices = [];
	    			$('.ms-selection ul li.ms-selected').each(function(index, value) {
	    				choices.push($(this).attr('value'));					
	    			});
	    			$("#choices").val(choices);
	    		},
	    		afterDeselect : function(value, text) {
	    			for (var i=choices.length-1; i>=0; i--) {
	    				if (String(choices[i]) === String(value)) {
	    					choices.splice(i, 1);
	    					break;
	    	    		}
	    			}
	    			$("#choices").val(choices);
	    		}
	        });	    				
	     } else {
	    	 $('#sequences').hide();
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
 	 <label for="slideType" class="col-md-2 col-form-label">Type:</label>
         <form:select path="slideType" id="type" class="form-control-xs target" style="height:50px;width:300px;">
             <option selected value="">Choose from existing</option>
             <option  id="slide" value="slide" label="Slide" >Slide</option>
             <option id="branchingPoint" value="branchingPoint" label="BranchingPoint">Branching Point</option>
         </form:select>
 </div>
  
 <div class="form-group row" id="sequences" style="display:none;">
    <label for="choices" class="col-md-2 col-form-label">Add Choices:</label>	
            <select multiple="multiple" id="selectSequence" name="selectSequence" class="form-control form-control-sm" style="width: 68px;">
				 <c:forEach items="${sequences}" var="sequence">
					<option value='${sequence.id}' id="${sequence.id}">${sequence.name}</option>
				 </c:forEach>
		   </select>
	<form:input type="hidden" id="choices" path="choices"></form:input>
 </div> 
   
 <div class="form-group row">
 	<label for="description" class="col-md-2 col-form-label">Description:</label>
 	<form:textarea class="form-control col-md-10" rows="5" cols="30" id="description" path="description" />
 </div>          
  <button class="btn btn-primary btn-sm" type="submit" value="submit">Create Slide</button>
</form:form>