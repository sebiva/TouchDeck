package se.chalmers.touchdeck.network;

import java.net.Socket;

import se.chalmers.touchdeck.gamecontroller.GameController;

/**
 * Sets up a single connection from the GameController to the GuiController
 * 
 * @author group17
 */
public class GameToGuiConnection extends ConnectionInterface {
	private final GameController	mGameController;

	public GameToGuiConnection(String ipAddr, int port, GameController gameController) {
		super(ipAddr, port);
		mGameController = gameController;
	}

	/**
	 * Add the socket to the gameController
	 */
	@Override
	public void send(Socket socket) {
		mGameController.addSocket(socket);
		mGameController.sendUpdatedState();
	}

}
