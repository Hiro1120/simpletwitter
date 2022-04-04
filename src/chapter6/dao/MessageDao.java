package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

			deleteSql.append("DELETE FROM messages ");
			deleteSql.append("WHERE id = ? ");

			ps = connection.prepareStatement(deleteSql.toString());

			ps.setInt(1, deleteMessageId);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<Message> toMessages(ResultSet rs) throws SQLException {

		List<Message> messages = new ArrayList<Message>();
		try {
			while (rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("id"));
				message.setUserId(rs.getInt("user_id"));
				message.setText(rs.getString("text"));
				message.setCreatedDate(rs.getTimestamp("created_date"));
				message.setUpdatedDate(rs.getTimestamp("updated_date"));

				messages.add(message);
			}
			return messages;
		} finally {
			close(rs);
		}
	}

	public Message select(Connection connection, int editMessageId) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM messages WHERE id = ?";

			ps = connection.prepareStatement(sql);

			ps.setInt(1, editMessageId);

			ResultSet rs = ps.executeQuery();
			List<Message> messages = toMessages(rs);
			if (messages.isEmpty()) {
				return null;
			} else if (2 <= messages.size()) {
				throw new IllegalStateException("ユーザーが重複しています");
			} else {
				return messages.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void editUpdate(Connection connection, Message message) {

		PreparedStatement ps = null;
		try {
			StringBuilder updateSql = new StringBuilder();

			updateSql.append("UPDATE messages SET ");
			updateSql.append("    text= ?, ");
			updateSql.append("    updated_date = CURRENT_TIMESTAMP ");
			updateSql.append("WHERE id = ?");

			ps = connection.prepareStatement(updateSql.toString());

			ps.setString(1, message.getText());
			ps.setInt(2, message.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}
