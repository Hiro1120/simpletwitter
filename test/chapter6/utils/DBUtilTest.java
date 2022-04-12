package chapter6.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

import chapter6.beans.Message;
import chapter6.beans.User;
import chapter6.beans.UserMessage;
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

			file = File.createTempFile("test", ".xml");
			FlatXmlDataSet.write(partialDataSet,
					new FileOutputStream(file));

			//(3)テストデータを投入する
			IDataSet dataSetUsers = new FlatXmlDataSet(new File("test.ver.xml"));
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
	//つぶやきの登録メソッドのテスト①(任意のメッセージを登録)
	public void registMessage() throws Exception {

		//テスト対象となる、storeメソッドを実行
		//テストのインスタンスを生成
		Message result001 = new Message();
		result001.setId(2);
		result001.setUserId(2);
		result001.setText("テスト：新しく登録されたよ");

		new MessageService().insert(result001);

		//テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
		IDatabaseConnection connection = null;
		try {

			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			//メソッド実行した実際のテーブル
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("messages");

			// テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
			IDataSet expectedDataSet = new FlatXmlDataSet(new File("RegistMessage.xml"));
			ITable expectedTable = expectedDataSet.getTable("messages");

			//期待されるITableと実際のITableの比較
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			if (connection != null)
				connection.close();
		}

	}

	public void selectMessage() throws Exception {
		//ユーザーごとのつぶやきの参照メソッドのテスト②（存在するユーザーIDで紐づくメッセージを取得）
		List<UserMessage> selectMessageList = new MessageService().select("1", "2022-01-01", "2022-04-01");
		//値の検証
		//件数
		assertEquals(2, selectMessageList.size());
		//データ
		UserMessage result001 = selectMessageList.get(0);
		assertEquals("id=1", "id=" + result001.getId());
		assertEquals("text=テスト：３回目の投稿です", "text=" + result001.getText());
	}

	//ユーザーの登録メソッドのテスト③
	public void registUser() throws Exception {

		//テスト対象となる、storeメソッドを実行
		//テストのインスタンスを生成
		User result001 = new User();
		result001.setId(3);
		result001.setAccount("Wario");
		result001.setName("ワリオ");
		result001.setEmail("Wario.co.jp");
		result001.setPassword("wario");
		result001.setDescription("登録されたワリオです");

		new UserService().insert(result001);

		//テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
		IDatabaseConnection connection = null;
		try {

			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			//メソッド実行した実際のテーブル
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("users");

			// テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
			IDataSet expectedDataSet = new FlatXmlDataSet(new File("InsertUser.xml"));
			ITable expectedTable = expectedDataSet.getTable("users");

			//期待されるITableと実際のITableの比較
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			if (connection != null)
				connection.close();
		}

	}

	//ユーザーの更新メソッドのテスト④
	@Test
	public void UpdataUser() throws Exception {

		//テスト対象となる、storeメソッドを実行
		//テストのインスタンスを生成
		User result001 = new User();
		result001.setId(1);
		result001.setAccount("New Luigi");
		result001.setName("ニュー ルイージ");
		result001.setEmail("New_Luigi.co.jp");
		result001.setPassword("newluigi");
		result001.setDescription("更新されたルイージです");

		new UserService().update(result001);

		//テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
		IDatabaseConnection connection = null;
		try {

			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			//メソッド実行した実際のテーブル
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("users, messages");

			// テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
			IDataSet expectedDataSet = new FlatXmlDataSet(new File("UpdataUser.xml"));
			ITable expectedTable = expectedDataSet.getTable("users");

			//期待されるITableと実際のITableの比較
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			if (connection != null)
				connection.close();
		}

	}

	//存在するアカウントで紐づくユーザーの取得メソッドのテスト⑤
	public void selectUser() throws Exception {

		User result001 = new User();
		result001.setId(2);
		result001.setAccount("Mario");
		result001.setName("マリオ");
		result001.setEmail("mario.co.jp");
		result001.setPassword("mario1");
		result001.setDescription("全準備されたマリオです");

		new UserService().select(result001.getAccount());

		//テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
		IDatabaseConnection connection = null;
		try {

			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			//メソッド実行した実際のテーブル
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("users");

			// テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
			IDataSet expectedDataSet = new FlatXmlDataSet(new File("SelectUser.xml"));
			ITable expectedTable = expectedDataSet.getTable("users");

			//期待されるITableと実際のITableの比較
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			if (connection != null)
				connection.close();
		}
	}
}
