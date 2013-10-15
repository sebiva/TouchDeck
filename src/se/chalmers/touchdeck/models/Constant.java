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

}
