$(document).ready(function() {
	
	$('#btn-register').click(function(e) {
		
		e.preventDefault();
		
		var username = $('#username').val();
		var password = $('#password').val();
		var repeatedPassword = $('#repeatedPassword').val();
		
		if(username == '') {
			alert('Username must have value!');
			$('#username').css('background-color','#FF4136');
			return;
		}
		
		if(password != repeatedPassword) {
			console.log(password);
			console.log(repeatedPassword);
			alert('Your password and repeated password do not match!!');
			$('#password').css('background-color','#FF4136');
			$('#repeatedPassword').css('background-color', '#FF4136');
			return;
		}

		var data = {
				'username': username,
				'password': password,
				'repeatedPassword': repeatedPassword
		}

		$.post('RegisterServlet', data, function(data){
			console.log(data);
			
			if(data.status == 'success') {
				window.location.replace('index.html');
			} 
			if(data.status == 'fail') {
				alert('Fail!');
				location.reload();
			}
			
		});
				
	});
	
});