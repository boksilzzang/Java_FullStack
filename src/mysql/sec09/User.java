package mysql.sec09;

import lombok.Data;

@Data
public class User {
	
	private String userid;
	private String userName;
	private String userPassword;
	private int userAge;
	private String userEmail;	
	
}
