package com.zwt.framework.redis.util;

import com.zwt.framework.redis.factory.JedisFactory;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zwt
 * @detail Redis基本操作工具类
 * @date 2018/9/3
 * @since 1.0
 */
public interface RedisUtil {
    /**
     * 获取 jedisFactory.
     *
     * @return jedisFactory
     */
    public JedisFactory getJedisFactory();
    /**
     * 设置一个key的过期时间（单位：秒） 在 Redis 中，带有生存时间的 key 被称为『可挥发』(volatile)的。
     *
     * @param key
     *            key值
     * @param seconds
     *            生命周期，多少秒后过期
     * @return 1：设置成功 0：已经超时或key不存在 /不能设置过期时间
     */
    public abstract long expire(String key, int seconds);

    /**
     * 设置一个key在某个时间点过期
     *
     * @param key
     *            key值
     * @param unixTimestamp
     *            unix时间戳，从1970-01-01 00:00:00开始到现在的秒数
     * @return 1：设置了过期时间 0：没有设置过期时间/不能设置过期时间
     */
    public abstract long expireAt(String key, int unixTimestamp);

    /**
     * 删除
     *
     * @param key
     *            匹配的key
     * @return 删除成功的条数
     */
    public abstract Long delKey(String key);

    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key
     *            匹配的key
     * @return 删除成功的条数
     */
    public abstract Long hdel(String key, String... fields);

    /**
     * 批量删除
     *
     * @param keys
     *            匹配的key的集合
     * @return 删除成功的条数
     */
    public abstract Long delKeys(String[] keys);

    /**
     * 返回哈希表 key 中的所有域。
     *
     * @param key
     *            匹配的key
     * @return 删除成功的条数
     */
    public abstract Set<String> hkeys(String key);

    /**
     * 自增长ID,递增1 一个跨jvm的id生成器，利用了redis原子性操作的特点
     *
     * @param key
     *            id的key
     * @return 返回生成的Id
     */
    public abstract long incr(String key);

    /**
     * 递增指定value
     *
     * @param key
     * @param value
     * @return 返回递增后的值
     */
    public abstract long incrby(String key, long value);

    /**
     * 递减1 利用了redis原子性操作的特点
     *
     * @param key
     *            id的key
     * @return 返回生成的Id
     */
    public abstract long decr(String key);

    /**
     * 递减指定value
     *
     * @param key
     * @param value
     * @return 返回递增后的值
     */
    public abstract long decrby(String key, long value);




    /**
     * 递增指定value
     *
     * @param key
     * @param value
     * @return 返回递增后的值
     */
    public abstract double incrFloatby(String key, double value);



    /**
     * 判断key是否存在
     *
     * @param key
     * @return true:存在，false:不存在
     */
    public abstract Boolean exists(String key);

    /**
     * 将字符串值 value 关联到 key 。 如果 key 已经持有其他值， setString 就覆写旧值，无视类型。
     * 对于某个原本带有生存时间（TTL）的键来说， 当 setString 成功在这个键上执行时， 这个键原有的 TTL 将被清除。
     * 时间复杂度：O(1)
     *
     * @param key
     *            key
     * @param value
     *            string value
     * @return 在设置操作成功完成时，才返回 OK 。
     */
    public abstract String setString(String key, String value);

    /**
     * 将值 value 关联到 key ，并将 key 的生存时间设为 expire (以秒为单位)。 如果 key 已经存在， 将覆写旧值。
     * 类似于以下两个命令: SET key value EXPIRE key expire # 设置生存时间
     * 不同之处是这个方法是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，在 Redis 用作缓存时，非常实用。
     * 时间复杂度：O(1)
     *
     * @param key
     *            key
     * @param value
     *            string value
     * @param expire
     *            生命周期
     * @return 设置成功时返回 OK 。当 expire 参数不合法时，返回一个错误。
     */
    public abstract String setString(String key, String value, int expire);

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。若给定的 key 已经存在，则 setStringIfNotExists
     * 不做任何动作。 时间复杂度：O(1)
     *
     * @param key
     *            key
     * @param value
     *            string value
     * @return 设置成功，返回 1 。设置失败，返回 0 。
     */
    public abstract Long setStringIfNotExists(String key, String value);

    /**
     * 返回 key 所关联的字符串值。如果 key 不存在那么返回null 。 假如 key 储存的值不是字符串类型，返回一个错误，因为
     * getString 只能用于处理字符串值。 时间复杂度: O(1)
     *
     * @param key
     *            key
     * @return 当 key 不存在时，返回 null ，否则，返回 key 的值。如果 key 不是字符串类型，那么返回一个错误。
     */
    public abstract String getString(String key);

    /**
     * 返回 key 所关联的数字值。如果 key 不存在或非数字时返回0 。 时间复杂度: O(1)
     *
     * @param key
     *            key
     * @return 当 key 不存在或非数字时，返回 0 ，否则，返回 key 的long型值。
     */
    public abstract long getLong(String key);

    /**
     * 返回 key 所关联的字符串值。如果 key 不存在那么返回null 。 假如 key 储存的值不是字符串类型，返回一个错误，因为
     * getString 只能用于处理字符串值。 时间复杂度: O(1)
     *
     * @param key
     *            key
     * @param defaultValue
     *            默认值
     * @return 当 key 不存在时，返回默认值 ，否则，返回 key 的值。如果 key 不是字符串类型，那么返回一个错误。
     */
    public abstract String getString(String key, String defaultValue);

    /**
     * 批量的 {@link #setString(String, String)} <br>
     * <font color=red>注意：由于需要对每个key进行计算slots，所以cluster暂时还无法使用pipeline方式</font>
     *
     * @param keys
     *            key数组
     * @param values
     *            values数组
     * @return 操作状态的集合
     */
    public abstract List<String> batchSetString(String[] keys, String[] values);

    /**
     * 批量的 {@link #getString(String)} <br>
     * <font color=red>注意：由于需要对每个key进行计算slots，所以cluster暂时还无法使用pipeline方式</font>
     *
     * @param keys
     *            key数组
     * @return value的集合
     */
    public abstract List<String> batchGetString(String[] keys);

    /**
     * 将哈希表 key 中的域 field 的值设为 value 。 如果 key 不存在，一个新的哈希表被创建并进行 hashSet 操作。 如果域
     * field 已经存在于哈希表中，旧值将被覆盖。 时间复杂度: O(1)
     *
     * @param key
     *            key
     * @param field
     *            域
     * @param value
     *            string value
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回
     *         0 。
     */
    public abstract Long hashSet(String key, String field, String value);

    /**
     * 返回哈希表 key 中给定域 field 的值。 时间复杂度:O(1)
     *
     * @param key
     *            key
     * @param field
     *            域
     * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 null 。
     */
    public abstract String hashGet(String key, String field);

    /**
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。 时间复杂度: O(N) (N为fields的数量)
     *
     * @param key
     *            key
     * @param hash
     *            field-value的map
     * @return 如果命令执行成功，返回 OK 。当 key 不是哈希表(hash)类型时，返回一个错误。
     */
    public abstract String hashMultipleSet(String key, Map<String, String> hash);

    /**
     * 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回一个 null 值。 时间复杂度: O(N)
     * (N为fields的数量)
     *
     * @param key
     *            key
     * @param fields
     *            field的数组
     * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
     */
    public abstract List<String> hashMultipleGet(String key, String... fields);

    /**
     * 返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field
     * name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。 时间复杂度: O(N)
     *
     * @param key
     *            key
     * @return 以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。
     */
    public abstract Map<String, String> hashGetAll(String key);

    /**
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     *
     * @param key
     *            key
     * @param values
     *            value的数组
     * @return 执行 listPushTail 操作后，表的长度
     */
    public abstract Long listPushTail(String key, String... values);

    /**
     * 将一个或多个值 value 插入到列表 key 的表头 (最左边)
     *
     * @param key
     *            key
     * @param values
     *            value的数组
     * @return 执行 listPushHead 命令后，列表的长度。
     */
    public abstract Long listPushHead(String key, String... values);

    /**
     * 返回list所有元素，key不存在返回空列表
     *
     * @param key
     *            key
     * @return list所有元素
     */
    public abstract List<String> listGetAll(String key);

    /**
     * 返回指定区间内的元素，下标从0开始，负值表示从后面计算，-1表示倒数第一个元素，key不存在返回空列表
     *
     * @param key
     *            key
     * @param beginIndex
     *            下标开始索引
     * @param endIndex
     *            下标结束索引
     * @return 指定区间内的元素
     */
    public abstract List<String> listRange(String key, long beginIndex, long endIndex);

    /**
     * 移除并返回列表 key 的头元素
     *
     * @param key
     *            key
     * @return 返回列表 key 的头元素
     */
    public abstract String listPop(String key);

    /**
     *
     * <br>
     * 描 述：返回列表 key 的长度。 <br>
     * 作 者：hy <br>
     * 历 史: (版本) 作者 时间 注释
     *
     * @param key
     * @return
     */
    public abstract long listLen(String key);

    /**
     * 一次获得多个链表的数据
     *
     * @param keys
     *            key的数组
     * @return 执行结果
     */
    public abstract Map<String, List<String>> batchGetAllList(String... keys);

    /**
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。 对象序列化 时间复杂度: O(N) (N为fields的数量)
     *
     * @param key
     *            key
     * @param hash
     *            field-value的map
     * @return 如果命令执行成功，返回 OK 。当 key 不是哈希表(hash)类型时，返回一个错误。
     */
    public abstract String hashBinarySet(byte[] key, Map<byte[], byte[]> hash);

    /**
     * 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回 null 值。 时间复杂度: O(N)
     * (N为fields的数量)
     *
     * @param key
     *            key
     * @param fields
     *            field的数组
     * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
     */
    public abstract List<byte[]> hashBinaryGet(byte[] key, byte[]... fields);

    /**
     * 添加元素到有序集中
     *
     * @param key
     * @param score
     * @param member
     */
    public abstract void zadd(String key, double score, String member);

    /**
     * 添加多个元素到有序集中
     *
     * @param key
     * @param scoreMembers
     */
    public abstract void zadd(String key, Map<String, Double> scoreMembers);

    /**
     * 返回有序集 key 的基数
     *
     * @param key
     * @return
     */
    public abstract long zcard(String key);

    /**
     * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public abstract long zcount(String key, double min, double max);

    /**
     * 返回有序集 key 中，指定区间内的成员。 0 -1 显示整个有序集成员 其中成员的位置按 score 值递增(从小到大)来排序
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public abstract Set<String> zrange(String key, long start, long end);

    /**
     * 返回有序集 key 中，指定区间内的成员。 0 -1 显示整个有序集成员 其中成员的位置按 score 值递增(从小到大)来排序
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public abstract Set<Tuple> zrangeWithScores(String key, long start, long end);


    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员 其中成员的位置按 score
     * 值递增(从小到大)来排序
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public abstract Set<String> zrangeByScore(String key, double min, double max);

    /**
     * 返回有序集 key 中成员 member 的排名 其中有序集成员按 score 值递增(从小到大)顺序排列
     *
     * @param key
     * @param member
     * @return
     */
    public abstract long zrank(String key, String member);

    /**
     * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略
     *
     * @param key
     * @param member
     * @return
     */
    public abstract long zrem(String key, String member);

    /**
     * 返回有序集 key 中，成员 member 的 score 值
     *
     * @param key
     * @param member
     * @return
     */
    public abstract double zscore(String key, String member);

    /**
     * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
     *
     * @param key
     * @param member
     * @return
     */
    public abstract double zincrby(String key, double score, String member);

    /**
     * 移除有序集 key 中，指定排名(rank)区间内的所有成员。 区间分别以下标参数 start 和 stop 指出，包含 start 和 stop
     * 在内。 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public abstract long zremrangeByRank(String key, long start, long end);

    /**
     * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public abstract long zremrangeByScore(String key, double start, double end);

    /**
     * 返回有序集 key 中，指定区间内的成员 其中成员的位置按 score 值递减(从大到小)来排列。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public abstract Set<String> zrevrange(String key, long start, long end);

    /**
     * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员 有序集成员按 score
     * 值递减(从大到小)的次序排列。
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public abstract Set<String> zrevrangeByScore(String key, double max, double min);

    /**
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序
     *
     * @param key
     * @param member
     * @return
     */
    public abstract long zrevrank(String key, String member);

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略
     *
     * @param key
     * @param members
     */
    public abstract void sadd(String key, String... members);

    /**
     * 返回集合 key 的基数(集合中元素的数量)
     *
     * @param key
     * @param members
     */
    public abstract long scard(String key, String... members);

    /**
     * 返回一个集合的全部成员，该集合是所有给定集合之间的差集。
     *
     * @param keys
     */
    public abstract Set<String> sdiff(String... keys);

    /**
     * 和 SDIFF 类似，但它将结果保存到 destination 集合，返回操作元素数
     *
     * @param dstkey
     * @param keys
     */
    public abstract long sdiffstore(String dstkey, String... keys);

    /**
     * 返回一个集合的全部成员，该集合是所有给定集合的交集。
     *
     * @param keys
     */
    public abstract Set<String> sinter(String... keys);

    /**
     * 和 SINTER 类似，但它将结果保存到 destination 集合，返回操作元素数
     *
     * @param dstkey
     * @param keys
     */
    public abstract long sinterstore(String dstkey, String... keys);

    /**
     * 判断 member 元素是否集合 key 的成员
     *
     * @param key
     * @param member
     */
    public abstract boolean sismember(String key, String member);

    /**
     * 返回集合 key 中的所有成员
     *
     * @param key
     */
    public abstract Set<String> smembers(String key);

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略
     *
     * @param key
     */
    public abstract long srem(String key, String... members);

    /**
     * 返回一个集合的全部成员，该集合是所有给定集合的并集
     *
     * @param keys
     */
    public abstract Set<String> sunion(String... keys);

    /**
     * 和 SUNION 类似，但它将结果保存到 destination 集合，返回操作元素数
     *
     * @param dstkey
     * @param keys
     */
    public abstract long sunionstore(String dstkey, String... keys);

    /**
     * 移除并返回集合中的一个随机元素。
     * @param key
     * @return
     */
    public abstract String spop(String key);

    /**
     * 返回集合中的一个随机元素。
     * @param key
     * @return
     */
    public abstract String srandmember(String key);

    /**
     * dbsize 返回当前数据库的key数量，集群不支持
     * @return
     */
    public abstract long dbSize();

    /**
     * 切换到指定的数据库，数据库索引号 index 用数字值指定
     * @return
     */
    //public abstract String select(int index);

    /**
     * 将byte值 value 关联到 key 。
     * 如果 key 已经持有其他值， setByte 就覆写旧值，无视类型。
     * 对于某个原本带有生存时间（TTL）的键来说， 当 setByte 成功在这个键上执行时， 这个键原有的 TTL 将被清除。
     * 时间复杂度：O(1)
     * @param key key
     * @param value byte[] value
     * @return 在设置操作成功完成时，才返回 OK 。
     */
    public abstract String setByte(final String key, final byte[] value) ;

    /**
     * 将值 byte值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
     * 如果 key 已经持有其他值， setByteEx 就覆写旧值，无视类型。
     * 对于某个原本带有生存时间（TTL）的键来说， 当 setByteEx 成功在这个键上执行时， 这个键原有的 TTL 将被清除。
     * 时间复杂度：O(1)
     * @param key key
     * @param
     * @param value byte[] value
     * @return 在设置操作成功完成时，才返回 OK 。
     */
    public abstract String setByteEx(final String key,int seconds, final byte[] value) ;

    /**
     * 返回 key 所关联的Byte值。
     *	如果 key 不存在那么返回null 。
     *	假如 key 储存的值不是Byte类型，返回一个错误，因为 getByte 只能用于处理Byte值。
     * @param key key
     * @return byte[]  value 。
     */
    public abstract byte[] getByte(final String key) ;

    /**
     * 删除给定的一个 key。
     * @param key key
     * @return long  value 。
     */
    public abstract long delByte(final String key) ;

    /**
     *
     * 描 述：获取hash数据 通过key
     * 作 者：hy
     * 历 史: (版本) 作者 时间 注释
     * @param key
     * @return
     */
    public Map<byte[], byte[]> hashByteGetAll(final String key) ;


    /**
     *
     * 描 述：删除hash中的某个值
     * 作 者：hy
     * 历 史: (版本) 作者 时间 注释
     * @param key
     * @param fields
     * @return
     */
    public Long hashByteDel(final byte[] key, final byte[]... fields);

    /**
     * 删除 根据byte[]的key
     * @param key 匹配的key
     * @return 删除成功的条数
     */
    public Long delByteKey(final byte[] key);

    /**
     *
     * 描 述：将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
     如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，
     并通过重新插入这个 member 元素，来保证该 member 在正确的位置上
     * 作 者：hy
     * 历 史: (版本) 作者 时间 注释
     * @param key
     * @param score 分数
     * @param member 成员
     * @return
     */
    public Long zaddHashByte(final byte[] key,double score,byte[] member);

    /**
     *
     * 描 述：移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员
     * 作 者：hy
     * 历 史: (版本) 作者 时间 注释
     * @param key
     * @param start 开始
     * @param end	结束
     * @return
     */
    public Long zremrangeByScoreHashByte(final byte[] key, double start, double end);

    /**
     *
     * 描 述：移除有序集 key 中的一个或多个成员，不存在的成员将被忽略
     * 作 者：hy
     * 历 史: (版本) 作者 时间 注释
     * @param key
     * @param member 开始
     * @return
     */
    public Long zremHashByte(final byte[] key,final byte[]... member);

    /**
     *
     * 描 述：返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列
     * 作 者:hy
     * 历 史: (版本) 作者 时间 注释
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<byte[]> zrangeByScoreHashByte(final byte[] key,double min,double max);

    /**
     * 查看redis统计信息,集群不支持
     * @return
     */
    public String info();

    /**
     * 查看redis统计信息，集群不支持
     * @return
     */
    public String info(String section);

    /**
     *
     * 描 述：根据key获取value ,若为null则赋值value,返回null
     * 作 者：hy
     * 历 史: (版本) 作者 时间 注释
     * @param key
     * @param value
     * @return
     */
    public String getSet(String key,String value);

    /**
     *
     * 描 述：返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列
     * 		可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset 很大时，定位 offset 的操作可能需要遍历整个有序集
     * 作 者：hy
     * 历 史: (版本) 作者 时间 注释
     * @param key
     * @param min   score 值 min   包括等于 min 或 max
     * @param max 	score 值  max  包括等于 min 或 max
     * @param offset 区间
     * @param count 数量
     * @return
     */
    public Set<String> zrangeByScore(String key,String min,String max,int offset,int count);

    /**
     *
     * 描 述：返回列表 key 中，下标为 index 的元素
     * 作 者：hy
     * 历 史: (版本) 作者 时间 注释
     * @param key
     * @param index
     * @return
     */
    public String lindex(String key, long index) ;

    /**
     *
     * 描 述：移除并返回列表 key 的尾元素
     * 作 者：hy
     * 历 史: (版本) 作者 时间 注释
     * @param key
     * @return
     */
    public String rpop(String key);
    /**
     * 描 述：增加 key 指定的哈希集中指定字段的数值。如果 key 不存在，会创建一个新的哈希集并与 key 关联。
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincrBy(String key,String field,int value);

}
