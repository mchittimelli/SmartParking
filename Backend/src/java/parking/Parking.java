/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
//import org.json.simple.JSONArray;
import net.sf.json.JSONArray;
import org.json.simple.JSONObject;
//import net.sf.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * REST Web Service
 *
 * @author naren
 */
@Path("generic")
public class Parking {

    static Connection con = null;
    static PreparedStatement stm = null;
    static ResultSet rs = null;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public Parking() {
    }
    
    JSONObject mainObj = new JSONObject();
    /**
     * Retrieves representation of an instance of parking.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        try {
            createConnection();
            
            String sql = "select * from slots";

            stm = con.prepareStatement(sql);

            ResultSet rs = stm.executeQuery();

            String slotid = null;
            Boolean availability = null;
            
            JSONArray jSONArray = new JSONArray();
            JSONObject slotsObj = new JSONObject();

            while (rs.next()){
                slotid = rs.getString(1);
                availability = rs.getBoolean(2);
                
                slotsObj.put("slot", slotid);
                slotsObj.put("availability", availability);
                jSONArray.add(slotsObj);
                slotsObj.clear();
                    
            } 
            mainObj.put("building", "OnePoint");
            mainObj.put("parkingFloorA", jSONArray);
            

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Parking.class.getName()).log(Level.SEVERE,
                    null, ex);
            return getError(ex.toString(), 0, 0);
        } catch (SQLException ex) {
            Logger.getLogger(Parking.class.getName()).log(Level.SEVERE,
                    null, ex);
            return getError(ex.getMessage(), 0, 0);
        } finally {
            closeConnection();
        }

        return mainObj.toString();
    }
    
    
    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    
    
     
    @POST
    @Consumes("application/json")
    public String putJson(String json) {
        System.out.println("JSON: " + json);
        JSONParser parser = new JSONParser();
        try {
            createConnection();
            JSONObject jSONObject = (JSONObject) parser.parse(json);
            jSONObject.get("id");                   
            
            String sql = "update slots "
                    + "set availability = ?"  
                    + " where slot_id = ?" ;
            
            stm = con.prepareStatement(sql);
            String slot_id = jSONObject.get("id").toString();
            
            stm.setString(2, slot_id);
            
            if(jSONObject.get("availability").equals("true")){
                stm.setBoolean(1, true);
            } else{
                stm.setBoolean(1, false);
            }
            
            
            
            
            stm.executeUpdate();
        
            
        } catch (ParseException ex) {
            Logger.getLogger(Parking.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Parking.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Parking.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return json.toString();
    }
    
    private static void createConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/parking?autoReconnect=true&useSSL=false",
                "root", "naren");
    }

    private static void closeConnection() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                /* ignored */
            }
        }
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                /* ignored */
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                /* ignored */
            }
        }
    }

    public String getError(String name, Object key, Object value) {
        mainObj.put("Status", "Error");
        if (!value.toString().equals("0")) {
            mainObj.put(key.toString(), value);
        }
        mainObj.put("Message", name);
        return mainObj.toString();
    }

}


