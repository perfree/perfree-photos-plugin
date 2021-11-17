package com.photos.controller;

import com.perfree.commons.FileUtil;
import com.perfree.commons.ResponseBean;
import com.photos.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @description 上传
 * @author Perfree
 * @date 2021/11/16 10:28
 */
@Controller
public class UploadController {
    /**
     * 添加相册
     * @return String
     */
    @PostMapping("/admin/plugin/photos/upload")
    @ResponseBody
    public ResponseBean upload(HttpServletRequest request) {
        try{
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile multiFile = multipartRequest.getFile("file");
            if (multiFile == null){
                return ResponseBean.fail("封面图不能为空!", null);
            }
            String multiFileName = multiFile.getOriginalFilename();
            if (StringUtils.isBlank(multiFileName)){
                return ResponseBean.fail("文件名不能为空!", null);
            }
            String path = FileUtil.uploadMultiFile(multiFile, Constants.PHOTOS_PATH, "upload");

            return ResponseBean.success("上传成功", path);
        }catch (Exception e){
            return ResponseBean.fail("上传失败", e.getMessage());
        }
    }
}
