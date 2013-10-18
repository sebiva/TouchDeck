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
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Represents the state of the game. Holds the position and content of all piles and therefore all cards. Also holds a
 * set of all the names of the piles.
 * 
 * @author or3x
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = -1348920124510465049L;
    private ArrayList<Pile>   mPiles;
    private HashSet<String>   mPileNames       = new HashSet<String>();
    private int               mDefaultPileNo   = 1;
    private boolean           mHostStillLeft   = true;
    private boolean           mIsRestarted     = false;

    /**
     * Holds the state for the game.
     * 
     * @param piles A list of all the piles on the table
     * @param pileNames A set of all the pile names
     */
    public GameState(ArrayList<Pile> piles, HashSet<String> pileNames) {
        mPiles = piles;
        mPileNames = pileNames;
    }

    /**
     * @return the piles
     */
    public ArrayList<Pile> getPiles() {
        return mPiles;
    }

    /**
     * @param piles the piles to set
     */
    public void setPiles(ArrayList<Pile> piles) {
        mPiles = piles;
    }

    /**
     * @return the pileNames
     */
    public HashSet<String> getPileNames() {
        return mPileNames;
    }

    /**
     * @return The default pile name
     */
    public String getDefaultPileName() {
        return "Pile " + mDefaultPileNo;
    }

    /**
     * @param pileNo The number to set as the default pile number
     */
    public void setDefaultPileNo(int pileNo) {
        mDefaultPileNo = pileNo;
    }

    /**
     * @return The number of the default pile
     */
    public int getDefaultPileNo() {
        return mDefaultPileNo;
    }

    /**
     * @param b Indicates whether the host is still in the game
     */
    public void setHostStillLeft(boolean b) {
        mHostStillLeft = b;
    }

    /**
     * @return Whether the host is still in the game
     */
    public boolean getHostStillLeft() {
        return mHostStillLeft;
    }

    /**
     * @return Whether or not the game has been restarted
     */
    public boolean getIsRestarted() {
        return mIsRestarted;
    }

    /**
     * @param isRestarted Whether or not the game has been restarted
     */
    public void setIsRestarted(boolean isRestarted) {
        mIsRestarted = isRestarted;
    }
}
