$(document).ready(function() {
	
	// proverava da li je korisnik prijavljen
	// ako je prijavljen proverava da li je admin
	// ako je admin puni listu sa aerodromima
	function getAirports() {
			
		$.get('AirportServlet', function(data) {
				
			if(data.status === 'fail') {
				$('.add-flight-container').empty();
				console.log('fail');
				
				window.location.replace('index.html');
			} else if(data.status === 'unauthenticated') {
				$('.add-flight-container').empty();
				console.log('unauthenticated');
				
				window.location.replace('index.html');
			} else if(data.status === 'success') {
				console.log('success');
			}
			
			console.log(data);
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
	
	// dodaje novi let u bazu
	$('#btn-add').click(function(e){
		
		e.preventDefault();
		
		var departureDatetime = $('#departuredatetime').datetimepicker({ dateFormat: 'dd,MM,yyyy' }).val();
		var arrivalDatetime = $('#arrivaldatetime').datetimepicker({ dateFormat: 'dd,MM,yyyy' }).val();
		var departureAirport = $('#departure-airport-id').val();
		var incomingAirport = $('#incoming-airport-id').val();
		var totalSeatNumber = $('#seat-number').val();
		var price = $('#price').val();
		
		if(departureDatetime == '' || arrivalDatetime == '' || departureAirport == '' || incomingAirport == '' || totalSeatNumber == '' || price == '') {
			alert('All fields must have values!');
			return;
		}
		
		if(new Date(departureDatetime) > new Date(arrivalDatetime)) {
			alert('Date error!');
			return;
		}
		
		if(new Date(departureDatetime) < new Date()) {
			alert('Departure datetime error!');
			return;
		}
		
		if(new Date(arrivalDatetime) < new Date()) {
			alert('Arrival datetime error');
			return;
		}
		
		var data = {
				'action': 'add',
				'departureDatetime': departureDatetime,
				'arrivalDatetime': arrivalDatetime,
				'departureAirport': departureAirport,
				'incomingAirport': incomingAirport,
				'totalSeatNumber': totalSeatNumber,
				'price': price
		}
		
		console.log(data);
		
		$.post('FlightServlet', data, function(data){
			
			if(data.status == 'success') {
				alert('Successfully added flight!');
				window.location.replace('index.html');
			}
			
			if(data.status == 'fail') {
				window.reload();
			}
			
		});

	});
	
	getAirports();
});