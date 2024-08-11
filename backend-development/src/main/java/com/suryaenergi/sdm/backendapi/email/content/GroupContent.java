package com.suryaenergi.sdm.backendapi.email.content;

import com.suryaenergi.sdm.backendapi.email.EmailContent;

import java.util.List;

public class GroupContent implements EmailContent {
    private List<EmailContent> children;

    public GroupContent(List<EmailContent> children) {
        this.children = children;
    }

    @Override
    public void build(StringBuilder builder) {
        if (children == null) {
            return;
        }
        for (EmailContent child : children) {
            if (child == null) {
                continue;
            }
            child.build(builder);
        }
    }
}
