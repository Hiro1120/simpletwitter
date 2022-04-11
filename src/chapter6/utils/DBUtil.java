package chapter6.utils;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatDtdDataSet;

import beans.Branch;

public class DBUtil {

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost/simple_twitter";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static Connection getConnection() throws Exception {
		Class.forName(DRIVER);
		Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
		return connection;
	}

	public static void main(String[] args) throws Exception {

		// データベースに接続する。
		IDatabaseConnection connection = new DatabaseConnection(getConnection());

		// Dtdファイルを作成する
		FlatDtdDataSet.write(connection.createDataSet(),
				new FileOutputStream("test.dtd"));
	}

	//◇◇DBUnit用追加機能①参照メソッド
	public static List<Users> allUser() throws Exception {

		Connection connection = null;
		List<Users> usersList = new ArrayList<Users>();

		try {
			connection = getConnection();
			String sql = "SELECT * FROM branch ORDER BY branch_code";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet result = ps.executeQuery();

			while (result.next()) {
				Branch branch = new Branch();
				branch.setBranchCode(result.getString("branch_code"));
				branch.setBranchName(result.getString("branch_name"));

				branchesList.add(branch);
			}

		} finally {
			if (connection != null)
				connection.close();
		}
		return branchesList;

	}

}
