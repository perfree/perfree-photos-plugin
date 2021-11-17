package com.photos.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.perfree.commons.DynamicDataSource;
import com.perfree.commons.Pager;
import com.perfree.directive.DirectivePage;
import com.perfree.model.Article;
import com.photos.common.Constants;
import com.photos.mapper.PhotoMapper;
import com.photos.mapper.PhotosMapper;
import com.photos.model.Photo;
import com.photos.model.Photos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class PhotosService {
    @Autowired
    private PhotosMapper photosMapper;

    @Autowired
    private PhotoMapper photoMapper;

    /**
     * 删除表
     */
    public void dropTable() {
        photosMapper.dropPhotosTable();
        photoMapper.dropPhotoTable();
    }

    /**
     * @description 创建表
     * @author Perfree
     * @date 2021/11/15 13:19
     */
    public void createTable() {
        if (DynamicDataSource.dataSourceType.equals("mysql")) {
            photosMapper.createPhotosTableForMysql();
            photoMapper.createPhotoTableForMysql();
        } else {
            photosMapper.createPhotosTableForSqlite();
            photoMapper.createPhotoTableForSqlite();
        }

    }

    /**
     * @description 相册分页
     * @author Perfree
     * @date 2021/11/15 16:10
     */
    public Pager<Photos> list(Pager<Photos> pager) {
        List<Photos> photos = photosMapper.getList((pager.getPageIndex() - 1) * pager.getPageSize(),pager.getPageSize(),
                pager.getForm().getName());
        pager.setTotal(photosMapper.getTotal(pager.getForm().getName()));
        pager.setData(photos);
        pager.setCode(Pager.SUCCESS_CODE);
        return pager;
    }

    /**
     * @description 添加相册
     * @author Perfree
     * @date 2021/11/15 16:10
     */
    public void addPhotos(Photos photos) {
        photos.setCreateTime(new Date());
        if (photos.getIsEncryption() == Constants.PHOTOS_ENCRYPTION) {
            photos.setPassword(SecureUtil.md5(photos.getPassword()));
        }
        photosMapper.addPhotos(photos);
    }

    /**
     * @description 删除相册
     * @author Perfree
     * @date 2021/11/15 16:10
     */
    public int del(String id) {
        long photosId = Long.parseLong(id);
        photoMapper.delPhoto(photosId);
        return photosMapper.del(photosId);
    }

    /**
     * @description 根据相册id获取相册信息
     * @author Perfree
     * @date 2021/11/15 16:29
     */
    public Photos getById(long id) {
        return photosMapper.getById(id);
    }

    /** 
     * @description 更新
     * @param photos  photos
     * @return int  int
     * @author Perfree
     */ 
    public int update(Photos photos) {
        photos.setUpdateTime(new Date());
        return photosMapper.update(photos);
    }

    /** 
     * @description 压缩相册为zip
     * @param photos 相册
     * @return java.io.File 
     * @author Perfree
     */ 
    public File getPhotosZip(Photos photos) {
        File file = new File(Constants.PHOTOS_TMP_PATH);
        if (!file.exists()) {
            FileUtil.mkdir(file.getAbsolutePath());
        }
        FileUtil.clean(file.getAbsolutePath());

        List<Photo> photoList =  photoMapper.getAllPhotoByPhotosId(photos.getId());
        List<File> photoFileList = new ArrayList<>();
        for (Photo photo : photoList) {
           File photoFile = new File(Constants.PHOTOS_PATH + photo.getUrl());
           if (photoFile.exists()) {
               photoFileList.add(photoFile);
           }
        }
        if (photoList.size() > 0) {
            return ZipUtil.zip(FileUtil.file(file.getAbsolutePath() + File.separator + photos.getName() + ".zip"), false,
                    photoFileList.toArray(new File[0])
            );
        }
        return null;
    }

    public DirectivePage<HashMap<String, String>> frontArticlesPage(DirectivePage<HashMap<String, String>> photosPage) {
        List<Photos> photos = photosMapper.getList((photosPage.getPageIndex() - 1) * photosPage.getPageSize(),photosPage.getPageSize(),null);
        photosPage.setTotal(photosMapper.getTotal(null));
        photosPage.setData(photos);
        return photosPage;
    }
}
