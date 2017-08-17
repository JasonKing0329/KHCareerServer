package com.king.mytennis.db;

public class PublicDatabase {

	public PublicDatabase() {
//		new MatchManager().createMatchData();
//		new MatchManager().updateWeeks();
		new MatchManager().createPlayerData();
	}
	
	public static void main(String[] args) throws Exception {
		new PublicDatabase();
	}
}
