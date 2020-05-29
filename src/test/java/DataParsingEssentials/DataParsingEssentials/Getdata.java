package DataParsingEssentials.DataParsingEssentials;

public class Getdata{

	static DatabaseConnection db=new DatabaseConnection();
	public static void main(String[] args) {
		db.GetRandomString(7);
		String i = db.GetRandomNumberString(10);
		db.ExecuteQuery("select * from emp");
		db.ReadJsonFileWithmultipledata("E:\\AppiumProject\\DataParsingEssentials\\MultiAdd.json");
	}
}