/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.io;

import java.io.Serializable;

/**
 *
 * @author chris
 */
public class PlayerScore implements Serializable
{
	public PlayerScore(String name)
	{
		this.name = name;
		this.score = 100;
	}
	
	public String name;
	public int score;
}
