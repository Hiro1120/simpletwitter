package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import chapter6.beans.Comment;
import chapter6.exception.SQLRuntimeException;

public class CommentDao {

	public void insert(Connection connection, Comment reply) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO comments ( ");
			sql.append("    text, ");
			sql.append("    user_id, ");
			sql.append("    message_id, ");
			sql.append("    created_date, ");
			sql.append("    updated_date ");
			sql.append(") VALUES ( ");
			sql.append("    ?, ");
			sql.append("    ?, ");
			sql.append("    ?, ");
			sql.append("    CURRENT_TIMESTAMP, ");
			sql.append("    CURRENT_TIMESTAMP ");
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, reply.getText());
			ps.setInt(2, reply.getUserId());
			ps.setInt(3, reply.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}