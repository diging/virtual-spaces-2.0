<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="t"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<h1>Exhibition Configuration</h1>

<div style="padding-bottom: 20px;">
	<c:url value="/staff/exhibit/config" var="postUrl" />
	<form:form method="POST"
		action="${postUrl}?${_csrf.parameterName}=${_csrf.token}">
		<input type="hidden" name="exhibitionParam" value="${exhibition.id}" />
		<label for="space">Select the start space of the exhibition:</label>
		<select class="form-control" name="spaceParam">
			<c:forEach items="${spacesList}" var="space">
				<option id=${space.id } value=${space.id }
					<c:if test="${space==exhibition.startSpace}">selected</c:if>>${space.name}</option>
			</c:forEach>
		</select>
		<div style="padding-top: 10px;">
			<label for="title">Exhibition Title:</label> <input type="text"
				class="form-control" name="title" value="${exhibition.title}" />
		</div>
		<div style="padding-top: 10px;">
			<label for="title">Exhibition Mode:</label> <select
				class="form-control" name="exhibitMode"
				onChange="modeChange(exhibitMode.value)">
				<c:forEach items="${exhibitionModes}" var="mode">
					<option id=${mode} value=${mode}
						<c:if test="${mode==exhibition.mode}">selected</c:if>>${mode}</option>
				</c:forEach>
			</select>
		</div>
		<div id="offlineMessage" style="padding-top: 10px;">
			<label for="title">Offline Message:</label> <input type="text"
				class="form-control" name="customMessage"
				value="${exhibition.customMessage}" placeholder ="This exhibition is currently offline. Please check back later."/>
		</div>
		<p style="padding-top: 10px;">
			<input class="btn btn-primary" type="submit" value="submit" />
		</p>

	</form:form>
</div>
<script>
$(document).ready(function() {
	var offlineMessage = $('#offlineMessage');
	if ('${exhibition}' != null && '${exhibition.mode}' == "OFFLINE") {
		offlineMessage.show();
	}
	else {
		offlineMessage.hide();
	}
});

	function modeChange(modeChosen) {
		var offlineMessage = $('#offlineMessage');
		if (modeChosen == "OFFLINE") {
			offlineMessage.show();
		}
		else {
			offlineMessage.hide();
		}
	}
</script>