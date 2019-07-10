package com.perol.asdpl.pixivez.objects;

public class Spotlight {
    private String title;
    private String username;
    private String userpic;
    private String pictureurl;
    private String illustrateid;
private Long userid;
    public Spotlight(String title, String username, String userpic, String pictureurl, String illustrateid,Long userid) {
        this.title = title;
        this.username = username;
        this.userpic = userpic;
        this.pictureurl = pictureurl;
        this.illustrateid = illustrateid;
        this.userid=userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getPictureurl() {
        return pictureurl;
    }

    public void setPictureurl(String pictureurl) {
        this.pictureurl = pictureurl;
    }

    public String getIllustrateid() {
        return illustrateid;
    }

    public void setIllustrateid(String illustrateid) {
        this.illustrateid = illustrateid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
