package com.github.tkpark.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class UltraSrtFcst {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JacksonXmlRootElement(localName = "response") // 최상위 항목명
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {

        @JacksonXmlProperty(localName = "header")
        Header header;

        @JacksonXmlProperty(localName = "body")
        Body body;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Header {

            @JacksonXmlProperty(localName = "resultCode")
            String resultCode;

            @JacksonXmlProperty(localName = "resultMsg")
            String resultMsg;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Body {

            @JacksonXmlProperty(localName = "dataType")
            String dataType;

            @JacksonXmlProperty(localName = "numOfRows")
            int numOfRows;

            @JacksonXmlProperty(localName = "pageNo")
            int pageNo;

            @JacksonXmlProperty(localName = "totalCount")
            int totalCount;

            @JacksonXmlProperty(localName = "items")
            List<Item> items;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class Item {

                @JacksonXmlProperty(localName = "baseDate")
                String baseDate;

                @JacksonXmlProperty(localName = "baseTime")
                String baseTime;

                @JacksonXmlProperty(localName = "category")
                String category;

                @JacksonXmlProperty(localName = "nx")
                int nx;

                @JacksonXmlProperty(localName = "ny")
                int ny;

                @JacksonXmlProperty(localName = "fcstDate")
                String fcstDate;

                @JacksonXmlProperty(localName = "fcstTime")
                String fcstTime;

                @JacksonXmlProperty(localName = "fcstValue")
                String fcstValue;
            }
        }
    }

}
