package com.yldyyn.bootredis.entity;

import org.springframework.context.annotation.Bean;

import java.io.Serializable;

/**
 * @Author: yld
 * @Date: 2019-02-25 14:13
 * @Version 1.0
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String CID;
    private String Cname;
    private String TID;

    public Course(String CID, String cname, String TID) {
        this.CID = CID;
        Cname = cname;
        this.TID = TID;
    }

    public Course() {
        super();
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public String getTID() {
        return TID;
    }

    public void setTID(String TID) {
        this.TID = TID;
    }
}
