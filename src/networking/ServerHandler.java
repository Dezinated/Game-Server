package networking;
import game.GameManager;
import game.Player;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


public class ServerHandler extends ChannelInboundHandlerAdapter {

	Player me;

	public ServerHandler(String username){
		me = new Player(username);
		GameManager.players.put(username, me);
	}
	
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		//Add the channel to the group
		System.out.println("SH: Server Handler Active");
		GameManager.channels.add(ctx.channel());
		GameManager.connections.put(ctx.channel(), me);
		me.setConnection(ctx.channel());
		super.handlerAdded(ctx);
		
		System.out.println("Sending player info");

		GameManager.channels.forEach(c -> System.out.println(c.id()));
		//GameManager.players.forEach((k,v) -> (System.out.println(v.getConnection().id()))); 
		
		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // closed on shutdown.
		GameManager.channels.add(ctx.channel());
		System.out.println("Channel added");
        super.channelActive(ctx);
    }
	
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		ByteBuf b = (ByteBuf) msg;
		short packetID = b.readShort();
		
		switch(packetID){
		case 1:
			//Walk packet
			if (b.readableBytes() >= 8) {
				float x = b.readFloat();
				float y = b.readFloat();
				char direction = b.readChar();
				//System.out.println("USER: "+me.getUsername()+" USER: "+GameManager.getPlayer(ctx.channel()).getUsername()+" ID: "+ctx.channel().id()+" X: "+x+" Y:"+y);
				GameManager.getPlayer(ctx.channel()).setLocation(x, y);
				GameManager.getPlayer(ctx.channel()).setDirection(direction);
			}
			break;
		}
		
		b.release();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		System.err.println(cause.getLocalizedMessage());
		GameManager.players.remove(me.getUsername());
		GameManager.connections.remove(ctx.channel().id());
	}
}
