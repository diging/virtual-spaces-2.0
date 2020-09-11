<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<link rel="stylesheet" type="text/css" id="applicationStylesheet"
    href="<c:url value="/resources/extra/Home.css" />">
<script>
    $(document).ready(function() {

    });
</script>
<div class="container-fluid">

    <div style="text-align: center;">
        <h1
            style="color: rgba(0, 0, 0, .9); font-size: 24px; margin-top: 70px;">The
            page you were looking for could not be found.</h1>
        <img
            src='<c:url value="/resources/images/404_Vspace-01.png"></c:url>'
            style="max-width: 900px; width: 100%; text-align: center; object-fit: cover; height: auto; max-height: 800px; margin-top: 30px;">
    </div>
</div>