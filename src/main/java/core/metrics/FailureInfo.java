package core.metrics;

public class FailureInfo {

    private String testName;
    private String errorMessage;
    private String stackTrace;   // Optional but powerful

    public FailureInfo() {}

    public FailureInfo(String testName,
                       String errorMessage,
                       String stackTrace) {
        this.testName = testName;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
    }

    public String getTestName() {
        return testName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }
}