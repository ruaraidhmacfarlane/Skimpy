package ProductExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import initialiseDatabase.*;

public class ScraperMainInterface {

	public static void main(String[] args) {
		try {
			System.out.println("Product Extractor and Database Initialize Interface");
			System.out.println("***************************************************");
			while (true) {
				InputStreamReader cin = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(cin);
				System.out.println("Enter the character beside the menu you would like to visit");
				System.out.println("Only the first character will be read");
				System.out.println();
				System.out.println("a:> Initialize database");
				System.out.println("b:> Run scrapers");
				System.out.println("c:> Initialize tables");
				System.out.println("d:> Quit");
				char userInput = (br.readLine().toLowerCase() + " ").charAt(0);
				switch (userInput) {
				case 'a':
					System.out.println("Initializing the database will delete everything in it");
					System.out.println("Would you like to continue (y/n)? Only first character is read");
					char userConfirm  = (br.readLine().toLowerCase() + " ").charAt(0);
					if (userConfirm == 'y') {
						AutoDB adb = new AutoDB();
						adb.initDB();
					}
					break;
				case 'b':
					scraperMenu();
					break;
				case 'c':
					AutoDBMainInterface.DBTableInitMenu();
					break;
				case 'd':
					System.exit(0);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void scraperMenu() {
		try {
			while (true) {
				AutoDB adb = new AutoDB();
				InputStreamReader cin = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(cin);
				System.out.println("Which website would you like to scrape? Any other option will cancel. Only reads the first character");
				System.out.println("[t = tesco, s = sainsburys, a = asda, x = all, q = quit]");
				char userInput = (br.readLine().toLowerCase() + " ").charAt(0);
				switch (userInput) {
				case 't':
					TescoScraper.main(new String[0]);
					adb.initTable('t');
					adb.pushToDB('t');
					break;
				case 's':
					System.out.println("How many threads would you like to run?");
					System.out.println("Default is 15");
					int noThreads;
					try {
						noThreads = Integer.parseInt(br.readLine().trim());
					} catch (Exception e) {
						noThreads = 15;
					}
					SainsburysScraper.runScraper(noThreads, true);
					adb.initTable('s');
					adb.pushToDB('s');
					break;
				case 'a':
					System.out.println("How many threads would you like to run?");
					System.out.println("Default is 5");
					int noAThreads;
					try {
						noAThreads = Integer.parseInt(br.readLine().trim());
					} catch (Exception e) {
						noAThreads = 5;
					}
					AsdaScraper.runScraper(noAThreads);
					adb.initTable('a');
					adb.pushToDB('a');
					break;
				case 'x':
					System.out.println("How many threads would you like the Sainsburys scraper to run?");
					System.out.println("Default is 15");
					int allSThreads;
					try {
						allSThreads = Integer.parseInt(br.readLine().trim());
					} catch (Exception e) {
						allSThreads = 15;
					}
					System.out.println();
					System.out.println("How many threads would you like the Asda scraper to run?");
					System.out.println("Default is 5");
					int allAThreads;
					try {
						allAThreads = Integer.parseInt(br.readLine().trim());
					} catch (Exception e) {
						allAThreads = 5;
					}
					runAllScrapers(allSThreads, allAThreads);
					break;
				case 'q':
					System.exit(0);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void runAllScrapers(int sThreads, int aThreads) {
		try {
			PrintWriter writer = new PrintWriter("data/scraperLog.txt");
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
			
			Date date = new Date();
			String s = dateFormat.format(date) + ":> Tesco Scraper started";
			System.out.println(s);
			writer.println(s);
			
			TescoScraper.main(new String[0]);
			
			Scanner file = new Scanner(new File("data/tesco.txt"));
			int count = 0;
			while (file.hasNextLine()) {
				count++;
				file.nextLine();
			}
			file.close();
			date = new Date();
			s = dateFormat.format(date) + ":> Tesco Scraper finished, file has " + count + " lines";
			System.out.println(s);
			writer.println(s);
			
			date = new Date();
			s = dateFormat.format(date) + ":> Sainsburys Scraper started";
			System.out.println(s);
			writer.println(s);
			
			SainsburysScraper.runScraper(sThreads, true);
			
			file = new Scanner(new File("data/sainsburys.txt"));
			count = 0;
			while (file.hasNextLine()) {
				count++;
				file.nextLine();
			}
			file.close();
			date = new Date();
			s = dateFormat.format(date) + ":> Sainsburys Scraper finished, file has " + count + " lines";
			System.out.println(s);
			writer.println(s);
			
			date = new Date();
			s = dateFormat.format(date) + ":> Asda Scraper started";
			System.out.println(s);
			writer.println(s);
			
			AsdaScraper.runScraper(aThreads);
			
			file = new Scanner(new File("data/asda.txt"));
			count = 0;
			while (file.hasNextLine()) {
				count++;
				file.nextLine();
			}
			file.close();
			date = new Date();
			s = dateFormat.format(date) + ":> Asda Scraper finished, file has " + count + " lines";
			System.out.println(s);
			writer.println(s);
			
			writer.close();
			
			AutoDB adb = new AutoDB();
			adb.initTable('t');
			adb.pushToDB('t');
			adb.initTable('s');
			adb.pushToDB('s');
			adb.initTable('a');
			adb.pushToDB('a');
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
