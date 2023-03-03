package mysql.sec11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BoardExample3 {

	private Scanner scanner = new Scanner(System.in);
	private Connection conn;
	private String loginId; 

	public BoardExample3() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/thisisjava",
					"root",
					"1234"
					);
		} catch (ClassNotFoundException e) {		
			e.printStackTrace();
			exit();
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
		

	}
	public static void main(String[] args) {
		BoardExample3 boardExample = new BoardExample3();
		boardExample.list();

	}
	
	public void list() {
		System.out.println();
		
		if(loginId != null) {
			System.out.println("[게시물 목록]  사용자: "+loginId);
		} else {
			System.out.println("[게시물 목록]  ");
		}
		
		System.out.println("--------------------------------------------------------------------------");
		System.out.printf("%-6s%-12s%-16s%-40s\n","no","writer","date","title");
		System.out.println("--------------------------------------------------------------------------");
		
		
		try {
			String sql = "SELECT bno, btitle, bcontent, bwriter, bdate "
					+"FROM boards ORDER BY bno DESC";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Board board = new Board ();
				board.setBno(rs.getInt("bno"));
				board.setBtitle(rs.getString("btitle"));
				board.setBcontent(rs.getString("bcontent"));
				board.setBwriter(rs.getString("bwriter"));
				board.setBdate(rs.getDate("bdate"));
				System.out.printf("%-6s%-12s%-16s%-40s \n",
						board.getBno(),
						board.getBwriter(),
						board.getBdate(),
						board.getBtitle());
			}			
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
		
		mainMenu();		
	}
	
	public void mainMenu() {
		System.out.println();
		System.out.println("--------------------------------------------------------------------------");
		
		if(loginId != null) {
			System.out.println("메인 메뉴: 1.Creat | 2. Read | 3.Clear | 4. logout | 5. Exit ");
		}else {
			System.out.println("메인 메뉴: 1.Creat | 2. Read | 3.Clear | 4. Join | 5. Login | 6. Exit");			
			
			}
		System.out.print("메뉴 선택:  ");
		String menuNo = scanner.nextLine();
		System.out.println();
		
		if(loginId != null) {
			switch(menuNo) {
			case "1" -> create();
			case "2" -> read();
			case "3" -> clear();
			case "4" -> logout();
			case "5" -> exit();	
			}
		} else {
			switch(menuNo) {
			case "1" -> create();
			case "2" -> read();
			case "3" -> clear();
			case "4" -> join();
			case "5" -> login();
			case "6" -> exit();
			}
		}			
	}
	
	public void create() {
		Board board = new Board();
		System.out.println("[새 게시물 입력]");
		System.out.print("제목: ");
		board.setBtitle(scanner.nextLine());
		System.out.print("내용: ");
		board.setBcontent(scanner.nextLine());
		
		if(loginId != null) {
			board.setBwriter(loginId);		
		}else { 
			System.out.print("작성자: ");
			board.setBwriter(scanner.nextLine());
		}
		
		System.out.println("------------------------");
		System.out.println("보조 메뉴: 1.OK | 2.Cancel");
		System.out.print("메뉴 선택: ");
		String menuNo = scanner.nextLine();
		
		if(menuNo.equals("1")) {
			try {
				String sql = "INSERT INTO boards (btitle, bcontent, bwriter, bdate) "
						+"VALUES (?,?,?,now())";
				
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, board.getBtitle());
				pstmt.setString(2, board.getBcontent());
				pstmt.setString(3, board.getBwriter());
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				exit();
			}
		}//if		
		list();
	}//create()
	
	public void read() {
		System.out.println("[게시물 읽기]");
		System.out.print("bno: ");
		int bno = Integer.parseInt(scanner.nextLine());
		
		
		try {
			String sql = "SELECT bno, btitle, bcontent, bwriter, bdate "
					+"FROM boards WHERE bno = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				Board board = new Board();
				board.setBno(rs.getInt("bno"));
				board.setBtitle(rs.getString("btitle"));
				board.setBcontent(rs.getString("bcontent"));
				board.setBwriter(rs.getString("bwriter"));
				board.setBdate(rs.getDate("bdate"));
				System.out.println("#############");
				System.out.println("번호: " + board.getBno());
				System.out.println("제목: " + board.getBtitle());
				System.out.println("내용: " + board.getBcontent());
				System.out.println("작성자: " + board.getBwriter());
				System.out.println("날짜: " + board.getBdate());
				
				if(board.getBwriter().equals(loginId)) {
					System.out.println("---------------------------------------");
					System.out.println("보조 메뉴 : 1.Update | 2.Delete | 3. List");
					System.out.print("메뉴 선택: ");
					String menuNo = scanner.nextLine();
					System.out.println();
					
					if(menuNo.equals("1")) {
						update(board);
					} else if(menuNo.equals("2")) {
						delete(board);
					}
				}//if(board.getBwriter()~)
			} //if	
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
		list();
	}
	
	public void update(Board board) {
		System.out.println("[수정 내용 입력]");
		System.out.print("제목: ");
		board.setBtitle(scanner.nextLine());
		System.out.print("내용: ");
		board.setBcontent(scanner.nextLine());
		
		
		
		
		System.out.println("---------------------------------------");
		System.out.println("보조 메뉴 : 1.Ok | 2.Cancel ");
		System.out.print("메뉴 선택: ");
		String menuNo = scanner.nextLine();
		if(menuNo.equals("1")) {
			try {
				String sql = "UPDATE boards SET btitle=?, bcontent=? "
						+"WHERE bno=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, board.getBtitle());
				pstmt.setString(2, board.getBcontent());
				pstmt.setInt(3, board.getBno());
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				exit();
			}
		}
		
		list();
		
	}//update ()
	
	public void delete(Board board) {
		try {
			String sql = "DELETE FROM boards WHERE bno = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board.getBno());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
		list();
	}//delete ()
	
	public void clear() {
		System.out.println("[게시물 전체 삭제]");
		System.out.println("---------------------------------------------------");
		System.out.println("보조 메뉴: 1.Ok | 2. Cancel");
		System.out.print("메뉴 선택: ");
		String menuNo = scanner.nextLine();
		
		if(menuNo.equals("1")) {
			try {
				String sql = "TRUNCATE TABLE boards";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				exit();
			}
		}
		list();
	}
	
	public void join() {
		User users = new User();
		System.out.println("[새 사용자 입력]");
		System.out.print("아이디: ");
		users.setUserId(scanner.nextLine());
		System.out.print("이름: ");
		users.setUserName(scanner.nextLine());
		System.out.print("비밀번호: ");
		users.setUserPassword(scanner.nextLine());
		System.out.print("나이: ");
//		users.setUserAge(scanner.nextInt());
		users.setUserAge(Integer.parseInt(scanner.nextLine()));
		System.out.print("이메일: ");
		users.setUserEmail(scanner.nextLine());
		
		System.out.println("---------------------------------------");
		System.out.println("보조 메뉴 : 1.Ok | 2.Cancel ");
		System.out.print("메뉴 선택: ");
		String menuNo = scanner.nextLine();
		if(menuNo.equals("1")) {
			String sql = "INSERT INTO users (userid, username, userpassword, userage, useremail) "
					+"VALUES(?,?,?,?,?)";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,users.getUserId());
				pstmt.setString(2,users.getUserName());
				pstmt.setString(3,users.getUserPassword());
				pstmt.setInt(4,users.getUserAge());
				pstmt.setString(5,users.getUserEmail());
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				exit();
			} 
		}	
		
		list();
	}
	
	public void login() {
		boolean sw = true;
		
		do {
			User users = new User();
			
			System.out.println();
			System.out.println("[로그인]");
			System.out.print("아이디: ");
			users.setUserId(scanner.nextLine());
			System.out.print("비밀번호: ");
			users.setUserPassword(scanner.nextLine());
			
			System.out.println("보조 메뉴: 1.OK | 2. Cancel ");
			System.out.print("메뉴 선택: ");
			String menuNo = scanner.nextLine();
			
			if(menuNo.equals("1")) {
				try {
					String sql = "SELECT userPassword FROM users "
							+"WHERE userid = ?";
	
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, users.getUserId());
					
					ResultSet rs = pstmt.executeQuery();
					if(rs.next()) {
						String password = rs.getString("userPassword");
						if(password.equals(users.getUserPassword())) {
							loginId =  users.getUserId();
							sw = false;
						}else {
							System.out.println("비밀번호가 일치 하지 않습니다.");
						}
					}else {
						System.out.println("아이디가 존재하지 않습니다.");
					}
					
					rs.close();
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					exit();
				}	
			} else {
				sw = false;
			}
		} while(sw);
		list();
	}
	
	
	public void logout() {
		loginId = null;
		
		list();
	}
	
	
	public void exit() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("** 게시판 종료 **");
		System.out.println();
	}
}
