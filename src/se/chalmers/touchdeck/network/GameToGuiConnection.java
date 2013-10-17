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

import se.chalmers.touchdeck.game.server.GameController;

/**
 * Sets up a single connection from the GameController to the GuiController.
 * 
 * @author group17
 */
public class GameToGuiConnection extends ConnectionInterface {
    private final GameController mGameController;

    /**
     * Creates a new GameToGuiConnection Object.
     * 
     * @param ipAddr The ip address that will be connected
     * @param port The port to connect to
     * @param gameController The associated GameController
     */
    public GameToGuiConnection(String ipAddr, int port, GameController gameController) {
        super(ipAddr, port);
        mGameController = gameController;
    }

    /**
     * Add the socket to the GameController.
     * 
     * @param socket The socket to add
     */
    @Override
    public void send(Socket socket) {
        mGameController.addSocket(socket);
        mGameController.sendUpdatedState();
    }

    /**
     * End the connection.
     */
    @Override
    public void end() {
        super.end();
    }

    /**
     * Removes the socket from the GameController.
     * 
     * @param socket The socket to remove
     */
    @Override
    public void remove(Socket socket) {
        mGameController.removeSocket(socket);
    }

}
