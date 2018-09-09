import com.zwt.framework.redis.util.RedisSingleUtil;

/**
 * @author zwt
 * @detail
 * @date 2018/9/4
 * @since 1.0
 */
public class RedisTest {
    private static RedisSingleUtil redisSingleUtil=new RedisSingleUtil();

    public static void main(String[] args) {
        redisSingleUtil.setString("str","123");
        redisSingleUtil.getString("str");
    }
}
