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
 * Controls the game logic.
 * 
 * @author group17
 */
public class GameController {

    private final ArrayList<Pile>                      mTable               = new ArrayList<Pile>();
    private final HashSet<String>                      mPileNames           = new HashSet<String>();

    private final GameState                            mGameState;
    private final int                                  mGuiPort             = Constant.GuiControllerPort;
    private final HashMap<String, GameToGuiConnection> mGameToGuiThreads    = new HashMap<String, GameToGuiConnection>();
    private final LinkedList<Socket>                   mAllGameToGuiSockets = new LinkedList<Socket>();
    private final GameListener                         mGameListener;

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
        Log.d("in GaC", "socket added to list " + socket.getRemoteSocketAddress().toString());
        mAllGameToGuiSockets.add(socket);
    }

    /**
     * Removes a socket from the list of connections from the Server to Clients.
     * 
     * @param socket The socket to remove
     */
    public void removeSocket(Socket socket) {
        Log.d("in GaC", "Socket removed from list" + socket.getRemoteSocketAddress().toString());
        mAllGameToGuiSockets.remove(socket);
    }

    /**
     * Sends the gameState to all the clients.
     */
    public void sendUpdatedState() {
        ObjectOutputStream out = null;
        Log.d("in GaC, sendUpdatedState ", "Sockets left: " + mAllGameToGuiSockets.size());

        for (Socket socket : mAllGameToGuiSockets) {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(mGameState);
                Log.d("sendUpdated GaC", "State written into socket "
                        + socket.getRemoteSocketAddress().toString() + "host still left: "
                        + mGameState.getHostStillLeft());
                out.flush();
            } catch (IOException e) {
                Log.e("in GaC, sendUpdatedState", "Error sending updated state");
            }
        }
    }

    /**
     * Creates a standard 52-card deck.
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
     * @return the gamestate
     */
    public GameState getGameState() {
        return mGameState;
    }

    /**
     * Performs the given operation and sends out the updated state to all guis.
     * 
     * @param op The operation to perform
     */
    public synchronized void performOperation(Operation op) {
        // Make sure the user is allowed to perform the operation
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
            moveCard(mTable.get(op.getPile1()), mTable.get(op.getPile2()), op.getCard());
            break;

        case flip:
            flipCard(mTable.get(op.getPile1()), op.getCard());
            break;

        case protect:
            protectPile(mTable.get(op.getPile1()), op.getName());
            break;

        case unprotect:
            unProtectPile(mTable.get(op.getPile1()), op.getName());
            break;

        case create:
            createPile(op.getPile1(), op.getName());
            break;

        case rename:
            renamePile(op.getPile1(), op.getName());
            break;

        case shuffle:
            shufflePile(op.getPile1());
            break;

        case delete:
            deletePile(op.getPile1());
            break;

        case faceUp:
            faceUpPile(op.getPile1());
            break;

        case faceDown:
            faceDownPile(op.getPile1());
            break;

        case moveAll:
            moveAllFromPile(mTable.get(op.getPile1()), mTable.get(op.getPile2()));
            break;

        case pileMove:
            movePile(op.getPile1(), op.getPile2());
            break;

        case restart:
            restartGame();
            break;

        case connect:
            connectClient(op.getIpAddr());
            break;

        case disconnect:
            disconnectClient(op.getIpAddr());
            break;
        default:
        }
    }

    /**
     * Moves a card, cardToMove, from srcPile to destPile.
     * 
     * @param srcPile The pile to move from
     * @param destPile The pile to move to
     * @param cardToMove The card to move
     */
    private void moveCard(Pile srcPile, Pile destPile, Card cardToMove) {
        if (destPile != null && srcPile != null) {
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
    }

    /**
     * Flips the face of a card.
     * 
     * @param currentPile The pile where the card is
     * @param cardToFlip The card to flip
     */
    private void flipCard(Pile currentPile, Card cardToFlip) {
        for (int i = 0; i < currentPile.getSize(); i++) {
            Card c = currentPile.getCard(i);
            if (c.equals(cardToFlip)) {
                c.flipFace();
                sendUpdatedState();
                return;
            }
        }
    }

    /**
     * Protects a pile.
     * 
     * @param pileToProtect The pile to protect
     * @param name The name (ip address) of the user protecting it
     */
    private void protectPile(Pile pileToProtect, String name) {
        if (pileToProtect != null) {
            pileToProtect.setOwner(name);
            sendUpdatedState();
        }
    }

    /**
     * Unprotects a pile.
     * 
     * @param protectedPile The pile that is protected
     * @param name The name (ip address) of the user unprotecting it
     */
    private void unProtectPile(Pile protectedPile, String name) {
        if (protectedPile != null && protectedPile.getOwner().equals(name)) {
            protectedPile.setOwner(Constant.PileHasNoOwner);
            sendUpdatedState();
        }
    }

    /**
     * Create a new Pile.
     * 
     * @param pilePos The position on which to create the pile
     * @param nameEntered The name entered for the pile
     */
    private void createPile(int pilePos, String nameEntered) {
        if (mTable.get(pilePos) != null) {
            return; // There was already a pile there
        }
        String name = getNameForPile(nameEntered);
        mPileNames.add(name);
        mTable.set(pilePos, new Pile(name));
        sendUpdatedState();
    }

    /**
     * Renames a pile.
     * 
     * @param pilePos The position of the pile to rename
     * @param nameEntered The new name entered for the pile
     */
    private void renamePile(int pilePos, String nameEntered) {
        Pile pileToRename = mTable.get(pilePos);
        if (pileToRename == null) {
            return;
        }
        String newName = getNameForPile(nameEntered);
        pileToRename.setName(newName);
        mPileNames.add(newName);
        String oldName = pileToRename.getName();
        mPileNames.remove(oldName);
        sendUpdatedState();
    }

    /**
     * Set the name of for a pile, if it already exists, the proper default pile name is returned.
     * 
     * @param nameEntered The name that was entered for the pile
     * @return The name the pile should have
     */
    private String getNameForPile(String nameEntered) {
        String name = "";
        if (mPileNames.contains(nameEntered)) {
            // Sets the next available default name
            int defaultNo = mGameState.getDefaultPileNo();
            do {
                name = "Pile " + defaultNo;
                defaultNo++;
            } while (mGameState.getPileNames().contains(name));
            mGameState.setDefaultPileNo(defaultNo);
        } else if (nameEntered.equals("Pile " + mGameState.getDefaultPileNo())) {
            name = mGameState.getDefaultPileName();
            mGameState.setDefaultPileNo(mGameState.getDefaultPileNo() + 1);
        } else {
            name = nameEntered;
        }
        return name;

    }

    /**
     * Shuffles a pile.
     * 
     * @param pilePos The position of the pile to shuffle
     */
    private void shufflePile(int pilePos) {
        Pile pileToShuffle = mTable.get(pilePos);
        if (pileToShuffle != null) {
            pileToShuffle.shuffle();
            sendUpdatedState();
        }
    }

    /**
     * Deletes a pile.
     * 
     * @param pilePosToDelete The position of the pile to delete
     */
    private void deletePile(int pilePosToDelete) {
        if (mTable.get(pilePosToDelete) != null && mTable.get(pilePosToDelete).getSize() == 0) {
            mPileNames.remove(mTable.get(pilePosToDelete).getName());
            mTable.set(pilePosToDelete, null);
            sendUpdatedState();
        }
    }

    /**
     * Faces an entire pile face up.
     * 
     * @param pilePos The position of the pile to face up
     */
    private void faceUpPile(int pilePos) {
        Pile pileToFaceUp = mTable.get(pilePos);
        if (pileToFaceUp != null) {
            for (Card p : pileToFaceUp.getCards()) {
                p.setFaceUp();
            }
            sendUpdatedState();
        }
    }

    /**
     * Faces an entire pile face down.
     * 
     * @param pilePos The position of the pile to face down
     */
    private void faceDownPile(int pilePos) {
        Pile pileToFaceDown = mTable.get(pilePos);
        if (pileToFaceDown != null) {
            for (Card p : pileToFaceDown.getCards()) {
                p.setFaceDown();
            }
            sendUpdatedState();
        }
    }

    /**
     * Move all cards from one pile to another.
     * 
     * @param fromPile The pile to move from
     * @param toPile The pile to move to
     */
    private void moveAllFromPile(Pile fromPile, Pile toPile) {
        if (fromPile != null && toPile != null) {
            int totalCards = fromPile.getSize();
            for (int i = totalCards; i > 0; i--) {
                Card card = fromPile.takeCard(i - 1);
                toPile.addCard(card);
            }
            sendUpdatedState();
        }
    }

    /**
     * Moves an entire pile.
     * 
     * @param pileToMovePos The position to move from
     * @param pileDestinationPos The position to move to
     */
    private void movePile(int pileToMovePos, int pileDestinationPos) {
        Pile pileToMove = mTable.get(pileToMovePos);
        Pile destination = mTable.get(pileDestinationPos);
        if (pileToMove != null && destination == null) {
            mTable.set(pileDestinationPos, pileToMove);
            mTable.set(pileToMovePos, destination);
            sendUpdatedState();
        }
    }

    /**
     * Restarts the game.
     */
    private void restartGame() {
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
    }

    /**
     * Connects a client to the game.
     * 
     * @param clientIpAddr The ip address of the client
     */
    private void connectClient(String clientIpAddr) {
        GameToGuiConnection connection = new GameToGuiConnection(clientIpAddr, mGuiPort, this);
        new Thread(connection).start();
        mGameToGuiThreads.put(clientIpAddr, connection);
    }

    /**
     * Disconnect a client from the game.
     * 
     * @param clientIpAddr The ip address of the client
     */
    private void disconnectClient(String clientIpAddr) {
        GameToGuiConnection conn = mGameToGuiThreads.get(clientIpAddr);
        conn.end();
        mGameToGuiThreads.remove(clientIpAddr);

        mGameListener.end(clientIpAddr);
        if (clientIpAddr.equals(IpFinder.LOOP_BACK)) {
            Log.d("in GaC", "Host leaving");
            mGameState.setHostStillLeft(false);
            sendUpdatedState();
            mAllGameToGuiSockets.clear();
        }
        // Remove ownership of piles for the client
        for (Pile p : mTable) {
            if (p != null) {
                if (p.getOwner().equals(clientIpAddr)) {
                    p.setOwner(Constant.PileHasNoOwner);
                }
            }
        }
        sendUpdatedState();
        Log.d("in GaC", "Disconnected: " + clientIpAddr);
    }
}
