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
body {
    font-family: 'Montserrat', sans-serif;
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
			<c:if test="${nextSlide !=  ''}">
                <a
                    href="<c:url value="/exhibit/${spaceId}/module/${module.id}/sequence/${currentSequenceId}/slide/${nextSlide}" />">
                    <div style="position: sticky; top: 30%; right: 106%; z-index: 5;" class="slideshow_next slideshow_next_Class">
                        <svg class="Ellipse_12">
                                    <ellipse fill="rgba(255,255,255,1)"
                                class="Ellipse_12_Class" rx="20" ry="20"
                                cx="20" cy="20">
                                    </ellipse>
                                </svg>
                        <svg class="Icon_ionic_ios_arrow_forward"
                            viewBox="11.246 6.196 20.021 35.017">
                                    <path fill="rgba(101,101,101,1)"
                                class="Icon_ionic_ios_arrow_forward_Class"
                                d="M 25.23231887817383 23.69813346862793 L 11.98148345947266 10.45771980285645 C 11.00148582458496 9.47772216796875 11.00148582458496 7.893041610717773 11.98148345947266 6.923468589782715 C 12.96148300170898 5.953895568847656 14.54616165161133 5.953895092010498 15.52616119384766 6.923468589782715 L 30.53891181945801 21.92579460144043 C 31.48763275146484 22.8745174407959 31.50848388671875 24.39664649963379 30.61189270019531 25.37664222717285 L 15.53658676147461 40.48322677612305 C 15.04658699035645 40.97322463989258 14.40020561218262 41.2130126953125 13.76424789428711 41.2130126953125 C 13.1282901763916 41.2130126953125 12.48190879821777 40.97322463989258 11.99190902709961 40.48322677612305 C 11.0119104385376 39.50322723388672 11.0119104385376 37.91854858398438 11.99190902709961 36.948974609375 L 25.23231887817383 23.69813346862793 Z">
                                    </path>
                                </svg>
                    </div>
                </a>
        </c:if>
		</div>
        
        
        <a  href="<c:url value="/exhibit/space/${spaceId}" />">
            <div style="position: sticky; z-index: 100;" class="exit_to_space_Class">
                <svg class="Ellipse_5_be">
                    <ellipse fill="rgba(255,255,255,1)"
						class="Ellipse_5_be_Class" rx="22" ry="22" cx="22" cy="22">
                    </ellipse>
                </svg>
                <i class="fas fa-angle-double-left fa-2x Icon_awesome_angle_double_left"></i>
                <span style="z-index: 100;" class="tooltiptext">Go To Space</span>
            </div>
        </a>
        <c:if test="${prevSlide !=  ''}">
                <a
                    href="<c:url value="/exhibit/${spaceId}/module/${module.id}/sequence/${currentSequenceId}/slide/${prevSlide}" />">
                    <div style="position: sticky; z-index: 90; top: 30%; left: 4%;" class="Slideshow_previous Slideshow_previous_Class">
                        <svg class="Ellipse_11">
                                    <ellipse fill="rgba(255,255,255,1)"
                                class="Ellipse_11_Class" rx="20" ry="20" cx="20" cy="20">
                                    </ellipse>
                                </svg>
                                <i class="fas fa-angle-left fa-2x Icon_awesome_angle_double_left"></i>
                    </div>
                </a>
        </c:if>
        <a
            href="<c:url value="/exhibit/${spaceId}/module/${module.id}/sequence/${startSequenceId}?clearHistory=true" />">
            <div style="position: sticky; z-index: 80;" class="exit_to_branchingPoint_Class">
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
            <div style="position: sticky; z-index: 70;" class="exit_to_previousChoice_Class">
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
