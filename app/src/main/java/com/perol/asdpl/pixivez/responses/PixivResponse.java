package com.perol.asdpl.pixivez.responses;

import java.util.List;

/**
 * Created by asdpl on 2018/2/18.
 */

public class PixivResponse {

    private List<String> search_auto_complete_keywords;

    public List<String> getSearch_auto_complete_keywords() {
        return search_auto_complete_keywords;
    }

    public void setSearch_auto_complete_keywords(List<String> search_auto_complete_keywords) {
        this.search_auto_complete_keywords = search_auto_complete_keywords;
    }
}
