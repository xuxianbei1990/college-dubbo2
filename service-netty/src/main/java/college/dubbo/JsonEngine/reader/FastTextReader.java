package college.dubbo.JsonEngine.reader;

import college.dubbo.JsonEngine.lifecycle.AbstractLifeCycle;
import sun.misc.Cleaner;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 *
 * 磁盘-----进程内存------
 * Name
 *
 * @author xxb
 * Date 2020/7/20
 * VersionV1.0
 * @description
 */
public class FastTextReader extends AbstractLifeCycle implements ITextReader {
    private String path;
    private FileChannel fileChannel;
    private MappedByteBuffer mappedByteBuffer;
    private Method cleanMethod;
    private byte nextSymbol = '\n';

    public FastTextReader(String path) {
        this.path = path;
    }

    @Override
    public String readline() throws Exception {
        int startPos = mappedByteBuffer.position();
        for (int i = startPos; i < mappedByteBuffer.capacity(); i++) {
            final byte b = mappedByteBuffer.get();
            if (b == nextSymbol) {
                break;
            }
        }
        if (startPos != mappedByteBuffer.position()) {
            byte[] bytes = new byte[mappedByteBuffer.position() - startPos];
            mappedByteBuffer.position(startPos);
            mappedByteBuffer.get(bytes);
            return new String(bytes);
        }
        return null;
    }

    @Override
    public void init() throws Exception {
        super.init();
        fileChannel = FileChannel.open(new File(path).toPath(), StandardOpenOption.READ);
        mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        System.out.println(String.format("file size %d", fileChannel.size()));
        cleanMethod = Cleaner.class.getDeclaredMethod("clean");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (mappedByteBuffer != null) {
            cleanMethod.invoke(mappedByteBuffer);
        }
        if (fileChannel != null) {
            fileChannel.close();
        }
    }
}
