package nettytest.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.ReferenceCountUtil;
import nettytest.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 字节消息编码器
 *
 * @author tangsc
 * @date 2023/02/13
 */
public class ByteToMessageCoder extends ByteToMessageCodec<Message> {


    private static final short DIVISION = 0xFE;

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 一个字节 head 0XFE
        out.writeByte(0XFE);
        // 两个字节 len
        out.writeBytes(new byte[]{0X00, 0X00});
        // 一个字节 mode
        out.writeByte(msg.getModel());
        // 一个 个字节 data
        out.writeByte(msg.getValue());
        // 一个字节 end 0XFE
        out.writeByte(0XFE);

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("解码");
        while (in.isReadable()){
            in.readByte();
        }
        out.add("hello");
//        while (true) {
//            if (in.readableBytes() < 5) {
//                break;
//            }
//            in.markReaderIndex();
//            // 一个字节 head 0XFE
//            short head = in.readUnsignedByte();
//            if (DIVISION == head){
//                // 两个字节 len
//                int len = in.readUnsignedShort() + 1;
//                // 一个字节 mode
//                short mode = in.readUnsignedByte();
//                if (in.readableBytes() < len) {
//                    in.resetReaderIndex();
//                    break;
//                }
//
//                String body = in.readCharSequence(len - 1, StandardCharsets.UTF_8).toString();
//                out.add(body);
//
//                // 最后一个字节 end 0XFE
//                short i = in.readUnsignedByte();
//            }
//
//        }

    }

}
