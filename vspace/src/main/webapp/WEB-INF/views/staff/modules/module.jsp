<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/multi-select/0.9.12/css/multi-select.css">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/multi-select/0.9.12/js/jquery.multi-select.min.js"></script>

<script>
$( document ).ready(function($) { 
	$('#multiselect1').multiSelect({keepOrder: true});
	
	
	$("#addSlideButton").on("click", function(e){ 	
		$("#createSlideAlert").show();
	});
	//$("#createSlideAlert").draggable();
	
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

<body>
<div id="result"></div>
<table border ="0" width="100%" height="50%" style=" margin-top:50px;">
	<tr>
		<td style="width: 20rem; padding-left: 15px; border:1;">
			<div class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1"><span style="float:left; font-size:medium; padding-top: 3px;">SLIDES</span>
			<a class="d-flex align-items-center text-muted" href="<c:url value="${module.id}/slide/add" />">
			<span data-feather="plus-circle"></span></a></div>
		</td>
		<td>&nbsp;</td>
		<td style="width: 20rem; padding-left: 15px; border:1;">
			<div class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1"><span style="float:left; font-size:medium; padding-top: 3px;">SEQUENCES</span>
			<a class="d-flex align-items-center text-muted" href="<c:url value="${module.id}/sequence/add" />">
			<span data-feather="plus-circle"></span></a></div>
		</td>
		<td>&nbsp;</td>
		<td style="width: 20rem; padding-left: 15px; border:1;">
			<div class="card-header sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1"> <span style="float:left; font-size:medium; padding-top: 3px;">START SEQUENCE</span>
			<span id="startSequence" style="float:right; padding-top: 3px;"></span></div>
		</td>
	</tr>
	<c:forEach items="${slides}" var="slide">
	<tr>
		<td style="padding-left: 22px; border-style:hidden; padding-top: 6px; padding-bottom: 6px;">
		  <div class="card" style="max-width: 18rem;">
		  	<div align="left" class="card-body">	<a href="<c:url value="/staff/module/${module.id}/slide/${slide.id}" />">
	            <h5 class="card-title">${slide.name}</h5>
	            <p class="card-text">${slide.description}</p></a>
	      	</div>
          </div>
        </td>
	</tr>	
	</c:forEach>
</table>

<h4>First multiselect</h4>
<div class="row">
    <div class="col-xs-5">
        <select name="from[]" id="multiselect1" class="form-control" size="8" multiple="multiple">
            <option value="1">Item 1</option>
            <option value="2">Item 5</option>
            <option value="2">Item 2</option>
            <option value="2">Item 4</option>
            <option value="3">Item 3</option>
        </select>
    </div>
    
    <div class="col-xs-2">
        <button type="button" id="multiselect1_rightAll" class="btn btn-block"><i class="glyphicon glyphicon-forward"></i></button>
        <button type="button" id="multiselect1_rightSelected" class="btn btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
        <button type="button" id="multiselect1_leftSelected" class="btn btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
        <button type="button" id="multiselect1_leftAll" class="btn btn-block"><i class="glyphicon glyphicon-backward"></i></button>
    </div>
    
    <div class="col-xs-5">
        <select name="to[]" id="multiselect1_to" class="form-control" size="8" multiple="multiple"></select>
    </div>
</div>
</body>
