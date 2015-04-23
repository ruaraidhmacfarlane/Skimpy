<%@page import="BusinessLogic.*"%>
<%@page import="interfc.*"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.io.FileWriter" %>
<%@page import="java.util.Arrays" %>
<%@ page language="java" contentType="text/html" %>
<%@page import="java.io.OutputStreamWriter"%>
<%@page import="java.io.Writer"%>
<%@page import="java.nio.charset.Charset"%>
<%@include file="header.jsp" %>

<html>
<head>
<title>Saving Meal</title>
</head>
<meta http-equiv="refresh" content="0; url=http://localhost:8080/Skimpy/viewRecipe.jsp" />
<body>

 <%  
 	    String MealName = request.getParameter("mealname");
	    String  Shops =  request.getParameter("supermarket");
	    ArrayList<String> shopList = new ArrayList<String>();
	    for(String s: Shops.split(";")){
	    	shopList.add(s);
	    	
	    }
	    String ShopIds =  request.getParameter("ingred");
	    ArrayList<String> idList = new ArrayList<String>();
	    for(String s: ShopIds.split(";")){
	    	idList.add(s);
	    	
	    }
	    
	    String [] Servings =  request.getParameterValues("mass");
	    ArrayList<Integer> massList = new ArrayList<Integer>();
	    for(String s: Servings){
	    	if(s.equals("")){
	    		massList.add(0);}else{
	    	
	    	massList.add(Integer.parseInt(s));
	    	
	    }
	    }
        
        String masses = java.util.Arrays.deepToString(Servings);
        int len = shopList.size();
        int lenid = idList.size();
        int lenm = massList.size();
        DBConnect con = new DBConnect();
    	
    		ArrayList<Food> f_ingredients = new ArrayList<Food>();
    		for (int i =0; i<shopList.size();i++){
    			
    		  f_ingredients.add(con.pullFood(shopList.get(i), idList.get(i)));
    		  }
    		
    		
    		Meal currentMeal = new Meal(MealName, f_ingredients, massList );
    		ArrayList<Meal> meals = new ArrayList<Meal>();
    		meals.add(currentMeal);
    		
    		XMLParser writeX = new XMLParser();
    		ArrayList<Meal> readmeals = new ArrayList<Meal>();
    		
    		if (writeX.readMeals(getServletContext().getRealPath("") + "/meals.xml")!=null){
    		
    	    readmeals = writeX.readMeals(getServletContext().getRealPath("") + "/meals.xml");
    	    readmeals.add(currentMeal);
    	    writeX.writeMeals(readmeals, getServletContext().getRealPath("") + "/meals.xml");
    	    }
    		else{
    		writeX.writeMeals(meals, getServletContext().getRealPath("") + "/meals.xml");
    		};
    	    
    	    
    	    
    	    String path = getServletContext().getRealPath("");
    	    
    	    
    	    
    	    String currentname= currentMeal.getName(); 
    	    %>
    	   <br>
Your new recipe:
RECIPE

<br>
Ingredients:
<br><%-- <%for (int i=0; i< currentMeal.getIngredients().size();i++){
	                %>
	                <%=currentMeal.getIngredients().get(i).getName()%>
	                <%=currentMeal.getMasses().get(i)%>
	                <br>
	                <% }%> --%>

SOME FOOD
<% session.setAttribute("name", currentname); %>
<form action="viewRecipe.jsp" method="POST">

<input type="hidden" value="<%=currentname%>" name="currentmeal">

</form>
 	
</body>
</html>
