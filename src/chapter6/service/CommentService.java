package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Comment;
import chapter6.dao.CommentDao;
import chapter6.dao.UserCommentDao;

public class CommentService {

	public void insert(Comment reply) {

		Connection connection = null;
		try {
			connection = getConnection();
			new CommentDao().insert(connection, reply);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

	public List<Comment> select(String userId) {
		final int LIMIT_NUM = 1000;

		Connection connection = null;
		try {
			connection = getConnection();

			Integer id = null;
			if (!StringUtils.isEmpty(userId)) {
				id = Integer.parseInt(userId);
			}

			List<Comment> comments = new UserCommentDao().select(connection, id, LIMIT_NUM);
			commit(connection);

			return comments;
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}
}
