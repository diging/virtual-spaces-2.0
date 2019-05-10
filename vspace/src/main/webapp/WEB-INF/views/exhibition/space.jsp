<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>

<script>
//# sourceURL=click.js
$( document ).ready(function() {
    
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
            var linkDisplay = $('<span data-feather="navigation-2" class="flex"></span><p class="label-${loop.index}" data-link-id="${link.link.id}">${link.link.name}</p>');
        }
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('left', ${link.positionX} + posX);
        linkDisplay.css('top', ${link.positionY} + posY);
        linkDisplay.css('transform', 'rotate(${link.rotation}deg)');
        linkDisplay.css('fill', 'red'); 
        linkDisplay.css('color', 'red');
        linkDisplay.css('font-size', "15px");	
         
        link.append(linkDisplay);
        $("#space").append(link);
        
        $(".label-${loop.index}").css({
			'transform': 'rotate(0deg)',
			'left': ${link.positionX} + posX - 10,
			'top': ${link.positionY} + posY + 16,
			'color': 'red'
        });	
    }
    </c:forEach>
    feather.replace();
}
</script>

<div class="row">
    <div
        class="<c:if test="${not empty space.description}">col-md-1</c:if><c:if test="${empty space.description}">col-md-2</c:if>"></div>
    <div id="space-container" class="col-md-8 text-center">
        <div id="space"
            style="width: ${display.width}px; height: ${display.height}px; min-height: 500px;  margin: auto; background-size: cover; background-image:url('<c:url value="/api/image/${space.image.id}" />')">
        </div>
    </div>
    <div
        class="<c:if test="${not empty space.description}">col-md-3</c:if><c:if test="${empty space.description}">col-md-2</c:if>">
        <c:if test="${not empty space.description}">
            <div class="descriptionBox"
                style="height: ${display.height - 20}px; overflow-y: scroll;">
                <h5>${space.name}</h5>
                ${space.description}
            </div>
        </c:if>
    </div>
</div>

<script>
$(function() {
    
});
</script>
