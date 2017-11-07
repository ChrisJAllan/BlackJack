/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

/**
 *
 * @author chris
 */
public class Card
{
	public enum Suit
	{
		CLUBS,
		DIAMONDS,
		HEARTS,
		SPADES
	}
	
	public enum Face
	{
		UP,
		DOWN
	}
	
	public Card(int value, Suit suit)
	{
		this.value = value;
		this.suit = suit;
	}
	
	public Node getNode()
	{
		if (node == null) {
			String sname = suit.name().toLowerCase();
			String resource;

			// Load image
			switch (value) {
			case 1:
				resource = String.format("cards/%s_of_%s.png", "ace", sname);
				break;
			case 11:
				resource = String.format("cards/%s_of_%s.png", "jack", sname);
				break;
			case 12:
				resource = String.format("cards/%s_of_%s.png", "queen", sname);
				break;
			case 13:
				resource = String.format("cards/%s_of_%s.png", "king", sname);
				break;
			default:
				resource = String.format("cards/%d_of_%s.png", value, sname);
			}

			front = new Image(getClass().getResourceAsStream(resource));
			back = new Image(getClass().getResourceAsStream("cards/back@2x.png"));
			
			node = new HBox();

			Pane pane = new Pane();
			pane.getStyleClass().add("card");

			view = new ImageView(front);
			view.preserveRatioProperty().set(true);
			view.minHeight(0);
			view.minWidth(10);
			pane.getChildren().add(view);

			Rectangle clip = new Rectangle();
			clip.widthProperty().bind(pane.widthProperty());
			clip.heightProperty().bind(pane.heightProperty());
			clip.setArcHeight(13);
			clip.setArcWidth(13);
			clip.setScaleX(0.99);
			clip.setScaleY(0.99);
			view.setClip(clip);

			node.setAlignment(Pos.CENTER);
			node.getChildren().add(pane);
			node.prefWidthProperty().bind(front.widthProperty());
			view.fitHeightProperty().bind(node.heightProperty());
		}
		
		return node;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public Suit getSuit()
	{
		return suit;
	}
	
	public static Queue<Card> getDeck(boolean shuffled)
	{
		ArrayList<Card> deck = new ArrayList<>();
		for (Suit s : Suit.values()) {
			for (int i = 1; i <= 13; ++i) {
				deck.add(new Card(i, s));
			}
		}
		
		if (shuffled) {
			Collections.shuffle(deck);
		}
		
		return new ArrayDeque(deck);
	}
	
	void setFace(Face f)
	{
		getNode();
		if (f == Face.UP) {
			view.setImage(front);
		}
		else {
			view.setImage(back);
		}
	}
	
	private final int value;
	private final Suit suit;
	private HBox node = null;
	private Image front, back;
	private ImageView view;
}
