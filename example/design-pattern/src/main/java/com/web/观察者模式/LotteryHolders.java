package com.web.观察者模式;

/**
 * 持有彩票的人
 */
public class LotteryHolders implements Observer {

    private String lottery;

    LotteryHolders(String lottery) {
        this.lottery = lottery;
    }

    @Override
    public void update(String msg) {
        //开奖
        System.out.println("本期开奖号码：" + msg + ",我的号码:" + lottery);
    }
}

