package com.madfinger.erik;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.*;

@WebServlet(
    name = "HelloAppEngine",
    urlPatterns = {"/hello"}
)
public class HelloAppEngine extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
      
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");

    response.getWriter().print("Hello App Engine!\r\n");
    
    
    DatastoreService store = DatastoreServiceFactory.getDatastoreService();

    
    Transaction tr = store.beginTransaction();
    
    Entity customer = new Entity("Customer",1);
    customer.setProperty("FirstName", "Antonio");
    customer.setProperty("LastName", "Salieri");
    customer.setProperty("Created", new Date());
    customer.setProperty("active", true);
    
    store.put(customer);
    
    
    Entity address = new Entity("Address",1,customer.getKey());
    address.setProperty("Street","Vodarenska");
    address.setProperty("HouseNumber",86);
    address.setProperty("City","Piestany");
    address.setProperty("ZipCOde","92101");
    store.put(address);
    
    Entity customer2 = new Entity("Customer",2);
    customer2.setProperty("FirstName", "Antonio");
    customer2.setProperty("LastName", "Salieri");
    customer2.setProperty("Created", new Date());
    customer2.setProperty("active", true);
    
    store.put(customer2);
    
    Entity address2 = new Entity("Address",2,customer2.getKey());
    address2.setProperty("Street","Pod Parovcami");
    address2.setProperty("HouseNumber",106);
    address2.setProperty("City","Piestany");
    address2.setProperty("ZipCOde","92101");
    store.put(address2);
    
    tr.commit();
    
    Key k1 = new KeyFactory.Builder("User","GrandGrandPa").addChild("User", "GrandPa").addChild("User", "Pa").getKey();
    System.out.println("Generated key for the Pa is: "+k1);
    
    
    Entity getEntity = null;
    try {
    	
    	Key keyIndex = KeyFactory.createKey(customer2.getKey(),"Address",2);
    	getEntity = store.get(keyIndex);
    	
    	Filter propertyFilter =
    		    new FilterPredicate("HouseNumber", FilterOperator.GREATER_THAN, 100); 
    	
    	
    	Query q = new Query("Address").setFilter(propertyFilter);
    	PreparedQuery pq =  store.prepare(q);
    	
    	for(Entity item : pq.asIterable()){
    		System.out.println(item.toString());
    	}
    	
    }catch(EntityNotFoundException e){
    	response.getWriter().println(e.getMessage());
    }
    
    if(getEntity != null) {
    	response.getWriter().print(((String)getEntity.getProperty("Street"))+ " ");
    	response.getWriter().print(((long)getEntity.getProperty("HouseNumber"))+"\r\n");
    }
    
    
    
  }
}