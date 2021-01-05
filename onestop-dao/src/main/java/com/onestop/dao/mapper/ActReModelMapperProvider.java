package com.onestop.dao.mapper;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class ActReModelMapperProvider {
    private static final String TABLE_NAME = "act_re_model";

    public String queryAllModel(Map<String, Object> parameter) {

        return new SQL() {
            {
                SELECT("p.id_, p.rev_,p.name_,p.key_,p.category_,p.version_");
                FROM(TABLE_NAME + " p");
                ORDER_BY("p.name_ desc");
            }
        }.toString();
    }
    public String queryDistinctModel(Map<String, Object> parameter) {

        return new SQL() {
            {
                SELECT("distinct p.name_");
                FROM(TABLE_NAME + " p");
                ORDER_BY("p.name_ desc");
            }
        }.toString();
    }
}
