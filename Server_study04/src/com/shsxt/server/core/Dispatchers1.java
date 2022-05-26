package com.shsxt.server.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Dispatchers1 implements Runnable {
	private Socket client;
	private Request request;
	private Response response;

	public Dispatchers1(Socket client) {
		// TODO Auto-generated constructor stub
		this.client = client;
		try {
			request = new Request(client);
			response = new Response(client);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.release();
		}
	}

	@Override
	public void run() {
		try {
			if (null == request.getUrl() || request.getUrl().equals("")) {
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("index.html");
				response.print(new String(is.readAllBytes()));
				response.pushToBrowser(200);
				is.close();
//				return;
			}
			Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
			if (null != servlet) {

				servlet.service(request, response);
				response.pushToBrowser(200);
			} else {
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("error.html");
				response.print(new String(is.readAllBytes()));
				response.pushToBrowser(404);
				is.close();
			}
		} catch (Exception e) {
			try {
				response.println("I will be right back");
				response.pushToBrowser(500);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		release();
	}

	private void release() {
		try {
			client.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
