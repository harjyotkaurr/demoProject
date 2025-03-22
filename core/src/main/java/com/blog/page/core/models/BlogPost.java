package com.blog.page.core.models;

public class BlogPost {
    private final String title;
    private final String description;
    private final String image;
    private final String link;
    private final String date;

    public BlogPost(String title, String description, String image, String link, String date) {
        this.title = title != null ? title : "";
        this.description = description != null ? description : "";
        this.image = image != null ? image : "";
        this.link = link != null ? link : "";
        this.date = date != null ? date : "N/A";
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getImage() {
        return image;
    }
    public String getLink() {
        return link;
    }
    public String getDate() {
        return date;
    }
}


