package se.chalmers.touchdeck.network;

import java.io.Serializable;

import se.chalmers.touchdeck.gamecontroller.GameController;
import se.chalmers.touchdeck.gamecontroller.Operation;

public class GameListener extends ListenerInterface {
	private final GameController	mGameController;

	public GameListener(GameController gc, int port) {
		super(true, port);
		mGameController = gc;
	}

	@Override
	public void handle(Serializable s, String ipAddr) {
		if (s instanceof Operation) {
			Operation op = (Operation) s;
			mGameController.performOperation(op);
		}
	}
}
