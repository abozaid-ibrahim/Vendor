package com.timore.vendor.beanBojo;

import org.parceler.Parcel;

/**
 * Created by usear on 12/1/2015.
 */
@Parcel
public class Notif {
    String type,post,content;
    long parent;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }
}
