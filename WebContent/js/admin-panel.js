$(document).ready(function(){
	
	$.get('UserServlet', {'action': 'all'}, function(data) {
		
		console.log(data);
				
		if(data.auth == null) {
			console.log('fail');
			alert('You must be logged in to access this page!');
			window.location.replace('login.html');
		}
		
		if(data.auth.role == 'USER') {
			alert('You dont have permission to access this page!')
			window.location.replace('index.html');
			return;
		}
		
		for(i in data.users) {
				//flights zbog css-a
			$('.flights-table').append(
				
					'<tr>' + 
					
						'<td><a href="profile.html?id=' + data.users[i].id + '">' + data.users[i].username + '</td>' +
						'<td>' + data.users[i].registeredAt + '</td>' + 
						'<td>' + data.users[i].role + '</td>' +
						'<td><input type="submit" data-id="' + data.users[i].id + '" value="Delete" class="search-input-submit" id="delete-user" style="background: red;"></td>' +
					
					'</tr>'				
			);
		}
	});
	
	$('.search-input-submit').click(function (e) {
		
		e.preventDefault();
		
		var search = $('.search-input').val();
		
		var usernameSearch = $('input[type=checkbox]').prop('checked');
		
		var searchRole = $('#search-by').val();
		
		var sortOption = $('#sort-by').val();
		
		var data = {
				'action': 'adminPanel',
				'search': search,
				'usernameSearch': usernameSearch,
				'searchRole': searchRole,
				'sortOption': sortOption
		}
		
		console.log(data);
		
		$('.flights-table').remove(); //brise se prethodno ucitana tabela
		
		$.get('SearchServlet', data, function(data) {
			
			console.log(data);
			
			for(i in data.users) {
				
				$('.users').append(
						
						'<table class="flights-table">' +
							'<tr>' + 
								
								'<td><a href="profile.html?id=' + data.users[i].id + '">' + data.users[i].username + '</td>' +
								'<td>' + data.users[i].registeredAt + '</td>' + 
								'<td>' + data.users[i].role + '</td>' +
								'<td><input type="submit" data-id="' + data.users[i].id + '" value="Delete" class="search-input-submit" id="delete-user" style="background: red;"></td>' +

							'</tr>' +
						'</table>'
							
				);
				
			}
			
		});
		
	});
	
	$('.flights-table').delegate('#delete-user', 'click', function() {
		
		var id = $(this).data('id');
		
		console.log('user id: ' + id);
		
		var data = {
				'action': 'delete',
				'userId': id
		}
		
		$.post('UserServlet', data, function(data) {
			
			if(data.status == 'success') {
				alert('Successfully deleted user!')
				window.location.reload();
			}
			
		});;
		
	});
});