package mysql.sec10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionExample {

	public static void main(String[] args) {
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/thisisjava",
					"root",
					"1234"
				);
			

			//트랜잭션 시작 ---------------------------------------
			conn.setAutoCommit(false); //자동 커밋 기능 끄기
			
			//출금
			String sql1 = "UPDATE accounts SET balance= balance-? WHERE ano=?";
			
			PreparedStatement pstmt1 = conn.prepareStatement(sql1);
			pstmt1.setInt(1, 10000);
			pstmt1.setString(2, "111-111-1111");
			int rows1 = pstmt1.executeUpdate();
			if(rows1 == 0) {
				throw new Exception("출금되지 않았음");}
			pstmt1.close();
			
			//입금
			String sql2 = "UPDATE accounts SET balance= balance+? WHERE ano=?";
			
			PreparedStatement pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setInt(1, 10000);
			pstmt2.setString(2, "222-222-2222");
			int rows2 = pstmt2.executeUpdate();
			if(rows2 == 0) {
				throw new Exception("입금되지 않았음");
			}
			pstmt2.close();
			
			conn.commit();
			System.out.println("계좌 이체 성공");
			
			//트랙잭션 종료 ------------------------------			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			try {
				conn.rollback();
				System.out.println("계좌 이체 실패");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			if(conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}	
		
		} //finally ~ 날 알고 감싸준거니! 유 스틸 마 넘버원~! 날 찾지 말아줘~!! 나의 슬픔 가려줘~~~ 저 구름 뒤에 나를 숨겨 빛을 닫아줘~~~
	} //main

} //class
