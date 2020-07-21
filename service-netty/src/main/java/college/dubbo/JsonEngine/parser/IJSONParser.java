package college.dubbo.JsonEngine.parser;

import college.dubbo.JsonEngine.beans.SpuBean;

import java.util.List;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/20
 * VersionV1.0
 * @description
 */
public interface IJSONParser {
    List<SpuBean> parse(String line) throws Exception;
}
