<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<script>
//# sourceURL=click.js
$( document ).ready(function() {
	
	<c:forEach items="${spaceLinks}" var="link">
	{
		var posX = $("#bgImage").position().left
        var posY = $("#bgImage").position().top;
		var icon = $('<span data-feather="navigation-2" class="flex"></span>');
	    icon.css('position', 'absolute');
	    icon.css('left', ${link.positionX} + posX);
	    icon.css('top', ${link.positionY} + posY);
	    icon.css('transform', 'rotate(${link.rotation}deg)');
	    icon.css('fill', 'red');
	    icon.css('color', 'red');
	    icon.css('font-size', "15px");
	    
	    $("#space").append(icon);
	    $("#space").append(link);

	}
	</c:forEach>
	
	var storeX;
	var storeY;
	
	$("#addSpaceLinkButton").click(function(e) {
		$("#bgImage").on("click", function(e){
			e.preventDefault();			
		    var icon = $('<span id="arrow" data-feather="navigation-2" class="flex"></span>');
		    icon.css('position', 'absolute');
		    
		    var posX = $(this).position().left
            var posY = $(this).position().top;
		    
		    storeX = e.pageX - $(this).offset().left;
		    storeY = e.pageY - $(this).offset().top;
		    icon.css('left', storeX + posX);
		    icon.css('top', storeY + posY);
		    icon.css('color', 'red');
		    icon.css('font-size', "15px");

		    $("#space").append(icon);
		    feather.replace(); 
		});
		$("#createSpaceLinkAlert").show();	
	});
	
	$("#createSpaceLinkAlert").draggable();
	
	$('#spaceLinkCreationModal.draggable>.modal-dialog>.modal-content>.modal-header').css('cursor', 'move');
	
	$("#cancelSpaceLinkBtn").click(function() {
		storeX = null;
		storeY = null;
		$("#createSpaceLinkAlert").hide();
	});
	
	$("#createSpaceLinkBtn").click(function(e) {
		var payload = {};
		payload["x"] = storeX;
		payload["y"] = storeY;
		payload["rotation"] = $("#spaceLinkRotation").val();
		payload["linkedSpace"] = $("#linkedSpace").val();
		$.post("<c:url value="/staff/space/${space.id}/spacelink?${_csrf.parameterName}=${_csrf.token}" />", payload, function(data) {
		 // TODO: show success/error message
		});
		$("#bgImage").on("click", function(e){});
		$("#createSpaceLinkAlert").hide();
	});

	$('#spaceLinkRotation').change(function() {
		$('#arrow').css('transform', 'rotate(' +$('#spaceLinkRotation').val()+ 'deg)');
	});
});
</script>

 <h1>Space: ${space.name}</h1> 

<div class="alert alert-light" role="alert">
  Created on <span class="date">${space.creationDate}</span> by ${space.createdBy}.
  <br>
  Modified on <span class="date">${space.modificationDate}</span> by ${space.modifiedBy}.
</div>

<div id="createSpaceLinkAlert" class="alert alert-secondary" role="alert" style="cursor:move; width:250px; height: 400px; display:none; position: absolute; top: 100px; right: 50px; z-index:999">
  <h6 class="alert-heading"><small>Create new Space Link</small></h6>
  <p><small>Please click on the image where you want to place the new space link. Then click "Create Space Link".</small></p>
  <hr>
  <label style="margin-right: 5px;"><small>Rotation:</small> </label>
  <input class="form-control-xs" type="number" id="spaceLinkRotation" value="0"> <br>
  
  <label style="margin-right: 5px;"><small>Type:</small> </label>
  <select id="type" class="form-control-xs">
  	<option selected value="">Choose...</option>
  	<option value="ALERT">Alert</option>
  </select><br>
  
  <label style="margin-right: 5px;"><small>Linked Space:</small> </label>
  <select id="linkedSpace" class="form-control-xs">
  
        <option selected value="">Choose...</option>
        <c:forEach items="${spaces}" var="space">
        <option value="${space.id}">${space.name}</option>
        </c:forEach>
  </select>
  <HR>
  <p class="mb-0 text-right"><button id="cancelSpaceLinkBtn" type="button" class="btn btn-light btn-xs">Cancel</button> <button id="createSpaceLinkBtn" type="button" class="btn btn-primary btn-xs">Create Space Link</button></p>
</div>

<nav class="navbar navbar-expand-sm navbar-light bg-light">
<button type="button" id="addSpaceLinkButton" class="btn btn-primary btn-sm">Add Space Link</button>
</nav>

<p></p>

<c:if test="${not empty space.image}">
<div id="space">
<img id="bgImage" width="800px" src="<c:url value="/api/image/${space.image.id}" />" />
</div>
</c:if>



