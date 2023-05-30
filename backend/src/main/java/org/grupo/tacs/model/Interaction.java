package org.grupo.tacs.model;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interaction {
    String url;
    String urlPattern;
    InteractionMethod method;
    String description;

    LocalDateTime dateTime;
    int statusCode;

    public Interaction(InteractionMethod method, String url,String description, int code){
        this.urlPattern = getUrlPattern(url);
        this.method = method;
        this.url = url;
        this.description = description;
        this.statusCode = code;
    }
    public Interaction(){}

    public String getUrlPattern(String url) {
        String pattern = "https?://[^/]+(/[^?#]+)(\\?[^#]+)?";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(url);

        if (matcher.find()) {
            String path = matcher.group(1);
            String query = matcher.group(2);

            String replacedPath = path.replaceAll("/[0-9a-fA-F]+(?=/|$)", "/:id");

            if (query != null) {
                String replacedQuery = query.replaceAll("=[0-9a-fA-F]+", "=UserId");
                return replacedPath + replacedQuery;
            } else {
                return replacedPath;
            }
        } else {
            return null;
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMethod(InteractionMethod method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public InteractionMethod getMethod() {
        return method;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
