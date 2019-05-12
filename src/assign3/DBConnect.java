package assign3;

import java.sql.*;
import java.util.ArrayList;

public class DBConnect {
	
	private ArrayList<MetropolisInfo> metropolises;
	private Statement stmt;
	private Connection con;
	
	public DBConnect(ArrayList<MetropolisInfo> metropolises) {
		this.metropolises = metropolises;
	}
	
	private void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + MyDBInfo.MYSQL_DATABASE_SERVER + "?autoReconnect=true&useSSL=false&",
														 MyDBInfo.MYSQL_USERNAME, MyDBInfo.MYSQL_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void readDB(String sql){
		stmt = null;
		con = null;
		try {
			connect();
			stmt = con.createStatement();
			metropolises.clear();
			
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				MetropolisInfo newMetro = new MetropolisInfo(rs.getString(1), rs.getString(2), rs.getInt(3));
				metropolises.add(newMetro);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	         if(stmt!=null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         if(con!=null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public void add(MetropolisInfo metro) {
		connect();
		try {
			PreparedStatement st = con.prepareStatement("INSERT into metropolises (metropolis, continent, population) VALUES (?, ?, ?)");
			st.setString(1, metro.getMetropolis());
			st.setString(2, metro.getContinent());
			st.setInt(3, metro.getPopulation());
			st.executeUpdate();
			metropolises.clear();
			metropolises.add(metro);
		} catch (Exception e) {
		}
	}
	
	public void search(MetropolisInfo metro, boolean partial, String largerEqualOrSmaller) {
		String sql = "SELECT * from metropolises";
		
		if(partial) {
			sql += " where metropolises.metropolis like '%" + metro.getMetropolis() + "%' and metropolises.continent like '%" + metro.getContinent();
		} else {
			sql += " where metropolises.metropolis = '" + metro.getMetropolis() + "' and metropolises.continent = '" + metro.getContinent();
		}
		
		if(largerEqualOrSmaller.equals("Larger Than")) {
			sql += "' and metropolises.population > " + metro.getPopulation() + ";";
		} else if(largerEqualOrSmaller.equals("Equals To")) {
			sql += "' and metropolises.population = " + metro.getPopulation() + ";";
		} else {
			sql += "' and metropolises.population < " + metro.getPopulation() + ";";
		}
		
		readDB(sql);
		
	}
	
	public void getFullData() {
		readDB("Select * from metropolises");
	}
	
	
	
	
}
