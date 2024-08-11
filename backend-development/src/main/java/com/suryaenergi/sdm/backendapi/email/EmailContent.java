package com.suryaenergi.sdm.backendapi.email;

import com.suryaenergi.sdm.backendapi.email.content.StyleContent;

public interface EmailContent {
    void build(StringBuilder builder);
    default StyleContent style() {
        return new StyleContent(this);
    }
}
