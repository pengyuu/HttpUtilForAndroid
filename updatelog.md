

# update

## 2017.04.18
* 增加设置公共interceptor,以及单个请求的拦截器的功能.(比如,便于使用chuck来显示请求和响应信息)
* demo中增加chuck的使用,果然很牛逼
* loadingdialog的弹出既可以指定activity,也可以不指定.

## 2017.04.10
* 移除原lite-asyntask.jar,以及对androidUtilCode的依赖.读写缓存的executor改由IClient提供
* 增加设置和更新公共header和公共参数的功能
* 三字段json请求时,如果为unlogin,调用自动登录方法,并在登录成功后自动发送原请求.


## 2017.04.07
* 增加onPreExecute()和onPreValidate(ConfigInfo config)两个回调
* 正式开始发请求前先检查网络连接,如果没网,直接走listener.onNoNetwork()
* 实现任意线程任意界面发请求并弹出dialog,不再需要传入activity引用,改成软引用自动获取,此时需要在BaseApplication中registerActivityLifecycleCallbacks
* 处理取消请求的回调,实现了:不管是哪一种取消方式,都会回调到对应的oncancel方法中
* 完成了六种缓存策略的实现
* ConfigInfo 增加一个extraTag,给开发者携带额外的参数,并提供设置方法setExtraTag(Object extraTag),以及校验的回调onPreValidate(ConfigInfo config)



## 2017.03.23

完善https证书的三个放置位置的读取(raw,assert,file)

完善log打印,加上了请求体和响应体的打印

完善了参数设置时,解决了原先一对对参数添加和整个参数字符串添加的冲突问题.



# 2017.02.08

### 大改

全新的api,完全的链式调用

移除retrofit的代码,底层直接依赖于okhttp

## 2016.12.22

#### 添加对https的支持:

1.全局配置:

自签名时,将证书文件放在你的项目的raw文件夹下,然后在init之前调用addCer(R.raw.xxx),即可全局使用.

可以多次调用,以添加多个证书.

2.对单个请求的配置:

适用请求:额外的或别人的自签名证书网站,你又不想添加他的证书,可以在单个请求中调用.setIgnoreCer(),在当前这个请求中忽略证书校验.  

不用担心,不会影响其他请求的证书校验,因为这个我用的是一个单独的okhttpclient.

