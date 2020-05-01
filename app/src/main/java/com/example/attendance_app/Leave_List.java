package com.example.attendance_app;

public class Leave_List {

    private String AppliedDate;
    private String TlApproval;
    private String HrApproval;

    public Leave_List(){}

    public Leave_List(String AppliedDate, String TlApproval, String HrApproval){
        this.AppliedDate = AppliedDate;
        this.TlApproval = TlApproval;
        this.HrApproval = HrApproval;
    }

    public String getAppliedDate() {
        return AppliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        AppliedDate = appliedDate;
    }

    public String getTlApproval() {
        return TlApproval;
    }

    public void setTlApproval(String tlApproval) {
        TlApproval = tlApproval;
    }

    public String getHrApproval() {
        return HrApproval;
    }

    public void setHrApproval(String hrApproval) {
        HrApproval = hrApproval;
    }
}
