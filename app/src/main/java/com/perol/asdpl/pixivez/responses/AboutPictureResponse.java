package com.perol.asdpl.pixivez.responses;

import java.util.List;

/**
 * Created by asdpl on 2018/2/13.
 */

public class AboutPictureResponse {
    /**
     * illusts : [{"id":50110785,"title":"水着こすみ","type":"illust","image_urls":{"square_medium":"https://i.pximg.net/c/360x360_70/img-master/img/2015/04/30/20/21/08/50110785_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2015/04/30/20/21/08/50110785_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90/img-master/img/2015/04/30/20/21/08/50110785_p0_master1200.jpg"},"caption":"コミ1新刊用のイラストです<br />横乳から脇にかけて塗ってる時間が最高に楽しい","restrict":0,"user":{"id":47406,"name":"煎路（せんじ）","account":"qcb3s9xr","profile_image_urls":{"medium":"https://i1.pixiv.net/img05/profile/qcb3s9xr/9041950.jpg"},"is_followed":false},"tags":[{"name":"オリジナル"},{"name":"孤島ちゃんねる"},{"name":"水着"},{"name":"挟まれたい谷間"},{"name":"女の子"},{"name":"ビキニ"},{"name":"おっぱい"},{"name":"おへそ"},{"name":"金髪"}],"tools":["Photoshop","SAI"],"create_date":"2015-04-30T20:21:08+09:00","page_count":1,"width":567,"height":800,"sanity_level":2,"meta_single_page":{"original_image_url":"https://i2.pixiv.net/img-original/img/2015/04/30/20/21/08/50110785_p0.jpg"},"meta_pages":[],"total_view":38050,"total_bookmarks":2728,"is_bookmarked":false,"visible":true}]
     * next_url : https://app-api.pixiv.net/v1/illust/related?illust_id=57065990&filter=for_ios&seed_illust_ids=53870257%2C54106899%2C57575769%2C50570076%2C56561969%2C56155587%2C52449428%2C57150551%2C53420776%2C57012459%2C57728098%2C57065617%2C57284644%2C46955238%2C50110785%2C56175842%2C56557178%2C57698476%2C55716084%2C57798791
     */

    private String next_url;
    private List<IllustsBean> illusts;

    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public List<IllustsBean> getIllusts() {
        return illusts;
    }

    public void setIllusts(List<IllustsBean> illusts) {
        this.illusts = illusts;
    }

//    public static class IllustsBean {
//        /**
//         * id : 50110785
//         * title : 水着こすみ
//         * type : illust
//         * image_urls : {"square_medium":"https://i.pximg.net/c/360x360_70/img-master/img/2015/04/30/20/21/08/50110785_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2015/04/30/20/21/08/50110785_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90/img-master/img/2015/04/30/20/21/08/50110785_p0_master1200.jpg"}
//         * caption : コミ1新刊用のイラストです<br />横乳から脇にかけて塗ってる時間が最高に楽しい
//         * restrict : 0
//         * user : {"id":47406,"name":"煎路（せんじ）","account":"qcb3s9xr","profile_image_urls":{"medium":"https://i1.pixiv.net/img05/profile/qcb3s9xr/9041950.jpg"},"is_followed":false}
//         * tags : [{"name":"オリジナル"},{"name":"孤島ちゃんねる"},{"name":"水着"},{"name":"挟まれたい谷間"},{"name":"女の子"},{"name":"ビキニ"},{"name":"おっぱい"},{"name":"おへそ"},{"name":"金髪"}]
//         * tools : ["Photoshop","SAI"]
//         * create_date : 2015-04-30T20:21:08+09:00
//         * page_count : 1
//         * width : 567
//         * height : 800
//         * sanity_level : 2
//         * meta_single_page : {"original_image_url":"https://i2.pixiv.net/img-original/img/2015/04/30/20/21/08/50110785_p0.jpg"}
//         * meta_pages : []
//         * total_view : 38050
//         * total_bookmarks : 2728
//         * is_bookmarked : false
//         * visible : true
//         */
//
//        private int id;
//        private String title;
//        private String type;
//        private ImageUrlsBean image_urls;
//        private String caption;
//        private int restrict;
//        private UserBean user;
//        private String create_date;
//        private int page_count;
//        private int width;
//        private int height;
//        private int sanity_level;
//        private MetaSinglePageBean meta_single_page;
//        private int total_view;
//        private int total_bookmarks;
//        private boolean is_bookmarked;
//        private boolean visible;
//        private List<TagsBean> tags;
//        private List<String> tools;
//        private List<?> meta_pages;
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public ImageUrlsBean getImage_urls() {
//            return image_urls;
//        }
//
//        public void setImage_urls(ImageUrlsBean image_urls) {
//            this.image_urls = image_urls;
//        }
//
//        public String getCaption() {
//            return caption;
//        }
//
//        public void setCaption(String caption) {
//            this.caption = caption;
//        }
//
//        public int getRestrict() {
//            return restrict;
//        }
//
//        public void setRestrict(int restrict) {
//            this.restrict = restrict;
//        }
//
//        public UserBean getUser() {
//            return user;
//        }
//
//        public void setUser(UserBean user) {
//            this.user = user;
//        }
//
//        public String getCreate_date() {
//            return create_date;
//        }
//
//        public void setCreate_date(String create_date) {
//            this.create_date = create_date;
//        }
//
//        public int getPage_count() {
//            return page_count;
//        }
//
//        public void setPage_count(int page_count) {
//            this.page_count = page_count;
//        }
//
//        public int getWidth() {
//            return width;
//        }
//
//        public void setWidth(int width) {
//            this.width = width;
//        }
//
//        public int getHeight() {
//            return height;
//        }
//
//        public void setHeight(int height) {
//            this.height = height;
//        }
//
//        public int getSanity_level() {
//            return sanity_level;
//        }
//
//        public void setSanity_level(int sanity_level) {
//            this.sanity_level = sanity_level;
//        }
//
//        public MetaSinglePageBean getMeta_single_page() {
//            return meta_single_page;
//        }
//
//        public void setMeta_single_page(MetaSinglePageBean meta_single_page) {
//            this.meta_single_page = meta_single_page;
//        }
//
//        public int getTotal_view() {
//            return total_view;
//        }
//
//        public void setTotal_view(int total_view) {
//            this.total_view = total_view;
//        }
//
//        public int getTotal_bookmarks() {
//            return total_bookmarks;
//        }
//
//        public void setTotal_bookmarks(int total_bookmarks) {
//            this.total_bookmarks = total_bookmarks;
//        }
//
//        public boolean isIs_bookmarked() {
//            return is_bookmarked;
//        }
//
//        public void setIs_bookmarked(boolean is_bookmarked) {
//            this.is_bookmarked = is_bookmarked;
//        }
//
//        public boolean isVisible() {
//            return visible;
//        }
//
//        public void setVisible(boolean visible) {
//            this.visible = visible;
//        }
//
//        public List<TagsBean> getTags() {
//            return tags;
//        }
//
//        public void setTags(List<TagsBean> tags) {
//            this.tags = tags;
//        }
//
//        public List<String> getTools() {
//            return tools;
//        }
//
//        public void setTools(List<String> tools) {
//            this.tools = tools;
//        }
//
//        public List<?> getMeta_pages() {
//            return meta_pages;
//        }
//
//        public void setMeta_pages(List<?> meta_pages) {
//            this.meta_pages = meta_pages;
//        }
//
//        public static class ImageUrlsBean {
//            /**
//             * square_medium : https://i.pximg.net/c/360x360_70/img-master/img/2015/04/30/20/21/08/50110785_p0_square1200.jpg
//             * medium : https://i.pximg.net/c/540x540_70/img-master/img/2015/04/30/20/21/08/50110785_p0_master1200.jpg
//             * large : https://i.pximg.net/c/600x1200_90/img-master/img/2015/04/30/20/21/08/50110785_p0_master1200.jpg
//             */
//
//            private String square_medium;
//            private String medium;
//            private String large;
//
//            public String getSquare_medium() {
//                return square_medium;
//            }
//
//            public void setSquare_medium(String square_medium) {
//                this.square_medium = square_medium;
//            }
//
//            public String getMedium() {
//                return medium;
//            }
//
//            public void setMedium(String medium) {
//                this.medium = medium;
//            }
//
//            public String getLarge() {
//                return large;
//            }
//
//            public void setLarge(String large) {
//                this.large = large;
//            }
//        }
//
//        public static class UserBean {
//            /**
//             * id : 47406
//             * name : 煎路（せんじ）
//             * account : qcb3s9xr
//             * profile_image_urls : {"medium":"https://i1.pixiv.net/img05/profile/qcb3s9xr/9041950.jpg"}
//             * is_followed : false
//             */
//
//            private int id;
//            private String name;
//            private String account;
//            private ProfileImageUrlsBean profile_image_urls;
//            private boolean is_followed;
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//
//            public String getAccount() {
//                return account;
//            }
//
//            public void setAccount(String account) {
//                this.account = account;
//            }
//
//            public ProfileImageUrlsBean getProfile_image_urls() {
//                return profile_image_urls;
//            }
//
//            public void setProfile_image_urls(ProfileImageUrlsBean profile_image_urls) {
//                this.profile_image_urls = profile_image_urls;
//            }
//
//            public boolean isIs_followed() {
//                return is_followed;
//            }
//
//            public void setIs_followed(boolean is_followed) {
//                this.is_followed = is_followed;
//            }
//
//            public static class ProfileImageUrlsBean {
//                /**
//                 * medium : https://i1.pixiv.net/img05/profile/qcb3s9xr/9041950.jpg
//                 */
//
//                private String medium;
//
//                public String getMedium() {
//                    return medium;
//                }
//
//                public void setMedium(String medium) {
//                    this.medium = medium;
//                }
//            }
//        }
//
//        public static class MetaSinglePageBean {
//            /**
//             * original_image_url : https://i2.pixiv.net/img-original/img/2015/04/30/20/21/08/50110785_p0.jpg
//             */
//
//            private String original_image_url;
//
//            public String getOriginal_image_url() {
//                return original_image_url;
//            }
//
//            public void setOriginal_image_url(String original_image_url) {
//                this.original_image_url = original_image_url;
//            }
//        }
//
//        public static class TagsBean {
//            /**
//             * name : オリジナル
//             */
//
//            private String name;
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//        }
//    }
}
