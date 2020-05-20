<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>

<link rel="stylesheet" type="text/css" id="applicationStylesheet"
    href="<c:url value="/resources/extra/Home.css" />">

<script>
//# sourceURL=click.js
function openNav(){
    document.getElementById("mySidenav").style.width = "240px";
    document.getElementById("mySidenav").style.height = ${display.height}+"px"
}
function closeNav(){
    document.getElementById("mySidenav").style.width = "0px";
}
function openModuleNav(){
    document.getElementById("mySideModulenav").style.width = "240px";
    document.getElementById("mySideModulenav").style.height = "auto";
}
function closeModuleNav(){
    document.getElementById("mySideModulenav").style.width = "0px";
}

function closeSpaceDescription(){
    document.getElementById("rightContent").style.width = "0px";
    document.getElementById("rightContent").style.height = "0px";
}

function openSpaceDescription(){
    document.getElementById("rightContent").style.width = "300px";
    document.getElementById("rightContent").style.height = ${display.height}+"px";
}

$( document ).ready(function() {
    
	$('#slide_sidebar').removeClass('active');
	$('#slide_sidebarCollapse').on('click', function() {
		$('#slide_sidebar').toggleClass('active');
	});
	
    drawLinks();
    
    $( window ).resize(function() {
        $("#space a").remove();
        drawLinks();
    });
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
            var linkDisplay=$('<div class="Info Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-walking fa-lg Icon_awesome_info_c"></i><span class="tooltiptext">${link.link.name}</span></div>');
        }
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('left', ${link.positionX} + posX);
        linkDisplay.css('top', ${link.positionY} + posY);
        linkDisplay.css('transform', 'rotate(${link.rotation}deg)');
        linkDisplay.css('fill', 'grey'); 
        linkDisplay.css('color', 'rgba(128,128,128,1)');
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
        var linkDisplay = $('<div class="Info Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-info fa-lg Icon_awesome_info_m"></i><span class="tooltiptext">${link.link.name}</span></div>');
     
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('left', ${link.positionX} + posX);
        linkDisplay.css('top', ${link.positionY} + posY);
        linkDisplay.css('transform', 'rotate(${link.rotation}deg)');
        linkDisplay.css('fill', 'grey');
        linkDisplay.css('color', 'rgba(128,128,128,1)');
         
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
            console.log("In else block");
            var linkDisplay = $('<div class="Info Info_cz_Class"><svg class="Ellipse_8_c"><ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_c_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5"></ellipse></svg><svg class="Ellipse_10_c"><ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_c_Class" rx="12.5" ry="12.5" cx="12.5" cy="12.5"></ellipse></svg><svg class="Ellipse_9_c"><ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_c_Class" rx="10.5" ry="10.5" cx="10.5" cy="10.5"></ellipse></svg><i class="fas fa-external-link-alt fa-lg Icon_awesome_info_e"></i><span class="tooltiptext">${link.name}</span></div>');
        }
        
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('left', ${link.positionX} + posX);
        linkDisplay.css('top', ${link.positionY} + posY);
        linkDisplay.css('transform', 'rotate(${link.rotation}deg)');
        linkDisplay.css('fill', 'grey');
        linkDisplay.css('color', 'rgba(128,128,128,1)');
         
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
    background: rgba(0,0,0,0.6);
}

.Info_cz_Class:hover .tooltiptext {
    visibility: visible;
}
</style>
<div class="container-fluid">
    <div id="Module_1" class="Home_Class">
        <!-- <div class="leftContent"> -->
        <div class="dropdown">
            <div id="mySidenav" class="sidenav">
                <i class="far fa-times-circle fa-lg closebtn"
                    onclick="closeNav()"></i>
                <!-- <div class="sidebar-header">
                    <p>In this Virtual Space</p>
                </div> -->
                <div class="list-group spaceNav">
                    <ul>
                        <c:forEach items="${allSpaces}" var="space">
                            <li><a class="dropdown-item"
                                href="<c:url value="/exhibit/space/${space.id}" />">${space.name}</a>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <i class="fas fa-bars fa-lg barPosition" onclick="openNav()"></i>
        </div>
        <div class="spaceClass"
            style="width: ${display.width}px; height: ${display.height}px; min-height: 780px;  margin: auto; background-size: cover; border-radius: 13px;">
            <c:if test="${not empty moduleList}">
                <div class=dropdown>
                    <div id="mySideModulenav" class="sideModulenav">
                        <i class="far fa-times-circle fa-lg closebtn"
                            onclick="closeModuleNav()"></i>
                        <div class="list-group spaceNav">
                            <ul style="list-style-type: none">
                                <c:forEach items="${moduleList}"
                                    var="module">
                                    <li><a class="dropdown-item"
                                        href="<c:url value="/exhibit/${space.id}/module/${module.link.module.id}" />">${module.link.name}</a>
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
                <h3>${space.name}
                    <c:if test="${not empty space.description}">
                        <i class="fas fa-info-circle fa-lg"
                            style="font-size: 20px; color: rgba(150, 45, 62, 1);"
                            onclick="openSpaceDescription()"></i>
                    </c:if>
                </h3>
            </div>

            <div id="space"
                style="width: ${display.width}px; height: ${display.height}px; min-height: 500px;  margin: auto; background-size: cover; border-radius: 13px; background-image:url('<c:url value="/api/image/${space.image.id}" />')">
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
