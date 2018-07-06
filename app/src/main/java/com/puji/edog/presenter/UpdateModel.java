package com.puji.edog.presenter;

/**
 * @author WangXuguang
 * @date 2018/5/25.
 */

public class UpdateModel {


    /**
     * code : 1000
     * data : {"download":"http://hachifile-oss.pujiapp.com/seedland-hachi/appFiles/20180523164639567.apk","isMust":0,"updateContent":"修复后台内容管理发布","version":"1.0.1"}
     * message : 操作成功
     */

    /**
     * 判断请求接口是否成功
     */
    private String code;
    private DataBean data;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * download : http://hachifile-oss.pujiapp.com/seedland-hachi/appFiles/20180523164639567.apk
         * isMust : 0
         * updateContent : 修复后台内容管理发布
         * version : 1.0.1
         */

        private String download;
        private int isMust;
        private String updateContent;
        private String version;

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public int getIsMust() {
            return isMust;
        }

        public void setIsMust(int isMust) {
            this.isMust = isMust;
        }

        public String getUpdateContent() {
            return updateContent;
        }

        public void setUpdateContent(String updateContent) {
            this.updateContent = updateContent;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
