package airport.model;

public class User {
	
	public enum Role {USER, ADMIN};
	
	private int id;
	private String username;
	private String password;
	private String registeredAt;
	private Role role;
	private boolean blocked;
	private boolean deleted;
	
	public User(int id, String username, String password, String registeredAt, Role role, boolean blocked,
			boolean deleted) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.registeredAt = registeredAt;
		this.role = role;
		this.blocked = blocked;
		this.deleted = deleted;
	}
	
	public User() {
		id = 0;
		username = "";
		password = "";
		registeredAt = "";
		role = Role.USER;
		blocked = false;
		deleted = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(String registeredAt) {
		this.registeredAt = registeredAt;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
