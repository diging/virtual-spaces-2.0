<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
	$(document).ready(function() {
		var vals = []
		var slides = []
		$('#ordered-slides').multiSelect({
			keepOrder : true,
			dblClick : true,
			afterInit : function(container) {
				$("#ordered-slides").find("option").each(function() {
					vals.push($(this).val());
				});
				$(".ms-selection ul").find("li").each(function(index) {
					$(this).attr('value', vals[index]);
				});
			},
			afterSelect : function(value) {
				slides = [];
				$('.ms-selection ul li.ms-selected').each(function(index, value) {
					slides.push($(this).attr('value'));					
				});
				$("#orderedSlides").val(slides);
			},
			afterDeselect : function(value, text) {
				for (var i=slides.length-1; i>=0; i--) {
	    			if (String(slides[i]) === String(value)) {
	    				slides.splice(i, 1);
	    				break;
	        		}
	    		}
				$("#orderedSlides").val(slides); 
			}
		})
		$('.ms-selection ul').sortable({
			distance : 5,
			stop : function(event, ui) {
				var new_val = []
				$('.ms-selection ul li.ms-selected').each(function(index, value) {
					new_val.push($(this).attr('value'));					
				});
				$("#orderedSlides").val(new_val);
			}
		});
		
	});
</script>

<h1>Add New Sequence</h1>

<c:url value="/staff/module/${moduleId}/sequence/add" var="postUrl" />
<form:form method="POST" action="${postUrl}?" modelAttribute="sequence">
	
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
			Slides: <br> <small>(Double click to select and drag to
				reorder)</small>
		</label> <select multiple="multiple" id="ordered-slides">
			<c:forEach items="${slides}" var="slide">
				<option value='${slide.id}' id="${slide.id}">${slide.name}</option>
			</c:forEach>
			</select>
		<form:input type="hidden" id="orderedSlides" path="orderedSlides"></form:input>
	</div>
	<button class="btn btn-primary btn-sm" type="submit" value="submit">Create
		Sequence</button>
	
</form:form>