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

package se.chalmers.touchdeck.models;

/**
 * Holds all constants for touchDeck
 * 
 * @author group17
 */
public class Constant {

	/**
	 * Numbers
	 */
	public static final int		NumRows					= 3;
	public static final int		NumColumns				= 8;
	public static final int		NumOfPiles				= NumRows * NumColumns;
	public static final int		MidOfTable				= NumOfPiles / 2 - 1;
	public static final int		GameControllerPort		= 4242;
	public static final int		GuiControllerPort		= 4243;
	public static final double	PileViewCardYFactor		= 2.0;
	public static final double	PileViewCardXFactor		= 0.73;
	public static final double	PileViewPeekFactor		= 0.8;
	public static final int		PileViewCardMargin		= 3;
	public static final int		MaxPileNameLength		= 20;
	public static final int		PileMargin				= 5;
	public static final int		MaxPileNameDisplayed	= 7;

	/**
	 * Strings
	 */

	public static final String	MainDeckName			= "deck";
	public static final String	IntentPileViewPileId	= "pileId";
	public static final String	IntentPileViewIp		= "pileViewIp";
	public static final String	IntentTableViewState	= "state";
	public static final String	IntentTableViewIP		= "ipAddr";
	public static final String	IntentTableViewHost		= "host";
	public static final String	PileHasNoOwner			= "noOwner";

}
