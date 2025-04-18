package net.uoay.chat.redis;

import net.uoay.chat.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final DefaultRedisScript<Set<String>> getSetScript;
    private static final DefaultRedisScript<Boolean> addToSetScript;

    static {
        getSetScript = new DefaultRedisScript<>();
        getSetScript.setScriptSource(
            new ResourceScriptSource(new ClassPathResource("redis/get_set_if_exist.lua"))
        );
        addToSetScript = new DefaultRedisScript<>();
        addToSetScript.setScriptSource(
            new ResourceScriptSource(new ClassPathResource("redis/add_to_set_if_exist.lua"))
        );
        addToSetScript.setResultType(Boolean.class);
    }

    public Optional<Set<String>> getSetIfExists(String key) {
        // null if not exists
        Set<String> members = stringRedisTemplate.execute(getSetScript, List.of(key));
        return Optional.ofNullable(members);
    }

    @Async
    public void createStringSet(String key, Set<String> set) {
        stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public<K, V> List<Object> execute(
                RedisOperations<K, V> operations
            ) throws DataAccessException {
                operations.multi();
                operations.delete((K) key);
                var ops = operations.opsForSet();
                for (var element: set) {
                    ops.add((K) key, (V) element);
                }
                return operations.exec();
            }
        });
    }

    public void deleteFromFriendsCache(String username1, String username2) {
        stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(
                RedisOperations<K, V> operations
            ) throws DataAccessException {
                operations.multi();
                var ops = operations.opsForSet();
                ops.remove((K) Utils.friendSetKey(username1), username2);
                ops.remove((K) Utils.friendSetKey(username2), username1);
                return operations.exec();
            }
        });
    }

    public void deleteKeys(Collection<String> keys) {
        stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(
                RedisOperations<K, V> operations
            ) throws DataAccessException {
                operations.multi();
                for (var key: keys) {
                    operations.delete((K) key);
                }
                return operations.exec();
            }
        });
    }

    public boolean addToSetIfExists(String key, String value) {
        return stringRedisTemplate.execute(addToSetScript, List.of(key), value);
    }
}
