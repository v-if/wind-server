package com.github.tkpark.orders;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

public class ReviewRequest {

    @NotBlank(message = "content must be provided")
    private String content;

    protected ReviewRequest() {/*empty*/}

    public ReviewRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("content", content)
                .toString();
    }

}