if redis.call('exists', KEYS[1]) then
    redis.call('sadd', KEYS[1], ARGV[1])
    return true
else
    return false
end
