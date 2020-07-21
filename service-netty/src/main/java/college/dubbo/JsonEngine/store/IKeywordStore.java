package college.dubbo.JsonEngine.store;

import college.dubbo.JsonEngine.beans.ConditionBean;
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
public interface IKeywordStore {

    /**
     *
     * @param keyword
     * @return  spuId List
     * @throws Exception
     */
    List<String> getSpuIdListByKeyword(String keyword) throws Exception;


    List<String> getSpuIdListByKeywordAndCondition(String keyword, List<ConditionBean> conditionBeanList) throws Exception;

    void putKeyword(SpuBean spuBean) throws Exception;
}
