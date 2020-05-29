package DataParsingEssentials.DataParsingEssentials;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class DatabaseConnection {

	private String UserName;
	private String Password;
	private String ConnectionString;
	private Connection con = null;
	private Statement stmt = null;
	Properties p = new Properties();

	public DatabaseConnection() {
		try {
			FileReader reader = new FileReader(".\\Resource.properties");
			p.load(reader);
			UserName = p.getProperty("UserName");
			Password = p.getProperty("Password");
			ConnectionString = p.getProperty("ConnectionString");
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(ConnectionString + "?user=" + UserName + "&password=" + Password);
			stmt = con.createStatement();
		} catch (Exception ex) {
			System.out.println("Connection is not established:" + ex.getMessage());
		}
	}

	public void ExecuteQuery(String query) {
		try {
			ArrayList<EmployeeDetails> ar = new ArrayList<EmployeeDetails>();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				EmployeeDetails ed = new EmployeeDetails();
				ed.setId(rs.getInt("Id"));
				ed.setName(rs.getString("Name"));
				ed.setLastName(rs.getString("LastName"));
				ed.setAge(rs.getInt("Age"));
				ed.setGender(rs.getString("Gender"));
				ar.add(ed);
			}
			con.close();
			System.out.println("Convert the java object to JSON formate");
			JSONArray jsonA = new JSONArray();
			for (int i = 0; i < ar.size(); i++) {
				// Convert java object into jsonString
				Gson s = new Gson();
				String JsonString = s.toJson(ar.get(i));
				jsonA.add(JsonString); // Add a json string to jsonArray
			}
			JSONObject jObj = new JSONObject();
			jObj.put("data", jsonA);
			FileWriter file = new FileWriter(".\\MultiAdd.json");
			System.out.println(jObj.toJSONString().replace("\\\"", "\"").replace("\"{", "{").replace("}\"", "}"));
			file.write(jObj.toJSONString().replace("\\\"", "\"").replace("\"{", "{").replace("}\"", "}"));
			file.close();
			System.out.println("Done!!");
		} catch (Exception ex) {
			System.out.println("Query is not executed:" + ex.getMessage());
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public void ReadJsonFileSingleData(String filePath) {
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(filePath));
			String value = (String) jsonObject.get("Name");
			System.out.println(value);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void ReadJsonFileWithmultipledata(String filePath) {
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(filePath));
			
			JSONArray jarr = (JSONArray)jsonObject.get("data");
			
			for(int i=0;i<jarr.size();i++) {
				 	JSONObject jobj = (JSONObject) jarr.get(i);
				 	String Id = String.valueOf(jobj.get("Id"));
				 	String Name =(String) jobj.get("Name");
				 	String LastName =(String) jobj.get("LastName");
				 	String Age =String.valueOf(jobj.get("Age"));
				 	String Gender =(String) jobj.get("Gender");
				 	System.out.println(Id +" "+ Name + " " + LastName +" " + Age +" "+ " "+Gender);
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public String GetRandomString(int value) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz"; 
		StringBuilder sb = new StringBuilder(value); 
        for (int i = 0; i < value; i++) { 
            int index = (int)(AlphaNumericString.length() * Math.random()); 
            sb.append(AlphaNumericString.charAt(index)); 
        } 
        return sb.toString(); 
	}
	
	public String GetRandomNumberString(int value) {
		String Number = "1234567890";
		StringBuilder sb=new StringBuilder(value);
		for (int i = 0; i < value; i++) {
			int index = (int)(Number.length() * Math.random());
			sb.append(Number.charAt(index));
		}
		return sb.toString();
	}
}
