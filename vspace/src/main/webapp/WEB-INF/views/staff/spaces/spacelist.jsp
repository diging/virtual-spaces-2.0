<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<script>
//# sourceURL=click.js

function checkSpaceLinkPresent(spaceId){  
    
    var urlToLoadOnSuccess = "<c:url value="/staff/space/list"/>";
    var urlToLoadOnError = "<c:url value="/staff/space/list"/>";
    var dataUrl = "<c:url value="/staff/space/" />" + spaceId + '?${_csrf.parameterName}=${_csrf.token}';
    var warningMessage = "Warning! Other spaces have links to this space! Do you still want to delete?";  

    $.ajax({
        url: "<c:url value="/staff/spaceLink/" />" + spaceId + "/spaces" + '?${_csrf.parameterName}=${_csrf.token}',
        type: 'GET',
        cache       : false,
        contentType : false,
        success: function(data) {
            
            if(data.length > 0){
              
               $("#headerSpaceValue").data('spaceValue', spaceIdGlobal);
               $('#deleteSpace').attr('data-toggle', 'modal');
               $('#deleteSpace').attr('data-target', "#confirm-space-delete");
               $('#deleteSpace').attr('data-url', dataUrl);
               $('#deleteSpace').attr('data-record-id', spaceId);
               $('#deleteSpace').attr('data-call-on-success', urlToLoadOnSuccess);
               $('#deleteSpace').attr('data-call-on-error', urlToLoadOnError);
               $('#warning').text(warningMessage);
               $('#deleteSpace').attr('data-warning', warningMessage);
               $('#confirm-space-delete').modal('show');
            }
            else{
                
                $('#deleteSpace').attr('data-toggle', 'modal');
                $('#deleteSpace').attr('data-target', "#confirm-space-delete");
                $('#deleteSpace').attr('data-url', dataUrl);
                $('#deleteSpace').attr('data-record-id', spaceId);
                $('#deleteSpace').attr('data-call-on-success', urlToLoadOnSuccess);
                $('#deleteSpace').attr('data-call-on-error', urlToLoadOnError);
                $('#deleteSpace').attr('data-warning', '');
                $('#warning').text('');
                $('#confirm-space-delete').modal('show');
              } 
        }
    });
}


$( document ).ready(function() {
    
    
 // STORY-49 Ashmi Changes start
    $('#confirm-space-delete').on('click', '.btn-ok', function(e) {
        var url = $("#deleteSpace").data('url');
        var urlToLoadOnSuccess = $("#deleteSpace").data('call-on-success');
        var urlToLoadOnError = $("#deleteSpace").data('call-on-error');
        $.ajax({
            url : url,
            type : 'DELETE',
            success : function(response) {
                window.location.href = urlToLoadOnSuccess;
                },
            error : function(errorMessage) {
                window.location.href = urlToLoadOnError+"?showAlert=true&alertType=danger&message="+errorMessage.responseText;
                }
            });
  }); 
 
});

</script>



<h1>Spaces</h1>

<div style="padding-bottom: 20px;">This virtual exhibition
	contains the following spaces.</div>
<ul class="list-group list-group-flush">
	<c:forEach items="${spaces}" var="space">
		<li class="list-group-item"><a
			href="<c:url value="/staff/space/${space.id}" />"> <span
				data-feather="box"></span> ${space.name}
		</a> (Created on <span class="date">${space.creationDate}</span> by
			${space.createdBy}) 
           <a href="javascript:checkSpaceLinkPresent('${space.id}')" class="checkSpaceLinkPresent" >
               <span class="float-right checkSpaceLinkPresent" id="deleteSpace" data-feather="trash-2"></span>
           </a>
        </li>
	</c:forEach>
</ul>

<!--  Ashmi changes start -->

<div class="modal fade" id="confirm-space-delete" tabindex="-1" role="dialog"
    aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" data-spaceValue="" id="headerSpaceValue">
                <h5 class="modal-title" id="deleteModalTitle">
                    Confirm Deletion?
                </h5>
                <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                <p id = "spaceData">
                    Are you sure you want to delete ?
                </p>
                <small class="text-danger" id="warning"></small>
            </div>
            <div class="modal-footer">
                <button type="button" id="closeButton" class="btn btn-default"
                    data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-danger btn-ok">Delete</button>
            </div>
        </div>
    </div>
</div>

<!--  Ashmi changes end -->
