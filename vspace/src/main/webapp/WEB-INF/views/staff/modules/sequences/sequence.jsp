
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<link href="<c:url value="/resources/multiselect/css/multiselect.css" />" rel="stylesheet">
<script src="<c:url value="/resources/multiselect/js/multiselect.min.js" />" ></script>

<ol class="breadcrumb">
	<li class="breadcrumb-item"><a
		href="<c:url value="/staff/dashboard" />">Dashboard</a></li>
	<li class="breadcrumb-item"><a
		href="<c:url value="/staff/module/list" />">Modules</a></li>
	<li class="breadcrumb-item"><a
		href="<c:url value="/staff/module/${module.id}" />">${module.name}</a></li>
	<li class="breadcrumb-item active">${sequence.name}</li>
</ol>



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
			$('.ms-selection ul li.ms-selected').each(function(index, value) {
				slides.push($(this).attr('value'));					
			});
			$("#orderedSlides").val(slides);
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
	
    //-------- edit contentblock description Sequence starts --------
    $("#submitDescription").hide()
    $("#cancelEditDescription").hide()	
    
    $("#editDescription").click(function() {
    	
    	// using data-attributes
    	var description = $("#description").data('value')  // gets value
        $('<textarea id="newDescription" style="margin-top: 1%;" class="form-control" type="text" data-value="">'+description+'</textarea>').insertBefore( "#description" );
        $("#description").hide()
        $("#newDescription").val(description)
        $("#newDescription").data('value',description);
        $("#editDescription").hide()
        $("#submitDescription").show()
        $("#cancelEditDescription").show()
        
        
    });

    
    // --- Submit Description ------
    
    $("#submitDescription").click(function() {
        var formData = new FormData();
        formData.append('description', $("#newDescription").val());
        $.ajax({
        url: "<c:url value="/staff/module/${module.id}/sequence/${sequence.id}/edit/description?${_csrf.parameterName}=${_csrf.token}" />",
        type: 'POST',
        cache       : false,
        contentType : false,
        processData : false,
        data: formData,
        enctype: 'multipart/form-data',
        success: function(data) {
            // replace text box with new description
            $("#submitDescription").hide()
            $("#cancelEditDescription").hide()
            $("#editDescription").show()
            var val = $("#newDescription").val();
            $("#description").text(val);
            $("#editDescription").show()
            $("#description").show() 
            $("#description").data('value',val);
            $("#newDescription").remove()
        },
        error: function(data) {
            $(".open").removeClass("open");
                var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
                $('.error').append(alert);
            }
        });
        
   });
    
    // ---- Cancel Edited Description ----
    
    $("#cancelEditDescription").click(function(){
        $("#submitDescription").hide()
        $("#editDescription").show()
        $("#cancelEditDescription").hide()
        $("#newDescription").remove()
        $("#description").show()
        
    });
    
    // ------- Edit/Save/Cancel Description ends --------
    
    
    
    //------- edit title of Sequence starts --------
    
    $("#submitTitle").hide();
    $("#cancelEditTitle").hide();
    
    $("#editTitle").click(function() {
    	// Using data attribute
    	var sequenceTitle = $("#title").data('value')  // gets value without using trim function
        $('<div class="col-4" id = "editSequenceTitle"><input id="newTitle" class="form-control" type="text"></div>').insertAfter( "#title" );
    	$('#title').text('Sequence: ')
        $("#newTitle").val(sequenceTitle)
        $("#editTitle").hide()
        $("#submitTitle").show()
        $("#cancelEditTitle").show()
    });
    	
    	
    	
    // ------ Submit title Sequence -------
    	
    $("#submitTitle").click(function() {
        var formData = new FormData();
        formData.append('title', $("#newTitle").val());
        $.ajax({
            url: "<c:url value="/staff/module/${module.id}/sequence/${sequence.id}/edit/title?${_csrf.parameterName}=${_csrf.token}" />",
            type: 'POST',
            cache       : false,
            contentType : false,
            processData : false,
            data: formData,
            enctype: 'multipart/form-data',
            success: function(data) {
                // replace text box with new description
                $("#submitTitle").hide()
                $("#cancelEditTitle").hide();
                $("#editTitle").show()
                var val = $("#newTitle").val();
                $("#editSequenceTitle").remove()
                $("#title").text("Sequence: " + val)
                $("#title").data('value', val)  // sets value
            },
            error: function(data) {
                var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
                $('.error').append(alert);
            }
        });
                
      });
    
    
   // ------  Edit Title Sequence ------
   
   $("#cancelEditTitle").click(function(){
        $("#submitTitle").hide()
        $("#editTitle").show()
        $("#cancelEditTitle").hide()
        var sequenceTitle = $("#title").data('value')  // gets value
        $("#editSequenceTitle").remove()
        $("#title").text("Sequence: "+sequenceTitle)
  
    });
   // ------- Edit/Save Sequence Title ends --------
});

</script>

<!-- title -->
<div class="row align-items-center">
	<h2 id="title" style="margin-bottom: 0%; margin-left: 1%;"
		data-value="${sequence.name}">Sequence: ${sequence.name}</h2>
	<a id="editTitle" class="btn" href="#"
		style="float: left; margin-right: 1%;"><i class="fas fa-edit"></i></a>
	<button id="submitTitle" type="button" class="btn btn-primary"
		style="float: left; margin-right: 1%;">Save</button>
	<button id="cancelEditTitle" type="button" class="btn btn-primary"
		style="margin-top: 1%; margin-bottom: 1%; margin-left: .5rem;">Cancel</button>
</div>
<div class="alert alert-light" role="alert">
	Created on <span class="date">${sequence.creationDate}</span> by
	${sequence.createdBy}.<br> Modified on <span class="date">${sequence.modificationDate}</span>
	by ${sequence.modifiedBy}.
</div>



<!-- description -->
<div style="margin-left: .1%;" class="row align-items-center">
	<h4 style="margin-bottom: 0px;">Description:</h4>
	<a id="editDescription" class="btn" href="#"
		style="font-size: .66rem; border-radius: .15rem; padding-top: .5%;"><i
		class="fas fa-edit"></i></a>
	<p id="description" style="margin-top: .5rem; margin-bottom: .5rem;"
		data-value="${sequence.description}">${sequence.description}</p>
	<button id="submitDescription" type="button"
		class="btn btn-primary btn-sm"
		style="margin-top: 1%; margin-bottom: 1%;">Save</button>
	<button id="cancelEditDescription" type="button"
		class="btn btn-primary btn-sm"
		style="margin-top: 1%; margin-bottom: 1%; margin-left: 1%;">Cancel</button>
</div>


<c:url
	value="/staff/module/${moduleId}/sequence/${sequence.id}/edit/slides"
	var="postUrl" />
<div id="slideSpace">
	<form:form method="POST" action="${postUrl}?"
		modelAttribute="sequenceForm">
		<div class="form-group row">
			<label for="Slides" class="col-md-2 col-form-label">Select
				Slides: <br> <small>(Double click to select and drag to
					reorder)</small>
			</label> <select multiple="multiple" id="ordered-slides">
				<c:forEach items="${allSlides}" var="slide">
					<c:if test='${!selectedSlides.contains(slide)}'>
						<option value='${slide.id}' id="${slide.id}">${slide.name}</option>
					</c:if>
				</c:forEach>
				<c:forEach items="${selectedSlides}" var="selectedSlide">
					<option value='${selectedSlide.id}' id="${selectedSlide.id}"
						selected="selected">${selectedSlide.name}</option>
				</c:forEach>
			</select>
			<form:input type="hidden" id="orderedSlides" path="orderedSlides"></form:input>
		</div>
		<button class="btn btn-primary btn-sm" type="submit" value="submit">Save
			Changes</button>
	</form:form>
</div>