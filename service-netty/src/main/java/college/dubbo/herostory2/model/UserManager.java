package college.dubbo.herostory2.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2020/4/30
 * Time: 20:42
 * Version:V1.0
 */
public class UserManager {

    /**
     * 用户字典  为什么这里这么用，也没啥问题？因为业务上，一个用户不会同时存在登录和登出。
     * 1.8 hashmap 并发问题：同样的key 设置时候可能会丢失； size问题。
     */
    private static final Map<Integer, User> userMap = new HashMap<>();

    private UserManager() {
    }

    /**
     * 添加用户
     *
     * @param newUser
     */
    static public void addUser(User newUser) {
        if (null != newUser) {
            userMap.put(newUser.getUserId(), newUser);
        }
    }

    /**
     * 根据用户 Id 移除用户
     *
     * @param userId
     */
    public static void removeUserById(int userId) {
        userMap.remove(userId);
    }

    /**
     * 列表用户
     *
     * @return
     */
    static public Collection<User> listUser() {
        return userMap.values();
    }
}
