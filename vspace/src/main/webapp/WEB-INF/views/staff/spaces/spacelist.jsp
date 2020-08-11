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

.unpublishedtooltiptext {
    display: none;
    color: #bfb168; 
    font-weight: bold;
    font-size: 0.875rem;
    font-family: 'Quicksand';
    float: left;
}

.unpublishedSpaceClass:hover + .unpublishedtooltiptext{
    display: block;
}

.noIncomingLinkMessage {
    display: none;
    color: #bfb168; 
    font-weight: bold;
    font-size: 0.875rem;
    font-family: 'Quicksand';
    float: left;
}

.noIncomingLinks:hover + .noIncomingLinkMessage{
    display: block;
}
</style>

<h1>Spaces</h1>

<div style="padding-bottom: 20px;">
    This virtual exhibition contains the following spaces. <i
        class="fa fa-play-circle" aria-hidden="true"
        style="color: #bfb168;"></i> indicates the start space.
</div>
<ul class="list-group list-group-flush">
    <c:forEach items="${spaces}" var="space">
        <li class="list-group-item"><c:if
                test="${space.id == startSpace.id}">
                <i class="fa fa-play-circle" aria-hidden="true"
                    style="color: #bfb168;"></i>
            </c:if> <a href="<c:url value="/staff/space/${space.id}" />"> <c:if
                    test="${space.id != startSpace.id}">
                    <span data-feather="box"></span>
                </c:if> ${space.name}
        </a> (Created on <span class="date">${space.creationDate}</span> by
            ${space.createdBy}) <div class="float-right"><c:if
                test="${space.spaceStatus=='UNPUBLISHED'}">
                <i
                    class="fas fa-exclamation-triangle unpublishedSpaceClass"
                    aria-hidden="true" style="color: #bfb168; padding-right: 8px;"></i><span
                    class="unpublishedtooltiptext">This space is
                        currently unpublished.</span>
            </c:if>  <c:if
                test="${space.hasIncomingLinks == 'true'}">
                    <i class="fas fa-eye-slash noIncomingLinks" aria-hidden="true" 
                    style="color: #bfb168; padding-right: 8px;"></i><span
                    class="noIncomingLinkMessage">This space has no incoming links.</span>
            </c:if><a
            href="javascript:checkSpaceLinkPresent('${space.id}', '<c:url value="/staff/" />', '?${_csrf.parameterName}=${_csrf.token}',$('#headerSpaceValue'))"
            class="checkSpaceLinkPresent"> <span
                class="checkSpaceLinkPresent"
                id="deleteSpace" data-feather="trash-2" style="color: #666;"></span>
        </a></div></li>
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
