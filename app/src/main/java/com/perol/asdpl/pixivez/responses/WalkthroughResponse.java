package com.perol.asdpl.pixivez.responses;

import java.util.List;

public class WalkthroughResponse {


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


}
