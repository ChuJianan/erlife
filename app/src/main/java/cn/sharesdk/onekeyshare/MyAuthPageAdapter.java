package cn.sharesdk.onekeyshare;

import cn.sharesdk.framework.authorize.AuthorizeAdapter;

/**
 * Created by cjn on 2016/9/22.
 */

public class MyAuthPageAdapter extends AuthorizeAdapter {
    public void onCreate() {
        hideShareSDKLogo();
        disablePopUpAnimation();
    }
}
