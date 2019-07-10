package com.perol.asdpl.pixivez.responses;

import java.io.Serializable;

/**
 * Created by Notsfsssf on 2018/4/2.
 */

public class IllustDetailResponse implements Serializable {


    /**
     * illust : {"id":67261030,"title":"吃狗粮的日子到了","type":"illust","image_urls":{"square_medium":"https://i.pximg.net/c/360x360_70/img-master/img/2018/02/14/01/02/59/67261030_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2018/02/14/01/02/59/67261030_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90/img-master/img/2018/02/14/01/02/59/67261030_p0_master1200.jpg"},"caption":"你们快乐，我继续肝崩崩崩","restrict":0,"user":{"id":24087148,"name":"脸黑の零氪渣","account":"zeng_yu","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/08/04/20/04/06/12978904_75e510554696aaa9f228cf94736b57e9_170.gif"},"is_followed":true},"tags":[{"name":"崩坏3"},{"name":"崩坏3rd"},{"name":"德莉莎"},{"name":"情人节"},{"name":"崩壊3rd"}],"tools":["SAI"],"create_date":"2018-02-14T01:02:59+09:00","page_count":1,"width":1200,"height":1600,"sanity_level":2,"series":null,"meta_single_page":{"original_image_url":"https://i.pximg.net/img-original/img/2018/02/14/01/02/59/67261030_p0.jpg"},"meta_pages":[],"total_view":884,"total_bookmarks":91,"is_bookmarked":false,"visible":true,"is_muted":false,"total_comments":3}
     */

    private IllustBean illust;

    public IllustBean getIllust() {
        return illust;
    }

    public void setIllust(IllustBean illust) {
        this.illust = illust;
    }


}
