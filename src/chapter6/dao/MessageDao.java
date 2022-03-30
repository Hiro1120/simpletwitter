package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import chapter6.beans.Message;
import chapter6.exception.SQLRuntimeException;

public class MessageDao {

	public void insert(Connection connection, Message message) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO messages ( ");
			sql.append("    user_id, ");
			sql.append("    text, ");
			sql.append("    created_date, ");
			sql.append("    updated_date ");
			sql.append(") VALUES ( ");
			sql.append("    ?, ");
			sql.append("    ?, ");
			sql.append("    CURRENT_TIMESTAMP, ");
			sql.append("    CURRENT_TIMESTAMP ");
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, message.getUserId());
			ps.setString(2, message.getText());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void delete(Connection connection, int deleteMessageId) {

		PreparedStatement ps = null;
		try {
			StringBuilder deleteSql = new StringBuilder();

			deleteSql.append("DELETE FROM messages  ( ");
			deleteSql.append("    WHERE id = ? ");
			deleteSql.append(")");

			ps = connection.prepareStatement(deleteSql.toString());

			ps.setInt(1, deleteMessageId);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

}
