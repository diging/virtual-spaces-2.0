<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
	$(document).ready(function() {
		$('#confirm-delete').on('click', '.btn-ok', function(e) {
			var url = $(this).data('url');
			var urlToLoadOnSuccess = $(this).data('urlToLoadOnSuccess');
			$.ajax({
				url : url,
				type : 'DELETE',
				success : function(response) {
					console.log(response);
					if(response == "Success") {
						window.location.href = urlToLoadOnSuccess;
					} else {
						$('#closeButton').click();
						$('#errorMessage').find("strong").text(response+", please reload the page and try again");
						$('#errorMessage').show();							
					}
				}
			});
		});
		$('#confirm-delete').on('show.bs.modal', function(e) {
			var data = $(e.relatedTarget).data();
			console.log(data);
			$('.btn-ok', this).data('url', data.url);
			$('.btn-ok', this).data('urlToLoadOnSuccess', data.callOnSuccess);
		});
	});
</script>
<div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title" id="myModalLabel"></h4>
			</div>
			<div class="modal-body">
				<p>Are you sure you want to delete?</p>
			</div>
			<div class="modal-footer">
				<button type="button" id="closeButton" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-danger btn-ok">Delete</button>
			</div>
		</div>
	</div>
</div>