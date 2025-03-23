package com.blog.page.core.util;

import com.blog.page.core.models.impl.PublishedBlogsModelImpl;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);
    private static final String DEFAULT_IMAGE = "/content/dam/default.jpg";

    public static String getImagePath(SlingHttpServletRequest request, Page page) {
    String ImagePath = page.getPath() + "/jcr:content/cq:featuredimage/file/jcr:content";
    ResourceResolver resolver = request.getResourceResolver();
    Resource ImageNode = resolver.getResource(ImagePath);
    String ImageLink = "";
    try {
        ValueMap properties = ImageNode.getValueMap();
        if (properties.containsKey("jcr:data")) { // Check if binary data exists
            ImageLink = resolver.map(request, ImagePath) + "/jcr:data";
        }
    } catch (Exception e) {
        return "{\"message\" : \" Could not get Banner Image from the Banner Component of Page\"";
    }
    return ImageLink;
}

}
