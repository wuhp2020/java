package com.web.service;

import com.web.vo.doc.DocVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DocService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    public void save(DocVO docVO) {

    }
}
