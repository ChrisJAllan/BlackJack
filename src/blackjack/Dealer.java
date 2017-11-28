/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import blackjack.io.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 *
 * @author chris
 */
public class Dealer extends Player
{
	public Dealer()
	{
		super(new PlayerScore("Dealer"));
	}
	
	@Override
	public void addCard(Card c)
	{
		cards.add(c);
		
		label.setText(String.format("%s\n%d", getName(), getValue()));
		
		if (getValue() > 21) {
			playerPane.getStyleClass().add("bust");
		}
		
		cardPane.getChildren().add(c.getNode());
	}
	
	@Override
	public void setUI(Pane cardPane, Pane playerPane, Button hitButton, Button standButton, Label label)
	{
		this.cardPane    = cardPane;
		this.playerPane  = playerPane;
		this.hitButton   = hitButton;
		this.standButton = standButton;
		this.label       = label;
		
		label.setText(String.format("%s", getName()));
	}
	
	@Override
	public void startTurn() { }
	@Override
	public void endTurn() { }
}
