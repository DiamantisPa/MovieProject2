package com.example.ryzen.movieproject;

/**
 * Created by Ryzen on 4/23/2018.
 */

public class Reviews {

    private String author;
    private String content;

    public Reviews (String author, String content) {

        this.author = author;
        this.content = content;
    }

    public String getAuthor () {return author;}

    public void setAuthor () {this.author = author;}

    public String getContent () {return content;}

    public void setContent () {this.content = content;}
}
