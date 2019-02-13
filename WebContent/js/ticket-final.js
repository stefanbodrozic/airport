$(document).ready(function() {
	
	var ticketId = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(ticketId);
	
	function findFlights() {
		
		$.get('ReservedTicketServlet', {'action': 'ticketFinal', 'ticketId': ticketId}, function(data) {
			
			if(data.reservedTicket == null) {
				alert('Error!');
				window.location.replace('index.html');
				return;
			}
			
			console.log(data);
			if(data.auth == null) {
				alert('You must be logged in to access this page!');
				window.location.replace('login.html');
				return;
			}
			
			if(data.auth.id != data.reservedTicket.user.id && data.auth.role == 'USER') {	//ako prijavljeni korisnik nije vlasnik rezervacije
				alert('You dont have permission to access this page!');
				window.location.replace('index.html');
				return;
			}
			
			if(data.auth.blocked == true) {
				alert('Your account has been blocked');
				$('.firstname-input').prop("disabled", true);
				$('.lastname-input').prop('disabled', true);
				$('#selected-seat-number-departure').prop('disabled', true);
				$('#selected-seat-number-incoming').prop('disabled', true);
				
				$('.button-edit-reservation').prop('disabled', true);
				$('.button-delete-ticket').prop('disabled', true);
				$('.button-confirm-reservation').prop('disabled', true);
				$('.button-buy-ticket').prop('disabled', true);
			}
			
			if(data.reservedTicket.boughtTicket == true) {
				alert('Ticket is bought and cant be changed or deleted!');
				$('.firstname-input').prop("disabled", true);
				$('.lastname-input').prop('disabled', true);
				$('#selected-seat-number-departure').prop('disabled', true);
				$('#selected-seat-number-incoming').prop('disabled', true);
				
				$('.button-edit-reservation').prop('disabled', true);
				$('.button-delete-ticket').prop('disabled', true);
				$('.button-confirm-reservation').prop('disabled', true);
				$('.button-buy-ticket').prop('disabled', true);
			}
						
			if(data.status == 'success') {
				
				$('#departure-flight-table').append(
						'<tr>' +
							'<td rowspan="2"><a href="flight.html?numberOfFlight=' + data.departureFlight.numberOfFlight + '">' + data.departureFlight.numberOfFlight + '</td>' +
							'<td><a href="airport.html?id=' + data.departureFlight.departureAirport.id + '">' + data.departureFlight.departureAirport.name + '</a>' + ' -> ' + '<a href="airport.html?id=' + data.departureFlight.incomingAirport.id + '">' + data.departureFlight.incomingAirport.name + '</td>' + 
							'<td>' + data.departureFlight.price + ' E' + '</td>' +
						'</tr>' +
						'<tr>' +
							'<td>' + data.departureFlight.dateOfDeparture + ' - ' + data.departureFlight.arrivalDate + '</td>' + 
							'<td><a href="profile.html?id=' + data.reservedTicket.user.id + '">' + data.reservedTicket.user.username + '</a></td>' +
						'</tr>'
					);
				
				$('.firstname-input').val(data.reservedTicket.firstname);
				$('.lastname-input').val(data.reservedTicket.lastname);
				if(data.reservedTicket.departureFlightSeatNumber == 0) {
					$('#selected-seat-number-departure').val('choose value');
				} else {
					$('#selected-seat-number-departure').val(data.reservedTicket.departureFlightSeatNumber);

				}
				if(data.reservedTicket.incomingFlightSeatNumber == 0) {
					$('#selected-seat-number-incoming').val('choose value');
				} else {
					$('#selected-seat-number-incoming').val(data.reservedTicket.incomingFlightSeatNumber);
				}
				
				$('#price').val(data.totalPrice);
				
				
				if($.isEmptyObject(data.incomingFlight)){ //provera da li ima povratnih letova
//					    alert("This Object is empty.");
					   	$("<h1>Incoming flight - None</h1>").insertAfter(".incoming-flight-table");
					   	$('#selected-seat-number-incoming').hide();
					   	$('#incoming-flight-table').hide();
					}else{
//					    alert("This Object is not empty.");
						
						$('#incoming-flight-table').append(
								
								'<tr>' +
									'<td rowspan="2"><a href="flight.html?numberOfFlight=' + data.incomingFlight.numberOfFlight + '">' + data.incomingFlight.numberOfFlight + '</td>' +
									'<td><a href="airport.html?id=' + data.incomingFlight.departureAirport.id + '">' + data.incomingFlight.departureAirport.name + '</a>' + ' -> ' + '<a href="airport.html?id=' + data.incomingFlight.incomingAirport.id + '">' + data.incomingFlight.incomingAirport.name + '</td>' + 
									'<td rowspan="2">' + data.incomingFlight.price + ' E' + '</td>' +
								'</tr>' +
								'<tr>' +
									'<td>' + data.incomingFlight.dateOfDeparture + ' - ' + data.incomingFlight.arrivalDate + '</td>' + 
									
								'</tr>'
									
						)
						
						if($.isEmptyObject(data.incomingAvailableSeats)){ //provera da li ima slobodnih mesta na povratnom letu
//							    alert("This Object is empty.");
							
								alert('No more available seats!');
							   	
								$('.firstname-input').prop("disabled", true);
								$('.lastname-input').prop('disabled', true);
								$('#selected-seat-number-departure').prop('disabled', true);
								$('#selected-seat-number-incoming').prop('disabled', true);
								
								$('.button-edit-reservation').prop('disabled', true);
								$('.button-delete-ticket').prop('disabled', true);
								$('.button-confirm-reservation').prop('disabled', true);
								$('.button-buy-ticket').prop('disabled', true);
																
							}else{
							
								for(i in data.incomingAvailableSeats) {
									var seatOption = $("<option></option>").attr("value", data.incomingAvailableSeats[i]);
									$('#choose-seat-incoming').append(seatOption);
								}
						}
							
							
					}
				
				if($.isEmptyObject(data.departureAvailableSeats)){ //provera da li ima slobodnih mesta na letu
//					    alert("This Object is empty.");
					
						alert('No more available seats!');
					   	
						$('.firstname-input').prop("disabled", true);
						$('.lastname-input').prop('disabled', true);
						$('#selected-seat-number-departure').prop('disabled', true);
						$('#selected-seat-number-incoming').prop('disabled', true);
						
						$('.button-edit-reservation').prop('disabled', true);
						$('.button-delete-ticket').prop('disabled', true);
						$('.button-confirm-reservation').prop('disabled', true);
						$('.button-buy-ticket').prop('disabled', true);
												
					}else{
						for(i in data.departureAvailableSeats) {
							var seatOption = $("<option></option>").attr("value", data.departureAvailableSeats[i]);
							$('#choose-seat-departure').append(seatOption);
						}
					}
				
				}
			
			
		});
		
	}
	
	$('.button-confirm-reservation').click(function(event) {
		
		event.preventDefault();

		
		var firstname = $('.firstname-input').val();
		var lastname = $('.lastname-input').val();
		var departureFlightSeatNumber = $('#selected-seat-number-departure').val();
		var incomingFlightSeatNumber = $('#selected-seat-number-incoming').val();
		var price = $('#price').val();
		
		
		if(firstname == "" || lastname == "" || departureFlightSeatNumber == "") {
			alert('All fields must have values!');
			return;
		}
		
		var data = {
				'action': 'update',
				'ticketId': ticketId,
				'firstname': firstname,
				'lastname': lastname,
				'departureFlightSeatNumber': departureFlightSeatNumber,
				'incomingFlightSeatNumber': incomingFlightSeatNumber,
				'price': price
		}
		
		$.post('ReservedTicketServlet', data, function(data) {
			console.log(data);
			alert('Make reservation - finished');
			window.location.reload();
		});
		
	});
	
	$('.button-buy-ticket').click(function(event) {
		event.preventDefault();
		
		var firstname = $('.firstname-input').val();
		var lastname = $('.lastname-input').val();
		var departureFlightSeatNumber = $('#selected-seat-number-departure').val();
		var incomingFlightSeatNumber = $('#selected-seat-number-incoming').val();
		var price = $('#price').val();
		
		if(firstname == "" || lastname == "" || departureFlightSeatNumber == "") {
			alert('All fields must have values!');
			return;
		}
		
		var data = {
				'action': 'buy',
				'ticketId': ticketId,
				'firstname': firstname,
				'lastname': lastname,
				'departureFlightSeatNumber': departureFlightSeatNumber,
				'incomingFlightSeatNumber': incomingFlightSeatNumber,
				'price': price,
				'boughtTicket': 'true'
		}
		
		$.post('ReservedTicketServlet', data, function(data) {
			console.log(data);
			alert('Buy ticket - finished');
			window.location.reload();
		});
		
	});
	
	$('.button-edit-reservation').click(function(event) {
		
		event.preventDefault();
		
		var firstname = $('.firstname-input').val();
		var lastname = $('.lastname-input').val();
		var departureFlightSeatNumber = $('#selected-seat-number-departure').val();
		var incomingFlightSeatNumber = $('#selected-seat-number-incoming').val();
		var price = $('#price').val();
		
		if(firstname == "" || lastname == "" || departureFlightSeatNumber == "") {
			alert('All fields must have values!');
			return;
		}
		
		var data = {
				'action': 'update',
				'ticketId': ticketId,
				'firstname': firstname,
				'lastname': lastname,
				'departureFlightSeatNumber': departureFlightSeatNumber,
				'incomingFlightSeatNumber': incomingFlightSeatNumber,
				'price': price
		}
		
		$.post('ReservedTicketServlet', data, function(data) {
			console.log(data);
			alert('Edit reservation - finished');
			window.location.reload();
		});
		
	})
	
	$('.button-delete-ticket').click(function(event) {
		
		event.preventDefault();
		
		data = {
				'action': 'delete',
				'ticketId': ticketId
		}
		
		$.post('ReservedTicketServlet', data, function(data){ 
			console.log(data)
			alert('Delete reservation - finished');
			window.location.replace('index.html');
		});
	});
	
	findFlights();
});