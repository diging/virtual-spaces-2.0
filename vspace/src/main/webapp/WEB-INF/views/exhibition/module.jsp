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
    function openLangNav() {
        document.getElementById("myLangSidenav").style.width = "240px";
    }
    function closeLangNav() {
        document.getElementById("myLangSidenav").style.width = "0";
    }
</script>
<!-- <div class="container-fluid"> -->
    <div id="Module_1" class="Module_1_Class">
        <c:if test="${showAlert != true}">
            <div class="textDiv">
                <h3>${module.name}</h3>
            </div>
            <div class="slideNumberClass">
                <p>Slide ${currentNumOfSlide}/${numOfSlides}</p>
            </div>
            <div class="Group_7_Class">
                <c:if test="${prevSlide !=  ''}">
                    <a
                        href="<c:url value="/exhibit/${spaceId}/module/${module.id}/sequence/${currentSequenceId}/slide/${prevSlide}" />">
                        <div
                            class="Slideshow_previous Slideshow_previous_Class">
                            <svg class="Ellipse_11">
                                    <ellipse fill="rgba(255,255,255,1)"
                                    class="Ellipse_11_Class" rx="20"
                                    ry="20" cx="20" cy="20">
                                    </ellipse>
                                </svg>
                            <svg class="Icon_ionic_ios_arrow_back"
                                viewBox="11.251 6.194 20.021 35.021">
                                    <path fill="rgba(101,101,101,1)"
                                    class="Icon_ionic_ios_arrow_back_Class"
                                    d="M 17.28610038757324 23.69940376281738 L 30.53693389892578 10.45899486541748 C 31.51693534851074 9.478996276855469 31.51693534851074 7.894316673278809 30.53693389892578 6.92474365234375 C 29.55693244934082 5.944746017456055 27.97225761413574 5.95517110824585 26.99225997924805 6.92474365234375 L 11.9795093536377 21.92706680297852 C 11.03078651428223 22.87578582763672 11.00993728637695 24.39791297912598 11.90652942657471 25.3779125213623 L 26.98183441162109 40.48448944091797 C 27.47183418273926 40.97449111938477 28.11821746826172 41.21427917480469 28.75417137145996 41.21427917480469 C 29.39012908935547 41.21427917480469 30.0365104675293 40.97449111938477 30.52651405334473 40.48448944091797 C 31.50650978088379 39.50448989868164 31.50650978088379 37.91981506347656 30.52651405334473 36.95023727416992 L 17.28610038757324 23.69940376281738 Z">
                                    </path>
                                </svg>
                        </div>
                    </a>
                </c:if>
                <c:if test="${nextSlide !=  ''}">
                    <a
                        href="<c:url value="/exhibit/${spaceId}/module/${module.id}/sequence/${currentSequenceId}/slide/${nextSlide}" />">
                        <div class="slideshow_next slideshow_next_Class">
                            <svg class="Ellipse_12">
                                    <ellipse fill="rgba(255,255,255,1)"
                                    class="Ellipse_12_Class" rx="20"
                                    ry="20" cx="20" cy="20">
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
            <a href="<c:url value="/exhibit/space/${spaceId}" />">
                <div class="exit_to_space_Class">
                    <svg class="Ellipse_5_be">
                            <ellipse fill="rgba(255,255,255,1)"
                            class="Ellipse_5_be_Class" rx="22" ry="22"
                            cx="22" cy="22">
                            </ellipse>
                        </svg>
                    <i class="fas fa-angle-double-left fa-2x Icon_awesome_angle_double_left"></i>
                </div>
            </a>
            <div class="Group_8_Class">
                <h3>${currentSlideCon.name}</h3>
                <c:forEach items="${currentSlideCon.contents}"
                    var="contents">
                    <c:if
                        test="${contents['class'].simpleName ==  'ImageBlock'}">
                        <img id="${contents.id}" class="imgDiv"
                            src="<c:url value="/api/image/${contents.image.id}" />" />
                    </c:if>
                    <c:if
                        test="${contents['class'].simpleName ==  'TextBlock'}">
                        <div id="${contents.id}" class="textDiv">${contents.text}</div>
                    </c:if>
                </c:forEach>
            </div>
        </c:if>
    </div>
<!-- </div> -->
