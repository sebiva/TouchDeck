package se.chalmers.touchdeck.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

/**
 * Used to set up a single connection
 * 
 * @author group17
 */
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
			Log.d("ConInt", "Client socket setup at " + mIpAddr + ":" + mPort);
			send(mSocket);
		} catch (IOException e1) {
			Log.e("ConInt", "Error setting up client" + e1.getMessage() + mIpAddr);
		}
	}

	public abstract void send(Socket socket);
}
