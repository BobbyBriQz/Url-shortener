package cc.lnkd.urlshortener.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {

    boolean status;
    String message;
    Object data;
}
