package com.example.aboutspring.async;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsyncController {

    private final AsyncService asyncService;

    public AsyncController(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    /**
     * API Call!
     * API End!
     * Start!
     * ...
     * End!
     */
    @GetMapping("/test/async")
    public void asyncTest() {
        System.out.println("API Call!");
        asyncService.asyncTest();
        System.out.println("API End!");
    }
}
