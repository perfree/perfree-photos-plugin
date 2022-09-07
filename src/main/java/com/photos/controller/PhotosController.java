package com.photos.controller;

import com.perfree.base.BaseController;
import com.perfree.commons.Pager;
import com.perfree.commons.ResponseBean;
import com.perfree.permission.AdminMenu;
import com.photos.model.Photos;
import com.photos.service.PhotosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PhotosController extends BaseController {

    @Autowired
    private PhotosService photosService;

    /**
     * @description 相册管理页
     * @return java.lang.String
     * @author Perfree
     */
    @AdminMenu(name="相册管理", groupId = com.perfree.commons.Constants.ADMIN_MENU_GROUP_PLUGIN, seq = 1)
    @GetMapping("/admin/plugin/photos")
    public String adminPhotos(){
        return "/photos-static/admin/photos/photos-list.html";
    }

    /** 
     * @description 相册分页
     * @param pager  pager
     * @return com.perfree.commons.Pager<com.photos.model.Photos> 
     * @author Perfree
     */ 
    @RequestMapping("/admin/plugin/photos/list")
    @ResponseBody
    public Pager<Photos> list(@RequestBody Pager<Photos> pager) {
        return photosService.list(pager);
    }

    /**
     * 添加相册
     * @return String
     */
    @PostMapping("/admin/plugin/photos/addPhotos")
    @ResponseBody
    public ResponseBean addPhotos(@RequestBody @Valid Photos photos) {
        try{
            photos.setUserId(getUser().getId());
            photosService.addPhotos(photos);
            return ResponseBean.success("添加成功", photos);
        }catch (Exception e){
            return ResponseBean.fail("添加失败", e.getMessage());
        }
    }

    /**
     * 删除相册
     * @return String
     */
    @PostMapping("/admin/plugin/photos/del")
    @ResponseBody
    public ResponseBean del(@RequestBody String id) {
        if (photosService.del(id) > 0) {
            return ResponseBean.success("删除成功", null);
        }
        return ResponseBean.fail("删除失败", null);
    }

    /**
     * 更新相册
     * @return String
     */
    @PostMapping("/admin/plugin/photos/editPhotos")
    @ResponseBody
    public ResponseBean editPhotos(@RequestBody @Valid Photos photos) {
        if (photosService.update(photos) > 0) {
            return ResponseBean.success("更新成功", null);
        }
        return ResponseBean.fail("更新失败", null);
    }

    @RequestMapping("/admin/plugin/photos/getById")
    @ResponseBody
    public ResponseBean getById(String id){
        return ResponseBean.success("success", photosService.getById(Long.parseLong(id)));
    }
}
