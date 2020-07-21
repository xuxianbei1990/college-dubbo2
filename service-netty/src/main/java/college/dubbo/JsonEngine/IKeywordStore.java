package college.dubbo.JsonEngine;

import college.dubbo.JsonEngine.beans.ConditionBean;
import college.dubbo.JsonEngine.beans.SpuBean;

import java.util.List;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/19
 * VersionV1.0
 * @description
 */
public interface IKeywordStore {

    /**
     * 添加spu实体
     *
     * @param spuBean
     */
    void add(SpuBean spuBean);

    /**
     * @param keyword
     * @return spuIdList
     */
    List<String> get(String keyword, List<ConditionBean> conditionBeans);
}
