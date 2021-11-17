package com.photos.mapper;

import com.photos.model.Photos;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PhotosMapper {
    void dropPhotosTable();

    void createPhotosTableForMysql();

    void createPhotosTableForSqlite();

    List<Photos> getList(@Param("page") int page, @Param("size") int size, @Param("name") String name);

    Long getTotal( @Param("name") String name);

    void addPhotos(Photos photos);

    int del(long photosId);

    Photos getById(long id);

    int update(Photos photos);

}
