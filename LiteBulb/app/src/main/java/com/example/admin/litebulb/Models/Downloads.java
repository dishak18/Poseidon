package com.example.admin.litebulb.Models;

public class Downloads{
    private String item_name, download_link, license_name;
    private int votes, item_id, user_id;
    private String thumbnail;
    public Downloads(){

    }
    public Downloads(int item_id, int user_id, String item_name, int votes, String thumbnail, String download_link, String license_name) {
        this.item_name = item_name;
        this.item_id=item_id;
        this.user_id=user_id;
        this.download_link=download_link;
        this.license_name=license_name;
        this.votes = votes;
        this.thumbnail = thumbnail;
    }

    public String getItemName() {
        return item_name;
    }

    public void setItemName(String item_name) {
        this.item_name = item_name;
    }

    public int getItemId() {
        return item_id;
    }

    public void setItemId(int item_id) {
        this.item_id = item_id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }
    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }


    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDownload_link() {
        return download_link;
    }
    public void setDownload_link(String download_link) {
        this.download_link = download_link;
    }

    public String getLicense_name() {
        return license_name;
    }
    public void setLicense_name(String license_name) {
        this.license_name = license_name;
    }

}
