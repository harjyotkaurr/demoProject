package com.blog.page.core.models;

import java.util.List;
import java.util.Map;

public interface ArchiveModel {
    List<Map<String, String>> getArchivedBlogs();
}