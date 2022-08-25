//package com.example.kafkatutorial.service;
//
//import com.alibaba.fastjson.annotation.JSONField;
//import java.io.Serializable;
//import java.util.Date;
//import org.riie.dataserve.utils.DateUtils;
//
///**
// * @author smile
// * @date 2022-07-27 10:35
// */
//
//public class PointValue implements Serializable {
//    private static final Long serialVersionUID = 1L;
//
//    private String code;
//
//
//    private String value;
//
//    private String time;
//
//    @JSONField(format= DateUtils.DATE_TIME_PATTERN)
//    private Date updateTime;
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public Date getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(Date updateTime) {
//        this.updateTime = updateTime;
//    }
//}
