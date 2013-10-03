package se.chalmers.touchdeck.gamecontroller;

import java.io.Serializable;

import se.chalmers.touchdeck.models.Card;

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
		move, flip, create, connect, disconnect, shuffle, delete, rename
	}

	private Op		mOp;
	private Integer	mPile1;
	private Integer	mPile2;
	private Card	mCard;
	private String	mName;
	private String	mIpAddr;

	// connect / disconnect
	public Operation(Op op) {
		mOp = op;
	}

	// shuffle / delete
	public Operation(Op op, Integer pile1) {
		mOp = op;
		mPile1 = pile1;
	}

	// create / rename
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
	 * @param mCard the mCard to set
	 */
	public void setCard(Card mCard) {
		this.mCard = mCard;
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.mName = name;
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
