/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

/**
 *
 * @author chris
 */
public class BlackJack
{
	// Calculates score for a set of cards
	public static int getScore(Card cards[])
	{
		int total = 0;
		int aceCount = 0;
		
		for (Card c : cards) {
			int value = c.getValue();
			
			if (value == 1) {
				total += 11;
				aceCount++;
			}
			else if (value > 10) {
				total += 10;
			}
			else {
				total += value;
			}
			
			if (total > 21 && aceCount > 0) {
				total -= 10;
				aceCount--;
			}
		}
		
		return total;
	}
}
