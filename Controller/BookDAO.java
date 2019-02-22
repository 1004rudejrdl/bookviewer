package Controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Book;
import Model.BookDB;
public class BookDAO {
	public static ArrayList<Book> dbArrayList = new ArrayList<>();//????
	 
	//1. �л�����ϴ� �Լ�
	public static int insertBookData(BookDB bookDB) {
		//1.1 ����Ÿ���̽��� �л����̺� �Է��ϴ� ������.
		StringBuffer insertBook = new StringBuffer();
		insertBook.append("insert into booktbl ");
		insertBook.append("(no,title,writer,score,genre,mdate,state,finish,number,image,story,markimage) ");
		insertBook.append("values ");
		insertBook.append("(?,?,?,?,?,?,?,?,?,?,?,?) ");
		//1.2 ����Ÿ���̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		//1.3 �������� �����ؾ��� Statement�� �������Ѵ�.
		PreparedStatement psmt=null;
		int count=0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(insertBook.toString());
			//1.4 �������� ���� ����Ÿ�� �����Ѵ�.
			psmt.setInt(1, bookDB.getNo());
			psmt.setString(2, bookDB.getTitle());
			psmt.setString(3, bookDB.getWriter());
			psmt.setDouble(4, bookDB.getScore());
			psmt.setString(5, bookDB.getGenre());
			psmt.setDate(6, bookDB.getMdate());
			psmt.setString(7, bookDB.getState());
			psmt.setString(8, bookDB.getFinish());
			psmt.setInt(9, bookDB.getNumber());
			psmt.setString(10, bookDB.getImage());
			psmt.setString(11, bookDB.getStroy());
			psmt.setString(12, bookDB.getMarkImage());
			//1.5 ��������Ÿ�� ������ �������� �����Ѵ�.
			count = psmt.executeUpdate();
			if(count==0) {
				MainController.callAlert("���� ���� ���� : ���� �������� �����߽��ϴ�.");
				return count;
			}
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : ����Ÿ���̽� ������ �����߽��ϴ�.");
		} finally {
			//1.6 �ڿ���ü�� �ݾ��ش�.
			try {
				if(psmt != null) { psmt.close(); }
				if(con != null) { con.close(); }
			} catch (SQLException e) { 
				MainController.callAlert("�ڿ� �ݱ� ���� : psmt, con �ݱ� ����.");
			}
		}
		return count;
	}	
	//2. ���̺��� ��ü������ ��� �������� �Լ�
	public static ArrayList<Book> getBookTotalData(){
		//2.1 ����Ÿ���̽��� �л����̺� �ִ� ���ڵ带 ��� �������� ������
				String selectBook = "select * from booktbl";
				//2.2 ����Ÿ���̽� Connection�� �����;� �Ѵ�.
				Connection con = null;
				//2.3 �������� �����ؾ��� Statement�� �������Ѵ�.
				PreparedStatement psmt=null;
				//2.4 �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
				ResultSet rs = null;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(selectBook);
					rs = psmt.executeQuery();
					if(rs==null) {
						MainController.callAlert("select ���� : select �������� �����߽��ϴ�.");
						return null;
					}
					while(rs.next()) {
						Book book = new Book(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getString(5),rs.getDate(6),
								rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10));
						dbArrayList.add(book);
					}
				} catch (SQLException e) {
					MainController.callAlert("���� ���� : ����Ÿ���̽� ������ �����߽��ϴ�.");
				} finally {
					//2.6 �ڿ���ü�� �ݾ��ش�.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("�ڿ� �ݱ� ���� : psmt, con �ݱ� ����.");
					}
				}
		
		return dbArrayList;
	}	
	//3. ���̺�信�� ������ ���ڵ带 ����Ÿ���̽����� �����ϴ� �Լ�
	public static int deleteBookData(int no) {
		//3.1 ����Ÿ���̽��� �л����̺� �ִ� ���ڵ带 �����ϴ� ������
		String deleteBook = "delete from booktbl where no = ? ";
		//3.2 ����Ÿ���̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		//3.3 �������� �����ؾ��� Statement�� �������Ѵ�.
		PreparedStatement psmt=null;
		//3.4 �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		int count=0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(deleteBook);
			psmt.setInt(1, no);
			//3.5 ��������Ÿ�� ������ �������� �����Ѵ�.(������ ġ�� ��)
			//executeQuery() �������� �����ؼ� ����� �����ö� ����ϴ� ������
			//executeUpdate() ��������  �����ؼ� ���̺� ������ �Ҷ� ����ϴ� ������
			count = psmt.executeUpdate();
			if(count==0) {
				MainController.callAlert("delete ���� : delete �������� �����߽��ϴ�.");
				return count;
			}
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : ����Ÿ���̽� ������ �����߽��ϴ�.");
		} finally {
			//3.6 �ڿ���ü�� �ݾ��ش�.
			try {
				if(psmt != null) { psmt.close(); }
				if(con != null) { con.close(); }
			} catch (SQLException e) { 
				MainController.callAlert("�ڿ� �ݱ� ���� : psmt, con �ݱ� ����.");
			}
		}
		return count;
	}
	//4. ���̺�信�� ������ ���ڵ带 ����Ÿ���̽� ���̺� �����ϴ��Լ�.
	public static int updateBookData(BookDB bookDB) {
		//4.1 ����Ÿ���̽��� �л����̺� �����ϴ� ������
				StringBuffer updateStudent = new StringBuffer();
				updateStudent.append("update booktbl set ");
				updateStudent.append("score=?,finish=?,number=? where no=? ");
			
				//4.2 ����Ÿ���̽� Connection�� �����;� �Ѵ�.
				Connection con = null;
				//4.3 �������� �����ؾ��� Statement�� �������Ѵ�.
				PreparedStatement psmt=null;
				int count=0;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(updateStudent.toString());
					//4.4 �������� ���� ����Ÿ�� �����Ѵ�.
					psmt.setDouble(1, bookDB.getScore());
					psmt.setString(2, bookDB.getFinish());
					psmt.setInt(3, bookDB.getNumber());
					psmt.setInt(4, bookDB.getNo());
					
					//4.5 ��������Ÿ�� ������ �������� �����Ѵ�.
					count = psmt.executeUpdate();
					if(count==0) {
						MainController.callAlert("���� ���� ���� : ���� �������� �����߽��ϴ�.");
						return count;
					}
				} catch (SQLException e) {
					MainController.callAlert("���� ���� : ����Ÿ���̽� ������ �����߽��ϴ�.");
				} finally {
					//4.6 �ڿ���ü�� �ݾ��ش�.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("�ڿ� �ݱ� ���� : psmt, con �ݱ� ����.");
					}
				}
		return count;
	}
	//5 �����ͺ��̽����� story ������ ������ ���� �Լ�
	public static String getBookStoryData(int no){
		String string=null;
				String selectBook = "select story from booktbl where no =" + no ;
				//5.2 ����Ÿ���̽� Connection�� �����;� �Ѵ�.
				Connection con = null;
				//5.3 �������� �����ؾ��� Statement�� �������Ѵ�.
				PreparedStatement psmt=null;
				//5.4 �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
				ResultSet rs = null;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(selectBook);
					
					//5.5 ��������Ÿ�� ������ �������� �����Ѵ�.(������ ġ�� ��)
					rs = psmt.executeQuery();
					
					if(rs==null) {
						MainController.callAlert("select ���� : select �������� �����߽��ϴ�.");
						return null;
					}
					while(rs.next()) {
						string = rs.getString("story");
					}
				} catch (SQLException e) {
					MainController.callAlert("���� ���� : ����Ÿ���̽� ������ �����߽��ϴ�.");
				} finally {
					//5.6 �ڿ���ü�� �ݾ��ش�.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("�ڿ� �ݱ� ���� : psmt, con �ݱ� ����.");
					}
				}
		
		return string;
	}
	//6 �����ͺ��̽����� �帣�� ī��Ʈ���� ������ ���� �Լ�
	public static int getBookGenreData(String genre){
		int count =0;
				String selectBook = "select count(genre) from booktbl where genre =" +  "'"+genre+"'";
				//6.2 ����Ÿ���̽� Connection�� �����;� �Ѵ�.
				Connection con = null;
				//6.3 �������� �����ؾ��� Statement�� �������Ѵ�.
				PreparedStatement psmt=null;
				//6.4 �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
				ResultSet rs = null;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(selectBook);
					
					//6.5 ��������Ÿ�� ������ �������� �����Ѵ�.(������ ġ�� ��)
					rs = psmt.executeQuery();
					
					if(rs==null) {
						MainController.callAlert("select ���� : select �������� �����߽��ϴ�.");
						return 0;
					}
					while(rs.next()) {
						count = rs.getInt("count(genre)");
					}
				} catch (SQLException e) {
					MainController.callAlert("���� ���� : ����Ÿ���̽� ������ �����߽��ϴ�.");
				} finally {
					//6.6 �ڿ���ü�� �ݾ��ش�.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("�ڿ� �ݱ� ���� : psmt, con �ݱ� ����.");
					}
				}
		
		return count;
	}
	//7. �ϸ�ũ ����� �ش� ��Ϲ�ȣ�� markImage ���̺� ���� �����ϴ� �Լ�
	public static int updateMarkImageData(BookDB bookDB) {
		//7.1 ����Ÿ���̽��� �����̺� �����ϴ� ������
				StringBuffer updateStudent = new StringBuffer();
				updateStudent.append("update booktbl set ");
				updateStudent.append("markimage=? where no=? ");
			
				//7.2 ����Ÿ���̽� Connection�� �����;� �Ѵ�.
				Connection con = null;
				//7.3 �������� �����ؾ��� Statement�� �������Ѵ�.
				PreparedStatement psmt=null;
				int count=0;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(updateStudent.toString());
					//7.4 �������� ���� ����Ÿ�� �����Ѵ�.
					psmt.setString(1, bookDB.getMarkImage());
					psmt.setInt(2, bookDB.getNo());
					
					//7.5 ��������Ÿ�� ������ �������� �����Ѵ�.
					count = psmt.executeUpdate();
					if(count==0) {
						MainController.callAlert("���� ���� ���� : ���� �������� �����߽��ϴ�.");
						return count;
					}
				} catch (SQLException e) {
					MainController.callAlert("���� ���� : ����Ÿ���̽� ������ �����߽��ϴ�.");
				} finally {
					//7.6 �ڿ���ü�� �ݾ��ش�.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("�ڿ� �ݱ� ���� : psmt, con �ݱ� ����.");
					}
				}
		return count;
	}
	//8 �����ͺ��̽����� markImage�� �������� �Լ�
	public static String getBookMarkImageData(int no){
		String mark =null;
				String selectBook = "select markimage from booktbl where no = "+no;
				//8.2 ����Ÿ���̽� Connection�� �����;� �Ѵ�.
				Connection con = null;
				//8.3 �������� �����ؾ��� Statement�� �������Ѵ�.
				PreparedStatement psmt=null;
				//8.4 �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
				ResultSet rs = null;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(selectBook);
					//8.5 ��������Ÿ�� ������ �������� �����Ѵ�.(������ ġ�� ��)
					rs = psmt.executeQuery();
					if(rs==null) {
						MainController.callAlert("select ���� : select �������� �����߽��ϴ�.");
						return null;
					}
					while(rs.next()) {
						mark = rs.getString("markImage");
					}
				} catch (SQLException e) {
					MainController.callAlert("���� ���� : ����Ÿ���̽� ������ �����߽��ϴ�.");
				} finally {
					//8.6 �ڿ���ü�� �ݾ��ش�.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("�ڿ� �ݱ� ���� : psmt, con �ݱ� ����.");
					}
				}
		
		return mark;
	}
}
