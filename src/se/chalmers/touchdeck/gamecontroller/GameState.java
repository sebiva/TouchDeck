package se.chalmers.touchdeck.gamecontroller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import se.chalmers.touchdeck.models.Pile;

/**
 * Represents the state of the game. Holds the position and content of all piles and therefore all cards. Also holds a
 * set of all the names of the piles.
 * 
 * @author or3x
 */
public class GameState implements Serializable {
	/**
	 * Serializable
	 */
	private static final long	serialVersionUID	= -1348920124510465049L;
	private ArrayList<Pile>		mPiles;
	private HashSet<String>		mPileNames			= new HashSet<String>();
	private int					mDefaultPileNo		= 1;

	/**
	 * Holds the state for the game
	 * 
	 * @param piles A list of all the piles on the table
	 * @param pileNames
	 */
	public GameState(ArrayList<Pile> piles, HashSet<String> pileNames) {
		this.mPiles = piles;
		this.mPileNames = pileNames;
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
		this.mPiles = piles;
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
}
