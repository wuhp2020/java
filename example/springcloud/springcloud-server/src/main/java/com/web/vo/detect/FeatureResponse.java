package com.web.vo.detect;

import lombok.Data;

import java.io.Serializable;

@Data
public class FeatureResponse implements Serializable {
    private Double left;
    private Double top;
    private Double length;
    private Double width;
}
