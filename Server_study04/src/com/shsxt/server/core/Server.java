package com.shsxt.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket serverSocket;
	private boolean isRunning;

	public static void main(String[] args) {

		Server server = new Server();
		server.start();
	}

	public void start() {
		try {
			serverSocket = new ServerSocket(8888);
			isRunning = true;
			receive();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Server fails starting...");
			stop();
		}
	}

	public void receive() {
		while (isRunning) {
			try {
				Socket client = serverSocket.accept();
				System.out.println("One client has connected");

				new Thread(new Dispatchers1(client)).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Client error");
			}
		}
	}

	public void stop() {
		isRunning = false;
		try {
			this.serverSocket.close();
			System.out.println("Server is stopped");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
