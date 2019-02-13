package airport.model;

public class Flight {
	
	private int id;
	private String numberOfFlight;
	private String dateOfDeparture;
	private String arrivalDate;
	private Airport departureAirport;
	private Airport incomingAirport;
	private int totalSeatNumber;
	private double price;
	private boolean deleted;

	public Flight(int id, String numberOfFlight, String dateOfDeparture, String arrivalDate, Airport departureAirport,
			Airport incomingAirport, int totalSeatNumber, double price, boolean deleted) {
		super();
		this.id = id;
		this.numberOfFlight = numberOfFlight;
		this.dateOfDeparture = dateOfDeparture;
		this.arrivalDate = arrivalDate;
		this.departureAirport = departureAirport;
		this.incomingAirport = incomingAirport;
		this.totalSeatNumber = totalSeatNumber;
		this.price = price;
		this.deleted = deleted;
	}

	public Flight() {
		id = 0;
		numberOfFlight = "";
		dateOfDeparture = "";
		arrivalDate = "";
		departureAirport = null;
		incomingAirport = null;
		totalSeatNumber = 0;
		price = 0.0;
		deleted = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumberOfFlight() {
		return numberOfFlight;
	}

	public void setNumberOfFlight(String numberOfFlight) {
		this.numberOfFlight = numberOfFlight;
	}

	public String getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(String dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Airport getDepartureAirport() {
		return departureAirport;
	}

	public void setDepartureAirport(Airport departureAirport) {
		this.departureAirport = departureAirport;
	}

	public Airport getIncomingAirport() {
		return incomingAirport;
	}

	public void setIncomingAirport(Airport incomingAirport) {
		this.incomingAirport = incomingAirport;
	}

	public int getTotalSeatNumber() {
		return totalSeatNumber;
	}

	public void setTotalSeatNumber(int totalSeatNumber) {
		this.totalSeatNumber = totalSeatNumber;
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
