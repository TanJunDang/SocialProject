package io.tanjungang.github.social.base.entity;

import java.util.ArrayList;

public class TianGouResp {
    ArrayList<TianGouInfo> tngou = new ArrayList<>();

    public ArrayList<TianGouInfo> getTngou() {
        return tngou;
    }

    public void setTngou(ArrayList<TianGouInfo> tngou) {
        this.tngou = tngou;
    }

    class TianGouInfo {
        String title;
        String fromurl;
        String img;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFromurl() {
            return fromurl;
        }

        public void setFromurl(String fromurl) {
            this.fromurl = fromurl;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}