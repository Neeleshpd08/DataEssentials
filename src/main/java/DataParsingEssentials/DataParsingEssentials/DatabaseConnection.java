package DataParsingEssentials.DataParsingEssentials;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class DatabaseConnection {

	private String UserName;
	private String Password;
	private String ConnectionString;
	private Connection con =null;
	private Statement stmt =null;
	Properties p=new Properties();

	public DatabaseConnection() {
		try {
			FileReader reader=new FileReader(".\\Resource.properties");
			p.load(reader);
			UserName = p.getProperty("UserName");
			Password = p.getProperty("Password");
			ConnectionString =p.getProperty("ConnectionString");
			Class.forName("com.mysql.cj.jdbc.Driver");  	
			con = DriverManager.getConnection(ConnectionString+"?user="+UserName+"&password="+Password);
			stmt=con.createStatement();
		}
		catch(Exception ex) {
			System.out.println("Connection is not established:"+ ex.getMessage());
		}
	}

	public void ExecuteQuery(String query) {
		try {
			ArrayList<EmployeeDetails> ar=new ArrayList<EmployeeDetails>();
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()) {
				EmployeeDetails ed=new EmployeeDetails();
				ed.setId(rs.getInt("Id"));
				ed.setName(rs.getString("Name"));
				ed.setLastName(rs.getString("LastName"));
				ed.setAge(rs.getInt("Age"));
				ed.setGender(rs.getString("Gender"));
				ar.add(ed);
			}
			con.close(); 
			System.out.println("Convert the java object to JSON formate");
			JSONArray jsonA=new JSONArray();
			for(int i=0;i<ar.size();i++) {
				//Convert java object into jsonString 
				Gson s=new Gson();
				String JsonString = s.toJson(ar.get(i));
				jsonA.add(JsonString); //Add a json string to jsonArray
			}
			JSONObject jObj=new JSONObject();
			jObj.put("data", jsonA);
			FileWriter file=new FileWriter(".\\MultiAdd.json");
			System.out.println(jObj.toJSONString().replace("\\\"", "\"").replace("\"{", "{").replace("}\"", "}"));
			file.write(jObj.toJSONString().replace("\\\"", "\"").replace("\"{", "{").replace("}\"", "}"));
			file.close();
			System.out.println("Done!!");
		}
		catch(Exception ex) {
			System.out.println("Query is not executed:"+ex.getMessage());
		}
	}

}
