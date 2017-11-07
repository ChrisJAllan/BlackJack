/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.ui;

import blackjack.*;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class GameBoardController
{
	public GameBoardController(Game game, ArrayList<Player> players)
	{
		this.game = game;
		this.players = players;
		dealer = new Dealer();
	}
	
	public void run(Stage stage)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
		loader.setController(this);
		
		try {
			root = loader.load();
		}
		catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			return;
		}
		
		InitPlayers();
		
		Scene scene = new Scene(root);
		
		stage.setScene(scene);
		stage.show();
		
		Thread dealThread = new Thread(() -> { deal(); });
		dealThread.start();
	}
	
	public void InitPlayers()
	{
		int playercount = players.size();
		
		root.lookup("#player2Area").setVisible(true);
		root.lookup("#player3Area").setVisible(true);
		
		setPlayerUI(dealer, 0);
		
		for (Player p : players)
		{
			p.wager();
		}
		
		switch (playercount) {
		case 1:
			setPlayerUI(players.get(0), 1);
			root.lookup("#player2Area").setVisible(false);
			root.lookup("#player2Area").setManaged(false);
			root.lookup("#player3Area").setVisible(false);
			root.lookup("#player3Area").setManaged(false);
			break;
		case 2:
			setPlayerUI(players.get(0), 1);
			setPlayerUI(players.get(1), 3);
			root.lookup("#player2Area").setVisible(false);
			root.lookup("#player2Area").setManaged(false);
			break;
		case 3:
			setPlayerUI(players.get(0), 2);
			setPlayerUI(players.get(1), 1);
			setPlayerUI(players.get(2), 3);
			break;
		}
		
		//scene.lookup("#player2Area").autosize();
	}
	
	private void setPlayerUI(Player p, int i)
	{
		Button hitButton = null;
		if (i != 0) {
			hitButton = (Button) root.lookup(String.format("#player%dHit",  i));
			hitButton.setOnAction((e) -> { dealTo(p); });
		}
		
		p.setUI(
				(Pane)   root.lookup(String.format("#player%dBox",  i)),
				(Pane)   root.lookup(String.format("#player%dArea", i)),
				hitButton,
				(Label)  root.lookup(String.format("#player%dText", i))
		);
	}
	
	public void deal()
	{
		deck = Card.getDeck(true);
		
		try {
			for (Player p : players) {
				Platform.runLater(() -> { dealTo(p); });
				Thread.sleep(sleepTime);
			}
			
			Platform.runLater(() -> { dealTo(dealer); });
			Thread.sleep(sleepTime);
			
			for (Player p : players) {
				Platform.runLater(() -> { dealTo(p); });
				Thread.sleep(sleepTime);
			}
		}
		catch (InterruptedException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void dealTo(Player p)
	{
		Card c = deck.poll();
		p.addCard(c);
		
		if (p.getCardPane() instanceof GridPane) {
			GridPane pane = (GridPane) p.getCardPane();
			int column, row;
			int cardCount = p.getCardCount() - 1;
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
			p.getCardPane().getChildren().add(c.getNode());
		}
	}
	
	@FXML private void stand()
	{
		Thread dealThread = new Thread(() -> {
			while (dealer.getValue() < 17) {
				Platform.runLater(() -> { dealTo(dealer); });
				
				try {
					Thread.sleep(sleepTime);
				}
				catch (InterruptedException ex) {
					Logger.getLogger(GameBoardController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			Platform.runLater(() -> { afterRound(); });
		});
		
		dealThread.start();
	}
	
	private void afterRound()
	{
		ArrayList<Player> allPlayers = new ArrayList<>();
		allPlayers.addAll(players);
		allPlayers.add(dealer);
		
		// Find winner
		Player winner = allPlayers.stream()
				.filter(p -> p.getValue() <= 21)
				.reduce((p1, p2) -> {
					return (p1.getValue() > p2.getValue()) ? p1 : p2;
				}).orElse(null);
		
		if (winner != null)
		{
			int winValue = winner.getValue();
			
			for (Player p : allPlayers) {
				if (p.getValue() == winValue) {
					p.getPlayerPane().getStyleClass().add("winner");
					p.win();
				}
			}
		}
		
		game.writeData();
		
		Alert change = new Alert(
				Alert.AlertType.CONFIRMATION,
				"Keep same players?",
				ButtonType.YES,
				ButtonType.NO);
		
		Optional<ButtonType> type = change.showAndWait();
		
		if (game.checkPlayers() && type.get() == ButtonType.YES) {
			players.forEach((p) -> { p.clear(); });
			game.showGameBoard();
		}
		else {
			game.showWelcome();
		}
	}
	
	private final int sleepTime = 500;
	
	private final Game game;
	
	private Pane root;
	
	private final Player dealer;
	private final ArrayList<Player> players;
	
	private Queue<Card> deck;
}
