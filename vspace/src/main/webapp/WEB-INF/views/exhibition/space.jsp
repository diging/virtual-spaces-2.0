<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<link rel="stylesheet" type="text/css" id="applicationStylesheet"
    href="<c:url value="/resources/extra/Home.css" />">

<script>
//# sourceURL=click.js
let maxWidth='(max-width: 1200px)';
function openNav(){
    if (window.matchMedia(maxWidth).matches){
        document.getElementById("mySidenav").style.width = "170px";
    	document.getElementById("mySidenav").style.height = "260px";
    }
    else{
        document.getElementById("mySidenav").style.width = "240px";
        document.getElementById("mySidenav").style.height = ${display.height}+"px";
    }
}
function closeNav(){
    document.getElementById("mySidenav").style.width = "0px";
}
function openModuleNav(){
    if (window.matchMedia(maxWidth).matches){
        document.getElementById("mySideModulenav").style.width = "170px";
        document.getElementById("mySideModulenav").style.height = "auto";
    }
    else{
        document.getElementById("mySideModulenav").style.width = "240px";
        document.getElementById("mySideModulenav").style.height = "auto";
    }
}
function closeModuleNav(){
    document.getElementById("mySideModulenav").style.width = "0px";
}

function closeSpaceDescription(){
    document.getElementById("rightContent").style.width = "0px";
    document.getElementById("rightContent").style.height = "0px";
}

function openSpaceDescription(){
    if (window.matchMedia(maxWidth).matches){
        document.getElementById("rightContent").style.width = "170px";
        document.getElementById("rightContent").style.height = "260px";
    }
    else{
        document.getElementById("rightContent").style.width = "300px";
        document.getElementById("rightContent").style.height = ${display.height}+"px";
    }
}

$(window).on("load", function() {
    let spaceHeight = $("#bgImage").css("height");
    $("#Module_1").css("height",spaceHeight);
	$( window ).resize(function() {
		$("#space a").remove();
    	drawLinks();
    });
    drawLinks();
});

function drawLinks() {
    <c:forEach items="${spaceLinks}" var="link" varStatus="loop">
    {
        var posX = parseInt($("#space").css('margin-left')) + $("#space").position().left; 
        var posY = $("#space").position().top;
        var link = $('<a></a>');
        link.attr('href', '<c:url value="/exhibit/space/${link.link.targetSpace.id}" />');

        if ("${link.type}" == 'ALERT') {
            var linkDisplay = $('<div class="alert alert-primary" role="alert">');
        } else if ("${link.type}" == 'IMAGE' && "${link.image}" != '') {
            var linkDisplay = $('<img id="${link.image.id}" src="<c:url value="/api/image/${link.image.id}" />" />');
        } else {
            var linkDisplay=$('<div class="InfoSpace_${loop.index} Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-walking fa-lg Icon_awesome_info_c"></i><span class="tooltiptext">${link.link.name}</span></div>');
        }
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('transform', 'rotate(${link.rotation}deg)');
        linkDisplay.css('fill', 'grey'); 
        linkDisplay.css('color', 'rgba(128,128,128,1)');
        let height = parseInt($("#bgImage").css("height"));
        let width = parseInt($("#bgImage").css("width"));
        let	linkPosX=(width*${link.positionX})/${display.width};
        let	linkPosY=(height*${link.positionY})/${display.height};
        linkDisplay.css('left',linkPosX + posX);
        linkDisplay.css('top', linkPosY + posY);
        link.append(linkDisplay);
        $("#space").append(link);
        $(".label-${loop.index}").css({
          'transform': 'rotate(0deg)',
          'left': ${link.positionX} + posX - 10,
          'padding-top': '30px',
          'color': 'rgba(150, 45, 62, 1)',
          'font-size': '12px',
          'overflow': 'visible'
        });
    }
    </c:forEach>
    <c:forEach items="${moduleList}" var="link" varStatus="moduleLoop">

    {
        var posX = parseInt($("#space").css('margin-left')) + $("#space").position().left; 
        var posY = $("#space").position().top;
        var link = $('<a></a>');
        link.attr('href', '<c:url value="/exhibit/${space.id}/module/${link.link.module.id}" />');
        var linkDisplay = $('<div class="InfoModule_${moduleLoop.index} Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-info fa-lg Icon_awesome_info_m"></i><span class="tooltiptext">${link.link.name}</span></div>');
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('transform', 'rotate(${link.rotation}deg)');
        linkDisplay.css('fill', 'grey'); 
        linkDisplay.css('color', 'rgba(128,128,128,1)');
        let height = parseInt($("#bgImage").css("height"));
        let width = parseInt($("#bgImage").css("width"));
        let	linkPosX=(width*${link.positionX})/${display.width};
        let	linkPosY=(height*${link.positionY})/${display.height};
        linkDisplay.css('left',linkPosX + posX);
        linkDisplay.css('top', linkPosY + posY);
        link.append(linkDisplay);
        $("#space").append(link);
        $(".moduleLabel-${moduleLoop.index}").css({
            'transform': 'rotate(0deg)',
            'left': ${link.positionX} + posX - 10,
            'padding-top': '30px',
            'color': 'rgba(150, 45, 62, 1)',
            'font-size': '12px'
          });
    }
    </c:forEach>

    <c:forEach items="${externalLinkList}" var="link" varStatus="externalLoop">
    {
        var posX = parseInt($("#space").css('margin-left')) + $("#space").position().left; 
        var posY = $("#space").position().top;
        var link = $('<a></a>');
        link.attr('href', "${link.externalLink.externalLink}");
        link.attr('target', "_blank");
        var linkDisplay = $('<span class="fas fa-globe"></span>');
       
        if ("${link.type}" == 'IMAGE' && "${link.image}" != '') {
            var linkDisplay = $('<img id="${link.image.id}" src="<c:url value="/api/image/${link.image.id}" />" />');
        } else {
            var linkDisplay = $('<div class="InfoExt_${externalLoop.index} Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-external-link-alt fa-lg Icon_awesome_info_e"></i><span class="tooltiptext">${link.name}</span></div>');
        }
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('transform', 'rotate(${link.rotation}deg)');
        linkDisplay.css('fill', 'grey'); 
        linkDisplay.css('color', 'rgba(128,128,128,1)');
        let height = parseInt($("#bgImage").css("height"));
        let width = parseInt($("#bgImage").css("width"));
        let	linkPosX=(width*${link.positionX})/${display.width};
        let	linkPosY=(height*${link.positionY})/${display.height};
        linkDisplay.css('left',linkPosX + posX);
        linkDisplay.css('top', linkPosY + posY);
        link.append(linkDisplay);
        $("#space").append(link);
        $(".externalLabel-${externalLoop.index}").css({
            'transform': 'rotate(0deg)',
            'left': ${link.positionX} + posX - 10,
            'padding-top': '30px',
            'color': 'rgba(150, 45, 62, 1)',
            'font-size': '12px'
          });
    }
    </c:forEach>
    <c:forEach items="${spaceTextBlocks}" var="block" varStatus="loop">
	{
		var posX = $("#bgImage").position().left;
		var posY = $("#bgImage").position().top;
		
		var block;
		block = $('<span data-link-id="${block.id}" class="spaceLink-${block.id} Info_cz_Class"><textarea class="textBlock" readonly="true" style="height:${block.height}px; width:${block.width}px;" readonly>${block.spaceTextBlock.text}</textarea></span>');
		
		block.css('position', 'absolute');
		let height = parseInt($("#bgImage").css("height"));
        let width = parseInt($("#bgImage").css("width"));
        let	linkPosX=(width*${block.positionX})/${display.width};
        let	linkPosY=(height*${block.positionY})/${display.height};
		block.css('left', linkPosX + posX);
		block.css('top', linkPosY + posY);
		$("#space").append(block);
	}
	</c:forEach> 
    feather.replace();
}
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
    top: 27px;
    background: rgba(0, 0, 0, 0.6);
}

.Info_cz_Class .textBlock{
    color: white;
    font-size: 12px padding: 3px 0;
    border-radius: 6px;
    position: absolute;
    z-index: 1;
    background: rgba(0, 0, 0, 0.6);
}

.Info_cz_Class:hover .tooltiptext {
    visibility: visible;
}

@media only screen and (max-width: 800px) {
    /* For mobile phones: */
    [class*="barPosition"] {
        font-size: 12px;
    }
    [class*="closebtn"] {
        font-size: 12px;
    }
    [class*="sidenav"] {
        font-size: 12px;
        padding-top: 20px;
        height: 260px;
    }
    [class*="Home_Class"] {
        min-height: 540px;
    }
    [class*="Group_3_Class"] {
        top: 34px;
    }
    [class*="sideModulenav"] {
        top: 34px;
        height: 260px;
    }
    [class*="container-fluid"] {
        padding-right: 0px;
    }
    [class*="spaceName"] {
        font-size: 20px;
    }
}
</style>
<div class="container-fluid" <c:if test ='${showAlert eq "true"}'>style="display: none;"</c:if>>
	<div class="modalDown alert alert-warning center col-md-12" style="text-align: center;<c:if test ='${exhibitionConfig.mode == "ACTIVE"}'>display: none;</c:if>" >
    	<c:choose>
        	<c:when test="${exhibitionConfig.customMessage != '' && exhibitionConfig.mode == 'OFFLINE'}">
                <h6>${exhibitionConfig.customMessage}</h6>
            </c:when>
            <c:otherwise>
           	    <h6>${exhibitionConfig.mode.value}</h6>
            </c:otherwise>
        </c:choose>
    </div>
    <div id="Module_1" class="Home_Class">
        <div class="dropdown">
            <div id="mySidenav" class="sidenav">
                <i class="far fa-times-circle fa-lg closebtn"
                    onclick="closeNav()"></i>
                <div class="list-group spaceNav">
                    <ul>
                        <c:forEach items="${publishedSpaces}" var="space">
                            <li><a class="dropdown-item"
                                href="<c:url value="/exhibit/space/${space.id}" />">${space.name}</a>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <i class="fas fa-bars fa-lg barPosition" onclick="openNav()"></i>
        </div>
        <div class="spaceClass" id="spaceDiv" style="width: 80%; margin: auto; display: flex; justify-content: center;">
        <div>
            <c:if test="${not empty moduleList}">
                <div class=dropdown>
                    <div id="mySideModulenav" class="sideModulenav">
                        <i class="far fa-times-circle fa-lg closebtn"
                            onclick="closeModuleNav()"></i>
                        <div class="list-group spaceNav">
                            <ul  style="list-style: none;">
                                <c:forEach items="${moduleList}"
                                    var="module">
                                    <c:if test="${not empty module.link.name}">
                                    <li>
                                    <a class="dropdown-item" style="padding-left: 15px;height: 30px;"
                                        href="<c:url value="/exhibit/${space.id}/module/${module.link.module.id}" />"><div class="Info Info_cz_module_Class"><svg class="Ellipse_8_module_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_module_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_module_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-info fa-lg Icon_awesome_info_module_m"></i></div> ${module.link.name}</a></li></c:if>
                                    <c:if test="${empty module.link.name}">
                                    <li>
                                    <a class="dropdown-item" style="padding-left: 15px;height: 30px;"
                                        href="<c:url value="/exhibit/${space.id}/module/${module.link.module.id}" />"><div class="Info Info_cz_module_Class"><svg class="Ellipse_8_module_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_module_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_module_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-info fa-lg Icon_awesome_info_module_m"></i></div> ${module.link.module.name}</a></li></c:if>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <div class="Group_3_Class">
                        <svg class="Ellipse_8_dc">
                        <ellipse fill="rgba(222,222,222,1)"
                                class="Ellipse_8_dc_Class" rx="19.5"
                                ry="19.5" cx="19.5" cy="19.5">
                        </ellipse>
                    </svg>
                        <svg class="Ellipse_10_dd">
                        <ellipse fill="rgba(240,240,240,1)"
                                class="Ellipse_10_dd_Class" rx="16.5"
                                ry="16.5" cx="16.5" cy="16.5">
                        </ellipse>
                    </svg>
                        <svg class="Ellipse_9_de">
                        <ellipse fill="rgba(255,255,255,1)"
                                class="Ellipse_9_de_Class" rx="13.5"
                                ry="13.5" cx="13.5" cy="13.5">
                        </ellipse>
                    </svg>
                        <i
                            class="fas fa-grip-vertical fa-lg moduleBarPosition"
                            onclick="openModuleNav()"></i>
                    </div>
                </div>
            </c:if>
            <div class="textDiv">
                <h3 class="spaceName">${space.name}
                    <c:if test="${not empty space.description}">
                        <i class="fas fa-info-circle fa-lg"
                            style="font-size: 20px; color: rgba(150, 45, 62, 1);"
                            onclick="openSpaceDescription()"></i>
                    </c:if>
                    <sec:authorize access="hasAnyRole('ADMIN','STAFF')">
            			<a href="<c:url value="/staff/space/${space.id}" />"><span data-feather="edit" style="display: inline-block; color: rgba(150, 45, 62, 1);"></span></a>
            		</sec:authorize>
                </h3>
				
            </div>
            <div id="space">
            <img style="max-width:${space.image.width}px; border-radius:13px; width: 100%;" id="bgImage" src="<c:url value="/api/image/${space.image.id}" />" />
            </div>
            </div>
            <c:if test="${not empty space.description}">
                <div id="rightContent">
                    <i class="far fa-times-circle fa-lg closebtn"
                        onclick="closeSpaceDescription()"></i>
                    <div class="spaceDescription">${space.description}</div>
                </div>
            </c:if>
            
        </div>
    </div>
</div>
<script>
$(function() {

});
</script>