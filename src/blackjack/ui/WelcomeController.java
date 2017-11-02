/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.ui;

import blackjack.Game;

import chrisallan.util.Binding;

import java.io.IOException;

import java.util.ArrayList;

import javafx.event.EventTarget;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class WelcomeController
{
	public WelcomeController(Game game)
	{
		this.game = game;
		
		names = new ArrayList<>();
	}
	
	public void run(Stage stage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Welcome.fxml"));
		loader.setController(this);
		root = loader.load();
		
		nameGrid = (GridPane) root.lookup("#nameGrid");
		
		addPlayer();
		
		Scene scene = new Scene(root);
		
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML private void addPlayer()
	{
		int num = names.size() + 1;
		names.add(String.format("Player %d", num));
		
		rebuildGrid();
	}
	
	@FXML private void startGame()
	{
		/* TODO:
		 *   Check for empty
		 *   Check for duplicates
		 *   Check for zero score
		 */
		game.setPlayers(names);
	}
	
	private void rebuildGrid()
	{
		nameGrid.getChildren().clear();
		
		for (int i = 0; i < names.size(); ++i) {
			final int n = i;
			HBox nameContainer = new HBox();
			TextField nameBox = new TextField(names.get(i));
			HBox.setHgrow(nameBox, Priority.ALWAYS);
			nameBox.setOnKeyReleased((event) -> { names.set(n, ((TextField) event.getSource()).getText()); });
			nameContainer.getChildren().add(nameBox);

			HBox xContainer = new HBox();
			Button xButton = new Button("X");
			xButton.getStyleClass().add("removePlayer");
			xButton.setOnAction((event) -> {
				Object obj = event.getSource();
				int j = GridPane.getRowIndex(((Node) obj).getParent());
				names.remove(j);
				rebuildGrid();
			});
			xContainer.getChildren().add(xButton);

			nameGrid.add(nameContainer, 1, i);
			nameGrid.add(xContainer, 2, i);
		}
		
		root.lookup("#addButton").setDisable(names.size() >= 4);
		root.lookup("#startButton").setDisable(names.size() == 0);
	}
	
	private ArrayList<String> names;
	
	private Game game;
	private Pane root;
	private GridPane nameGrid;
}
