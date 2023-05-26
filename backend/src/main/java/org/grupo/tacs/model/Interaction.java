package org.grupo.tacs.model;

public class Interaction {
    String url;
    InteractionMethod method;
    String description;

    int statusCode;

    public Interaction(InteractionMethod method, String url,String description, int code){
        this.method = method;
        this.url = url;
        this.description = description;
        this.statusCode = code;
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
}
