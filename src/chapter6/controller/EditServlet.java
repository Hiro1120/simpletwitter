package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String messageId = request.getParameter("id");

		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();
		if (!isCheck(messageId, errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		Integer editMessageId = Integer.valueOf(messageId);
		Message message = new MessageService().select(editMessageId);

		request.setAttribute("message", message);

		if (!isCheckNull(message, errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		request.getRequestDispatcher("edit.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();

		String text = request.getParameter("text");
		String id = request.getParameter("id");

		if (!isValid(text, errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		Message message = new Message();
		message.setText(text);
		int messageId = Integer.valueOf(id);
		message.setId(messageId);

		new MessageService().editUpdate(message);
		response.sendRedirect("./");
	}

	private boolean isValid(String text, List<String> errorMessages) {
		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}
		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}

	private boolean isCheck(String messageId, List<String> errorMessages) {
		if (!(messageId.matches("^[0-9]*$")) || StringUtils.isBlank(messageId)) {
			errorMessages.add("不正なパラメータが入力されました");
		}
		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}

	private boolean isCheckNull(Message message, List<String> errorMessages) {
		if (message == null) {
			errorMessages.add("不正なパラメータが入力されました");
		}
		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}
