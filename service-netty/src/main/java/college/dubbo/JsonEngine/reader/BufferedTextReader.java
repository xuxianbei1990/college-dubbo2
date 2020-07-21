package college.dubbo.JsonEngine.reader;

import college.dubbo.JsonEngine.lifecycle.AbstractLifeCycle;

import java.io.*;
import java.util.Objects;

/**
 * Name
 *  磁盘---操作系统---JVM进程内存----堆内存
 * @author xxb
 * Date 2020/7/20
 * VersionV1.0
 * @description
 */
public class BufferedTextReader extends AbstractLifeCycle implements ITextReader, Closeable {

    private String path;

    private BufferedReader bufferedReader;

    public BufferedTextReader(String path) {
        this.path = path;
    }

    @Override
    public void init() throws Exception {
        super.init();
        /**
         * 使用bufferReader 读取是去jvm中拷贝的，增加读的内存大小可以增加读的速度
         * 但是会额外增加GC开销
         */
        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)), 10240);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        path = null;
        if (Objects.nonNull(bufferedReader)) {
            bufferedReader.close();
        }
    }

    @Override
    public String readline() throws Exception {
        return bufferedReader.readLine();
    }

    @Override
    public void close(){
        try{
            stop();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
