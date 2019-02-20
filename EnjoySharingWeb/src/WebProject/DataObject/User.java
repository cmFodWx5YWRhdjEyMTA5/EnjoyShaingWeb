package WebProject.DataObject;

public class User {
	private Long userId;
	private String username;
	private String name;
	private String surname;
	private String email;
	private String password;
	private int roleId;
	private boolean isAdmin;

	public User() { }

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public boolean getIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(byte isAdmin) {
		this.isAdmin = isAdmin == (byte)1;
	}
	
	public boolean CheckUser(String email, String password)
	{
		return email.equals(getEmail()) && password.equals(getPassword());
	}
	
	public boolean UserIsEmpty()
	{
		return getEmail() == null || getEmail().equals("") || getPassword() == null || getPassword().equals("");
	}

}
