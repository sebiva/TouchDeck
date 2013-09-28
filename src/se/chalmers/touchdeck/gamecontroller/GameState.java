package se.chalmers.touchdeck.gamecontroller;

import java.io.Serializable;
import java.util.ArrayList;

import se.chalmers.touchdeck.models.Pile;

public class GameState implements Serializable {
	/**
	 * Serializable
	 */
	private static final long	serialVersionUID	= 1L;
	private ArrayList<Pile>		piles;

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
}
