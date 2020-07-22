package defaultPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

// Assumes database is a MySQL database with tables Athlete, Meet, etc. as predefined.

public class RunningDatabase {
	private String url;
	private Connection con;
	private boolean isConnected = false;

	// creates server url based on given info
	public RunningDatabase (String serverHost, int portNum, String databaseName) {
		url = "jdbc:mysql://" + serverHost + ":" + Integer.toString(portNum)
			+ "/" + databaseName;
	}
	
	// creates server url using default port number
	public RunningDatabase (String serverHost, String databaseName) {
		this(serverHost, 3306, databaseName);
	}
	
	// logs into server with given credentials
	// returns true if successful
	public boolean login(String username, String password) {
		// resets connection to false
		isConnected = false;
		// attempts to connect and login to database
		try {
			con = DriverManager.getConnection(url, username, password);
			isConnected = true;
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(RunningDatabase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
		return isConnected;
	}
	
	// executes a simple given query if connected, outputs results to console
	// returns true if successful
	public boolean executeQuery(String query) {
		if (isConnected) {
			try {
				// executes query, capturing the returned result set and meta data for result set
				PreparedStatement pst = con.prepareStatement(query);
			    ResultSet rs = pst.executeQuery();
			    ResultSetMetaData meta = rs.getMetaData();
			    // gets number of rows from meta data
			    int numColumns = meta.getColumnCount();
			    // prints relation name
			    System.out.println("Table " + meta.getTableName(1) + ":");
			    // then prints each column name
			    for (int i = 1; i <= numColumns; i++)
		    		if (i != numColumns) {
		    			System.out.print(String.format("%-" +
		    					Math.max(meta.getColumnDisplaySize(i) + 1, meta.getTableName(1).length() + 1) +
		    					"s", meta.getColumnName(i)));
		    		}else 
	    				System.out.println(meta.getColumnName(i));
		    		
			    // prints out all results from query
			    // iterates along each row, printing to system.out
			    while (rs.next()) {
			    	for (int i = 1; i <= numColumns; i++)
			    		if (i != numColumns)
			    			System.out.print(String.format("%-" +
			    					Math.max(meta.getColumnDisplaySize(i) + 1, meta.getTableName(1).length() + 1) +
			    					"s", rs.getString(i)));
		    			else 
		    				System.out.println(rs.getString(i));
			    }
			    return true;
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(RunningDatabase.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		} else {
			System.out.println("ERROR: Cannot execute query without logging in to database first.");
		}
		return false;	// if something failed along the way
	}
	
	// executes a simple update if connected,
	// returns true if successful
	public boolean executeUpdate(String update) {
		if (isConnected) {
			try {
				// executes update
				PreparedStatement pst = con.prepareStatement(update);
				pst.executeUpdate(update);
			    return true;
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(RunningDatabase.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		} else {
			System.out.println("ERROR: Cannot execute update without logging in to database first.");
		}
		return false;	// if something failed along the way
	}
	
	
	
	/* The following inserts are to insert into each one of the relations */
	// each one returns true if successful
	// autochecks for existence of rows with given primary key already,
	// 		preventing the duplicate insertion from taking place
	// each one has two overloaded methods: one that accepts arrays and one that only accepts a single row to insert
	
	
	public boolean insert_into_athlete(long [] ID, String [] name) {
		if (isConnected) {
			try {
				// creates insert and select update / query
				String insertString = "INSERT INTO athlete(ID, name) VALUES(?, ?)";
				String selectString = "SELECT * FROM athlete WHERE ID = ?";
				PreparedStatement insertStatement = con.prepareStatement(insertString);
				PreparedStatement selectStatement = con.prepareStatement(selectString);
				// checks for row existence before attempting insert
				for (int i = 0; i < ID.length; i++) {
					// checks for existence of given primary key
					selectStatement.setLong(1, ID[i]);
					// if given primary key row does not exist then continue to insert
					if (!selectStatement.executeQuery().next()) {
						insertStatement.setLong(1, ID[i]);
						insertStatement.setString(2, name[i]);
						insertStatement.executeUpdate();
					}
				}
			    return true;
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(RunningDatabase.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		} else {
			System.out.println("ERROR: Cannot execute insert without logging in to database first.");
		}
		return false;	// if something failed along the way
	}
	public boolean insert_into_athlete(long ID, String name) {
		long [] ID_arr = new long[1];
		String [] name_arr = new String[1];
		ID_arr[0] = ID;
		name_arr[0] = name;
		return insert_into_athlete(ID_arr, name_arr);
	}

	public boolean insert_into_athlete_competes_for(long [] athlete_id, String [] organization_name) {
		if (isConnected) {
			try {
				// creates insert and select update / query
				String insertString = "INSERT INTO athlete_competes_for(athlete_id, organization_name) VALUES(?, ?)";
				String selectString = "SELECT * FROM athlete_competes_for WHERE athlete_id = ?";
				PreparedStatement insertStatement = con.prepareStatement(insertString);
				PreparedStatement selectStatement = con.prepareStatement(selectString);
				// checks for row existence before attempting insert
				for (int i = 0; i < athlete_id.length; i++) {
					// checks for existence of given primary key
					selectStatement.setLong(1, athlete_id[i]);
					// if given primary key row does not exist then continue to insert
					if (!selectStatement.executeQuery().next()) {
						insertStatement.setLong(1, athlete_id[i]);
						insertStatement.setString(2, organization_name[i]);
						insertStatement.executeUpdate();
					}
				}
			    return true;
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(RunningDatabase.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		} else {
			System.out.println("ERROR: Cannot execute insert without logging in to database first.");
		}
		return false;	// if something failed along the way
	}
	public boolean insert_into_athlete_competes_for(long athlete_id, String organization_name) {
		long [] athlete_id_arr = new long[1];
		String [] organization_name_arr = new String[1];
		athlete_id_arr[0] = athlete_id;
		organization_name_arr[0] = organization_name;
		return insert_into_athlete_competes_for(athlete_id_arr, organization_name_arr);
	}
	
	public boolean insert_into_course(String [] name, String [] state, String [] city, String [] type) {
		if (isConnected) {
			try {
				// creates insert and select update / query
				String insertString = "INSERT INTO course(name, state, city, type) VALUES(?, ?, ?, ?)";
				String selectString = "SELECT * FROM course WHERE name = ?" +
															" AND state = ?" +
															" AND city = ?";
				PreparedStatement insertStatement = con.prepareStatement(insertString);
				PreparedStatement selectStatement = con.prepareStatement(selectString);
				// checks for row existence before attempting insert
				for (int i = 0; i < name.length; i++) {
					// checks for existence of given primary key
					selectStatement.setString(1, name[i]);
					selectStatement.setString(2, state[i]);
					selectStatement.setString(3, city[i]);
					// if given primary key row does not exist then continue to insert
					if (!selectStatement.executeQuery().next()) {
						insertStatement.setString(1, name[i]);
						insertStatement.setString(2, state[i]);
						insertStatement.setString(3, city[i]);
						insertStatement.setString(4, type[i]);
						insertStatement.executeUpdate();
					}
				}
			    return true;
			    
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(RunningDatabase.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		} else {
			System.out.println("ERROR: Cannot execute insert without logging in to database first.");
		}
		return false;	// if something failed along the way
	}
	public boolean insert_into_course(String name, String state, String city, String type) {
		String [] name_arr = new String[1];
		String [] state_arr = new String[1];
		String [] city_arr = new String[1];
		String [] type_arr = new String[1];
		name_arr[0] = name;
		state_arr[0] = state;
		city_arr[0] = city;
		type_arr[0] = type;
		return insert_into_course(name_arr, state_arr, city_arr, type_arr);
	}
	
	public boolean insert_into_league_contains(String [] league_name, String [] organization_name) {
		if (isConnected) {
			try {
				// creates insert and select update / query
				String insertString = "INSERT INTO league_contains(league_name, organization_name) VALUES(?, ?)";
				String selectString = "SELECT * FROM league_contains WHERE league_name = ?" +
																	 " AND organization_name = ?";
				PreparedStatement insertStatement = con.prepareStatement(insertString);
				PreparedStatement selectStatement = con.prepareStatement(selectString);
				// checks for row existence before attempting insert
				for (int i = 0; i < league_name.length; i++) {
					// checks for existence of given primary key
					selectStatement.setString(1, league_name[i]);
					selectStatement.setString(2, organization_name[i]);
					// if given primary key row does not exist then continue to insert
					if (!selectStatement.executeQuery().next()) {
						insertStatement.setString(1, league_name[i]);
						insertStatement.setString(2, organization_name[i]);
						insertStatement.executeUpdate();
					}
				}
			    return true;
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(RunningDatabase.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		} else {
			System.out.println("ERROR: Cannot execute insert without logging in to database first.");
		}
		return false;	// if something failed along the way
	}
	public boolean insert_into_league_contains(String league_name, String organization_name) {
		String [] league_name_arr = new String[1];
		String [] organization_name_arr = new String[1];
		league_name_arr[0] = league_name;
		organization_name_arr[0] = organization_name;
		return insert_into_league_contains(league_name_arr, organization_name_arr);
	}
	
	// * date String must be in format of "yyyy-[m]m-[d]d", leading zeros for mm and dd may be omitted
	// (this can be achieved by Date.toString() method)
	public boolean insert_into_meet(long [] ID, boolean [] isXC, String [] name, String [] start_date, short [] length,
			String [] course_name, String [] course_state, String [] course_city) {
		if (isConnected) {
			try {
				// creates insert and select update / query
				String insertString = "INSERT INTO meet(ID, isXC, name, start_date, length, course_name, course_state, course_city)" +
												 " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
				String selectString = "SELECT * FROM meet WHERE ID = ?" +
														  " AND isXC = ?";
				PreparedStatement insertStatement = con.prepareStatement(insertString);
				PreparedStatement selectStatement = con.prepareStatement(selectString);
				// checks for row existence before attempting insert
				for (int i = 0; i < ID.length; i++) {
					// checks for existence of given primary key
					selectStatement.setLong(1, ID[i]);
					selectStatement.setBoolean(2, isXC[i]);
					// if given primary key row does not exist then continue to insert
					if (!selectStatement.executeQuery().next()) {
						
						// TEMPORARY
						if (name[i].length() > 60)
							name[i] = name[i].substring(0, 59);
						
						insertStatement.setLong(1, ID[i]);
						insertStatement.setBoolean(2, isXC[i]);
						insertStatement.setString(3, name[i]);
						insertStatement.setDate(4, java.sql.Date.valueOf(start_date[i]));
						insertStatement.setShort(5, length[i]);
						insertStatement.setString(6, course_name[i]);
						insertStatement.setString(7, course_state[i]);
						insertStatement.setString(8, course_city[i]);
						insertStatement.executeUpdate();
					}
				}
			    return true;
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(RunningDatabase.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		} else {
			System.out.println("ERROR: Cannot execute insert without logging in to database first.");
		}
		return false;	// if something failed along the way
	}
	public boolean insert_into_meet(long ID, boolean isXC, String name, String start_date, short length,
			String course_name, String course_state, String course_city) {
		long [] ID_arr = new long[1];
		boolean [] isXC_arr = new boolean[1];
		String [] name_arr = new String[1];
		String [] start_date_arr = new String[1];
		short [] length_arr = new short[1];
		String [] course_name_arr = new String[1];
		String [] course_state_arr = new String[1];
		String [] course_city_arr = new String[1];
		ID_arr[0] = ID;
		isXC_arr[0] = isXC;
		name_arr[0] = name;
		start_date_arr[0] = start_date;
		length_arr[0] = length;
		course_name_arr[0] = course_name;
		course_state_arr[0] = course_state;
		course_city_arr[0] = course_city;
		return insert_into_meet(ID_arr, isXC_arr, name_arr, start_date_arr, length_arr, course_name_arr, course_state_arr, course_city_arr);
	}
	
	public boolean insert_into_organization(String [] name, String [] state, String [] division) {
		if (isConnected) {
			try {
				// creates insert and select update / query
				String insertString = "INSERT INTO organization(name, state, division) VALUES(?, ?, ?)";
				String selectString = "SELECT * FROM organization WHERE name = ?";
				PreparedStatement insertStatement = con.prepareStatement(insertString);
				PreparedStatement selectStatement = con.prepareStatement(selectString);
				// checks for row existence before attempting insert
				for (int i = 0; i < name.length; i++) {
					// checks for existence of given primary key
					selectStatement.setString(1, name[i]);
					// if given primary key row does not exist then continue to insert
					if (!selectStatement.executeQuery().next()) {
						insertStatement.setString(1, name[i]);
						insertStatement.setString(2, state[i]);
						insertStatement.setString(3, division[i]);
						insertStatement.executeUpdate();
					}
				}
			    return true;
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(RunningDatabase.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		} else {
			System.out.println("ERROR: Cannot execute insert without logging in to database first.");
		}
		return false;	// if something failed along the way
	}
	public boolean insert_into_organization(String name, String state, String division) {
		String [] name_arr = new String[1];
		String [] state_arr = new String[1];
		String [] division_arr = new String[1];
		name_arr[0] = name;
		state_arr[0] = state;
		division_arr[0] = division;
		return insert_into_organization(name_arr, state_arr, division_arr);
	}
	
	public boolean insert_into_organization_hosts(String [] organization_name, long [] meet_id) {
		if (isConnected) {
			try {
				// creates insert and select update / query
				String insertString = "INSERT INTO organization_hosts(organization_name, meet_id) VALUES(?, ?)";
				String selectString = "SELECT * FROM organization_hosts WHERE organization_name = ?" +
																		" AND meet_id = ?";
				PreparedStatement insertStatement = con.prepareStatement(insertString);
				PreparedStatement selectStatement = con.prepareStatement(selectString);
				// checks for row existence before attempting insert
				for (int i = 0; i < organization_name.length; i++) {
					// checks for existence of given primary key
					selectStatement.setString(1, organization_name[i]);
					selectStatement.setLong(2, meet_id[i]);
					// if given primary key row does not exist then continue to insert
					if (!selectStatement.executeQuery().next()) {
						insertStatement.setString(1, organization_name[i]);
						insertStatement.setLong(2, meet_id[i]);
						insertStatement.executeUpdate();
					}
				}
			    return true;
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(RunningDatabase.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		} else {
			System.out.println("ERROR: Cannot execute insert without logging in to database first.");
		}
		return false;	// if something failed along the way
	}
	public boolean insert_into_organization_hosts(String organization_name, long meet_id) {
		String [] organization_name_arr = new String[1];
		long [] meet_id_arr = new long[1];
		organization_name_arr[0] = organization_name;
		meet_id_arr[0] = meet_id;
		return insert_into_organization_hosts(organization_name_arr, meet_id_arr);
	}
	
	public boolean insert_into_performance(long [] meet_id, String [] event_name, int [] place,
			long [] athlete_id, Performance.CREDENTIALS [] athlete_credentials, double [] mark) {
		if (isConnected) {
			try {
				// creates insert and select update / query
				String insertString = "INSERT INTO performance(meet_id, event_name, place, athlete_id, athlete_credentials, mark)" +
												 " VALUES(?, ?, ?, ?, ?, ?)";
				String selectString = "SELECT * FROM performance WHERE meet_id = ?" +
																 " AND event_name = ?" +
																 " AND place = ?" +
																 " AND athlete_id = ?";
				PreparedStatement insertStatement = con.prepareStatement(insertString);
				PreparedStatement selectStatement = con.prepareStatement(selectString);
				// checks for row existence before attempting insert
				for (int i = 0; i < meet_id.length; i++) {
					// checks for existence of given primary key
					selectStatement.setLong(1, meet_id[i]);
					selectStatement.setString(2, event_name[i]);
					selectStatement.setInt(3, place[i]);
					selectStatement.setLong(4, athlete_id[i]);
					// if given primary key row does not exist then continue to insert
					if (!selectStatement.executeQuery().next()) {
						insertStatement.setLong(1, meet_id[i]);
						insertStatement.setString(2, event_name[i]);
						insertStatement.setInt(3, place[i]);
						insertStatement.setLong(4, athlete_id[i]);
						insertStatement.setString(5, athlete_credentials[i].toString());
						insertStatement.setDouble(6, mark[i]);
						insertStatement.executeUpdate();
					}
				}
			    return true;
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(RunningDatabase.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		} else {
			System.out.println("ERROR: Cannot execute insert without logging in to database first.");
		}
		return false;	// if something failed along the way
	}
	public boolean insert_into_performance(long meet_id, String event_name, int place,
			long athlete_id, Performance.CREDENTIALS athlete_credentials, double mark) {
		long [] meet_id_arr = new long[1];
		String [] event_name_arr = new String[1];
		int [] place_arr = new int[1];
		long [] athlete_id_arr = new long[1];
		Performance.CREDENTIALS [] athlete_credentials_arr = new Performance.CREDENTIALS[1];
		double [] mark_arr = new double[1];
		meet_id_arr[0] = meet_id;
		event_name_arr[0] = event_name;
		place_arr[0] = place;
		athlete_id_arr[0] = athlete_id;
		athlete_credentials_arr[0] = athlete_credentials;
		mark_arr[0] = mark;
		return insert_into_performance(meet_id_arr, event_name_arr, place_arr, athlete_id_arr, athlete_credentials_arr, mark_arr);
	}
}


