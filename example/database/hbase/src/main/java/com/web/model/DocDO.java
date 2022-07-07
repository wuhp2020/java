package com.web.model;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class DocDO {

    private String id;
    private String title;
    private String type;
    private String author;
    private String content;
    private Date date;

}
