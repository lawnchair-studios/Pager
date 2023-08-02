package pala.apps.pager;

import java.io.IOException;
import java.net.ServerSocket;

import pala.libs.generic.io.menus.MenuUtils;
import pala.libs.generic.io.menus.MenuUtils.InputException;

public class PagerServer {

	public static void main(String[] args) throws IOException {
		int port = MenuUtils.inputValue("Enter a port (1-65535): ", a -> {
			int res;
			try {
				res = Integer.parseInt(a);
			} catch (NumberFormatException e) {
				throw new InputException("Port must be a number from 1-65535.");
			}
			if (res < 1 || res > 65535)
				throw new InputException("Port must be from 1-65535.");
			return res;
		});
		try (ServerSocket ss = new ServerSocket(port)) {
			System.out.println("Hosting a server on port: " + port
					+ ".\nSend messages by writing them then hitting enter.\nReceived messages will be written here automatically.");
			while (true)
				Pager.pagerApp(ss::accept);
		}
	}

}
