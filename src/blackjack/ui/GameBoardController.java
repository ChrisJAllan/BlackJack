/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.ui;

import blackjack.*;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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
		this.players = new ArrayList<>();
		dealer = new Dealer();
		this.players.add(dealer);
		this.players.addAll(players);
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
		
		Thread animationThread = new Thread(() -> { deal(); });
		animationThread.start();
	}
	
	public void InitPlayers()
	{
		int playercount = players.size() - 1;
		
		root.lookup("#player2Area").setVisible(true);
		root.lookup("#player3Area").setVisible(true);
		
		setPlayerUI(dealer, 0);
		
		switch (playercount) {
		case 1:
			setPlayerUI(players.get(1), 1);
			root.lookup("#player2Area").setVisible(false);
			root.lookup("#player2Area").setManaged(false);
			root.lookup("#player3Area").setVisible(false);
			root.lookup("#player3Area").setManaged(false);
			break;
		case 2:
			setPlayerUI(players.get(1), 1);
			setPlayerUI(players.get(2), 3);
			root.lookup("#player2Area").setVisible(false);
			root.lookup("#player2Area").setManaged(false);
			break;
		case 3:
			setPlayerUI(players.get(1), 2);
			setPlayerUI(players.get(2), 1);
			setPlayerUI(players.get(3), 3);
			break;
		}
		
		for (Player p : players) {
			p.wager();
			p.endTurn();
		}
	}
	
	private void setPlayerUI(Player p, int i)
	{
		Button hitButton = null, standButton = null;
		if (i != 0) {
			hitButton = (Button) root.lookup(String.format("#player%dHit", i));
			hitButton.setOnAction((e) -> { dealTo(p); });
			standButton = (Button) root.lookup(String.format("#player%dStand", i));
		}
		
		p.setUI(
				(Pane)   root.lookup(String.format("#player%dBox",  i)),
				(Pane)   root.lookup(String.format("#player%dArea", i)),
				hitButton,
				standButton,
				(Label)  root.lookup(String.format("#player%dText", i))
		);
	}
	
	public void deal()
	{
		deck = Card.getDeck(true);
		
		try {
			Thread.sleep(sleepTime);
			
			// Skip dealer on first deal (European rules, doesn't need card backside)
			for (int i = 1; i < players.size(); ++i) {
				Player p = players.get(i);
				Platform.runLater(() -> { dealTo(p); });
				Thread.sleep(sleepTime);
			}
			
			for (Player p : players) {
				Platform.runLater(() -> { dealTo(p); });
				Thread.sleep(sleepTime);
			}
		}
		catch (InterruptedException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
		
		currentPlayer = 0;
		
		Platform.runLater(() -> { nextPlayer(); });
	}
	
	private void dealTo(Player p)
	{
		Card c = deck.poll();
		p.addCard(c);
		
		if (p.getValue() > 21 && currentPlayer != 0) {
			nextPlayer();
		}
	}
	
	@FXML
	private void nextPlayer()
	{
		players.get(currentPlayer).endTurn();
		
		currentPlayer = (currentPlayer + 1) % players.size();
		
		if (currentPlayer == 0) {
			stand();
		}
		else {
			players.get(currentPlayer).startTurn();
		}
	}
	
	private void stand()
	{
		Thread animationThread = new Thread(() -> {
			try {
				Thread.sleep(sleepTime);
				while (dealer.getValue() < 17) {
					Platform.runLater(() -> { dealTo(dealer); });
					Thread.sleep(sleepTime);
				}
			}
			catch (InterruptedException ex) {
				Logger.getLogger(GameBoardController.class.getName()).log(Level.SEVERE, null, ex);
			}
			Platform.runLater(() -> { afterRound(); });
		});
		
		animationThread.start();
	}
	
	private void afterRound()
	{
		// Find winner
		Player winner = players.stream()
				.filter(p -> p.getValue() <= 21)
				.reduce((p1, p2) -> {
					return (p1.getValue() > p2.getValue()) ? p1 : p2;
				})
				.orElse(null);
		
		if (winner != null)
		{
			int winValue = winner.getValue();
			
			players.stream().filter((p) -> (p.getValue() == winValue)).forEach((p) -> {
				p.win();
			});
		}
		
		game.afterRound();
	}
	
	private final int sleepTime = 500;
	
	private final Game game;
	
	private int currentPlayer;
	
	private Pane root;
	
	private final Player dealer;
	private final ArrayList<Player> players;
	
	private Queue<Card> deck;
}
