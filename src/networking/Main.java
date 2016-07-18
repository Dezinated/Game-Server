package networking;
import game.GameLoop;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Main {
	static final String HOST = System.getProperty("host", "108.168.30.206");
	static final int PORT = Integer.parseInt(System.getProperty("port", "42042"));

	public static void main(String[] args) throws Exception {
		
		Thread gameLoop = new Thread(new GameLoop());
		gameLoop.start();
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup,workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.option(ChannelOption.TCP_NODELAY, true)
			//.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();

					//p.addLast(new LoggingHandler(LogLevel.INFO));
					p.addLast("login handler",new LoginHandler());
				}
			});

			// Start the server.
			ChannelFuture f = bootstrap.bind(PORT).sync();

			// Wait until the server socket is closed.
			f.channel().closeFuture().sync();
			gameLoop.interrupt();
		} finally {
			// Shut down all event loops to terminate all threads.
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}