package com.shsxt.server.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Response {
	private BufferedWriter bw;
	private StringBuilder content;
	private StringBuilder headInfo;
	private int len;
	private final String BLANK = " ";
	private final String CRLF = "\r\n";

	private Response() {
		content = new StringBuilder();
		headInfo = new StringBuilder();
		len = 0;
	}

	public Response(Socket client) {
		this();
		try {
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			headInfo = null;
		}
	}

	public Response(OutputStream os) {
		this();
		bw = new BufferedWriter(new OutputStreamWriter(os));
	}

	public Response print(String info) {
		content.append(info);
		len += info.getBytes().length;
		return this;
	}

	public Response println(String info) {
		content.append(info).append(CRLF);
		len += (info + CRLF).getBytes().length;
		return this;
	}

	public void pushToBrowser(int code) throws IOException {
		if (null == headInfo) {
			code = 505;
		}
		createHeadInfo(code);
		bw.append(headInfo);
		bw.append(content);
		bw.flush();
	}

	private void createHeadInfo(int code) {
		headInfo.append("HTTP/1.1").append(BLANK);
		headInfo.append(code).append(BLANK);
		switch (code) {
		case 200:
			headInfo.append("OK").append(CRLF);
			break;
		case 404:
			headInfo.append("NOT FOUND").append(CRLF);
			break;
		case 505:
			headInfo.append("SERVER ERROR").append(CRLF);
			break;
		}

		headInfo.append("Date:").append(new Date()).append(CRLF);
		headInfo.append("Server:").append("shsxt Server/0.0.1;charset=GBK").append(CRLF);
		headInfo.append("Content-type:text/html").append(CRLF);
		headInfo.append("Content-length:").append(len).append(CRLF);
		headInfo.append(CRLF);
	}
}
