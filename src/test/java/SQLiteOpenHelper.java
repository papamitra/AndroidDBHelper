
package android.database.sqlite;

public class SQLiteOpenHelper{
	public SQLiteDatabase getReabableDatabase(){
		return new SQLiteDatabase();
			}
	
	public SQLiteDatabase getWritableDatabase(){
		return new SQLiteDatabase();
	}
	
}
