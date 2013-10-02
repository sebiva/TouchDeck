package se.chalmers.touchdeck.network;

import java.net.Socket;

import se.chalmers.touchdeck.gamecontroller.Operation;
import se.chalmers.touchdeck.gamecontroller.Operation.Op;
import se.chalmers.touchdeck.gui.GuiController;

public class GuiToGameConnection extends ConnectionInterface {
	private final GuiController	mGuiController;

	public GuiToGameConnection(String ipAddr, int port, GuiController guiController) {
		super(ipAddr, port);
		mGuiController = guiController;
	}

	@Override
	public void send(Socket socket) {
		mGuiController.setSocket(socket);
		mGuiController.sendOperation(new Operation(Op.connect));
	}

}
