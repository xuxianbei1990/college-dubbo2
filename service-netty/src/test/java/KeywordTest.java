import college.dubbo.JsonEngine.KeywordEngine;
import college.dubbo.JsonEngine.reader.BufferedTextReader;
import college.dubbo.JsonEngine.reader.FastTextReader;
import org.junit.Test;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/20
 * VersionV1.0
 * @description
 */
public class KeywordTest {

    private String path = "E:\\Java\\GitHub\\college-dubbo2\\service-netty\\sojson.com.json";

    @Test
    public void testTextReader() {
        BufferedTextReader bufferedTextReader;
        try {
            bufferedTextReader = new BufferedTextReader(path);
            bufferedTextReader.init();
            String line;
            while ((line = bufferedTextReader.readline()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFastReader() {
        FastTextReader fastTextReader;
        try {
            fastTextReader = new FastTextReader(path);
            fastTextReader.init();
            String line;
            while ((line = fastTextReader.readline()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEngine() throws Exception {
        final KeywordEngine keywordEngine = new KeywordEngine();
        keywordEngine.init();
        keywordEngine.stop();
    }

}
