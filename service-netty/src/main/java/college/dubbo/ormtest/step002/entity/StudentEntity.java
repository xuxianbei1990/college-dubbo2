package college.dubbo.ormtest.step002.entity;

/**
 * @author: xuxianbei
 * Date: 2020/5/2
 * Time: 21:12
 * Version:V1.0
 */
public class StudentEntity {
    /**
     * 用户 Id
     */
    @Column(name = "id")
    public Integer _userId;

    /**
     * 用户名
     */
    @Column(name = "name")
    public String _userName;

    /**
     * 密码
     */
    @Column(name = "password")
    public String _password;
}
