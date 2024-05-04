package com.example.loupgarou;


public class RecyclerData {

    public String password;
    private String name;
    private int imgid;

    private String role;

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name;}
    public int getImgid() {return imgid;}
    public void setImgid(int imgid) {this.imgid = imgid;}
    public String getRole() { return role;}
    public void setRole(String role) { this.role = role;}

    public String getPassword() {return password;}

    public void setPassword(String password) { this.password = password;}

    public RecyclerData(String name, int imgid) {
        this.name = name;
        this.imgid = imgid;
    }
    public RecyclerData(String name,int imgid, String role) {
        this(name,imgid);
        this.role = role;
    }
}
