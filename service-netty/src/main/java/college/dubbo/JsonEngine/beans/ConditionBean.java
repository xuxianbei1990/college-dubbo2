package college.dubbo.JsonEngine.beans;

import lombok.Data;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/19
 * VersionV1.0
 * @description
 */
@Data
public class ConditionBean {
    private String fieldName;
    private Condition condition;
    private String value;
    private LogicCondition logicCondition;

    public enum Condition {
        EQ, LT, MT;
    }

    public enum LogicCondition {
        AND, OR
    }
}
