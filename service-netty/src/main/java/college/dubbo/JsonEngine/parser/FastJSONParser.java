package college.dubbo.JsonEngine.parser;

import college.dubbo.JsonEngine.beans.SpuBean;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static college.dubbo.JsonEngine.constant.KeyWordConstants.*;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/20
 * VersionV1.0
 * @description
 */
public class FastJSONParser implements IJSONParser {

    @Override
    public List<SpuBean> parse(String line) throws Exception {
        Objects.requireNonNull(line, "json 字符串不能为空");
        List<SpuBean> spuBeans = new ArrayList<>(32);
        JSONObject jsonObject = JSONObject.parseObject(line);
        jsonObject.entrySet().forEach(entrySet -> {
            final String spuId = entrySet.getKey();
            final JSONObject spuIdAttr = (JSONObject) entrySet.getValue();
            final JSONObject kwScorePosJO = spuIdAttr.getJSONObject(KW_SCORE_POS_NAME);
            kwScorePosJO.entrySet().parallelStream().forEach(kwScorePosEntrySet -> {
                final String key = kwScorePosEntrySet.getKey();
                final JSONObject JO = (JSONObject) kwScorePosEntrySet.getValue();
                SpuBean spuBean = new SpuBean(spuId, key,
                        JO.getBigDecimal(OPT_TYPE_NAME),
                        JO.getBigDecimal(POS_NAME),
                        JO.getBigDecimal(SCORE_NAME));
//                System.out.println(spuBean);
                spuBeans.add(spuBean);
            });
        });
        return spuBeans;
    }
}
