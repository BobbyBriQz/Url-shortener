package cc.lnkd.urlshortener.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkResponse {
    int id;
    String shortUrl;
    String url;
    int visitCount;
}
