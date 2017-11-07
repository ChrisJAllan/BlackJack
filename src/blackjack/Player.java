/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import blackjack.io.PlayerScore;

/**
 *
 * @author chris
 */
public class Player
{
	public Player(PlayerScore ps)
	{
		score = ps;
	}
	
	public String getName()
	{
		return score.name;
	}
	
	public int getScore()
	{
		return score.score;
	}
	
	PlayerScore score;
}
