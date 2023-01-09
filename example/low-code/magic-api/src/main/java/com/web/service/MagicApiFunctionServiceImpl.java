package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ssssssss.magicapi.core.model.Group;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("MagicApiFunctionServiceImpl")
public class MagicApiFunctionServiceImpl implements IMagicApiService {

    @Override
    public void autoCreate(Group group, String tableName, List<Map<String, Object>> columns) {

    }

    @Override
    public String type() {
        return "function";
    }
}
