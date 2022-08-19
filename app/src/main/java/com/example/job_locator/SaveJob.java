package com.example.job_locator;

public class SaveJob {

    String id;
    String idj;
    String uid;
    public SaveJob(){


    }

    public SaveJob(String id, String idj, String uid) {
        this.id = id;
        this.idj = idj;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public String getIdj() {
        return idj;
    }

    public String getUid() {
        return uid;
    }


}
