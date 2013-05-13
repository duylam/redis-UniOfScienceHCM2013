import java.util.*;
import redis.clients.jedis.*;

public class SubscriberClient {
	
	public static void printHelp() {
		System.out.println("This sample program assumes Redis is at localhost with default port");
		System.out.println("Usage: channel");
		System.out.println("Where:");
		System.out.println("	channel is a string for channel to listen from redis");
	}
	
	public static void subscribeChannel(String channel) throws java.lang.InterruptedException {
		Jedis client = new Jedis("localhost");
        System.out.println( String.format("Subscribed at channel %s. Press Ctrl+C to exit. Waiting for update from redis", channel) );
		client.subscribe(new JedisPubSub() {
            public void onMessage(String channel, String message) {
                System.out.println( String.format("Received data: %s", message) );
            }

            public void onSubscribe(String channel, int subscribedChannels) {  }

            public void onUnsubscribe(String channel, int subscribedChannels) { }

            public void onPSubscribe(String pattern, int subscribedChannels) { }

            public void onPUnsubscribe(String pattern, int subscribedChannels) {  }

            public void onPMessage(String pattern, String channel, String message) { }
			
        }, channel);
        while( true ) {
			Thread.sleep(1000);
		}
	}
	
	public static void main (String args[]) throws java.lang.InterruptedException {
		if( args.length == 1 ) {
			subscribeChannel(args[0]);
		}
		else { 
			printHelp();
		}
	}
}