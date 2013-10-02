//package se.chalmers.touchdeck.gui;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.Observable;
//
//import se.chalmers.touchdeck.gamecontroller.GameState;
//import android.util.Log;
//
///**
// * TODO Refactor, Class for getting the changes from the gameController over the network to the guiController
// * 
// * @author group17
// */
//public class Updater extends Observable {
//
//	private ServerSocket	mServerSocket;
//	private Socket			mClientSocket;
//	private final int		mPort	= 4243;
//
//	/**
//	 * Create a new Updater
//	 * 
//	 * @param gc The associated gui
//	 */
//	public Updater(GuiController gc) {
//		addObserver(gc);
//		new Thread(new AcceptIncoming()).start();
//	}
//
//	// public void close() {
//	// try {
//	// mServerSocket.close();
//	// mClientSocket.close();
//	// Log.d("close", "GuC in sockets closed");
//	// } catch (IOException e) {
//	// // TODO Auto-generated catch block
//	// e.printStackTrace();
//	// }
//	// }
//
//	/**
//	 * Accept the incomming request from the gameController
//	 * 
//	 * @author or3x
//	 */
//	private class AcceptIncoming implements Runnable {
//
//		@Override
//		public void run() {
//			mServerSocket = null;
//			try {
//				// Set up a socket that the gameController can connect to
//				mServerSocket = new ServerSocket(mPort);
//				Log.d("network U", "Server socket set up on port " + mPort);
//			} catch (IOException e1) {
//				Log.d("network U", "Server socket could not be set up on port " + mPort);
//			}
//			try {
//				// Wait for the game controller to connect and give it a thread for handling its updates
//				mClientSocket = mServerSocket.accept();
//				new Thread(new ConnectionHandler()).start();
//				Log.d("network U", "New connection handler started");
//			} catch (IOException e) {
//				Log.d("network U", "Could not create socket on port " + mPort);
//				return;
//			}
//		}
//	}
//
//	/**
//	 * Handles the updates received from the gameController
//	 */
//	private class ConnectionHandler implements Runnable {
//
//		@Override
//		public void run() {
//			// Keep getting updates
//			while (true) {
//				ObjectInputStream ois = null;
//				try {
//					ois = (new ObjectInputStream(mClientSocket.getInputStream()));
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				GameState gameState;
//				try {
//					// Read the GameState
//					gameState = (GameState) ois.readObject();
//					if (gameState != null) {
//						setChanged();
//						notifyObservers(gameState);
//					}
//					Log.d("network U", "New State received ");
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
// }
