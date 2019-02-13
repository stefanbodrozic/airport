$(document).ready(function(){
	
	var numberOfFlight = window.location.search.slice(1).split('&')[0].split('=')[1];
	
	$('.admin-options').hide();
	$('.edit-flight').hide();
	$('.reserved-tickets').hide();
	$('.bought-tickets').hide();
	
	// pronalazi let na osnovu broja leta
	function getFlight() {
		
		console.log(numberOfFlight);
		
		$.get('FlightServlet', {'numberOfFlight': numberOfFlight}, function(data){
			
			//prikaz leta 
			console.log("Prvi get na flight servlet: ");
			console.log(data);
			
			if(data.status === 'success') {

				
				if(data.reservedAndBoughtTicketNumber < data.flight.totalSeatNumber) {
					
					$('#flights-table').append(
							
							'<tr>' +
								'<td rowspan="2"><a href="flight.html?numberOfFlight=' + data.flight.numberOfFlight + '">' + data.flight.numberOfFlight + '</td>' +
								'<td><a href="airport.html?id=' + data.flight.departureAirport.id + '">' + data.flight.departureAirport.name + '</a>' + ' -> ' + '<a href="airport.html?id=' + data.flight.incomingAirport.id + '">' + data.flight.incomingAirport.name + '</td>' + 
								'<td rowspan="2">' + data.flight.price + ' E' + '</td>' +
								'<td rowspan="2"><button type="submit" class="flights-submit-buy">Buy ticket</button></td>' +
							'</tr>' +
							'<tr>' +
								'<td>' + data.flight.dateOfDeparture + ' - ' + data.flight.arrivalDate + '</td>' + 		
							'</tr>'
								
						)
					
					$('.available-seats').append('Tickets bought: ' + data.reservedAndBoughtTicketNumber + '/' + data.flight.totalSeatNumber);

					
				} else if (data.reservedAndBoughtTicketNumber >= data.flight.totalSeatNumber) {
					alert('No more available seats!');
					
					$('#flights-table').append(
							
							'<tr>' +
								'<td rowspan="2"><a href="flight.html?numberOfFlight=' + data.flight.numberOfFlight + '">' + data.flight.numberOfFlight + '</td>' +
								'<td><a href="airport.html?id=' + data.flight.departureAirport.id + '">' + data.flight.departureAirport.name + '</a>' + ' -> ' + '<a href="airport.html?id=' + data.flight.incomingAirport.id + '">' + data.flight.incomingAirport.name + '</td>' + 
								'<td rowspan="2">' + data.flight.price + ' E' + '</td>' +
							'</tr>' +
							'<tr>' +
								'<td>' + data.flight.dateOfDeparture + ' - ' + data.flight.arrivalDate + '</td>' + 		
							'</tr>'
								
						)
					
					$('.available-seats').append('Tickets bought: ' + data.reservedAndBoughtTicketNumber + '/' + data.flight.totalSeatNumber);

				}
				
				if(data.auth.blocked == true) {
					alert('Your account has been blocked');
					$('.flights-submit-buy').prop("disabled", true);
					$('.flights-submit-reserve').prop('disabled', true);

				}
				
				if(data.auth == null) {
					console.log('nije prijavljen');
					$('.flights-submit-reserve').prop('disabled', true);
					$('.flights-submit-buy').prop('disabled', true);
				}
				
					
				console.log('pre admin provere');
				console.log(data);

				if(data.auth != null) { // proverava da li je korisnik prijavljen, ako nije nece proveravati da li admin
					if(data.auth.role == "ADMIN") {
						$('.admin-options').show();
						
						// update leta prikaz forme
						$('#edit-flight').on('click', function(event) {
	
							$('.edit-flight').show();
							
							// postavljanje na vrednosti polja na trenutne vrednosti upisane u bazu 
							$('#arrivaldatetime').val(data.flight.arrivalDate);
							$('#departure-airport-id').val(data.flight.departureAirport.name);
							$('#incoming-airport-id').val(data.flight.incomingAirport.name);
							$('#seat-number').val(data.flight.totalSeatNumber);
							$('#price').val(data.flight.price);
							
							$.get('AirportServlet', function(data) {
	
								$('#departure-airports').empty();
								$('#incoming-airports').empty();
								for(i in data.airports) {
								
									var departureOption = $("<option></option>").attr("value", data.airports[i].name);
									$('#departure-airports').append(departureOption);
									
									var incomingOption = $("<option></option>").attr("value", data.airports[i].name);
									$('#incoming-airports').append(incomingOption);
								}
								
							});	
						});
						
						
					}
				}
								
			}
			
		});
		
		
	}
	
	// brisanje leta
	$('#delete-flight').on('click', function(event) {
		
		var data = {
				'action': 'delete',
				'numberOfFlight': numberOfFlight
		}
		
		$.post('FlightServlet', data, function(data) {
			
			if(data.status == 'success') {
				alert('Successfully deleted flight!');
				window.location.replace('index.html');
			}
			
		})
		
		event.preventDefault();
		return false;
	});
	

		
	// update slanje
	$('#btn-update-flight').on('click', function(event){
		
		event.preventDefault();
		
		var arrivalDatetime = $('#arrivaldatetime').datetimepicker({ dateFormat: 'dd,MM,yyyy' }).val();
		var departureAirport = $('#departure-airport-id').val();
		var incomingAirport = $('#incoming-airport-id').val();
		var totalSeatNumber = $('#seat-number').val();
		var price = $('#price').val();
		
		var data = {
				'action': 'update',
				'numberOfFlight': numberOfFlight,
				'arrivalDatetime': arrivalDatetime,
				'departureAirport': departureAirport,
				'incomingAirport': incomingAirport,
				'totalSeatNumber': totalSeatNumber,
				'price': price
		}
		
		console.log("log na update: " + data);
		$.post('FlightServlet', data, function(data){
			
			if(data.status == 'success') {
				window.location.reload();
			}
			
		});
			
	});
	
	
	function buyTicket() {
		
		$('.flights-table').delegate('button', 'click', function() {
			
			if(confirm('Go to reservation page!')) {
				window.location.replace('confirm-reservation.html?numberOfFlight=' + numberOfFlight);
			} else {
				
			}
		});

	}
			
	getFlight();
	buyTicket();
	
	function findReservedTickets() {
		
		var data = {
				'action': 'flightTickets',
				'numberOfFlight': numberOfFlight
		}
		
		$.get('ReservedTicketServlet', data, function(data) {
			
			console.log(data);
			
			if(data.auth != null) {
				if(data.auth.role == 'ADMIN') {
				
				$('.reserved-tickets').show();
						
				for(i in data.reservedTickets) {
					if(data.reservedTickets[i].departureFlight.numberOfFlight == numberOfFlight) {
						$('#reserved-tickets-table').append(
								
								'<tr>' +
									'<td><a href="ticket-final.html?id=' + data.reservedTickets[i].id + '">' + data.reservedTickets[i].reservationDate + '</td>' +
									'<td>Seat number: ' + data.reservedTickets[i].departureFlightSeatNumber + '</td>' +
									'<td><a href="profile.html?id=' + data.reservedTickets[i].user.id + '">' + data.reservedTickets[i].user.username + '</td>' +

								'</tr>' 
			
						);
					}  else if(data.reservedTickets[i].incomingFlight.numberOfFlight == numberOfFlight) {
						$('#reserved-tickets-table').append(
								
								'<tr>' +
									'<td><a href="ticket-final.html?id=' + data.reservedTickets[i].id + '">' + data.reservedTickets[i].reservationDate + '</td>' +
									'<td>Seat number: ' + data.reservedTickets[i].incomingFlightSeatNumber + '</td>' +
									'<td><a href="profile.html?id=' + data.reservedTickets[i].user.id + '">' + data.reservedTickets[i].user.username + '</td>' +

								'</tr>' 
			
						);
					}

				}
				}
			}
			
		});
		
	}
	
	function findBoughtTickets() {
		
		var data = {
				'action': 'flightTickets',
				'numberOfFlight': numberOfFlight
		}
		
		$.get('ConfirmTicketServlet', data, function(data) {
			
			console.log(data);
			
			if(data.auth != null) {
				if(data.auth.role == 'ADMIN') {
				
				$('.bought-tickets').show();
						
				for(i in data.confirmTicket) {
					if(data.confirmTicket[i].reservedTicket.departureFlight.numberOfFlight == numberOfFlight) {
						$('#bought-tickets-table').append(
								
								'<tr>' +
									'<td><a href="ticket-final.html?id=' + data.confirmTicket[i].id + '">' + data.confirmTicket[i].reservedTicket.reservationDate + '</td>' +
									'<td>Seat number: ' + data.confirmTicket[i].reservedTicket.departureFlightSeatNumber + '</td>' +
									'<td><a href="profile.html?id=' + data.confirmTicket[i].reservedTicket.user.id + '">' + data.confirmTicket[i].reservedTicket.user.username + '</td>' +

								'</tr>' 
			
						);
					}  else if(data.confirmTicket[i].reservedTicket.incomingFlight.numberOfFlight == numberOfFlight) {
						$('#bought-tickets-table').append(
								
								'<tr>' +
									'<td><a href="ticket-final.html?id=' + data.confirmTicket[i].id + '">' + data.confirmTicket[i].reservedTicket.reservationDate + '</td>' +
									'<td>Seat number: ' + data.confirmTicket[i].reservedTicket.incomingFlightSeatNumber + '</td>' +
									'<td><a href="profile.html?id=' + data.confirmTicket[i].reservedTicket.user.id + '">' + data.confirmTicket[i].reservedTicket.user.username + '</td>' +

								'</tr>' 
			
						);
					}

				}
				}
			}
			
		});
		
	}
	
	findReservedTickets();
	findBoughtTickets();
	
	$('#sort-button').on('click', function(event) {
		
		var sort = $('#sort-by').val();
		
		var data = {
				'action': 'search',
				'numberOfFlight': numberOfFlight,
				'sort': sort
		}
		
		$.get('ReservedTicketServlet', data, function(data) {
			
			console.log('sort');
			console.log(data);
			
			for(i in data.reservedTickets) {
				if(data.reservedTickets[i].departureFlight.numberOfFlight == numberOfFlight) {
					$('#reserved-tickets-table').append(
							
							'<tr>' +
								'<td><a href="ticket-final.html?id=' + data.reservedTickets[i].id + '">' + data.reservedTickets[i].reservationDate + '</td>' +
								'<td>' + data.reservedTickets[i].departureFlightSeatNumber + '</td>' +
								'<td><a href="profile.html?id=' + data.reservedTickets[i].user.id + '">' + data.reservedTickets[i].user.username + '</td>' +

							'</tr>' 
		
					);
				}  else if(data.reservedTickets[i].incomingFlight.numberOfFlight == numberOfFlight) {
					$('#reserved-tickets-table').append(
							
							'<tr>' +
								'<td><a href="ticket-final.html?id=' + data.reservedTickets[i].id + '">' + data.reservedTickets[i].reservationDate + '</td>' +
								'<td>Seat number: ' + data.reservedTickets[i].incomingFlightSeatNumber + '</td>' +
								'<td><a href="profile.html?id=' + data.reservedTickets[i].user.id + '">' + data.reservedTickets[i].user.username + '</td>' +

							'</tr>' 
		
					);
				}

			}
			
		});
		
		event.preventDefault();
	});
	
});