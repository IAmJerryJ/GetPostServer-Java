package com.shsxt.user;

import com.shsxt.server.core.Request;
import com.shsxt.server.core.Response;
import com.shsxt.server.core.Servlet;

public class RegisterServlet implements Servlet {

	@Override
	public void service(Request request, Response response) {
		String uname = request.getParameter("uname");
		String[] favs = request.getParameterValues("fav");
		response.print("<html>");
		response.print("<head>");
		response.print("<meta http-equiv=\"content-type\"content=\"text/html;charset=utf-8\">");
		response.print("<title>");
		response.print("Register successfull");
		response.print("</title>");
		response.print("</head>");
		response.print("<body>");
		response.println("Your register info: " + uname);
		response.println("Your fav type is: ");
		for (String v : favs) {
			if (v.equals("0")) {
				response.print("1");
			} else if (v.equals("1")) {
				response.print("2");
			} else if (v.equals("2")) {
				response.print("3");
			}
		}
		response.print("</body>");
		response.print("</html>");

	}

}
