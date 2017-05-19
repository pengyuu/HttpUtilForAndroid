package com.hss01248.netdemo;

import android.app.Activity;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

public class PhoneActy extends Activity {

 /*   Activity activity;

    String num1;
    String num2;

    String smallNum = "8617092580665";
    String aparty = "";
    String bparty = "";
    String appkey = "8b575f9208f4181d974b72a71ca3ad24";
    String appSecret = "ebvbBE";


    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.tv_smallnum)
    TextView tvSmallnum;
    @Bind(R.id.et_num1)
    EditText etNum1;
    @Bind(R.id.et_num2)
    EditText etNum2;
    @Bind(R.id.btn_bind)
    Button btnBind;
    @Bind(R.id.button3)
    Button btnDial;
    @Bind(R.id.button2)
    Button btnUnbind;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        Logger.init("dd");
        NetUtil.init(getApplication(),"http://www.qxinli.com:9001/api/", new ILoginManager() {
            @Override
            public boolean isLogin() {
                return false;
            }

            @Override
            public <T> ConfigInfo<T> autoLogin() {
                return null;
            }

            @Override
            public <T> ConfigInfo<T> autoLogin(MyNetListener<T> listener) {
                return null;
            }
        });

        NetUtil.initAppDefault("session_id","data","code","msg",0,5,2);
        context = getApplicationContext();

        setContentView(R.layout.aty_phone);
        ButterKnife.bind(this);

    }

    public static Context getContext(){
        return context;

    }

    boolean isToDial = false;


    @OnClick({R.id.btn_bind, R.id.button3, R.id.button2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bind:
                isToDial = false;
                dial();
                break;
            case R.id.button2:
                isToDial = true;
                dial();
                break;
            case R.id.button3:
                unbind();
                break;
        }
    }

    *//**
     * 请求权限
     *//*
    private void dial() {

       // bind();

        PermissionUtils.askCallPhone(new PermissionUtils.PermissionListener() {
            @Override
            public void onGranted() {
                bind();


            }

            @Override
            public void onDenied(List<String> permissions) {

            }
        });




       *//* Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:17092580665" );
        intent.setData(data);
        startActivity(intent);*//*

    }

    private void unbind() {
        num1 = etNum1.getText().toString().trim();
        num2 = etNum2.getText().toString().trim();
       *//* if (TextUtils.isEmpty(num1) && TextUtils.isEmpty(num2)) {
            ToastUtils.showShortToast(activity, "输入为空");
            return;
        }*//*
        aparty = "86" + num1;
        bparty = "86" + num2;
        String timeStamp2 = System.currentTimeMillis() / 1000 + "";
        Logger.e("time:" + timeStamp2);

        final String act = "unbindnumber";
        String c = "virt";
        String m = "interfaces";

        String params = "";
        params = smallNum ;

        String str = appkey + m + c + act + params + appSecret + timeStamp2;
        Logger.e("str:" + str);
        String sign = EncryptUtils.encryptMD5ToString(str).toLowerCase();
        Logger.e("签名后的:" + sign);

        Map map11 = new HashMap<>();
        map11.put("m", m);
        map11.put("c", c);
        map11.put("a", "index");
        map11.put("act", act);
        map11.put("appkey", appkey);
        map11.put("sign", sign);
        map11.put("time", timeStamp2);

        map11.put("virtualnumber", smallNum);
       *//* map11.put("aparty", aparty);
        map11.put("bparty", bparty);*//*

        MyNetApi.getStandardJson("http://api.mixcom.cn/v2/",
                map11, VersionInfo.class, new MyNetListener<VersionInfo>() {
                    @Override
                    public void onSuccess(VersionInfo response, String resonseStr) {
                        Logger.e(resonseStr);
                        ToastUtils.showShortToast(activity, "成功解除绑定");


                    }

                    @Override
                    public void onEmpty() {
                        super.onEmpty();
                    }

                    @Override
                    public void onError(String msgCanShow) {
                        super.onError(msgCanShow);
                        Logger.e(msgCanShow);
                        if("号码已绑定".equals(msgCanShow)){
                            doCall();
                        }
                        ToastUtils.showShortToast(activity, msgCanShow);
                    }


                })
                .setIsAppendToken(false)
                .setCustomCodeValue(200, -1, -1)
                .setShowLoadingDialog(activity,"正在绑定号码")
                .start();

    }


    private void bind() {

        num1 = etNum1.getText().toString().trim();
        num2 = etNum2.getText().toString().trim();
        if (TextUtils.isEmpty(num1) || TextUtils.isEmpty(num2)) {
            ToastUtils.showShortToast(activity, "输入为空");
            return;
        }
        aparty = "86" + num1;
        bparty = "86" + num2;
        String timeStamp2 = System.currentTimeMillis() / 1000 + "";
        Logger.e("time:" + timeStamp2);

        final String act = "bindnumber";
        String c = "virt";
        String m = "interfaces";

        String params = "";
        params = smallNum + aparty + bparty;

        String str = appkey + m + c + act + params + appSecret + timeStamp2;
        Logger.e("str:" + str);
        String sign = EncryptUtils.encryptMD5ToString(str).toLowerCase();
        Logger.e("签名后的:" + sign);

        Map map11 = new HashMap<>();
        map11.put("m", m);
        map11.put("c", c);
        map11.put("a", "index");
        map11.put("act", act);
        map11.put("appkey", appkey);
        map11.put("sign", sign);
        map11.put("time", timeStamp2);

        map11.put("virtualnumber", smallNum);
        map11.put("aparty", aparty);
        map11.put("bparty", bparty);

        MyNetApi.getStandardJson("http://api.mixcom.cn/v2/",
                map11, VersionInfo.class, new MyNetListener<VersionInfo>() {
                    @Override
                    public void onSuccess(VersionInfo response, String resonseStr) {
                        Logger.e(resonseStr);
                        ToastUtils.showShortToast(activity, "绑定成功,开始拨打对方电话");


                    }

                    @Override
                    public void onEmpty() {
                        super.onEmpty();
                    }

                    @Override
                    public void onError(String msgCanShow) {
                        super.onError(msgCanShow);
                        Logger.e(msgCanShow);
                        if("sorry,号码已被其他号码绑定,解绑中...".equals(msgCanShow)){
                            unbind();
                        }
                        ToastUtils.showShortToast(activity, msgCanShow);
                    }


                })
                .setIsAppendToken(false)
                .setCustomCodeValue(200, -1, -1)
                .setShowLoadingDialog(activity,"正在绑定号码")
                .start();
    }
    @SuppressWarnings("all")
    private void doCall(){
        if(isToDial){
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + "17092580665");
            intent.setData(data);
            activity.startActivity(intent);
        }

    }

*/


}
