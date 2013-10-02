package se.chalmers.touchdeck.gamecontroller;

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
		move, flip, create, connect, disconnect, shuffle, delete
	}

	private Op		op;
	private Integer	pile1;
	private Integer	pile2;
	private Integer	cardPos;
	private String	name;
	private String	ipAddr;

	public Operation(Op op, Integer pile1, Integer pile2, Integer cardPos, String name) {
		this.op = op;
		this.pile1 = pile1;
		this.pile2 = pile2;
		this.cardPos = cardPos;
		this.name = name;
	}

	/**
	 * @return the pile2
	 */
	public Integer getPile2() {
		return pile2;
	}

	/**
	 * @param pile2 the pile2 to set
	 */
	public void setPile2(Integer pile2) {
		this.pile2 = pile2;
	}

	/**
	 * @return the pile1
	 */
	public Integer getPile1() {
		return pile1;
	}

	/**
	 * @param pile1 the pile1 to set
	 */
	public void setPile1(Integer pile1) {
		this.pile1 = pile1;
	}

	/**
	 * @return the op
	 */
	public Op getOp() {
		return op;
	}

	/**
	 * @param op the op to set
	 */
	public void setOp(Op op) {
		this.op = op;
	}

	/**
	 * @return the cardPos
	 */
	public Integer getCardPos() {
		return cardPos;
	}

	/**
	 * @param cardPos the cardPos to set
	 */
	public void setCardPos(Integer cardPos) {
		this.cardPos = cardPos;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the ipAddr
	 */
	public String getIpAddr() {
		return ipAddr;
	}

	/**
	 * @param ipAddr the ipAddr to set
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
}
