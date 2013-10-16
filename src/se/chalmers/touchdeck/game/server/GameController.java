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

package se.chalmers.touchdeck.game.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import se.chalmers.touchdeck.misc.Constant;
import se.chalmers.touchdeck.misc.enums.Rank;
import se.chalmers.touchdeck.misc.enums.Suit;
import se.chalmers.touchdeck.network.GameListener;
import se.chalmers.touchdeck.network.GameToGuiConnection;
import se.chalmers.touchdeck.network.IpFinder;
import android.util.Log;

/**
 * Controls the game logic
 * 
 * @author group17
 */
public class GameController {

	private final ArrayList<Pile>						mTable					= new ArrayList<Pile>();
	private final HashSet<String>						mPileNames				= new HashSet<String>();

	private final GameState								mGameState;
	private final int									mGuiPort				= Constant.GuiControllerPort;
	private final HashMap<String, GameToGuiConnection>	mGameToGuiThreads		= new HashMap<String, GameToGuiConnection>();
	private final LinkedList<Socket>					mAllGameToGuiSockets	= new LinkedList<Socket>();
	private final GameListener							mGameListener;

	/**
	 * Creates a new gameController and sets up a deck.
	 */
	public GameController() {
		// Fill the table empty positions.
		for (int i = 0; i < Constant.NumOfPiles; i++) {
			mTable.add(i, null);
		}
		createDeck();
		mGameState = new GameState(mTable, mPileNames);

		// Start the listener for incoming connections
		mGameListener = new GameListener(this, Constant.GameControllerPort);
		new Thread(mGameListener).start();
	}

	/**
	 * @param socket The socket to add as the connection to the guiController
	 */
	public void addSocket(Socket socket) {
		Log.d("network GaC", "socket added to list " + socket.getRemoteSocketAddress().toString());
		mAllGameToGuiSockets.add(socket);
	}

	public void removeSocket(Socket socket) {
		Log.e("In GaC", "Socket removed from list" + socket.getRemoteSocketAddress().toString());
		mAllGameToGuiSockets.remove(socket);
	}

	/**
	 * Sends the gameState to all the guis
	 */
	public void sendUpdatedState() {
		ObjectOutputStream out = null;
		Log.e("in GaC, sendUpdatedState ", "Sockets left: " + mAllGameToGuiSockets.size());

		for (Socket socket : mAllGameToGuiSockets) {
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(mGameState);
				Log.d("sendUpdated GaC", "State written into socket " + socket.getRemoteSocketAddress().toString() + "host still left: "
						+ mGameState.getHostStillLeft());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Creates a standard 52-card deck
	 * 
	 * @return A pile containing the deck
	 */
	private Pile createDeck() {
		Pile deck = new Pile(Constant.MainDeckName);
		mPileNames.add(Constant.MainDeckName);
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				deck.addCard(new Card(suit, rank));
			}
		}
		// Put the deck at the middle of the table
		mTable.set(Constant.MidOfTable, deck);
		return deck;
	}

	/**
	 * Performs the given operation and sends out the updated state to all guis
	 * 
	 * @param op
	 */
	public synchronized void performOperation(Operation op) {
		String ipAddr = op.getIpAddr();
		Integer pilePosition = op.getPile1();
		if (pilePosition != null) {
			Pile p = mTable.get(pilePosition);
			if (p != null) {
				if (!p.getOwner().equals(Constant.PileHasNoOwner) && !p.getOwner().equals(ipAddr)) {
					return; // The user was not allowed to perform the operation
				}
			}
		}

		switch (op.getOp()) {

		case move:
			Pile srcPile = mTable.get(op.getPile1());
			Pile destPile = mTable.get(op.getPile2());
			if (destPile != null && srcPile != null) {
				Card cardToMove = op.getCard();
				for (int i = 0; i < srcPile.getSize(); i++) {
					Card card = srcPile.getCard(i);
					if (card.equals(cardToMove)) {
						srcPile.takeCard(i);
						destPile.addCard(card);
						sendUpdatedState();
						return;
					}
				}
			}

			Log.d("handle GaC", "move");
			break;

		case flip:
			Card cardToFlip = op.getCard();
			Pile currentPile = mTable.get(op.getPile1());
			for (int i = 0; i < currentPile.getSize(); i++) {
				Card c = currentPile.getCard(i);
				if (c.equals(cardToFlip)) {
					c.flipFace();
					sendUpdatedState();
					Log.d("handle GaC", "flip");
					return;
				}
			}
			break;

		case create:
			int pilePos = op.getPile1();
			if (mTable.get(pilePos) != null) {
				return; // There was already a pile there
			}
			String name = "";
			if (mPileNames.contains(op.getName())) {
				int defaultNo = mGameState.getDefaultPileNo();
				do {
					name = "Pile " + defaultNo;
					defaultNo++;
				} while (mGameState.getPileNames().contains(name));
				mGameState.setDefaultPileNo(defaultNo);
			} else if (op.getName().equals("Pile " + mGameState.getDefaultPileNo())) {
				name = mGameState.getDefaultPileName();
				mGameState.setDefaultPileNo(mGameState.getDefaultPileNo() + 1);
			} else {
				name = op.getName();
			}

			mTable.set(pilePos, new Pile(name));
			mPileNames.add(name);
			sendUpdatedState();
			Log.d("handle GaC", "create" + name);
			break;

		case connect:
			GameToGuiConnection connection = new GameToGuiConnection(op.getIpAddr(), mGuiPort, this);
			new Thread(connection).start();
			mGameToGuiThreads.put(op.getIpAddr(), connection);
			Log.d("handle GaC", "connected : " + op.getIpAddr());
			break;

		case shuffle:
			Pile pileToShuffle = mTable.get(op.getPile1());
			if (pileToShuffle != null) {
				pileToShuffle.shuffle();
				sendUpdatedState();
			}
			Log.d("handle GaC", "shuffle");
			break;

		case delete:
			int pilePosToDelete = op.getPile1();
			if (mTable.get(pilePosToDelete) != null && mTable.get(pilePosToDelete).getSize() == 0) {
				mPileNames.remove(mTable.get(pilePosToDelete).getName());
				mTable.set(pilePosToDelete, null);
				sendUpdatedState();
			}
			break;

		case rename:
			Pile pileToRename = mTable.get(op.getPile1());
			if (pileToRename == null) {
				return;
			}
			String oldName = pileToRename.getName();
			String newName = "";
			if (mPileNames.contains(op.getName())) {
				int defaultNo = mGameState.getDefaultPileNo();
				do {
					newName = "Pile " + defaultNo;
					defaultNo++;
				} while (mGameState.getPileNames().contains(newName));
				mGameState.setDefaultPileNo(defaultNo);
			} else if (op.getName().equals("Pile " + mGameState.getDefaultPileNo())) {
				newName = mGameState.getDefaultPileName();
				mGameState.setDefaultPileNo(mGameState.getDefaultPileNo() + 1);
			} else {
				newName = op.getName();
			}

			pileToRename.setName(newName);
			mPileNames.add(newName);
			mPileNames.remove(oldName);
			sendUpdatedState();
			break;

		case faceUp:
			Pile pileToFaceUp = mTable.get(op.getPile1());
			if (pileToFaceUp != null) {
				for (Card p : pileToFaceUp.getCards()) {
					p.setFaceUp();
				}
				sendUpdatedState();
			}
			break;

		case faceDown:
			Pile pileToFaceDown = mTable.get(op.getPile1());
			if (pileToFaceDown != null) {
				for (Card p : pileToFaceDown.getCards()) {
					p.setFaceDown();
				}
				sendUpdatedState();
			}
			break;

		case moveAll:
			Pile fromPile = mTable.get(op.getPile1());
			Pile toPile = mTable.get(op.getPile2());
			if (fromPile != null && toPile != null) {
				int totalCards = fromPile.getSize();
				for (int i = totalCards; i > 0; i--) {
					Card card = fromPile.takeCard(i - 1);
					toPile.addCard(card);
				}
				sendUpdatedState();
			}
			break;

		case protect:
			Pile pileToProtect = mTable.get(op.getPile1());
			if (pileToProtect != null) {
				pileToProtect.setOwner(op.getName());
				sendUpdatedState();
			}

			break;

		case unprotect:
			Pile protectedPile = mTable.get(op.getPile1());
			if (protectedPile != null && protectedPile.getOwner().equals(op.getName())) {
				protectedPile.setOwner(Constant.PileHasNoOwner);
				sendUpdatedState();
			}
			break;

		case restart:
			mTable.clear();
			for (int i = 0; i < Constant.NumOfPiles; i++) {
				mTable.add(i, null);
			}
			mPileNames.clear();
			createDeck();
			mGameState.setDefaultPileNo(1);
			mGameState.setIsRestarted(true);
			sendUpdatedState();
			mGameState.setIsRestarted(false);
			break;

		case pileMove:
			Pile pileToMove = mTable.get(op.getPile1());
			Pile destination = mTable.get(op.getPile2());
			if (pileToMove != null && destination == null) {
				mTable.set(op.getPile2(), pileToMove);
				mTable.set(op.getPile1(), destination);
				sendUpdatedState();
			}
			break;

		case disconnect:

			String ipDeviceAddr = op.getIpAddr();
			Log.d("in GaC Disconnect", ipDeviceAddr);
			GameToGuiConnection conn = mGameToGuiThreads.get(ipDeviceAddr);
			conn.end();
			mGameToGuiThreads.remove(op.getIpAddr());
			Log.d("in GaC Disconnect", "GameToGui removed, ip : " + ipDeviceAddr);

			mGameListener.end(op.getIpAddr());
			if (ipDeviceAddr.equals(IpFinder.LOOP_BACK)) {
				Log.d("GaC host", "Leaving");
				mGameState.setHostStillLeft(false);
				sendUpdatedState();
				mAllGameToGuiSockets.clear();
			}
			// Remove ownership of piles for the user
			for (Pile p : mTable) {
				if (p != null) {
					if (p.getOwner().equals(ipDeviceAddr)) {
						p.setOwner(Constant.PileHasNoOwner);
					}
				} else {
				}
			}
			sendUpdatedState();
			Log.d("in GaC Disconnect", "GameListener ended");
			break;
		default:
		}
	}

	/**
	 * @return the gamestate
	 */
	public GameState getGameState() {
		return mGameState;
	}

}
