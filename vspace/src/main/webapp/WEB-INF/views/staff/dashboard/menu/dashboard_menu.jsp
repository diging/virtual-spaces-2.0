<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
  <span>Spaces</span>
  <a class="d-flex align-items-center text-muted" href="<c:url value="/staff/space/add" />">
    <span data-feather="plus-circle"></span>
  </a>
</h6>
 
<h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
  <span>Modules</span>
  <a class="d-flex align-items-center text-muted" href="<c:url value="/staff/module/add" />">
    <span data-feather="plus-circle"></span>
  </a>
</h6>