import java.util.*;
import redis.clients.jedis.*;

public class CacheStore {
	private Jedis _jedis;
	
	public CacheStore() {
		_jedis = new Jedis("localhost");
	}
	
	public void saveString(String key, String value) {
		System.out.println("Saving the string-value item to cache: key=" + key + " value=" + value);
		_jedis.set(key, value);
		System.out.println("Saved!");
	}
	
	public void getString(String key) {
		System.out.println("Getting the string-value item from cache with key=" + key);
		String value = _jedis.get(key);
		if( value == null ) System.out.println("Could not found value");
		else System.out.println( String.format("Finished! The value=%s",value) );
	}
	
	public void getArray(String key) {
		System.out.println("Getting the array-value item from cache with key=" + key);
		List<String> values = _jedis.lrange(key, 0, -1);
		System.out.println( "Finished! The values=" + getArrayItemsString(values.toArray(new String[0])) );
	}
	
	public void delete(String key) {
		System.out.println("Deleting the item from cache with key=" + key);
		 _jedis.del(key);
		System.out.println("Finished!");
	}
	
	public void saveArray(String key, String[] values) {
		System.out.println("Saving the array-value item to cache: key=" + key + " value=" + getArrayItemsString(values));
		_jedis.del(key); // don't add up values
		_jedis.rpush(key, values);
		System.out.println("Saved!");
	}
	
	public String getArrayItemsString(String[] items) {
		boolean firstItem = true;
		String totalText = "";
		for(int i = 0; i < items.length ; ++i ) {
			String vl = items[i];
			if(firstItem) totalText += String.format("%s", vl);
			else totalText += String.format(" , %s ", vl);
			firstItem = false;
		}
		
		return totalText;
	}
	
	
	public static void printHelp() {
		System.out.println("This sample program assumes Redis is at localhost with default port");
		System.out.println("Usage: <action> key [value1[,value2]]");
		System.out.println("Where:");
		System.out.println("	<action> is one of following value: set-string , set-array , get-string , get-array , del");
		System.out.println("	key is a string for the key of object saved in redis");
		System.out.println("Examples:");
		System.out.println("	Create an object in redis with value as string: set-string mykey myvalue");
		System.out.println("	Create an object in redis with value as array: set-array mykey value1 value2 value3");
		System.out.println("	Get value of an object in redis: get-string mykey");
	}
	
	public static void main (String args[]) {
		if( args.length < 1 ) { 
			printHelp();
			return;
		}
		
		String itemKey = args[1];
		String action = args[0];
		CacheStore cs = new CacheStore();
		System.out.println("");
		if( "del".equals(action) ) cs.delete(itemKey);
		else if( "get-string".equals(action) ) cs.getString(itemKey);
		else if( "get-array".equals(action) ) cs.getArray(itemKey);
		else {
			if( args.length == 2 ) { 
				printHelp();
				return;
			}
			
			String itemValue = args[2];
			if( "set-string".equals(action) ) cs.saveString(itemKey, itemValue);
			else if( "set-array".equals(action) ) {
				String[] itemValues = (String[])Arrays.copyOfRange(args, 2, args.length);
				cs.saveArray(itemKey, itemValues);
			}
		}
	}
}