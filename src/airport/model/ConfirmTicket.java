package airport.model;

public class ConfirmTicket {
	
	private int id;
	private ReservedTicket reservedTicket;
	private String confirmDate;
	private boolean deleted;
	
	public ConfirmTicket() {
		id = 0;
		reservedTicket = null;
		confirmDate = "";
		deleted = false;
	}
	
	public ConfirmTicket(int id, ReservedTicket reservedTicket, String confirmDate, boolean deleted) {
		super();
		this.id = id;
		this.reservedTicket = reservedTicket;
		this.confirmDate = confirmDate;
		this.deleted = deleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ReservedTicket getReservedTicket() {
		return reservedTicket;
	}

	public void setReservedTicket(ReservedTicket reservedTicket) {
		this.reservedTicket = reservedTicket;
	}

	public String getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
