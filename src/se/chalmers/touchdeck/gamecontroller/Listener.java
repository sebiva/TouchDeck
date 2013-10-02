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

	private final GameController	gameController;
	private ServerSocket			serverSocket;

	private final int				port	= 4242;

	/**
	 * Sets up the listener and starts listening for incoming connections
	 * 
	 * @param gc The gamecontroller in the session
	 */
	public Listener(GameController gc) {
		this.gameController = gc;
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
			gameController.moveCard(op);
			Log.d("handle", "move");
			break;
		case flip:
			gameController.flip(op);
			Log.d("handle", "flip");
			break;
		case create:
			gameController.createPile(op);
			Log.d("handle", "create");
			break;
		case connect:
			gameController.startConnectThread(op);
			Log.d("handle", "connected");
			break;
		case shuffle:
			gameController.shufflePile(op);
			Log.d("handle", "shuffle");
			break;
		case delete:
			gameController.deletePile(op);
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
					Socket clientSocket = serverSocket.accept();
					new Thread(new ConnectionHandler(clientSocket)).start();
					Log.d("network L", "New connection handler started: " + clientSocket.getInetAddress().getHostAddress());
				} catch (IOException e) {
					Log.d("network L", "Could not create client socket");
					return;
				}
			}
		}
	}

	/**
	 * Handles the connection with one gui
	 */
	private class ConnectionHandler implements Runnable {
		private final Socket	clientSocket;

		public ConnectionHandler(Socket s) {
			clientSocket = s;
		}

		@Override
		public void run() {
			// Keep reading the input
			while (true) {
				Log.d("network L", "In loop");
				ObjectInputStream ois = null;
				try {
					ois = (new ObjectInputStream(clientSocket.getInputStream()));
					Log.d("network L", "InputStream created");
				} catch (Exception e) { // TODO Should be IOException
					Log.d("network L", "Exiting ConnectionHandler");
					e.printStackTrace();
					break;// Should be return
				}
				Operation op;
				try {
					// Read the object and handle the operation
					op = (Operation) ois.readObject();
					String ipAddr = clientSocket.getRemoteSocketAddress().toString();
					ipAddr = ipAddr.substring(1, ipAddr.indexOf(":"));
					op.setIpAddr(ipAddr);
					handleOp(op);
					Log.d("network L" + clientSocket.getLocalPort(), "Operation completed" + op.getOp());
				} catch (IOException e) {
					Log.d("network L", "Reading went wrong, IO");
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					Log.d("network L", "Reading went wrong, ClassNotFound");
					e.printStackTrace();
				}
			}
		}
	}
}
