/**
 Copyright (c) 2013 Karl Engstr�m, Sebastian Ivarsson, Jacob Lundberg, Joakim Karlsson, Alexander Persson and Fredrik Westling
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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.util.Log;

/**
 * Used to get the ip of the device
 * 
 * @author group17
 */
public class IpFinder {
	private static String		mIpAddr				= null;
	private static final int	MAX_LENGTH_OF_IP	= 15;

	/**
	 * Get the ip of the device
	 * 
	 * @return The ip address
	 */
	public static String getMyIp() {
		if (mIpAddr != null) {
			return mIpAddr;
		}
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						mIpAddr = inetAddress.getHostAddress().toString();
						// Filter out IPv6 addresses
						if (mIpAddr != null && mIpAddr.length() > MAX_LENGTH_OF_IP) {
							mIpAddr = null;
						}
					}
				}
			}

		} catch (SocketException se) {
			Log.e("GuC", "Error getting ip address");
		}
		// If no other address found, use the loopback address
		if (mIpAddr == null) {
			mIpAddr = "127.0.0.1";
		}
		return mIpAddr;
	}
}
