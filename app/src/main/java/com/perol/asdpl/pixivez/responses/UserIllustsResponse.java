package com.perol.asdpl.pixivez.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by asdpl on 2018/2/21.
 */

public class UserIllustsResponse implements Serializable {

    /**
     * illusts : [{"id":56585648,"title":"-残月桥-","type":"illust","image_urls":{"square_medium":"https://i.pximg.net/c/360x360_70/img-master/img/2016/04/29/03/33/27/56585648_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2016/04/29/03/33/27/56585648_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90/img-master/img/2016/04/29/03/33/27/56585648_p0_master1200.jpg"},"caption":"","restrict":0,"user":{"id":660788,"name":"swd3e2","account":"swd3e2","profile_image_urls":{"medium":"https://i3.pixiv.net/user-profile/img/2016/03/24/22/59/20/10710018_5f0becdb366368a8ffd34ca47839a0a2_170.png"},"is_followed":true},"tags":[{"name":"落書き"},{"name":"pixivファンタジアT"},{"name":"オリジナル"},{"name":"王の間の決戦【青】"},{"name":"タイガ"},{"name":"PFT新津新天地編"},{"name":"新津百景"},{"name":"ピクファン1000users入り"}],"tools":["Photoshop","SAI"],"create_date":"2016-04-29T03:33:27+09:00","page_count":1,"width":1403,"height":702,"sanity_level":2,"meta_single_page":{"original_image_url":"https://i1.pixiv.net/img-original/img/2016/04/29/03/33/27/56585648_p0.jpg"},"meta_pages":[],"total_view":53181,"total_bookmarks":1347,"is_bookmarked":false,"visible":true,"total_comments":21}]
     * next_url : https://app-api.pixiv.net/v1/user/illusts?user_id=660788&filter=for_ios&type=illust&offset=60
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


}
