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

package se.chalmers.touchdeck.misc.exceptions;

/**
 * Exception thrown when a card is not found
 * 
 * @author group17
 */
public class CardNotFoundException extends Exception {
	private static final long	serialVersionUID	= 6883143284907049675L;

	public CardNotFoundException(String msg) {
		super(msg);
	}

	public CardNotFoundException(String msg, Exception cause) {
		super(msg, cause);
	}
}
