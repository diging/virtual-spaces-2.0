<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>

<!doctype html>
<html lang="en" class="h-100">

<head>
<meta charset="utf-8">
<meta name="viewport"
    content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<title>Virtual Exhibition</title>

<link rel="stylesheet"
    href="https://use.fontawesome.com/releases/v5.7.2/css/all.css"
    integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr"
    crossorigin="anonymous">

<!-- Bootstrap core CSS -->
<link
    href="<c:url value="/resources/bootstrap-4.3.1-dist/css/bootstrap.min.css" />"
    rel="stylesheet">
<script src="<c:url value="/resources/jquery/jquery-3.3.1.min.js" />"></script>
<script src="<c:url value="/resources/jquery/jquery-ui.min.js" />"></script>
<script
    src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
    integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
    crossorigin="anonymous">
    
</script>
<script
    src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
    integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
    crossorigin="anonymous">
    
</script>

<style>
.bd-placeholder-img {
    font-size: 1.125rem;
    text-anchor: middle;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}

@media ( min-width : 768px) {
    .bd-placeholder-img-lg {
        font-size: 3.5rem;
    }
}

.push {
    height: 100px;
}

.pushContent {
    margin-top: 45px;
}
</style>
<!-- Custom styles for this template -->
<link
    href="<c:url value="/resources/bootstrap-4.3.1-dist/sticky-footer.css" />"
    rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Nanum+Gothic"
    rel="stylesheet">
<link
    href="https://fonts.googleapis.com/css?family=EB+Garamond|Montserrat"
    rel="stylesheet">

<script>
    $(document).ready(function() {

        $('#sidebarCollapse').on('click', function() {
            $('#sidebar').toggleClass('active');
        });

    });
</script>
</head>

<body class="d-flex flex-column h-100">
    <div class="wrapper">
        <nav class="navbar navbar-light" style="position: fixed; width: 100%;">
            <a class="navbar-brand" href="<c:url value="/" />"><c:if
                    test="${not empty exhibition.title}">
					${exhibition.title}</c:if> <c:if test="${empty exhibition.title}">Virtual Spaces</c:if></a>

            <ul class="navbar-nav ml-auto" style="flex-direction: row">
                <!-- <li class="nav-item"><a href="#"
                    id="sidebarCollapse" class="nav-link"> <i
                        class="fas fa-compass"></i>
                </a></li> -->
                <sec:authorize
                    access="isAuthenticated() and hasAnyRole('ADMIN', 'STAFF')">
                    <li class="nav-item"><a
                        href="<c:url value="/staff/dashboard" />"
                        class="nav-link"> <i
                            class="fas fa-tachometer-alt"></i>
                    </a></li>
                </sec:authorize>
                <sec:authorize access="isAnonymous()">
                    <li class="nav-item" style="padding-left: 20px;">
                        <a class="nav-link"
                        href="<c:url value="/login" />"><i
                            class="fas fa-sign-in-alt"></i></a>
                    </li>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <li class="nav-item" style="padding-left: 20px;">
                        <form action="<c:url value="/logout" />"
                            method="POST">
                            <input type="hidden"
                                name="${_csrf.parameterName}"
                                value="${_csrf.token}" />
                            <button class="btn navbar-link"
                                type="submit" title="Logout">
                                <i class="fas fa-sign-out-alt"></i>
                            </button>
                        </form>
                    </li>
                </sec:authorize>
            </ul>
        </nav>
        <!-- Sidebar -->
        <nav id="sidebar"
            class="mCustomScrollbar active float-left position-absolute">
            <div class="sidebar-header">
                <p>In this Virtual Space:</p>
            </div>

            <div class="list-group spaceNav">
                <c:forEach items="${publishedSpaces}" var="space">
                    <a
                        href="<c:url value="/exhibit/space/${space.id}" />"
                        class="list-group-item
						list-group-item-action"><i
                        class="fas fa-grip-horizontal"></i>
                        ${space.name}</a>
                </c:forEach>
            </div>
        </nav>
        <!-- Begin page content -->
        <main role="main" class="flex-shrink-0"
            style="padding-top: 20px;">
            <div class="pushContent"></div>
            <c:if
            test="${showAlert eq true}">
            <div id="errorMsg"
                class="alert alert-${alertType} custom-${messageType}">
                ${message}</div>
        </c:if> <tiles:insertAttribute name="content" /> 
        <div class="push"></div>
        </main>
    </div>

    <footer class="footer mt-auto" style="position: fixed; bottom: 0px; width: 100%;">
        <div class="d-flex justify-content-center">
             <span class="text-muted">This web application was
                built with <a class="text-muted" href="<c:url value="https://github.com/diging/virtual-spaces-2.0" />"> Virtual Spaces 2.0 </a></span>
        </div>
    </footer>

    <!-- Icons -->
    <script src="https://unpkg.com/feather-icons/dist/feather.min.js"></script>
    <script>
			feather.replace()
		</script>
</body>

</html>