package com.web.service;

import com.google.common.collect.Lists;
import com.web.vo.doc.DocVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DocService {

    public List<DocVO> findDoc() throws Exception {
        return Lists.newArrayList(new DocVO());
    }
}
