package college.dubbo.JsonEngine.store;

import college.dubbo.JsonEngine.beans.ConditionBean;
import college.dubbo.JsonEngine.beans.SpuBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/21
 * VersionV1.0
 * @description
 */
public class DefaultKeywordStore implements IKeywordStore {
    private final Map<String, List<SpuBean>> spuMap = new HashMap<>();

    @Override
    public List<String> getSpuIdListByKeyword(String keyword) throws Exception {
        return null;
    }

    @Override
    public List<String> getSpuIdListByKeywordAndCondition(String keyword, List<ConditionBean> conditionBeanList) throws Exception {
        return null;
    }

    @Override
    public void putKeyword(SpuBean spuBean) throws Exception {
        List<SpuBean> spuBeans = spuMap.get(spuBean.getKeyword());
        if (spuBeans == null) {
            spuBeans = new ArrayList<>();
            spuMap.put(spuBean.getKeyword(), spuBeans);
        }
        spuBeans.add(spuBean);
    }
}
