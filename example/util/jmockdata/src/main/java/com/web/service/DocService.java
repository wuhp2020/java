package com.web.service;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.google.common.collect.Lists;
import com.web.vo.doc.DocVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DocService {

    public List<DocVO> findDoc() throws Exception {
        List<DocVO> docVOS = Lists.newLinkedList();
        MockConfig mockConfig = new MockConfig()
                .subConfig("id").stringSeed("1", "2", "3")
                .subConfig("title").stringSeed("垃圾满天飞", "过年啦")
                .subConfig("author").stringSeed("AA", "张三", "李四", "王五")
                .subConfig("types").stringSeed("DB", "UI")
                .subConfig("content").stringSeed("测试内容")
                .subConfig("date").dateRange("2022-01-10", "2022-01-30")
                .globalConfig();
        for (int i = 0; i < 10; i++) {
            docVOS.add(JMockData.mock(DocVO.class, mockConfig));
        }
        return Lists.newArrayList(docVOS);
    }
}
