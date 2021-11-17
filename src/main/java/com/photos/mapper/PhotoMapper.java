package com.photos.mapper;

import com.photos.model.Photo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PhotoMapper {
    void delPhoto(long photosId);

    void createPhotoTableForSqlite();

    void createPhotoTableForMysql();

    void dropPhotoTable();

    void addPhoto(Photo photo);

    List<Photo> getList(@Param("page") int page, @Param("size") int size,
                        @Param("name") String name,  @Param("photosId") Long photosId);

    Long getTotal( @Param("name") String name,  @Param("photosId") Long photosId);

    int del(String id);

    Photo getById(long id);

    int update(Photo photo);

    List<Photo> getAllPhotoByPhotosId(Long photosId);

}
