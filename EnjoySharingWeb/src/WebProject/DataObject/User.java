package WebProject.DataObject;

public class User {
	private int userId;
	private String username;
	private String password;
	private int roleId;
	private boolean isAdmin;

	public User() { }

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	
	public boolean CheckUser(String username, String password) // la password mi arriva già criptata
	{
		return username.equals(getUsername()) && password.equals(getPassword());
	}
	
	public boolean UserIsEmpty()
	{
		return getUsername() == null || getUsername().equals("") || getPassword() == null || getPassword().equals("");
	}

}
