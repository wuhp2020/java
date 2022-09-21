package com.web.观察者模式;

import java.util.Random;

/**
 * 买彩票模拟
 */
public class LotteryTest {
    public static void main(String[] args) {
        LotteryHolders li = new LotteryHolders(String.valueOf(new Random().nextInt(10)));
        LotteryHolders wang = new LotteryHolders(String.valueOf(new Random().nextInt(10)));
        LotteryHolders yang = new LotteryHolders(String.valueOf(new Random().nextInt(10)));
        LotterySubject lotteryStore = new LotterySubject();
        lotteryStore.registerObserver(li);
        lotteryStore.registerObserver(wang);
        lotteryStore.registerObserver(yang);
        lotteryStore.lottery("5");
    }
}
