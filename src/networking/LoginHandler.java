package networking;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class LoginHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("LH: Connection established");
	}
	

	public void channelRead(ChannelHandlerContext ctx, Object msg){
		ByteBuf b = (ByteBuf) msg;
		String[] info = b.toString(CharsetUtil.UTF_8).split("\n");
		System.out.println("USER FROM "+ctx.channel().localAddress()+ " TRYING TO LOG INTO ACCOUNT "+info[0]);
		//if(info[0].equalsIgnoreCase("username") && info[1].equals("password")){
		if(info[1].equals("password")){
			System.out.println("USER FROM "+ctx.channel().localAddress()+ " HAS LOGGED IN WITH USERNAME "+info[0]);
			ctx.writeAndFlush(Unpooled.copyInt(1)); //Send the client valide login code
			
			ctx.pipeline().removeLast(); //Remove the login handler
			ctx.pipeline().addLast("server handler",new ServerHandler(info[0]));
			ctx.pipeline().addLast("idleStateHandler", new IdleStateHandler(0,150,0,TimeUnit.MILLISECONDS));
			ctx.pipeline().addLast("server updater",new ServerUpdater());
		}else{ //If the login fails
			ctx.writeAndFlush(Unpooled.copyInt(0)); //Send the client an error code
			ctx.channel().close();
		}
		
		b.release();
	}
}
