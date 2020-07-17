package Admin.pending;

import Admin.Admin;

public class model_class {
    private String name;
    private String email;
    private String labNo;

    public model_class(){}

    public model_class(String name, String email, String labNo) {
        this.name = name;
        this.email = email;
        this.labNo = labNo;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLabNo() {
        return labNo;
    }
}
