package com.safepay.safepay.Service;



import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheTestService {

    @Cacheable(value = "testCache", key = "#id")
    public String getData(Long id) {
        System.out.println("DATABASE/METHOD EXECUTED: " + id);
        return "User-" + id;
    }
}