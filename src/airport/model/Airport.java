package airport.model;

public class Airport {

	private int id;
	private String name;
	
	public Airport(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Airport() {
		id = 0;
		name = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
}
