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

/**
 * Class representing the operations that can be made on cards and piles
 * 
 * @author group17
 */
public class Operation implements Serializable {
	/**
	 * Serializable
	 */
	private static final long	serialVersionUID	= -2413214187514892785L;

	public enum Op {
		move, flip, create, connect, shuffle, delete, rename, faceUp, faceDown, moveAll, protect, unprotect, disconnect, pileMove, restart
	}

	private Op		mOp;
	private Integer	mPile1;
	private Integer	mPile2;
	private Card	mCard;
	private String	mName;
	private String	mIpAddr;

	// connect / disconnect / restart
	public Operation(Op op) {
		mOp = op;
	}

	// shuffle / delete / faceUp / faceDown
	public Operation(Op op, Integer pile1) {
		mOp = op;
		mPile1 = pile1;
	}

	// create / rename / protect / unprotect
	public Operation(Op op, Integer pile1, String name) {
		mOp = op;
		mPile1 = pile1;
		mName = name;
	}

	// flip
	public Operation(Op op, Integer pile1, Card card) {
		mOp = op;
		mPile1 = pile1;
		mCard = card;
	}

	// move
	public Operation(Op op, Integer pile1, Integer pile2, Card card) {
		mOp = op;
		mPile1 = pile1;
		mPile2 = pile2;
		mCard = card;
	}

	/**
	 * @return the pile1
	 */
	public Integer getPile1() {
		return mPile1;
	}

	/**
	 * @param pile1 the pile1 to set
	 */
	public void setPile1(Integer pile1) {
		this.mPile1 = pile1;
	}

	/**
	 * @return the pile2
	 */
	public Integer getPile2() {
		return mPile2;
	}

	/**
	 * @param pile2 the pile2 to set
	 */
	public void setPile2(Integer pile2) {
		this.mPile2 = pile2;
	}

	/**
	 * @return the mCard
	 */
	public Card getCard() {
		return mCard;
	}

	/**
	 * @param card the card to set
	 */
	public void setCard(Card card) {
		mCard = card;
	}

	/**
	 * @return the op
	 */
	public Op getOp() {
		return mOp;
	}

	/**
	 * @param op the op to set
	 */
	public void setOp(Op op) {
		this.mOp = op;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @return the ipAddr
	 */
	public String getIpAddr() {
		return mIpAddr;
	}

	/**
	 * @param ipAddr the ipAddr to set
	 */
	public void setIpAddr(String ipAddr) {
		this.mIpAddr = ipAddr;
	}
}
