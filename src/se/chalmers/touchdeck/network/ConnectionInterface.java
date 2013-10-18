/**
 Copyright (c) 2013 Karl Engström, Sebastian Ivarsson, Jacob Lundberg, Joakim Karlsson, Alexander Persson and Fredrik Westling
 */

/**
 This file is part of TouchDeck.

 TouchDeck is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 2 of the License, or
 (at your option) any later version.

 TouchDeck is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with TouchDeck.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.chalmers.touchdeck.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

/**
 * Used to set up a single connection.
 * 
 * @author group17
 */
public abstract class ConnectionInterface implements Runnable {
    private final String mIpAddr;
    private final int    mPort;
    private Socket       mSocket;

    /**
     * Create a new Connection Object for the given ip and port.
     * 
     * @param ipAddr The ip address
     * @param port The port
     */
    public ConnectionInterface(String ipAddr, int port) {
        mIpAddr = ipAddr;
        mPort = port;
    }

    @Override
    public void run() {
        try {
            InetAddress serverAddr = InetAddress.getByName(mIpAddr);
            mSocket = new Socket(serverAddr, mPort);
            Log.d("ConInt " + mPort, "Client socket setup at " + mIpAddr + ":" + mPort);
            send(mSocket);
        } catch (IOException e1) {
            Log.e("ConInt " + mPort, "Error setting up client" + mIpAddr + e1.getMessage());
        }
    }

    /**
     * Passes the created socket on.
     * 
     * @param socket The created socket
     */
    public abstract void send(Socket socket);

    /**
     * Removes the socket.
     * 
     * @param socket The socket to remove
     */
    public abstract void remove(Socket socket);

    /**
     * Ends the connection and closes the socket.
     */
    public void end() {
        try {
            remove(mSocket);
            if (mSocket != null) {
                mSocket.close();
            }
        } catch (IOException e) {
            Log.e("ConInt " + mPort, "Error closing socket");
        }
    }
}
