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
import java.util.List;


public class IllustCommentsResponse implements Serializable {

    /**
     * total_comments : 0
     * comments : [{"id":74774246,"comment":"ty   -w-","date":"2018-01-13T17:21:57+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74770901,"comment":"You painting WA2000 is so cute !","date":"2018-01-13T14:30:17+09:00","user":{"id":4866052,"name":"塩からす","account":"sa1ty9","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2018/01/05/00/35/46/13649713_dac36c983345d7b7946931ee42d4d32d_170.jpg"}}}},{"id":74770901,"comment":"You painting WA2000 is so cute !","date":"2018-01-13T14:30:17+09:00","user":{"id":4866052,"name":"塩からす","account":"sa1ty9","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2018/01/05/00/35/46/13649713_dac36c983345d7b7946931ee42d4d32d_170.jpg"}},"parent_comment":{}},{"id":74662199,"comment":"原来如此！受教了！顺便你老婆真棒！","date":"2018-01-09T10:51:50+09:00","user":{"id":11647632,"name":"墨色幻想","account":"2710012810","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2015/08/31/23/07/37/9827872_970a1eb253251d4b76f5920e90b5e2b8_170.jpg"}},"parent_comment":{"id":74540061,"comment":"這個兄dei可以百度一下哦","date":"2018-01-05T11:27:56+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}}}},{"id":74563797,"comment":"热恋中","date":"2018-01-06T05:09:17+09:00","user":{"id":10797538,"name":"luke-shining","account":"luke-shining","profile_image_urls":{"medium":"https://source.pixiv.net/common/images/no_profile.png"}},"parent_comment":{"id":74539716,"comment":"内个。。戒指为什么。。在中指上。。。","date":"2018-01-05T11:03:52+09:00","user":{"id":11647632,"name":"墨色幻想","account":"2710012810","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2015/08/31/23/07/37/9827872_970a1eb253251d4b76f5920e90b5e2b8_170.jpg"}}}},{"id":74540061,"comment":"這個兄dei可以百度一下哦","date":"2018-01-05T11:27:56+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74539716,"comment":"内个。。戒指为什么。。在中指上。。。","date":"2018-01-05T11:03:52+09:00","user":{"id":11647632,"name":"墨色幻想","account":"2710012810","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2015/08/31/23/07/37/9827872_970a1eb253251d4b76f5920e90b5e2b8_170.jpg"}}}},{"id":74539716,"comment":"内个。。戒指为什么。。在中指上。。。","date":"2018-01-05T11:03:52+09:00","user":{"id":11647632,"name":"墨色幻想","account":"2710012810","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2015/08/31/23/07/37/9827872_970a1eb253251d4b76f5920e90b5e2b8_170.jpg"}},"parent_comment":{}},{"id":74506296,"comment":"2333","date":"2018-01-04T09:07:32+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74496760,"comment":"白学被狗日还行","date":"2018-01-03T23:12:34+09:00","user":{"id":14365251,"name":"UNSC117","account":"445238815","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2018/01/24/00/05/08/13732069_431baefb21fe73881c9233c982b1903f_170.jpg"}}}},{"id":74504177,"comment":"好猥琐的二哈","date":"2018-01-04T05:43:46+09:00","user":{"id":7364338,"name":"oberstein","account":"aquiladigenova","profile_image_urls":{"medium":"https://source.pixiv.net/common/images/no_profile.png"}},"parent_comment":{}},{"id":74501897,"comment":"有這個打算","date":"2018-01-04T02:21:21+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74499658,"comment":"下次可以试试白学画成97那样的双马尾也不错","date":"2018-01-04T00:44:11+09:00","user":{"id":13104736,"name":"Rozzary","account":"fake0914","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/03/28/02/44/19/12330956_452c674d80de692279ead05596597338_170.jpg"}}}},{"id":74500171,"comment":"二哈(normal2)","date":"2018-01-04T01:01:18+09:00","user":{"id":7651412,"name":"yumenzou","account":"963869260","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2018/01/30/00/22/35/13756778_7e0b7524491d6f09f00aabed807960d6_170.png"}},"parent_comment":{}},{"id":74499658,"comment":"下次可以试试白学画成97那样的双马尾也不错","date":"2018-01-04T00:44:11+09:00","user":{"id":13104736,"name":"Rozzary","account":"fake0914","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/03/28/02/44/19/12330956_452c674d80de692279ead05596597338_170.jpg"}},"parent_comment":{}},{"id":74496760,"comment":"白学被狗日还行","date":"2018-01-03T23:12:34+09:00","user":{"id":14365251,"name":"UNSC117","account":"445238815","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2018/01/24/00/05/08/13732069_431baefb21fe73881c9233c982b1903f_170.jpg"}},"parent_comment":{}},{"id":74488770,"comment":"我要上車，我要看直播(interesting2)","date":"2018-01-03T18:52:44+09:00","user":{"id":9229724,"name":"考拉扬","account":"it_emperor","profile_image_urls":{"medium":"https://source.pixiv.net/common/images/no_profile.png"}},"parent_comment":{"id":74487935,"comment":"被我啪了有問題嗎(￣▽￣)~*","date":"2018-01-03T18:15:28+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}}}},{"id":74487935,"comment":"被我啪了有問題嗎(￣▽￣)~*","date":"2018-01-03T18:15:28+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74484142,"comment":"這\u2026\u2026感覺白學要被狗啪了","date":"2018-01-03T15:31:42+09:00","user":{"id":9229724,"name":"考拉扬","account":"it_emperor","profile_image_urls":{"medium":"https://source.pixiv.net/common/images/no_profile.png"}}}},{"id":74484142,"comment":"這\u2026\u2026感覺白學要被狗啪了","date":"2018-01-03T15:31:42+09:00","user":{"id":9229724,"name":"考拉扬","account":"it_emperor","profile_image_urls":{"medium":"https://source.pixiv.net/common/images/no_profile.png"}},"parent_comment":{}},{"id":74483512,"comment":"不了，會死人的（激寒）","date":"2018-01-03T15:04:52+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74483050,"comment":"把身子往右妞，脖子向左试试~","date":"2018-01-03T14:44:03+09:00","user":{"id":7719361,"name":"丿晶丶泪","account":"459073305","profile_image_urls":{"medium":"https://source.pixiv.net/common/images/no_profile.png"}}}},{"id":74483497,"comment":"nice","date":"2018-01-03T15:03:49+09:00","user":{"id":13120274,"name":"潺陵侯","account":"1194921083","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/11/26/20/48/23/13498472_2444c24b4a36612baad296add7ecb7e2_170.jpg"}},"parent_comment":{}},{"id":74483050,"comment":"把身子往右妞，脖子向左试试~","date":"2018-01-03T14:44:03+09:00","user":{"id":7719361,"name":"丿晶丶泪","account":"459073305","profile_image_urls":{"medium":"https://source.pixiv.net/common/images/no_profile.png"}},"parent_comment":{"id":74482725,"comment":"我試了一下，貌似扭得到？！","date":"2018-01-03T14:29:37+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}}}},{"id":74482725,"comment":"我試了一下，貌似扭得到？！","date":"2018-01-03T14:29:37+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74482604,"comment":"毕竟是人形，脖子随便扭都蒙大奶","date":"2018-01-03T14:23:31+09:00","user":{"id":7719361,"name":"丿晶丶泪","account":"459073305","profile_image_urls":{"medium":"https://source.pixiv.net/common/images/no_profile.png"}}}},{"id":74482604,"comment":"毕竟是人形，脖子随便扭都蒙大奶","date":"2018-01-03T14:23:31+09:00","user":{"id":7719361,"name":"丿晶丶泪","account":"459073305","profile_image_urls":{"medium":"https://source.pixiv.net/common/images/no_profile.png"}},"parent_comment":{}},{"id":74482192,"comment":"我不會賠錢的（滑稽","date":"2018-01-03T14:01:41+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74480704,"comment":"我剛試著學做那動作⋯現在脖子扭傷了⋯","date":"2018-01-03T13:03:20+09:00","user":{"id":2617896,"name":"Vitas","account":"wingchung529","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/17/02/11/52/13351492_64d32a180ecda999a97961a3b4486f3d_170.jpg"}}}},{"id":74480816,"comment":"是有点，不过下次就有经验了啊 (normal2)","date":"2018-01-03T13:08:09+09:00","user":{"id":9783163,"name":"眼镜才是本体","account":"jxxy","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/04/22/14/45/33/12451859_c1d511c6b7ca0d1011bf361f7137dbe4_170.jpg"}},"parent_comment":{"id":74480365,"comment":"感覺還是畫得有點草 o(TωT)o ","date":"2018-01-03T12:48:59+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}}}},{"id":74480704,"comment":"我剛試著學做那動作⋯現在脖子扭傷了⋯","date":"2018-01-03T13:03:20+09:00","user":{"id":2617896,"name":"Vitas","account":"wingchung529","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/17/02/11/52/13351492_64d32a180ecda999a97961a3b4486f3d_170.jpg"}},"parent_comment":{}},{"id":74480365,"comment":"感覺還是畫得有點草 o(TωT)o ","date":"2018-01-03T12:48:59+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74480177,"comment":"雪地靴加黑丝，好sex啊 (love2)","date":"2018-01-03T12:40:22+09:00","user":{"id":9783163,"name":"眼镜才是本体","account":"jxxy","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/04/22/14/45/33/12451859_c1d511c6b7ca0d1011bf361f7137dbe4_170.jpg"}}}},{"id":74480350,"comment":"好像是透視出了問題，謝謝啊(Ｔ▽Ｔ)","date":"2018-01-03T12:47:59+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74479875,"comment":"靴子角度有点奇怪(heaven)其他都好看(love2)","date":"2018-01-03T12:23:35+09:00","user":{"id":7035607,"name":"╰´韵つ","account":"qq767366925","profile_image_urls":{"medium":"https://source.pixiv.net/common/images/no_profile.png"}}}},{"id":74480177,"comment":"雪地靴加黑丝，好sex啊 (love2)","date":"2018-01-03T12:40:22+09:00","user":{"id":9783163,"name":"眼镜才是本体","account":"jxxy","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/04/22/14/45/33/12451859_c1d511c6b7ca0d1011bf361f7137dbe4_170.jpg"}},"parent_comment":{}},{"id":74479875,"comment":"靴子角度有点奇怪(heaven)其他都好看(love2)","date":"2018-01-03T12:23:35+09:00","user":{"id":7035607,"name":"╰´韵つ","account":"qq767366925","profile_image_urls":{"medium":"https://source.pixiv.net/common/images/no_profile.png"}},"parent_comment":{}},{"id":74479737,"comment":"謝謝謝謝(〃'▽'〃)","date":"2018-01-03T12:16:01+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74479305,"comment":"狗狗和人都很赞！","date":"2018-01-03T11:51:15+09:00","user":{"id":15772140,"name":"唧剑的勇者唧","account":"1601174066","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2016/07/10/21/50/31/11182262_ee783ffc6af8557b0b6ed347c744afb4_170.jpg"}}}},{"id":74479729,"comment":"非常感謝(๑*◡*๑)","date":"2018-01-03T12:15:34+09:00","user":{"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}},"parent_comment":{"id":74479347,"comment":"画的真好 (heart)","date":"2018-01-03T11:53:23+09:00","user":{"id":10132893,"name":"響soul","account":"cxbtchbc","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2015/04/26/15/30/16/9283162_198a243826fe15a0bd0b62746c03e42e_170.jpg"}}}}]
     * next_url : https://app-api.pixiv.net/v1/illust/comments?illust_id=66619229&offset=30&include_total_comments=false
     */

    private int total_comments;
    private String next_url;
    private List<CommentsBean> comments;

    public int getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(int total_comments) {
        this.total_comments = total_comments;
    }

    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        /**
         * id : 74774246
         * comment : ty   -w-
         * date : 2018-01-13T17:21:57+09:00
         * user : {"id":20980424,"name":"方丈要吃肉(修行ing）","account":"1015453836","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}}
         * parent_comment : {"id":74770901,"comment":"You painting WA2000 is so cute !","date":"2018-01-13T14:30:17+09:00","user":{"id":4866052,"name":"塩からす","account":"sa1ty9","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2018/01/05/00/35/46/13649713_dac36c983345d7b7946931ee42d4d32d_170.jpg"}}}
         */

        private int id;
        private String comment;
        private String date;
        private UserBean user;
        private ParentCommentBean parent_comment;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public ParentCommentBean getParent_comment() {
            return parent_comment;
        }

        public void setParent_comment(ParentCommentBean parent_comment) {
            this.parent_comment = parent_comment;
        }

        public static class UserBean {
            /**
             * id : 20980424
             * name : 方丈要吃肉(修行ing）
             * account : 1015453836
             * profile_image_urls : {"medium":"https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg"}
             */

            private int id;
            private String name;
            private String account;
            private ProfileImageUrlsBean profile_image_urls;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

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

            public static class ProfileImageUrlsBean {
                /**
                 * medium : https://i.pximg.net/user-profile/img/2017/10/05/19/56/20/13307506_0f98c48babaac8ceb11673464ad40ad3_170.jpg
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

        public static class ParentCommentBean {
            /**
             * id : 74770901
             * comment : You painting WA2000 is so cute !
             * date : 2018-01-13T14:30:17+09:00
             * user : {"id":4866052,"name":"塩からす","account":"sa1ty9","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2018/01/05/00/35/46/13649713_dac36c983345d7b7946931ee42d4d32d_170.jpg"}}
             */

            private int id;
            private String comment;
            private String date;
            private UserBeanX user;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public UserBeanX getUser() {
                return user;
            }

            public void setUser(UserBeanX user) {
                this.user = user;
            }

            public static class UserBeanX {
                /**
                 * id : 4866052
                 * name : 塩からす
                 * account : sa1ty9
                 * profile_image_urls : {"medium":"https://i.pximg.net/user-profile/img/2018/01/05/00/35/46/13649713_dac36c983345d7b7946931ee42d4d32d_170.jpg"}
                 */

                private int id;
                private String name;
                private String account;
                private ProfileImageUrlsBeanX profile_image_urls;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

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

                public ProfileImageUrlsBeanX getProfile_image_urls() {
                    return profile_image_urls;
                }

                public void setProfile_image_urls(ProfileImageUrlsBeanX profile_image_urls) {
                    this.profile_image_urls = profile_image_urls;
                }

                public static class ProfileImageUrlsBeanX {
                    /**
                     * medium : https://i.pximg.net/user-profile/img/2018/01/05/00/35/46/13649713_dac36c983345d7b7946931ee42d4d32d_170.jpg
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
        }
    }
}
