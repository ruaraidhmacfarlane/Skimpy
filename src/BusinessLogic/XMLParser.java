//@author Lee
package BusinessLogic;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLParser {
	
	/*
	 * Returns an arraylist of meals from the file meals.xml
	 */
	public static ArrayList<Meal> readMeals() {
		DBConnect dbcon = new DBConnect();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse("data/meals.xml");
			doc.getDocumentElement().normalize();
			NodeList rawMeals = doc.getElementsByTagName("Meal");
			ArrayList<Meal> meals = new ArrayList<Meal>();
			for (int i = 0; i < rawMeals.getLength(); i++) {
				Element mealelem = (Element)rawMeals.item(i);
				String name = mealelem.getElementsByTagName("Name").item(0).getTextContent();
				NodeList rawFood = mealelem.getElementsByTagName("Food");
				ArrayList<Food> foods = new ArrayList<Food>();
				System.out.print(name + ": ");
				for (int j = 0; j < rawFood.getLength(); j++) {
					Element foodelem = (Element)rawFood.item(j);
					String shopName = foodelem.getAttribute("shop");
					String foodid = foodelem.getTextContent();
					System.out.print(", " + shopName + ": " + foodid);
					Food f = dbcon.pullFood(shopName, foodid);
					foods.add(f);
				}
				System.out.println("");
				Meal m = new Meal(name, foods);
				meals.add(m);
			}
			return meals;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Writes an arraylist of meals to the file meals.xml
	 */
	public static void writeMeals(ArrayList<Meal> meals) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			Element root = doc.createElement("Meals");
			doc.appendChild(root);
			for (Meal m: meals) {
				Element meal = doc.createElement("Meal");
				root.appendChild(meal);
				Element name = doc.createElement("Name");
				name.appendChild(doc.createTextNode(m.getName()));
				meal.appendChild(name);
				Element ing = doc.createElement("Ingredients");
				meal.appendChild(ing);
				for (Food f: m.getIngredients()) {
					Element food = doc.createElement("Food");
					Attr shopname = doc.createAttribute("shop");
					shopname.setValue(f.getShopID());
					food.setAttributeNode(shopname);
					food.appendChild(doc.createTextNode("" + f.getDBID()));
					ing.appendChild(food);
				}
			}
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			DOMSource src = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("data/meals.xml"));
			
			t.transform(src, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Reads an arraylist of mealplans from the file mealplans.xml
	 */
	public static ArrayList<MealPlanner> readMealPlans() {
		DBConnect dbcon = new DBConnect();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse("data/mealplans.xml");
			doc.getDocumentElement().normalize();
			NodeList rawMealPlans = doc.getElementsByTagName("MealPlan");
			ArrayList<MealPlanner> mealplans = new ArrayList<MealPlanner>();
			MealPlanner mp = new MealPlanner();
			for (int i = 0; i < rawMealPlans.getLength(); i++) {
				Element mealplanelem = (Element)rawMealPlans.item(i);
				NodeList days = mealplanelem.getElementsByTagName("Day");
				for (int j = 0; j < days.getLength(); j++) {
					Element dayElem = (Element)days.item(j);
					String day = dayElem.getAttribute("value");
					NodeList times = dayElem.getElementsByTagName("Time");
					for (int k = 0; k < times.getLength(); k++) {
						Element timeElem = (Element)times.item(k);
						String time = timeElem.getAttribute("value");
						String name = timeElem.getElementsByTagName("Name").item(0).getTextContent();
						NodeList rawFood = timeElem.getElementsByTagName("Food");
						ArrayList<Food> foods = new ArrayList<Food>();
						System.out.print(day + " - " + time + ":> " + name + ", ");
						for (int l = 0; l < rawFood.getLength(); l++) {
							Element foodelem = (Element)rawFood.item(l);
							String shopName = foodelem.getAttribute("shop");
							String foodid = foodelem.getTextContent();
							System.out.print(", " + shopName + ": " + foodid);
							Food f = dbcon.pullFood(shopName, foodid);
							foods.add(f);
						}
						System.out.println("");
						Meal m = new Meal(name, foods);
						mp.add(m, day, time);
					}
				}
				mealplans.add(mp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * writes an arraylist of mealplans to the file mealplans.xml
	 */
	public static void writeMealPlans(ArrayList<MealPlanner> mealplanners) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			Element root = doc.createElement("MealPlans");
			doc.appendChild(root);
			for (MealPlanner mp : mealplanners) {
				Element mealplan = doc.createElement("MealPlan");
				root.appendChild(mealplan);
				String[] dayValues = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
				String[] timeValues = {"Breakfast", "Lunch", "Dinner"};
				for (int i = 0; i < dayValues.length; i++) {
					Element day = doc.createElement("Day");
					Attr dayValue = doc.createAttribute("value");
					dayValue.setValue(dayValues[i]);
					day.setAttributeNode(dayValue);
					mealplan.appendChild(day);
					for (int j = 0; j < timeValues.length; j++) {
						Element time = doc.createElement("Time");
						Attr timeValue = doc.createAttribute("value");
						timeValue.setValue(timeValues[j]);
						time.setAttributeNode(timeValue);
						day.appendChild(time);
						Meal m = mp.getMeal(i, j);
						Element meal = doc.createElement("Meal");
						time.appendChild(meal);
						Element name = doc.createElement("Name");
						name.appendChild(doc.createTextNode(m.getName()));
						meal.appendChild(name);
						Element ing = doc.createElement("Ingredients");
						meal.appendChild(ing);
						for (Food f: m.getIngredients()) {
							Element food = doc.createElement("Food");
							Attr shopname = doc.createAttribute("shop");
							shopname.setValue(f.getShopID());
							food.setAttributeNode(shopname);
							food.appendChild(doc.createTextNode("" + f.getDBID()));
							ing.appendChild(food);
						}
					}
				}
			}

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			DOMSource src = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("data/mealplans.xml"));
			
			t.transform(src, result);
		} catch (Exception e) {
			
		}
	}

	

}
