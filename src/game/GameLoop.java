package game;

import io.netty.buffer.Unpooled;
import io.netty.channel.group.ChannelMatchers;
import io.netty.util.CharsetUtil;

public class GameLoop implements Runnable {

	long lastTime;
	
	@Override
	public void run(){
		while(!Thread.currentThread().isInterrupted()){
			lastTime = System.currentTimeMillis();
			
			//System.out.println("Updating players");
			for(int i=0;i<GameManager.players.size();i++){
				Player p = GameManager.getPlayer(i);
				//System.out.println(p.getUsername()+" X:"+p.getLocation().x+" Y:"+p.getLocation().y);
				long time = System.nanoTime();
				//System.out.println("["+time+"] Update player "+p.getUsername() +" X: "+p.getLocation().x+" Y:"+p.getLocation().y);
				GameManager.channels.write(Unpooled.copyShort(0), ChannelMatchers.isNot(p.getConnection()));
				GameManager.channels.write(Unpooled.copyFloat(p.getLocation().x), ChannelMatchers.isNot(p.getConnection()));
				GameManager.channels.write(Unpooled.copyFloat(p.getLocation().y), ChannelMatchers.isNot(p.getConnection()));
				GameManager.channels.write(Unpooled.copyShort((int)p.getDirection()), ChannelMatchers.isNot(p.getConnection()));
				//GameManager.channels.write(Unpooled.copyLong(time), ChannelMatchers.isNot(p.getConnection()));
				GameManager.channels.write(Unpooled.copiedBuffer(p.getUsername()+"\n", CharsetUtil.UTF_8), ChannelMatchers.isNot(p.getConnection()));
				GameManager.channels.flush(ChannelMatchers.isNot(p.getConnection()));
				//GameManager.channels.flush();*/
			}
			GameManager.players.forEach((k,v) -> {
				
			});
			
			if((System.currentTimeMillis() - lastTime) < 50){
				try {
					Thread.sleep(50 - (System.currentTimeMillis() - lastTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}

}
