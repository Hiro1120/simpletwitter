package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.beans.UserMessage;
import chapter6.dao.MessageDao;
import chapter6.dao.UserMessageDao;

public class MessageService {

	public void insert(Message message) {

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().insert(connection, message);
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

	public List<UserMessage> select(String userId, String startDate, String endDate) {
		final int LIMIT_NUM = 1000;

		Connection connection = null;
		try {
			connection = getConnection();

			Integer id = null;
			if (!StringUtils.isEmpty(userId)) {
				id = Integer.parseInt(userId);
			}

			String startDay = null;
			String endDay = null;

			if (!StringUtils.isEmpty(startDate)) {
				startDay = startDate + " " + "00:00:00";
			} else {
				startDay = "2022-01-01" + " " + "00:00:00";
			}

			if (!StringUtils.isEmpty(endDate)) {
				endDay = endDate + " " + "23:59:59";
			} else {
				Date dt = new Date();
				SimpleDateFormat endDefaultDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				endDay = endDefaultDate.format(dt);
			}

			List<UserMessage> messages = new UserMessageDao().select(connection, id, LIMIT_NUM, startDay, endDay);
			commit(connection);

			return messages;
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

	public void delete(int deleteMessageId) {

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().delete(connection, deleteMessageId);
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

	public Message select(int editMessageId) {

		Connection connection = null;
		try {
			connection = getConnection();
			Message user = new MessageDao().select(connection, editMessageId);
			commit(connection);
			return user;
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

	public void editUpdate(Message message) {

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().editUpdate(connection, message);
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

}
