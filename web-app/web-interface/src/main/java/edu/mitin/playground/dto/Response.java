package edu.mitin.playground.dto;

import java.io.Serializable;

public class Response implements Serializable {
    Boolean success;
    String description;

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", description='" + description + '\'' +
                '}';
    }

    public Response(Boolean success, String description) {
        this.success = success;
        this.description = description;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
