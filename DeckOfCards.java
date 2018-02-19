package onlinePoker;

import java.util.Random;
import java.util.Stack;

public class DeckOfCards {
	Stack<Cards> cards;

	public DeckOfCards() {
		super();
		this.cards = new Stack<Cards>();
		for(Cards c: Cards.values()){
			if(this.cards.isEmpty()){
				this.cards.push(c);
			}
			else{
				this.cards.add(new Random().nextInt(this.cards.size() + 1), c);
			}
		}
	}

	public Cards DrawTopCard() throws EmptyDeckException{
		if(this.cards.isEmpty()) 
			throw new EmptyDeckException();
		return this.cards.pop();
	}

	@Override
	public String toString() {
		String cardsString = "";
		for(int i = 0; i < cards.size(); i++){
			cardsString += cards.get(i) + ",\n";				
		}
		return "Deck [cards=\n" + cardsString + "]";
	}
}
