package com.web.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.web.service.HystrixService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.internal.util.InternalObservableUtils;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/hystrix")
@Api(tags = "服务降级和熔断")
@Slf4j
public class HystrixController {

    @Autowired
    private HystrixService hystrixService;

    @GetMapping("lower/grade")
    @ApiOperation(value = "服务降级")
    public String lowerGrade() {
        return hystrixService.lowerGrade();
    }

    @HystrixCommand(commandProperties = {
        // 熔断器在整个统计时间内是否开启的阀值
        @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
        // 至少有3个请求才进行熔断错误比率计算
        /**
         * 设置在一个滚动窗口中, 打开断路器的最少请求数
         * 比如: 如果值是20, 在一个窗口内（比如10秒）, 收到19个请求, 即使这19个请求都失败了, 断路器也不会打开
         */
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
        // 当出错率超过50%后熔断器启动
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
        // 熔断器工作时间, 超过这个时间, 先放一个请求进去, 成功的话就关闭熔断, 失败就再等一段时间
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
        // 统计滚动的时间窗口
        @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000")
    }, defaultFallback = "lowerDefault")
    @GetMapping("fusing/{id}")
    @ApiOperation(value = "服务熔断")
    public String fusing(@RequestParam("id") Integer id) {
        log.info("id: " + id);

        if (id % 2 == 0) {
            throw new RuntimeException();
        }
        return "test: " + id;
    }

    @GetMapping("time/window")
    @ApiOperation(value = "时间窗口")
    public String timeWindow() throws Exception {
        Observable<Integer> source = Observable.interval(50, TimeUnit.MILLISECONDS)
                .map(i -> RandomUtils.nextInt(2));
        source.window(1, TimeUnit.SECONDS).subscribe(window -> {
            int[] metrics = new int[2];
            window.subscribe(i -> metrics[i]++,
                    InternalObservableUtils.ERROR_NOT_IMPLEMENTED,
                    () -> log.info("窗口Metrics:" + metrics));
        });
        TimeUnit.SECONDS.sleep(3);
        return "ok";
    }

    public String lowerDefault() {
        return "降级到Default";
    }
}
