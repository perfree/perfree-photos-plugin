package com.photos.controller;

import cn.hutool.crypto.SecureUtil;
import com.perfree.base.BaseController;
import com.perfree.commons.Pager;
import com.photos.common.Constants;
import com.photos.model.Photo;
import com.photos.model.Photos;
import com.photos.service.PhotoService;
import com.photos.service.PhotosService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FrontController extends BaseController {
    @Autowired
    private PhotosService photosService;

    @Autowired
    private PhotoService photoService;

    @RequestMapping("/photos")
    public String frontIndex(Model model){
        model.addAttribute("url", "/photosList/");
        return pluginView("/photos.html", "/photos.html", "/photos-static/index.html");
    }

    @RequestMapping("/photosList/{pageIndex}")
    public String articleListPage(@PathVariable("pageIndex") int pageIndex,Model model) {
        model.addAttribute("url", "/photosList/");
        model.addAttribute("pageIndex", pageIndex);
        return  pluginView("/photos.html", "/photos.html", "/photos-static/index.html");
    }

    @RequestMapping("/photos/{id}")
    public String frontPhotos(@PathVariable("id") Long id, Model model){
        model.addAttribute("photos", photosService.getById(id));
        return pluginView("/photoList.html", "/photoList.html", "/photos-static/photos.html");
    }

    @PostMapping("/photos/photosList")
    @ResponseBody
    public Pager<Photos> photosList(@RequestBody Pager<Photos> pager) {
        return photosService.list(pager);
    }


    @PostMapping("/photos/photoList")
    @ResponseBody
    public Pager<Photo> photoList(@RequestBody Pager<Photo> pager) {
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
