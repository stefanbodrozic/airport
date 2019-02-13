$(document).ready(function() {
	
	console.log('index js');
	
	function getFlights() {
		
		$.get('IndexServlet', function(data) {
			
			console.log(data);
			
			for(i in data.flights) {

				$('.flights-table').append(
						
						'<tr>' +
							'<td rowspan="2"><a href="flight.html?numberOfFlight=' + data.flights[i].numberOfFlight + '">' + data.flights[i].numberOfFlight + '</td>' +
							'<td><a href="flight.html?numberOfFlight=' + data.flights[i].numberOfFlight + '">' + data.flights[i].departureAirport.name + ' -> ' + data.flights[i].incomingAirport.name  + '</td>' + 
							'<td rowspan="2">' + data.flights[i].price + ' E' + '</td>' +
							'<td rowspan="2"><button type="submit" class="flights-submit-buy"><a href="flight.html?numberOfFlight=' + data.flights[i].numberOfFlight + '">' + 'Buy</button></td>' +
						'</tr>' +
						'<tr>' +
							'<td>' + data.flights[i].dateOfDeparture + ' - ' + data.flights[i].arrivalDate + '</td>' + 
							
						'</tr>'		
							
				);
			}
		});
	}
	

	
	function setAirports() {
		
		$.get('AirportServlet', function(data){
			console.log('set');
			$('#departure-airports').empty();
			$('#incoming-airports').empty();
			
			for(i in data.airports) {
				
				var departureOption = $("<option></option>").attr("value", data.airports[i].name);
				$('#departure-airports').append(departureOption);
				
				var incomingOption = $("<option></option>").attr("value", data.airports[i].name);
				$('#incoming-airports').append(incomingOption);
			
			}
			
		});
		
	}
	
	getFlights();
	setAirports();
});