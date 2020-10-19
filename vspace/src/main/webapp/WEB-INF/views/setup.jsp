<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!-- Custom styles for this template -->
<link href="<c:url value="/resources/bootstrap-4.1.2/login.css" />"
	rel="stylesheet">

<script src="https://cdnjs.cloudflare.com/ajax/libs/pwstrength-bootstrap/2.2.1/pwstrength-bootstrap.min.js"></script>
<script>
$(document).ready(function () {
    "use strict";
    var options = {};
    options.ui = {
        bootstrap4: true,
        container: "#pwd-container",
        viewports: {
            progress: ".pwstrength_viewport_progress"
        },
        showVerdictsInsideProgressBar: true
    };
    options.common = {
        debug: true,
        onLoad: function () {
            $('#messages').text('Start typing password');
        }
    };
    $(':password').pwstrength(options);
});
</script>
<div class="row justify-content-center">
	<div class="col-8">
		<div class="jumbotron">
            <h2>Setup Virtual Spaces</h2>
			<p>It looks like this instance of Virtual Spaces has not been set up yet.</p>
            
            <p>Please choose an admin password:</p> 
            <c:url value="/setup/admin" var="postUrl" />
            <form action="${postUrl}" method="POST">
                <input type="hidden" name="${_csrf.parameterName}"
                            value="${_csrf.token}" />
                        
                <input type="password" class="form-control" name="adminpw" required />
                <div style="padding-top: 10px;">
                <div class="pwstrength_viewport_progress"></div>
                </div>
                <div class="text-right">
                <button type="submit" class="btn btn-md btn-primary">Set Password</button>
                </div>
            </form>
		</div>
	</div>
</div>

