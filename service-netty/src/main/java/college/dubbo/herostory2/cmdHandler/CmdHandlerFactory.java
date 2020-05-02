package college.dubbo.herostory2.cmdHandler;

import college.dubbo.herostory2.util.PackageUtil;
import com.google.protobuf.GeneratedMessageV3;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: xuxianbei
 * Date: 2020/4/30
 * Time: 20:48
 * Version:V1.0
 */
@Slf4j
public final class CmdHandlerFactory {

    /**
     * 处理器字典
     */
    static private Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> _handlerMap = new HashMap<>();

    /**
     * 私有化类默认构造器
     */
    private CmdHandlerFactory() {
    }

    /**
     * 重构一般是if else 改为 map 或者 for
     */
    static public void init() {
//        _handlerMap.put(GameMsgProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
//        _handlerMap.put(GameMsgProtocol.WhoElseIsHeroCmd.class, new WhoElseIsHereCmdHandler());
//        _handlerMap.put(GameMsgProtocol.UserMoveToCmd.class, new UserMoveToCmdHandler());
        log.info("==== 完成 Cmd 和 Handler 的关联 ====");
        // 获取包名称
        final String packageName = CmdHandlerFactory.class.getPackage().getName();
        // 获取所有的 ICmdHandler 子类
        Set<Class<?>> clazzSet = PackageUtil.listSubClazz(
                packageName,
                true,
                ICmdHandler.class
        );

        for (Class<?> clazz : clazzSet) {
            if ((clazz.getModifiers() & Modifier.ABSTRACT) != 0) {
                // 如果是抽象类,
                continue;
            }

            // 获取方法数组
            Method[] methodArray = clazz.getDeclaredMethods();
            // 消息类型
            Class<?> msgType = null;

            for (Method currMethod : methodArray) {
                if (!currMethod.getName().equals("handle")) {
                    // 如果不是 handle 方法,
                    continue;
                }

                // 获取函数参数类型
                Class<?>[] paramTypeArray = currMethod.getParameterTypes();

                if (paramTypeArray.length != 2 ||
                        paramTypeArray[1] == GeneratedMessageV3.class || // 这里最好加上这个判断
                        !GeneratedMessageV3.class.isAssignableFrom(paramTypeArray[1])) {
                    continue;
                }
                msgType = paramTypeArray[1];
                break;
            }

            if (null == msgType) {
                continue;
            }

            try {
                // 创建指令处理器
                ICmdHandler<?> newHandler = (ICmdHandler<?>) clazz.newInstance();

                log.info(
                        "关联 {} <==> {}",
                        msgType.getName(),
                        clazz.getName()
                );

                _handlerMap.put(msgType, newHandler);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }
        /**
         * 创建指令处理器工厂
         *
         * @param msgClazz 消息类
         * @return
         */
        static public ICmdHandler<? extends GeneratedMessageV3> create (Class < ? > msgClazz){
            if (null == msgClazz) {
                return null;
            }

            return _handlerMap.get(msgClazz);
        }
    }
