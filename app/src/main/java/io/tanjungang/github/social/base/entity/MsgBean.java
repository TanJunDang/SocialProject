package io.tanjungang.github.social.base.entity;

import java.util.ArrayList;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/5/2
 */

public class MsgBean {

    private MsgRespon result;

    public MsgRespon getResult() {
        return result;
    }

    public void setResult(MsgRespon result) {
        this.result = result;
    }

    public class MsgRespon {
        private ArrayList<MsgInfo> data = new ArrayList<>();

        public ArrayList<MsgInfo> getData() {
            return data;
        }

        public void setData(ArrayList<MsgInfo> data) {
            this.data = data;
        }

        public class MsgInfo {
            private String title;
            private String category;
            private String date;
            private String author_name;
            private String url;
            private String thumbnail_pic_s;
            private String thumbnail_pic_s02;
            private String thumbnail_pic_s03;
            private String uniquekey;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbnail_pic_s() {
                return thumbnail_pic_s;
            }

            public void setThumbnail_pic_s(String thumbnail_pic_s) {
                this.thumbnail_pic_s = thumbnail_pic_s;
            }

            public String getThumbnail_pic_s02() {
                return thumbnail_pic_s02;
            }

            public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
                this.thumbnail_pic_s02 = thumbnail_pic_s02;
            }

            public String getThumbnail_pic_s03() {
                return thumbnail_pic_s03;
            }

            public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
                this.thumbnail_pic_s03 = thumbnail_pic_s03;
            }

            public String getUniquekey() {
                return uniquekey;
            }

            public void setUniquekey(String uniquekey) {
                this.uniquekey = uniquekey;
            }
        }
    }

}
