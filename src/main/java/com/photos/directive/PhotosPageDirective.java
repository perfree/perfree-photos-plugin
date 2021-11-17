package com.photos.directive;

import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import com.perfree.directive.BaseDirective;
import com.perfree.directive.DirectivePage;
import com.perfree.directive.TemplateDirective;
import com.perfree.service.ArticleService;
import com.photos.service.PhotosService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@TemplateDirective("photosPage")
@Component
public class PhotosPageDirective extends BaseDirective {
    private static PhotosService photosService;

    @Autowired
    public void setArticleService(PhotosService photosService){
        PhotosPageDirective.photosService = photosService;
    }

    public void setExprList(ExprList exprList) {
        super.setExprList(exprList);
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        HashMap<String, String> query = new HashMap<>();
        String url = getModelDataToStr("url", scope);
        DirectivePage<HashMap<String, String>> photosPage = new DirectivePage<>();
        photosPage.setPageIndex(getModelDataToInt("pageIndex", scope, 1));


        photosPage.setForm(query);
        photosPage.setPageSize(getExprParamToInt("pageSize", 10));
        photosPage = photosService.frontArticlesPage(photosPage);
        photosPage.setUrlPrefix(url);
        photosPage.initPagers();

        scope.set("photosPage", photosPage);
        stat.exec(env, scope, writer);
    }

    @Override
    public boolean hasEnd() {
        return true;
    }
}
