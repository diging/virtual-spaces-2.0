<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<link rel="stylesheet" type="text/css" id="applicationStylesheet"
	href="<c:url value="/resources/extra/Home.css" />">

<script>
//# sourceURL=click.js
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
        	var linkDisplay=$('<i class="fas fa-walking fa-lg"></i><p class="label-${loop.index}" data-link-id="${link.link.id}">${link.link.name}</p>');
        }
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('left', ${link.positionX} + posX);
        linkDisplay.css('top', ${link.positionY} + posY);
        linkDisplay.css('transform', 'rotate(${link.rotation}deg)');
        linkDisplay.css('fill', 'red'); 
        linkDisplay.css('color', 'red');
        link.append(linkDisplay);
        $("#space").append(link);
        $(".label-${loop.index}").css({
          'transform': 'rotate(0deg)',
          'left': ${link.positionX} + posX - 10,
          'top': ${link.positionY} + posY + 16,
          'color': 'red',
          'font-size': '12px'
        });	
    }
    </c:forEach>
    
    <c:forEach items="${moduleList}" var="link" varStatus="moduleLoop">
    {
        var posX = parseInt($("#space").css('margin-left')) + $("#space").position().left; 
        var posY = $("#space").position().top;
        var link = $('<a></a>');
        link.attr('href', '<c:url value="/exhibit/module/${link.link.module.id}" />');
        var linkDisplay = $('<i class="fas fa-info-circle fa-lg"></i><p class="moduleLabel-${moduleLoop.index}" data-link-id="${link.link.id}">${link.link.name}</p>');
     
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('left', ${link.positionX} + posX);
        linkDisplay.css('top', ${link.positionY} + posY);
        linkDisplay.css('transform', 'rotate(${link.rotation}deg)');
        linkDisplay.css('fill', 'red');
        linkDisplay.css('color', 'red');
         
        link.append(linkDisplay);
        $("#space").append(link);
        
        $(".moduleLabel-${moduleLoop.index}").css({
            'transform': 'rotate(0deg)',
            'left': ${link.positionX} + posX - 10,
            'top': ${link.positionY} + posY + 16,
            'color': 'red',
            'font-size': '12px'
          });
    }
    </c:forEach>

    <c:forEach items="${externalLinkList}" var="link">
    {
        var posX = parseInt($("#space").css('margin-left')) + $("#space").position().left; 
        var posY = $("#space").position().top;
        var link = $('<a></a>');
        link.attr('href', "${link.externalLink.externalLink}");
        link.attr('target', "_blank");
        
        var linkDisplay = $('<span class="fa fa-globe"></span>');
       
        if ("${link.type}" == 'IMAGE' && "${link.image}" != '') {
            var linkDisplay = $('<img id="${link.image.id}" src="<c:url value="/api/image/${link.image.id}" />" />');
        } else {
            var linkDisplay = $('<span class="fa fa-globe"></span>');
        }
        
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('left', ${link.positionX} + posX);
        linkDisplay.css('top', ${link.positionY} + posY);
        linkDisplay.css('fill', 'red');
        linkDisplay.css('color', 'red');
        linkDisplay.css('font-size', "15px");
         
        link.append(linkDisplay);
        $("#space").append(link);
    }
    </c:forEach>
    
    feather.replace();
}
</script>
<div class="row">
	<div id="wrapper" class="toggled col-md-1">
		<nav id="slide_sidebar">
			<div class="slide_sidebar-header">
				<h3>MODULES</h3>
			</div>
			<ul class="list-unstyled components">
				<li class="active"><c:forEach items="${moduleList}" var="link">
						<li><a href="/vspace/exhibit/module/${link.link.module.id}">${link.link.module.name}</a></li>
					</c:forEach></li>
			</ul>
		</nav>
		<div id="slide_content">
			<nav class="slide_navbar navbar-default">
				<div class="container-fluid">
					<div class="navbar-header">
						<button type="button" id="slide_sidebarCollapse" class="btn">
							<i class="fas fa-align-justify"></i>
						</button>
					</div>
				</div>
			</nav>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-2 Home Home__Class">
		<div>${space.name}</div>

	</div>
</div>
<div class="row">
	<div class="col-md-2 Home__Image__Class">
		<div id="space"
			style="width: ${display.width}px; height: ${display.height}px; min-height: 500px;  margin: auto; background-size: cover; background-image:url('<c:url value="/api/image/${space.image.id}" />')">
		</div>
	</div>
</div>
