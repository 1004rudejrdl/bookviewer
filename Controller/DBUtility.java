package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DBUtility {

	public static Connection getConnection() {
		Connection con=null;
		try {
			//1. Mysql database class load
			Class.forName("com.mysql.jdbc.Driver");
			//2. �ּ�, ID, PW�� ���ؼ� ���ӿ�û
			con= DriverManager.getConnection("jdbc:mysql://192.168.0.190/bookdb", "root", "123456");
		} catch (Exception e) {
			e.printStackTrace();
			MainController.callAlert("���� ���� : ����Ÿ���̽� ���� ������ �ֽ��ϴ�.");
			return null;
		}
		return con;
	}
	
}
