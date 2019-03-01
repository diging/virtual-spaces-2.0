<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<script>
$( document ).ready(function() { 
	
	<c:forEach items="${slides}" var="link" varStatus="loop">
	{		
        $('table tr:last')
        
            .after('<tr><td style="padding-left: 22px; border-style:hidden; padding-top: 6px; padding-bottom: 6px;"><a href="<c:url value="/staff/module/slide/${link.id}" />">'+     
            '<div class="card" style="max-width: 18rem;">' +          
            '<div align="left" class="card-body"><h5 class="card-title">${link.name}</h5>'+                     
              '<p class="card-text">${link.description}</p>'+
            '</div>'+
          '</div>'+ 
          '</td>');

	}
	</c:forEach> 
	
	$("#addSlideButton").on("click", function(e){ 	
		$("#createSlideAlert").show();
	});
	$("#createSlideAlert").draggable();
	
	$("#cancelSlideBtn").click(function() {
		$("#createSlideAlert").hide();	
	});
});

</script>


<h1>Module: ${module.name}</h1>
<h3>Description: ${module.description}</h3>
	
<div class="alert alert-light" role="alert">
  Created on <span class="date">${module.creationDate}</span> by ${module.createdBy}.
  <br>
  Modified on <span class="date">${module.modificationDate}</span> by ${module.modifiedBy}.
</div>

<%-- <c:url value="/staff/module/${module.id}/slide/" var="postUrl" />
<form:form method="post" action="${postUrl}?${_csrf.parameterName}=${_csrf.token}" enctype="multipart/form-data">

	<div id="createSlideAlert" class="alert alert-secondary" role="alert" style="cursor:move; width:250px; height: 400px; display:none; position: absolute; top: 100px; right: 50px; z-index:999"> 
		  <h6 class="alert-heading"><small>Create new Slide</small></h6>
		  <p><small>Please enter the following details of new Slide. Then click "Create Slide".</small></p>
		  <hr>
		  <label style="margin-right: 5px;"><small>Title:</small> </label>
		  <input class="form-control-xs" type="text" id="slideTitle" name="slideTitle"><br>
		  
		  <label style="margin-right: 5px;"><small>Description:</small> </label>
		  <input class="form-control-xs" type="text" id="slideDescription" name="slideDescription"><br>
		  
		  <label style="margin-right: 5px;"><small>Upload Image:</small> </label>
		  <input type="file" name="file" rows="5" cols="500" id="file" multiple="multiple" accept="image/*"/>		  
		  <hr>
		  <p class="mb-0 text-right"><button id="cancelSlideBtn" type="reset" class="btn btn-light btn-xs">Cancel</button> 
		  <button id="createSlideBtn" type="submit" class="btn btn-primary btn-xs">Create Slide</button></p>
	</div>
</form:form> --%>

<body>
<div id="result"></div>
<table border ="0" width="100%" height="50%" style=" margin-top:50px;">
	<tr>
		<td style="width: 20rem; padding-left: 15px; border:1;">			
  				<div class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1"> <span style="float:left; font-size:medium; padding-top: 3px;">SLIDES</span> 
  					<a class="d-flex align-items-center text-muted" href="<c:url value="staff/module/slide/add" />">
    				<span data-feather="plus-circle"></span>
  					</a></div>
		</td>
		<td>&nbsp;</td>
		<td style="width: 20rem; padding-left: 15px; border:1;">			
  				<div class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1"> <span style="float:left; font-size:medium; padding-top: 3px;">SEQUENCES</span> 
  					<span id="addSequenceButton" data-feather="plus-circle" style="float:right; padding-top: 3px;"></span> </div>
		</td>
		<td>&nbsp;</td>
		<td style="width: 20rem; padding-left: 15px; border:1;">			
  				<div class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1"> <span style="float:left; font-size:medium; padding-top: 3px;">START SEQUENCE</span> 
  					<span id="startSequence" style="float:right; padding-top: 3px;"></span> </div>
		</td>
	</tr>
</table>
</body>