package com.example.pickinglist;

import java.util.ArrayList;
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

    public ArrayList<Article> getArticles() {
        return articles;
    }
    public String getSectionName() {
        return sectionName;
    }
    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public void orderSection()
    {
            Queue<PriorityQueueClass> queue = new PriorityQueue<>();
            ArrayList<PriorityQueueClass> storageUnits = new ArrayList();

            for (Article a : this.articles)
            {
                String locationName = a.getLocation().getStorageUnit(1);
                if(locationName.isEmpty())
                    locationName = "Z";

                Integer i = PriorityQueueClass.Contains(locationName, storageUnits);
                if( i >= 0)
                    storageUnits.get(i).getArticles().add(a.getId());
                else
                {
                    PriorityQueueClass p = new PriorityQueueClass(locationName, new ArrayList<Long>());
                    p.getArticles().add(a.getId());
                    PriorityQueueClass.Add(p, storageUnits);
                }
            }

            ArrayList<Article> orderedArticles = new ArrayList<>();
            for (PriorityQueueClass current: storageUnits)
            {
                orderedArticles.addAll(current.getArticlesById(articles));
            }
            this.articles = orderedArticles;
    }

    public static class PriorityQueueClass {
        String key;
        ArrayList<Long> articles;

        public PriorityQueueClass(){}
        public PriorityQueueClass(String name, ArrayList<Long> articles){
            this.key = name;
            this.articles = articles;
        }

        public ArrayList<Long> getArticles() {
            return articles;
        }

        static public Integer Contains(String key, ArrayList<PriorityQueueClass> queue)
        {
            for (int i = 0; i < queue.size(); i++)
                if(queue.get(i).key.compareTo(key) == 0)
                    return  i;
            return  -1;
        }

        static public void Add (PriorityQueueClass element, ArrayList<PriorityQueueClass> list)
        {
            Integer i = -1;
            while (list.get(i+i).key.compareTo(element.key) < 0) {
                i++;
            }

            PriorityQueueClass last = list.get( list.size() - 1);//ultimo
            PriorityQueueClass tmp;
            for (int j = list.size() - 1; j >= i ; j--) {
                tmp = list.get(j - 1);
                list.set(j, tmp);
            }
            list.add(i, element);
            list.add(last);
        }

        public ArrayList<Article> getArticlesById(ArrayList<Article> articles) {

            ArrayList<Article> articlesToReturn = new ArrayList<>();
            for ( Article a : articles) {
                if(this.articles.contains(a.getId()))
                    articlesToReturn.add(a);
            }

            return articlesToReturn;
        }
    }
}
