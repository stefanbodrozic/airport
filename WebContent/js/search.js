$(document).ready(function() {
	
	$('.search-input-submit').click(function (e) {
		
		e.preventDefault();
		
		var search = $('.search-input').val();
		
		var ticketNumberCheckBox = $('input[type=checkbox]').prop('checked');
		
		var departureAirport = $('#departure-airport-id').val();
		
		var incomingAirport = $('#incoming-airport-id').val();
		
		var departureDatetime = $('#departuredatetime').datetimepicker({ dateFormat: 'dd,MM,yyyy' }).val();
		
		var arrivalDatetime = $('#arrivaldatetime').datetimepicker({ dateFormat: 'dd,MM,yyyy' }).val();
		
		var minPrice = $('#min-price').val();
		
		var maxPrice = $('#max-price').val();
		
		var sort = $('#sort-by').val();
		
		var data = {
				'action': 'index',
				'search': search,
				'ticketNumberCheckBox': ticketNumberCheckBox,
				'departureAirport': departureAirport,
				'incomingAirport': incomingAirport,
				'departureDatetime': departureDatetime,
				'arrivalDatetime': arrivalDatetime,
				'minPrice': minPrice,
				'maxPrice': maxPrice,
				'sort': sort			
		}
		
		console.log(data);
		
		$('.flights-table').remove(); //brise se prethodno ucitana tabela
		
		$.get('SearchServlet', data, function(data) {
			
			console.log(data);
			
			for(i in data.flights) {
				
				$('.flights').append(
						
						'<table class="flights-table">' +
							'<tr>' +
								'<td rowspan="2"><a href="flight.html?numberOfFlight=' + data.flights[i].numberOfFlight + '">' + data.flights[i].numberOfFlight + '</td>' +
								'<td><a href="flight.html?numberOfFlight=' + data.flights[i].numberOfFlight + '">' + data.flights[i].departureAirport.name + ' -> ' + data.flights[i].incomingAirport.name  + '</td>' + 
								'<td rowspan="2">' + data.flights[i].price + ' E' + '</td>' +
								'<td rowspan="2"><button type="submit" class="flights-submit-buy"><a href="flight.html?numberOfFlight=' + data.flights[i].numberOfFlight + '">' + 'Buy</button></td>' +
							'</tr>' +
							'<tr>' +
								'<td>' + data.flights[i].dateOfDeparture + ' - ' + data.flights[i].arrivalDate + '</td>' + 
								
							'</tr>' + 
						'</table>'
							
				);
				
			}
			
		});
		
	});	
});