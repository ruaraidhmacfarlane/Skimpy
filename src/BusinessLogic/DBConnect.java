/**
 * @author Ruaraidh, FPS, Greg, Alina
 */
package BusinessLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBConnect extends HttpServlet{
	private Connection con;
	private Statement st;
	private ResultSet rs;
	
	public DBConnect()
	{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/Skimpy", "root", "");
			st = (Statement) con.createStatement();
		}
		catch(Exception ex)
		{
			System.out.println("Error:"+ex );
		}
	}
	
	public void openCon()
	{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/Skimpy", "root", "");
			st = (Statement) con.createStatement();
		}
		catch(Exception ex)
		{
			System.out.println("Error:"+ex );
		}
		
	}
	//open connection to a database of a name other than Skimpy, for testing
	public void openCon(String database)
	{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database , "root", "");
			st = (Statement) con.createStatement();
		}
		catch(Exception ex)
		{
			System.out.println("Error:"+ex );
		}
		
	}
	
	
	public void closeCon()

	{
		try 
		{
			st.close();
		} 
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
		try {
				con.close();
			} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}	
	}

	
	public Food pullFood(String t, String ID)
    
	{
		openCon();
		Food returnedFood = null;
		String table;
		try{
			
			if(
			t.equals("A")){
			 table = "asda";
			}else if(
			t.equals("T")){
			table = "tesco";
		    }else{
		    table="sains";
		    }
			
			String query = "select * FROM " + table + " WHERE ID=" + ID + ";";
//			System.out.println(query);
			rs = st.executeQuery(query);
			
			int DBID = -1;
			String shopID = null;
			String name = null;
			double mass = -1;
			String unit = null;
			double price = -1;
			double PPUPrice = -1;
			String PPUUnit = null;
			String foodCat = null;
			String foodCat2 = null;
			String supermarket = "X";
			double calories = -1;
			double proteins = -1;
			double carbs = -1;
			double sugars = -1;
			double fats = -1;
			double saturates = -1;
			double fibre = -1;
			double salt = -1;
			
			while(rs.next())
			{
				DBID = rs.getInt("ID");
				shopID = rs.getString("shopID");
				name = rs.getString("Name");
				unit = rs.getString("Unit");
				mass = rs.getDouble("Mass");
				price = rs.getDouble("Price");
				PPUPrice = rs.getDouble("PPUPrice");
				PPUUnit = rs.getString("PPUUnit");
				foodCat = rs.getString("FoodCat");
				foodCat2 = rs.getString("FoodCat2");
				supermarket = rs.getString("SuperMarket");
				calories = rs.getDouble("Calories");
				proteins = rs.getDouble("Proteins");
				carbs = rs.getDouble("Carbs");
				sugars = rs.getDouble("Sugars");
				fats = rs.getDouble("Fats");
				saturates = rs.getDouble("Saturates");
				salt = rs.getDouble("Salt");
				fibre = rs.getDouble("Fibre");
			}
			returnedFood = new Food(DBID, shopID, name, mass, unit, price, PPUPrice, PPUUnit,
					foodCat, foodCat2, supermarket, calories, proteins, carbs, sugars,
					fats, saturates, fibre, salt);	

			} 
			catch(Exception ex) 
			{
			System.out.println("Error:"+ex );	
			}	
			//close connections.
			finally 
			{
			closeCon();
			}
			
			return returnedFood;
	}

	//Push food object with nutrition data to DB
	public void pushFood(Food food, String tableName)
	{	
		openCon();
		if(food != null)
		{
			try{
				String query = "insert into " + tableName 
								+ "(shopID, Name, Unit, Mass, Price, PPUPrice, PPUUnit, FoodCat, FoodCat2, Supermarket,"
								+ " Calories, Proteins, Carbs, Sugars, Fats, Saturates, Salt, Fibre)"
								+ " values(\" " + food.getShopID() + "\", \"" + food.getName() + "\", \"" 
								+ food.getUnit() + "\", \"" + food.getMass()  + "\", \"" + food.getPrice() 
								+ "\", \"" + food.getPricePU() + "\", \"" + food.getPPUUnit() + "\", \"" 
								+ food.getFoodCat() + "\", \"" + food.getFoodCat2() + "\", \"" + food.getSupermarket() + "\", \"" 
								+ food.getCalories() + "\", \"" + food.getProteins() + "\", \"" 
								+ food.getCarbs() + "\", \"" + food.getSugars() + "\", \"" 
								+ food.getFats() + "\", \"" + food.getSaturates() + "\", \"" 
								+ food.getSalt() + "\", \"" + food.getFibre()+ "\");";

				st.executeUpdate(query);
							
			} catch(Exception ex){
					System.out.println(ex);
			}
			finally 
			{
				closeCon();
			}
		}
	}
		
	public Person pullUser(String ID)
	{
		openCon();
		try{
			Person user = null;
			boolean foundUser = false;
			rs = st.executeQuery("select * FROM user_info WHERE UserID= '" + ID + "';");
			while (rs.next()){
				foundUser = true;
				String userName = rs.getString("UserName");
				
				String userEmail = rs.getString("UserEmail");
				String password = rs.getString("UserPassword");
				
				Date dob = rs.getDate("DateOfBirth");
				double weight = rs.getDouble("Weight");
				double height = rs.getDouble("Height");
				char gender = rs.getString("Gender").charAt(0);
				int exercise = rs.getInt("Exercise");
				
				
				user = new Person(userName, userEmail, password, dob, height, weight, gender, exercise);
				user.setID(rs.getInt("UserID"));
			}
			if(foundUser){
				return user;
			}
			return null;
	
		} catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		finally 
		{
			closeCon();
		}
	}
			
	public int getIDfromEmail(String email){
		openCon();
		try{
			rs = st.executeQuery("select * FROM user_info WHERE UserEmail = '" + email + "'");
			while (rs.next()){
				return rs.getInt("UserID");
			}

			return -1;
	
		} catch(Exception ex){
			System.out.println(ex);
			return -1;
		}
		finally 
		{
			closeCon();
		}
	}
	
	public void updateUser(Person user){
		openCon();
		try{				
			String query = "UPDATE user_info " +
					"SET UserName = '" + user.getName() + "' " +
					", UserEmail = '" + user.getEmail() + "' " +
					", UserPassword = '" + user.getPassword() + "' " +
					", DateOfBirth = '" + new java.sql.Date(user.getDob().getTime()) + "' " +
					", Age = '" + user.getAge() + "' " +
					", Height = '" + user.getHeight() + "' " +
					", Weight = '" + user.getWeight() + "' " +
					", Gender = '" + user.getGender() + "' " +
					", Exercise = '" + user.getExercise() + "' " +
					"WHERE UserID = '" + user.getID() + "';";
			st.executeUpdate(query);
			
		} catch(Exception ex){
			System.out.println(ex);
		}
		finally 
		{
			closeCon();
		}
	}
	
	public void pushUser(Person user){
		openCon();
		try{
			String query ="INSERT INTO user_info (UserName, UserEmail, UserPassword, DateOfBirth, Age, Height, Weight, Gender, Exercise)"
					+ "VALUES ('" + 
							user.getName() +  "', '" + user.getEmail() + "', '" + user.getPassword() + "', '"  +
							new java.sql.Date(user.getDob().getTime()) + "', '" + user.getAge() + "', '" + user.getHeight() + 
							"', '" + user.getWeight() + "', '" + user.getGender() + "', '" + user.getExercise() +"')";
			st.executeUpdate(query);

		} catch(Exception ex){
			System.out.println(ex);
		}
		finally 
		{
			closeCon();
		}
	}
	
	public boolean validateEmail(String email) {
		try {
			rs = st.executeQuery(String.format("SELECT * FROM user_info WHERE UserEmail = '%s'", email));

			if (rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	
	//I think this is unused
	public void findCat(String qu){
		try{
			 String query ="SELECT * FROM sains_scraped WHERE name LIKE '%" + qu + " %';";
		 
		     ResultSet rs = st.executeQuery(query);
		     String temp = "";
		     String name = "";
		     boolean found = false;
		     while (rs.next()) {
		    	 found = true;
		    	 temp = name;
		    	 name = rs.getString("name");
		    	 if(!temp.equals(name))
		    	 {
		    		 System.out.println(name+"  ");
		    	 }
		     }
		     if(!found){
		    	 System.out.println("No results for query: " + qu);
		     }
		     System.out.println();
			 
		} catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	

	public void pushPortionSizes(String table, String foodCat, String item, double mass, String unit)
	{
		openCon();
		try{
			String query = "INSERT INTO "+ table +" (FoodCat, Item, Mass, Unit) VALUES (\"" + 
							foodCat +  "\", \"" + item + "\", \""  + mass + "\", \"" + unit  + "\");";
			
			System.out.println(query);
			st.executeUpdate(query);
			System.out.println("Pushed Portion Sizes");
			
		} catch(Exception ex){
			System.out.println(ex);
		}
		finally 
		{
			closeCon();
		}
	}

	public void pullPortionSizes(String itemSearch)
	{
		openCon();
		try{
			System.out.println("Records from Database:");
			rs = st.executeQuery("select * FROM user_info WHERE item=" + itemSearch);
			while (rs.next()){
				String foodCat = rs.getString("FoodCat");
				String item = rs.getString("Item");
				int mass = rs.getInt("Mass");
				String unit = rs.getString("Unit");
				
				System.out.println("Food Cat: "  + foodCat + 
									"\nItem: " + item + 
									"\nMass: " + mass + 
									"\nUnit: " + unit);
			}
	
		} catch(Exception ex){
			System.out.println(ex);
		}
		finally 
		{
			closeCon();
		}
	}

	public ArrayList<Food> search(String table, String qu)
	{
		openCon();
		try{
			ArrayList<String> query = new ArrayList<String>();
			//exact match
			for(int i = 0; i < 1; i++){
				query.add("SELECT * FROM " + table + " WHERE Name = '" + qu + "';");;
			}
			for(int i = 0; i < 1; i++){
				query.add("SELECT * FROM " + table + " WHERE FoodCat2 = '" + qu + "';");
			}
			for(int i = 0; i < 1; i++){
				query.add("SELECT * FROM " + table + " WHERE FoodCat = '" + qu + "';");
			}
			if(qu.contains(" ")){
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE Name LIKE '% " + qu + " %';");
				}
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE FoodCat LIKE '% " + qu + " %';");
				}
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE FoodCat2 LIKE '% " + qu + " %';");
				}
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE Name LIKE '%" + qu + "%';");
				}
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE FoodCat LIKE '%" + qu + "%';");
				}
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE FoodCat2 LIKE '%" + qu + "%';");
				}
			}
			else{
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE Name LIKE '% " + qu + " %';");
				}
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE FoodCat LIKE '% " + qu + " %';");
				}
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE FoodCat2 LIKE '% " + qu + " %';");
				}
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE Name LIKE '%" + qu + "%';");
				}
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE FoodCat LIKE '%" + qu + "%';");
				}
				for(int i = 0; i < 1; i++){
					query.add("SELECT * FROM " + table + " WHERE FoodCat2 LIKE '%" + qu + "%';");
				}
			}
			
			
			
			
			Map<String, Integer> resultsHash = new HashMap<String, Integer>();
			ArrayList<String> results = new ArrayList<String>();
			String temp = "";
		    String nameID = "";
			for(String q: query){
				ResultSet rs = st.executeQuery(q);
			    while (rs.next()) {

			    	temp = nameID;
			    	nameID = rs.getString("ShopID");
			    	//stops duplicates
			    	if(!temp.equals(nameID)){
			    		results.add(nameID);
			    	}
			    }
			}
			
			
			Set<String> mySet = new HashSet<String>(results);
			for(String s: mySet){
				resultsHash.put(s, Collections.frequency(results, s));
			}
			
			List<Entry<String, Integer>> sortedRes = new ArrayList<Entry<String, Integer>>();
			sortedRes = entriesSortedByValues(resultsHash);

			ArrayList<Food> foodArr = new ArrayList<>();
			for (Map.Entry<String,Integer> entry : sortedRes) {
			    foodArr.add(pullFood(table, entry.getKey()));
			    
			}
			return foodArr;

		} catch(Exception ex){
			System.out.println(ex);
			return null;
		}
		finally 
		{
			closeCon();
		}
	}
	static <K,V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {
		
		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
	
		Collections.sort(sortedEntries, new Comparator<Entry<K,V>>() {@Override public int compare(Entry<K,V> e1, Entry<K,V> e2) 
			{
				return e2.getValue().compareTo(e1.getValue());
			}
	    });
	
		return sortedEntries;
	}
	
	public void getFoodCategories(){
		openCon();
		try{
			String supermarket = "portion_sizes";
			String query = "select * FROM " + supermarket + 
						" ORDER BY " + supermarket + ".`FoodCat` ASC;";
			rs = st.executeQuery(query);
			
			ArrayList<String> categories = new ArrayList<>();
			String temp = "";
			String cat = "";
			while(rs.next())
			{	
				cat = rs.getString("FoodCat");
//				System.out.println(cat);
//				System.out.println(temp);
//				
				if(!temp.equals(cat)){
					categories.add(cat);
				}	
				temp = cat;
			}
			temp = "";
			rs = st.executeQuery("SELECT * FROM " + supermarket + 
								" ORDER BY " + supermarket + ".`Item` ASC");
			for(String s: categories){
				System.out.println("> " + s);
				rs = st.executeQuery("SELECT * FROM " + supermarket + " WHERE FoodCat = '" + s + "'"
						+ " ORDER BY " + supermarket + ".`Item` ASC;");
				
				while(rs.next()){
					cat = rs.getString("Item");
					if(!temp.equals(cat)){
						System.out.println("\t * " + cat);
					}
					temp = cat;
				}
			}
			
		} 
		catch(Exception ex) 
		{
			System.out.println("Error:"+ex );	
		}	
		//close connections.
		finally 
		{
			closeCon();
		}	
	}

	public void recommend(String val, String coloumn)
	{
		openCon();
		try{
			 String query = "SELECT * FROM fooditems WHERE " + coloumn + " LIKE '" + val + "';";
		 
		     ResultSet rs = st.executeQuery(query);
		     String temp = "";
		     String name = "";
		     boolean found = false;
		     while (rs.next()) {
		    	 found = true;
		    	 temp = name;
		    	 name = rs.getString("name");
		    	 if(!temp.equals(name)){
		    		 System.out.println(name+"  ");
		    	 }
		     }
		     if(!found){
		    	 System.out.println("No results for query: " + val);
		     }
		     System.out.println();
			 
		} catch(Exception ex){
			System.out.println(ex);
		}
		finally 
		{
			closeCon();
		}
	}
	
	public List<Food> productSearch(String phrase) {
		List<Food> results = new ArrayList<Food>();
		
		String[] words = phrase.split("\\s");
		String regexpPhrase = "";
		for (int i = 0; i < words.length - 1; i++) {
			regexpPhrase += words[i] + ".*";
		}
		regexpPhrase += words[words.length - 1];
		
		String tQuery = String.format(
			"SELECT DISTINCT * FROM tesco WHERE Name REGEXP ' %s | %s$' AND FoodCat2 REGEXP '%s' AND PPUUnit NOT LIKE 'NULL'",
			regexpPhrase, regexpPhrase, regexpPhrase);
		String sQuery = String.format(
			"SELECT DISTINCT * FROM sains WHERE Name REGEXP ' %s | %s$' AND FoodCat2 REGEXP '%s'",
			regexpPhrase, regexpPhrase, regexpPhrase);
		String query = tQuery + " UNION " + sQuery + " ORDER BY Price ASC";
		
		String moreGeneralQuery_1 = String.format(
			"SELECT DISTINCT * FROM tesco WHERE Name REGEXP ' %s | %s$' AND PPUUnit NOT LIKE 'NULL'",
			regexpPhrase, regexpPhrase);
		String moreGeneralQuery_2 = String.format(
			"SELECT DISTINCT * FROM sains WHERE Name REGEXP ' %s | %s$'",
			regexpPhrase, regexpPhrase);
		String moreGeneralQuery = moreGeneralQuery_1 + " UNION " + moreGeneralQuery_2
				+ " ORDER BY Price ASC";
		
		try {
			openCon();
			rs = st.executeQuery(query);
			if (rs.next()) {
				do {
					Food temp = new Food(
							rs.getInt("ID"),
							rs.getString("shopID"),
							rs.getString("Name"),
							rs.getDouble("Mass"),
							rs.getString("Unit"),
							rs.getDouble("Price"),
							rs.getDouble("PPUPrice"),
							rs.getString("PPUUnit"),
							rs.getString("FoodCat"),
							rs.getString("FoodCat2"),
							rs.getString("SuperMarket"),
							rs.getDouble("Calories"),
							rs.getDouble("Proteins"),
							rs.getDouble("Carbs"),
							rs.getDouble("Sugars"),
							rs.getDouble("Fats"),
							rs.getDouble("Saturates"),
							rs.getDouble("Fibre"),
							rs.getDouble("Salt"));
					
					results.add(temp);
				} while (rs.next());
			}
			else {
				rs = st.executeQuery(moreGeneralQuery);
				while (rs.next()) {
					Food temp = new Food(
							rs.getInt("ID"),
							rs.getString("shopID"),
							rs.getString("Name"),
							rs.getDouble("Mass"),
							rs.getString("Unit"),
							rs.getDouble("Price"),
							rs.getDouble("PPUPrice"),
							rs.getString("PPUUnit"),
							rs.getString("FoodCat"),
							rs.getString("FoodCat2"),
							rs.getString("SuperMarket"),
							rs.getDouble("Calories"),
							rs.getDouble("Proteins"),
							rs.getDouble("Carbs"),
							rs.getDouble("Sugars"),
							rs.getDouble("Fats"),
							rs.getDouble("Saturates"),
							rs.getDouble("Fibre"),
							rs.getDouble("Salt"));
					
					results.add(temp);
				}
			}
		}
		catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		finally {
			closeCon();
		}
		return results;
	}
	
	public String jsonSearch(String phrase, String[] categories) {
		JSONArray results = new JSONArray();
		String query;
		
		String[] words = phrase.split("\\s");
		String regexpPhrase = "";
		for (int i = 0; i < words.length - 1; i++) {
			regexpPhrase += words[i] + ".*";
		}
		regexpPhrase += words[words.length - 1];
		
		if (categories != null) {
			String aQuery = "SELECT DISTINCT * FROM asda WHERE ";
			String tQuery = "SELECT DISTINCT * FROM tesco WHERE ";
			String sQuery = "SELECT DISTINCT * FROM sains WHERE ";
			
			for (int i = 0; i < categories.length - 1; i++) {
				aQuery += String.format("Price NOT LIKE '0' AND Name REGEXP '%s' AND FoodCat2 LIKE '%s' OR ",
						regexpPhrase, categories[i]);
				tQuery += String.format("Price NOT LIKE '0' AND Name REGEXP '%s' AND FoodCat2 LIKE '%s' OR ",
						regexpPhrase, categories[i]);
				sQuery += String.format("Price NOT LIKE '0' AND Name REGEXP '%s' AND FoodCat2 LIKE '%s' OR ",
						regexpPhrase, categories[i]);
			}
			
			aQuery += String.format("Price NOT LIKE '0' AND Name REGEXP '%s' AND FoodCat2 LIKE '%s'",
					regexpPhrase, categories[categories.length - 1]);
			tQuery += String.format("Price NOT LIKE '0' AND Name REGEXP '%s' AND FoodCat2 LIKE '%s'",
					regexpPhrase, categories[categories.length - 1]);
			sQuery += String.format("Price NOT LIKE '0' AND Name REGEXP '%s' AND FoodCat2 LIKE '%s' ",
					regexpPhrase, categories[categories.length - 1]);
			
			query = aQuery + " UNION " + tQuery + " UNION " + sQuery + " ORDER BY Price ASC";
		}
		else {
			query = String.format("SELECT DISTINCT * FROM asda WHERE Name REGEXP '%s' AND Price NOT LIKE '0' "
					+ "UNION "
					+ "SELECT DISTINCT * FROM tesco WHERE Name REGEXP '%s' AND Price NOT LIKE '0' "
					+ "UNION "
					+ "SELECT DISTINCT * FROM sains WHERE Name REGEXP '%s' AND Price NOT LIKE '0' ORDER BY Price ASC LIMIT 50",
					regexpPhrase, regexpPhrase, regexpPhrase);
		}
		
		try {
			openCon();
			rs = st.executeQuery(query);
			while (rs.next()) {
				JSONObject temp = new JSONObject();
				temp.put("ID", rs.getInt("ID"));
				temp.put("name", rs.getString("Name").trim());
				temp.put("price", rs.getDouble("Price"));
				temp.put("shopID", rs.getString("ShopID").trim());
				temp.put("supermarket", rs.getString("SuperMarket"));
				temp.put("shelf", rs.getString("FoodCat2").trim());
				temp.put("mass", rs.getString("Mass").trim());
				temp.put("unit", rs.getString("Unit").trim());
				
				results.put(temp);
			}
		}
		catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		finally {
			closeCon();
		}
		
		return results.toString();
	}
	
	public String categorySearch(String phrase) {
		JSONArray results = new JSONArray();
		String timestamp;
		
		String[] words = phrase.split("\\s");
		String regexpPhrase = "";
		for (int i = 0; i < words.length - 1; i++) {
			regexpPhrase += words[i] + ".*";
		}
		regexpPhrase += words[words.length - 1];
		
		String asdaCatQuery = String.format(
				"(SELECT FoodCat2, COUNT(Name) AS entries FROM asda WHERE Name REGEXP '%s' AND Price NOT LIKE '0' GROUP BY FoodCat2)",
				regexpPhrase);
		
		String tescoCatQuery = String.format(
				"(SELECT FoodCat2, COUNT(Name) AS entries FROM tesco WHERE Name REGEXP '%s' AND Price NOT LIKE '0' GROUP BY FoodCat2)",
				regexpPhrase);
		
		String sainsCatQuery = String.format(
				"(SELECT FoodCat2, COUNT(Name) AS entries FROM sains WHERE Name REGEXP '%s' AND Price NOT LIKE '0' GROUP BY FoodCat2)",
				regexpPhrase);
		
		timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
		
		String queryCreateView = 
				"CREATE VIEW temp_" + timestamp + " AS "
				+ asdaCatQuery + " UNION " + tescoCatQuery + " UNION " + sainsCatQuery + " ORDER BY entries DESC";
		
		String query = String.format(
				"SELECT FoodCat2, SUM(entries) AS entries_total FROM %s GROUP BY FoodCat2 ORDER BY entries_total DESC LIMIT 25",
				"temp_" + timestamp);
		
		try {
			openCon();
			PreparedStatement tempView = con.prepareStatement(queryCreateView);
			tempView.execute();
			
			rs = st.executeQuery(query);
			while (rs.next()) {
				results.put(rs.getString("FoodCat2").trim());
			}
			
			PreparedStatement dropTable = con.prepareStatement(
					String.format("DROP VIEW IF EXISTS %s", "temp_" + timestamp));
			dropTable.execute();
		}
		catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		finally {
			closeCon();
		}
		
		return results.toString();
	}
	
	public String findOffers(String tableID, String shopID) {
		JSONArray results = new JSONArray();
		String supermarket;
		
		String queryFormat = "SELECT * FROM %s "
				+ "WHERE FoodCat2 IN (SELECT FoodCat2 FROM %s WHERE id=%s) "
				+ "AND NOT id=%s AND NOT price = 0 ORDER BY Price LIMIT 3";
		if (shopID.equals("A")) {
			supermarket = "asda";
		}
		else if (shopID.equals("S")) {
			supermarket = "sains";
		}
		else if (shopID.equals("T")) {
			supermarket = "tesco";
		}
		else {
			return "";
		}
		
		String query = String.format(queryFormat, supermarket, supermarket, tableID, tableID);
		
		try {
			rs = st.executeQuery(query);
			while (rs.next()) {
				JSONObject temp = new JSONObject();
				if (rs.getString("Supermarket").equals("A") || rs.getString("SuperMarket").equals("S")) {
					temp.put("url", rs.getString("ShopID").trim());
				}
				else if (rs.getString("SuperMarket").equals("T")) {
					temp.put("url", "http://www.tesco.com/groceries/product/details/?id=" + rs.getString("ShopID").trim());
				}
				
				temp.put("name", rs.getString("Name"));
				temp.put("price", rs.getDouble("Price"));
				results.put(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results.toString();
	}
}
