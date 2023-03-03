package mysql.sec09;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSelectExample {

	public static void main(String[] args) {
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/thisisjava",
					"root",
					"1234");
			
			String sql = "SELECT userid, username, userpassword, userage,useremail "
					+ "From users WHERE userid=?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "winter");
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				
//				String userid = rs.getString(1);
//				String username = rs.getString(2);
//				String userpassword= rs.getString(3);
//				int userage = rs.getInt(4);
//				String useremail = rs.getString(5);
//				System.out.println(userid+","+username+","+userpassword+","+userage+","+useremail);
		
				User user = new User();
				user.setUserid(rs.getString(1));
				user.setUserName(rs.getString(2));
				user.setUserPassword(rs.getString(3));
				user.setUserAge(rs.getInt(4));
				user.setUserEmail(rs.getString(5));
				
				System.out.println(user);

			} else {
				System.out.println("사용자 아이디가 존재하지 않음");
			}
			rs.close();
			
			pstmt.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}

}
