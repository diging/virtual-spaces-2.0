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
		var link = $('<span data-feather="external-link" class="flex"></span><p id="label-${loop.index}"><a href ="${link.externalLink.externalLink}" style="color:blue;">${link.externalLink.name}</a></p>'); 
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
			$("#external-arrow").remove();
			$("#space_label").remove();
			$("#link").remove();
			var icon;
			var posX = $(this).position().left;
			var posY = $(this).position().top;    
			storeX = e.pageX - $(this).offset().left;
			storeY = e.pageY - $(this).offset().top;
			
			var space_label = $("<p id='space_label'></p>");
			space_label.text($("#spaceLinkLabel").val());
					
			if ($("#type").val() == "ARROW" || $("#type").val() == "") {
				$(space_label).css({
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
		    $("#space").append(space_label);
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
		    $("#link").remove();
		    $("#external-arrow").remove();
		    $("#ext_label").remove();
		    var icon = $('<span id="external-arrow" data-feather="external-link" class="flex"></span>');
		    	    
		    var posX = $(this).position().left
		    var posY = $(this).position().top;
		    storeX = e.pageX - $(this).offset().left;
		    storeY = e.pageY - $(this).offset().top;
		    
		    if ($("#externalLinkLabel").val() != "") {
		    	var ext_label = $("<p id='ext_label'></p>").append('<a href="' + $("#url").val() + '" style=\"color:blue;\">'+$("#externalLinkLabel").val()+'</a>');
		    	$(ext_label).css({
		    		'position': 'absolute',
					'font-size': "10px",
					'transform': 'rotate(0deg)',
					'left': storeX + posX - 10,
					'top': storeY + posY + 16,
					'color': 'blue'
				});
			}
		    icon.css('position', 'absolute');
		    icon.css('left', storeX + posX);
		    icon.css('top', storeY + posY);
		    icon.css('color', 'blue');
		    icon.css('font-size', "15px");
		    
		    $("#space").append(icon);
		    $("#space").append(ext_label);
		    feather.replace();
		});
		$("#createExternalLinkAlert").show();
	});
	
	$("#createExternalLinkAlert").draggable();
	
	$("#cancelSpaceLinkBtn").click(function() {
		storeX = null;
		storeY = null;
		$("#link").remove();
		$("#space_label").remove();
		$("#createSpaceLinkAlert").hide();	
	});
	
	$("#cancelExternalLinkBtn").click(function() {
		storeX = null;
		storeY = null;
		$("#external-link").remove();
		$("#external-arrow").remove();
		$("#ext_label").remove();
		$("#createExternalLinkAlert").hide();
	});
	
	$("#createSpaceLinkBtn").click(function(e) {
		e.preventDefault();
		
		if (storeX == undefined || storeY == undefined) {
			$("#errorMsg").text("Please click on the image to specify where the new link should be located.")
			$('#errorAlert').show();
			return;
		}
		
		$("#spaceLinkX").val(storeX);
		$("#spaceLinkY").val(storeY);
		
		var form = $("#createSpaceLinkForm");
		var formData = new FormData(form[0]);
		
		var payload = {};
        payload["x"] = storeX;
        payload["y"] = storeY;
        payload["rotation"] = $("#spaceLinkRotation").val();
        payload["linkedSpace"] = $("#linkedSpace").val();
        payload["spaceLinkLabel"] = $("#spaceLinkLabel").val();
        payload["type"] = $("#type").val();

		$.ajax({
			type: "POST",
			url: "<c:url value="/staff/space/${space.id}/spacelink?${_csrf.parameterName}=${_csrf.token}" />",
			cache       : false,
	        contentType : false,
	        processData : false,
	        enctype: 'multipart/form-data',
	        data: formData, 
	        success: function(data) {
	        	$("#bgImage").on("click", function(e){});
	            showSpaceLink(payload, true);
	            $("#space_label").attr("id","");
	            $("#link").attr("id","");
	            $("#createSpaceLinkAlert").hide();  
	            $("#errorMsg").text("")
	            $('#errorAlert').hide();
	        }
		});
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
		$("#ext_label").attr("id","");
		$("#external-arrow").attr("id","");
	});
	
	$(".extlink-target").change(function() {
		var externalLink = {};
		externalLink["x"] = storeX;
		externalLink["y"] = storeY;
		externalLink["externalLinkLabel"] = $("#externalLinkLabel").val();
		externalLink["url"] = $("#externalLink").val();
		showExternalLinks(externalLink);
	});
	
	function showExternalLinks(externalLink) {
		$("#ext_label").remove();
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;		
		var ext_label = $("<p id='ext_label'></p>").append('<a href="' + externalLink["url"] + '" style=\"color:blue;\">'+externalLink["externalLinkLabel"]+'</a>');
		ext_label.css({
			'position': 'absolute',
			'font-size': "10px",
			'transform': 'rotate(0deg)',
			'left': externalLink["x"] + posX - 10,
			'top': externalLink["y"] + posY + 16,
			'color': 'blue'
		});
		var link = $('<span id="external-link" data-feather="external-link" class="flex"></span>');

		link.css('position', 'absolute');
		link.css('left', externalLink["x"] + posX);
		link.css('top', externalLink["y"] + posY);
		link.css('color', 'blue');
		link.css('font-size', "10px");
		
		$("#space").append(link);
		$("#space").append(ext_label);
		$("#external-link").remove();
	} 		
		
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
		showSpaceLink(spaceLink);
	}); 

	function showSpaceLink(spaceLink, show) {
		$("#space_label").remove();
		$("#link").remove();
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		var space_label = $("<p id='space_label'></p>");
		space_label.text(spaceLink["spaceLinkLabel"]);
			
		var link;
		if (spaceLink["type"] == "ALERT") {
			link = $('<div id="link" class="alert alert-primary" role="alert"><p>'+spaceLink["spaceLinkLabel"]+'</p>');
		} else {
			$(space_label).css({
				'position': 'absolute',
				'font-size': "10px",
				'transform': 'rotate(0deg)',
				'left': spaceLink["x"] + posX - 10,
				'top': spaceLink["y"] + posY + 16,
				'color': 'red'
			});
			link = $('<div id="link" data-feather="navigation-2" class="flex"></div>');
		}
		if(show) {
			link.css('fill', 'red');
		}
		link.css('position', 'absolute');
		link.css('left', spaceLink["x"] + posX);
		link.css('top', spaceLink["y"] + posY);
		link.css('color', 'red');
		link.css('transform', 'rotate(' +$('#spaceLinkRotation').val()+ 'deg)');
		link.css('font-size', "10px");

		$("#space").append(link);
		$("#space").append(space_label);

		feather.replace();
	}
	
	function showExternalLinks(externalLink) {
		$("#ext_label").remove();
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;		
		var ext_label = $("<p id='ext_label'></p>").append('<a href="' + externalLink["url"] + '" style=\"color:blue;\">'+externalLink["externalLinkLabel"]+'</a>');
		ext_label.css({
			'position': 'absolute',
			'font-size': "10px",
			'transform': 'rotate(0deg)',
			'left': externalLink["x"] + posX - 10,
			'top': externalLink["y"] + posY + 16,
			'color': 'blue'
		});
		var link = $('<span id="external-link" data-feather="external-link" class="flex"></span>');

		link.css('position', 'absolute');
		link.css('left', externalLink["x"] + posX);
		link.css('top', externalLink["y"] + posY);
		link.css('color', 'blue');
		link.css('font-size', "10px");
		
		$("#space").append(link);
		$("#space").append(ext_label);
		$("#external-link").remove();
	}
});

</script>

<div id="errorAlert" class="alert alert-danger alert-dismissible fade show" role="alert" style="display: none; position: absolute; top: 10px; right: 50px;">
   <strong>Error!</strong> <span id="errorMsg"></span>
   <button type="button" class="close" data-dismiss="alert" aria-label="Close">
       <span aria-hidden="true">&times;</span>
   </button>
</div>

<h1>Space: ${space.name}</h1> 
 
<div class="alert alert-light" role="alert">
  Created on <span class="date">${space.creationDate}</span> by ${space.createdBy}.
  <br>
  Modified on <span class="date">${space.modificationDate}</span> by ${space.modifiedBy}.     
</div>

<c:url value="/staff/space/${space.id}/spacelink?${_csrf.parameterName}=${_csrf.token}" var="postUrl" />
<form id="createSpaceLinkForm">
	<div id="createSpaceLinkAlert" class="alert alert-secondary" role="alert" style="cursor:move; width:250px; height: 400px; display:none; position: absolute; top: 100px; right: 50px; z-index:999">
	  <div class="row">
	  <div class="col">
	  <h6 class="alert-heading"><small>Create new Space Link</small></h6>
	  </div>
	  </div>
	  <div class="row">
      <div class="col">
      <small>Please click on the image where you want to place the new space link. Then click "Create Space Link".</small></p>
	  <hr>
	  </div>
	  </div>
	  
	  <input type="hidden" name="x" id="spaceLinkX" />
	  <input type="hidden" name="y" id="spaceLinkY"/>
	  
	  <div class="row">
      <div class="col-sm-4">
	  <label><small>Rotation:</small> </label>
	  </div>
	  <div class="col-sm-8">
	  <input class="form-control-xs" type="number" id="spaceLinkRotation" name="rotation" value="0"><br>
	  </div>
	  </div>
	  
	  <div class="row">
      <div class="col-sm-4">
	  <label><small>Label:</small> </label>
	  </div>
      <div class="col-sm-8">
	  <input class="form-control-xs target" type="text" name="spaceLinkLabel" id="spaceLinkLabel"><br>
	  </div>
      </div>
      
      <div class="row">
      <div class="col-sm-4">
	  <label><small>Type:</small> </label>
	  </div>
      <div class="col-sm-8">
	  <select id="type" name="type" class="form-control-xs target" >
	  	<option selected value="">Choose...</option>
	  	<option value="ARROW">Link</option>
	  	<option value="ALERT">Alert</option>
	  </select>
	  </div>
      </div>
	  
	  <div class="row">
      <div class="col-sm-5" style="padding-right: 0px;">
	  <label><small>Linked Space:</small> </label>
	  </div>
      <div class="col-sm-7" >
	  <select id="linkedSpace" name="linkedSpace" class="form-control-xs">
	        <option selected value="">Choose...</option>
	        <c:forEach items="${spaces}" var="space">
	        <option value="${space.id}">${space.name}</option>
	        </c:forEach>
	  </select>
	  </div>
      </div>
      
      <div class="row">
      <div class="col-sm-3" style="padding-right: 0px;">
	  <label><small>Image:</small> </label>
	  </div>
      <div class="col-sm-9">
      <input type="file" class="form-control-xs target" type="text" name="spaceLinkImage" id="spaceLinkImage"><br>
      </div>
      </div>
	  
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
<form>
	<div id="createExternalLinkAlert" class="alert alert-secondary" role="alert" style="cursor:move; width:250px; height: 400px; display:none; position: absolute; top: 100px; right: 50px; z-index:999">
		 <h6 class="alert-heading"><small>Create new External Link</small></h6>
		  <p><small>Please click on the image where you want to place the new external link. Then click "Create External Link".</small></p>
		  <hr>  
		  <label style="margin-right: 5px;"><small>Label:</small> </label>
		  <input class="form-control-xs extlink-target" type="text" id="externalLinkLabel"><br>
		  
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

