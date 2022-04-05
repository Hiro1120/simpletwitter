package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import chapter6.beans.Comment;
import chapter6.exception.SQLRuntimeException;

public class UserCommentDao {

	public List<Comment> select(Connection connection, Integer idNum, int num) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append("    comments.id as id, ");
			sql.append("    comments.text as text, ");
			sql.append("    comments.user_id as user_id, ");
			sql.append("    comments.message_id as message_id, ");
			sql.append("    users.account as account, ");
			sql.append("    users.name as name, ");
			sql.append("    comments.created_date as created_date ");
			sql.append("FROM comments ");
			sql.append("INNER JOIN users ");
			sql.append("ON comments.user_id = users.id ");

			sql.append("ORDER BY created_date DESC limit " + num);

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<Comment> comments = toUserComments(rs);
			return comments;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<Comment> toUserComments(ResultSet rs) throws SQLException {

		List<Comment> comments = new ArrayList<Comment>();
		try {
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setId(rs.getInt("id"));
				comment.setText(rs.getString("text"));
				comment.setUserId(rs.getInt("user_id"));
				comment.setAccount(rs.getString("account"));
				comment.setMessageId(rs.getInt("message_id"));
				comment.setName(rs.getString("name"));
				comment.setCreatedDate(rs.getTimestamp("created_date"));

				comments.add(comment);
			}
			return comments;
		} finally {
			close(rs);
		}
	}
}
