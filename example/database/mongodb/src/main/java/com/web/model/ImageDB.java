package com.web.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@CompoundIndexes({@CompoundIndex(name = "index_imagedb_type_createtime", def = "{'type':1, 'createTime':1}")})
public class ImageDB {

    public ImageDB(){}
    public ImageDB(String imageId) {
        this.imageId = imageId;
    }

    @Id
    private ObjectId id;
    @Indexed(name = "index_imagedb_imageid")
    private String imageId;
    private String type;
    private String imagebase64;
    @Indexed(name = "index_imagedb_createtime")
    private Date createTime;

}
