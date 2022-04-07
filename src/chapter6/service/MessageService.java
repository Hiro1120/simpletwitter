package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.text.DateFormat;
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

	public List<UserMessage> select(String userId, String startDay, String endDay) {
		final int LIMIT_NUM = 1000;

		Connection connection = null;
		try {
			connection = getConnection();

			Integer id = null;
			if (!StringUtils.isEmpty(userId)) {
				id = Integer.parseInt(userId);
			}

			Date dt = new Date(); //現在日時の取得
			DateFormat df = DateFormat.getDateInstance(); //DateFormatの取得
			String startDefaultDate = "2020/01/01 00:00:00";
			String endDefaultDate = df.format(dt); //formatメソッドを用いて文字列に変換(現在日時）
			String startDate;
			String endDate;

			if (startDay != null) {
				startDate = startDay + "00:00:00";
			} else {
				startDate = startDefaultDate;
			}

			if (endDay != null) {
				endDate = endDay + "23:59:59";
			} else {
				endDate = endDefaultDate;
			}

			List<UserMessage> messages = new UserMessageDao().select(connection, id, LIMIT_NUM, startDate, endDate);
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
