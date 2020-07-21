package college.dubbo.JsonEngine;

import college.dubbo.JsonEngine.lifecycle.AbstractLifeCycle;
import college.dubbo.JsonEngine.parser.FastJSONParser;
import college.dubbo.JsonEngine.parser.IJSONParser;
import college.dubbo.JsonEngine.reader.BufferedTextReader;
import college.dubbo.JsonEngine.reader.ITextReader;
import college.dubbo.JsonEngine.store.DefaultKeywordStore;
import college.dubbo.JsonEngine.store.IKeywordStore;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/21
 * VersionV1.0
 * @description
 */
public class KeywordEngine extends AbstractLifeCycle {
    private final String path = "E:\\Java\\GitHub\\college-dubbo2\\service-netty\\sojson.com.json";
    private ITextReader reader = new BufferedTextReader(path);
    private IJSONParser parser = new FastJSONParser();
    //    private IKeywordStore store = new DefaultKeywordStore();
    private int cpuNum = Runtime.getRuntime().availableProcessors();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(cpuNum, cpuNum, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(200), (r) -> new Thread(r, "KeywordEngine"), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 无锁两种方案：
     * 1.使用cas，牺牲cpu
     * 2.充分使用多线程，ThreadLocal 牺牲空间
     */
    private ThreadLocal<IKeywordStore> keywordStoreThreadLocal = new ThreadLocal<IKeywordStore>() {
        @Override
        protected IKeywordStore initialValue() {
            return new DefaultKeywordStore();
        }
    };

    @Override
    public void init() throws Exception {
        super.init();
        reader.init();
        final StopWatch stopWatch = new StopWatch();
        String line;
        stopWatch.start();
        while ((line = reader.readline()) != null) {
            final String str = line;
            threadPoolExecutor.execute(() -> {
                try {
                    final IKeywordStore store = keywordStoreThreadLocal.get();
                    //优化细节：循环套try catch 实际性能较差
                    parser.parse(str).forEach(spuBean -> {
                        try {
                            store.putKeyword(spuBean);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
        stopWatch.watch();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        reader.stop();
    }
}
