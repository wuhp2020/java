package com.api.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class FeatureResponse implements Serializable {
    private Double left;
    private Double top;
    private Double length;
    private Double width;
}
