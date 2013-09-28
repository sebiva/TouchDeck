package se.chalmers.touchdeck.gamecontroller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class Listener {

	private final GameController	gc;
	private ServerSocket			serverSocket;
	private Socket					clientSocket;

	public Listener(GameController gc) {
		this.gc = gc;
		new Thread(new AcceptIncoming()).start();
	}

	class AcceptIncoming implements Runnable {

		@Override
		public void run() {
			serverSocket = null;
			try {
				serverSocket = new ServerSocket(4242);
				Log.d("network", "Server socket set up on port 4242");
			} catch (IOException e1) {
			}
			while (true) {
				try {
					clientSocket = serverSocket.accept();
					new Thread(new ConnectionHandler()).start();
				} catch (IOException e) {
					Log.d("network", "Could not create socket on port 4242");
				}
			}
		}

	}

	class ConnectionHandler implements Runnable {

		@Override
		public void run() {
			// BufferedReader in = null;
			ObjectInputStream ois = null;
			try {
				// in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				ois = (new ObjectInputStream(clientSocket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			GameState state;
			try {
				while ((state = (GameState) ois.readObject()) != null) {
					System.out.println("Received: " + state.getPiles().get(10).getCard(0).toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Log.d("network", "Connection established");
		}
	}
}
