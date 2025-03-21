package com.blog.page.core.models.impl;

import com.blog.page.core.models.FooterModel;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

@Model(adaptables = Resource.class,
        adapters = FooterModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterModelImpl implements FooterModel {



    @SlingObject
    private Resource resource;

    @ValueMapValue
    private String copyrightText;

//    @ValueMapValue
//    private String extraText;

//    @ChildResource(name = "footerLinks")
//    private Resource footerLinks;

    @Override
    public String getCopyrightText() {

        return copyrightText;
    }

//    @Override
//    public String getExtraText() {
//
//        return extraText;
//    }

    @Override
    public List<Map<String, String>> getFooterLinks() {
        List<Map<String, String>> footerItems = new ArrayList<>();
        Resource footerLinks = resource.getChild("footerLinks");

        if (footerLinks != null) {
            for (Resource linkResource : footerLinks.getChildren()) {
                ValueMap valueMap = linkResource.getValueMap();
                String text = valueMap.get("text", String.class);
                String link = valueMap.get("link", String.class);

                if (text != null && link != null) {
                    Map<String, String> itemMap = new HashMap<>();
                    itemMap.put("text", text);
                    itemMap.put("link", link + ".html"); // Ensure .html extension
                    footerItems.add(itemMap);
                }
            }
        }
        return footerItems;
    }


}





