package DataParsingEssentials.DataParsingEssentials;

public class Getdata{

	static DatabaseConnection db=new DatabaseConnection();
	public static void main(String[] args) {
		db.ExecuteQuery("select * from emp");
	}
}