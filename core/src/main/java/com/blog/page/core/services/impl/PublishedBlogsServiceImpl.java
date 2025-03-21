package com.blog.page.core.services.impl;

import com.blog.page.core.config.PublishedBlogsConfig;
import com.blog.page.core.services.PublishedBlogsService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = PublishedBlogsService.class, immediate = true)
@Designate(ocd = PublishedBlogsConfig.class)
public class PublishedBlogsServiceImpl implements PublishedBlogsService {

    private static final Logger LOG = LoggerFactory.getLogger(PublishedBlogsServiceImpl.class);

    private int maxBlogs;

    @Activate
    @Modified
    protected void activate(PublishedBlogsConfig config) {
        this.maxBlogs = config.maxBlogs();
        LOG.info("Published Blogs Configuration updated: maxBlogs = {}", maxBlogs);
    }

    @Override
    public int getMaxBlogs() {
        return maxBlogs;
    }
}
