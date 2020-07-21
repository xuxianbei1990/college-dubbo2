package college.dubbo.JsonEngine.lifecycle;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/20
 * VersionV1.0
 * @description
 */
public interface ILifeCycle {

    /**
     * 初始化
     */
    void init() throws Exception;

    /**
     * 启动
     */
    void start() throws Exception;

    /**
     * 关闭
     */
    void stop() throws Exception;
}
