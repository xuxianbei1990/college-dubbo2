package college.dubbo.ormtest.step002;

import college.dubbo.ormtest.step001.entity.StudentEntity;
import college.dubbo.ormtest.step002.entity.AbstractEntityHelper;
import college.dubbo.ormtest.step002.entity.EntityHelperFactory;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author: xuxianbei
 * Date: 2020/5/2
 * Time: 21:12
 * Version:V1.0
 */
public class App000 {

    @SneakyThrows
    public static void main(String[] args) {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String dbconnStr = "jdbc:mysql://localhost:3306/college?allowMu" +
                "ltiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=f" +
                "alse&serverTimezone=GMT%2B8&user=root&password=1qaz!QAZ";
        Connection connection = DriverManager.getConnection(dbconnStr);
        Statement stmt = connection.createStatement();
        String sql = "select * from student limit 200000";
        // 执行查询
        ResultSet rs = stmt.executeQuery(sql);
        // 创建助手类, 这里采用全新设计的工厂类!
        AbstractEntityHelper helper = EntityHelperFactory.getEntityHelper(StudentEntity.class);

        // 获取开始时间
        long t0 = System.currentTimeMillis();

        while (rs.next()) {
            // 创建新的实体对象
            StudentEntity ue = (StudentEntity) helper.create(rs);
        }

        // 获取结束时间
        long t1 = System.currentTimeMillis();

        // 关闭数据库连接
        stmt.close();
        connection.close();

        // 打印实例化花费时间
        System.out.println("实例化花费时间 = " + (t1 - t0) + "ms");
    }
}
