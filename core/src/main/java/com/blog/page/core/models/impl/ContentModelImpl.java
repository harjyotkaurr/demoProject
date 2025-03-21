package com.blog.page.core.models.impl;

import com.blog.page.core.models.ContentModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = ContentModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContentModelImpl implements ContentModel {

    @ValueMapValue
    private String richText;

    @Override
    public String getRichText() {
        return richText;
    }
}
