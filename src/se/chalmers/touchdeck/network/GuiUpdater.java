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

import java.io.Serializable;

import se.chalmers.touchdeck.gamecontroller.GameState;
import se.chalmers.touchdeck.gui.GuiController;

/**
 * @author group17
 */
public class GuiUpdater extends ListenerInterface {

	public GuiUpdater(GuiController guiController, int port) {
		super(false, port);
		addObserver(guiController);
	}

	@Override
	public void handle(Serializable s, String ipAddr) {
		if (s instanceof GameState) {
			GameState gameState = (GameState) s;
			if (gameState != null) {
				setChanged();
				notifyObservers(gameState);
			}
		}
	}

	@Override
	public void end(String ipAddr) {
		super.end(ipAddr);
	}
}
