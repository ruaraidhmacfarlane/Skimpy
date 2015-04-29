<%@ page import="java.util.*"%>
<%@ page import="BusinessLogic.*"%>
<%@ page import="interfc.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Arrays.*"%>
<%@ page language="java" contentType="text/html"%>
<%@ page import="java.io.File"%>

<% String pageTitle = "Shopping List"; %>
<% String currentPage = "shopping_list"; %>
<%@ include file="header.jsp"%>

<% 
if (session.getAttribute("username") == null) {
	response.sendRedirect("login.jsp");
}
%>
<div class="container-fluid">
	<div class="col-sm-8">

<%
	int userId = (Integer)session.getAttribute("ID");
 	ShoppingList list = new ShoppingList();
 	String path = getServletContext().getRealPath("");
 	HashMap<ArrayList<Food>, ArrayList<Double>> shoppingList = list.getShoppingList(path, userId);

 	ArrayList<Food> foodList = new ArrayList<Food>();
 	ArrayList<Double> massList = new ArrayList<Double>();
 	
 	double total = 0;
 	DecimalFormat moneyDecimal = new DecimalFormat("0.00");

    if (shoppingList != null) {   
	 	for (Map.Entry<ArrayList<Food>, ArrayList<Double>> entry : shoppingList.entrySet()){
 	  		foodList = entry.getKey(); 
 	    	massList = entry.getValue(); 
 	    }
 	    
	 	for (int i = 0; i < foodList.size(); i++){
	 		 String link;
	    	 if (foodList.get(i).getSupermarket().equals("T")) {
	    		 link = "http://www.tesco.com/groceries/product/details/?id=" + foodList.get(i).getShopID();
	    	 } else if (foodList.get(i).getSupermarket().equals("S")) {
	    		 link = foodList.get(i).getShopID();
	    	 } else {
	    		 link = foodList.get(i).getShopID();
	    	 }
	    	 
	    	 total += foodList.get(i).getPrice();
%>
		<div class="container-fluid">
			<div class="col-sm-6">
				<a href="<%=link%>"><span class="list-product-name"><%=foodList.get(i).getName()%>
				</span></a>
			</div>
			<div class="col-sm-4">
				<span>�<%=moneyDecimal.format(foodList.get(i).getPrice())%></span>
			</div>
			<div class="col-sm-4">
				<span><%=massList.get(i)%></span>
			</div>
			<button id="button_<%= i %>" class="optimise btn btn-block btn-success" style="width:100px"	type="button"
				onclick="findOffers('<%=foodList.get(i).getDBID()%>', '<%=foodList.get(i).getSupermarket()%>', <%= moneyDecimal.format(foodList.get(i).getPrice())%>, this.id)">
				Optimise
			</button>
			<div style="clear:both"></div>
			<div class="suggestions-box col-sm-6" id="<%= i %>"></div>
		</div>
		
<%
		}
%>
	<h2>Total: �<%= moneyDecimal.format(total) %></h2>
<%
	} else {
%>
		<p>
			<button class="btn btn-block btn-success btn-lg" style="width: 150px"
				onclick="document.location.href='editPlan.jsp'">Create a
				Meal Plan</button>
		</p>
	<%
     	}
     %>

	</div>
	
	<div id="log-box">
		
	</div>
</div>

</body>
</html>
