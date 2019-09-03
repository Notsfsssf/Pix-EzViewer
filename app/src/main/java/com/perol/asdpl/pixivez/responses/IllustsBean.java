package com.perol.asdpl.pixivez.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by asdpl on 2018/2/18.
 */
public class IllustsBean implements Serializable {
    private Long id;
    private String title;
    private String type;
    private ImageUrlsBean image_urls;
    private String caption;
    private int restrict;
    private UserBean user;
    private String create_date;
    private int page_count;
    private int width;
    private int height;
    private int sanity_level;
    private Object series;
    private MetaSinglePageBean meta_single_page;
    private int total_view;
    private int total_bookmarks;
    private boolean is_bookmarked;
    private boolean visible;
    private boolean is_muted;
    private List<Tags> tags;
    private List<?> tools;
    private List<MetaPagesBean> meta_pages;




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ImageUrlsBean getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(ImageUrlsBean image_urls) {
        this.image_urls = image_urls;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getRestrict() {
        return restrict;
    }

    public void setRestrict(int restrict) {
        this.restrict = restrict;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSanity_level() {
        return sanity_level;
    }

    public void setSanity_level(int sanity_level) {
        this.sanity_level = sanity_level;
    }

    public Object getSeries() {
        return series;
    }

    public void setSeries(Object series) {
        this.series = series;
    }

    public MetaSinglePageBean getMeta_single_page() {
        return meta_single_page;
    }

    public void setMeta_single_page(MetaSinglePageBean meta_single_page) {
        this.meta_single_page = meta_single_page;
    }

    public int getTotal_view() {
        return total_view;
    }

    public void setTotal_view(int total_view) {
        this.total_view = total_view;
    }

    public int getTotal_bookmarks() {
        return total_bookmarks;
    }

    public void setTotal_bookmarks(int total_bookmarks) {
        this.total_bookmarks = total_bookmarks;
    }

    public boolean isIs_bookmarked() {
        return is_bookmarked;
    }

    public void setIs_bookmarked(boolean is_bookmarked) {
        this.is_bookmarked = is_bookmarked;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isIs_muted() {
        return is_muted;
    }

    public void setIs_muted(boolean is_muted) {
        this.is_muted = is_muted;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public List<?> getTools() {
        return tools;
    }

    public void setTools(List<?> tools) {
        this.tools = tools;
    }

    public List<MetaPagesBean> getMeta_pages() {
        return meta_pages;
    }

    public void setMeta_pages(List<MetaPagesBean> meta_pages) {
        this.meta_pages = meta_pages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public static class ImageUrlsBean implements Serializable {
        /**
         * square_medium : https://i.pximg.net/c/360x360_70/img-master/img/2018/02/11/00/05/32/67207120_p0_square1200.jpg
         * medium : https://i.pximg.net/c/540x540_70/img-master/img/2018/02/11/00/05/32/67207120_p0_master1200.jpg
         * large : https://i.pximg.net/c/600x1200_90/img-master/img/2018/02/11/00/05/32/67207120_p0_master1200.jpg
         */

        private String square_medium;
        private String medium;
        private String large;

        public String getSquare_medium() {
            return square_medium;
        }

        public void setSquare_medium(String square_medium) {
            this.square_medium = square_medium;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
    }



    public static class MetaSinglePageBean  implements Serializable{
        /**
         * original_image_url : https://i.pximg.net/img-original/img/2018/02/11/00/05/32/67207120_p0.jpg
         */

        private String original_image_url;

        public String getOriginal_image_url() {
            return original_image_url;
        }

        public void setOriginal_image_url(String original_image_url) {
            this.original_image_url = original_image_url;
        }
    }
    public static class MetaPagesBean implements Serializable{
        /**
         * image_urls : {"square_medium":"https://i.pximg.net/c/360x360_70/img-master/img/2017/01/03/18/12/50/60743858_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2017/01/03/18/12/50/60743858_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90/img-master/img/2017/01/03/18/12/50/60743858_p0_master1200.jpg","original":"https://i.pximg.net/img-original/img/2017/01/03/18/12/50/60743858_p0.jpg"}
         */

        @SerializedName("image_urls")
        private ImageUrlsBean image_urlsX;

        public ImageUrlsBean getImage_urlsX() {
            return image_urlsX;
        }

        public void setImage_urlsX(ImageUrlsBean image_urlsX) {
            this.image_urlsX = image_urlsX;
        }

        public static class ImageUrlsBean implements Serializable {
            /**
             * square_medium : https://i.pximg.net/c/360x360_70/img-master/img/2017/01/03/18/12/50/60743858_p0_square1200.jpg
             * medium : https://i.pximg.net/c/540x540_70/img-master/img/2017/01/03/18/12/50/60743858_p0_master1200.jpg
             * large : https://i.pximg.net/c/600x1200_90/img-master/img/2017/01/03/18/12/50/60743858_p0_master1200.jpg
             * original : https://i.pximg.net/img-original/img/2017/01/03/18/12/50/60743858_p0.jpg
             */

            private String square_medium;
            private String medium;
            private String large;
            private String original;

            public String getSquare_medium() {
                return square_medium;
            }

            public void setSquare_medium(String square_medium) {
                this.square_medium = square_medium;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getOriginal() {
                return original;
            }

            public void setOriginal(String original) {
                this.original = original;
            }
        }
    }
}
