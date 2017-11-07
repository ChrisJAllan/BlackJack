/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import blackjack.io.PlayerScore;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

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
		}
		
		if (cardPane instanceof GridPane) {
			GridPane pane = (GridPane) cardPane;
			int column, row;
			int cardCount = cards.size() - 1;
			boolean newColumn = false, newRow;
			
			if (cardCount < 6) {
				column = (cardCount / 3) + 1;
				row = (cardCount % 3) + 1;
				newColumn = (cardCount == 0 || cardCount == 3);
				newRow = (cardCount < 3);
			}
			else {
				column = (cardCount % 2) + 1;
				row = (cardCount / 2) + 1;
				newRow = (cardCount % 2 == 0);
			}
			
			if (newColumn) {
				ColumnConstraints columnC = new ColumnConstraints();
				columnC.setPercentWidth(100);
				pane.getColumnConstraints().add(columnC);
			}

			if (newRow) {
				RowConstraints rowC = new RowConstraints();
				rowC.setPercentHeight(50);
				pane.getRowConstraints().add(rowC);
			}
			
			pane.add(c.getNode(), column, row);
		}
		else {
			cardPane.getChildren().add(c.getNode());
		}
	}
	
	public void clear()
	{
		cards.clear();
	}
	
	public String getName()
	{
		return score.name;
	}
	
	public int getScore()
	{
		return score.score;
	}
	
	public int getValue()
	{
		return BlackJack.getScore(cards.toArray(new Card[0]));
	}
	
	public void setUI(Pane cardPane, Pane playerPane, Button hitButton, Button standButton, Label label)
	{
		this.cardPane    = cardPane;
		this.playerPane  = playerPane;
		this.hitButton   = hitButton;
		this.standButton = standButton;
		this.label       = label;
		
		label.setText(String.format("%s: %d\n", getName(), getScore()));
		
		this.animationThread = new Thread(() -> {
			while (active) {
				Platform.runLater(() -> {
					if (playerPane.getStyleClass().contains("active")) {
						playerPane.getStyleClass().remove("active");
					}
					else {
						playerPane.getStyleClass().add("active");
					}
				});
				try {
					Thread.sleep(500);
				}
				catch (InterruptedException ex) {
					Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
	}
	
	public void startTurn()
	{
		active = true;
		hitButton.setDisable(false);
		standButton.setDisable(false);
		
		animationThread.start();
	}
	
	public void endTurn()
	{
		active = false;
		hitButton.setDisable(true);
		standButton.setDisable(true);
		
		playerPane.getStyleClass().remove("active");
	}
	
	public void wager()
	{
		score.score -= 10;
	}
	
	public void win()
	{
		score.score += 20;
		
		playerPane.getStyleClass().add("winner");
	}
	
	private Thread animationThread;
	
	private boolean active = false;
	
	PlayerScore score;
	Pane cardPane;
	Pane playerPane;
	Button hitButton, standButton;
	Label label;
	ArrayList<Card> cards;
}
