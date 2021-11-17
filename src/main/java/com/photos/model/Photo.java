package com.photos.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 图像
 * @author Perfree
 * @date 2021/11/15 9:29
 */
public class Photo implements Serializable {
    private static final long serialVersionUID = 4901274588193322136L;

    private Long id;
    private Long photosId;
    private String name;
    private String desc;
    private String url;
    private Long userId;
    private Date createTime;
    private Date updateTime;
    private String password;;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPhotosId() {
        return photosId;
    }

    public void setPhotosId(Long photosId) {
        this.photosId = photosId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
