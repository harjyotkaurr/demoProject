package com.blog.page.core.models.impl;

import com.blog.page.core.models.BlogPost;
import com.blog.page.core.models.PublishedBlogsModel;
import com.blog.page.core.services.PublishedBlogsService;
import com.blog.page.core.util.Util;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = PublishedBlogsModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PublishedBlogsModelImpl implements PublishedBlogsModel {

    private static final Logger LOG = LoggerFactory.getLogger(PublishedBlogsModelImpl.class);
    private static final String DEFAULT_IMAGE = "/content/dam/default.jpg";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
    private static final SimpleDateFormat REQUEST_DATE_FORMAT = new SimpleDateFormat("MM-yyyy", Locale.ENGLISH);

    @ScriptVariable
    private PageManager pageManager;

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private PublishedBlogsService publishedBlogsService;

    @ValueMapValue
    private String parentPath;

    private List<BlogPost> blogs;

    @PostConstruct
    protected void init() {
        blogs = new ArrayList<>();
        Page currentPage = pageManager.getPage(parentPath);

        if (currentPage == null) {
            LOG.error("Current page not found!");
            return;
        }

        int maxBlogs = publishedBlogsService.getMaxBlogs();
        int count=0;
        LOG.info("Fetching a maximum of {} blogs", maxBlogs);

        Iterator<Page> children = currentPage.listChildren();
        String paramDate = request.getParameter("month");
        boolean filteredDate = paramDate != null;

        while (children.hasNext() && count < maxBlogs) {
            Page blogPage = children.next();
            String title = blogPage.getTitle();
            String description = blogPage.getProperties().get("jcr:description", String.class);
            String image = getImagePath(blogPage);
            String link = blogPage.getPath() + ".html";
            String date = getFormattedDate(blogPage);

            if (filteredDate && !matchesRequestedDate(date, paramDate)) {
                continue;
            }

            if (title != null || description != null || image != null) {
                blogs.add(new BlogPost(title, description, image, link, date));
                count++;
            }
        }

        LOG.info("Fetched {} blogs", blogs.size());
    }

    private boolean matchesRequestedDate(String formattedDate, String requestDate) {
        try {
            Date blogDate = DATE_FORMAT.parse(formattedDate); //mmm dd yyyy
            String blogMonthYear = REQUEST_DATE_FORMAT.format(blogDate); //mm yyyy

            LOG.debug("Comparing blog date: {} with requested date: {}", blogMonthYear, requestDate);
            return blogMonthYear.equals(requestDate);
        } catch (ParseException e) {
            LOG.error("Error parsing blog date: {}", formattedDate, e);
            return false;
        }
    }



    private String getImagePath(Page childPage) {
        String imagePath = DEFAULT_IMAGE;
        Resource imageResource = childPage.getContentResource("root/container/responsivegrid/image/file/jcr:content");

        if (imageResource != null) {
            ValueMap properties = imageResource.getValueMap();
            if (properties.containsKey("jcr:data")) {
                imagePath = childPage.getPath() + "/jcr:content/root/container/responsivegrid/image/file";
                LOG.debug("Image found at: {}", imagePath);
            } else {
                LOG.warn("No image found under root/container/responsivegrid/image/file/jcr:content for page: {}", childPage.getPath());
            }
        } else {
            LOG.warn("Image resource not found for page: {}", childPage.getPath());
        }

        return imagePath;
    }

    private String getFormattedDate(Page childPage) {
        Calendar createdDateCal = childPage.getProperties().get("jcr:created", Calendar.class);
        if (createdDateCal != null) {
            String formattedDate = DATE_FORMAT.format(createdDateCal.getTime());
            LOG.debug("Formatted date for page {}: {}", childPage.getPath(), formattedDate);
            return formattedDate;
        }
        return "N/A";
     }


    @Override
    public List<BlogPost> getBlogs() {
        return blogs;
    }
}
