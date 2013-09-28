package se.chalmers.touchdeck.gamecontroller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import se.chalmers.touchdeck.models.Pile;

public class GameState implements Serializable {
	/**
	 * Serializable
	 */
	private static final long		serialVersionUID	= -1348920124510465049L;
	private ArrayList<Pile>			piles;
	private final HashSet<String>	pileNames			= new HashSet<String>();

	/**
	 * Holds the state for the game
	 * 
	 * @param piles A list of all the piles on the table
	 */
	public GameState(ArrayList<Pile> piles) {
		this.piles = piles;
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
}
