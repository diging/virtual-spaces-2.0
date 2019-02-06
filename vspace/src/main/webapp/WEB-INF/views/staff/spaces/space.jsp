<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<script>
//# sourceURL=click.js
$( document ).ready(function() {
	
	<c:forEach items="${spaceLinks}" var="link" varStatus="loop">
	{
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		if ("${link.type}" == "ALERT") {
			var link = $('<div class="alert alert-primary" role="alert"><p>${link.link.name}</p>');
		} else {
			var link = $('<span data-feather="navigation-2" class="flex"></span><p class="label-${loop.index}">${link.link.name}</p>'); 
		}
		link.css('position', 'absolute');
		link.css('left', ${link.positionX} + posX);
		link.css('top', ${link.positionY} + posY);
		link.css('transform', 'rotate(${link.rotation}deg)');
		link.css('fill', 'red');
		link.css('color', 'red');
		link.css('font-size', "10px");
		
		$("#space").append(link);
		
		$(".label-${loop.index}").css({
			'transform': 'rotate(0deg)',
			'left': ${link.positionX} + posX - 10,
			'top': ${link.positionY} + posY + 16,
			'color': 'red'
		});
		
	}
	</c:forEach>
	
	<c:forEach items="${externalLinks}" var="link" varStatus="loop">
	{
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		var link = $('<span data-feather="arrow-right-circle" class="flex"></span><p id="label-${loop.index}"><a href ="${link.externalLink.name}" style="color:blue;">${link.externalLink.name}</a></p>'); 
		link.css('position', 'absolute');
		link.css('left', ${link.positionX} + posX);
		link.css('top', ${link.positionY} + posY);
		link.css('color', 'blue');
		link.css('font-size', "10px");
		
		$("#space").append(link);
		
		$("#label-${loop.index}").css({
			'transform': 'rotate(0deg)',
			'left': ${link.positionX} + posX - 10,
			'top': ${link.positionY} + posY + 16,
			'text-color': 'blue'
		});
		
	}
	</c:forEach>
	
	var storeX;
	var storeY;

	$("#addSpaceLinkButton").click(function(e) {
		$("#createExternalLinkAlert").hide();
		$("#changeBgImgAlert").hide();
		$("#bgImage").on("click", function(e){
		    e.preventDefault();
		    $("#arrow").remove();
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
	
	$("#addExternalLinkButton").click(function(e) {
		$("#createSpaceLinkAlert").hide();
		$("#bgImage").on("click", function(e){
		    e.preventDefault();
		    $("#arrow").remove();
		    $("#title").remove();
		    var icon = $('<span id="arrow" data-feather="arrow-right-circle" class="flex"></span>');
		    icon.css('position', 'absolute');
		    
		    var posX = $(this).position().left
		    var posY = $(this).position().top;
		    
		    storeX = e.pageX - $(this).offset().left;
		    storeY = e.pageY - $(this).offset().top;
		    icon.css('left', storeX + posX);
		    icon.css('top', storeY + posY);
		    icon.css('color', 'blue');
		    icon.css('font-size', "15px");
		    
		    $("#space").append(icon);
		    feather.replace();
		});
		$("#createExternalLinkAlert").show();
	});
	
	$("#createExternalLinkAlert").draggable();
	
	$("#cancelSpaceLinkBtn").click(function() {
		storeX = null;
		storeY = null;
		$("#arrow").remove();
		$("#createSpaceLinkAlert").hide();
	});
	
	$("#cancelExternalLinkBtn").click(function() {
		storeX = null;
		storeY = null;
		$("#icon").remove();
		$("#arrow").remove();
		$("#title").remove();
		$("#createExternalLinkAlert").hide();
	});
	
	$("#createSpaceLinkBtn").click(function(e) {
		var payload = {};
		payload["x"] = storeX;
		payload["y"] = storeY;
		payload["rotation"] = $("#spaceLinkRotation").val();
		payload["linkedSpace"] = $("#linkedSpace").val();
		payload["type"] = $("#type").val();
		$("#arrow").remove();
		$.post("<c:url value="/staff/space/${space.id}/spacelink?${_csrf.parameterName}=${_csrf.token}" />", payload, function(data) {
			// TODO: show success/error message
		});
		$("#bgImage").on("click", function(e){});
		$("#createSpaceLinkAlert").hide();	
	});
	
	$("#createExternalLinkBtn").click(function(e) {
		var payload = {};
		payload["x"] = storeX;
		payload["y"] = storeY;
		payload["externalLinkLabel"] = $("#externalLinkLabel").val();
		payload["url"] = $("#externalLink").val();
		$.post("<c:url value="/staff/space/${space.id}/externallink?${_csrf.parameterName}=${_csrf.token}" />", payload, function(data) {
			// TODO: show success/error message
		});
		$("#bgImage").on("click", function(e){});
		$("#createExternalLinkAlert").hide();
		$("#title").attr("id","");
		$("#arrow").attr("id","");
	});
	
	$(".target").change(function() {
		var externalLink = {};
		externalLink["x"] = storeX;
		externalLink["y"] = storeY;
		externalLink["externalLinkLabel"] = $("#externalLinkLabel").val();
		showExternalLinks(externalLink);
	});
	
	function showExternalLinks(externalLink) {
		$("#title").remove();
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;		
		var label = $("<p id='title'></p>").append('<a href="' + externalLink["externalLinkLabel"] + '" style=\"color:blue;\"></a>');
		label.text(externalLink["externalLinkLabel"]);
		label.css({
			'position': 'absolute',
			'font-size': "10px",
			'transform': 'rotate(0deg)',
			'left': externalLink["x"] + posX - 10,
			'top': externalLink["y"] + posY + 16,
			'color': 'blue'
		});
		var link = $('<span id="icon" data-feather="arrow-right-circle" class="flex"></span>');

		link.css('position', 'absolute');
		link.css('left', externalLink["x"] + posX);
		link.css('top', externalLink["y"] + posY);
		link.css('color', 'blue');
		link.css('font-size', "10px");
		
		$("#space").append(link);
		$("#space").append(label);
		$("#icon").remove();
	} 		
		
	$('#changeBgImgButton').click(function(file) {
		$("#createSpaceLinkAlert").hide();
		$("#changeBgImgAlert").show();	
		
	});
			
	$("#changeBgImgAlert").draggable();
		
	$('#spaceLinkCreationModal.draggable>.modal-dialog>.modal-content>.modal-header').css('cursor', 'move');
	
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
  <input class="form-control-xs" type="number" id="spaceLinkRotation" value="0"><br>
  
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
	        
<c:url value="/staff/space/update/${space.id}" var="postUrl" />
<form:form method="post" action="${postUrl}?${_csrf.parameterName}=${_csrf.token}" enctype="multipart/form-data">

	<div id="changeBgImgAlert" class="alert alert-secondary" role="alert" style="cursor:move; width:340px; height: 130px; display:none; position: absolute; top: 100px; right: 50px; z-index:999">
		<h6><small>Change Background Image: </small></h6>
		<input type="file" name="file" rows="5" cols="500" id="file" /><br><br>
	        <p class="mb-0 text-right"><button type="submit" id="changeBgImgBtn" class="btn btn-primary btn-xs">Upload Image</button> &nbsp
		<button id="cancelBgImgBtn" type="button" class="btn light btn-xs">Cancel</button></p>
	</div>
	
</form:form>
<form>
	<div id="createExternalLinkAlert" class="alert alert-secondary" role="alert" style="cursor:move; width:250px; height: 400px; display:none; position: absolute; top: 100px; right: 50px; z-index:999">
		 <h6 class="alert-heading"><small>Create new External Link</small></h6>
		  <p><small>Please click on the image where you want to place the new external link. Then click "Create External Link".</small></p>
		  <hr>  
		  <label style="margin-right: 5px;"><small>Label:</small> </label>
		  <input class="form-control-xs target" type="text" id="externalLinkLabel"><br>
		  
		  <label style="margin-right: 5px;"><small>External Link</small> </label>
		  <input class="form-control-xs" type="text" size="15" id="externalLink"><br>
		  <HR>
		  <p class="mb-0 text-right"><button id="cancelExternalLinkBtn" type="reset" class="btn btn-light btn-xs">Cancel</button> <button id="createExternalLinkBtn" type="reset" class="btn btn-primary btn-xs">Create External Link</button></p>
	</div>
</form>

<nav class="navbar navbar-expand-sm navbar-light bg-light">
<button type="button" id="addSpaceLinkButton" class="btn btn-primary btn-sm">Add Space Link</button> &nbsp
<button type="button" id="addExternalLinkButton" class="btn btn-primary btn-sm">Add External Link</button> &nbsp
<button type="button" id="changeBgImgButton" class="btn btn-primary btn-sm"> Change Image</button>
</nav>

<p></p>

<c:if test="${not empty space.image}">
<div id="space">
<img id="bgImage" width="800px" src="<c:url value="/api/image/${space.image.id}" />" />
</div>
</c:if>

