package com.blog.page.core.util;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

public class Util {
    private static final String DEFAULT_IMAGE = "/content/dam/default.jpg";
    public static String getImagePath(Page childPage) {
        String imagePath = DEFAULT_IMAGE;
        Resource imageResource = childPage.getContentResource("root/responsivegrid/image/file/jcr:content");

        if (imageResource != null) {
            ValueMap properties = imageResource.getValueMap();
            if (properties.containsKey("jcr:data")) { // Check if binary data exists
                imagePath = childPage.getPath() + "/jcr:content/root/responsivegrid/image/file";
            }
        }

        return imagePath;
    }
}
