package com.photos.service;

import com.perfree.commons.Pager;
import com.photos.mapper.PhotoMapper;
import com.photos.model.Photo;
import com.photos.model.Photos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PhotoService {

    @Autowired
    private PhotoMapper photoMapper;

    public void addPhoto(Photo photo) {
        photo.setCreateTime(new Date());
        photoMapper.addPhoto(photo);
    }

    public Pager<Photo> list(Pager<Photo> pager) {
        List<Photo> photos = photoMapper.getList((pager.getPageIndex() - 1) * pager.getPageSize(),pager.getPageSize(),
                pager.getForm().getName(),  pager.getForm().getPhotosId());
        pager.setTotal(photoMapper.getTotal(pager.getForm().getName(),  pager.getForm().getPhotosId()));
        pager.setData(photos);
        pager.setCode(Pager.SUCCESS_CODE);
        return pager;
    }

    public int del(String id) {
        return photoMapper.del(id);
    }

    public Photo getById(long id) {
        return photoMapper.getById(id);
    }

    public int update(Photo photo) {
        photo.setUpdateTime(new Date());
        return photoMapper.update(photo);
    }
}
