package com.blog.page.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Published Blogs Configuration",
        description = "Configuration for controlling the number of published blogs displayed"
)
public @interface PublishedBlogsConfig {

    @AttributeDefinition(
            name = "Maximum Blogs to Display",
            description = "Specify the maximum number of published blogs to display"
    )
    int maxBlogs() default 5; // Default is 5 blogs
}
