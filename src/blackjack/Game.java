/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import blackjack.io.*;
import blackjack.ui.WelcomeController;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author chris
 */
public class Game extends Application
{
	public Game()
	{
		loadData();
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		stage = primaryStage;
		
		showWelcome();
	}
	
	public void showWelcome()
	{
		WelcomeController welcome = new WelcomeController(this, gameData.lastPlayers);
		
		try {
			welcome.run(stage);
		}
		catch (IOException ex) {
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void setPlayers(ArrayList<String> names)
	{
		System.out.println(names);
		
		players = new ArrayList<>();
		
		names.forEach(n -> {
			PlayerScore score = gameData.playerData.stream().filter(ps -> { return ps.name.equals(n); }).findAny().orElse(null);
			if (score == null) {
				score = new PlayerScore(n);
			}
			players.add(new Player(score));
		});
		
		if (!checkPlayers())
		{
			return;
		}
		
		// Set current players in persistent data
		gameData.lastPlayers = new ArrayList<>();
		players.forEach(p -> { gameData.lastPlayers.add(p.getName()); });
		writeData();
		
		showGameBoard();
	}
	
	/**
	 * Checks for players with zero score
	 * @return true if all players are eligible
	 */
	public boolean checkPlayers()
	{
		Player bad[] = players.stream().filter(p -> p.getScore() == 0).toArray(Player[]::new);
		if (bad.length > 1) {
			// List string
			StringBuilder sb = new StringBuilder(bad[0].getName());
			for (int i = 1; i < bad.length - 1; ++i) {
				sb.append(", ");
				sb.append(bad[i].getName());
			}
			if (bad.length > 2) {
				sb.append(",");
			}
			sb.append(" and ");
			sb.append(bad[bad.length - 1].getName());
			
			// Error message
			Alert error = new Alert(
					Alert.AlertType.ERROR,
					String.format("Players %s are not eligible. (Score = 0)",
								  sb.toString()));
			error.show();
		}
		else if (bad.length > 0) {
			Alert error = new Alert(
					Alert.AlertType.ERROR,
					String.format("Player %s is not eligible. (Score = 0)", bad[0].getName()));
			error.show();
		}
		return (bad.length == 0);
	}
	
	public void showGameBoard()
	{
		
	}
	
	private void loadData()
	{
		File file = new File(filename);
		
		if (file.exists()) {
			try (
					FileInputStream fStream = new FileInputStream(file);
					ObjectInputStream oStream = new ObjectInputStream(fStream)
				) {
				
				gameData = (PersistentData) oStream.readObject();
				
				return;
			}
			catch (Exception e) { System.err.printf("Shit done broke: %s\n", e); }
		}
		
		gameData = new PersistentData();
	}
	
	private void writeData()
	{
		File file = new File(filename);
		
		try (
				FileOutputStream fStream = new FileOutputStream(file);
				ObjectOutputStream oStream = new ObjectOutputStream(fStream)
			) {
			
			oStream.writeObject(gameData);
		}
		catch (Exception e) { System.err.printf("Shit done broke: %s\n", e); }
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		launch(args);
	}
	
	private Stage stage;
	
	
	private ArrayList<Player> players;
	private String filename = "gamedata";
	private PersistentData gameData;
}
