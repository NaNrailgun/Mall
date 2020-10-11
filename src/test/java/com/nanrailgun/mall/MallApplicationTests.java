package com.nanrailgun.mall;

import com.nanrailgun.springbootstartersnowflake.beans.IdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class MallApplicationTests {

    @Autowired
    IdWorker idWorker;

    @Test
    void contextLoads() {
        Set<Long> set = new HashSet<>();
        Long temp = idWorker.nextId();
        for (int i = 0; i < 1000; i++) {
            Long id = idWorker.nextId();
            if (set.contains(id)){
                System.out.println("error");
                break;
            }
            if (id <= temp) {
                System.out.println("min");
                break;
            }
            set.add(id);
            System.out.println(idWorker.nextId());
        }
    }

}
