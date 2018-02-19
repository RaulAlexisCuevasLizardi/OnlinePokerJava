package onlinePoker;

/**
 * @author Raul A. Cuevas Lizardi
 * Cards have an image name that should link them to a unique image in the source folder,
 * they also have two integer values. The first integer value is that card's rank. A rank
 * of 13 is a king, 12 is a queen and so forth down to 1 which is an ace. The second integer
 * value represents that card's suit. A value of 1 on the second integer represents a spade,
 * 2 represents a heart, a 3 represents a club and a 4 represents a diamond.
 *
 */


public enum Cards {
	KING_OF_SPADES ("KS.png", 13, 1),
	KING_OF_HEARTS ("KH.png", 13, 2),
	KING_OF_CLUBS ("KC.png", 13, 3),
	KING_OF_DIAMONDS ("KD.png", 13, 4),
	
	
	QUEEN_OF_SPADES ("QS.png", 12, 1),
	QUEEN_OF_HEARTS ("QH.png", 12, 2),
	QUEEN_OF_CLUBS ("QC.png", 12, 3),
	QUEEN_OF_DIAMONDS ("QD.png", 12, 4),
	
	JACK_OF_SPADES ("JS.png", 11, 1),
	JACK_OF_HEARTS ("JH.png", 11, 2),
	JACK_OF_CLUBS ("JC.png", 11, 3),
	JACK_OF_DIAMONDS ("JD.png", 11, 4),
	
	TEN_OF_SPADES ("10S.png", 10, 1),
	TEN_OF_HEARTS ("10H.png", 10, 2),
	TEN_OF_CLUBS ("10C.png", 10, 3),
	TEN_OF_DIAMONDS ("10D.png", 10, 4),
	
	NINE_OF_SPADES ("9S.png", 9, 1),
	NINE_OF_HEARTS ("9H.png", 9, 2),
	NINE_OF_CLUBS ("9C.png", 9, 3),
	NINE_OF_DIAMONDS ("9D.png", 9, 4),
	
	EIGHT_OF_SPADES ("8S.png", 8, 1),
	EIGHT_OF_HEARTS ("8H.png", 8, 2),
	EIGHT_OF_CLUBS ("8C.png", 8, 3),
	EIGHT_OF_DIAMONDS ("8D.png", 8, 4),
	
	SEVEN_OF_SPADES ("7S.png", 7, 1),
	SEVEN_OF_HEARTS ("7H.png", 7, 2),
	SEVEN_OF_CLUBS ("7C.png", 7, 3),
	SEVEN_OF_DIAMONDS ("7D.png", 7, 4),
	
	SIX_OF_SPADES ("6S.png", 6, 1),
	SIX_OF_HEARTS ("6H.png", 6, 2),
	SIX_OF_CLUBS ("6C.png", 6, 3),
	SIX_OF_DIAMONDS ("6D.png", 6, 4),
	
	FIVE_OF_SPADES ("5S.png", 5, 1),
	FIVE_OF_HEARTS ("5H.png", 5, 2),
	FIVE_OF_CLUBS ("5C.png", 5, 3),
	FIVE_OF_DIAMONDS ("5D.png", 5, 4),
	
	FOUR_OF_SPADES ("4S.png", 4, 1),
	FOUR_OF_HEARTS ("4H.png", 4, 2),
	FOUR_OF_CLUBS ("4C.png", 4, 3),
	FOUR_OF_DIAMONDS ("4D.png", 4, 4),
	 
	THREE_OF_SPADES ("3S.png", 3, 1),
	THREE_OF_HEARTS ("3H.png", 3, 2),
	THREE_OF_CLUBS ("3C.png", 3, 3),
	THREE_OF_DIAMONDS ("3D.png", 3, 4),
	
	TWO_OF_SPADES ("2S.png", 2, 1),
	TWO_OF_HEARTS ("2H.png", 2, 2),
	TWO_OF_CLUBS ("2C.png", 2, 3),
	TWO_OF_DIAMONDS ("2D.png", 2, 4),
	
	ACE_OF_SPADES ("AS.png", 1, 1),
	ACE_OF_HEARTS ("AH.png", 1, 2),
	ACE_OF_CLUBS ("AC.png", 1, 3),
	ACE_OF_DIAMONDS ("AD.png", 1, 4);
	
	private final int value;
	private final String imageName;
	private final int suit;
	
	Cards(String imageName, int value, int suit){
		this.imageName = imageName;
		this.value = value;
		this.suit = suit;
	}
	
	public String getCardImageName() {
		return this.imageName;
	}
	
	public int getCardValue() {
		return this.value;
	}
	
	public int getCardSuit() {
		return this.suit;
	}
}
