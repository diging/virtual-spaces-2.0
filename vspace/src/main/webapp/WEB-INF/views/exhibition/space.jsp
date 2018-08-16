<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script>
//# sourceURL=click.js
$( document ).ready(function() {
	
	<c:forEach items="${spaceLinks}" var="link">
	{
		var posX = $("#bgImage").position().left
        var posY = $("#bgImage").position().top;
		var link = $('<a></a>');
		link.attr('href', '<c:url value="/exhibit/space/${link.link.targetSpace.id}" />');
		var icon = $('<span data-feather="navigation-2" class="flex"></span>');
	    icon.css('position', 'absolute');
	    icon.css('left', ${link.positionX});
	    icon.css('top', ${link.positionY} + posY);
	    icon.css('transform', 'rotate(${link.rotation}deg)');
	    icon.css('fill', 'red');
	    icon.css('color', 'red');
	    icon.css('font-size', "15px");
	    
	    link.append(icon);
	    $("#space").append(link);
	}
	</c:forEach>
	feather.replace();
});
</script>

<div class="row">
<div class="col-md-12 text-center">
<div id="space">
	<img id="bgImage" class="rounded" style="width: 800px" src="<c:url value="/api/image/${space.image.id}" />" >
</div>
</div>
</div>