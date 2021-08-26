package com.example.startnews;

public  class News {
    private String Image;
    private String Author;
    private String title;
    private String description;
    private String url;



    public News(String image, String author, String title, String description, String url) {
        Image = image;
        Author = author;
        this.title = title;
        this.description = description;
        this.url=url;

    }

    public String getImage() {
        return Image;
    }

    public String getAuthor() {
        return Author;
    }



    public String getTitle() {
        return title;
    }



    public String getDescription() {
        return description;
    }


    public String getUrl() {
        return url;
    }



}
