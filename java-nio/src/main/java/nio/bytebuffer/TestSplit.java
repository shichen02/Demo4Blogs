package nio.bytebuffer;


import static nio.util.ByteBufferUtil.debugAll;

import java.nio.ByteBuffer;

/**
 * 测试粘包，半包的处理
 *
 * @author tangsc
 * @date 2022/09/26
 */
public class TestSplit {

    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        //                     11            24
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);

        source.put("w are you?\nhaha!\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer source) {
        source.flip();
        int oldLimit = source.limit();
        for (int i = 0; i < oldLimit; i++) {
            if (source.get(i) == '\n') {
                System.out.println(i);
                ByteBuffer target = ByteBuffer.allocate(i + 1 - source.position());
                // 0 ~ limit
                source.limit(i + 1);
                // 从source 读，向 target 写
                target.put(source);
                debugAll(target);
                source.limit(oldLimit);
            }
        }
        source.compact();
    }



}
