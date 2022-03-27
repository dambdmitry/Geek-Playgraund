package edu.mitin.verification.model;

public class VerificationResult {
    public enum Result{
        SUCCESS,
        FAILURE
    }

    String description;
    Result result;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public VerificationResult(String description, Result result) {
        this.description = description;
        this.result = result;
    }
}
