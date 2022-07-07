package com.web.service;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
@Slf4j
public class LambdaService {

    public void isLambda() {
        // 布尔表达式
        Predicate<List<String>> predicate = (List<String> list) -> list.isEmpty();

        // 创建一个对象
        Supplier<Object> supplier1 = () -> new Object();
        Supplier<Runnable> supplier2 = () -> new Runnable() {
            @Override
            public void run() {
                log.info("=======");
            }
        };

        // 消费一个对象
        Consumer<Object> consumer = (Object o) -> System.out.println(o);

        // 从一个对象中选择
        Function<String, Integer> function = (String s) -> s.length();

        // 组合两个值
        BinaryOperator<Integer> binaryOperator = (Integer a, Integer b) -> a + b;

        // 比较两个对象
        Comparator<String> comparator = (String a, String b) -> a.compareTo(b);
    }

    public void lambdaExample() {
        // 消费一个对象
        List<String> list1 = Lists.newArrayList();
        list1.stream().forEach((s) -> {
            System.out.println(s);
        });

        // 布尔表达式
        List<String> list2 = Lists.newArrayList();
        list1.stream().filter(s -> s.isEmpty());

        // 从一个对象中选择
        List<String> list3 = Lists.newArrayList();
        list1.stream().map(s -> s.length());

        // 组合两个值
        List<String> list4 = Lists.newArrayList();
        list1.stream().reduce((a, b) -> a + b);

        // 比较两个对象
        List<String> list5 = Lists.newArrayList();
        list1.stream().sorted((a, b) -> a.compareTo(b));

    }

}
