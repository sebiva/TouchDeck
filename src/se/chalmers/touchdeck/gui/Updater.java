package se.chalmers.touchdeck.gui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Updater implements Runnable {

	public Updater() {

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	class ClientThread implements Runnable {

		@Override
		public void run() {

			try {
				InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
				// et.setText("IP: " + adr);
				Socket socket = new Socket(serverAddr, 4242);

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}
}
