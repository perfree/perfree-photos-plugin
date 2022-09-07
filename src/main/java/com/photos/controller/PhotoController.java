package com.photos.controller;

import com.perfree.base.BaseController;
import com.perfree.commons.Pager;
import com.perfree.commons.ResponseBean;
import com.photos.model.Photo;
import com.photos.model.Photos;
import com.photos.service.PhotoService;
import com.photos.service.PhotosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PhotoController extends BaseController {

    @Autowired
    private PhotosService photosService;

    @Autowired
    private PhotoService photoService;

    @RequestMapping("/admin/plugin/photo/listPage/{id}")
    public String listPage(@PathVariable("id") String id, Model model){
        Photos photos = photosService.getById(Long.parseLong(id));
        model.addAttribute("photos", photos);
        return "/photos-static/admin/photo/photo-list.html";
    }

    /**
     * 添加图片
     * @return String
     */
    @PostMapping("/admin/plugin/photo/addPhoto")
    @ResponseBody
    public ResponseBean addPhotos(@RequestBody @Valid Photo photo) {
        try{
            photo.setUserId(getUser().getId());
            photoService.addPhoto(photo);
            return ResponseBean.success("添加成功", photo);
        }catch (Exception e){
            return ResponseBean.fail("添加失败", e.getMessage());
        }
    }

    /**
     * @description 图片分页
     * @param pager  pager
     * @return com.perfree.commons.Pager<com.photos.model.Photos>
     * @author Perfree
     */
    @RequestMapping("/admin/plugin/photo/list")
    @ResponseBody
    public Pager<Photo> list(@RequestBody Pager<Photo> pager) {
        return photoService.list(pager);
    }

    /**
     * 删除图片
     * @return String
     */
    @PostMapping("/admin/plugin/photo/del")
    @ResponseBody
    public ResponseBean del(@RequestBody String id) {
        if (photoService.del(id) > 0) {
            return ResponseBean.success("删除成功", null);
        }
        return ResponseBean.fail("删除失败", null);
    }

    @GetMapping("/admin/plugin/photo/getById")
    @ResponseBody
    public ResponseBean getById(String id) {
        return ResponseBean.success("success", photoService.getById(Long.parseLong(id)));
    }

    /**
     * 更新图片
     * @return String
     */
    @PostMapping("/admin/plugin/photo/editPhoto")
    @ResponseBody
    public ResponseBean editPhoto(@RequestBody @Valid Photo photo) {
        if (photoService.update(photo) > 0) {
            return ResponseBean.success("更新成功", null);
        }
        return ResponseBean.fail("更新失败", null);
    }
}
