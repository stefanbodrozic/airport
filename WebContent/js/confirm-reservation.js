$(document).ready(function(){ 
	
	var numberOfFlight = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(numberOfFlight)
	
	function getFlights() {
		
		$.get('FlightServlet', {'numberOfFlight': numberOfFlight}, function(data) {
			
			console.log(data);
			if(data.auth == null) {
				$('.flights-table').hide();
				alert('You must be logged in to access this page!');
				window.location.replace('login.html');
				return;
			}
			
			if(data.auth.blocked == true) {
				alert('Your account has been blocked');
				window.location.replace('index.html');
				return;
			}
			
			if(data.status == 'success') {
	
				$('#departure-flight-table').append(
					'<tr>' +
						'<td rowspan="2"><a href="flight.html?numberOfFlight=' + data.flight.numberOfFlight + '">' + data.flight.numberOfFlight + '</td>' +
						'<td><a href="airport.html?id=' + data.flight.departureAirport.id + '">' + data.flight.departureAirport.name + '</a>' + ' -> ' + '<a href="airport.html?id=' + data.flight.incomingAirport.id + '">' + data.flight.incomingAirport.name + '</td>' + 
						'<td rowspan="2">' + data.flight.price + ' E' + '</td>' +
					'</tr>' +
					'<tr>' +
						'<td>' + data.flight.dateOfDeparture + ' - ' + data.flight.arrivalDate + '</td>' + 
						
					'</tr>'
				);
			
				$('#incoming-flights').empty();
				
				if($.isEmptyObject(data.incomingFlights)){ //provera da li ima povratnih letova
//					   alert("This Object is empty.");
					   $("<h1>None</h1>").insertAfter("#incoming-flight-h1");
					   $('#selected-incoming-flight').hide();
					}else{
//					  alert("This Object is not empty.");
					}
				
				for(i in data.incomingFlights) {
					
					var incomingFlightOption = $("<option></option>").attr("value", data.incomingFlights[i].numberOfFlight);
					$('#choose-incoming-flight').append(incomingFlightOption);
					
					$('#incoming-flights').append(
							
						'<tr>' +
							'<td rowspan="2"><a href="flight.html?numberOfFlight=' + data.incomingFlights[i].numberOfFlight + '">' + data.incomingFlights[i].numberOfFlight + '</td>' +
							'<td><a href="airport.html?id=' + data.incomingFlights[i].departureAirport.id + '">' + data.incomingFlights[i].departureAirport.name + '</a>' + ' -> ' + '<a href="airport.html?id=' + data.incomingFlights[i].incomingAirport.id + '">' + data.incomingFlights[i].incomingAirport.name + '</td>' + 
							'<td rowspan="2">' + data.incomingFlights[i].price + ' E' + '</td>' +
						'</tr>' +
						'<tr>' +
							'<td>' + data.incomingFlights[i].dateOfDeparture + ' - ' + data.incomingFlights[i].arrivalDate + '</td>' + 
							
						'</tr>'
							
					)
					
				}
			}
			
		});
		
	}
	
	$('.button-next').click(function(event) {
		event.preventDefault();
	
		var selectedIncomingFlight = $('#selected-incoming-flight').val();
		
		var data = {
				'action': 'add',
				'departureFlightNumber': numberOfFlight,
				'incomingFlightNumber': selectedIncomingFlight
		}
		
		$.post('ReservedTicketServlet', data, function(data) {
			console.log(data);
			window.location.replace('ticket-final.html?id=' + data.newReservation);
		});
		
	});
	
	getFlights();
});