package com.blog.page.core.models.impl;
import com.adobe.granite.security.user.UserProperties;
import com.adobe.granite.security.user.UserPropertiesManager;
import com.adobe.granite.security.user.UserPropertiesService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.blog.page.core.models.HeadingModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Session;
import java.text.SimpleDateFormat;
import java.util.Date;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = HeadingModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeadingModelImpl implements HeadingModel {

    private static final Logger LOG = LoggerFactory.getLogger(HeadingModelImpl.class);

    @ScriptVariable
    private PageManager pageManager;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Self
    private SlingHttpServletRequest request;

    private String title;
    private String author;
    private String date;

    @PostConstruct
    protected void init() {
        Page currentPage = pageManager.getContainingPage(request.getResource());
        if (currentPage != null) {
            this.title = currentPage.getTitle();
            Date createdDate = currentPage.getProperties().get("jcr:created", Date.class);
            this.date = (createdDate != null) ? formatDate(createdDate) : "No Date Available";
        }
        this.author = getLoggedInUserName();
    }

    private String getLoggedInUserName() {
        try {
            Session session = resourceResolver.adaptTo(Session.class);
            if (session != null) {
                String userId = session.getUserID();
                UserPropertiesManager upm = resourceResolver.adaptTo(UserPropertiesManager.class);
                if (upm != null) {
                    UserProperties userProperties = upm.getUserProperties(userId, UserPropertiesService.PROFILE_PATH);
                    if (userProperties != null) {
                        String fullName = userProperties.getProperty("profile/fullName");
                        return (fullName != null && !fullName.isEmpty()) ? fullName : userId;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error fetching user properties", e);
        }
        return "Unknown User";
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy").format(date);
    }

    @Override
    public String getTitle() {

        return title;
    }

    @Override
    public String getAuthor() {

        return author;
    }

    @Override
    public String getDate() {

        return date;
    }
}