<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

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
 
	
    //-------- edit contentblock description Sequence starts --------
    $("#submitDescription").hide()
    $("#cancelEditDescription").hide()
    
    $("#editDescription").click(function() {
    	var description = $("#description").text()
        $('<textarea id="newDescription" style="margin-top: 1%;" class="form-control" type="text">'+description+'</textarea>').insertAfter( "#description" );
        $("#description").remove()
        $("#editDescription").hide()
        $("#submitDescription").show()
        $("#cancelEditDescription").show()
        
    });

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
            $('<p id="description"style="margin-top: .5rem; margin-bottom: .5rem;">val</p>').insertBefore( "#newDescription" );
            $("#newDescription").remove();
            $("#description").text(val);
        },
        error: function(data) {
            $(".open").removeClass("open");
                var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
                $('.error').append(alert);
            }
        });
        
   });
    
    $("#cancelEditDescription").click(function(){
        $("#submitDescription").hide()
        $("#editDescription").show()
        $("#cancelEditDescription").hide()
        var description = $("#newDescription").val()
        $('<p id="description" style="margin-top: .5rem; margin-bottom: .5rem;">'+description+'</p>').insertBefore( "#newDescription" );
        $("#newDescription").remove()
        $("#description").text(description);
        
    });
    
    // ------- Edit/Save Description ends --------
    
    
    
    //------- edit title of Sequence starts --------
    
    $("#submitTitle").hide();
    $("#cancelEditTitle").hide();
    
    
    	$("#editTitle").click(function() {
    	
    	var getTitleText = $("#title").text().split(": ")[1].trim()
        $('<div class="col-4"><input id="newTitle" class="form-control" type="text"></div>').insertAfter( "#title" );
        $('#title').text('Sequence: ')
        $("#newTitle").val(getTitleText)
        $("#editTitle").hide()
        $("#submitTitle").show()
        $("#cancelEditTitle").show()
        
    });
    
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
                $("#newTitle").closest('div').remove();
                $("#title").text("Sequence: " + val)
                
            },
            error: function(data) {
                var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
                $('.error').append(alert);
            }
        });
                
      });
   $("#cancelEditTitle").click(function(){
        $("#submitTitle").hide()
        $("#editTitle").show()
        $("#cancelEditTitle").hide()
        var value = $("#newTitle").val();
        $("#newTitle").closest('div').remove();
        $("#title").text("Sequence: " + value)
  
        
    });
   // ------- Edit/Save Description ends --------
});



</script>


<!-- title -->
<div class="row align-items-center">
	<h2 id="title" style="margin-bottom: 0%; margin-left: 1%;">
		Sequence: ${sequence.name}
		</h2>
		<a id="editTitle" class="btn" href="#"
			style="float: left; margin-right: 1%;"><i class="fas fa-edit"></i></a>
		<button id="submitTitle" type="button" class="btn btn-primary" style="float: left; margin-right: 1%;">Save</button>
		<button id="cancelEditTitle" type="button" class="btn btn-primary" style="margin-top: 1%; margin-bottom: 1%; margin-left: .5rem;">Cancel</button>
</div>
<div class="alert alert-light" role="alert">
	Created on <span class="date">${sequence.creationDate}</span> by
	${sequence.createdBy}.<br> Modified on <span class="date">${sequence.modificationDate}</span>
	by ${sequence.modifiedBy}.
</div>



<!-- description -->
<div style="margin-left: .1%;" class="row align-items-center">
	<h4 style="margin-bottom: 0px;">
		Description:
		</h5>
		<a id="editDescription" class="btn" href="#"
			style="font-size: .66rem; border-radius: .15rem; padding-top: .5%;"><i
			class="fas fa-edit"></i></a>
		<p id="description" style="margin-top: .5rem; margin-bottom: .5rem;">${sequence.description}</p>
		<button id="submitDescription" type="button"
			class="btn btn-primary btn-sm"
			style="margin-top: 1%; margin-bottom: 1%;">Save</button>
		<button id="cancelEditDescription" type="button"
			class="btn btn-primary btn-sm"
			style="margin-top: 1%; margin-bottom: 1%; margin-left: 1%;">Cancel</button>
</div>


<div id="slideSpace">
	<table width="100%" height="50%" style="margin-top: 50px;">
		<c:forEach items="${slides}" var="slide">
			<tr>
				<td
					style="padding-left: 22px; border-style: hidden; padding-top: 6px; padding-bottom: 6px;">
					<div class="card" style="max-width: 18rem;">
						<div align="left" class="card-body">
							<a
								href="<c:url value="/staff/module/${module.id}/slide/${slide.id}" />">
								<h5 class="card-title">${slide.name}</h5>
								<p class="card-text">${slide.description}</p>
							</a>
						</div>
					</div>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>



