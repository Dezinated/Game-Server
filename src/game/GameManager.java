package game;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;

public class GameManager {
	public static HashMap<String, Player> players = new HashMap<String,Player>();
	public static HashMap<Channel, Player> connections = new HashMap<Channel,Player>();
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	public static synchronized Player getPlayer(int index){
		return (Player) players.values().toArray()[index];
	}
	
	public static synchronized Player getPlayer(Channel c){
		return connections.get(c);
	}
}
