package com.ncu.collage.dto;

import jakarta.validation.constraints.Size;

public class UpdatePostRequest {
    @Size(min=5, max=200)
    private String title;
    private String content;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
