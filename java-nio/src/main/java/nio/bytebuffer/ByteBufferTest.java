package nio.bytebuffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufferTest {

    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile(
            "E:\\code\\Demo4Blogs\\java-nio\\src\\main\\resources\\data\\helloworld.txt", "rw")) {
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(5);
            // 向 buffer 中写入
            for (int len = 0; len != -1; len = channel.read(buffer)) {

                buffer.flip();
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    log.debug("char: {} :{}", (char) b, b);
                }
                // 清空
                buffer.clear();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
