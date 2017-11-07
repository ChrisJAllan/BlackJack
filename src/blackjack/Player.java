/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import blackjack.io.PlayerScore;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author chris
 */
public class Player
{
	public Player(PlayerScore ps)
	{
		score = ps;
		
		cards = new ArrayList<>();
	}
	
	public void addCard(Card c)
	{
		cards.add(c);
		
		label.setText(String.format("%s: %d\n%d", getName(), getScore(), getValue()));
		
		if (getValue() > 21) {
			playerPane.getStyleClass().add("bust");
			hitButton.setDisable(true);
		}
	}
	
	public void clear()
	{
		cards.clear();
	}
	
	public int getCardCount()
	{
		return cards.size();
	}
	
	public Pane getCardPane()
	{
		return cardPane;
	}
	
	public Button getHitButton()
	{
		return hitButton;
	}
	
	public String getName()
	{
		return score.name;
	}
	
	public Pane getPlayerPane()
	{
		return playerPane;
	}
	
	public int getScore()
	{
		return score.score;
	}
	
	public int getValue()
	{
		return BlackJack.getScore(cards.toArray(new Card[0]));
	}
	
	public void setUI(Pane cardPane, Pane playerPane, Button hitButton, Label label)
	{
		this.cardPane   = cardPane;
		this.playerPane = playerPane;
		this.hitButton  = hitButton;
		this.label      = label;
		
		label.setText(String.format("%s: %d\n", getName(), getScore()));
	}
	
	public void wager()
	{
		score.score -= 10;
	}
	
	public void win()
	{
		score.score += 20;
	}
	
	PlayerScore score;
	Pane cardPane;
	Pane playerPane;
	Button hitButton;
	Label label;
	ArrayList<Card> cards;
}
