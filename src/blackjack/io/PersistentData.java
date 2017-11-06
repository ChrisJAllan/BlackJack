/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.io;

import java.io.Serializable;

import java.util.ArrayList;

/**
 *
 * @author chris
 */
public class PersistentData implements Serializable
{
	public PersistentData() {
		lastPlayers = new ArrayList<>();
		playerData  = new ArrayList<>();
	}
	
	public ArrayList<String> lastPlayers;
	public ArrayList<PlayerScore> playerData;
}
