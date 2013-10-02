//package se.chalmers.touchdeck.gamecontroller;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//import android.util.Log;
//
///**
// * TODO Refactor, Listens to operations from the gui over the network
// * 
// * @author group17
// */
//public class Listener {
//
//	private final GameController	mGameController;
//	private ServerSocket			mServerSocket;
//
//	private final int				mPort	= 4242;
//
//	/**
//	 * Sets up the listener and starts listening for incoming connections
//	 * 
//	 * @param gc The gamecontroller in the session
//	 */
//	public Listener(GameController gc) {
//		mGameController = gc;
//		new Thread(new AcceptIncoming()).start();
//	}
//
//	/**
//	 * Performs the operation requested from the gui
//	 * 
//	 * @param op The operation to be performed
//	 */
//	public void handleOp(Operation op) {
//		switch (op.getOp()) {
//		case move:
//			mGameController.moveCard(op);
//			Log.d("handle", "move");
//			break;
//		case flip:
//			mGameController.flip(op);
//			Log.d("handle", "flip");
//			break;
//		case create:
//			mGameController.createPile(op);
//			Log.d("handle", "create");
//			break;
//		case connect:
//			mGameController.startConnectThread(op);
//			Log.d("handle", "connected");
//			break;
//		case shuffle:
//			mGameController.shufflePile(op);
//			Log.d("handle", "shuffle");
//			break;
//		case delete:
//			mGameController.deletePile(op);
//			break;
//		default:
//		}
//	}
//
//	/**
//	 * Accepts incomming connections and assigns them a connectionhandler
//	 */
//	private class AcceptIncoming implements Runnable {
//
//		@Override
//		public void run() {
//			mServerSocket = null;
//			// Create the "welcome" socket
//			try {
//				mServerSocket = new ServerSocket(mPort);
//				Log.d("network L", "Server socket set up on port " + mPort);
//			} catch (IOException e1) {
//				Log.d("network L", "Server socket could not be set up on port " + mPort);
//				return;
//			}
//			// Accept all incomming requests to this socket and assign them a connection handler
//			while (true) {
//				try {
//					Socket clientSocket = mServerSocket.accept();
//					new Thread(new ConnectionHandler(clientSocket)).start();
//					Log.d("network L", "New connection handler started: " + clientSocket.getInetAddress().getHostAddress());
//				} catch (IOException e) {
//					Log.d("network L", "Could not create client socket");
//					return;
//				}
//			}
//		}
//	}
//
//	/**
//	 * Handles the connection with one gui
//	 */
//	private class ConnectionHandler implements Runnable {
//		private final Socket	clientSocket;
//
//		public ConnectionHandler(Socket s) {
//			clientSocket = s;
//		}
//
//		// TODO Error handling
//		@Override
//		public void run() {
//			// Keep reading the input
//			while (true) {
//				Log.d("network L", "In loop");
//				ObjectInputStream ois = null;
//				try {
//					ois = (new ObjectInputStream(clientSocket.getInputStream()));
//					Log.d("network L", "InputStream created");
//				} catch (IOException e) {
//					Log.d("network L", "Exiting ConnectionHandler");
//					e.printStackTrace();
//					break;// Should be return
//				}
//				Operation op;
//				try {
//					// Read the object and handle the operation
//					op = (Operation) ois.readObject();
//					String ipAddr = clientSocket.getRemoteSocketAddress().toString();
//					ipAddr = ipAddr.substring(1, ipAddr.indexOf(":"));
//					op.setIpAddr(ipAddr);
//					handleOp(op);
//					Log.d("network L" + clientSocket.getLocalPort(), "Operation completed" + op.getOp());
//				} catch (IOException e) {
//					Log.d("network L", "Reading went wrong, IO");
//					e.printStackTrace();
//				} catch (ClassNotFoundException e) {
//					Log.d("network L", "Reading went wrong, ClassNotFound");
//					e.printStackTrace();
//				}
//			}
//		}
//	}
// }
