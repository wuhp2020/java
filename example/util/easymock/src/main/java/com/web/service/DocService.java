package com.web.service;

import com.google.common.collect.Lists;
import com.web.vo.doc.DocVO;
import lombok.extern.slf4j.Slf4j;
import org.easymock.EasyMock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DocService {

    /**
     * EasyMock创建了一个模拟股票服务
     * EasyMock.createMock(...)
     *
     * 模拟执行stockService接口的getPrice方法. 对于googleStock, 请返回50.00作为价格
     * EasyMock.expect(...).andReturn(...)
     *
     * EasyMock准备Mock对象准备就绪因此它可以用于测试
     * EasyMock.replay(...)
     *
     * 投资组合现在包含一个列表两个股票
     * portfolio.setStocks(...)
     *
     * 将stockService Mock对象分配给投资组合
     * portfolio.setStockService(...)
     *
     * 投资组合使用模拟股票服务基于其股票返回市场价值
     * portfolio.getMarketValue()
     * @return
     * @throws Exception
     */
    public List<DocVO> findDoc() throws Exception {
        return Lists.newArrayList(new DocVO());
    }
}
