package com.cmci.common.model;

import java.util.List;
import java.util.Map;

public interface CommonMapper {
    public Map<String, Object> getObject(String querId);
    public List<Map<String, Object>> getList(String queryId);
    public int add(String id, Map<String, Object> param);
    public int remove(String id);
    public int modify(String id, Map<String, Object> param);
}
