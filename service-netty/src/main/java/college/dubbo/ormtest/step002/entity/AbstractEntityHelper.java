package college.dubbo.ormtest.step002.entity;

import java.sql.ResultSet;

/**
 *
 * 如果还要进一步优化 ASM框架，动态编码java技术
 * 抽象的实体助手
 */
public abstract class AbstractEntityHelper {
    /**
     * 将数据集转换为实体对象
     *
     * @param rs 数据集
     * @return
     *
     */
    public abstract Object create(ResultSet rs);
}
