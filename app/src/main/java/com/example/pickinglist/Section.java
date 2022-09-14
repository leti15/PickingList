package com.example.pickinglist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

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
    public void setArticles(ArrayList<Article> articles) { this.articles = articles; }

    public String getSectionName() {
        return sectionName;
    }

    static public void orderByShelvingUnit(ArrayList<Section> sections)
    {
        for (Section section : sections) {
            ArrayList<Article> articles = section.getArticles();
            Queue<PriorityQueueClass> queue = new PriorityQueue<>();
            for (Article a : articles)
            {
                PriorityQueueClass x = new PriorityQueueClass(a.GetLocation().getShelvingUnit(), a);
                queue.add(x);
            }

            ArrayList<Article> orderedArticles = new ArrayList<>();
            while (!queue.isEmpty())
            {
                PriorityQueueClass currentElement = queue.poll();
                Integer i = articles.indexOf(currentElement.value);
                orderedArticles.add(articles.get(i));
            }
            section.setArticles(orderedArticles);
        }
    }

    public static class PriorityQueueClass {
        Long key;
        Article value;
        public PriorityQueueClass(Long key, Article value){
            this.key = key;
            this.value = value;
        }
    }
}
