package chapter6.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.Comment;
import chapter6.beans.User;
import chapter6.service.CommentService;

@WebServlet(urlPatterns = { "/comment" })
public class CommentServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();

		String text = request.getParameter("text");
		String id = request.getParameter("id");

		Comment reply = new Comment();
		reply.setText(text);
		int replyId = Integer.valueOf(id);
		reply.setId(replyId);

		User user = (User) session.getAttribute("loginUser");
		reply.setUserId(user.getId());

		new CommentService().insert(reply);
		response.sendRedirect("./");
	}
}