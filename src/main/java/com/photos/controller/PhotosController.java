package com.photos.controller;

import cn.hutool.crypto.SecureUtil;
import com.perfree.base.BaseController;
import com.perfree.commons.Pager;
import com.perfree.commons.ResponseBean;
import com.perfree.permission.AdminMenu;
import com.photos.common.Constants;
import com.photos.model.Photo;
import com.photos.model.Photos;
import com.photos.service.PhotoService;
import com.photos.service.PhotosService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;

@Controller
public class PhotosController extends BaseController {

    @Autowired
    private PhotosService photosService;
    @Autowired
    private PhotoService photoService;

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
     * @description 添加相册页
     * @return java.lang.String
     * @author Perfree
     */
    @GetMapping("/admin/plugin/photos/addPage")
    public String addPhotosPage(){
        return "/photos-static/admin/photos/photos-add.html";
    }


    /**
     * 添加相册
     * @return String
     */
    @PostMapping("/admin/plugin/photos/addPhotos")
    @ResponseBody
    public ResponseBean addPhotos(@RequestBody @Valid Photos photos) {
        try{
            if (photos.getIsEncryption() == Constants.PHOTOS_ENCRYPTION && StringUtils.isBlank(photos.getPassword())) {
                return ResponseBean.fail("请填写相册密码",null);
            }
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
     * 编辑相册页
     * @return String
     */
    @GetMapping("/admin/plugin/photos/editPage/{id}")
    public String editPage(@PathVariable("id") String id, Model model) {
        Photos photos = photosService.getById(Long.parseLong(id));
        model.addAttribute("photos", photos);
        return view("/photos-static/admin/photos/photos-edit.html");
    }

    /**
     * 更新相册
     * @return String
     */
    @PostMapping("/admin/plugin/photos/editPhotos")
    @ResponseBody
    public ResponseBean editPhotos(@RequestBody @Valid Photos photos) {
        if (photos.getIsEncryption() == Constants.PHOTOS_ENCRYPTION && StringUtils.isNotBlank(photos.getPassword())) {
            photos.setPassword(SecureUtil.md5(photos.getPassword()));
        }
        if (photosService.update(photos) > 0) {
            return ResponseBean.success("更新成功", null);
        }
        return ResponseBean.fail("更新失败", null);
    }

    /**
     * 下载相册
     * @param response response
     * @param id id
     * @return String
     */
    @GetMapping("/admin/plugin/photos/download/{id}")
    @ResponseBody
    @RequiresRoles(value={"admin","editor"}, logical= Logical.OR)
    public String downloadPhotos(HttpServletResponse response, @PathVariable("id") String id) {
        Photos photos = photosService.getById(Long.parseLong(id));
        File file = photosService.getPhotosZip(photos);
        if (file != null && file.exists()) {
            response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
            response.setContentType("application/force-download");
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try{
                response.addHeader("Content-Disposition", "attachment;fileName="+ URLEncoder.encode(photos.getName() + ".zip", "UTF-8"));
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0 , i);
                    i = bis.read(buffer);
                }
                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "下载失败";
    }


    @RequestMapping("/photos")
    public String frontIndex(Model model){
        model.addAttribute("url", "/photosList/");
        return "/photos-static/index.html";
    }

    @RequestMapping("/photosList/{pageIndex}")
    public String articleListPage(@PathVariable("pageIndex") int pageIndex,Model model) {
        model.addAttribute("url", "/photosList/");
        model.addAttribute("pageIndex", pageIndex);
        return  "/photos-static/index.html";
    }

    @RequestMapping("/photos/{id}")
    public String frontPhotos(@PathVariable("id") Long id,Model model){
        model.addAttribute("photos", photosService.getById(id));
        return "/photos-static/photos.html";
    }

    @PostMapping("/photos/photoList")
    @ResponseBody
    public Pager<Photo> del(@RequestBody Pager<Photo> pager) {
        Photos photosById = photosService.getById(pager.getForm().getPhotosId());
        if (photosById.getIsEncryption() == Constants.PHOTOS_ENCRYPTION) {
            if (StringUtils.isBlank(pager.getForm().getPassword())) {
                pager.setCode(Pager.ERROR_CODE);
                pager.setMsg("请输入访问密码");
                return pager;
            }
            pager.getForm().setPassword(SecureUtil.md5(pager.getForm().getPassword()));
            if (!pager.getForm().getPassword().equals(photosById.getPassword())){
                pager.setCode(Pager.ERROR_CODE);
                pager.setMsg("密码错误");
                return pager;
            }
        }
        return photoService.list(pager);
    }
}
