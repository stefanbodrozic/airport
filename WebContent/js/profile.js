$(document).ready(function() {
	
	var userId = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(userId);
	
	var data = {
		'action': 'single',
		'userId': userId
	}
	
	$.get('UserServlet', data, function(data) {
		
		console.log(data);
		
		if(data.user == null) {
			alert('User does not exist!');
			window.location.replace('index.html');
			return;
		}
		
		if(data.auth == null) {
			console.log('korisnik nije prijavljen');
			alert('You must be logged in to access this page!');
			window.location.replace('login.html');
			return;
		}
		
		if(data.auth.id != data.user.id && data.auth.role == 'USER') {	//ako prijavljeni korisnik nije vlasnik rezervacije
			alert('You dont have permission to access this page!');
			window.location.replace('index.html');
			return;
		}
				
		$('#username').val(data.user.username);
		$('#password').val(data.user.password);
		$('#registration-date').val(data.user.registeredAt);
		$('#user-role').val(data.user.role);
		
		$('input[name=' + 'blocked' + ']').prop('checked', data.user.blocked)
		

		$('#role').hide();
		$('#password').hide();
		
		$('#btn-edit-user').on('click', function(event) {
			
			event.preventDefault();
			
			if(data.auth.role == 'ADMIN') {
				$('#role').show();
			}

			$('#password').show();

			$("#username").prop('readonly', false);
			$("#password").prop('readonly', false);
			
			$('#btn-confirm-edit').show();
			
		});
	});
	

	
	$('#btn-confirm-edit').on('click', function(event) {
		
		var username = $('#username').val();
		var password = $('#password').val();
		var role = $('#role').val();	
		var blocked = $('input[type=checkbox]').prop('checked');
		
		var data = {
			'action': 'update',
			'userId': userId,
			'username': username,
			'password': password,
			'role': role,
			'blocked': blocked
		}
		
		console.log(data);
		
		$.post('UserServlet', data, function(data){
			
			console.log(data);
			
			alert('Successfully updated!');
			window.location.reload();
		});
		
		event.preventDefault();
		
	});
	
	function findReservedTickets() {
		
		var data = {
			'action': 'userTickets',
			'userId': userId
		}
		
		$.get('ReservedTicketServlet', data, function(data) {
			console.log(data);
			
			for(i in data.reservedTickets) {
							
				$('#reserved-tickets-table').append(
					
					'<tr>' +
						'<td><a href="ticket-final.html?id=' + data.reservedTickets[i].id + '">' + data.reservedTickets[i].id + '</td>' +
						'<td>' + data.reservedTickets[i].reservationDate + '</td>' +
					'</tr>' 
	
						
				);
			
			}
		})
		
	}
	
	function findBoughtTickets() {
		
		var data = {
				'action': 'userTickets',
				'userId': userId
		}
		
		$.get('ConfirmTicketServlet', data, function(data) {
			
			console.log(data);
			
			for(i in data.confirmTickets) {
				
				$('#bought-tickets-table').append(
						
						'<tr>' +
							'<td><a href="ticket-final.html?id=' + data.confirmTickets[i].reservedTicket.id + '">' + data.confirmTickets[i].reservedTicket.id + '</td>' +
							'<td>' + data.confirmTickets[i].confirmDate + '</td>' +
						'</tr>' 
		
							
				);
				
			}
		});
	}
	
	findReservedTickets();
	findBoughtTickets();
});