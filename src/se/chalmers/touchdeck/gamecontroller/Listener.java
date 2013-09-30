package se.chalmers.touchdeck.gamecontroller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

/**
 * TODO Refactor, Listens to operations from the gui over the network
 * 
 * @author group17
 */
public class Listener {

	private final GameController	gc;
	private ServerSocket			serverSocket;
	private Socket					clientSocket;

	private final int				port	= 4242;

	/**
	 * Sets up the listener and starts listening for incoming connections
	 * 
	 * @param gc The gamecontroller in the session
	 */
	public Listener(GameController gc) {
		this.gc = gc;
		new Thread(new AcceptIncoming()).start();
	}

	/**
	 * Performs the operation requested from the gui
	 * 
	 * @param op The operation to be performed
	 */
	public void handleOp(Operation op) {
		switch (op.getOp()) {
		case move:
			gc.moveCard(op.getPile1(), op.getCardPos(), op.getPile2());
			Log.d("handle", "move");
			break;
		case flip:
			gc.flip(op.getPile1(), op.getCardPos());
			Log.d("handle", "flip");
			break;
		case create:
			gc.createPile(op.getPile1(), op.getName());
			Log.d("handle", "create");
			break;
		case connect:
			gc.startConnectThread(op.getIpAddr());
			Log.d("handle", "connected");
			break;
		default:
		}
	}

	/**
	 * Accepts incomming connections and assigns them a connectionhandler
	 */
	private class AcceptIncoming implements Runnable {

		@Override
		public void run() {
			serverSocket = null;
			// Create the "welcome" socket
			try {
				serverSocket = new ServerSocket(port);
				Log.d("network L", "Server socket set up on port " + port);
			} catch (IOException e1) {
				Log.d("network L", "Server socket could not be set up on port " + port);
				return;
			}
			// Accept all incomming requests to this socket and assign them a connection handler
			while (true) {
				try {
					clientSocket = serverSocket.accept();
					new Thread(new ConnectionHandler()).start();
					Log.d("network L", "New connection handler started: " + clientSocket.getInetAddress().getHostAddress());
				} catch (IOException e) {
					Log.d("network L", "Could not create socket on port " + port);
					return;
				}
			}
		}
	}

	/**
	 * Handles the connection with one gui
	 */
	private class ConnectionHandler implements Runnable {

		@Override
		public void run() {
			// Keep reading the input
			while (true) {
				ObjectInputStream ois = null;
				try {
					ois = (new ObjectInputStream(clientSocket.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				Operation op;
				try {
					// Read the object and handle the operation
					op = (Operation) ois.readObject();
					String ipAddr = clientSocket.getRemoteSocketAddress().toString();
					ipAddr = ipAddr.substring(1, ipAddr.indexOf(":"));
					op.setIpAddr(ipAddr);
					handleOp(op);
					Log.d("network L", "Operation completed");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
