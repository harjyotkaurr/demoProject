package com.blog.page.core.models;

import java.util.List;
import java.util.Map;

public interface FooterModel {
//    String getExtraText();
    String getCopyrightText();
    List<Map<String, String>> getFooterLinks();
}
