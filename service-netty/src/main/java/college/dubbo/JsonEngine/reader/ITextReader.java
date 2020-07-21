package college.dubbo.JsonEngine.reader;

import college.dubbo.JsonEngine.lifecycle.ILifeCycle;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/20
 * VersionV1.0
 * @description
 */
public interface ITextReader extends ILifeCycle {

    /**
     * @return json 字符串
     */
    String readline() throws Exception;

}
