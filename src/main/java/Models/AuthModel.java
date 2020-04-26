package Models;

public class AuthModel {

	public String email;
	public String token;
	public String errorMessage;
	
	public AuthModel(String email,String token)
	{
		this.email=email;
		this.token=token;
	}
	
	public AuthModel(String errorMessage)
	{
		this.errorMessage=errorMessage;
	}
}
