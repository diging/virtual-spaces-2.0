<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h2>Users</h2>
<ul class="list-group">
<c:forEach items="${users}" var="user">
    <li class="list-group-item <c:if test="${not user.enabled}">list-group-item-secondary</c:if> clearfix">
        <c:if test="${not user.enabled}"><span class="badge badge-light">Account Deactivated</span></c:if>
        <c:if test="${user.admin}"><span class="badge badge-warning">Admin User</span></c:if>
        <c:if test="${user.enabled}"><span class="badge badge-info">Active</span></c:if>
        <c:if test="${fn:contains(user.roles, 'STAFF')}"><span class="badge badge-primary">Staff</span></c:if>
        ${user.username} (${user.firstName} ${user.lastName})
        <div class="pull-right text-right">
        <c:if test="${not user.enabled}">
            <c:url value="/staff/user/${user.username}/approve" var="approveUrl"/>
            <form:form action="${approveUrl}" method="POST">
            <button style="padding:0px" class="btn btn-link"><span data-feather="user-check"></span> Activate</button>
            </form:form>
        </c:if>
        
        <c:if test="${user.enabled}">
            <c:if test="${not user.admin}">
	            <c:url value="/staff/user/${user.username}/admin" var="postUrl"/>
	            <form:form action="${postUrl}" method="POST">
	            <button style="padding:0px" class="btn btn-link"><span data-feather="key"></span> Make Admin</button>
	            </form:form>
            </c:if>
            <c:if test="${user.admin}">
	            <c:url value="/staff/user/${user.username}/admin/remove" var="removeUrl"/>
	            <form:form action="${removeUrl}" method="POST">
	            <button style="padding:0px" class="btn btn-link"><span data-feather="user"></span> Remove Admin Role</button>
	            </form:form>
            </c:if>
            
            <c:if test="${not fn:contains(user.roles, 'STAFF')}">
		        <c:url value="/staff/user/${user.username}/role/add?roles=ROLE_STAFF" var="url"/>
	            <form:form action="${url}" method="POST">
	            <button style="padding:0px" class="btn btn-link"><span data-feather="user"></span> Make Staff</button>
	            </form:form>
	        </c:if>
            <c:if test="${fn:contains(user.roles, 'STAFF')}">
	            <c:url value="/staff/user/${user.username}/role/remove?roles=ROLE_STAFF" var="url"/>
		        <form:form action="${url}" method="POST">
	            <button style="padding:0px" class="btn btn-link"><span data-feather="user"></span> Remove Staff Role</button>
	            </form:form>
	        </c:if>
        
            <c:url value="/staff/user/${user.username}/disable" var="disableUrl"/>
            <form:form action="${disableUrl}" method="POST">
            <button style="padding:0px" class="btn btn-link"><span data-feather="user-x"></span> Deactivate User</button>
            </form:form>
        </c:if>
        </div>
    </li>
</c:forEach>
</ul>