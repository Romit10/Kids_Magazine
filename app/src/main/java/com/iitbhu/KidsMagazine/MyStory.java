package com.iitbhu.KidsMagazine;

public class MyStory {
    String title;
    String story;
    String storyBeng;
    String storyKnd;
    public MyStory() {
    }

    public MyStory(String title, String story,String storyBeng,String storyKnd) {
        this.title = title;
        this.story = story;
        this.storyBeng = storyBeng;
        this.storyKnd = storyKnd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getStoryBeng() {
        return storyBeng;
    }

    public void setStoryBeng(String storyBeng) {
        this.storyBeng = storyBeng;
    }

    public String getStoryKnd() {
        return storyKnd;
    }

    public void setStoryKnd(String storyKnd) {
        this.storyKnd = storyKnd;
    }
}
