<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<script src="<c:url value="/resources/extra/space.js" />" ></script>
<script>
$( document ).ready(function() {
	readyDoc($("#deleteSpace"), $('#confirm-space-delete'));
});
</script>

<h1>Spaces</h1>

<div style="padding-bottom: 20px;">This virtual exhibition
	contains the following spaces.</div>
<ul class="list-group list-group-flush">
	<c:forEach items="${spaces}" var="space">
		<li class="list-group-item"><a
			href="<c:url value="/staff/space/${space.id}" />"> <span
				data-feather="box"></span> ${space.name}
		</a> (Created on <span class="date">${space.creationDate}</span> by
			${space.createdBy}) 
           <a href="javascript:checkSpaceLinkPresent('${space.id}', '<c:url value="/staff/" />', '?${_csrf.parameterName}=${_csrf.token}',$('#headerSpaceValue'))" class="checkSpaceLinkPresent" >
               <span class="float-right checkSpaceLinkPresent" id="deleteSpace" data-feather="trash-2"></span>
           </a>
        </li>
	</c:forEach>
</ul>

<!--  Ashmi changes start -->

<div class="modal fade" id="confirm-space-delete" tabindex="-1" role="dialog"
    aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" data-spaceValue="" id="headerSpaceValue">
                <h5 class="modal-title" id="deleteModalTitle">
                    Confirm Deletion?
                </h5>
                <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                <p id = "spaceData">
                    Are you sure you want to delete ?
                </p>
                <div id="warningMessage">
                <small class="text-danger">
                Warning! Other spaces have links to this space!
                </small>
                </div>
                <div id="exhibitionMessage">
                <small class="text-danger">
                Warning! This space is exhibition start space!
                </small>
                </div>
                <div id="finalWarning">
                <small class="text-danger">
                Do you still want to delete?
                </small>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" id="closeButton" class="btn btn-default"
                    data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-danger btn-ok">Delete</button>
            </div>
        </div>
    </div>
</div>

<!--  Ashmi changes end -->
