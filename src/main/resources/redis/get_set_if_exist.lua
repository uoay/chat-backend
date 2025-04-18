if redis.call('exists', KEYS[1]) then
    return redis.call('smembers', KEYS[1])
else
    return nil
end
