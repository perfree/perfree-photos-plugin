package com.photos.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @description 相册
 * @author Perfree
 * @date 2021/11/15 9:23
 */
public class Photos implements Serializable {
    private static final long serialVersionUID = 4900274588193322136L;

    private Long id;
    @NotBlank(message = "相册名不允许为空")
    @Length(max = 100,message = "相册名最多100个字符")
    private String name;

    @NotBlank(message = "相册描述不允许为空")
    @Length(max = 500,message = "相册描述最多500个字符")
    private String desc;
    @NotBlank(message = "相册封面图不允许为空")
    private String coverUrl;
    private Integer isEncryption;
    private String password;
    private Long userId;
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Integer getIsEncryption() {
        return isEncryption;
    }

    public void setIsEncryption(Integer isEncryption) {
        this.isEncryption = isEncryption;
    }

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

    @Override
    public String toString() {
        return "Photos{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", isEncryption=" + isEncryption +
                ", password='" + password + '\'' +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
