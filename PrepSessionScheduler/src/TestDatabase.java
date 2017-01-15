import model.StudentDatabase;

public class TestDatabase {

	public static void main(String[] args) {
		
		StudentDatabase db = new StudentDatabase();
				
		System.out.println("running database test");
		
		try {
			//db.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//db.disconnect();
	}

}
