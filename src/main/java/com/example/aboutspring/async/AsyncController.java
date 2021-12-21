package com.example.aboutspring.async;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @see <a href="https://www.baeldung.com/spring-async">How To Do @Async in Spring</a>
 */
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

    /**
     * API Call!
     * Start!
     * ...
     * End!
     * API End!
     */
    @GetMapping("/test/sync")
    public void syncTest() {
        System.out.println("API Call!");
        asyncService.syncTest();
        System.out.println("API End!");
    }
}
