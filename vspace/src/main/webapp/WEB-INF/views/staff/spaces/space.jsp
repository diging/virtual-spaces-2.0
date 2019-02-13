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
			var link = $('<span data-feather="navigation-2" class="flex"></span><p id="label-${loop.index}">${link.link.name}</p>'); 
		}
		link.css('position', 'absolute');
		link.css('left', ${link.positionX} + posX);
		link.css('top', ${link.positionY} + posY);
		link.css('transform', 'rotate(${link.rotation}deg)');
		link.css('fill', 'red');
		link.css('color', 'red');
		link.css('font-size', "10px");
		
		$("#space").append(link);
		
		$("#label-${loop.index}").css({
			'transform': 'rotate(0deg)',
			'left': ${link.positionX} + posX - 10,
			'top': ${link.positionY} + posY + 16,
			'color': 'red'
		});  
	}
	</c:forEach> 
	
	var storeX;
	var storeY;
	
	$("#addSpaceLinkButton").click(function(e) {
		$("#changeBgImgAlert").hide();
		$("#bgImage").on("click", function(e){		
			e.preventDefault();
			$("#label").remove();
			$("#link").remove();
			var icon;
			var posX = $(this).position().left;
			var posY = $(this).position().top;    
			storeX = e.pageX - $(this).offset().left;
			storeY = e.pageY - $(this).offset().top;
			
			var label = $("<p id='label'></p>");
			label.text($("#spaceLinkLabel").val());
					
			if ($("#type").val() == "ARROW" || $("#type").val() == "") {
				$(label).css({
					'position': 'absolute',
					'font-size': "10px",
					'transform': 'rotate(0deg)',
					'left': storeX + posX - 10,
					'top': storeY + posY + 16,
					'color': 'red'
				});
				icon = $('<div id="link" data-feather="navigation-2" class="flex"></div>');
			} else { 
				icon = $('<div id="link" class="alert alert-primary" role="alert"><p>'+$("#spaceLinkLabel").val()+'</p>');
			}
			    
		    icon.css('position', 'absolute');			    
		    icon.css('transform','rotate(' +$('#spaceLinkRotation').val()+ 'deg)');
		    icon.css('left', storeX + posX);
		    icon.css('top', storeY + posY);
		    icon.css('color', 'red');
		    icon.css('font-size', "10px");
		    
		    $("#space").append(icon);
		    $("#space").append(label);
		    feather.replace(); 
			
		});
		$("#createSpaceLinkAlert").show();
	});
	
	$("#createSpaceLinkAlert").draggable();
	
	$('#spaceLinkCreationModal.draggable>.modal-dialog>.modal-content>.modal-header').css('cursor', 'move');
	
	$("#cancelSpaceLinkBtn").click(function() {
		storeX = null;
		storeY = null;
		$("#link").remove();
		$("#label").remove();
		$("#createSpaceLinkAlert").hide();	
	});
	
	$("#cancelBgImgBtn").click(function() {
		$("#changeBgImgAlert").hide();
	});
	
	$("#createSpaceLinkBtn").click(function(e) {		
		var payload = {};
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		payload["x"] = storeX;
		payload["y"] = storeY;
		payload["rotation"] = $("#spaceLinkRotation").val();
		payload["linkedSpace"] = $("#linkedSpace").val();
		payload["spaceLinkLabel"] = $("#spaceLinkLabel").val();
		payload["type"] = $("#type").val();

		$.post("<c:url value="/staff/space/${space.id}/spacelink?${_csrf.parameterName}=${_csrf.token}" />", payload, function(data) {
			// TODO: show success/error message
		});
		$("#createSpaceLinkAlert").hide();

		show(payload, true);
  		$("#label").attr("id","");
		$("#link").attr("id","");
	});
		
	$('#changeBgImgButton').click(function(file) {
		$("#createSpaceLinkAlert").hide();
		$("#changeBgImgAlert").show();			
	});
			
	$("#changeBgImgAlert").draggable();
		
	$('#spaceLinkCreationModal.draggable>.modal-dialog>.modal-content>.modal-header').css('cursor', 'move');
	
	$('#spaceLinkRotation').change(function() {
		$('#link').css('transform', 'rotate(' +$('#spaceLinkRotation').val()+ 'deg)');
	});
	
	$(".target").change(function() {
		var spaceLink = {};
		spaceLink["x"] = storeX;
		spaceLink["y"] = storeY;
		spaceLink["spaceLinkLabel"] = $("#spaceLinkLabel").val();
		spaceLink["type"] = $("#type").val();
		show(spaceLink);
	}); 

	function show(spaceLink, showSpaceLink) {
		$("#label").remove();
		$("#link").remove();
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		var label = $("<p id='label'></p>");
		label.text(spaceLink["spaceLinkLabel"]);
			
		var link;
		if (spaceLink["type"] == "ALERT") {
			link = $('<div id="link" class="alert alert-primary" role="alert"><p>'+spaceLink["spaceLinkLabel"]+'</p>');
		} else {
			$(label).css({
				'position': 'absolute',
				'font-size': "10px",
				'transform': 'rotate(0deg)',
				'left': spaceLink["x"] + posX - 10,
				'top': spaceLink["y"] + posY + 16,
				'color': 'red'
			});
			link = $('<div id="link" data-feather="navigation-2" class="flex"></div>');
		}

		if(showSpaceLink) {
			link.css('fill', 'red');
		}
		link.css('position', 'absolute');
		link.css('left', spaceLink["x"] + posX);
		link.css('top', spaceLink["y"] + posY);
		link.css('color', 'red');
		link.css('transform', 'rotate(' +$('#spaceLinkRotation').val()+ 'deg)');
		link.css('font-size', "10px");

		$("#space").append(link);
		$("#space").append(label);

		feather.replace();
	}	
});

</script>

 <h1>Space: ${space.name}</h1> 
 
<div class="alert alert-light" role="alert">
  Created on <span class="date">${space.creationDate}</span> by ${space.createdBy}.
  <br>
  Modified on <span class="date">${space.modificationDate}</span> by ${space.modifiedBy}.     
</div>

<form>
	<div id="createSpaceLinkAlert" class="alert alert-secondary" role="alert" style="cursor:move; width:250px; height: 400px; display:none; position: absolute; top: 100px; right: 50px; z-index:999">
	  <h6 class="alert-heading"><small>Create new Space Link</small></h6>
	  <p><small>Please click on the image where you want to place the new space link. Then click "Create Space Link".</small></p>
	  <hr>
	  <label style="margin-right: 5px;"><small>Rotation:</small> </label>
	  <input class="form-control-xs" type="number" id="spaceLinkRotation" value="0"><br>
	  
	  <label style="margin-right: 5px;"><small>Label:</small> </label>
	  <input class="form-control-xs target" type="text" id="spaceLinkLabel"><br>
	  
	  <label style="margin-right: 5px;"><small>Type:</small> </label>
	  <select id="type" class="form-control-xs target" >
	  	<option selected value="">Choose...</option>
	  	<option value="ARROW">Link</option>
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
	  <p class="mb-0 text-right"><button id="cancelSpaceLinkBtn" type="reset" class="btn btn-light btn-xs">Cancel</button> <button id="createSpaceLinkBtn" type="reset" class="btn btn-primary btn-xs">Create Space Link</button></p>
	</div>
</form>
	        
<c:url value="/staff/space/update/${space.id}" var="postUrl" />
<form:form method="post" action="${postUrl}?${_csrf.parameterName}=${_csrf.token}" enctype="multipart/form-data">

	<div id="changeBgImgAlert" class="alert alert-secondary" role="alert" style="cursor:move; width:340px; height: 130px; display:none; position: absolute; top: 100px; right: 50px; z-index:999">
		<h6><small>Change Background Image: </small></h6>
		<input type="file" name="file" rows="5" cols="500" id="file" /><br><br>
	        <p class="mb-0 text-right"><button type="submit" id="changeBgImgBtn" class="btn btn-primary btn-xs">Upload Image</button> &nbsp
		<button id="cancelBgImgBtn" type="button" class="btn light btn-xs">Cancel</button></p>
	</div>
	
</form:form>

<nav class="navbar navbar-expand-sm navbar-light bg-light">
<button type="button" id="addSpaceLinkButton" class="btn btn-primary btn-sm">Add Space Link</button> &nbsp 
<button type="button" id="changeBgImgButton" class="btn btn-primary btn-sm"> Change Image</button>
</nav>

<p></p>

<c:if test="${not empty space.image}">
<div id="space">
<img id="bgImage" width="800px" src="<c:url value="/api/image/${space.image.id}" />" />
</div>
</c:if>
