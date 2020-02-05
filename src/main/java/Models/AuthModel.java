package Models;

public class AuthModel {

	public String email;
	public String token;
	
	public AuthModel(String email,String token)
	{
		this.email=email;
		this.token=token;
	}
}
