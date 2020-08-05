<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>

<script src="<c:url value="/resources/extra/space.js" />"></script>
<script>
    $(document).ready(function() {
        onPageReady($("#deleteSpace"), $('#confirm-space-delete'));
    });
</script>

<style>
.startSpaceFlagClass .tooltiptext {
    visibility: hidden;
    width: 240px;
    background: rgba(0, 0, 0, 0.5);
    font-size: 15px;
    color: white;
    text-align: center;
    border-radius: 6px;
    position: absolute;
    z-index: 1;
    left: -19px;
    top: 33px;
    background: rgba(0, 0, 0, 0.5);
}
.startSpaceFlagClass:hover .tooltiptext {
    visibility: visible;
}
</style>

<h1>Spaces</h1>

<div style="padding-bottom: 20px;">This virtual exhibition
    contains the following spaces.</div>
<ul class="list-group list-group-flush">
    <c:forEach items="${spaces}" var="space">
        <li class="list-group-item"><c:if
                test="${space.id == startSpace.id}">
                <i class="fa fa-flag fa-sm startSpaceFlagClass"
                    aria-hidden="true" style="color: #bfb168;"><span
                    class="tooltiptext">This space is the start
                        space of the exhibition.</span></i>
            </c:if><a href="<c:url value="/staff/space/${space.id}" />"> <span
                data-feather="box"></span> ${space.name}
        </a> (Created on <span class="date">${space.creationDate}</span> by
            ${space.createdBy}) <a
            href="javascript:checkSpaceLinkPresent('${space.id}', '<c:url value="/staff/" />', '?${_csrf.parameterName}=${_csrf.token}',$('#headerSpaceValue'))"
            class="checkSpaceLinkPresent"> <span
                class="float-right checkSpaceLinkPresent"
                id="deleteSpace" data-feather="trash-2"></span>
        </a></li>
    </c:forEach>
</ul>

<!--  Ashmi changes start -->

<div class="modal fade" id="confirm-space-delete" tabindex="-1"
    role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" data-spaceValue=""
                id="headerSpaceValue">
                <h5 class="modal-title" id="deleteModalTitle">
                    Confirm Deletion?</h5>
                <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                <p id="spaceData">Are you sure you want to delete ?
                </p>
                <div id="warningMessage">
                    <div class="text-danger">Other spaces have
                        links to this space!</div>
                </div>
                <div id="exhibitionMessage">
                    <div class="text-danger">This space is the
                        start of the exhibition. Deleting it will make
                        your exhibition unavailable.</div>
                </div>
                <div id="finalWarning">
                    <div class="text-danger">Do you want to
                        continue?</div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" id="closeButton"
                    class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-danger btn-ok">Yes,
                    delete!</button>
            </div>
        </div>
    </div>
</div>

<!--  Ashmi changes end -->
