package com.imooc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: icych
 * @description:
 * @date: Created on 19:40 2018/7/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoggerTest {
//    private final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void test() {
        log.info("test info");
        log.debug("test debug");
        log.error("test error");
    }
}
