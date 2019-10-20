/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.responses;

import java.io.Serializable;


public class IllustDetailResponse implements Serializable {


    /**
     * illust : {"id":67261030,"title":"吃狗粮的日子到了","type":"illust","image_urls":{"square_medium":"https://i.pximg.net/c/360x360_70/img-master/img/2018/02/14/01/02/59/67261030_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2018/02/14/01/02/59/67261030_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90/img-master/img/2018/02/14/01/02/59/67261030_p0_master1200.jpg"},"caption":"你们快乐，我继续肝崩崩崩","restrict":0,"user":{"id":24087148,"name":"脸黑の零氪渣","account":"zeng_yu","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/08/04/20/04/06/12978904_75e510554696aaa9f228cf94736b57e9_170.gif"},"is_followed":true},"tags":[{"name":"崩坏3"},{"name":"崩坏3rd"},{"name":"德莉莎"},{"name":"情人节"},{"name":"崩壊3rd"}],"tools":["SAI"],"create_date":"2018-02-14T01:02:59+09:00","page_count":1,"width":1200,"height":1600,"sanity_level":2,"series":null,"meta_single_page":{"original_image_url":"https://i.pximg.net/img-original/img/2018/02/14/01/02/59/67261030_p0.jpg"},"meta_pages":[],"total_view":884,"total_bookmarks":91,"is_bookmarked":false,"visible":true,"is_muted":false,"total_comments":3}
     */

    private Illust illust;

    public Illust getIllust() {
        return illust;
    }

    public void setIllust(Illust illust) {
        this.illust = illust;
    }


}
