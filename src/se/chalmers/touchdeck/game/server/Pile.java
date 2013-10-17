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

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

import se.chalmers.touchdeck.misc.Constant;
import se.chalmers.touchdeck.misc.exceptions.CardNotFoundException;

/**
 * Class modeling a pile.
 * 
 * @author group17
 */
public class Pile implements Serializable {

    private static final long      serialVersionUID = -3056217673776714467L;
    private final LinkedList<Card> mCards           = new LinkedList<Card>();
    private String                 mName;
    private String                 mOwner           = "noOwner";

    /**
     * Constructor.
     */
    public Pile() {

    }

    /**
     * Constructor.
     * 
     * @param name The name of the pile
     */
    public Pile(String name) {
        this.mName = name;
    }

    /**
     * Adds a card to the pile.
     * 
     * @param card The card to add
     */
    public void addCard(Card card) {
        mCards.addFirst(card);
    }

    /**
     * Takes one card from the pile.
     * 
     * @param pos The position of the card
     * @return The card
     * @throws CardNotFoundException If there is no such card in the pile
     */
    public Card takeCard(int pos) {
        try {
            Card card = mCards.remove(pos);
            return card;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns the number of cards in the pile.
     * 
     * @return The number of cars in the pile
     */
    public int getSize() {
        return mCards.size();
    }

    /**
     * Returns the cards in the pile.
     * 
     * @return The cards in the piles
     */
    public LinkedList<Card> getCards() {
        return mCards;
    }

    /**
     * Gives the name of pile.
     * 
     * @return The name
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the name of pile.
     * 
     * @param name The name to set to the pile
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Returns the card at a certain index.
     * 
     * @param cardPos the position of the card
     * @return The requested card, or null if pos was out of range
     */
    public Card getCard(int cardPos) {
        try {
            Card card = mCards.get(cardPos);
            return card;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

    }

    /**
     * Randomly rearranges the order of cards in the pile, effectively shuffling it.
     */
    public void shuffle() {
        for (int i = 0; i < Constant.RepeatShuffle; i++) {
            Collections.shuffle(mCards);
        }
    }

    /**
     * @return the Owner
     */
    public String getOwner() {
        return mOwner;
    }

    /**
     * @param owner the Owner to set
     */
    public void setOwner(String owner) {
        this.mOwner = owner;
    }
}
