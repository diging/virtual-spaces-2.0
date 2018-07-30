<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<script>
//# sourceURL=click.js
$( document ).ready(function() {
	$("#bgImage").on("click", function(e){
	    e.preventDefault();
	    console.log(e);
	    var icon = $('<span data-feather="arrow-up" class="flex"></span>');
	    icon.css('position', 'fixed');
	    
	    icon.css('left', e.pageX);
	    icon.css('top', e.pageY);
	    var storeX = e.pageX - $(this).offset().left;
	    var storeY = e.pageY - $(this).offset().top;
	    console.log(storeX);
	    console.log(storeY);
	    icon.css('color', 'red');
	    icon.css('font-size', "15px");
	    icon.css('transform', 'rotate(45deg)');
	    
	    $("#space").append(icon);
	    feather.replace();
	});
	
});
</script>

 <h1>Space: ${space.name}</h1> 

<c:if test="${not empty space.image}">
<div id="space">
<img id="bgImage" width="800px" src="<c:url value="/staff/api/image/${space.image.id}" />" />
</div>
</c:if>
