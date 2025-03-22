package com.blog.page.core.services.impl;

import com.blog.page.core.config.PublishedBlogsConfig;
import com.blog.page.core.services.PublishedBlogsService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = PublishedBlogsService.class, immediate = true)
@Designate(ocd = PublishedBlogsConfig.class)
public class PublishedBlogsServiceImpl implements PublishedBlogsService {


    private int maxBlogs;

    @Activate
    @Modified
    protected void activate(PublishedBlogsConfig config) {
        this.maxBlogs = config.maxBlogs();

    }

    @Override
    public int getMaxBlogs() {
        return maxBlogs;
    }
}
