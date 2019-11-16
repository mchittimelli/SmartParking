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
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
    @Path("slot")
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
                
                slotsObj.accumulate("slot", slotid);
                slotsObj.accumulate("availability", availability);
                jSONArray.add(slotsObj);
                slotsObj.clear();
                    
            } 
            mainObj.accumulate("building", "OnePoint");
            mainObj.accumulate("parkingFloorA", jSONArray);
            

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
    
    
    
    @PUT
    @Consumes("application/json")
    public void putJson(String jSON) {
        System.out.println("JSON: " + jSON);
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
        mainObj.accumulate("Status", "Error");
        if (!value.toString().equals("0")) {
            mainObj.accumulate(key.toString(), value);
        }
        mainObj.accumulate("Message", name);
        return mainObj.toString();
    }

}


