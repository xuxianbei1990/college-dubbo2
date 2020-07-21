package college.dubbo.JsonEngine.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/19
 * VersionV1.0
 * @description
 */
@Data
@AllArgsConstructor
public class SpuBean {
    private String spuId;
    private String keyword;
    private BigDecimal optType;
    private BigDecimal pos;
    private BigDecimal score;

}
