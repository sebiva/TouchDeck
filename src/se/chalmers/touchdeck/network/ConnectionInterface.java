package se.chalmers.touchdeck.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

public abstract class ConnectionInterface implements Runnable {
	private final String	mIpAddr;
	private final int		mPort;
	private Socket			mSocket;

	public ConnectionInterface(String ipAddr, int port) {
		mIpAddr = ipAddr;
		mPort = port;
	}

	@Override
	public void run() {
		try {
			InetAddress serverAddr = InetAddress.getByName(mIpAddr);
			mSocket = new Socket(serverAddr, mPort);
			Log.d("network GaC", "Client socket setup at " + mPort);
			send(mSocket);
		} catch (IOException e1) {
			Log.d("network GaC", "Error setting up client" + e1.getMessage() + mIpAddr);
		}

	}

	/**
	 * @return the Socket
	 */
	public Socket getSocket() {
		return mSocket;
	}

	public abstract void send(Socket socket);
}
