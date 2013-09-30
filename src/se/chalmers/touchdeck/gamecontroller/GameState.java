package se.chalmers.touchdeck.gamecontroller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import se.chalmers.touchdeck.models.Pile;

public class GameState implements Serializable {
	/**
	 * Serializable
	 */
	private static final long	serialVersionUID	= -1348920124510465049L;
	private ArrayList<Pile>		piles;
	private HashSet<String>		pileNames			= new HashSet<String>();
	private int					defaultPileNo;

	/**
	 * Holds the state for the game
	 * 
	 * @param piles A list of all the piles on the table
	 * @param pileNames
	 */
	public GameState(ArrayList<Pile> piles, HashSet<String> pileNames, int defaultPileNo) {
		this.piles = piles;
		this.pileNames = pileNames;
		this.defaultPileNo = defaultPileNo;
	}

	/**
	 * @return the piles
	 */
	public ArrayList<Pile> getPiles() {
		return piles;
	}

	/**
	 * @param piles the piles to set
	 */
	public void setPiles(ArrayList<Pile> piles) {
		this.piles = piles;
	}

	/**
	 * @return the pileNames
	 */
	public HashSet<String> getPileNames() {
		return pileNames;
	}

	public String getDefaultPileName() {
		return "Pile " + defaultPileNo;
	}

	public void setDefaultPileNo(int pileNo) {
		defaultPileNo = pileNo;

	}
}
