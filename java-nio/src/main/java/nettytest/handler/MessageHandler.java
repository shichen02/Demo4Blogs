package nettytest.handler;

import cn.hutool.json.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import nettytest.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class MessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println(msg);

        // 处理业务逻辑
//        if (!"heartBeat".equals(msg)){
//            return;
//        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("method", "tts");

        HashMap<String, Object> content = new HashMap<>();
        content.put("cfg_tts","123456");

        map.put("data",content);

        JSONObject json = new JSONObject();
        json.putAll(map);

        byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);

        ByteBuf buffer = ctx.alloc().buffer(bytes.length);
        buffer.writeBytes(bytes);
        Message message = new Message();
        byte a = 0x12;
        message.setModel(a);
        message.setValue(a);
        ctx.channel().write(message);
//        ChannelFuture f = ctx.write(buffer);
//        f.addListener(ChannelFutureListener.CLOSE);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
