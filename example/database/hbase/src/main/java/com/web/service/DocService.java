package com.web.service;

import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import com.web.mapper.DocRowMapper;
import com.web.model.DocDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class DocService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    public List<DocDO> queryList(String startRow, String stopRow) {
        Scan scan = new Scan(Bytes.toBytes(startRow), Bytes.toBytes(stopRow));
        scan.setCaching(5000);
        List<DocDO> docDOS = this.hbaseTemplate.find("doc", scan, new DocRowMapper());
        return docDOS;
    }

    public DocDO queryOne(String row) {
        DocDO docDO = this.hbaseTemplate.get("doc", row, new DocRowMapper());
        return docDO;
    }

    public void saveOrUpdate() {
        Mutation delete = new Delete(Bytes.toBytes(""));
        this.hbaseTemplate.saveOrUpdate("doc", delete);
    }

    public void saveOrUpdateList() {
        List<Mutation> saveOrUpdates = new ArrayList<>();
        Put put = new Put(Bytes.toBytes("135xxxxxx"));
        put.addColumn(Bytes.toBytes("doc"), Bytes.toBytes("title"), Bytes.toBytes("文档测试"));
        saveOrUpdates.add(put);

        Delete delete = new Delete(Bytes.toBytes("136xxxxxx"));
        saveOrUpdates.add(delete);

        // 继续add
        this.hbaseTemplate.saveOrUpdates("doc", saveOrUpdates);
    }
}
