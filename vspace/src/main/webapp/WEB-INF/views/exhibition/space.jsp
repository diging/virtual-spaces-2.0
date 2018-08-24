<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

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
		var icon = $('<span data-feather="navigation-2" class="flex"></span>');
	    icon.css('position', 'absolute');
	    icon.css('left', ${link.positionX} + posX);
	    icon.css('top', ${link.positionY} + posY);
	    icon.css('transform', 'rotate(${link.rotation}deg)');
	    icon.css('fill', 'red');
	    icon.css('color', 'red');
	    icon.css('font-size', "15px");
	    icon.css('width', 16);
	    icon.css('height', 16);
	    
	    link.append(icon);
	    $("#space").append(link);
	}
	</c:forEach>
	feather.replace();
});
</script>

<div class="row">
	<div id="space-container" class="col-md-12 text-center">
		<div id="space"
			style="width: 800px; height: 600px; margin: auto; background-size: cover; background-image:url('<c:url value="/api/image/${space.image.id}" />')">

		</div>
	</div>
</div>