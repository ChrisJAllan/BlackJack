/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import blackjack.ui.WelcomeController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author chris
 */
public class Game extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		stage = primaryStage;
		
		showWelcome();
	}
	
	public void showWelcome()
	{
		WelcomeController welcome = new WelcomeController(this);
		
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
	}
	
	public void showGameBoard()
	{
		
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		launch(args);
	}
	
	Stage stage;
}
