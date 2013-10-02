package se.chalmers.touchdeck.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

import android.util.Log;

public abstract class ListenerInterface extends Observable implements Runnable {
	private final boolean	mLoopForever;
	private ServerSocket	mServerSocket;

	private final int		mPort;

	public ListenerInterface(boolean loopForever, int port) {
		mLoopForever = loopForever;
		mPort = port;
	}

	@Override
	public void run() {
		mServerSocket = null;
		// Create the "welcome" socket
		try {
			mServerSocket = new ServerSocket(mPort);
			Log.d("ListenerInt", "Server socket set up on port " + mPort);
		} catch (IOException e1) {
			Log.d("ListenerInt", "Server socket could not be set up on port " + mPort);
			return;
		}
		// Accept all incomming requests to this socket and assign them a connection handler
		do {
			try {
				Socket clientSocket = mServerSocket.accept();
				new Thread(new ConnectionHandler(clientSocket)).start();
				Log.d("ListenerInt", "New connection handler started: " + clientSocket.getInetAddress().getHostAddress());
			} catch (IOException e) {
				Log.d("ListenerInt", "Could not create client socket");
				return;
			}
		} while (mLoopForever);
	}

	private class ConnectionHandler implements Runnable {
		private final Socket	clientSocket;

		public ConnectionHandler(Socket s) {
			clientSocket = s;
		}

		// TODO Error handling
		@Override
		public void run() {
			// Keep reading the input
			while (true) {
				Log.d("ListenerInt", "In loop");
				ObjectInputStream ois = null;
				try {
					ois = (new ObjectInputStream(clientSocket.getInputStream()));
					Log.d("ListenerInt", "InputStream created");
				} catch (IOException e) {
					Log.d("ListenerInt", "Exiting ConnectionHandler");
					e.printStackTrace();
					break;// Should be return
				}
				Serializable op;
				try {
					// Read the object and handle the operation
					op = (Serializable) ois.readObject();
					String ipAddr = clientSocket.getRemoteSocketAddress().toString();
					ipAddr = ipAddr.substring(1, ipAddr.indexOf(":"));
					handle(op, ipAddr);
					Log.d("ListenerInt" + clientSocket.getLocalPort(), "Operation completed");
				} catch (IOException e) {
					Log.d("ListenerInt", "Reading went wrong, IO");
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					Log.d("ListenerInt", "Reading went wrong, ClassNotFound");
					e.printStackTrace();
				}
			}
		}
	}

	public abstract void handle(Serializable s, String ipAddr);
}
