package com.ratwareid.sertronik.model;

public class ReportedAccount {
    String userIdReported, userIdReporter, userNameReported, report;

    public ReportedAccount() {
    }

    public ReportedAccount(String userIdReported, String userIdReporter, String userNameReported, String report) {
        this.userIdReported = userIdReported;
        this.userIdReporter = userIdReporter;
        this.userNameReported = userNameReported;
        this.report = report;
    }

    public String getUserIdReported() {
        return userIdReported;
    }

    public void setUserIdReported(String userIdReported) {
        this.userIdReported = userIdReported;
    }

    public String getUserIdReporter() {
        return userIdReporter;
    }

    public void setUserIdReporter(String userIdReporter) {
        this.userIdReporter = userIdReporter;
    }

    public String getUserNameReported() {
        return userNameReported;
    }

    public void setUserNameReported(String userNameReported) {
        this.userNameReported = userNameReported;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
