import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

// TODO: Fixes!
/*
 * Fix last week so that it is super-updated
 * 
 * Fix overwriting dates after new year
 * 
 * Don't forget to enter correct connection info in opendb()!
 * 
 * */



public class Main {
	public static final boolean DEBUG = true;
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DB_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
	public static Connection conn = null;
	
	public static List<String> products1;
	public static List<String> products2;
	public static List<String> bloglist;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 *  The program needs to run daily. We might need to adjust the location radius depending on popularity. 
		 *  For impopular words we might even have to expand search (e.g. more blogs)
		 *  
		 */
		
		//Redirect out-stream to a file
		Calendar logd = Calendar.getInstance();	
		int logdayofyear = logd.get(Calendar.DAY_OF_YEAR);
		int loghourofday = logd.get(Calendar.HOUR_OF_DAY);
		String logdate = new String(logdayofyear + "_" + loghourofday);
		//System.out.println(logdate);
		
		File file  = new File("logs/ef_sysout_" + logdate + ".log");
	    PrintStream printStream;
		try {
			printStream = new PrintStream(new FileOutputStream(file));
			if(!DEBUG)
				System.setOut(printStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    
		products1 = new LinkedList<String>();
		products2 = new LinkedList<String>();
		bloglist = new LinkedList<String>();
		
		System.out.println("Start...");
		System.out.println("OpenDB...");
		opendb();
		loadProducts();
		System.out.println("Product List Size: " + products1.size());
		
		//saveBlogs();
		loadBlogs(); 
		System.out.println("Blog List Size: " + bloglist.size());
		
		//Input a handful of inköp
		//createInkop();
		
		System.out.println("Twitter...");
		//Twitter
		parseTwitter();
					
		System.out.println("Blogs...");
		//Blogs
		parseBlogs();
		
		System.out.println("Recept...");
		//Reciepes
		parseRecipes();
		
		System.out.println("Calculus...");
		//Make the final table for all twitter and blog values. Currently pure frequencies
		calculateSocialValues();
		
		System.out.println("CloseDB...");
		
		closedb();
		
		
		

		System.out.println("End...");
		
	}
	
	static private void opendb(){
		try {			
		    conn = DriverManager.getConnection("jdbc:mysql://the.mysql.server","username","pass");
		    
		    System.out.println("OK");
		    // Do something with the Connection		  
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}			
	}
	
	static private void closedb(){
		try {
			conn.close();
			System.out.println("OK");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static private void loadProducts(){
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
		    stmt = conn.createStatement();
		    rs = stmt.executeQuery("SELECT * FROM fruktlista");
		    
		    int i = 0;
		    while (rs.next())
		    {
		    	i++;		    		
		    	//System.out.println(Integer.toString(i) + ". " + rs.getString("fruktNamn1"));		    	
		    	products1.add(rs.getString("fruktNamn1"));
		    	products2.add(rs.getString("fruktNamn2"));
		    }
		    System.out.println("Loaded " + Integer.toString(i) + " products");
		    

		    // Now do something with the ResultSet ....
		}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		
	}
	
	static private void loadBlogs(){
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
		    stmt = conn.createStatement();
		    rs = stmt.executeQuery("SELECT * FROM bloglista");
		    
		    int i = 0;
		    while (rs.next())
		    {
		    	i++;		    		
		    	//System.out.println(Integer.toString(i) + ". " + rs.getString("fruktNamn1"));		    	
		    	bloglist.add(rs.getString("blogAddress"));
		    	//products2.add(rs.getString("fruktNamn2"));
		    }
		    System.out.println("Loaded " + Integer.toString(i) + " blogs");
		    

		    // Now do something with the ResultSet ....
		}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
		
	@SuppressWarnings(value={"unused"})
	static private void saveBlogs(){
		Statement stmt = null;
		//ResultSet rs = null;
		
		try {
		    stmt = conn.createStatement();
		    
		    BufferedReader in2;
		    String str2;
		    int i = 0; //Good thinking! Reset the value for each product!
			try {
				in2 = new BufferedReader(new FileReader("res/expertblogs.txt"));
								
			    while ((str2 = in2.readLine()) != null) {
			    	str2 = str2.replaceAll("\r", "");
			    	str2 = str2.replaceAll("\n", "");
			    	i++;
			    	stmt.executeUpdate("INSERT INTO bloglista VALUES (" + Integer.toString(i) + ",'" + str2 + "')");			    	
			    }
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    
		    
		    System.out.println("Saved " + Integer.toString(i) + " blogs");	    

		    // Now do something with the ResultSet ....
		}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	@SuppressWarnings(value={"unused"})
	static private void createInkop(){
		System.out.println("Create Inköp");
		
		Statement stmt = null;
		Random rand = new Random();
    	rand.setSeed(1);
    	int produkt = 0;
    	SimpleDateFormat sdf = new SimpleDateFormat(DB_DATE_FORMAT);
    	Calendar ed = Calendar.getInstance();
    	ed.add(Calendar.HOUR, 24*7*16);
		
    	for(int week = 1; week <= 28; week++){
    		for(int anv = 1; anv <= 18; anv++){			
				for(int ink = 0; ink < 10; ink++){
					//Create a random inköp
					produkt = 1 + (Math.abs(rand.nextInt()) % products1.size());
					
					try{
				    	stmt = conn.createStatement();
				    	//WARNING!! The following line will affect the database in the carefully defined random manner!
					    //stmt.executeUpdate("INSERT INTO inkop (userId, fruktId, datum) VALUES (" + anv + "," + produkt + ",'" + sdf.format(ed.getTime()).toString() + "')");				    
					}
					catch (SQLException ex){
					    // handle any errors
					    System.out.println("SQLException: " + ex.getMessage());
					    System.out.println("SQLState: " + ex.getSQLState());
					    System.out.println("VendorError: " + ex.getErrorCode());
					}
					
				}		
				//System.out.println(produkt + " > " + sdf.format(ed.getTime()).toString() + " > " + anv + " > " + week);
				
			}
			
			System.out.println(produkt + " > " + sdf.format(ed.getTime()).toString());
			ed.add(Calendar.HOUR, -24*7);
		}		
    	
    	/*try{
	    	stmt = conn.createStatement();
		    stmt.executeUpdate("INSERT INTO inkop (userId, fruktId, datum) VALUES (" + 18 + "," + 3 + ",'" + sdf.format(ed.getTime()).toString() + "')");				    
		}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}*/
		
	}
	
	/*
	 * 
	 * So the idea with twitter for now will be to fetch data for two weeks back
	 * Check with the last stored ID from the last time. 
	 * If the read data equals ID from last time - stop otherwise insert.
	 * 
	 */
	static private void parseTwitter(){
		try{
			//BufferedReader in = new BufferedReader(new FileReader("res/products.txt"));
		    String str, str2;
		    int[] dist = {500,500,3,1000,300,5,1000,300,
		    			  1000,500,1000,500,1000,300,100,300,
		    			  100,3000,3000,300,3000,100,3000,1000,
		    			  1000,3000,3000,3000,500,3000,500,1000};
		    Statement stmt = null;
			//ResultSet rs = null;
		    //while ((str = in.readLine()) != null) {
			for(int a = 0; a < products1.size(); a++){
				str = strtourlfix(products1.get(a));
				str2 = strtourlfix(products2.get(a));		    	
		    	
		    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				Calendar sd = Calendar.getInstance();
				Calendar ed = Calendar.getInstance();
				sd.add(Calendar.HOUR, -24);
				
				int dayofyear = ed.get(Calendar.DAY_OF_YEAR);
				int weekofyear = ed.get(Calendar.WEEK_OF_YEAR);
				int values[] = new int[10];
		    	String content = new String();
				
		    	for(int i = 0; i < 1; i++){		    		
		    		
		    		//String myUrl = "http://search.twitter.com/search.atom?q=gurka&rpp=100&page=1&since=2011-09-09&until=2011-09-10";
					String myUrl = "http://search.twitter.com/search.atom?q=" + str + "%20OR%20" + str2 + "&rpp=100&page=1&geocode=59.334157,18.097020,"+Integer.toString(dist[a])+"mi&since="+sdf.format(sd.getTime())+"&until="+sdf.format(ed.getTime());
					
					//URL url = new URL("http://search.twitter.com/search.atom?q=tomat%20geocode%3A59.334157%2C18.097020%2C10mi");
					URL url = new URL(myUrl);
			
					/* Get a SAXParser from the SAXPArserFactory. */
					//SAXParserFactory sp = SAXParserFactory.newInstance();
					SAXParserImpl spf = org.ccil.cowan.tagsoup.jaxp.SAXParserImpl.newInstance(null);
			
					/* Get the XMLReader of the SAXParser we created. */
					//XMLReader xr = sp.;
					XMLReader xr = spf.getXMLReader();
					/* Create a new ContentHandler and apply it to the XML-Reader*/ 
					ItemsHandler myExampleHandler = new ItemsHandler();
					xr.setContentHandler(myExampleHandler);
					
					/* Parse the xml-data from our URL. */
					HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
					httpConnection.connect(); 
					//xr.parse(new InputSource(url.openStream()));
					xr.parse(new InputSource(httpConnection.getInputStream()));
					
					//String b = new String();
					for(int j = 0; j < myExampleHandler.getCounterValue(); j++){
						//b = myExampleHandler.getParsedData(i).getIdString().toString();
						//System.out.println(b);
						if(myExampleHandler.getParsedData(0).getContentString().toString() != null)
						content = myExampleHandler.getParsedData(0).getContentString().toString();
						//System.out.println(b);
					}
					
					values[i] = myExampleHandler.getCounterValue();
					
					httpConnection.disconnect();
					
					
					
					//Subtract a day
					sd.add(Calendar.HOUR, -24);
					ed.add(Calendar.HOUR, -24);
		    	}
		    	//System.out.println(str + ", " + Integer.toString(myExampleHandler.getCounterValue()));
		    	//System.out.println(Integer.toString(a+1) + ". " + urltostrfix(str) + ", " + values[0] + ", " + values[1] + ", " + values[2] + ", " + values[3]
		    	//                       + ", " + values[4] + ", " + values[5] + ", " + values[6]); 
		    	
		    	//cleanup
		    	content = content.replace("<em>", "");
		    	content = content.replace("</em>", "");
		    	content = content.replace("null", "");
		    	content = content.replace("@", "");	
		    	content = content.replace("  ", " ");		
		    	content = content.replaceAll("<a ([^<]*)</a>", "");
		    	content = content.trim();	
		    	content = content.replace("'", "''");		    	
		    	
		    	System.out.println(Integer.toString(a+1) + ". " + urltostrfix(str) + ", " + values[0] + ", " + content + ", " + Integer.toString(dayofyear) );
		    	
		    	try {
		    		
		    		//stmt = conn.createStatement();
				    //rs = stmt.executeQuery("SELECT * FROM fruktlista");				    
				    
				    stmt = conn.createStatement();
				    stmt.executeUpdate("INSERT INTO twitterdata VALUES (" + Integer.toString(dayofyear) + "," + Integer.toString(weekofyear) + "," + Integer.toString(a+1) + ",'" + content + "'," + Integer.toString(values[0]) +  ")");				    

				    // Now do something with the ResultSet ....
				}
				catch (SQLException ex){
				    // handle any errors
				    System.out.println("SQLException: " + ex.getMessage());
				    System.out.println("SQLState: " + ex.getSQLState());
				    System.out.println("VendorError: " + ex.getErrorCode());
				}
		    	
		    	
		    }
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		return;
	}
	
	//@SuppressWarnings(value={"unused"})
	static private void parseRecipes(){
		try{
			String str;
			Statement stmt3 = null;
			ResultSet rs = null;		
			int max_twitter_value;
			int max_blog_value;
			Random rand = new Random();
	    	rand.setSeed(1);
			for(int a = 0; a < products1.size(); a++){
				str = strtourlfix(products1.get(a));
				//str2 = strtourlfix(products2.get(a));	
		    	//This is just the four seasonal highlights
		    	int values[] = new int[4];
		    	int x_g = 0;
		    	int slump = 0;
		    	double recipe_result[];
		    	double random_result[];
		    	for(int i = 0; i < 4; i++){
		    		String myUrl = "";
		    		if(i == 0)
		    			myUrl = "http://m.recept.nu/?rcp_recipeCategory=&rcp_kitchenCategory=&rcp_mainIngredientCategory=&rcp_themeCategory=P%C3%A5sk&searchString="+str+"&expand=true&button=s%C3%B6k&search=true";
		    		else if(i == 1)
		    			myUrl = "http://m.recept.nu/?rcp_recipeCategory=&rcp_kitchenCategory=&rcp_mainIngredientCategory=&rcp_themeCategory=Midsommar&searchString="+str+"&expand=true&button=s%C3%B6k&search=true";
		    		else if(i == 2)
		    			myUrl = "http://m.recept.nu/?rcp_recipeCategory=&rcp_kitchenCategory=&rcp_mainIngredientCategory=&rcp_themeCategory=Halloween&searchString="+str+"&expand=true&button=s%C3%B6k&search=true";
		    		else if(i == 3)
		    			myUrl = "http://m.recept.nu/?rcp_recipeCategory=&rcp_kitchenCategory=&rcp_mainIngredientCategory=&rcp_themeCategory=Jul&searchString="+str+"&expand=true&button=s%C3%B6k&search=true";
		    		
					//URL url = new URL("http://m.recept.nu/?rcp_recipeCategory=&rcp_kitchenCategory=&rcp_mainIngredientCategory=&rcp_themeCategory=P%C3%A5sk&searchString=tomat&expand=true&button=s%C3%B6k&search=true");
					URL url = new URL(myUrl);
					/* Get a SAXParser from the SAXPArserFactory. */
					//SAXParserFactory sp = SAXParserFactory.newInstance();
					SAXParserImpl spf = org.ccil.cowan.tagsoup.jaxp.SAXParserImpl.newInstance(null);
			
					/* Get the XMLReader of the SAXParser we created. */
					//XMLReader xr = sp.;
					XMLReader xr = spf.getXMLReader();
					/* Create a new ContentHandler and apply it to the XML-Reader*/ 
					RecipesHandler myRecipesHandler = new RecipesHandler();
					xr.setContentHandler(myRecipesHandler);
					
					/* Parse the xml-data from our URL. */
					HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
					httpConnection.connect(); 
					//xr.parse(new InputSource(url.openStream()));
					xr.parse(new InputSource(httpConnection.getInputStream()));
					
					//String c = new String();
					//for(int i = 0; i < myRecipesHandler.getCounterValue(); i++){
						//b = myExampleHandler.getParsedData(i).getIdString().toString();
						//System.out.println(b);
						
						//c = myExampleHandler.getParsedData(i).getContentString().toString();
						//System.out.println(c);
					//}
					//System.out.println(str + ", " + Integer.toString(myRecipesHandler.getHitsValue()));
					values[i] = myRecipesHandler.getHitsValue();
					httpConnection.disconnect();
		    	}
		    	x_g = (int)Math.round((double)(13 * values[0] + 26 * values[1] + 39 * values[2] + 52 * values[3]) / (double)(values[0] +values[1] +values[2] +values[3]));
		    	
		    	//get a max-value for that product to multiply the sine-curve with.
		    	stmt3 = conn.createStatement();
			    
			    rs = stmt3.executeQuery("SELECT * FROM socialvalues WHERE fruktId=" + a + " AND twittervalue = (SELECT max( twittervalue ) FROM socialvalues WHERE fruktId=" + a +")"); 
			    max_twitter_value = 0;
			    while(rs.next())			    	
			    	max_twitter_value = rs.getInt("twittervalue");
			    
			    if(max_twitter_value > 0)
			    	max_twitter_value--;
			    
			    rs = stmt3.executeQuery("SELECT * FROM socialvalues WHERE fruktId=" + a + " AND blogvalue = (SELECT max( blogvalue ) FROM socialvalues WHERE fruktId=" + a +")"); 
			    max_blog_value = 0;
			    while(rs.next())			    	
			    	max_blog_value = rs.getInt("blogvalue");
			    
			    if(max_blog_value > 0)
			    	max_blog_value--;
			    //System.out.println(max_blog_value);
		    	//final resulting array
		    	recipe_result = sinevalues(x_g, 53);
		    	
		    	//The random result
		    	
		    	slump = 1 + (Math.abs(rand.nextInt()) % 53);
		    	
		    	random_result = sinevalues(slump, 53);
		    	
		    	System.out.println(urltostrfix(str) + ": " + values[0] + ", " + values[1] + ", " + values[2] + ", " + values[3] + ": " + x_g + ": " + slump);
				
		    	
		    	for(int week = 1; week <= 53 ;week++){
		    		//System.out.println((int)(max_twitter_value * recipe_result[week]));
					//Updating table
					stmt3 = conn.createStatement();
					stmt3.executeUpdate("UPDATE socialvalues SET twittervalue=" + (int)(max_twitter_value * recipe_result[week]) + " WHERE weekofyear=" + week + " AND fruktId=" + a);				   
					stmt3.executeUpdate("UPDATE socialvalues SET blogvalue=" + (int)(max_blog_value * random_result[week]) + " WHERE weekofyear=" + week + " AND fruktId=" + a);				   					
		    	}
		    	
		    }
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		return;
	}
	
	//http://www.google.com/search?q=tomat%20tomater%20daterange:2455841-2455842&hl=en&client=firefox-a&tbm=blg&output=atom
	//It is possible to do a google search - even back in time - but there might be a robot detection algorithm in the way
	static private void parseBlogs(){
		String timespan = "24h"; //m=month, w=week, 24h=day, 12h, 1h, etc
		boolean svlang = false;
		
		try{
			//BufferedReader in = new BufferedReader(new FileReader("res/products.txt"));
			Statement stmt = null;
		    String str1, str2, str3;
		    str3 = "";
		    for(int j = 0; j < bloglist.size(); j++){
		    	if(j > 0)
		    		str3 += "|";
		    	str3 = str3 + bloglist.get(j);
		    	str3 = str3.replaceAll(":", "%3A");
		    	str3 = str3.replaceAll("/", "%2F");
		    	str3 = str3.replaceAll("\r", "");
		    	str3 = str3.replaceAll("\n", "");
		    }
		    System.out.println(str3);
		    for(int i = 0; i < products1.size(); i++){
				str1 = strtourlfix(products1.get(i));
				str2 = strtourlfix(products2.get(i));
				
				//SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				Calendar sd = Calendar.getInstance();
				Calendar ed = Calendar.getInstance();
				sd.add(Calendar.HOUR, -24);
				
				int dayofyear = ed.get(Calendar.DAY_OF_YEAR);
				int weekofyear = ed.get(Calendar.WEEK_OF_YEAR);
		    	//str = str.toLowerCase();
		    	//BufferedReader in2 = new BufferedReader(new FileReader("res/expertblogs.txt"));
			    //String str2;
				String title = new String();
				String content = new String();
			    int value = 0; //Good thinking! Reset the value for each product!
			    //There was a fore loop here
			    	
		    		String myUrl = "http://www.twingly.com/search?q=&phrase=&any=" + str1 + "+" + str2 + "&without=&blog=&link=&site=" + str3 + "&sort=&order=desc";
		    		if(timespan != "")
		    			myUrl += "&tspan=" + timespan;
		    		if(svlang)
		    			myUrl += "&lang=sv";
		    		
		    		URL url = new URL(myUrl);
					/* Get a SAXParser from the SAXPArserFactory. */
					//SAXParserFactory sp = SAXParserFactory.newInstance();
					SAXParserImpl spf = org.ccil.cowan.tagsoup.jaxp.SAXParserImpl.newInstance(null);
			
					/* Get the XMLReader of the SAXParser we created. */
					//XMLReader xr = sp.;
					XMLReader xr = spf.getXMLReader();
					/* Create a new ContentHandler and apply it to the XML-Reader*/ 
					BlogsHandler myBlogsHandler = new BlogsHandler();
					xr.setContentHandler(myBlogsHandler);
					
					/* Parse the xml-data from our URL. */
					HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
					httpConnection.connect(); 
					//xr.parse(new InputSource(url.openStream()));
					xr.parse(new InputSource(httpConnection.getInputStream()));
										
					//for(int i = 0; i < myRecipesHandler.getCounterValue(); i++){
						//b = myExampleHandler.getParsedData(i).getIdString().toString();
						//System.out.println(b);
					if(myBlogsHandler.getDataIndex() > 0){
						title = myBlogsHandler.getParsedData(0).getTitleString().toString();
						content = myBlogsHandler.getParsedData(0).getContentString().toString();
						//System.out.println(c + " : " + d);
					}
					//}
					//System.out.println(str + ", " + Integer.toString(myRecipesHandler.getHitsValue()));
					value += myBlogsHandler.getHitsValue();
					httpConnection.disconnect();
		    	
			    
			    //Normally the collected blog titles and contents would need to be randomized. Right
			    //now the last one is selected, thus a bias towards the latter blogs in the list.
			    
		    	System.out.println(Integer.toString(i+1) + ". " + urltostrfix(str1) + ", " + value + ", " + title + " : " + content );
		    	
		    	try {				    
				    stmt = conn.createStatement();
				    stmt.executeUpdate("INSERT INTO blogdata VALUES (" + Integer.toString(dayofyear) + "," + Integer.toString(weekofyear) + "," + Integer.toString(i+1) + ",'" + title + "','" + content + "'," + Integer.toString(value) +  ")");				    

				    // Now do something with the ResultSet ....
				}
				catch (SQLException ex){
				    // handle any errors
				    System.out.println("SQLException: " + ex.getMessage());
				    System.out.println("SQLState: " + ex.getSQLState());
				    System.out.println("VendorError: " + ex.getErrorCode());
				}
				
				title = null;
				content = null;
					
		    }
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		return;
	}
	
	//@SuppressWarnings(value={"unused"})
	static private void calculateSocialValues(){
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet rs = null;		
		for(int fruktid = 1; fruktid <= products1.size(); fruktid++){ //try to get the number of products here!
			int max_twitter_value;
			int min_twitter_value;
			int max_blog_value;
			int min_blog_value;
			
			try {
			    stmt = conn.createStatement();
			    
			    rs = stmt.executeQuery("SELECT * FROM twitterdata WHERE fruktId=" + fruktid + " AND freq = (SELECT max( freq ) FROM twitterdata WHERE fruktId=" + fruktid +")"); 
			    rs.next();
			    max_twitter_value = rs.getInt("freq");			    
			    
			    rs = stmt.executeQuery("SELECT * FROM twitterdata WHERE fruktId=" + fruktid + " AND freq = (SELECT min( freq ) FROM twitterdata WHERE fruktId=" + fruktid +")"); 
			    rs.next();
			    min_twitter_value = rs.getInt("freq");
			    
			    rs = stmt.executeQuery("SELECT * FROM blogdata WHERE fruktId=" + fruktid + " AND freq = (SELECT max( freq ) FROM blogdata WHERE fruktId=" + fruktid +")"); 
			    rs.next();
			    max_blog_value = rs.getInt("freq");			    
			    
			    rs = stmt.executeQuery("SELECT * FROM blogdata WHERE fruktId=" + fruktid + " AND freq = (SELECT min( freq ) FROM blogdata WHERE fruktId=" + fruktid +")"); 
			    rs.next();
			    min_blog_value = rs.getInt("freq");
			    
			    //1 - 53
			    for(int week = 1; week <= 53; week++){
			    	//Twittercalculation 
			    	double value = 0.0;
			    	int twitter_result = 0;
			    	rs = stmt.executeQuery("SELECT * FROM twitterdata WHERE fruktId=" + fruktid + " AND weekofyear=" + week); 
					int i = 0;
			    	while(rs.next()){
			    		i++; //OBS - not always 7!
						if((max_twitter_value - min_twitter_value) > 0)
					    	value += ((double)(rs.getInt("freq") - min_twitter_value) / (double)(max_twitter_value - min_twitter_value));
					    //value += ((double)rs.getInt("freq") / (double)max_twitter_value);	
						twitter_result += rs.getInt("freq");
					}
					value = (value / i) * 100;
					//twitter_result = (int)Math.round(value);
					
					//System.out.println(Integer.toString(i) + "," + twitter_result);
					
				    //Blogcalculation
					value = 0.0;
					int blog_result = 0;
					rs = stmt.executeQuery("SELECT * FROM blogdata WHERE fruktId=" + fruktid + " AND weekofyear=" + week); 
					i = 0;
			    	while(rs.next()){
			    		i++; 
						if((max_blog_value - min_blog_value) > 0)
					    	value += ((double)(rs.getInt("freq") - min_blog_value) / (double)(max_blog_value - min_blog_value)); 
						blog_result += rs.getInt("freq");
					}
					value = (value / i) * 100;
					//int blog_result = (int)Math.round(value);
					if(twitter_result > 0 || blog_result > 0)
						System.out.println("Week: " + week + ", T-Value: " + Integer.toString(twitter_result) + ", B-Value: " + Integer.toString(blog_result));
				    
					
					//stmt.executeUpdate("INSERT INTO socialvalues VALUES (" + week + "," + fruktid + "," + twitter_result + "," + blog_result + ")");				   
					if(twitter_result > 0 || blog_result > 0){
						//Updating table
						stmt2 = conn.createStatement();
						stmt2.executeUpdate("UPDATE socialvalues SET twittervalue=" + twitter_result + " WHERE weekofyear=" + week + " AND fruktId=" + fruktid);				   
						stmt2.executeUpdate("UPDATE socialvalues SET blogvalue=" + blog_result + " WHERE weekofyear=" + week + " AND fruktId=" + fruktid);				   
					}
			    }
			    
			}
			catch (SQLException ex){
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
		
		return;
	}	
	
	
	static private String urltostrfix(String str){
		
		str = str.replaceAll("%C3%A5", "å");
		str = str.replaceAll("%C3%A4", "ä");
		str = str.replaceAll("%C3%B6", "ö");
		
		return str;
	}
	
	static private String strtourlfix(String str){
		str = str.replaceAll("å", "%C3%A5");
		str = str.replaceAll("ä", "%C3%A4");
		str = str.replaceAll("ö", "%C3%B6");
		
		//In case of upper chars
		str = str.replaceAll("Å", "%C3%A5");
		str = str.replaceAll("Ä", "%C3%A4");
		str = str.replaceAll("ö", "%C3%B6");
		
		return str;
	}
	
	
	//Gives a list of "count" number of double values at an interval(start,stop).
	//The values correspond to a tangent hyperbolic function. Default start:stop = -2,5:9,5
	@SuppressWarnings(value={"unused"})
	static private double[] hypvalues(double start, double stop, int count){
		double step = Math.abs(stop-start)/count;
		double x;
		double y[] = new double[count+1];
		for(int i = 0; i < count; i++){
			x = start + i * step;
			y[i] = ((Math.exp(-x)-Math.exp(x))/(Math.exp(-x)+Math.exp(x)) + 1)/2;
			System.out.println(Double.toString(y[i]));
		}
			
		return y;
	}
	
	//Gives a list of "count" number of double values at an interval(start,stop).
	//How about a max-value point?
	//@SuppressWarnings(value={"unused"})
	static private double[] sinevalues(int middle, int count){
		double step = (2* Math.PI) / count;
		double mid_step = middle * step;
		double x;
		double y[] = new double[count+1];
		for(int i = 0; i < count; i++){
			x = i * step;
			y[i] = (Math.cos(x - mid_step) + 1.0f) / 2.0f;
			//System.out.println(Double.toString(y[i]));
		}
			
		return y;
	}
	

}
