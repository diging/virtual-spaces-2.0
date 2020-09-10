<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script src="https://use.fontawesome.com/releases/v5.8.1/js/all.js" data-auto-replace-svg="nest"></script>
<script src="<c:url value="/resources/extra/space.js" />" ></script>
<link rel="stylesheet" type="text/css" id="applicationStylesheet"
    href="<c:url value="/resources/extra/Home.css" />">
<script>

$(function(){
    $("#deleteSpace").click(function(){
        var spaceId = "${space.id}";
        checkSpaceLinkPresent(spaceId, "<c:url value='/staff/' />", "?${_csrf.parameterName}=${_csrf.token}", $("#headerSpaceValue"));
});    


$( document ).ready(function() {
	onPageReady($("#deleteSpace"), $('#confirm-space-delete'));
	
	if ($("div#outgoingLinks a").length < 2) {
		  $("#noLinksOnSpace").show()
	 }
	if ($("div#incomingLinks a").length < 2) {
		  $("#noLinksToSpace").show()
	  }
  });    

	<c:forEach items="${spaceLinks}" var="link" varStatus="loop">
	{
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		
		var link;
		if ("${link.type}" == "ALERT") {
			link = $('<div class="alert alert-primary spaceLink-${link.link.id}" role="alert" data-link-id="${link.link.id}"><p class="slabel-${link.link.id}">${link.link.name}</p></div>');
		} else if ("${link.type}" == "IMAGE" && "${link.image.id}" != "") {
           link = $('<img id="${link.image.id} class="spaceLink-${link.link.id}" data-link-id="${link.link.id}" src="<c:url value="/api/image/${link.image.id}" />" />');
		}  else {
			link = $('<span data-link-id="${link.link.id}" class="spaceLink-${link.link.id} Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-walking fa-lg Icon_awesome_info_staff_c"></i><p class="slabel-${link.link.id}" data-link-id="${link.link.id}"><span class="tooltiptext">${link.link.name}</span></p></span>');
		}
		link.css('position', 'absolute');
		link.css('left', ${link.positionX} + posX);
		link.css('top', ${link.positionY} + posY);
		link.css('transform', 'rotate(${link.rotation}deg)');
		link.find("span").css('fill', 'red');
		link.css('color', 'rgba(128,128,128,1)');
		link.css('font-size', "12px");
		var inputRotation = document.createElement("input");
		inputRotation.setAttribute("type", "hidden");
		inputRotation.setAttribute("id", "spaceLinkRotation-${link.link.id}");
		inputRotation.setAttribute("value", "${link.rotation}");
		var inputLabel = document.createElement("input");
		inputLabel.setAttribute("type", "hidden");
		inputLabel.setAttribute("id", "spaceLinkLabel-${link.link.id}");
		inputLabel.setAttribute("value", "${link.link.name}");
		var inputType = document.createElement("input");
		inputType.setAttribute("type", "hidden");
		inputType.setAttribute("id", "spaceLinkType-${link.link.id}");
		inputType.setAttribute("value", "${link.type}");
		var inputLinkedSpace = document.createElement("input");
		inputLinkedSpace.setAttribute("type", "hidden");
		inputLinkedSpace.setAttribute("id", "spaceLinkTarget-${link.link.id}");
		inputLinkedSpace.setAttribute("value", "${link.link.targetSpace.id}");
		var unpublishedSpaceElement=$('<c:if test="${link.link.targetSpace.spaceStatus=='UNPUBLISHED'}"><i data-link-id="unpublished-${link.link.id}" class="fa fa-exclamation-triangle fa-lg unpublishedSpaceClass unpublishedClass-${link.link.id}" style="color: #bfb168;"></i></c:if>')
		unpublishedSpaceElement.css('position', 'absolute');
		unpublishedSpaceElement.css('left', ${link.positionX} + posX + 25);
		unpublishedSpaceElement.css('top', ${link.positionY} + posY - 13);
		unpublishedSpaceElement.css('transform', 'rotate(${link.rotation}deg)');
		unpublishedSpaceElement.css('font-size', "12px");
		
		//append to form element that you want .
		$("#space").append(inputRotation);
		$("#space").append(inputLabel);
		$("#space").append(inputType);
		$("#space").append(inputLinkedSpace);
		$("#space").append(link);
		$("#space").append(unpublishedSpaceElement);
		
		$(".slabel-${link.link.id}").css({
			'transform': 'rotate(0deg)',
			'left': ${link.positionX} + posX - 10,
			'top': ${link.positionY} + posY + 35,
			'color': 'red'
		});		
		
		$('[data-link-id="${link.link.id}"]').css('cursor', 'pointer');
		$('[data-link-id="${link.link.id}"]').click(function(e) {
		    var rotation = document.getElementById("spaceLinkRotation-${link.link.id}").value;
		    var label = document.getElementById("spaceLinkLabel-${link.link.id}").value;
		    var type = document.getElementById("spaceLinkType-${link.link.id}").value;
		    var targetSpace = document.getElementById("spaceLinkTarget-${link.link.id}").value;
			makeSpaceLinksEditable(label, "${link.link.id}", rotation ,targetSpace,"${link.positionX}","${link.positionY}","${link.id}",type);
		});
	}
	</c:forEach>
	
	<c:forEach items="${moduleLinks}" var="link" varStatus="loop">
	{
		var link;
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		if ("${link.type}" == "ALERT") {
			link = $('<div class="alert alert-primary moduleLink-${link.link.id}" role="alert" data-link-id="${link.link.id}"><p class="mlabel-${link.link.id}">${link.link.name}</p></div>');
		} else if ("${link.type}" == "IMAGE" && "${link.image.id}" != "") {
           link = $('<img id="${link.image.id}" data-link-id="${link.link.id}" class="moduleLink-${link.link.id}" src="<c:url value="/api/image/${link.image.id}" />" />');
		}  else {
			link = $('<span data-link-id="${link.link.id}" class="moduleLink-${link.link.id} Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-info fa-lg Icon_awesome_info_staff_m"></i><p class="mlabel-${link.link.id}" data-link-id="${link.link.id}"><span class="tooltiptext">${link.link.name}</span></p></span>');
		}
		link.css('position', 'absolute');
		link.css('left', ${link.positionX} + posX);
		link.css('top', ${link.positionY} + posY);
		link.css('transform', 'rotate(${link.rotation}deg)');
		link.find("span").css('fill', 'red');
		link.css('color', 'red');
		link.css('font-size', "12px");
		
		var inputRotation = document.createElement("input");
		inputRotation.setAttribute("type", "hidden");
		inputRotation.setAttribute("id", "moduleLinkRotation-${link.link.id}");
		inputRotation.setAttribute("value", "${link.rotation}");
		var inputLabel = document.createElement("input");
		inputLabel.setAttribute("type", "hidden");
		inputLabel.setAttribute("id", "moduleLinkLabel-${link.link.id}");
		inputLabel.setAttribute("value", "${link.link.name}");
		var inputType = document.createElement("input");
		inputType.setAttribute("type", "hidden");
		inputType.setAttribute("id", "moduleLinkType-${link.link.id}");
		inputType.setAttribute("value", "${link.type}");
		var inputLinkedSpace = document.createElement("input");
		inputLinkedSpace.setAttribute("type", "hidden");
		inputLinkedSpace.setAttribute("id", "moduleLinkTarget-${link.link.id}");
		inputLinkedSpace.setAttribute("value", "${link.link.module.id}");
		
		$("#space").append(inputRotation);
		$("#space").append(inputLabel);
		$("#space").append(inputType);
		$("#space").append(inputLinkedSpace);
		$("#space").append(link);
		
		$(".mlabel-${link.link.id}").css({
			'transform': 'rotate(0deg)',
			'left': ${link.positionX} + posX - 10,
			'top': ${link.positionY} + posY + 35,
			'color': 'red'
		});		
		
		$('[data-link-id="${link.link.id}"]').css('cursor', 'pointer');
		$('[data-link-id="${link.link.id}"]').click(function(e) {
		    var rotation = document.getElementById("moduleLinkRotation-${link.link.id}").value;
		    var label = document.getElementById("moduleLinkLabel-${link.link.id}").value;
		    var type = document.getElementById("moduleLinkType-${link.link.id}").value;
		    var targetModule = document.getElementById("moduleLinkTarget-${link.link.id}").value;
			makeModuleLinksEditable(label, "${link.link.id}",rotation,targetModule,"${link.positionX}","${link.positionY}","${link.id}", type);
		});
	}
	</c:forEach> 	
	
	<c:forEach items="${externalLinks}" var="link" varStatus="loop">
	{
        var posX = $("#bgImage").position().left;
        var posY = $("#bgImage").position().top;
        var link ;
        if ("${link.type}" == "IMAGE" && "${link.image.id}" != "") {
            link = $('<img id="${link.image.id}" class="externalLink-${link.externalLink.id}" data-link-id="${link.externalLink.id}" src="<c:url value="/api/image/${link.image.id}" />" />');
        }  else {
            link = $('<span data-link-id="${link.externalLink.id}" class="externalLink-${link.externalLink.id} Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-external-link-alt fa-lg Icon_awesome_info_staff_e"></i><p class="elabel-${link.externalLink.id}" data-link-id="${link.externalLink.id}"><span class="tooltiptext">${link.externalLink.name}</span></p></span>');
        }
        link.css('position', 'absolute');
        link.css('left', ${link.positionX} + posX);
        link.css('top', ${link.positionY} + posY);
        link.css('color', 'blue');
        link.css('font-size', "12px");
        
		var inputLabel = document.createElement("input");
		inputLabel.setAttribute("type", "hidden");
		inputLabel.setAttribute("id", "externalLinkLabel-${link.externalLink.id}");
		inputLabel.setAttribute("value", "${link.externalLink.name}");
		var inputType = document.createElement("input");
		inputType.setAttribute("type", "hidden");
		inputType.setAttribute("id", "externalLinkType-${link.externalLink.id}");
		inputType.setAttribute("value", "${link.type}");
		var inputExternalLinkURL = document.createElement("input");
		inputExternalLinkURL.setAttribute("type", "hidden");
		inputExternalLinkURL.setAttribute("id", "externalLinkTarget-${link.externalLink.id}");
		inputExternalLinkURL.setAttribute("value", "${link.externalLink.externalLink}");
		
		$("#space").append(inputLabel);
		$("#space").append(inputType);
		$("#space").append(inputExternalLinkURL);
        $("#space").append(link);
        
        $(".elabel-${link.externalLink.id}").css({
            'transform': 'rotate(0deg)',
            'left': ${link.positionX} + posX - 10,
            'top': ${link.positionY} + posY + 35,
            'text-color': 'blue'
        });
        
        $('[data-link-id="${link.externalLink.id}"]').css('cursor', 'pointer');
        $('[data-link-id="${link.externalLink.id}"]').click(function(e) {
		    var label = document.getElementById("externalLinkLabel-${link.externalLink.id}").value;
		    var type = document.getElementById("externalLinkType-${link.externalLink.id}").value;
		    var targetLink = document.getElementById("externalLinkTarget-${link.externalLink.id}").value;
            makeExternalLinksEditable(label,"${link.externalLink.id}",targetLink,"${link.positionX}","${link.positionY}","${link.id}",type);
        });
	}
	</c:forEach>
	
	// --------- draggable modals -----------
	$("#createSpaceLinkAlert").draggable();
	$("#createModuleLinkAlert").draggable();
	$('#spaceLinkCreationModal.draggable>.modal-dialog>.modal-content>.modal-header').css('cursor', 'move');
	 
	$("#createExternalLinkAlert").draggable();
	$("#changeBgImgAlert").draggable();
	$("#editModuleLinkInfo").draggable();
	$("#editSpaceLinkInfo").draggable();
	$("#editExternalLinkInfo").draggable();
    
	// store where a user clicked on an image
	var storeX;
	var storeY;
	var selectedModuleLinkId;
	var selectedSpaceLinkId;
	var selectedExternalLinkId;

	// -------- buttons that open modals (e.g. to create space links) ------
	$("#addSpaceLinkButton").click(function(e) {
		$("#createExternalLinkAlert").hide();
		$("#changeBgImgAlert").hide();
		$("#bgImage").off("click");
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
			
		    showSpaceLink(createSpaceLinkInfo());
		});
		hideLinkInfoTabs();
		$("#createSpaceLinkAlert").show();
	});
	
	 $("#addModuleLinkButton").click(function(e) {
		$("#createModuleLinkAlert").hide();
		$("#bgImage").off("click");
		$("#bgImage").on("click", function(e){
			e.preventDefault();
			$("#external-arrow").remove();
			$("#module_label").remove();
			$("#link").remove();
			var icon;
			var posX = $(this).position().left;
			var posY = $(this).position().top;    
			storeX = e.pageX - $(this).offset().left;
			storeY = e.pageY - $(this).offset().top;
			
		    showModuleLink(createModuleLinkInfo());
		});
		hideLinkInfoTabs();
		$("#createModuleLinkAlert").show();
	}); 
	
	$("#addExternalLinkButton").click(function(e) {
		$("#createSpaceLinkAlert").hide();
		$("#changeBgImgAlert").hide();
		$("#bgImage").off("click");
		$("#bgImage").on("click", function(e){
		    e.preventDefault();
		    $("#link").remove();
		   	$("#external-arrow").remove();
		    $("#ext_label").remove();
		    var icon;
		    	    
		    var posX = $(this).position().left
		    var posY = $(this).position().top;
		    storeX = e.pageX - $(this).offset().left;
		    storeY = e.pageY - $(this).offset().top;
		    
		    showExternalLinks(createExternalLinkInfo());
		});
		hideLinkInfoTabs();
		$("#createExternalLinkAlert").show();
	});
	
	$('#changeBgImgButton').click(function(file) {
        $("#createSpaceLinkAlert").hide();
        $("#createExternalLinkAlert").hide();
        $("#changeBgImgAlert").show();          
    });
	
	
	// ----------- submit buttons (e.g. to create space links) ------------------
	$("#createSpaceLinkBtn").click(function(e) {
		e.preventDefault();
		$('#errorAlert').hide();
		var label = $("#spaceLinkLabel").val();
		var spaceName = $("#linkedSpace option:selected").text();
		
		if (label == undefined || label == "") {
			$("#errorMsg").text("Please fill the Label field before submitting.")
			$('#errorAlert').show();
			return;
		}
		if (spaceName == undefined || spaceName == "Choose...") {
			$("#errorMsg").text("Please select a space in Linked Space dropdown.")
			$('#errorAlert').show();
			return;
		}
		
		if (storeX == undefined || storeY == undefined) {
			$("#errorMsg").text("Please click on the image to specify where the new link should be located.")
			$('#errorAlert').show();
			return;
		}
		
		$("#spaceLinkX").val(storeX);
		$("#spaceLinkY").val(storeY);
		
		var form = $("#createSpaceLinkForm");
		var label = $("#spaceLinkLabel").val();
		var spaceName = $("#linkedSpace option:selected").text();
		var spaceId = $("#linkedSpace option:selected").val(); 
		var formData = new FormData(form[0]);
		
		var spaceLinkInfo = createSpaceLinkInfo();
        
	    $.ajax({
			type: "POST",
			url: "<c:url value="/staff/space/${space.id}/spacelink?${_csrf.parameterName}=${_csrf.token}" />",
			cache       : false,
	        contentType : false,
	        processData : false,
	        enctype: 'multipart/form-data',
	        data: formData, 
	        success: function(data) {
	        	var linkData = JSON.parse(data);
	        	$("#bgImage").off("click");
	        	spaceLinkInfo["id"] = linkData["id"];
	        	spaceLinkInfo["displayId"]=linkData["displayId"];
	        	spaceLinkInfo["x"]=linkData["x"];
	        	spaceLinkInfo["y"]=linkData["y"];
	        	spaceLinkInfo["linkedSpaceStatus"]=linkData["linkedSpaceStatus"];
	            showSpaceLink(spaceLinkInfo, true);
	            $("#space_label").attr("id","");
	            $("#link").attr("id","");
	            $("#createSpaceLinkAlert").hide();  
	            $("#errorMsg").text("");
	            $('#errorAlert').hide();
	            $("#noLinksOnSpace").hide();
	    		$("#outgoingLinks").append("<a href='<c:url value='/staff/space/"+spaceId+"'/>' style='padding: .25rem .25rem;' class='list-group-item' id="+linkData["id"]+">"+label+"  &nbsp;-> &nbsp;"+spaceName+"</a>");
	    		if (spaceName == "${space.name}") {
	    			$("#noLinksToSpace").hide();
	    			$("div#incomingLinks").append("<a href='<c:url value='/staff/space/"+spaceId+"'/>' style='padding: .25rem .25rem;' class='list-group-item' id="+linkData["id"]+">"+spaceName+"</a>");
	    		}
	        }
		});
	});
	
	$("#createModuleLinkBtn").click(function(e) {
		e.preventDefault();
		
		if (storeX == undefined || storeY == undefined) {
			$("#errorMsg").text("Please click on the image to specify where the new link should be located.")
			$('#errorAlert').show();
			return;
		}
		
		var linkedModules = $("#linkedModule").val();
		var moduleLabel = $("#moduleLinkLabel").val();
		
		if (moduleLabel == undefined || moduleLabel == "") {
			$("#errorMsg").text("Please enter the label for module.")
			$('#errorAlert').show();
			return;
		}
		
		if (linkedModules == undefined || linkedModules == "") {
			$("#errorMsg").text("Please select linked modules.")
			$('#errorAlert').show();
			return;
		}
		
		$("#moduleLinkX").val(storeX);
		$("#moduleLinkY").val(storeY);
		
		var form = $("#createModuleLinkForm");
		var formData = new FormData(form[0]);
		
		var moduleLinkInfo = createModuleLinkInfo();
        
	    $.ajax({
			type: "POST",
			url: "<c:url value="/staff/space/${space.id}/modulelink?${_csrf.parameterName}=${_csrf.token}" />",
			cache       : false,
	        contentType : false,
	        processData : false,
	        enctype: 'multipart/form-data',
	        data: formData, 
	        success: function(data) {
	        	var linkData = JSON.parse(data);
	        	$("#bgImage").off("click");
	        	moduleLinkInfo["id"] = linkData["id"];
	        	moduleLinkInfo["displayId"]=linkData["displayId"];
	        	moduleLinkInfo["x"]=linkData["x"];
	        	moduleLinkInfo["y"]=linkData["y"];
	        	showModuleLink(moduleLinkInfo, true);
	        	$("#module_label").attr("id","");
	        	$("#link").attr("id","");
	        	$("#createModuleLinkAlert").hide();
	        	$("#errorMsg").text("");
	        	$('#errorAlert').hide();
	        }
		});
	});
	$("#editModuleLinkBtn").click(function(e) {
	    e.preventDefault();
	    var linkId = $("#moduleLinkIdEdit").val();
	    if(storeX == undefined || storeY == undefined){
	        $("#errorMsg").text("Please click on the image to specify where the new link should be located.");
	        $('#errorAlert').show();
	        return;    
	    }
	    var linkedModules = $("#moduleLinkIdEdit").val();
	    $("#moduleLinkXEdit").val(storeX);
	    $("#moduleLinkYEdit").val(storeY);
	    var form = $("#editModuleLinkForm");
	    var formData = new FormData(form[0]);
	    var moduleLinkInfo = editModuleLinkInfo();
	    $.ajax({
	        type: "POST",
	        url: "<c:url value="/staff/space/link/module/${space.id}?${_csrf.parameterName}=${_csrf.token}" />",
	        cache: false,
	        contentType: false,
	        processData: false,
	        enctype: 'multipart/form-data',
	        data: formData,
	        success: function(data){
	            var linkData = JSON.parse(data);
	            $("#bgImage").off("click");
	            moduleLinkInfo["id"] = linkData["id"];
	            moduleLinkInfo["displayId"]=linkData["displayId"];
	            moduleLinkInfo["x"]=linkData["x"];
	        	moduleLinkInfo["y"]=linkData["y"];
	        	$('#moduleLinkRotation-'+selectedModuleLinkId).val(linkData["rotation"]);
	            $('#moduleLinkLabel-'+selectedModuleLinkId).val(linkData["label"]);
	            $('#moduleLinkType-'+selectedModuleLinkId).val(linkData["dislpayType"]);
	            $('#moduleLinkTarget-'+selectedModuleLinkId).val(linkData["linkedId"]);
	            showModuleLinkEdit(moduleLinkInfo, true);
	            hideLinkInfoTabs();
	        }
	    });
	});
	
	$("#editSpaceLinkBtn").click(function(e) {
	    e.preventDefault();
	    var linkId = $("#spaceLinkIdEdit").val();
	    if (storeX == undefined || storeY == undefined) {
	        $("#errorMsg").text("Please click on the image to specify where the new link should be located.")
	        $('#errorAlert').show();
	        return;    
	    }
	    var linkedSpace = $("#spaceLinkIdEdit").val();
	    $("#spaceLinkXEdit").val(storeX);
	    $("#spaceLinkYEdit").val(storeY);

	    var form = $("#editSpaceLinkForm");
	    var formData = new FormData(form[0]);
	    var spaceLinkInfo = editSpaceLinkInfo();
	    $.ajax({
	        type: "POST",
	        url: "<c:url value="/staff/space/link/space/${space.id}?${_csrf.parameterName}=${_csrf.token}" />",
	        cache: false,
	        contentType : false,
	        processData : false,
	        enctype: 'multipart/form-data',
	        data: formData,
	        success: function(data) {
	            var linkData = JSON.parse(data);
	            $("#bgImage").off("click");
	            spaceLinkInfo["id"] = linkData["id"];
	            spaceLinkInfo["displayId"]=linkData["displayId"];
	            spaceLinkInfo["x"]=linkData["x"];
	            spaceLinkInfo["y"]=linkData["y"];
	            spaceLinkInfo["linkedSpaceStatus"]=linkData["linkedSpaceStatus"];
	            showSpaceLinkEdit(spaceLinkInfo, true);
	            $('#spaceLinkRotation-'+selectedSpaceLinkId).val(linkData["rotation"]);
	            $('#spaceLinkLabel-'+selectedSpaceLinkId).val(linkData["label"]);
	            $('#spaceLinkType-'+selectedSpaceLinkId).val(linkData["dislpayType"]);
	            $('#spaceLinkTarget-'+selectedSpaceLinkId).val(linkData["linkedId"]);
	            hideLinkInfoTabs();
	       	}
	 	});
	});
	
	$("#editExternalLinkBtn").click(function(e) {
	    e.preventDefault();
	    var linkId = $("#externalLinkIdEdit").val();
	    if (storeX == undefined || storeY == undefined) {
	        $("#errorMsg").text("Please click on the image to specify where the new link should be located.")
	        $('#errorAlert').show();
	        return;
	    }
	    var linkedSpace = $("#externalLinkIdEdit").val();
	    $("#externalLinkXEdit").val(storeX);
	    $("#externalLinkYEdit").val(storeY);
	    var form = $("#editExternalLinkForm");
	    var formData = new FormData(form[0]);
	    var externalLinkInfo = editExternalLinkInfo();
	    $.ajax({
	        type: "POST",
	        url: "<c:url value="/staff/space/link/external/${space.id}?${_csrf.parameterName}=${_csrf.token}" />",
	        cache: false,
	        contentType : false,
	        processData : false,
	        enctype: 'multipart/form-data',
	        data: formData,
	        success: function(data) {
	            var linkData = JSON.parse(data);
	            $("#bgImage").off("click");
	            externalLinkInfo["id"] = linkData["id"];
	            externalLinkInfo["displayId"]=linkData["displayId"];
	            externalLinkInfo["x"]=linkData["x"];
	            externalLinkInfo["y"]=linkData["y"];
	            $('#externalLinkLabel-'+selectedExternalLinkId).val(linkData["label"]);
	            $('#externalLinkType-'+selectedExternalLinkId).val(linkData["dislpayType"]);
	            $('#externalLinkTarget-'+selectedExternalLinkId).val(linkData["url"]);
	            showExternalLinkEdit(editExternalLinkInfo, true);
	            hideLinkInfoTabs();
	       	}
	 	});
	});
	
	$("#createExternalLinkBtn").click(function(e) {
		e.preventDefault();
		var payload = {};
		
		if (storeX == undefined || storeY == undefined) {
			$("#errorMsg").text("Please click on the image to specify where the new link should be located.")
			$('#errorAlert').show();
			return;
		}
		
		var externalLink = $("#externalLink").val();
		var externalLinkLabel = $("#externalLinkLabel").val();
		
		
		if (externalLink == undefined || externalLink == "") {
			$("#errorMsg").text("Please provide the link to the external link.")
			$('#errorAlert').show();
			return;
		}
		
		if (externalLinkLabel == undefined || externalLinkLabel == "") {
			$("#errorMsg").text("Please enter a label for this external link.")
			$('#errorAlert').show();
			return;
		}
		$("#externalLinkX").val(storeX);
		$("#externalLinkY").val(storeY);
		
		var form = $("#createExternalLinkForm");
		var formData = new FormData(form[0]);
		var externalLinkInfo = createExternalLinkInfo();
        
	    $.ajax({
			type: "POST",
			url: "<c:url value="/staff/space/${space.id}/externallink?${_csrf.parameterName}=${_csrf.token}" />",
			cache       : false,
	        contentType : false,
	        processData : false,
	        enctype: 'multipart/form-data',
	        data: formData, 
	        success: function(data) {
	        	var linkData = JSON.parse(data);
	        	$("#bgImage").off("click");
	        	externalLinkInfo["id"] = linkData["id"];
	        	externalLinkInfo["displayId"]=linkData["displayId"];
	        	externalLinkInfo["x"]=linkData["x"];
	        	externalLinkInfo["y"]=linkData["y"];
	        	showExternalLinks(externalLinkInfo, true);
	        	$("#ext_label").attr("id","");
	        	$("#link").attr("id","");
	        	$("#createExternalLinkAlert").hide();
	        	$("#errorMsg").text("");
	        	$('#errorAlert').hide();
	        	$("#external-arrow").attr("id","");
	        }
		});
	});
	
	// ------------- other buttons ------------
	$("#deleteSpaceLinkButton").click(function() {
		var linkId = $("#spaceLinkIdValueEdit").val();
		$.ajax({
		  url: "<c:url value="/staff/space/${space.id}/spacelink/" />" + linkId + "?${_csrf.parameterName}=${_csrf.token}",
		  method: "DELETE",
		  success:function(data) {
			  $('[data-link-id="' + linkId + '"]').remove();
			  $('[data-link-id="unpublished-' + linkId + '"]').remove();
			  hideLinkInfoTabs();
			  $('a#'+linkId).remove();
			  if ($("div#outgoingLinks a").length < 2) {
				  $("#noLinksOnSpace").show();
			  }
			  if ($("div#incomingLinks a").length < 2) {
				  $("#noLinksToSpace").show();
			  }
	          $("#spaceLinkInfo").hide();
		    }
		});
	});
	
	
	$("#deleteModuleLinkButton").click(function() {
		var linkId = $("#moduleLinkIdValueEdit").val();
		$.ajax({
			url: "<c:url value="/staff/space/${space.id}/modulelink/" />" + linkId + "?${_csrf.parameterName}=${_csrf.token}",
			method: "DELETE",
				success:function(data) {
					$('[data-link-id="' + linkId + '"]').remove();
					hideLinkInfoTabs();
				}
			});
	});
	
	$("#deleteExternalLinkButton").click(function() {
        var linkId = $("#externalLinkIdValueEdit").val();
        $.ajax({
            url: "<c:url value="/staff/space/${space.id}/externallink/" />" + linkId + "?${_csrf.parameterName}=${_csrf.token}",
            method: "DELETE",
                success:function(data) {
                    $('[data-link-id="' + linkId + '"]').remove();
                    hideLinkInfoTabs();
                }
            });
    });
	
    $("#closeAlert").click(function() {
    	$('#errorAlert').hide();
    });
	
	// ------------- adjust links on background image (e.g. when inputs are changed) ------------
	// external links
	$(".extlink-target").change(function() {
		var externalLink = {};
		externalLink["x"] = storeX;
		externalLink["y"] = storeY;
		externalLink["externalLinkLabel"] = $("#externalLinkLabel").val();
		externalLink["url"] = $("#externalLink").val();
		externalLink["type"] = $("#extType").val();
		showExternalLinks(externalLink);
	});		
		
	// space links	
	$('#spaceLinkRotation').change(function() {
		$('#link').css('transform', 'rotate(' +$('#spaceLinkRotation').val()+ 'deg)');
	});
	
	$(".target").change(function() {
		showSpaceLink(createSpaceLinkInfo());
	}); 
	
	$(".modulelink-target").change(function() {
        showModuleLink(createModuleLinkInfo());
    }); 
	
	// link icons for space links
	var linkIconReader = new FileReader();
	var linkIcon;
	linkIconReader.onload = function(e) {
		linkIcon = e.target.result;
		showSpaceLink(createSpaceLinkInfo());
	}
	
	$('#moduleLinkRotation').change(function() {
		$('#link').css('transform', 'rotate(' +$('#moduleLinkRotation').val()+ 'deg)');
	});
	
	$('#moduleLinkRotationEdit').change(function() {
		$('#link').css('transform', 'rotate(' +$('#moduleLinkRotationEdit').val()+ 'deg)');
	});
	
	$(".modulelink-targetEdit").change(function() {
	    showModuleLinkEdit(editModuleLinkInfo());
    }); 
	
	$(".spacelink-targetEdit").change(function() {
	    showSpaceLinkEdit(editSpaceLinkInfo());
    });
	
	$(".externallink-targetEdit").change(function() {
	    showExternalLinkEdit(editExternalLinkInfo());
    });
	
	
	$("#spaceLinkImage").change(function() {
		if (this.files && this.files[0]) {
			linkIconReader.readAsDataURL(this.files[0]);
		}
	});
	
	// link icons for module links
	var moduleLinkIconReader = new FileReader();
    var moduleLinkIcon;
    moduleLinkIconReader.onload = function(e) {
    	moduleLinkIcon = e.target.result;
        showModuleLink(createModuleLinkInfo());
    }
	$("#moduleLinkImage").change(function() {
		if (this.files && this.files[0]) {
			moduleLinkIconReader.readAsDataURL(this.files[0]);
		}
	});

	var externalLinkIconReader = new FileReader();
    var externalLinkIcon;
    externalLinkIconReader.onload = function(e) {
    	externalLinkIcon = e.target.result;
        showExternalLinks(createExternalLinkInfo());
    }
	$("#externalLinkImage").change(function() {
		if (this.files && this.files[0]) {
			externalLinkIconReader.readAsDataURL(this.files[0]);
		}
	});

	// --------- show links functions --------------
	function showSpaceLink(spaceLink, show) {
		$("#space_label").remove();
		$("#link").remove();
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		var space_label = $("<p id='space_label' class='tooltiptext'></p>");
		space_label.text(spaceLink["spaceLinkLabel"]);
		
		var link;
		if (spaceLink["type"] == "ALERT") {
			link = $('<div id="link" class="alert alert-primary spaceLink-"' + spaceLink["id"] + '" role="alert" data-link-id="' + spaceLink["id"] + '"><p>'+spaceLink["spaceLinkLabel"]+'</p></div>');
		} else if(spaceLink["type"] == "IMAGE" && linkIcon) {
			link = $('<div id="link" data-link-id="' + spaceLink["id"] + '"><img src="' + linkIcon + '"></div>');
		} else {
			$(space_label).css({
				'position': 'absolute',
				'font-size': "10px",
				'transform': 'rotate(0deg)',
				'left': spaceLink["x"] + posX - 10,
				'top': spaceLink["y"] + posY + 35,
				'color': 'red'
			});
			link = $('<span data-link-id="' + spaceLink["id"] + '" class="spaceLink-' + spaceLink["id"] + '"><div id="link" class="Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-walking fa-lg Icon_awesome_info_staff_c"></i></div></span>');
		}
		if(show) {
		    var unpublishedSpaceElement;
		    if(spaceLink["linkedSpaceStatus"]=="UNPUBLISHED"){
		        unpublishedSpaceElement=$('<i data-link-id = "unpublished-' + spaceLink["id"] + '" class="fa fa-exclamation-triangle fa-lg unpublishedSpaceClass unpublishedClass-' + spaceLink["id"] + '" style="color: #bfb168;"></i>')
		        unpublishedSpaceElement.css('position', 'absolute');
		        unpublishedSpaceElement.css('left', parseInt(spaceLink["x"]) + posX + 25);
		        unpublishedSpaceElement.css('top', parseInt(spaceLink["y"]) + posY - 13);
		        unpublishedSpaceElement.css('transform', 'rotate(' +$('#spaceLinkRotation').val()+ 'deg)');
		        unpublishedSpaceElement.css('font-size', "12px");
		    }else{
		        $('[data-link-id="unpublished-' + spaceLink["id"] + '"]').remove();
		    }    
		}
		link.css('position', 'absolute');
		link.css('left', spaceLink["x"] + posX);
		link.css('top', spaceLink["y"] + posY);
		link.css('color', 'rgba(128,128,128,1)');
		link.css('transform', 'rotate(' +$('#spaceLinkRotation').val()+ 'deg)');
		link.css('font-size', "12px");
		if (spaceLink["id"]) {
			/* link.attr("data-link-id", "Info_cz_Class"); */
			link.attr("class", spaceLink["id"]);
			link.css('cursor', 'pointer');
			link.css('color', 'rgba(128,128,128,1)');
			link.click(function(e) {
				makeSpaceLinksEditable(spaceLink["spaceLinkLabel"], spaceLink["id"], spaceLink["rotation"], spaceLink["linkedSpace"], spaceLink["x"], spaceLink["y"], spaceLink["displayId"], spaceLink["type"]);
	        });
			
			space_label.attr("data-link-id", spaceLink["id"]);
			space_label.attr("class", "tooltiptext slabel-"+spaceLink["id"]);
            space_label.css('cursor', 'pointer');
            space_label.click(function(e) {
                makeSpaceLinksEditable(spaceLink["spaceLinkLabel"], spaceLink["id"], spaceLink["rotation"], spaceLink["linkedSpace"], spaceLink["x"], spaceLink["y"], spaceLink["displayId"], spaceLink["type"]);
            });
		}

		$("#space").append(link);
		$("#space").append(unpublishedSpaceElement);
		$("#link").append(space_label);

		feather.replace();
	}			        
	
	function showModuleLink(moduleLink, show) {
		$("#module_label").remove();
		$("#link").remove();
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		var module_label = $("<p id='module_label' class='tooltiptext'></p>");
		module_label.text(moduleLink["moduleLinkLabel"]);
		
		var link;
		if (moduleLink["type"] == "ALERT") {
			link = $('<div id="link" class="alert alert-primary moduleLink-"' + moduleLink["id"] + '" role="alert" data-link-id="' + moduleLink["id"] + '"><p>'+moduleLink["moduleLinkLabel"]+'</p></div>');
		} else if(moduleLink["type"] == "IMAGE" && moduleLinkIcon) {
			link = $('<div id="link" data-link-id="' + moduleLink["id"] + '" ><img src="' + moduleLinkIcon + '"></div>');
		} else { 
			$(module_label).css({
				'position': 'absolute',
				'font-size': "10px",
				'transform': 'rotate(0deg)',
				'left': moduleLink["x"] + posX - 10,
				'top': moduleLink["y"] + posY + 35,
				'color': 'red'
			});
			link = $('<span data-link-id="' + moduleLink["id"] + '" class="moduleLink-' + moduleLink["id"] + '"><div id="link" class="Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-info fa-lg Icon_awesome_info_staff_m"></i></div></span>');
		} 
		if(show) {
			link.find("div").css('fill', 'red');
		}
		link.css('position', 'absolute');
		link.css('left', moduleLink["x"] + posX);
		link.css('top', moduleLink["y"] + posY);
		link.css('color', 'red');
		link.css('transform', 'rotate(' +$('#moduleLinkRotation').val()+ 'deg)');
		link.css('font-size', "12px");
		
		if (moduleLink["id"]) {
			link.attr("data-link-id", moduleLink["id"]);
			link.css('cursor', 'pointer');
			link.click(function(e) {
				makeModuleLinksEditable(moduleLink["moduleLinkLabel"], moduleLink["id"], moduleLink["rotation"], moduleLink["linkedModule"], moduleLink["x"], moduleLink["y"], moduleLink["displayId"], moduleLink["type"]);
			});
			module_label.attr("data-link-id", moduleLink["id"]);
			module_label.css('cursor', 'pointer');
			module_label.attr("class", "tooltiptext mlabel-"+moduleLink["id"]);
			module_label.click(function(e) {
				makeModuleLinksEditable(moduleLink["moduleLinkLabel"], moduleLink["id"], moduleLink["rotation"], moduleLink["linkedModule"], moduleLink["x"], moduleLink["y"], moduleLink["displayId"], moduleLink["type"]);
			});
		}

		$("#space").append(link);
		$("#link").append(module_label);

		feather.replace();
	}
	
	function showModuleLinkEdit(moduleLink, show) {
		moduleLink["x"]=storeX;
		moduleLink["y"]=storeY;
		var selectedLinkClass = '.moduleLink-'+selectedModuleLinkId;
		var selectedLabelClass = '.mlabel-'+selectedModuleLinkId;
		updateLinkProperties(selectedLinkClass,selectedLabelClass,moduleLink["rotation"],moduleLink["x"],moduleLink["y"],moduleLink["moduleLinkLabel"]);
	}
	
	function showSpaceLinkEdit(spaceLink, show) {
		spaceLink["x"]=storeX;
		spaceLink["y"]=storeY;
		var selectedLinkClass = '.spaceLink-'+selectedSpaceLinkId;
		var selectedLabelClass = '.slabel-'+selectedSpaceLinkId;
		var selectedUnpublishedIconClass = '.unpublishedClass-'+selectedSpaceLinkId;
		if(show) {
		    var unpublishedSpaceElement;
		    if(spaceLink["linkedSpaceStatus"]=="UNPUBLISHED"){
		        unpublishedSpaceElement=$('<i data-link-id = "unpublished-' + spaceLink["id"] + '" class="fa fa-exclamation-triangle fa-lg unpublishedSpaceClass unpublishedClass-' + spaceLink["id"] + '" style="color: #bfb168;"></i>')
		        unpublishedSpaceElement.css('position', 'absolute');
		        unpublishedSpaceElement.css('left', parseInt(spaceLink["x"]) + posX + 25);
		        unpublishedSpaceElement.css('top', parseInt(spaceLink["y"]) + posY - 13);
		        unpublishedSpaceElement.css('transform', 'rotate(' +spaceLink["rotation"]+ 'deg)');
		        unpublishedSpaceElement.css('font-size', "12px");
		        $("#space").append(unpublishedSpaceElement);
		    }else{
		        $('[data-link-id="unpublished-' + spaceLink["id"] + '"]').remove();
		    }    
		}
		$(selectedUnpublishedIconClass).css({ 'transform': 'rotate(' + parseInt(spaceLink["rotation"]) + 'deg)'});
	    $(selectedUnpublishedIconClass).css({ 'position': 'absolute'});
	    $(selectedUnpublishedIconClass).css({ 'left': parseInt(spaceLink["x"]) + posX + 25});
	    $(selectedUnpublishedIconClass).css({ 'top': parseInt(spaceLink["y"]) + posY - 13});
		updateLinkProperties(selectedLinkClass,selectedLabelClass,spaceLink["rotation"],spaceLink["x"],spaceLink["y"],spaceLink["spaceLinkLabel"]);
	}
	
	function showExternalLinkEdit(externalLink, show) {
		externalLink["x"]=storeX;
		externalLink["y"]=storeY;
		var selectedLinkClass = '.externalLink-'+selectedExternalLinkId;
		var selectedLabelClass = '.elabel-'+selectedExternalLinkId;
		updateLinkProperties(selectedLinkClass,selectedLabelClass,externalLink["rotation"],externalLink["x"],externalLink["y"],externalLink["externalLinkLabel"]);
	}
	
	function updateLinkProperties(selectedLinkClass,selectedLabelClass,rotation,x,y,linkLabel){
	    var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
	    $(selectedLinkClass).css({ 'transform': 'rotate(' + rotation + 'deg)'});
	    $(selectedLinkClass).css({ 'position': 'absolute'});
	    $(selectedLinkClass).css({ 'left': x + posX});
	    $(selectedLinkClass).css({ 'top': y + posY});
	    $(selectedLabelClass).text(linkLabel);
	    $(selectedLabelClass).css({ 'position': 'absolute'});
	    $(selectedLabelClass).css({ 'left': x + posX - 10});
	    $(selectedLabelClass).css({ 'top': y + posY + 30});
	    feather.replace();    
	}
	
	function hideLinkInfoTabs(){
	    $("#editExternalLinkInfo").hide();
	    $("#editSpaceLinkInfo").hide();
	    $("#editModuleLinkInfo").hide();
        $("#errorMsg").text("");
        $('#errorAlert').hide();    
	}
	
	function showExternalLinks(externalLink) {
		$("#ext_label").remove();
		$("#link").remove();
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		var ext_label = $("<p id='ext_label' class='tooltiptext'></p>");
		ext_label.text(externalLink["externalLinkLabel"]);
		
		var link;
		if(externalLink["type"] == "IMAGE" && externalLinkIcon) {
			link = $('<div id="link" data-link-id="' + externalLink["id"] + '"><img src="' + externalLinkIcon + '"></div>');
		} else {
			$(ext_label).css({
				'position': 'absolute',
				'font-size': "12px",
				'transform': 'rotate(0deg)',
				'left': externalLink["x"] + posX - 10,
				'top': externalLink["y"] + posY + 35,
				'color': 'blue'
			});
			link = $('<span data-link-id="' + externalLink["id"] + '"  class="externalLink-' + externalLink["id"] + '"><div id="link" class="Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-external-link-alt fa-lg Icon_awesome_info_staff_e"></i></div></span>');
		}
		
		link.css('position', 'absolute');
		link.css('left', externalLink["x"] + posX);
		link.css('top', externalLink["y"] + posY);
		link.css('color', 'blue');
		link.css('font-size', "12px");
		
		if (externalLink["id"]) {
			link.attr("data-link-id", externalLink["id"]);
			link.css('cursor', 'pointer');
			link.attr('href', externalLink["url"]);
			link.click(function(e) {
                makeExternalLinksEditable(externalLink["externalLinkLabel"], externalLink["id"], externalLink["externalLinkURL"], externalLink["x"], externalLink["y"], externalLink["displayId"], externalLink["type"]);
            });
			
			ext_label.attr("data-link-id", externalLink["id"]);
            ext_label.css('cursor', 'pointer');
            ext_label.attr("class", "elabel-"+externalLink["id"]);
            ext_label.click(function(e) {
                makeExternalLinksEditable(externalLink["externalLinkLabel"], externalLink["id"], externalLink["externalLinkURL"], externalLink["x"], externalLink["y"], externalLink["displayId"], externalLink["type"]);
            });
		}

		$("#space").append(link);
		$("#link").append(ext_label);
		$("#external-link").remove();
		
	}
	
	// ------------ Cancel buttons -----------------
	$("#cancelSpaceLinkBtn").click(function() {
        storeX = null;
        storeY = null;
        $("#link").remove();
        $("#space_label").remove();
        $("#createSpaceLinkAlert").hide(); 
        $('#errorAlert').hide();
    });
	
	$("#cancelModuleLinkBtn").click(function() {
        storeX = null;
        storeY = null;
        $("#link").remove();
        $("#module_label").remove();
        $("#createModuleLinkAlert").hide();  
        $('#errorAlert').hide();
    }); 
    
    $("#cancelExternalLinkBtn").click(function() {
        storeX = null;
        storeY = null;
        $("#external-link").remove();
        $("div#link").remove();
        $("#external-arrow").remove();
        $("#ext_label").remove();
        $("#createExternalLinkAlert").hide();
        $('#errorAlert').hide();
    });
    
    $("#cancelBgImgBtn").click(function() {
        $("#file").val('');
        $("#changeBgImgAlert").hide();
    });
    
    $("#closeEditModuleLinkInfo").click(function(e) {
    	e.preventDefault();
    	$("#moduleLinkInfoLabel").text("");
        $("#moduleLinkId").val("");
        resetHighlighting();
        $("#editModuleLinkInfo").hide();
    }); 
    
    $("#closeEditSpaceLinkInfo").click(function(e) {
    	e.preventDefault();
    	$("#spaceLinkInfoLabel").text("");
        $("#spaceLinkId").val("");
        resetHighlighting();
        $("#editSpaceLinkInfo").hide();
    }); 
    
    $("#closeEditExternalLinkInfo").click(function(e) {
    	e.preventDefault();
    	$("#externalLinkInfoLabel").text("");
        $("#externalLinkId").val("");
        resetHighlighting();
        $("#editExternalLinkInfo").hide();
    }); 
	
	// --------- Utility functions -------------
	function createSpaceLinkInfo() {
		var info = {};
		info["x"] = storeX;
		info["y"] = storeY;
		info["rotation"] = $("#spaceLinkRotation").val();
		info["linkedSpace"] = $("#linkedSpace").val();
		info["spaceLinkLabel"] = $("#spaceLinkLabel").val();
		info["type"] = $("#type").val();
	    return info;
	}
	
	function createExternalLinkInfo() {
		var info = {};
		info["x"] = storeX;
		info["y"] = storeY;
		info["type"] = $("#extType").val();
		info["externalLinkLabel"] = $("#externalLinkLabel").val();
		info["externalLinkURL"] = $("#externalLink").val();
	    return info;
	}

	function createModuleLinkInfo() {
		var info = {};
		info["x"] = storeX;
		info["y"] = storeY;
		info["rotation"] = $("#moduleLinkRotation").val();
		info["linkedModule"] = $("#linkedModule").val();
		info["moduleLinkLabel"] = $("#moduleLinkLabel").val();
		info["type"] = $("#type").val();
		return info;
	}
	
	function editModuleLinkInfo() {
		var info = {};
		info["x"] = storeX;
		info["y"] = storeY;
		info["rotation"] = $("#moduleLinkRotationEdit").val();
		info["linkedModule"] = $("#moduleLinkIdEdit").val();
		info["moduleLinkLabel"] = $("#moduleLinkLabelEdit").val();
		info["type"] = $("#typeEdit").val();
		return info;
	}
	
	function editSpaceLinkInfo() {
		var info = {};
		info["x"] = storeX;
		info["y"] = storeY;
		info["rotation"] = $("#spaceLinkRotationEdit").val();
		info["linkedSpace"] = $("#spaceLinkIdEdit").val();
		info["spaceLinkLabel"] = $("#spaceLinkLabelEdit").val();
		info["type"] = $("#typeSpaceEdit").val();
		return info;
	}
	
	function editExternalLinkInfo() {
		var info = {};
		info["x"] = storeX;
		info["y"] = storeY;
		info["type"] = $("#extTypeEdit").val();
		info["externalLinkLabel"] = $("#externalLinkLabelEdit").val();
		info["externalLinkURL"] = $("#externalLinkURLEdit").val();
		return info;
	}
	
	function makeSpaceLinksEditable(spaceLinkName, spaceLinkId, rotation, selectedSpaceId, posXEdit, posYEdit, displayLinkId, linkType) {
		$("#spaceLinkInfoLabel").text(spaceLinkName);
        $("#spaceLinkId").val(spaceLinkId);
        selectedSpaceLinkId=spaceLinkId;
        storeX=posXEdit;
		storeY=posYEdit;
		$("#spaceLinkInfoLabel").text(spaceLinkName);
		$("#spaceLinkDisplayId").val(displayLinkId);
		$("#spaceLinkInfoLabelEdit").text(spaceLinkName);
		$("#spaceLinkLabelEdit").val(spaceLinkName);
		$("#spaceLinkId").val(spaceLinkId);
		$("#spaceLinkIdValueEdit").val(spaceLinkId);
		$("#spaceLinkRotationEdit").val(rotation);
		$('#typeSpaceEdit option[value="'+linkType+'"]').attr("selected", "selected");
		$('#spaceLinkIdEdit option[value="'+selectedSpaceId+'"]').attr("selected", "selected");
        resetHighlighting(); 
        $('[data-link-id="' + spaceLinkId + '"]').css("color", "#c1bb88");
        $('div[data-link-id="' + spaceLinkId + '"]').removeClass("alert-primary");
        $('div[data-link-id="' + spaceLinkId + '"]').addClass("alert-warning");
        $('img[data-link-id="' + spaceLinkId + '"]').css("border", "solid 1px #c1bb88");
        $('[data-link-id="unpublished-' + spaceLinkId + '"]').css("color", "#c1bb88");
        
        $("#bgImage").on("click", function(e){
			e.preventDefault();
			var posX = $(this).position().left;
			var posY = $(this).position().top;    
			storeX = e.pageX - $(this).offset().left;
			storeY = e.pageY - $(this).offset().top;
			showSpaceLinkEdit(editSpaceLinkInfo());
		});
        hideLinkInfoTabs();
        $("#editSpaceLinkInfo").show();
	}
	
	function makeModuleLinksEditable(moduleLinkName, moduleLinkId, rotation, selectedModuleId, posXEdit, posYEdit, displayLinkId, linkType) {
	    selectedModuleLinkId=moduleLinkId;
	    storeX=posXEdit;
		storeY=posYEdit;
		$("#moduleLinkInfoLabel").text(moduleLinkName);
		$("#moduleLinkDisplayId").val(displayLinkId);
		$("#moduleLinkInfoLabelEdit").text(moduleLinkName);
		$("#moduleLinkLabelEdit").val(moduleLinkName);
		$("#moduleLinkId").val(moduleLinkId);
		$("#moduleLinkIdValueEdit").val(moduleLinkId);
		$("#moduleLinkRotationEdit").val(rotation);
		$('#typeEdit option[value="'+linkType+'"]').attr("selected", "selected");
		$('#moduleLinkIdEdit option[value="'+selectedModuleId+'"]').attr("selected", "selected");
		resetHighlighting();     
		$('[data-link-id="' + moduleLinkId + '"]').css("color", "#c1bb88");
		$('div[data-link-id="' + moduleLinkId + '"]').removeClass("alert-primary");
		$('div[data-link-id="' + moduleLinkId + '"]').addClass("alert-warning");
		$('img[data-link-id="' + moduleLinkId + '"]').css("border", "solid 1px #c1bb88");
		
		$("#bgImage").on("click", function(e){
			e.preventDefault();
			var posX = $(this).position().left;
			var posY = $(this).position().top;    
			storeX = e.pageX - $(this).offset().left;
			storeY = e.pageY - $(this).offset().top;
			showModuleLinkEdit(editModuleLinkInfo());
		});
		hideLinkInfoTabs();
        $("#editModuleLinkInfo").show();
	}
	
	function makeExternalLinksEditable(linkName, linkId, externalLinkURL, posXEdit, posYEdit, displayLinkId, linkType) {
	    selectedExternalLinkId=linkId;
	    storeX=posXEdit;
		storeY=posYEdit;
        $("#externalLinkInfoLabel").text(linkName);
        $("#externalLinkId").val(linkId);
		$("#externalLinkDisplayId").val(displayLinkId);
		$("#externalLinkInfoLabelEdit").text(linkName);
		$("#externalLinkLabelEdit").val(linkName);
		$("#externalLinkIdValueEdit").val(linkId);
		$("#externalLinkURLEdit").val(externalLinkURL);
		$('#extTypeEdit option[value="'+linkType+'"]').attr("selected", "selected");
        resetHighlighting();
              
        $('[data-link-id="' + linkId + '"]').css("color", "#c1bb88");
        $('div[data-link-id="' + linkId + '"]').removeClass("alert-primary");
        $('div[data-link-id="' + linkId + '"]').addClass("alert-warning");
        $('img[data-link-id="' + linkId + '"]').css("border", "solid 1px #c1bb88");
        
        $("#bgImage").on("click", function(e){
			e.preventDefault();
			var posX = $(this).position().left;
			var posY = $(this).position().top;    
			storeX = e.pageX - $(this).offset().left;
			storeY = e.pageY - $(this).offset().top;
			showExternalLinkEdit(editExternalLinkInfo());
		});
        hideLinkInfoTabs();
        $("#externalLinkInfo").show();
        $("#editExternalLinkInfo").show();
    }
	
	function resetHighlighting() {
        // reset icon links
        $('[data-link-id]').css("color", "red");
         
        // reset alert links
        $('div[data-link-id]').removeClass("alert-warning");
        $('div[data-link-id]').addClass("alert-primary");
        
        // reset image links
        $('img[data-link-id]').css("border-width", "0px"); 
    }
});

</script>
<style>
.Info_cz_Class .tooltiptext {
    visibility: hidden;
    width: 120px;
    color: white;
    text-align: center;
    font-size: 12px padding: 3px 0;
    border-radius: 6px;
    position: absolute;
    z-index: 1;
    left: -38px;
    top: 31px;
    background: rgba(0, 0, 0, 0.6);
}

.Info_cz_Class:hover .tooltiptext {
    visibility: visible;
}
</style>

<h1>
	Space: ${space.name} <small style="margin-left: 10px;"><a
		href="<c:url value="/staff/space/${space.id}/edit" />"><span
			data-feather="edit"></span></a></small>
</h1>

<div class="alert alert-light" role="alert">
	Created on <span class="date">${space.creationDate}</span> by
	${space.createdBy}. <br> Modified on <span class="date">${space.modificationDate}</span>
	by ${space.modifiedBy}.<br><br>
    <i class="fa fa-exclamation-triangle fa-sm" aria-hidden="true"
        style="color: #bfb168;"></i> indicates the links to unpublished spaces.
</div>
<div style="padding-bottom: 10px;">
	<c:url value="/staff/space/${space.id}/status" var="postUrl" />
	<form:form method="POST"
		action="${postUrl}?${_csrf.parameterName}=${_csrf.token}">
		<label><h5>Space Status:</h5></label>
		<select class="form-control" name="statusParam"
			style="width: 200px; display: inline;">
			<option id="Published" value="PUBLISHED"
				<c:if test="${space.spaceStatus=='PUBLISHED'}">selected</c:if>>Published</option>
			<option id="Unpublished" value="UNPUBLISHED"
				<c:if test="${space.spaceStatus=='UNPUBLISHED'}">selected</c:if>>Unpublished</option>
		</select>
		<p style="display: inline; padding-left: 10px; padding-top: 1000px;">
			<input class="btn btn-primary" type="submit" value="Submit" />
		</p>
	</form:form>
</div>
<div style="padding-bottom: 10px;">
	<c:url value="/staff/space/${space.id}/showSpaceLinks" var="postUrl" />
	<form:form method="POST"
		action="${postUrl}?${_csrf.parameterName}=${_csrf.token}">
		<label><h5>Show Links to Unpublished Spaces:</h5></label>
		<select class="form-control" name="showSpaceLinksParam"
			style="width: 200px; display: inline;">
			<option id="No" value=false
				<c:if test="${space.showUnpublishedLinks eq false}">selected</c:if>>No</option>
			<option id="Yes" value=true
				<c:if test="${space.showUnpublishedLinks eq true}">selected</c:if>>Yes</option>
		</select>
		<p style="display: inline; padding-left: 10px; padding-top: 1000px;">
			<input class="btn btn-primary" type="submit" value="Submit" />
		</p>
	</form:form>
</div>



<h5>Description:</h5>
<p style="max-height: 100px; overflow-y: scroll;">
	${space.description}</p>


<div id="errorAlert"
	class="alert alert-danger alert-dismissible fade show" role="alert"
	style="display: none; position: fixed; top: 10%; right: 50px; z-index: 10">
	<strong>Error!</strong> <span id="errorMsg"></span>
	<button type="button" class="close" id="closeAlert" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
</div>

<c:url
	value="/staff/space/${space.id}/spacelink?${_csrf.parameterName}=${_csrf.token}"
	var="postUrl" />
<form id="createSpaceLinkForm">
	<div id="createSpaceLinkAlert" class="alert alert-secondary"
		role="alert"
		style="cursor: move; width: 250px; height: 400px; display: none; position: absolute; top: 400px; right: 50px; z-index: 999">
		<div class="row">
			<div class="col">
				<h6 class="alert-heading">
					<small>Create new Space Link</small>
				</h6>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<small>Please click on the image where you want to place the
					new space link. Then click "Create Space Link".</small>
				</p>
				<hr>
			</div>
		</div>

		<input type="hidden" name="x" id="spaceLinkX" /> <input type="hidden"
			name="y" id="spaceLinkY" />

		<div class="row">
			<div class="col-sm-4">
				<label><small>Rotation:</small> </label>
			</div>
			<div class="col-sm-8">
				<input class="form-control-xs" type="number" id="spaceLinkRotation"
					name="rotation" value="0"><br>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-4">
				<label><small>Label:</small> </label>
			</div>
			<div class="col-sm-8">
				<input class="form-control-xs target" type="text"
					name="spaceLinkLabel" id="spaceLinkLabel"><br>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-4">
				<label><small>Type:</small> </label>
			</div>
			<div class="col-sm-8">
				<select id="type" name="type" class="form-control-xs target"  style="width: inherit;">
					<option selected value="">Choose...</option>
					<option value="IMAGE">Image</option>
					<option value="ARROW">Link</option>
					<option value="ALERT">Alert</option>
				</select>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-5" style="padding-right: 0px;">
				<label><small>Linked Space:</small> </label>
			</div>
			<div class="col-sm-7">
				<select id="linkedSpace" name="linkedSpace"
					class="form-control-xs target" style="width: inherit;">
					<option selected value="">Choose...</option>
					<c:forEach items="${spaces}" var="space">
						<option value="${space.id}">${space.name}<c:if test="${space.spaceStatus=='UNPUBLISHED'}"> (Unpublished)</c:if></option>
					</c:forEach>
				</select>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-3" style="padding-right: 0px;">
				<label><small>Image:</small> </label>
			</div>
			<div class="col-sm-9">
				<input type="file" class="form-control-xs" type="text"
					name="spaceLinkImage" id="spaceLinkImage"><br>
			</div>
		</div>

		<HR>
		<p class="mb-0 text-right">
			<button id="cancelSpaceLinkBtn" type="reset"
				class="btn btn-light btn-xs">Cancel</button>
			<button id="createSpaceLinkBtn" type="reset"
				class="btn btn-primary btn-xs">Create Space Link</button>
		</p>

	</div>
</form>

<c:url
	value="/staff/space/${space.id}/modulelink?${_csrf.parameterName}=${_csrf.token}"
	var="postUrl" />
<form id="createModuleLinkForm">
	<div id="createModuleLinkAlert" class="alert alert-secondary"
		role="alert"
		style="cursor: move; width: 250px; height: 400px; display: none; position: absolute; top: 400px; right: 50px; z-index: 999">
		<div class="row">
			<div class="col">
				<h6 class="alert-heading">
					<small>Create new Module Link</small>
				</h6>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<small>Please click on the image where you want to place the
					new module link. Then click "Create Module Link".</small>
				</p>
				<hr>
			</div>
		</div>

		<input type="hidden" name="x" id="moduleLinkX" /> <input
			type="hidden" name="y" id="moduleLinkY" />

		<div class="row">
			<div class="col-sm-4">
				<label><small>Rotation:</small> </label>
			</div>
			<div class="col-sm-8">
				<input class="form-control-xs modulelink-target" type="number"
					id="moduleLinkRotation" name="rotation" value="0"><br>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-4">
				<label><small>Label:</small> </label>
			</div>
			<div class="col-sm-8">
				<input class="form-control-xs modulelink-target" type="text"
					name="moduleLinkLabel" id="moduleLinkLabel"><br>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-4">
				<label><small>Type:</small> </label>
			</div>
			<div class="col-sm-8">
				<select id="type" name="type"
					class="form-control-xs modulelink-target">
					<option selected value="">Choose...</option>
					<option value="MODULE">Module</option>
				</select>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-5" style="padding-right: 0px;">
				<label><small>Linked Modules:</small> </label>
			</div>
			<div class="col-sm-7">
				<select id="linkedModule" name="linkedModule"
					class="form-control-xs modulelink-target">
					<option selected value="">Choose...</option>
					<c:forEach items="${moduleList}" var="module">
						<option value="${module.id}">${module.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>

		<HR>
		<p class="mb-0 text-right">
			<button id="cancelModuleLinkBtn" type="reset"
				class="btn btn-light btn-xs">Cancel</button>
			<button id="createModuleLinkBtn" type="reset"
				class="btn btn-primary btn-xs">Create Module Link</button>
		</p>

	</div>
</form>

<c:url value="/staff/space/update/${space.id}" var="postUrl" />
<form:form method="post"
	action="${postUrl}?${_csrf.parameterName}=${_csrf.token}"
	enctype="multipart/form-data">

	<div id="changeBgImgAlert" class="alert alert-secondary" role="alert"
		style="cursor: move; width: 340px; height: 130px; display: none; position: absolute; top: 100px; right: 50px; z-index: 999">
		<h6>
			<small>Change Background Image: </small>
		</h6>
		<input type="file" name="file" rows="5" cols="500" id="file" /><br>
		<br>
		<p class="mb-0 text-right">
			<button type="submit" id="changeBgImgBtn"
				class="btn btn-primary btn-xs">Upload Image</button>
			&nbsp
			<button id="cancelBgImgBtn" type="button" class="btn light btn-xs">Cancel</button>
		</p>
	</div>

</form:form>

<form id="createExternalLinkForm">
	<div id="createExternalLinkAlert" class="alert alert-secondary"
		role="alert"
		style="cursor: move; width: 250px; height: 400px; display: none; position: absolute; top: 400px; right: 50px; z-index: 999">
		<h6 class="alert-heading">
			<small>Create new External Link</small>
		</h6>
		<p>
			<small>Please click on the image where you want to place the
				new external link. Then click "Create External Link".</small>
		</p>
		<hr>

		<input type="hidden" name="x" id="externalLinkX" /> <input
			type="hidden" name="y" id="externalLinkY" /> <label
			style="margin-right: 5px;"><small>Label:</small> </label> <input
			class="form-control-xs extlink-target" type="text"
			name="externalLinkLabel" id="externalLinkLabel"><br> <label
			style="margin-right: 5px;"><small>External Link</small> </label> <input
			class="form-control-xs extlink-target" type="text" size="15"
			name="url" id="externalLink"><br>

		<div class="row">
			<div class="col-sm-4">
				<label><small>Type:</small> </label>
			</div>
			<div class="col-sm-8">
				<select id="extType" name="type"
					class="form-control-xs extlink-target">
					<option selected value="">Choose...</option>
					<option value="IMAGE">Image</option>
					<option value="ARROW">Link</option>
				</select>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-3" style="padding-right: 0px;">
				<label><small>Image:</small> </label>
			</div>
			<div class="col-sm-9">
				<input type="file" class="form-control-xs" type="text"
					name="externalLinkImage" id="externalLinkImage"><br>
			</div>
		</div>

		<HR>
		<p class="mb-0 text-right">
			<button id="cancelExternalLinkBtn" type="reset"
				class="btn btn-light btn-xs">Cancel</button>
			<button id="createExternalLinkBtn" type="submit"
				class="btn btn-primary btn-xs">Create External Link</button>
		</p>
	</div>
</form>

<form id="editSpaceLinkForm">
	<div id="editSpaceLinkInfo" class="alert alert-secondary" role="alert"
		style="cursor: move; width: 250px; height: 400px; display: none; position: absolute; top: 400px; right: 50px; z-index: 999">
		<p class="float-right">
			<a href="#" id="closeEditSpaceLinkInfo"><span
				data-feather="x-square"></span></a>
		</p>
		<h6 class="alert-heading">
			Space Link: <span id="spaceLinkInfoLabelEdit"></span>
		</h6>
		<input type="hidden" name="spaceLinkIdValueEdit"
			id="spaceLinkIdValueEdit" />
		<div class="row">
			<div class="col">
				<h6 class="alert-heading">
					<small>Modify Space Link</small>
				</h6>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<small>Please click on the image where you want to place the
					selected space link. Then click "Edit Space Link".</small>
				</p>
				<hr>
			</div>
		</div>

		<input type="hidden" name="x" id="spaceLinkXEdit" /> <input
			type="hidden" name="y" id="spaceLinkYEdit" /> <input type="hidden"
			name="spaceLinkDisplayId" id="spaceLinkDisplayId" />


		<div class="row">
			<div class="col-sm-4">
				<label><small>Rotation:</small> </label>
			</div>
			<div class="col-sm-8">
				<input class="form-control-xs spacelink-targetEdit" type="number"
					id="spaceLinkRotationEdit" name="rotation"><br>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-4">
				<label><small>Label:</small> </label>
			</div>
			<div class="col-sm-8">
				<input class="form-control-xs spacelink-targetEdit" type="text"
					name="spaceLinkLabel" id="spaceLinkLabelEdit"><br>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-4">
				<label><small>Type:</small> </label>
			</div>
			<div class="col-sm-8">
				<select id="typeSpaceEdit" name="type"
					class="form-control-xs spacelink-targetEdit" style="width: inherit;">
					<option value="IMAGE">Image</option>
					<option value="ARROW">Link</option>
					<option value="ALERT">Alert</option>
				</select>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-5" style="padding-right: 0px;">
				<label><small>Linked Space:</small> </label>
			</div>
			<div class="col-sm-7">
				<select id="spaceLinkIdEdit" name="linkedSpace"
					class="form-control-xs spacelink-targetEdit" style="width: inherit;">
					<c:forEach items="${spaces}" var="space">
						<option value="${space.id}">${space.name}<c:if test="${space.spaceStatus=='UNPUBLISHED'}">(Unpublished)</c:if></option>
					</c:forEach>
				</select>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-3" style="padding-right: 0px;">
				<label><small>Image:</small> </label>
			</div>
			<div class="col-sm-9">
				<input type="file" class="form-control-xs" type="text"
					name="spaceLinkImage" id="spaceLinkImageEdit"><br>
			</div>
		</div>
		<HR>
		<div class="row">
			<div class="col-sm-4">
				<button id="editSpaceLinkBtn" type="reset"
					class="btn btn-primary btn-xs">Save</button>
			</div>
			<div class="col-sm-8">
				<button id="deleteSpaceLinkButton" type="reset"
					class="btn btn-primary btn-xs">Delete</button>
			</div>
		</div>
	</div>
</form>

<form id="editModuleLinkForm">
	<div id="editModuleLinkInfo" class="alert alert-secondary" role="alert"
		style="cursor: move; width: 250px; height: 400px; display: none; position: absolute; top: 400px; right: 50px; z-index: 999">
		<p class="float-right">
			<a href="#" id="closeEditModuleLinkInfo"><span
				data-feather="x-square"></span></a>
		</p>
		<h6 class="alert-heading">
			Module Link: <span id="moduleLinkInfoLabelEdit"></span>
		</h6>
		<input type="hidden" name="moduleLinkIdValueEdit"
			id="moduleLinkIdValueEdit" />
		<div class="row">
			<div class="col">
				<h6 class="alert-heading">
					<small>Modify Module Link</small>
				</h6>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<small>Please click on the image where you want to place the
					existing module link. Then click "Edit Module".</small>
				</p>
				<hr>
			</div>
		</div>
		<input type="hidden" name="x" id="moduleLinkXEdit" /> <input
			type="hidden" name="y" id="moduleLinkYEdit" /> <input type="hidden"
			name="moduleLinkDisplayId" id="moduleLinkDisplayId" />

		<div class="row">
			<div class="col-sm-4">
				<label><small>Rotation:</small> </label>
			</div>
			<div class="col-sm-8">
				<input class="form-control-xs modulelink-targetEdit" type="number"
					id="moduleLinkRotationEdit" name="rotation"><br>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-4">
				<label><small>Label:</small> </label>
			</div>
			<div class="col-sm-8">
				<input class="form-control-xs modulelink-targetEdit" type="text"
					name="moduleLinkLabel" id="moduleLinkLabelEdit"><br>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-4">
				<label><small>Type:</small> </label>
			</div>
			<div class="col-sm-8">
				<select id="typeEdit" name="type"
					class="form-control-xs modulelink-targetEdit">
					<option value="MODULE">Module</option>
				</select>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-5" style="padding-right: 0px;">
				<label><small>Linked Modules:</small> </label>
			</div>
			<div class="col-sm-7">
				<select id="moduleLinkIdEdit" name="linkedModule"
					class="form-control-xs modulelink-targetEdit" style="width: 100%;">
					<c:forEach items="${moduleList}" var="module">
						<option value="${module.id}">${module.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<HR>
		<div class="row">
			<div class="col-sm-4">
				<button id="editModuleLinkBtn" type="reset"
					class="btn btn-primary btn-xs">Save</button>
			</div>
			<div class="col-sm-8">
				<button id="deleteModuleLinkButton" type="reset"
					class="btn btn-primary btn-xs">Delete</button>
			</div>
		</div>
	</div>
</form>

<form id="editExternalLinkForm">
	<div id="editExternalLinkInfo" class="alert alert-secondary"
		role="alert"
		style="cursor: move; width: 250px; height: 400px; display: none; position: absolute; top: 400px; right: 50px; z-index: 999">
		<p class="float-right">
			<a href="#" id="closeEditExternalLinkInfo"><span
				data-feather="x-square"></span></a>
		</p>
		<h6 class="alert-heading">
			External Link: <span id="externalLinkInfoLabelEdit"></span>
		</h6>
		<input type="hidden" name="externalLinkIdValueEdit"
			id="externalLinkIdValueEdit" />
		<div class="row">
			<div class="col">
				<h6 class="alert-heading">
					<small>Modify External Link</small>
				</h6>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<small>Please click on the image where you want to place the
					existing external link. Then click "Edit External Link".</small>
				</p>
				<hr>
			</div>
		</div>
		<input type="hidden" name="x" id="externalLinkXEdit" /> <input
			type="hidden" name="y" id="externalLinkYEdit" /> <input
			type="hidden" name="externalLinkDisplayId" id="externalLinkDisplayId" />

		<div class="row">
			<div class="col-sm-4">
				<label><small>Label:</small> </label>
			</div>
			<div class="col-sm-8">
				<input class="form-control-xs externallink-targetEdit" type="text"
					name="externalLinkLabel" id="externalLinkLabelEdit"><br>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-5" style="padding-right: 0px;">
				<label><small>External Link</small> </label>
			</div>
			<div class="col-sm-7">
				<input class="form-control-xs externallink-targetEdit" type="text"
					name="url" id="externalLinkURLEdit">
			</div>
		</div>

		<div class="row">
			<div class="col-sm-4">
				<label><small>Type:</small> </label>
			</div>
			<div class="col-sm-8">
				<select id="extTypeEdit" name="type"
					class="form-control-xs externallink-targetEdit">
					<option value="IMAGE">Image</option>
					<option value="ARROW">Link</option>
				</select>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-3" style="padding-right: 0px;">
				<label><small>Image:</small> </label>
			</div>
			<div class="col-sm-9">
				<input type="file" class="form-control-xs" type="text"
					name="externalLinkImage" id="externalLinkImageEdit"><br>
			</div>
		</div>

		<HR>
		<div class="row">
			<div class="col-sm-4">
				<button id="editExternalLinkBtn" type="reset"
					class="btn btn-primary btn-xs">Save</button>
			</div>
			<div class="col-sm-8">
				<button id="deleteExternalLinkButton" type="reset"
					class="btn btn-primary btn-xs">Delete</button>
			</div>
		</div>
	</div>
</form>

<nav class="navbar navbar-expand-sm navbar-light bg-light">
	<button type="button" id="addSpaceLinkButton"
		class="btn btn-primary btn-sm">Add Space Link</button>
	&nbsp
	<button type="button" id="addModuleLinkButton"
		class="btn btn-primary btn-sm">Add Module Link</button>
	&nbsp
	<button type="button" id="addExternalLinkButton"
		class="btn btn-primary btn-sm">Add External Link</button>
	&nbsp
	<button type="button" id="changeBgImgButton"
		class="btn btn-primary btn-sm">Change Image</button>
	&nbsp
	<button type="button" id="deleteSpace" class="btn btn-primary btn-sm">Delete
		Space</button>
</nav>
<p></p>

<div class="modal fade" id="confirm-space-delete" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="deleteModalTitle">Confirm
					${space.id} Deletion?</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
			</div>
			<div class="modal-body">
				<p>Are you sure you want to delete ${space.id}?</p>
				<div id="warningMessage">
					<div class="text-danger">Other spaces have links to this
						space!</div>
				</div>
				<div id="exhibitionMessage">
					<div class="text-danger">This space is the start of the
						exhibition. Deleting it will make your exhibition unavailable.</div>
				</div>
				<div id="finalWarning">
					<div class="text-danger">Do you want to continue?</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id="closeButton" class="btn btn-default"
					data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-danger btn-ok">Yes,
					delete!</button>
			</div>
		</div>
	</div>
</div>

<c:if test="${not empty space.image}">
	<div id="space" style="float: left">
		<img style="max-width:${display.width}px;" id="bgImage"
			src="<c:url value="/api/image/${space.image.id}" />" />
	</div>
</c:if>

<!-- To display the spacelinks on current space and spaces from where current space is linked. -->

<div id="outgoingLinks" class="list-group"
	style="margin-left: auto; padding-right: 0.25em; text-align: right; width: 20%">
	<a href="#" style="padding-right: 0.25em; background-color: #c1bb88;"
		class="list-group-item active"> Space links on this space: </a>
	<div id="noLinksOnSpace"
		style="overflow: hidden; padding-right: 0.25em; text-align: right; display: none">
		There are no links on this space.</div>
	<c:forEach items="${linksOnThisSpace}" var="spaceLinks">
		<c:if test="${not empty spaceLinks.targetSpace}">
			<a href="<c:url value='/staff/space/${spaceLinks.targetSpace.id}'/>"
				style='padding: .25rem .25rem;' class="list-group-item"
				id="${spaceLinks.id}">${spaceLinks.name}&nbsp;->
				&nbsp;${spaceLinks.targetSpace.name}</a>
		</c:if>
		<c:if test="${empty spaceLinks.targetSpace}">
			<a href="#" style='padding: .25rem .25rem;' class="list-group-item"
				id="${spaceLinks.id}">${spaceLinks.name}&nbsp;-> &nbsp;&lt;No
				Space&gt;</a>
		</c:if>
	</c:forEach>
</div>
<p></p>
<div id="incomingLinks" class="list-group"
	style="margin-left: auto; padding-right: 0.25em; text-align: right; width: 20%">
	<a href="#" style="padding-right: 0.25em; background-color: #c1bb88;"
		class="list-group-item active"> Space links to this space: </a>
	<div id="noLinksToSpace"
		style="overflow: hidden; padding-right: 0.25em; text-align: right; display: none">
		There are no links to this space.</div>
	<c:forEach items="${linksToThisSpace}" var="spaceLinks">
		<a href="<c:url value='/staff/space/${spaceLinks.sourceSpace.id}'/>"
			style='padding: .25rem .25rem;' class="list-group-item"
			id="${spaceLinks.id}">${spaceLinks.sourceSpace.name}</a>
	</c:forEach>
</div>
