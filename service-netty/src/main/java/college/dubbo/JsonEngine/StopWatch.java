package college.dubbo.JsonEngine;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/21
 * VersionV1.0
 * @description
 */
public class StopWatch {

    private long start;
    private long end;

    public void start() {
        start = System.currentTimeMillis();
    }

    public void end() {
        end = System.currentTimeMillis();
    }

    public void watch() {
        end();
        System.out.println((end - start) + "ms");
    }
}
