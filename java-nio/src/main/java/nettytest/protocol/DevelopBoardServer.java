package nettytest.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import nettytest.TimeServerHandler;
import nettytest.handler.MessageHandler;

/**
 * 开发板服务器
 *
 * @author tangsc
 * @date 2023/02/13
 */
public class DevelopBoardServer {

    private Integer port;

    public DevelopBoardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // NioEventLoopGroup 是一个处理 IO 操作的多线程事件循环。Netty 中的 EventLoopGroup 为不同类型的 transport 提供不同的实现
        // 这个例子实现的服务侧的应用程序，所以将会有两个 NioEventLoopGroup 被使用
        // 第一个通常被称为 boss accepts an incoming connection
        // 第二个通常被称为 worker handlers the traffic of the accepted connection
        //  once the boss accepts the connection and registers the accepted connection to the worker
        // how many threads are used and how they are mapped to created channels depends on the EventLoopGroup implementation and may be even configuration via a constructor
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(2);

        try {
            // ServerBootStrap 是一个设置服务器的辅助类，你也可以直接通过一个 channel 直接设置需要的参数
            // 但是请不要这样做，那将会是一个乏味的过程，在大多数场景下你都不需要这样做
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    // 这里我们使用指定的 NioServerSocketChannel which is used to instantiate Channel to accept incoming connections
                    .channel(NioServerSocketChannel.class)
                    // 此处指定的 handler will always be evaluated by newly accepted channel.
                    // ChannelInitializer 是一个特殊的处理器 that purposed to help a user configure a new Channel.
                    // It is most likely that you want to configure the ChannelPipeline of the new Channel by adding some handlers such as DiscardServerHandler to implement your network application.
                    //  As the application gets complicated, it is likely that you will add more handlers to the pipeline and extract this anonymous class into a top-level class eventually.
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
//                                    .addLast(new LengthFieldBasedFrameDecoder(65535, 1, 2, 1, 4))
                                    .addLast(new ByteToMessageCoder())
                                    .addLast(new MessageHandler());
//                            ch.pipeline().addLast(new JsonObjectDecoder()).addLast(new MessageHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // bind and start to accept incoming connections
            ChannelFuture f = b.bind(port).sync();

            // what until the server socket is closed
            // In this example , this dose not happen
            // but you can do that to gracefully shut down your server
            f.channel().closeFuture().sync();


        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }


    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new DevelopBoardServer(port).run();
    }

}
