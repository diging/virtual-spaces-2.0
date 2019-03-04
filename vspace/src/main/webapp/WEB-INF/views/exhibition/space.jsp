<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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
	<c:forEach items="${spaceLinks}" var="link">
	{
		var posX = $("#space").offset().left + $("#space").position().left; // $("#space-container").offset().left;
        var posY = $("#space").position().top ;//+ parseInt($("main").css("padding-top"));
        console.log($("#space").css("margin-left"));
		var link = $('<a></a>');
		link.attr('href', '<c:url value="/exhibit/space/${link.link.targetSpace.id}" />');
		
		if ("${link.type}" == 'ALERT') {
			var linkDisplay = $('<div class="alert alert-primary" role="alert">');
		} else {
			var linkDisplay = $('<span data-feather="navigation-2" class="flex"></span>');
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
	}
	</c:forEach>
	feather.replace();
}
</script>

<div class="row">
<div id="space-container" class="col-md-12 text-center">
<div id="space" style="max-width: 800px; min-width:800px; max-height:600px; min-height: 500px;  margin: auto; background-size: cover; background-image:url('<c:url value="/api/image/${space.image.id}" />')" >
	<c:if test="${not empty space.description}">
	<a id="descriptionLink" href="#" data-toggle="collapse" data-target=".descriptionBox" class="nav-item float-left" style="margin-left: 0px; z-index: 99; position: relative"><i class="fas fa-info-circle"></i></a>
	<div class="descriptionBox collapse">
	${space.description}
	</div>
	</c:if>
</div>
</div>
</div>

<script>
$(function() {
	
});
</script>
