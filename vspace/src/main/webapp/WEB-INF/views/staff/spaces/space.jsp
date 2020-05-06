<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script src="https://use.fontawesome.com/releases/v5.8.1/js/all.js" data-auto-replace-svg="nest"></script>
<script>
//# sourceURL=click.js
$( document ).ready(function() {	

	
	<c:forEach items="${spaceLinks}" var="link" varStatus="loop">
	{
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		
		var link;
		if ("${link.type}" == "ALERT") {
			link = $('<div class="alert alert-primary" role="alert" data-link-id="${link.link.id}"><p>${link.link.name}</p></div>');
		} else if ("${link.type}" == "IMAGE" && "${link.image.id}" != "") {
           link = $('<img id="${link.image.id}" data-link-id="${link.link.id}" src="<c:url value="/api/image/${link.image.id}" />" />');
		}  else {
			link = $('<span data-link-id="${link.link.id}"><span data-feather="navigation-2" class="flex"></span></span><p class="label-${loop.index}" data-link-id="${link.link.id}">${link.link.name}</p>');
		}
		link.css('position', 'absolute');
		link.css('left', ${link.positionX} + posX);
		link.css('top', ${link.positionY} + posY);
		link.css('transform', 'rotate(${link.rotation}deg)');
		link.find("span").css('fill', 'red');
		link.css('color', 'red');
		link.css('font-size', "10px");
		
		$("#space").append(link);
		
		$(".label-${loop.index}").css({
			'transform': 'rotate(0deg)',
			'left': ${link.positionX} + posX - 10,
			'top': ${link.positionY} + posY + 16,
			'color': 'red'
		});		
		
		$('[data-link-id="${link.link.id}"]').css('cursor', 'pointer');
		$('[data-link-id="${link.link.id}"]').click(function(e) {
			makeSpaceLinksEditable("${link.link.name}", "${link.link.id}");
		});
	}
	</c:forEach>
	
	<c:forEach items="${moduleLinks}" var="link" varStatus="loop">
	{
		var link;
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		if ("${link.type}" == "ALERT") {
			link = $('<div class="alert alert-primary" role="alert" data-link-id="${link.link.id}"><p>${link.link.name}</p></div>');
		} else if ("${link.type}" == "IMAGE" && "${link.image.id}" != "") {
           link = $('<img id="${link.image.id}" data-link-id="${link.link.id}" src="<c:url value="/api/image/${link.image.id}" />" />');
		}  else {
			link = $('<span data-link-id="${link.link.id}" ><span class="fas fa-book-open"></span></span><p class="mlabel-${loop.index}" data-link-id="${link.link.id}">${link.link.name}</p>');
		}
		link.css('position', 'absolute');
		link.css('left', ${link.positionX} + posX);
		link.css('top', ${link.positionY} + posY);
		link.css('transform', 'rotate(${link.rotation}deg)');
		link.find("span").css('fill', 'red');
		link.css('color', 'red');
		link.css('font-size', "10px");
		
		$("#space").append(link);
		
		$(".mlabel-${loop.index}").css({
			'transform': 'rotate(0deg)',
			'left': ${link.positionX} + posX - 10,
			'top': ${link.positionY} + posY + 16,
			'color': 'red'
		});		
		
		$('[data-link-id="${link.link.id}"]').css('cursor', 'pointer');
		$('[data-link-id="${link.link.id}"]').click(function(e) {
			makeModuleLinksEditable("${link.link.name}", "${link.link.id}");
		});
	}
	</c:forEach> 	
	
	<c:forEach items="${externalLinks}" var="link" varStatus="loop">
	{
        var posX = $("#bgImage").position().left;
        var posY = $("#bgImage").position().top;
        var link ;
        if ("${link.type}" == "IMAGE" && "${link.image.id}" != "") {
            link = $('<img id="${link.image.id}" data-link-id="${link.externalLink.id}" src="<c:url value="/api/image/${link.image.id}" />" />');
        }  else {
            link = $('<span data-link-id="${link.externalLink.id}" ><span class="fa fa-globe"></span></span><p class="mlabel-${loop.index}" data-link-id="${link.externalLink.id}">${link.externalLink.name}</p>');
        }
        link.css('position', 'absolute');
        link.css('left', ${link.positionX} + posX);
        link.css('top', ${link.positionY} + posY);
        link.css('color', 'blue');
        link.css('font-size', "12px");
        
        $("#space").append(link);
        
        $("#label-${loop.index}").css({
            'transform': 'rotate(0deg)',
            'left': ${link.positionX} + posX - 10,
            'top': ${link.positionY} + posY + 16,
            'text-color': 'blue'
        });
        
        $('[data-link-id="${link.externalLink.id}"]').css('cursor', 'pointer');
        $('[data-link-id="${link.externalLink.id}"]').click(function(e) {
            makeExternalLinksEditable("${link.externalLink.name}", "${link.externalLink.id}");
        });
	}
	</c:forEach> 
	
	// --------- draggable modals -----------
	$("#createSpaceLinkAlert").draggable();
	$("#createModuleLinkAlert").draggable();
	$('#spaceLinkCreationModal.draggable>.modal-dialog>.modal-content>.modal-header').css('cursor', 'move');
	 
	$("#createExternalLinkAlert").draggable();
	$("#changeBgImgAlert").draggable();
	$("#spaceLinkInfo").draggable();
	$("#externalLinkInfo").draggable();
	$("#moduleLinkInfo").draggable();
    
    
	// store where a user clicked on an image
	var storeX;
	var storeY;

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
		
		if (storeX == undefined || storeY == undefined) {
			$("#errorMsg").text("Please click on the image to specify where the new link should be located.")
			$('#errorAlert').show();
			return;
		}
		
		$("#spaceLinkX").val(storeX);
		$("#spaceLinkY").val(storeY);
		
		var form = $("#createSpaceLinkForm");
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
	            showSpaceLink(spaceLinkInfo, true);
	            $("#space_label").attr("id","");
	            $("#link").attr("id","");
	            $("#createSpaceLinkAlert").hide();  
	            $("#errorMsg").text("");
	            $('#errorAlert').hide();
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
	        	showModuleLink(moduleLinkInfo, true);
	        	$("#module_label").attr("id","");
	        	$("#link").attr("id","");
	        	$("#createModuleLinkAlert").hide();
	        	$("#errorMsg").text("");
	        	$('#errorAlert').hide();
	        }
		});
	});
	
	$("#createExternalLinkBtn").click(function(e) {
		var payload = {};
		
		if (storeX == undefined || storeY == undefined) {
			$("#errorMsg").text("Please click on the image to specify where the new link should be located.")
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
		var linkId = $("#spaceLinkId").val();
		$.ajax({
		  url: "<c:url value="/staff/space/${space.id}/spacelink/" />" + linkId + "?${_csrf.parameterName}=${_csrf.token}",
		  method: "DELETE",
		  success:function(data) {
			  $('[data-link-id="' + linkId + '"]').remove();
	          $("#spaceLinkInfo").hide();
		    }
		});
	});
	
	
	$("#deleteModuleLinkButton").click(function() {
		var linkId = $("#moduleLinkId").val();
		$.ajax({
			url: "<c:url value="/staff/space/${space.id}/modulelink/" />" + linkId + "?${_csrf.parameterName}=${_csrf.token}",
			method: "DELETE",
				success:function(data) {
					$('[data-link-id="' + linkId + '"]').remove();
					$("#moduleLinkInfo").hide();
				}
			});
	});
	
	$("#deleteExternalLinkButton").click(function() {
        var linkId = $("#externalLinkId").val();
        $.ajax({
            url: "<c:url value="/staff/space/${space.id}/externallink/" />" + linkId + "?${_csrf.parameterName}=${_csrf.token}",
            method: "DELETE",
                success:function(data) {
                    $('[data-link-id="' + linkId + '"]').remove();
                    $("#externalLinkInfo").hide();
                }
            });
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
		var space_label = $("<p id='space_label'></p>");
		space_label.text(spaceLink["spaceLinkLabel"]);
		
		var link;
		if (spaceLink["type"] == "ALERT") {
			link = $('<div id="link" class="alert alert-primary" role="alert" data-link-id="' + spaceLink["id"] + '"><p>'+spaceLink["spaceLinkLabel"]+'</p></div>');
		} else if(spaceLink["type"] == "IMAGE" && linkIcon) {
			link = $('<div id="link" data-link-id="' + spaceLink["id"] + '"><img src="' + linkIcon + '"></div>');
		} else {
			$(space_label).css({
				'position': 'absolute',
				'font-size': "10px",
				'transform': 'rotate(0deg)',
				'left': spaceLink["x"] + posX - 10,
				'top': spaceLink["y"] + posY + 16,
				'color': 'red'
			});
			link = $('<span data-link-id="' + spaceLink["id"] + '"><div id="link" data-feather="navigation-2" class="flex"></div></span>');
		}
		if(show) {
			link.find("div").css('fill', 'red');
		}
		link.css('position', 'absolute');
		link.css('left', spaceLink["x"] + posX);
		link.css('top', spaceLink["y"] + posY);
		link.css('color', 'red');
		link.css('transform', 'rotate(' +$('#spaceLinkRotation').val()+ 'deg)');
		link.css('font-size', "10px");
		
		if (spaceLink["id"]) {
			link.attr("data-link-id", spaceLink["id"]);
			link.css('cursor', 'pointer');
			link.click(function(e) {
				makeSpaceLinksEditable(spaceLink["spaceLinkLabel"], spaceLink["id"]);
	        });
			space_label.attr("data-link-id", spaceLink["id"]);
            space_label.css('cursor', 'pointer');
            space_label.click(function(e) {
                makeSpaceLinksEditable(spaceLink["spaceLinkLabel"], spaceLink["id"]);
            });
		}

		$("#space").append(link);
		$("#space").append(space_label);

		feather.replace();
	}			        
	
	function showModuleLink(moduleLink, show) {
		$("#module_label").remove();
		$("#link").remove();
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		var module_label = $("<p id='module_label'></p>");
		module_label.text(moduleLink["moduleLinkLabel"]);
		
		var link;
		if (moduleLink["type"] == "ALERT") {
			link = $('<div id="link" class="alert alert-primary" role="alert" data-link-id="' + moduleLink["id"] + '"><p>'+moduleLink["moduleLinkLabel"]+'</p></div>');
		} else if(moduleLink["type"] == "IMAGE" && moduleLinkIcon) {
			link = $('<div id="link" data-link-id="' + moduleLink["id"] + '"><img src="' + moduleLinkIcon + '"></div>');
		} else { 
			$(module_label).css({
				'position': 'absolute',
				'font-size': "10px",
				'transform': 'rotate(0deg)',
				'left': moduleLink["x"] + posX - 10,
				'top': moduleLink["y"] + posY + 16,
				'color': 'red'
			});
			link = $('<span data-link-id="' + moduleLink["id"] + '"><div id="link" class="fas fa-book-open"></div></span>');
		} 
		if(show) {
			link.find("div").css('fill', 'red');
		}
		link.css('position', 'absolute');
		link.css('left', moduleLink["x"] + posX);
		link.css('top', moduleLink["y"] + posY);
		link.css('color', 'red');
		link.css('transform', 'rotate(' +$('#moduleLinkRotation').val()+ 'deg)');
		link.css('font-size', "10px");
		
		if (moduleLink["id"]) {
			link.attr("data-link-id", moduleLink["id"]);
			link.css('cursor', 'pointer');
			link.click(function(e) {
				makeModuleLinksEditable(moduleLink["moduleLinkLabel"], moduleLink["id"]);
			});
			module_label.attr("data-link-id", moduleLink["id"]);
			module_label.css('cursor', 'pointer');
			module_label.click(function(e) {
				makeModuleLinksEditable(moduleLink["moduleLinkLabel"], moduleLink["id"]);
			});
		}

		$("#space").append(link);
		$("#space").append(module_label);

		feather.replace();
	}
	
	function showExternalLinks(externalLink) {
		$("#ext_label").remove();
		$("#link").remove();
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		var ext_label = $("<p id='ext_label'></p>");
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
				'top': externalLink["y"] + posY + 16,
				'color': 'blue'
			});
			link = $('<span data-link-id="' + externalLink["id"] + '"><div id="link" class="fa fa-globe"></div></span>');
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
                makeExternalLinksEditable(externalLink["spaceLinkLabel"], externalLink["id"]);
            });
			
			ext_label.attr("data-link-id", externalLink["id"]);
            ext_label.css('cursor', 'pointer');
            ext_label.attr("data-link-id", externalLink["id"]);
            ext_label.css('cursor', 'pointer');
            ext_label.click(function(e) {
            	makeExternalLinksEditable(externalLink["spaceLinkLabel"], externalLink["id"]);
            });
		}

		$("#space").append(link);
		$("#space").append(ext_label);
		$("#external-link").remove();

		
	}
	
	// ------------ Cancel buttons -----------------
	$("#cancelSpaceLinkBtn").click(function() {
        storeX = null;
        storeY = null;
        $("#link").remove();
        $("#space_label").remove();
        $("#createSpaceLinkAlert").hide();  
    });
	
	$("#cancelModuleLinkBtn").click(function() {
        storeX = null;
        storeY = null;
        $("#link").remove();
        $("#module_label").remove();
        $("#createModuleLinkAlert").hide();  
    }); 
    
    $("#cancelExternalLinkBtn").click(function() {
        storeX = null;
        storeY = null;
        $("#external-link").remove();
        $("#external-arrow").remove();
        $("#ext_label").remove();
        $("#createExternalLinkAlert").hide();
    });
    
    $("#cancelBgImgBtn").click(function() {
        $("#file").val('');
        $("#changeBgImgAlert").hide();
    });
    
    $("#closeSpaceLinkInfo").click(function(e) {
    	e.preventDefault();
    	$("#spaceLinkInfoLabel").text("");
        $("#spaceLinkId").val("");
        resetHighlighting();
        $("#spaceLinkInfo").hide();
    });
    
    $("#closeModuleLinkInfo").click(function(e) {
    	e.preventDefault();
    	$("#moduleLinkInfoLabel").text("");
        $("#moduleLinkId").val("");
        resetHighlighting();
        $("#moduleLinkInfo").hide();
    }); 
    
    $("#closeExternalLinkInfo").click(function(e) {
        e.preventDefault();
        $("#externalLinkInfoLabel").text("");
        $("#externalLinkId").val("");
        resetHighlighting();
        $("#externalLinkInfo").hide();
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
	
	function makeSpaceLinksEditable(spaceLinkName, spaceLinkId) {
		$("#spaceLinkInfoLabel").text(spaceLinkName);
        $("#spaceLinkId").val(spaceLinkId);
        resetHighlighting(); 
        
        $('[data-link-id="' + spaceLinkId + '"]').css("color", "#c1bb88");
        $('div[data-link-id="' + spaceLinkId + '"]').removeClass("alert-primary");
        $('div[data-link-id="' + spaceLinkId + '"]').addClass("alert-warning");
        $('img[data-link-id="' + spaceLinkId + '"]').css("border", "solid 1px #c1bb88");
        $("#moduleLinkInfo").hide();
        $("#externalLinkInfo").hide();
        $("#spaceLinkInfo").show();
	}
	
	function makeModuleLinksEditable(moduleLinkName, moduleLinkId) {
		$("#moduleLinkInfoLabel").text(moduleLinkName);
		$("#moduleLinkId").val(moduleLinkId);
		resetHighlighting();
		      
		$('[data-link-id="' + moduleLinkId + '"]').css("color", "#c1bb88");
		$('div[data-link-id="' + moduleLinkId + '"]').removeClass("alert-primary");
		$('div[data-link-id="' + moduleLinkId + '"]').addClass("alert-warning");
		$('img[data-link-id="' + moduleLinkId + '"]').css("border", "solid 1px #c1bb88");
		$("#externalLinkInfo").hide();
        $("#spaceLinkInfo").hide();
        $("#moduleLinkInfo").show();
	}
	
	function makeExternalLinksEditable(linkName, linkId) {
        $("#externalLinkInfoLabel").text(linkName);
        $("#externalLinkId").val(linkId);
        resetHighlighting();
              
        $('[data-link-id="' + linkId + '"]').css("color", "#c1bb88");
        $('div[data-link-id="' + linkId + '"]').removeClass("alert-primary");
        $('div[data-link-id="' + linkId + '"]').addClass("alert-warning");
        $('img[data-link-id="' + linkId + '"]').css("border", "solid 1px #c1bb88");
        $("#spaceLinkInfo").hide();
        $("#moduleLinkInfo").hide();
        $("#externalLinkInfo").show();
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

<div id="errorAlert" class="alert alert-danger alert-dismissible fade show" role="alert" style="display: none; position: absolute; top: 10px; right: 50px;">
	<strong>Error!</strong> <span id="errorMsg"></span>
	<button type="button" class="close" data-dismiss="alert" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
</div>

<h1> Space: ${space.name} <small style="margin-left: 10px;"><a href="<c:url value="/staff/space/${space.id}/edit" />"><span data-feather="edit"></span></a></small></h1>

<div class="alert alert-light" role="alert">
	Created on <span class="date">${space.creationDate}</span> by ${space.createdBy}. 
	<br>
	Modified on <span class="date">${space.modificationDate}</span> by ${space.modifiedBy}.
</div>
<div style="padding-bottom: 10px;">
    <c:url value="/staff/space/${space.id}/updateStatus" var="postUrl" />
    <form:form method="POST"
        action="${postUrl}?${_csrf.parameterName}=${_csrf.token}">
        <label style="width: 200px"><h5>Space Status:</h5></label>
        <select class="form-control" name="statusParam"
            style="width: 200px; display: inline; padding-bottom: 110px">
            <option id="Published" value="Published"
                <c:if test="${space.spaceStatus=='Published'}">selected</c:if>>Published</option>
            <option id="Unpublished" value="Unpublished"
                <c:if test="${space.spaceStatus=='Unpublished'}">selected</c:if>>Unpublished</option>
        </select>
        <p
            style="display: inline; padding-left: 10px; padding-top: 1000px;">
            <input class="btn btn-primary" type="submit" value="submit" />
        </p>
    </form:form>
</div>
<h5>Description:</h5>
<p style="max-height: 100px; overflow-y: scroll;">
${space.description}
</p>

<c:url value="/staff/space/${space.id}/spacelink?${_csrf.parameterName}=${_csrf.token}" var="postUrl" />
<form id="createSpaceLinkForm">
	<div id="createSpaceLinkAlert" class="alert alert-secondary" role="alert" style="cursor: move; width: 250px; height: 400px; display: none; position: absolute; top: 400px; right: 50px; z-index: 999">
		<div class="row">
			<div class="col">
				<h6 class="alert-heading"> <small>Create new Space Link</small></h6>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<small>Please click on the image where you want to place the new space link. Then click "Create Space Link".</small></p>
				<hr>
			</div>
		</div>

		<input type="hidden" name="x" id="spaceLinkX" /> 
		<input type="hidden" name="y" id="spaceLinkY" />

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
				<select id="type" name="type" class="form-control-xs target">
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
					class="form-control-xs target">
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
				<input type="file" class="form-control-xs" type="text" name="spaceLinkImage" id="spaceLinkImage"><br>
			</div>
		</div>

		<HR>
		<p class="mb-0 text-right"> <button id="cancelSpaceLinkBtn" type="reset" class="btn btn-light btn-xs">Cancel</button> <button id="createSpaceLinkBtn" type="reset" class="btn btn-primary btn-xs">Create Space Link</button></p>

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

		<input type="hidden" name="x" id="moduleLinkX" /> 
        <input type="hidden" name="y" id="moduleLinkY" />

		<div class="row">
			<div class="col-sm-4">
				<label><small>Rotation:</small> </label>
			</div>
			<div class="col-sm-8">
				<input class="form-control-xs modulelink-target" type="number" id="moduleLinkRotation"
					name="rotation" value="0"><br>
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
				<select id="type" name="type" class="form-control-xs modulelink-target">
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
<form:form method="post" action="${postUrl}?${_csrf.parameterName}=${_csrf.token}" enctype="multipart/form-data">

	<div id="changeBgImgAlert" class="alert alert-secondary" role="alert" style="cursor: move; width: 340px; height: 130px; display: none; position: absolute; top: 100px; right: 50px; z-index: 999">
		<h6> <small>Change Background Image: </small></h6>
		<input type="file" name="file" rows="5" cols="500" id="file" /><br><br>
		<p class="mb-0 text-right"> <button type="submit" id="changeBgImgBtn" class="btn btn-primary btn-xs">Upload Image</button> &nbsp
		<button id="cancelBgImgBtn" type="button" class="btn light btn-xs">Cancel</button></p>
	</div>

</form:form>

<form id="createExternalLinkForm">
	<div id="createExternalLinkAlert" class="alert alert-secondary" role="alert" style="cursor:move; width:250px; height: 400px; display:none; position: absolute; top: 400px; right: 50px; z-index:999">
		 <h6 class="alert-heading"><small>Create new External Link</small></h6>
		  <p><small>Please click on the image where you want to place the new external link. Then click "Create External Link".</small></p>
		  <hr>  
		  
		  <input type="hidden" name="x" id="externalLinkX" />
	  	  <input type="hidden" name="y" id="externalLinkY"/>
		  
		  <label style="margin-right: 5px;"><small>Label:</small> </label>
		  <input class="form-control-xs extlink-target" type="text" name="externalLinkLabel" id="externalLinkLabel"><br>
		  
		  <label style="margin-right: 5px;"><small>External Link</small> </label>
		  <input class="form-control-xs extlink-target" type="text" size="15" name="url" id="externalLink"><br>
		  
		  <div class="row">
	      <div class="col-sm-4">
		  <label><small>Type:</small> </label>
		  </div>
	      <div class="col-sm-8">
		  <select id="extType" name="type" class="form-control-xs extlink-target" >
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
	      <input type="file" class="form-control-xs" type="text" name="externalLinkImage" id="externalLinkImage"><br>
	      </div>
	      </div>
		  
		  <HR>
		  <p class="mb-0 text-right"><button id="cancelExternalLinkBtn" type="reset" class="btn btn-light btn-xs">Cancel</button> <button id="createExternalLinkBtn" type="reset" class="btn btn-primary btn-xs">Create External Link</button></p>
</div>
</form>
<form>
	<div id="createExternalLinkAlert" class="alert alert-secondary" role="alert" style="cursor: move; width: 250px; height: 400px; display: none; position: absolute; top: 300px; right: 50px; z-index: 999">
		<h6 class="alert-heading"> <small>Create new External Link</small></h6>
		<p><small>Please click on the image where you want to place the new external link. Then click "Create External Link".</small></p>
		<hr>
		<label style="margin-right: 5px;"><small>Label:</small> </label> 
		<input class="form-control-xs extlink-target" type="text" id="externalLinkLabel"><br> 
		<label style="margin-right: 5px;"><small>External Link</small> </label> 
		<input class="form-control-xs" type="text" size="15" id="externalLink"><br>
		<HR>
		<p class="mb-0 text-right"> <button id="cancelExternalLinkBtn" type="reset" class="btn btn-light btn-xs">Cancel</button> <button id="createExternalLinkBtn" type="reset"  class="btn btn-primary btn-xs">Create External Link</button></p>
	</div>
</form>

<div id="spaceLinkInfo" class="alert alert-secondary" role="alert" style="cursor: move; width: 250px; height: 200px; display: none; position: absolute; top: 400px; right: 50px; z-index: 999">
	<p class="float-right"> <a href="#" id="closeSpaceLinkInfo"><span data-feather="x-square"></span></a></p>
	<h6 class="alert-heading"> Space Link: <span id="spaceLinkInfoLabel"></span></h6>
	<input type="hidden" name="spaceLinkId" id="spaceLinkId" />
	<button id="deleteSpaceLinkButton" type="reset" class="btn btn-primary btn-xs">Delete Link</button>
</div>

<div id="moduleLinkInfo" class="alert alert-secondary" role="alert"
	style="cursor: move; width: 250px; height: 200px; display: none; position: absolute; top: 400px; right: 50px; z-index: 999">
	<p class="float-right">
		<a href="#" id="closeModuleLinkInfo"><span data-feather="x-square"></span></a>
	</p>
	<h6 class="alert-heading">
		Module Link: <span id="moduleLinkInfoLabel"></span>
	</h6>
	<input type="hidden" name="moduleLinkId" id="moduleLinkId" />
	<button id="deleteModuleLinkButton" type="reset"
		class="btn btn-primary btn-xs">Delete Module Link</button>
</div>

<div id="externalLinkInfo" class="alert alert-secondary" role="alert"
    style="cursor: move; width: 250px; height: 200px; display: none; position: absolute; top: 400px; right: 50px; z-index: 999">
    <p class="float-right">
        <a href="#" id="closeExternalLinkInfo"><span data-feather="x-square"></span></a>
    </p>
    <h6 class="alert-heading">
        External Link: <span id="externalLinkInfoLabel"></span>
    </h6>
    <input type="hidden" name="externalLinkId" id="externalLinkId" />
    <button id="deleteExternalLinkButton" type="reset"
        class="btn btn-primary btn-xs">Delete External Link</button>
</div>

<nav class="navbar navbar-expand-sm navbar-light bg-light">
<button type="button" id="addSpaceLinkButton" class="btn btn-primary btn-sm">Add Space Link</button> &nbsp
<button type="button" id="addModuleLinkButton" class="btn btn-primary btn-sm">Add Module Link</button> &nbsp
<button type="button" id="addExternalLinkButton" class="btn btn-primary btn-sm">Add External Link</button> &nbsp
<button type="button" id="changeBgImgButton" class="btn btn-primary btn-sm"> Change Image</button> &nbsp
<button type="button" class="btn btn-primary btn-sm" data-record-id="${space.id}" data-url="<c:url value="/staff/space/${space.id}?${_csrf.parameterName}=${_csrf.token}"/>" data-call-on-success = "<c:url value="/staff/space/list"/>" data-call-on-error="<c:url value="/staff/space/list"/>" data-toggle="modal" data-target="#confirm-delete">
Delete Space
</button>
</nav>

<p></p>

<c:if test="${not empty space.image}">
	<div id="space">
		<img id="bgImage" width="800px" src="<c:url value="/api/image/${space.image.id}" />" />
	</div>
</c:if>
<jsp:include page="../../deleteModal.jsp" >
<jsp:param name="elementType" value="Space" /> 
</jsp:include>
