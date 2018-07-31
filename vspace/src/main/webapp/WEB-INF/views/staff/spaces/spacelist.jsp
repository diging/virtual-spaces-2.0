<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script>
//# sourceURL=date.js
$( document ).ready(function() {
	var longDateFormat  = 'dd/MM/yyyy HH:mm:ss';

    jQuery(".date").each(function (idx, elem) {
        if (jQuery(elem).is(":input")) {
            jQuery(elem).val(DateFormat.format.date(jQuery(elem).val(), longDateFormat));
        } else {
            jQuery(elem).text(DateFormat.format.date(jQuery(elem).text(), longDateFormat));
        }
    });
});
</script>

<h1>Spaces</h1>

<div style="padding-bottom: 20px;">
This virtual exhibition contains the following spaces.

</div>

<ul class="list-group list-group-flush">
<c:forEach items="${spaces}" var="space">
	<li class="list-group-item">
		<a href="<c:url value="/staff/space/${space.id}" />">
		<span data-feather="box"></span> ${space.name}
		</a>
		
		(Created at <span class="date">${space.creationDate}</span> by ${space.createdBy})
	</li>
</c:forEach>
</ul>

<script src="<c:url value="/resources/extra/dateFormat.min.js" />" />