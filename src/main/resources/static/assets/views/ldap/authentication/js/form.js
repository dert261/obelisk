$(document).ready(function() {
		
	$(function () {
		$('[data-toggle="tooltip"]').tooltip()
	})
		
	$('.ldapAuthServerPart').on('click', '.add-server', function (){
		$.ajax({
			type: 'GET',
			url: "/ldap/authentication/ajax/addserver?"+$('#ldapAuthParams').serialize(),
			//data: ({ }),
		}).done(function(result) {
			$('#ldapAuthServersContent').html(result);
		});
				
		return false;
	});
	
	$('.ldapAuthServerPart').on('click', '.remove-server', function (){
		$.ajax({
			type: 'GET',
			url: "/ldap/authentication/ajax/delserver?"+$('#ldapAuthParams').serialize(),
			data: ({
				index: $(this).val(),
			}),
		}).done(function(result) {
			$('#ldapAuthServersContent').html(result);
		});
				
		return false;
	});
});

	