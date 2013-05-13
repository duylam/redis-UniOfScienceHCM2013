import java.util.*;
import redis.clients.jedis.*;

public class PublisherClient {
	public static void printHelp() {
		System.out.println("This sample program assumes Redis is at localhost with default port");
		System.out.println("Usage: channel data");
		System.out.println("Where:");
		System.out.println("	channel is a string for channel to publish to redis");
		System.out.println("	data is a string to send to subcriber at given channel");
	}
	
	public static void main (String args[]) {
		if( args.length == 2 ) {
			String data = args[1];
			String channel = args[0];
			Jedis client = new Jedis("localhost");
			client.publish(channel, data);
			System.out.println("Published!");
		}
		else { 
			printHelp();
		}
	}
}