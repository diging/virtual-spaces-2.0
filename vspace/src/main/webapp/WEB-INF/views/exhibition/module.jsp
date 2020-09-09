<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="t"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<link
    href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />"
    rel="stylesheet">

<script
    src="<c:url value="/resources/bootstrap-4.3.1-dist/js/bootstrap.min.js" />"></script>

<link id="applicationStylesheet" rel="stylesheet"
    href="<c:url value="/resources/extra/Module_1.css" />">

<link rel="stylesheet" type="text/css" id="applicationStylesheet"
    href="<c:url value="/resources/extra/Module_1___slideshow___1.css" />">

<style type="text/css">

.btn-floating {
     box-shadow: 3px 3px 10px #ccc !important;
     background-color: white;
}
​
.btn-floating:hover {
     box-shadow: 3px 3px 10px #aaa !important;
     background-color: #eee;
}
​
.fixed-action-btn {
	 position: fixed;
	 right: 1px;
	 bottom: 0px;
	 padding-top: 0px;
	 margin-bottom: 0;
	 z-index: 997;
	 
body {
    font-family: 'Montserrat', sans-serif;
}

}
</style>
<script>
$( document ).ready(function() {
	$("#blocks a").css('color','rgb(150, 45, 62)').css('font-weight','bold');
});
    function openLangNav() {
        document.getElementById("myLangSidenav").style.width = "240px";
    }
    function closeLangNav() {
        document.getElementById("myLangSidenav").style.width = "0";
    }
    function openNav(){
        document.getElementById("mySidenav").style.width = "170px";
        document.getElementById("mySidenav").style.height = "700px";
    }
    function closeNav(){
        document.getElementById("mySidenav").style.width = "0px";
    }
</script> 
<div id="Module_1" class="Module_1_Class">
    <c:if test="${showAlert != true}">
    	<div style="position: fixed; z-index: 100; top: 50px; width: 100%">
			<h3 class="textDiv" style="background-color: white;">${module.name}</h3>
			<p class="slideNumberClass" style="position: fixed; left: 1%;">Slide
				${currentNumOfSlide}/${numOfSlides}</p>
    	</div>
        <div class="Group_7_Class" style="position: sticky; top: 250px;">
            <c:if test="${prevSlide !=  ''}">
                <a class="btn-floating btn-large" href="<c:url value="/exhibit/${spaceId}/module/${module.id}/sequence/${currentSequenceId}/slide/${prevSlide}" />">
                    <div class="fixed-action-btn">
                    	<i class="fa fa-chevron-left" style="color: black; left: 4%; top: 30%; height: 30px; width: 30px; text-align: center" aria-hidden="true"></i>
          			</div>
          		</a>
            </c:if>
            <c:if test="${nextSlide !=  ''}">
                <a
                    href="<c:url value="/exhibit/${spaceId}/module/${module.id}/sequence/${currentSequenceId}/slide/${nextSlide}" />">
                    <div class="fixed-action-btn">
                    	<i class="fa fa-chevron-right" style="color: black; top: 30%; height: 30px; width: 30px;  text-align: center" aria-hidden="true"></i>
          			</div>
                </a>
            </c:if>
        </div>
        <div>
        <a  href="<c:url value="/exhibit/space/${spaceId}" />">
            <div style="position: fixed; z-index: 100; top: 20%" class="exit_to_space_Class">
                <svg class="Ellipse_5_be">
                    <ellipse fill="rgba(255,255,255,1)"
						class="Ellipse_5_be_Class" rx="22" ry="22" cx="22" cy="22">
                    </ellipse>
                </svg>
                <i class="fas fa-angle-double-left fa-2x Icon_awesome_angle_double_left"></i>
                <span style="z-index: 100;" class="tooltiptext">Go To Space</span>
            </div>
        </a>
        <a
            href="<c:url value="/exhibit/${spaceId}/module/${module.id}/sequence/${startSequenceId}?clearHistory=true" />">
            <div style="position: fixed; z-index: 80;" class="exit_to_branchingPoint_Class">
                <svg class="Ellipse_5">
                    <ellipse fill="rgba(255,255,255,1)"
                        class="Ellipse_5_Class" rx="22" ry="22" cx="22"
                        cy="22">
                    </ellipse>
                </svg>
                <i class="fas fa-fast-backward fa-2x Icon_awesome_angle_double_left"></i>
                <span style="z-index: 80;" class="tooltiptext">Go To Start Sequence of Module</span>
            </div>
        </a>
        <c:if test="${showBackToPreviousChoice eq true}">
        <a href="<c:url value="/exhibit/${spaceId}/module/${module.id}/sequence/${previousSequenceId}/slide/${previousBranchingPoint.id}?back=true" />">
            <div style="position: fixed; z-index: 70;" class="exit_to_previousChoice_Class">
                <svg class="Ellipse_5_be">
                    <ellipse fill="rgba(255,255,255,1)"
                        class="Ellipse_5_be_Class" rx="22" ry="22"
                        cx="22" cy="22">
                    </ellipse>
                </svg>
                <i class="fas fa-step-backward fa-2x Icon_awesome_angle_double_left"></i>
                <span style="z-index: 70;" class="tooltiptext">Go back to ${previousBranchingPoint.name}</span>
            </div>
        </a>
        </c:if>
        </div>
        <div id="blocks" class="Group_8_Class">
            <h3>${currentSlideCon.name}</h3>
            <c:forEach items="${currentSlideCon.contents}"
                var="contents">
                <c:if
                    test="${contents['class'].simpleName ==  'ImageBlock'}">
                    <img id="${contents.id}" class="imgDiv"
                        src="<c:url value="/api/image/${contents.image.id}" />" />
                </c:if>
                <c:if test="${contents['class'].simpleName ==  'TextBlock'}">
                	<div id="${contents.id}" class="textDiv">${contents.htmlRenderedText()}</div>
                </c:if>
                <c:if
                    test="${contents['class'].simpleName == 'ChoiceBlock'}">
                    <div id="${contents.id}"
                        class="textDiv"
                        style="margin: 1%;">
                        <c:if test="${contents.showsAll eq true}">
                            <div class="list-group">
                                <c:forEach items="${choices}"
                                    var="choice">
                                    <a
                                        href="<c:url value="/exhibit/${spaceId}/module/${module.id}/sequence/${choice.sequence.id}?branchingPoint=${slideId}&previousSequenceId=${currentSequenceId}" />"
                                        class="list-group-item list-group-item-action">${choice.sequence.name}</a>
                                </c:forEach>
                            </div>
                        </c:if>
                        <c:if test="${contents.showsAll eq false}">
                            <div class="list-group">
                                <c:forEach items="${contents.choices}"
                                    var="choice">
                                    <a
                                        href="<c:url value="/exhibit/${spaceId}/module/${module.id}/sequence/${choice.sequence.id}?branchingPoint=${slideId}&previousSequenceId=${currentSequenceId}" />"
                                        class="list-group-item list-group-item-action">${choice.sequence.name}</a>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </c:if>
</div>
