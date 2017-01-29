package introsde.rest.storage;

import introsde.assignment.soap.ws.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.List;

import javax.ejb.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import org.json.*;



@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
@Path("/storage")
public class Storage {
 
     //Getting a motivation quote from quotesondesign
     @GET
     @Path("/getQuoteMotivation")
     public Response getQuote() throws ClientProtocolException, IOException {
    	
     	//String ENDPOINT = "http://10.218.204.124:5700/introsde/adapter/getQuote";
     	String ENDPOINT = "https://sdeadapter.herokuapp.com/introsde/adapter/getQuote";
     	DefaultHttpClient client = new DefaultHttpClient();
     	HttpGet request = new HttpGet(ENDPOINT);
     	HttpResponse response = client.execute(request);
    	
     	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

     	StringBuffer result = new StringBuffer();
     	String line = "";
     	while ((line = rd.readLine()) != null) {
     	    result.append(line);
     	}
    	
     	JSONObject o = new JSONObject(result.toString());
    	
     	if(response.getStatusLine().getStatusCode() == 200){
     		return Response.ok(o.toString()).build();
         }
    	
     	return Response.status(204).build();
    	
     }
     
   //Getting a motivation quote from forismatic
     @GET
     @Path("/getQuoteMotivation2")
     public Response getQuote2() throws ClientProtocolException, IOException {
    	
     	//String ENDPOINT = "http://10.218.204.124:5700/introsde/adapter/getQuote2";
     	String ENDPOINT = "https://sdeadapter.herokuapp.com/introsde/adapter/getQuote2";
     	DefaultHttpClient client = new DefaultHttpClient();
     	HttpGet request = new HttpGet(ENDPOINT);
     	HttpResponse response = client.execute(request);
    	
     	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

     	StringBuffer result = new StringBuffer();
     	String line = "";
     	while ((line = rd.readLine()) != null) {
     	    result.append(line);
     	}
    	
     	JSONObject o = new JSONObject(result.toString());
    	
     	if(response.getStatusLine().getStatusCode() == 200){
     		return Response.ok(o.toString()).build();
         }
    	
     	return Response.status(204).build();
    	
     }

	 // -------------"SOAP client requests to Local DB service"------------------------ 


	// 1) Getting person details
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/getPersonDetails")
    public Response getPersonDetails() throws ClientProtocolException, IOException {
        PeopleService service = new PeopleService();
        People people = service.getPeopleImplPort();
        Person p = people.readPerson(1);

        return Response.status(200).entity(p).build();
    }

    // 2) Updating healthprofile measures 
    @PUT
    @Path("/updateHP/{idPerson}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updatePersonHealthMeasureHistory (@PathParam("idPerson") int idPerson, HealthMeasureHistory newHealthMeasureHistory) throws IOException {

        PeopleService service4 = new PeopleService();
        People people = service4.getPeopleImplPort();
        HealthMeasureHistory newHMH2 = people.updatePersonHealthMeasureHistory(idPerson,newHealthMeasureHistory.getMeasureName(), newHealthMeasureHistory);
        Person p1 = people.readPerson(1);
        return Response.status(200).entity(p1).build();
        }

    // 3) Reading all the goals

    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/getGoals")

    public Response getGoals() throws ClientProtocolException, IOException {
        PeopleService service2 = new PeopleService();
        People people = service2.getPeopleImplPort();

        List<Goal> gList = people.getGoalsList();
        return Response.status(200).entity(gList).build();

    }

    // 4) Updating the goal
    @PUT
    @Path("/updateGoal")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateGoal (Goal goal) throws IOException {

        PeopleService service3 = new PeopleService();
        People people = service3.getPeopleImplPort();
        
        Goal newGoal = new Goal();
        newGoal.setType(goal.getType());
        newGoal.setValue(goal.getValue());
        newGoal.setIdGoal(goal.getIdGoal());
        
        Goal updatedGoal = people.updateGoal(newGoal);
        if(updatedGoal.getIdGoal() == -1){
            return Response.status(400).build();
        }
        
        return Response.ok(updatedGoal).build();
    }

}
