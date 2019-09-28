package com.perol.asdpl.pixivez.responses;

import java.io.Serializable;

public class UserBean implements Serializable{
    private Long id;
    private String name;
    private String account;
    private ProfileImageUrlsBean profile_image_urls;
    private String comment;
    private boolean is_followed;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public ProfileImageUrlsBean getProfile_image_urls() {
        return profile_image_urls;
    }

    public void setProfile_image_urls(ProfileImageUrlsBean profile_image_urls) {
        this.profile_image_urls = profile_image_urls;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isIs_followed() {
        return is_followed;
    }

    public void setIs_followed(boolean is_followed) {
        this.is_followed = is_followed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static class ProfileImageUrlsBean implements Serializable {
        /**
         * medium : https://i.pximg.net/user-profile/img/2018/08/18/13/33/23/14652189_7fc9018d975d494657755b007d178a3f_170.jpg
         */

        private String medium;

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }
}
