package com.perol.asdpl.pixivez.responses;

import java.util.List;

public class BookMarkTagsResponse {

    /**
     * bookmark_tags : [{"name":"*少女前线","count":1},{"name":"002","count":1},{"name":"1000users入り","count":1},{"name":"10点じゃ足りない","count":1},{"name":"2017122638","count":1},{"name":"57","count":3},{"name":"5本指ストッキング","count":1},{"name":"95式","count":2},{"name":"AN-94","count":1},{"name":"AR","count":1},{"name":"AR-15","count":2},{"name":"Blitz","count":1},{"name":"C85","count":1},{"name":"C90","count":1},{"name":"C91","count":1},{"name":"C94","count":1},{"name":"CA","count":1},{"name":"CLIPSTUDIOPAINT","count":5},{"name":"DARLINGintheFRANXX","count":1},{"name":"DQ1000users入り","count":1},{"name":"DT21","count":1},{"name":"DarkSouls3","count":1},{"name":"Devil","count":1},{"name":"Dragunov","count":1},{"name":"Elphelt","count":1},{"name":"FAL","count":3},{"name":"FGO","count":7},{"name":"FGOイラコン2","count":1},{"name":"FNC","count":1},{"name":"Fate","count":2}]
     * next_url : https://app-api.pixiv.net/v1/user/bookmark-tags/illust?user_id=14713395&restrict=public&offset=30
     */

    private String next_url;
    private List<BookmarkTagsBean> bookmark_tags;

    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public List<BookmarkTagsBean> getBookmark_tags() {
        return bookmark_tags;
    }

    public void setBookmark_tags(List<BookmarkTagsBean> bookmark_tags) {
        this.bookmark_tags = bookmark_tags;
    }

    public static class BookmarkTagsBean {
        /**
         * name : *少女前线
         * count : 1
         */

        private String name;
        private int count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
