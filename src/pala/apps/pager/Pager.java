package pala.apps.pager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import pala.libs.generic.io.menus.MenuUtils;
import pala.libs.generic.io.menus.MenuUtils.InputException;
import pala.libs.generic.util.FallibleSupplier;

public class Pager {

	public static void pagerApp(FallibleSupplier<? extends IOException, ? extends Socket> conSupp) {
		try (Socket con = conSupp.get()) {
			System.out.println(
					"Connection succeeded.\nWrite a message then hit enter to send.\nMessages received will be shown automatically.");
			Thread listener = new Thread(() -> {
				try (Socket sock = con) {
					Scanner in = new Scanner(sock.getInputStream());
					while (in.hasNextLine())
						System.out.println("[" + sock.getInetAddress() + ':' + sock.getPort() + "]: " + in.nextLine());
				} catch (IOException e) {
					System.err.println("[IO ERR] " + e.getLocalizedMessage());
				}
			});
			listener.start();
			PrintWriter pw = new PrintWriter(con.getOutputStream());
			while (true) {
				String l = MenuUtils.getDefaultScanner().nextLine();
				if (l.equalsIgnoreCase("!stop"))
					return;
				pw.println(l);
				pw.flush();
			}
		} catch (IOException e) {
			System.err.println("[IO ERR] " + e.getLocalizedMessage());
		}
	}

	public static void main(String[] args) {
		var endpoint = MenuUtils.inputValue("Enter an endpoint (IP, web-address, or blank for localhost): ", a -> {
			try {
				return InetAddress.getByName(a);
			} catch (UnknownHostException e) {
				throw new InputException("Could not resolve host from provided input.");
			}
		});
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

		System.out.println("Connecting to " + endpoint + ':' + port + "...");

		while (true)
			pagerApp(() -> new Socket(endpoint, port));
	}

}
