package chapter6.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chapter6.beans.UserMessage;
import chapter6.beans.Message;
import chapter6.beans.RegisterTest;
import chapter6.beans.SelectTest;
import chapter6.beans.UpdataTest;
import chapter6.beans.User;
import chapter6.service.MessageService;
import chapter6.service.UserService;
import junit.framework.TestCase;

public class DBUtilTest extends TestCase {

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost/test";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	public DBUtilTest(String name) {
		super(name);
	}

	private File file;

	//DB接続部分(DBUtil)
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

	@Before
	public void setUp() throws Exception {
		//(1)IDatabaseConnectionを取得
		IDatabaseConnection connection = null;
		try {
			super.setUp();
			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			//(2)現状のバックアップを取得
			QueryDataSet partialDataSet = new QueryDataSet(connection);
			partialDataSet.addTable("messages");
			partialDataSet.addTable("users");

			file = File.createTempFile("users", ".xml");
			FlatXmlDataSet.write(partialDataSet,
					new FileOutputStream(file));

			//(3)テストデータを投入する
			IDataSet dataSetUsers = new FlatXmlDataSet(new File("messageRegister.xml"));
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSetUsers);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}
	}

	@After
	public void tearDown() throws Exception {

		IDatabaseConnection connection = null;
		try {
			super.tearDown();
			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			IDataSet dataSet = new FlatXmlDataSet(file);
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			//一時ファイルの削除
			if (file != null) {
				file.delete();
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}

		}

	}

	@Test
	public void messageSelect() throws Exception {
		//参照メソッドの実行
		List<UserMessage> messageSelectTestList = MessageService.select(String userId, String startDate, String endDate);
		//値の検証
		//件数
		assertEquals(2, messageSelectTestList.size());
		//データ
		UserMessage result001 = messageSelectTestList.get(0);
		assertEquals("user_id=2", "user_id=" + result001.getUserId());
		assertEquals("text=テスト：３回目の投稿です", "text=" + result001.getText());
	}

	public void userSelect() throws Exception {
		//参照メソッドの実行
		List<User> userSelectTestList = UserService.select(String account) ;
		//値の検証
		//件数
		assertEquals(5, userSelectTestList.size());
		//データ
		User result002 = userSelectTestList.get(0);
		assertEquals("account=java1", "account=" + result002.getAccount());
		assertEquals("name=ジャヴァ1", "name=" + result002.getName());
		assertEquals("email=java1.co.jp", "email=" + result002.getEmail());
		assertEquals("password=java1", "password=" + result002.getPassword());
		assertEquals("description=java1になりました", "description=" + result002.getDescription());
	}

	/**
	 * 更新メソッドのテスト
	 */
	@Test
	public void messageRegister() throws Exception {

		//テスト対象となる、storeメソッドを実行
		//テストのインスタンスを生成
		Message result001 = new Message();
		result001.setAccount("nakayama1");
		result001.setName("中山1");
		result001.setEmail("nakayama1.co.jp");
		result001.setPassword("nakayama1");
		result001.setDescription("nakayama1です");
		result001.setUserId(1);
		result001.setText("実践課題1");

		List<Message> messageRegisterSetList = new ArrayList<Message>();
		messageRegisterSetList.add(result001);

		MessageService.insert(messageRegisterSetList);

		//テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
		IDatabaseConnection connection = null;
		try {

			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			//メソッド実行した実際のテーブル
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("users, messages");

			// テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
			IDataSet expectedDataSet = new FlatXmlDataSet(new File("simple_twitter_test_data2.xml"));
			ITable expectedTable = expectedDataSet.getTable("users, messages");

			//期待されるITableと実際のITableの比較
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			if (connection != null)
				connection.close();
		}

	}

	public void testAllUpdata() throws Exception {

		//テスト対象となる、storeメソッドを実行
		//テストのインスタンスを生成
		UpdataTest result001 = new UpdataTest();
		result001.setAccount("java1");
		result001.setName("ジャヴァ1");
		result001.setEmail("java1.co.jp");
		result001.setPassword("java1");
		result001.setDescription("java1になりました");

		UpdataTest result002 = new UpdataTest();
		result002.setAccount("java2");
		result002.setName("ジャヴァ2");
		result002.setEmail("java2.co.jp");
		result002.setPassword("java2");
		result002.setDescription("java2になりました");

		List<UpdataTest> UpdataSetList = new ArrayList<UpdataTest>();
		UpdataSetList.add(result001);
		UpdataSetList.add(result002);

		DBUtil_2.allUpdata(UpdataSetList);

		//テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
		IDatabaseConnection connection = null;
		try {

			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			//メソッド実行した実際のテーブル
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("users");

			// テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
			IDataSet expectedDataSet = new FlatXmlDataSet(new File("simple_twitter_test_data3.xml"));
			ITable expectedTable = expectedDataSet.getTable("users, messages");

			//期待されるITableと実際のITableの比較
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			if (connection != null)
				connection.close();
		}

	}

}
