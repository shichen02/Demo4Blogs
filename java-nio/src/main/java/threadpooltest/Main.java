package threadpooltest;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {


    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            Runnable job = new Runnable() {
                @Override
                public void run() {
                    // 模拟任务执行...
                    Long v = RandomUtil.randomLong(500, 1000);
                    log.info("正在执行任务...");
                    try {
                        Thread.sleep(v);
                    } catch (InterruptedException e) {
                        log.error("发生异常了", e);
                    }
                    log.info("执行任务完成...");
                }
            };
            ThreadPoolManager.getInstance().scheduleAtFix(job,
                    StrUtil.format("this is job {} ", i),
                    1, 5, TimeUnit.SECONDS);
        }
        while (true) {
        }

    }
}
