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

import chapter6.beans.Comment;
import chapter6.beans.User;
import chapter6.service.CommentService;

@WebServlet(urlPatterns = { "/comment" })
public class CommentServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		List<String> errorMessages = new ArrayList<String>();
		HttpSession session = request.getSession();

		String text = request.getParameter("text");
		String id = request.getParameter("id");

		if (!isValid(text, errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		Comment comment = new Comment();
		comment.setText(text);
		int commentId = Integer.valueOf(id);
		comment.setId(commentId);

		User user = (User) session.getAttribute("loginUser");
		comment.setUserId(user.getId());

		new CommentService().insert(comment);

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
}