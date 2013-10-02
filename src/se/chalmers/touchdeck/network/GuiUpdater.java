package se.chalmers.touchdeck.network;

import java.io.Serializable;

import se.chalmers.touchdeck.gamecontroller.GameState;
import se.chalmers.touchdeck.gui.GuiController;

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
}
