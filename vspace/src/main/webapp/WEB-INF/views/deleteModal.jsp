<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
	$(document).ready(function() {
		$('#confirm-delete').on('click', '.btn-ok', function(e) {
			var url = $(this).data('url');
			var urlToLoadOnSuccess = $(this).data('urlToLoadOnSuccess');
			var urlToLoadOnError = $(this).data('urlToLoadOnError');
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
		$('#confirm-delete').on('show.bs.modal', function(e) {
			var data = $(e.relatedTarget).data();
			$('.recordId', this).text(data.recordId);
			$('.btn-ok', this).data('url', data.url);
			$('#warning', this).text(data.warning);
			$('.btn-ok', this).data('urlToLoadOnSuccess', data.callOnSuccess);
			$('.btn-ok', this).data('urlToLoadOnError', data.callOnError);
		});
	});
</script>
<div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="deleteModalTitle">
					Confirm
					<%=request.getParameter("elementType")%>
					Deletion?
				</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
			</div>
			<div class="modal-body">
				<p>
					Are you sure you want to delete
					<%=request.getParameter("elementType")%>
				<b class="recordId"></b>
				?
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