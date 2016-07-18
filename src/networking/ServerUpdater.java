package networking;
import game.GameManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;


public class ServerUpdater extends ChannelDuplexHandler {
	
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
            	//System.out.println("Update player "+ctx.channel().id());
            	//GameManager.channels.write(Unpooled.copyShort(0));
            	//System.out.println(Unpooled.copyFloat((GameManager.players.get(ctx.channel().id()).getLocation().x)));
            	//GameManager.channels.write(Unpooled.copyFloat((GameManager.players.get(ctx.channel().id()).getLocation().x)));
            	//GameManager.channels.write(Unpooled.copyFloat((GameManager.players.get(ctx.channel().id()).getLocation().y)));
            	//GameManager.channels.write(Unpooled.copiedBuffer(GameManager.players.get(ctx.channel().id()).getUsername()+"\n", CharsetUtil.UTF_8));
            	//GameManager.channels.flush();
            }
        }
    }
	
}
