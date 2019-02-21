<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<script>
$( document ).ready(function() { 
	
	<c:forEach items="${slides}" var="link" varStatus="loop">
	{		
        // ADD ROWS TO THE TABLE.
        $('table tr:last')
            .after('<tr><td align="center"><a href="http://www.google.com">'+     
            '<div class="card text-white bg-info mb-1" style="max-width: 22rem;">' +
            '<div align="left" class="card-header"><h6 class="card-title">${link.name}</h6></div>'+
            '<div class="card-body">'+         
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
	
	function myFunction() {
		alert("working");
		  var table = document.getElementById("myTable");
		  var row = table.insertRow(0);
		  var cell1 = row.insertCell(0);
		  var cell2 = row.insertCell(1);
		  cell1.innerHTML = "NEW CELL1";
		}
});

</script>


<h1>Module: ${module.name}</h1>
<h3>Description: ${module.description}</h3>
	
<div class="alert alert-light" role="alert">
  Created on <span class="date">${module.creationDate}</span> by ${module.createdBy}.
  <br>
  Modified on <span class="date">${module.modificationDate}</span> by ${module.modifiedBy}.
</div>

<c:url value="/staff/module/${module.id}/slide/" var="postUrl" />
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
		  <input type="file" name="file" rows="5" cols="500" id="file" />		  
		  <hr>
		  <p class="mb-0 text-right"><button id="cancelSlideBtn" type="reset" class="btn btn-light btn-xs">Cancel</button> 
		  <button id="createSlideBtn" type="submit" class="btn btn-primary btn-xs" onclick="myFunction()">Create Slide</button></p>
	</div>
</form:form>


<nav class="navbar navbar-expand-sm navbar-light bg-light">
<button type="button" id="addSlideButton" class="btn btn-primary btn-sm">Add Slide</button>
</nav>


<body>
<div id="result"></div>
<table width="100%" height="50%" style=" margin-top:50px;">
	<tr>
		<td align="center">
			<div class="card text-white bg-warning" style="max-width: 12rem;">
  				<div class="card-header">Slides</div></div>
			<button type="button" id="addSlideButton" class="btn btn-primary btn-sm">Add</button>
		</td>
		<td>
			<td align="center">
			<div class="card text-white bg-warning" style="max-width: 12rem;">
  				<div class="card-header">Sequences</div></div>
			<button type="button" id="addSlideButton" class="btn btn-primary btn-sm">Add</button>
		</td>
		</td>
		<td>
			Start Sequence
		</td>
	</tr>
</table>


</body>