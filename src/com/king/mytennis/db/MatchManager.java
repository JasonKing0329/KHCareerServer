package com.king.mytennis.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchManager {

	private Map<String, MatchBean> matchMap;
	private Map<String, Integer> weekMap;
	private Map<String, PlayerBean> playerMap;
	
	public MatchManager() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		matchMap = new HashMap<>();
		weekMap = new HashMap<>();
		playerMap = new HashMap<>();
	}
	
	private Connection connect(String db) {
		Connection connection = null;
        try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + db);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return connection;
	}
	
	public void createMatchData() {
		List<MatchBean> matchList = new ArrayList<MatchBean>();
		List<MatchNameBean> matchNameList = new ArrayList<MatchNameBean>();
		
		queryDataFromRecord("mytennis", matchList, matchNameList);
		queryDataFromRecord("mytennis_Flamenco", matchList, matchNameList);
		queryDataFromRecord("mytennis_Henry", matchList, matchNameList);
		queryDataFromRecord("mytennis_TianQi", matchList, matchNameList);
		insertDatasToPublic(matchList, matchNameList);
		
		System.out.println("matchList size " + matchList.size());
		System.out.println("matchNameList size " + matchNameList.size());
	}

	private void insertDatasToPublic(List<MatchBean> matchList,
			List<MatchNameBean> matchNameList) {
		Connection connection = connect("db/mytennis_public");
		try {
			String sql = "INSERT INTO _match(level, court, region, country, city, week, month)"
					+ " VALUES(?, ?, ?, ? , ?, ?, ?)";
			PreparedStatement stmt = connection.prepareStatement(sql);
			for (MatchBean bean:matchList) {
				stmt.setString(1, bean.getLevel());
				stmt.setString(2, bean.getCourt());
				stmt.setString(3, bean.getRegion());
				stmt.setString(4, bean.getCountry());
				stmt.setString(5, bean.getCity());
				stmt.setInt(6, bean.getWeek());
				stmt.setInt(7, bean.getMonth());
				stmt.executeUpdate();
			}
			stmt.close();

			sql = "INSERT INTO _match_name(name, match_id)"
					+ " VALUES(?, ?)";
			stmt = connection.prepareStatement(sql);
			for (MatchNameBean bean:matchNameList) {
				stmt.setString(1, bean.getName());
				stmt.setInt(2, bean.getMatchId());
				stmt.executeUpdate();
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void queryDataFromRecord(String database, List<MatchBean> matchList,
			List<MatchNameBean> matchNameList) {
		Connection connection = connect("db/" + database);
		try {
			Statement stmt = connection.createStatement();
			String sql = "SELECT level, court, region, match_country, match_city, match, date_str FROM record";
			ResultSet set = stmt.executeQuery(sql);
			int id = 1;
			while (set.next()) {
				String match = set.getString(6);
				MatchBean bean = matchMap.get(match);
				if (bean == null) {
					bean = new MatchBean();
					bean.setLevel(set.getString(1));
					bean.setCourt(set.getString(2));
					bean.setRegion(set.getString(3));
					bean.setCountry(set.getString(4));
					bean.setCity(set.getString(5));
					String date = set.getString(7);
					bean.setMonth(Integer.parseInt(date.split("-")[1]));
					bean.setId(id);
					matchList.add(bean);
					matchMap.put(match, bean);
					
					MatchNameBean mnBean = new MatchNameBean();
					mnBean.setId(id);
					mnBean.setName(match);
					mnBean.setMatchId(id);
					matchNameList.add(mnBean);
					
					id ++;
				}
			}
			set.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void updateWeeks() {
		Connection connection = connect("db/mytennis_public");
		try {
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM match_seq";
			ResultSet set = stmt.executeQuery(sql);
			while (set.next()) {
				weekMap.put(set.getString(2), set.getInt(3));
			}
			set.close();
			stmt.close();

			sql = "UPDATE _match SET week = ? WHERE _id=(SELECT match_id FROM _match_name WHERE name= ?)";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			for (String name:weekMap.keySet()) {
				pstmt.setInt(1, weekMap.get(name));
				pstmt.setString(2, name);
				pstmt.executeUpdate();
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createPlayerData() {
		List<PlayerBean> playerList = new ArrayList<PlayerBean>();
		
		queryDataFromRecord("mytennis", playerList);
		queryDataFromRecord("mytennis_Flamenco", playerList);
		queryDataFromRecord("mytennis_Henry", playerList);
		queryDataFromRecord("mytennis_TianQi", playerList);
		insertDatasToPublic(playerList);
		
		System.out.println("playerList size " + playerList.size());
	}

	private void insertDatasToPublic(List<PlayerBean> playerList) {
		Connection connection = connect("db/mytennis_public");
		try {
			String sql = "INSERT INTO _player(name_chn, name_pinyin, country)"
					+ " VALUES(?, ?, ?)";
			PreparedStatement stmt = connection.prepareStatement(sql);
			for (PlayerBean bean:playerList) {
				stmt.setString(1, bean.getNameChn());
				stmt.setString(2, bean.getNamePinyin());
				stmt.setString(3, bean.getCountry());
				stmt.executeUpdate();
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("insertDatasToPublic done");
	}

	private void queryDataFromRecord(String database, List<PlayerBean> playerList) {
		Connection connection = connect("db/" + database);
		try {
			Statement stmt = connection.createStatement();
			String sql = "SELECT competitor, competitor_country FROM record";
			ResultSet set = stmt.executeQuery(sql);
			while (set.next()) {
				String player = set.getString(1);
				PlayerBean bean = playerMap.get(player);
				if (bean == null) {
					bean = new PlayerBean();
					bean.setNameChn(player);
					bean.setNamePinyin(PinyinUtil.getPinyin(player));
					bean.setCountry(set.getString(2));
					playerList.add(bean);
					playerMap.put(player, bean);
				}
			}
			set.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("queryDataFromRecord " + database + " done");
	}
}
