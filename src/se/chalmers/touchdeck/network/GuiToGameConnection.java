package se.chalmers.touchdeck.network;

import java.net.Socket;

import se.chalmers.touchdeck.gamecontroller.Operation;
import se.chalmers.touchdeck.gamecontroller.Operation.Op;
import se.chalmers.touchdeck.gui.GuiController;

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

}
