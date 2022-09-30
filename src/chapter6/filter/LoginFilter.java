package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;


@WebFilter(urlPatterns = { "/edit", "/setting" })
public class LoginFilter implements Filter {

	public static String INIT_PARAMETER_NAME_ENCODING = "encoding";

	public static String DEFAULT_ENCODING = "UTF-8";

	private String encoding;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		User user = (User) ((HttpServletRequest) request).getSession().getAttribute("loginUser");
		List<String> errorMessages = new ArrayList<String>();
		HttpSession session = ((HttpServletRequest) request).getSession();

		if (user != null) {
			chain.doFilter(request, response);
		} else {
			errorMessages.add("ログインしてください");
			session.setAttribute("errorMessages", errorMessages);
			((HttpServletResponse) response).sendRedirect("./login");
		}

	}

	@Override
	public void init(FilterConfig config) {
		encoding = config.getInitParameter(INIT_PARAMETER_NAME_ENCODING);
		if (encoding == null) {
			System.out.println("EncodingFilter# デフォルトのエンコーディング(UTF-8)を利用します。");
			encoding = DEFAULT_ENCODING;
		} else {
			System.out.println("EncodingFilter# 設定されたエンコーディング(" + encoding
					+ ")を利用します。。");
		}
	}

	@Override
	public void destroy() {
	}

}