package com.web.service;

import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class GridFsFileService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    public String save(String fileBase64) {
        byte[] fileBytes = Base64Utils.decodeFromString(fileBase64);
        return this.save(new ByteArrayInputStream(fileBytes), null, null, null);
    }

    private String save(InputStream inputStream, String fileName, String contentType, DBObject metaData) {
        ObjectId objectId = gridFsTemplate.store(inputStream, fileName, contentType, metaData);
        if (objectId != null) {
            return objectId.toString();
        }
        return "";
    }

    public String get(String id) throws Exception {
        GridFSFile file = this.gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        String imageBase64 = "";
        if(file != null) {
            GridFSDownloadStream in = gridFSBucket.openDownloadStream(file.getObjectId());
            GridFsResource resource = new GridFsResource(file, in);
            InputStream inputStream = resource.getInputStream();
            imageBase64 = Base64Utils.encodeToString(this.getBytes(inputStream));
        }
        if (StringUtils.isEmpty(imageBase64)) {
            log.info("==========>>>>>>>>>> not get imageBase64");
        } else {
            log.info("==========>>>>>>>>>> get imageBase64");
        }
        return imageBase64;
    }

    public void delete(List<String> imageIds) {
        imageIds.stream().forEach(id -> gridFsTemplate.delete(new Query(Criteria.where("_id").is(id))));
    }

    private byte[] getBytes(InputStream fileInputStream) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int i = 0;
        try {
            while (-1 != (i = fileInputStream.read(buffer))) {
                bos.write(buffer, 0, i);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            try {
                fileInputStream.close();
                bos.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return bos.toByteArray();
    }

}
