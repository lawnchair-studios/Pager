package pala.apps.pager;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import pala.libs.generic.io.menus.MenuPrompter;
import pala.libs.generic.io.menus.MenuUtils;
import pala.libs.generic.networking.sockets.Server;

import static pala.libs.generic.io.menus.MenuUtils.*;

public class Pager {

	public static void main(String[] args) {
		Scanner s = MenuUtils.getDefaultScanner();
		var mp = new MenuPrompter(s, System.out);

		while (true) {
			int res = mp.prompt("Select an option: ", "Listen for incoming messages", "Send message",
					"Send series of messages", "Exit the program");
			switch (res) {
			case 1:
				try {
					listenForIncomingMessages();
				} catch (IOException e) {
					System.err.print(
							"Failed to listen to incoming messages. Error message:\n\t" + e.getLocalizedMessage());
				}
				break;
			case 2:
				sendMessage();
				break;
			case 3:
				sendMessageSeries();
				break;
			case 4:
				return;
			}
		}
	}

	private static void listenForIncomingMessages() throws IOException {
		int port = inputNumber("Enter a port: ", "That's not a number.");

		Server serv = new Server(new ServerSocket());
	}

	private static void sendMessage() {

	}

	private static void sendMessageSeries() {

	}

}
