package com.shsxt.server.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
	private String requestInfo;
	private String method;
	private String url;
	private String queryStr;
	private Map<String, List<String>> parameterMap;
	private final String CRLF = "\r\n";

	public Request(Socket client) throws IOException {
		this(client.getInputStream());
	}

	public Request(InputStream is) {
		parameterMap = new HashMap<String, List<String>>();
		byte[] datas = new byte[1024 * 1024 * 1024];
		int len;
		try {
			len = is.read(datas);
			this.requestInfo = new String(datas, 0, len);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		parseRequestInfo();
	}

	private void parseRequestInfo() {
		System.out.println("----Start parsing----");
		System.out.println("----1. Obtain request method----");
		this.method = this.requestInfo.substring(0, this.requestInfo.indexOf("/")).toLowerCase();
		this.method = this.method.trim();
		System.out.println(method);
		System.out.println("----2.Obtain request url----");
		int startIdx = this.requestInfo.indexOf("/") + 1;
		int endIdx = this.requestInfo.indexOf("HTTP/");
		this.url = this.requestInfo.substring(startIdx, endIdx).trim();
		int queryIdx = this.url.indexOf("?");
		if (queryIdx >= 0) {
			String[] urlArray = this.url.split("\\?");
			this.url = urlArray[0];
			queryStr = urlArray[1];
		}
		System.out.println(this.url);

		System.out.println("----3.Obtain parameters----");

		if (method.equals("post")) {
			String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
			System.out.println(qStr + "-->");
			if (null == queryStr) {
				queryStr = qStr;
			} else {
				queryStr += "&" + qStr;
			}
		}
		queryStr = null == queryStr ? "" : queryStr;
		System.out.println(method + "-->" + url + "-->" + queryStr);
		convertMap();
	}

	private void convertMap() {
		String[] keyValues = this.queryStr.split("&");
		for (String queryStr : keyValues) {
			String[] kv = queryStr.split("=");
			kv = Arrays.copyOf(kv, 2);
			String key = kv[0];
			String value = kv[1] == null ? null : decode(kv[1], "utf-8");
			if (!parameterMap.containsKey(key)) {
				parameterMap.put(key, new ArrayList<String>());
			}

			parameterMap.get(key).add(value);
		}
	}

	private String decode(String value, String enc) {
		try {
			return java.net.URLDecoder.decode(value, enc);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String[] getParameterValues(String key) {
		List<String> values = this.parameterMap.get(key);
		if (null == values || values.size() < 1) {
			return null;
		}
		return values.toArray(new String[0]);

	}

	public String getParameter(String key) {
		String[] values = getParameterValues(key);
		return values == null ? null : values[0];
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getQueryStr() {
		return queryStr;
	}

}
