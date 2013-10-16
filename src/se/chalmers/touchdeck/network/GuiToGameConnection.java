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

import java.net.Socket;

import se.chalmers.touchdeck.game.client.GuiController;
import se.chalmers.touchdeck.game.server.Operation;
import se.chalmers.touchdeck.game.server.Operation.Op;

/**
 * Sets up a single connection from the GuiController to the GameController
 * 
 * @author group17
 */
public class GuiToGameConnection extends ConnectionInterface {
	private final GuiController	mGuiController;

	public GuiToGameConnection(String ipAddr, int port, GuiController guiController) {
		super(ipAddr, port);
		mGuiController = guiController;
	}

	/**
	 * Send the Socket to the GuiController, allowing it to send operations to the gameController
	 */
	@Override
	public void send(Socket socket) {
		mGuiController.setSocket(socket);
		Operation operation = new Operation(Op.connect);
		mGuiController.sendOperation(operation);
	}

	@Override
	public void end() {
		super.end();
	}

	@Override
	public void remove(Socket socket) {
		mGuiController.removeSocket();

	}

}
