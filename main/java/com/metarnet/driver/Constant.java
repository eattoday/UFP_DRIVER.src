package com.metarnet.driver;

/**
 * Created by Administrator on 2016/9/24/0024.
 */
public class Constant {

//    public final static String SETTING_STATUS_OLD = "SETTING_STATUS_OLD";

//    public final static String SETTING_STATUS_PRO = "SETTING_STATUS_PRO";

//    public final static String SETTING_STATUS_TMP = "SETTING_STATUS_TMP";

    //表单
    private String cformUrl;
    public static String Cform_Url;

    private String pmosUrl;
    public static String PMOS_URL;
    public String getCformUrl() {
        return cformUrl;
    }

    public void setCformUrl(String cformUrl) {
        this.cformUrl = cformUrl;
        Cform_Url =cformUrl;
    }

    public String getPmosUrl() {
        return pmosUrl;
    }

    public void setPmosUrl(String pmosUrl) {
        this.PMOS_URL = pmosUrl;
        this.pmosUrl = pmosUrl;
    }
}
