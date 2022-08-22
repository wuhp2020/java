package com.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;


@Data
@Accessors(chain = true)
public class DocDO {

    private String id;
    private String title;
    private String type;
    private String author;
    private String content;
    private Date date;

}
