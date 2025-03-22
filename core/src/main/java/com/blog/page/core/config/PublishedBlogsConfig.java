package com.blog.page.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Published Blogs Configuration",
        description = "Configuration for no. of published blogs displayed"
)
public @interface PublishedBlogsConfig {

    @AttributeDefinition(
            name = "Maximum Blogs to Display",
            description = "Max number of published blogs to display"
    )
    int maxBlogs() default 5;
}
