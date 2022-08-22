package com.web.mapper;

import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import com.web.model.DocDO;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class DocRowMapper implements RowMapper<DocDO> {

    private static byte[] COLUMNFAMILY = "doc".getBytes();
    private static byte[] TITLE = "title".getBytes();
    private static byte[] TYPE = "type".getBytes();
    private static byte[] AUTHOR = "author".getBytes();
    private static byte[] CONTENT = "content".getBytes();


    @Override
    public DocDO mapRow(Result result, int i) throws Exception {
        DocDO docDO = new DocDO();
        // 设置相关的属性值
        String title = Bytes.toString(result.getValue(COLUMNFAMILY, TITLE));
        String type = Bytes.toString(result.getValue(COLUMNFAMILY, TYPE));
        String author = Bytes.toString(result.getValue(COLUMNFAMILY, AUTHOR));
        String content = Bytes.toString(result.getValue(COLUMNFAMILY, CONTENT));

        return docDO.setTitle(title).setType(type).setAuthor(author).setContent(content);
    }
}
