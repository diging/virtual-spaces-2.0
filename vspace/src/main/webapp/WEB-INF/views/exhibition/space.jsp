<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script>
//# sourceURL=click.js
$( document ).ready(function() {
	
	<c:forEach items="${spaceLinks}" var="link">
	{
		var posX = $("#space").offset().left - $("#space-container").offset().left;
        var posY = $("#space").position().top;
		console.log("x " + ${link.positionX})
		console.log("offset " + $("#space").offset().left)
		console.log("container: " + $("#space-container").offset().left)
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
});
</script>

<div class="row">
<div id="space-container" class="col-md-12 text-center">
<div id="space" style="width: 800px; height: 600px; margin: auto; background-size: cover; background-image:url('<c:url value="/api/image/${space.image.id}" />')" >
	
</div>
</div>
</div>