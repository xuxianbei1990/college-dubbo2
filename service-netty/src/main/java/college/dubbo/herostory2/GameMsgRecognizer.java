package college.dubbo.herostory2;

import college.dubbo.herostory.msg.GameMsgProtocol;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2020/5/2
 * Time: 16:04
 * Version:V1.0
 */
@Slf4j
public class GameMsgRecognizer {

    /**
     * 消息代码和消息体字典
     */
    static private final Map<Integer, GeneratedMessageV3> _msgCodeAndMsgBodyMap = new HashMap<>();

    /**
     * 消息类型和消息编号字典
     */
    static private final Map<Class<?>, Integer> _msgClazzAndMsgCodeMap = new HashMap<>();

    /**
     * 私有化类默认构造器
     */
    private GameMsgRecognizer() {
    }

    public static void init() {
        //以下代码之前的版本是map里面放了很多类
        //拿到GameMsgProtocol所有声明的类
        Class<?>[] innerClazzArray = GameMsgProtocol.class.getDeclaredClasses();
        for (Class<?> innerClazz : innerClazzArray) {
            if (!GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
                continue;
            }
            String clazzName = innerClazz.getSimpleName();
            clazzName = clazzName.toLowerCase();
            for (GameMsgProtocol.MsgCode msgCode : GameMsgProtocol.MsgCode.values()) {
                String strMsgCode = msgCode.name();
                strMsgCode = strMsgCode.replaceAll("_", "");
                strMsgCode = strMsgCode.toLowerCase();
                if (!strMsgCode.startsWith(clazzName)) {
                    continue;
                }
                try {
                    Object returnObj = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);
                    log.info("{} <==> {}", innerClazz.getName(), msgCode.getNumber());

                    _msgCodeAndMsgBodyMap.put(
                            msgCode.getNumber(),
                            (GeneratedMessageV3) returnObj
                    );

                    _msgClazzAndMsgCodeMap.put(
                            innerClazz,
                            msgCode.getNumber()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Message.Builder getBuilderByMsgCode(int msgCode) {
        if (msgCode < 0) {
            return null;
        }

        GeneratedMessageV3 msg = _msgCodeAndMsgBodyMap.get(msgCode);
        if (null == msg) {
            return null;
        }

        return msg.newBuilderForType();
    }

    public static int getMsgCodeByMsgClazz(Class<?> msgClazz) {
        if (null == msgClazz) {
            return -1;
        }

        Integer msgCode = _msgClazzAndMsgCodeMap.get(msgClazz);
        if (null != msgCode) {
            return msgCode.intValue();
        } else {
            return -1;
        }
    }
}
