package com.example.pickinglist;

import java.util.ArrayList;
import java.util.List;

public class Section {
    ArrayList<Article> articles;
    String sectionName;
    long sectionId;

    public Section(){}

    public Section(String sectionName, ArrayList<Article> articles)
    {
        this.sectionName = sectionName;
        this.articles = articles;
    }
    public Section(String sectionName, long sectionId, ArrayList<Article> articles)
    {
        this.sectionName = sectionName;
        this.articles = articles;
        this.sectionId = sectionId;
    }

    public long getSectionId() {
        return sectionId;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public String getSectionName() {
        return sectionName;
    }
}
