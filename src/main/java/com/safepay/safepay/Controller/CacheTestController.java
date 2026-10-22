package com.safepay.safepay.Controller;

import com.safepay.safepay.Service.CacheTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class CacheTestController {

    private final CacheTestService cacheTestService;

    public CacheTestController(CacheTestService cacheTestService) {
        this.cacheTestService = cacheTestService;
    }

    @GetMapping("/cache/{id}")
    public String testCache(@PathVariable Long id) {
        return cacheTestService.getData(id);
    }
}