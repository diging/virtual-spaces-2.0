<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<link rel="stylesheet" type="text/css" id="applicationStylesheet"
    href="<c:url value="/resources/extra/Home.css" />">
<script>
    $(document).ready(function() {

    });

    let maxWidth = '(max-width: 1200px)';
    function openNav() {
        if (window.matchMedia(maxWidth).matches) {
            document.getElementById("mySidenav").style.width = "170px";
            document.getElementById("mySidenav").style.height = "260px";
        } else {
            document.getElementById("mySidenav").style.width = "240px";
            document.getElementById("mySidenav").style.height = $
            {
                display.height
            }
            +"px";
        }
    }
    function closeNav() {
        document.getElementById("mySidenav").style.width = "0px";
    }
</script>
<div class="container-fluid">
    <div id="Module_1" class="Home_Class">
        <div class="dropdown">
            <div id="mySidenav" class="sidenav">
                <i class="far fa-times-circle fa-lg closebtn"
                    onclick="closeNav()"></i>
                <div class="list-group spaceNav">
                    <ul>
                        <c:forEach items="${publishedSpaces}"
                            var="space">
                            <li><a class="dropdown-item"
                                href="<c:url value="/exhibit/space/${space.id}" />">${space.name}</a>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <i class="fas fa-bars fa-lg barPosition" onclick="openNav()"></i>
        </div>
    </div>
    <div>
        <img
            src='<c:url value="/resources/images/404_Vspace-01.png"></c:url>'
            style="max-width: 1300px; width: 100%;">
    </div>
</div>