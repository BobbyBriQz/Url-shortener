package cc.lnkd.urlshortener.models.request;

import lombok.Data;

@Data
public class LinkRequest {
    int slugLength;
    String slug;
    String url;
}
