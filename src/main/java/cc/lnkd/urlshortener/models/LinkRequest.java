package cc.lnkd.urlshortener.models;

import lombok.Data;

@Data
public class LinkRequest {
    int slugLength;
    String slug;
    String url;
}
