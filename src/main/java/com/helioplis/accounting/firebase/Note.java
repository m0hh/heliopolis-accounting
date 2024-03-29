package com.helioplis.accounting.firebase;

import lombok.Data;

import java.util.Map;

@Data
public class Note {
    private String subject;
    private String content;
    private Map<String, String> data;
    private String image;

    public Note(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}