package se.chalmers.touchdeck.gui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

import se.chalmers.touchdeck.gamecontroller.GameState;
import android.util.Log;

/**
 * TODO Refactor, Class for getting the changes from the gameController over the network to the guiController
 * 
 * @author group17
 */
public class Updater extends Observable {

	private ServerSocket	serverSocket;
	private Socket			clientSocket;
	private final int		port	= 4243;
	private GameState		gs;

	/**
	 * Create a new Updater
	 * 
	 * @param gc The associated gui
	 */
	public Updater(GuiController gc) {
		addObserver(gc);
		new Thread(new AcceptIncoming()).start();
	}

	public void close() {
		try {
			serverSocket.close();
			clientSocket.close();
			Log.d("close", "GuC in sockets closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Accept the incomming request from the gameController
	 * 
	 * @author or3x
	 */
	private class AcceptIncoming implements Runnable {

		@Override
		public void run() {
			serverSocket = null;
			try {
				// Set up a socket that the gameController can connect to
				serverSocket = new ServerSocket(port);
				Log.d("network U", "Server socket set up on port " + port);
			} catch (IOException e1) {
				Log.d("network U", "Server socket could not be set up on port " + port);
			}
			try {
				// Wait for the game controller to connect and give it a thread for handling its updates
				clientSocket = serverSocket.accept();
				new Thread(new ConnectionHandler()).start();
				Log.d("network U", "New connection handler started");
			} catch (IOException e) {
				Log.d("network U", "Could not create socket on port " + port);
				return;
			}
		}
	}

	/**
	 * Handles the updates received from the gameController
	 */
	private class ConnectionHandler implements Runnable {

		@Override
		public void run() {
			// Keep getting updates
			while (true) {
				ObjectInputStream ois = null;
				try {
					ois = (new ObjectInputStream(clientSocket.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					// Read the GameState
					gs = (GameState) ois.readObject();
					if (gs != null) {
						setChanged();
						notifyObservers(gs);
					}
					Log.d("network U", "New State received ");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
