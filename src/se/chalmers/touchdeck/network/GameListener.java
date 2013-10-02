package se.chalmers.touchdeck.network;

import java.io.Serializable;

import se.chalmers.touchdeck.gamecontroller.GameController;
import se.chalmers.touchdeck.gamecontroller.Operation;

/**
 * Listens to incoming connections from guiControllers, and handles their requests for operations
 * 
 * @author group17
 */
public class GameListener extends ListenerInterface {
	private final GameController	mGameController;

	public GameListener(GameController gc, int port) {
		super(true, port);
		mGameController = gc;
	}

	/**
	 * Handle the operations from the guiController
	 */
	@Override
	public void handle(Serializable s, String ipAddr) {
		if (s instanceof Operation) {
			Operation op = (Operation) s;
			op.setIpAddr(ipAddr);
			mGameController.performOperation(op);
		}
	}
}
