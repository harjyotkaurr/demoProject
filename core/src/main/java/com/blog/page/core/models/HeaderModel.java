package com.blog.page.core.models;

import java.util.List;
import java.util.Map;

public interface HeaderModel {
    String getTitle();
    String getLogo();
    List<Map<String,String>> getNavItems();


}
