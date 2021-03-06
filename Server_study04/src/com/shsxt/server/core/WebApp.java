package com.shsxt.server.core;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class WebApp {
	private static WebContext webContext;
	static {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parse = factory.newSAXParser();
			WebHandler handler = new WebHandler();
			parse.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml"), handler);
			webContext = new WebContext(handler.getEntitys(), handler.getMappings());
		} catch (Exception e) {
			System.out.println("Parsing error");
		}
	}

	public static Servlet getServletFromUrl(String url) {

		String className = webContext.getClz("/" + url);
		Class clz;
		try {
			clz = Class.forName(className);
			Servlet servlet = (Servlet) clz.getConstructor().newInstance();
			return servlet;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
