var eventAuth = null;

$.get('SessionServlet', function(data) {
	
	eventAuth = data.auth;
	setData(data);
	
});

function setData(data) {
	var auth = data.auth;
	var status = data.status;
	
	if(data.status == "logged in") {
		$('.ul-nav').append('<li><a href="profile.html?id=' + data.auth.id +'">Profile</a></li>');
		$('.ul-nav').append('<li><a href="#" id="logout">Logout</a></li>');
		
		if(data.auth.role == 'ADMIN') {
			$('.ul-nav').append('<li><a href="admin-panel.html">Admin panel</a></li>');
			$('.ul-nav').append('<li><a href="add-flight.html">Add new flight</a></li>');
			$('.flights-h1').after('<a href="add-flight.html" style="color: rgb(1, 230, 2)"><h1>Add new flight</h1></a>');
		}
	} else {
		$('.ul-nav').append('<li><a href="login.html">Login</a></li>');
	}
	
	//logout
	$('#logout').click(function(e) {
		
		e.preventDefault();
		
		$.get('LogoutServlet', function() {
			console.log('logging out');
			
			window.location.replace('index.html');
		});
		
	});
	
}