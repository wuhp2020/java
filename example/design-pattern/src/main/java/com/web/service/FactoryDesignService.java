package com.web.service;

import com.web.service.factorydesign.Food;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/20
 * @ Description: 描述
 */
@Slf4j
@Service
public class FactoryDesignService {

    @Autowired
    private List<Food> foods;

    public Food makeFood(String name) {
        Optional<Food> ofs = Optional.ofNullable(foods)
                .orElse(Collections.emptyList())
                .stream().filter(foodVO -> name.equals(foodVO.foodName())).findFirst();
        if (!ofs.isPresent()) {
            throw new RuntimeException("未找到实现类");
        }
        return ofs.get();
    }
}
