//package chapter6.utils;
//
//import java.io.FileOutputStream;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.dbunit.database.DatabaseConnection;
//import org.dbunit.database.IDatabaseConnection;
//import org.dbunit.dataset.xml.FlatDtdDataSet;
//
//import chapter6.beans.RegisterTest;
//import chapter6.beans.SelectTest;
//import chapter6.beans.UpdataTest;
//
//public class DBUtil_2 {
//
//	private static final String DRIVER = "com.mysql.jdbc.Driver";
//	private static final String URL = "jdbc:mysql://localhost/simple_twitter";
//	private static final String USER = "root";
//	private static final String PASSWORD = "root";
//
//	static {
//		try {
//			Class.forName(DRIVER);
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	private static Connection getConnection() throws Exception {
//		Class.forName(DRIVER);
//		Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//		return connection;
//	}
//
//	public static void main(String[] args) throws Exception {
//
//		// データベースに接続する。
//		IDatabaseConnection connection = new DatabaseConnection(getConnection());
//
//		// Dtdファイルを作成する
//		FlatDtdDataSet.write(connection.createDataSet(),
//				new FileOutputStream("test.dtd"));
//	}
//
//	//◇◇DBUnit用追加機能①参照メソッド
//	public static List<SelectTest> allSelect() throws Exception {
//
//		Connection connection = null;
//		List<SelectTest> selectTestList = new ArrayList<SelectTest>();
//
//		try {
//			connection = getConnection();
//			String sql = "SELECT * FROM users";
//			PreparedStatement ps = connection.prepareStatement(sql);
//
//			ResultSet result = ps.executeQuery();
//			System.out.println(ps.toString());
//			int i = 0;
//			while (result.next()) {
//				if(i<3) {
//				SelectTest selectTest = new SelectTest();
//				selectTest.setAccount(result.getString("account"));
//				selectTest.setId(result.getInt("id"));
//
//				selectTestList.add(selectTest);
//				}
//				i++;
//			}
//
//		} finally {
//			if (connection != null)
//				connection.close();
//		}
//		return selectTestList;
//
//	}
//
//	//◇◇DBUnit用追加機能②更新メソッド
//	public static boolean allRegister(List<RegisterTest> registerTestList) throws Exception {
//
//		Connection connection = null;
//
//		for (int i = 0; i < registerTestList.size(); i++) {
//
//			RegisterTest registerTest = registerTestList.get(i);
//
//			try {
//				connection = getConnection();
//				StringBuilder sql = new StringBuilder();
//				sql.append("INSERT ALL INTO  users ( ");
//				sql.append("account, ");
//				sql.append("name, ");
//				sql.append("email, ");
//				sql.append("password, ");
//				sql.append("description ");
//				sql.append(") VALUES ( ");
//				sql.append("?, ");
//				sql.append("?, ");
//				sql.append("?, ");
//				sql.append("?, ");
//				sql.append("? ");
//				sql.append(") ");
//				sql.append("INTO  messages ( ");
//				sql.append("userId, ");
//				sql.append("text ");
//				sql.append(") VALUES ( ");
//				sql.append("?, ");
//				sql.append("? ");
//				sql.append(")");
//
//				PreparedStatement ps = connection.prepareStatement(sql.toString());
//
//				ps.setString(1, registerTest.getAccount());
//				ps.setString(2, registerTest.getName());
//				ps.setString(3, registerTest.getEmail());
//				ps.setString(4, registerTest.getPassword());
//				ps.setString(5, registerTest.getDescription());
//				ps.setInt(6, registerTest.getUserId());
//				ps.setString(7, registerTest.getText());
//
//				ps.executeUpdate();
//			} finally {
//				try {
//					if (connection != null) {
//						connection.close();
//					}
//				} catch (SQLException e) {
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//
//	//◇◇DBUnit用追加機能②更新メソッド
//	public static boolean allUpdata(List<UpdataTest> updataTestList) throws Exception {
//
//		Connection connection = null;
//
//		for (int i = 0; i < updataTestList.size(); i++) {
//
//			UpdataTest updataTest = updataTestList.get(i);
//
//			try {
//				connection = getConnection();
//				StringBuilder sql = new StringBuilder();
//				sql.append("REPLACE  users SET ");
//				sql.append("account = ?, ");
//				sql.append("name = ?, ");
//				sql.append("email = ?, ");
//				sql.append("password = ?, ");
//				sql.append("description = ?");
//
//				PreparedStatement ps = connection.prepareStatement(sql.toString());
//
//				ps.setString(1, updataTest.getAccount());
//				ps.setString(2, updataTest.getName());
//				ps.setString(3, updataTest.getEmail());
//				ps.setString(4, updataTest.getPassword());
//				ps.setString(5, updataTest.getDescription());
//
//				ps.executeUpdate();
//			} finally {
//				try {
//					if (connection != null) {
//						connection.close();
//					}
//				} catch (SQLException e) {
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//}