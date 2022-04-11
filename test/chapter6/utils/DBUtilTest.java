package chapter6.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DBUtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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

			file = File.createTempFile("messages", "users", ".xml");
			FlatXmlDataSet.write(partialDataSet,
					new FileOutputStream(file));

			//(3)テストデータを投入する
			IDataSet dataSetUsers = new FlatXmlDataSet(new File("simple_twitter_test_data2.xml"));
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

	}

	@Test
	public void test() {
		//参照メソッドの実行
		List<Branch> resultList = BranchDbUtil.allBranch();
		//値の検証
		//件数
		assertEquals(3, resultList.size());
		//データ
		Branch result001 = resultList.get(0);
		assertEquals("branchCode=001", "branchCode=" + result001.getBranchCode());
		assertEquals("branchName=札幌支店", "branchName=" +
				result001.getBranchName());
		Branch result002 = resultList.get(1);
		assertEquals("branchCode=002", "branchCode=" + result002.getBranchCode());
		assertEquals("branchName=北極支店", "branchName=" +
				result002.getBranchName());
		Branch result003 = resultList.get(2);
		assertEquals("branchCode=003", "branchCode=" + result003.getBranchCode());
		assertEquals("branchName=南極支店", "branchName=" +
				result003.getBranchName());
	}
}

}
