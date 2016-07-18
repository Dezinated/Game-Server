package game;

import io.netty.channel.Channel;
import math.Vector3f;

public class Player {

	private String username;
	private Vector3f location = new Vector3f();
	private Channel connection;
	private char direction;
	
	public Player(String username){
		this.username = username;
	}
	
	public void setConnection(Channel c){
		connection = c;
	}
	
	public Channel getConnection(){
		return connection;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setLocation(float x, float y){
		this.location.x = x;
		this.location.y = y;
	}
	
	public Vector3f getLocation(){
		return location;
	}
	
	public void setDirection(char d){
		direction = d;
	}
	
	public char getDirection(){
		return direction;
	}
}
