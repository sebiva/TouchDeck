package se.chalmers.touchdeck.gamecontroller;

import java.io.Serializable;

import se.chalmers.touchdeck.enums.Rank;
import se.chalmers.touchdeck.enums.Suit;

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
		move, flip, create, connect, disconnect, shuffle, delete
	}

	private Op		mOp;
	private Integer	mPile1;
	private Integer	mPile2;
	private Suit	mSuit;
	private Rank	mRank;
	private String	mName;
	private String	mIpAddr;

	// connect / disconnect
	public Operation(Op op) {
		this.mOp = op;
	}

	// shuffle / delete
	public Operation(Op op, Integer pile1) {
		this.mOp = op;
		this.mPile1 = pile1;
	}

	// create
	public Operation(Op op, Integer pile1, String name) {
		this.mOp = op;
		this.mPile1 = pile1;
		this.mName = name;
	}

	// flip
	public Operation(Op op, Integer pile1, Rank rank, Suit suit) {
		this.mOp = op;
		this.mPile1 = pile1;
		this.setRank(rank);
		this.setSuit(suit);
	}

	// move
	public Operation(Op op, Integer pile1, Integer pile2, Rank rank, Suit suit) {
		this.mOp = op;
		this.mPile1 = pile1;
		this.mPile2 = pile2;
		this.setRank(rank);
		this.setSuit(suit);
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
	 * @return the rank
	 */
	public Rank getRank() {
		return mRank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(Rank rank) {
		this.mRank = rank;
	}

	/**
	 * @return the suit
	 */
	public Suit getSuit() {
		return mSuit;
	}

	/**
	 * @param suit the suit to set
	 */
	public void setSuit(Suit suit) {
		this.mSuit = suit;
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
