package com.web.service;

import org.ssssssss.magicapi.core.model.Group;
import java.util.*;

public interface IMagicApiService {

    public abstract void autoCreate(Group group, String tableName, List<Map<String, Object>> columns);
    public abstract String type();
}
