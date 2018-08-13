<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<script>
//# sourceURL=click.js
$( document ).ready(function() {
	
	var storeX;
	var storeY;
	
	$("#addSpaceLinkButton").click(function(e) {
		$("#createSpaceLinkAlert").show();
	});
	
	$("#createSpaceLinkAlert").draggable();
	
	$("#bgImage").on("click", function(e){
	    e.preventDefault();
	    var icon = $('<span data-feather="arrow-up" class="flex"></span>');
	    icon.css('position', 'fixed');
	    
	    icon.css('left', e.pageX);
	    icon.css('top', e.pageY);
	    storeX = e.pageX - $(this).offset().left;
	    storeY = e.pageY - $(this).offset().top;
	    icon.css('color', 'red');
	    icon.css('font-size', "15px");
	    icon.css('transform', 'rotate(45deg)');
	    
	    $("#space").append(icon);
	    feather.replace();
	});
	
	$('#spaceLinkCreationModal.draggable>.modal-dialog').draggable({
	    cursor: 'move',
	    handle: '.modal-header'
	});
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
		$.post("<c:url value="/staff/space/${space.id}/spacelink?${_csrf.parameterName}=${_csrf.token}" />", payload, function(data) {
			console.log(data);
		});
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
  <p class="mb-0 text-right"><button id="cancelSpaceLinkBtn" type="button" class="btn btn-light btn-xs">Cancel</button> <button id="createSpaceLinkBtn" type="button" class="btn btn-primary btn-xs">Create Space Link</button></p>
</div>

<nav class="navbar navbar-expand-sm navbar-light bg-light">
<button type="button" id="addSpaceLinkButton" class="btn btn-primary btn-sm">Add Space Link</button>
</nav>

<p></p>

<c:if test="${not empty space.image}">
<div id="space">
<img id="bgImage" width="800px" src="<c:url value="/staff/api/image/${space.image.id}" />" />
</div>
</c:if>



<div id="spaceLinkModel" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <!-- dialog body -->
      <div class="modal-body">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        Please click on the image where you want to place the new space link. Then click "Create Space Link".
      </div>
      <!-- dialog buttons -->
      <div class="modal-footer"><button id="spaceLinkDlgClose" type="button" class="btn btn-primary">Got it!</button></div>
    </div>
  </div>
</div>

<div id="spaceLinkCreationModal" class="modal draggable fade ">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">Create new Space Link.</div>
            <div class="modal-body">Please click on the image where you want to place the new space link. Then click "Create Space Link".</div>
            <div class="modal-footer"><button id="cancelSpaceLinkBtn" type="button" class="btn btn-light btn-sm">Cancel</button><button id="createSpaceLinkBtn" type="button" class="btn btn-primary btn-sm">Create Space Link</button></div>
        </div>
    </div>
</div>

