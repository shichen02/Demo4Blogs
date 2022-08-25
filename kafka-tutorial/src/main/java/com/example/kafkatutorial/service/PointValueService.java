//package com.example.kafkatutorial.service;
//
//import java.sql.Connection;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import org.riie.dataserve.constant.SysConstant;
//import org.riie.dataserve.utils.FastJsonUtils;
//import org.riie.dataserve.utils.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
///**
// * 时序数据库插入 逻辑
// *
// * @author smile
// * @date 2022-07-27 10:25
// */
//@Component
//public class PointValueService {
//    private static final Logger logger = LoggerFactory.getLogger(PointValueService.class);
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    /**
//     * 批量插入数据库
//     *
//     * @param pointValues:
//     * @return: void
//     * @author: smile
//     */
//
//    public void saveValue(List<PointValue> pointValues) {
//        insertTdengine(pointValues);
//        saveRedis(pointValues);
//    }
//
//
//    public void saveValue(List<PointValue> pointValues, Integer size) {
//        if(StringUtils.isNotNull(pointValues)){
//            if(size == null || size == 0){
//                size = 1000;
//            }
//            List<List<PointValue>> lists = StringUtils.splitList(pointValues, size);
//            for (List<PointValue> values : lists) {
//                insertTdengine(values);
//                saveRedis(values);
//            }
//
//        }
//
//    }
//
//    /**
//     * 分表生成插入语句
//     *
//     * @param pointValues:
//     * @return: void
//     * @author: smile
//     */
//
//    public void saveValueGroup(List<PointValue> pointValues) {
//        insertTdengine(pointValues);
//        saveRedis(pointValues);
//    }
//
//    /**
//     * 单挑保存
//     */
//    public void saveValue(PointValue pointValue) {
//        insertTdengine(pointValue);
//        saveRedis(pointValue);
//    }
//
//    /**
//     * @param pointValues:
//     * @return: void
//     * @author: smile
//     */
//
//    public void insertTdengine(List<PointValue> pointValues) {
//        if (StringUtils.isNotNull(pointValues)) {
//            String sql = getSql(pointValues);
//            insertTdengine(sql);
//        }
//
//    }
//
//    /**
//     * 分表生成插入语句
//     *
//     * @param pointValues:
//     * @return: void
//     * @author: smile
//     */
//
//    public void insertTdengineGroup(List<PointValue> pointValues) {
//        if (StringUtils.isNotNull(pointValues)) {
//            String sql = getSqlGroup(pointValues);
//            insertTdengine(sql);
//        }
//
//    }
//
//    /**
//     * @param pointValue:
//     * @return: void
//     * @author: smile
//     */
//
//    public void insertTdengine(PointValue pointValue) {
//        if (pointValue != null) {
//            String sql = getSql(pointValue);
//            insertTdengine(sql);
//        }
//
//    }
//
//    /**
//     * 插入时序数据库
//     *
//     * @param sql:
//     * @return: void
//     * @author: smile
//     */
//
//    public void insertTdengine(String sql) {
//        if (StringUtils.isNotNull(sql)) {
//            Connection con = TDengineUtil.getJdbcConnection();
//            try {
//                TDengineUtil.exeUpdate(con, sql);
//            } catch (Exception e) {
//                logger.error(e.getMessage());
//            } finally {
//                TDengineUtil.close(con);
//            }
//        }
//
//    }
//
//    /**
//     * 实时值保存到redis
//     *
//     * @param pointValue:
//     * @return: void
//     * @author: smile
//     */
//
//    public void saveRedis(PointValue pointValue) {
//        if (pointValue != null && StringUtils.isNotNull(pointValue.getCode())) {
//            String key = SysConstant.RDEISREALKEY + pointValue.getCode();
//            try {
//                pointValue.setUpdateTime(new Date());
//                redisTemplate.opsForValue().set(key, FastJsonUtils.beanToJson(pointValue));
//            } catch (Exception e) {
//                logger.error(e.getMessage());
//            }
//        }
//    }
//
//    /**
//     * 实时值保存到redis
//     *
//     * @param pointValues:
//     * @return: void
//     * @author: smile
//     */
//
//    public void saveRedis(List<PointValue> pointValues) {
//        if (StringUtils.isNotNull(pointValues)) {
//            for (PointValue pointValue : pointValues) {
//                saveRedis(pointValue);
//            }
//        }
//    }
//
//    /**
//     * 封装插入sql
//     */
//    public String getSql(PointValue pointValue) {
//        if (pointValue != null && StringUtils.isNotNull(pointValue.getCode()) && StringUtils.isNotNull(pointValue.getTime())) {
//            StringBuilder sql = new StringBuilder();
//            sql.append("INSERT INTO ");
//            sql.append(pointValue.getCode());
//            sql.append(" USING pointvalue TAGS ('");
//            sql.append(pointValue.getCode());
//            sql.append("')");
//            sql.append(" VALUES ");
//            sql.append("('").append(pointValue.getTime()).append("', ").append(pointValue.getValue()).append(")");
//            return sql.toString();
//
//        }
//        return null;
//    }
//
//    /**
//     * 封装插入sql
//     */
//    public String getSql(List<PointValue> pointValues) {
//        StringBuilder sql = new StringBuilder();
//        if (StringUtils.isNotNull(pointValues)) {
//            sql.append("INSERT INTO ");
//            for (PointValue pointValue : pointValues) {
//                if (pointValue != null && StringUtils.isNotNull(pointValue.getCode()) && StringUtils.isNotNull(pointValue.getTime())) {
//                    sql.append(" ");
//                    sql.append(pointValue.getCode());
//                    sql.append(" USING pointvalue TAGS ('");
//                    sql.append(pointValue.getCode());
//                    sql.append("')");
//                    sql.append(" VALUES ");
//                    sql.append("('").append(pointValue.getTime()).append("', ").append(pointValue.getValue()).append(") ");
//                }
//            }
//            return sql.toString();
//        }
//
//        return null;
//    }
//
//    /**
//     * 封装插入sql
//     */
//    public String getSqlGroup(List<PointValue> pointValues) {
//        StringBuilder sql = new StringBuilder();
//        if (StringUtils.isNotNull(pointValues)) {
//            sql.append("INSERT INTO ");
//            Map<String, List<PointValue>> maps = pointValues.stream().filter(item -> StringUtils.isNotNull(item.getCode())).collect(Collectors.groupingBy(PointValue::getCode));
//            for (Map.Entry<String, List<PointValue>> entry : maps.entrySet()) {
//                String code = entry.getKey();
//                List<PointValue> values = entry.getValue();
//                sql.append(" ");
//                sql.append(code);
//                sql.append(" USING pointvalue TAGS ('");
//                sql.append(code);
//                sql.append("')");
//                sql.append(" VALUES ");
//                for (PointValue pointValue : values) {
//                    if (pointValue != null && StringUtils.isNotNull(pointValue.getCode()) && StringUtils.isNotNull(pointValue.getTime())) {
//                        sql.append("('").append(pointValue.getTime()).append("', ").append(pointValue.getValue()).append(") ");
//                    }
//                }
//
//            }
//            return sql.toString();
//
//        }
//        return null;
//    }
//}