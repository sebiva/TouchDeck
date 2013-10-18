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

package se.chalmers.touchdeck.misc;

/**
 * Holds all constants for TouchDeck.
 * 
 * @author group17
 */
public class Constant {

    /**
     * Numbers.
     */
    public static final int    NumRows                 = 3;                   // The number of rows
    public static final int    NumColumns              = 8;                   // The number of columns
    public static final int    NumOfPiles              = NumRows * NumColumns; // The total number of piles
    public static final int    MidOfTable              = NumOfPiles / 2 - 1;  // The position of the deck
    public static final int    GameControllerPort      = 4242;                // The port for the GameController
    public static final int    GuiControllerPort       = 4243;                // The port for the GuiController
    public static final double PileViewCardYFactor     = 2.0;                 // The scaling factor for the card in x
                                                                               // in
    public static final double PILE_VIEW_CARD_X_FACTOR = 0.73;                // The scaling factor for the card in y
                                                                               // in
                                                                               // the PileView
    public static final double PileViewPeekFactor      = 0.8;                 // The size of the card when peeked
    public static final int    PileViewCardMargin      = 3;                   // The margin between cards in the
                                                                               // PileView
    public static final int    MaxPileNameLength       = 20;                  // Maximum length of a pilename
    public static final int    PileMargin              = 5;                   // The margin between piles on the table
    public static final int    MaxPileNameDisplayed    = 7;                   // Maximum length of pilename displayed
                                                                               // on
                                                                               // the table
    public static final int    PileNameTextSize        = 12;                  // Size of the text in the pileview
    public static final int    TextbarAlertColor       = 0xff303a7a;          // The color of the textbar when in
                                                                               // another
                                                                               // mode than normal mode
    public static final int    TextbarNormalColor      = 0xff000000;          // The color of the textbar when in
                                                                               // normal
                                                                               // mode
    public static final int    RepeatShuffle           = 10;                  // The number of times to shuffle the
                                                                               // pile
    public static final int    ExitDubbleTapTimeout    = 2000;                // The timeout between back-presses to
                                                                               // exit
                                                                               // the app
    public static final int    TableButtonWeight       = 9;                   // The weight of the pileimage on the
                                                                               // table
    public static final int    TableTextWeight         = 2;                   // The weight of the pilename on the
                                                                               // table

    /**
     * Strings.
     */

    public static final String MainDeckName            = "deck";              // The name of the starting pile
    public static final String IntentPileViewPileId    = "pileId";            // The identifier for the pile id of the
                                                                               // pile that the pileview will display
    public static final String IntentPileViewIp        = "pileViewIp";        // The identifier for the ip of the
                                                                               // device
                                                                               // in PileView
    public static final String IntentTableViewState    = "state";             // The identifier for the state of the
                                                                               // table
    public static final String IntentTableViewIP       = "ipAddr";            // The identifier for the ip of the
                                                                               // device
                                                                               // in TableView
    public static final String IntentTableViewHost     = "host";              // The identifier for the user being the
                                                                               // host in TableView
    public static final String PileHasNoOwner          = "noOwner";           // Indicates that a pile has no owner
    public static final String BackOfCardImage         = "rb";                // The name of the image for the back of
                                                                               // a
                                                                               // card
    public static final String ProtectedCardImage      = "bb";                // The name of the image for the
                                                                               // protected
                                                                               // back of a card
    public static final String NoPileImage             = "nopile";            // The name of the image for an empty
                                                                               // position on the table
    public static final String EmptyPileImage          = "empty";             // The name of the image for an empty
                                                                               // pile
    public static final String DefaultIpAddress        = "192.168.0.1";       // The default ip address to show when
                                                                               // entering the ip address

}
