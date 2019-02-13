package airport.model;

public class ReservedTicket {
	
	private int id;
	private Flight departureFlight;
	private int departureFlightSeatNumber;
	private Flight incomingFlight;
	private int incomingFlightSeatNumber;
	private String reservationDate;
	private User user;
	private String firstname;
	private String lastname;
	private boolean boughtTicket;
	private double price;
	private boolean deleted;
	
	public ReservedTicket() {
		id = 0;
		departureFlight = null;
		departureFlightSeatNumber = 0;
		incomingFlight = null;
		incomingFlightSeatNumber = 0;
		reservationDate = "";
		user = null;
		firstname = "";
		lastname = "";
		boughtTicket = false;
		price = 0.0;
		deleted = false;
	}
	
	public ReservedTicket(int id, Flight departureFlight, int departureFlightSeatNumber, Flight incomingFlight,
			int incomingFlightSeatNumber, String reservationDate, User user, String firstname, String lastname,
			boolean boughtTicket, double price, boolean deleted) {
		super();
		this.id = id;
		this.departureFlight = departureFlight;
		this.departureFlightSeatNumber = departureFlightSeatNumber;
		this.incomingFlight = incomingFlight;
		this.incomingFlightSeatNumber = incomingFlightSeatNumber;
		this.reservationDate = reservationDate;
		this.user = user;
		this.firstname = firstname;
		this.lastname = lastname;
		this.boughtTicket = boughtTicket;
		this.price = price;
		this.deleted = deleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Flight getDepartureFlight() {
		return departureFlight;
	}

	public void setDepartureFlight(Flight departureFlight) {
		this.departureFlight = departureFlight;
	}

	public int getDepartureFlightSeatNumber() {
		return departureFlightSeatNumber;
	}

	public void setDepartureFlightSeatNumber(int departureFlightSeatNumber) {
		this.departureFlightSeatNumber = departureFlightSeatNumber;
	}

	public Flight getIncomingFlight() {
		return incomingFlight;
	}

	public void setIncomingFlight(Flight incomingFlight) {
		this.incomingFlight = incomingFlight;
	}

	public int getIncomingFlightSeatNumber() {
		return incomingFlightSeatNumber;
	}

	public void setIncomingFlightSeatNumber(int incomingFlightSeatNumber) {
		this.incomingFlightSeatNumber = incomingFlightSeatNumber;
	}

	public String getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public boolean isBoughtTicket() {
		return boughtTicket;
	}

	public void setBoughtTicket(boolean boughtTicket) {
		this.boughtTicket = boughtTicket;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
