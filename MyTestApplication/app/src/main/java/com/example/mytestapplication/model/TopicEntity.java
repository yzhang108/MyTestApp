package com.example.mytestapplication.model;

import java.io.Serializable;

/**
 * Created by 张艳 on 2016/9/12.
 */
public class TopicEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private int bookId;
    private String bookName;
    private boolean isSelected=false;

    public TopicEntity(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
