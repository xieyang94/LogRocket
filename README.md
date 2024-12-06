# LogRocket

<div align=center>
<img src="https://github.com/xieyang94/LogRocket/blob/dev/images/icon_logo.png" width="128" height="128"/>
</div>

### 简介
当前开发者可在AS控制台查看日志，或者通过adb查看日志，对于开发大有帮助；发生异常也可通过日志上报到日志平台去查看，十分便捷；

LogRocket换了一个角度，从测试者的角度出发；

开发者应该遇到过一些情况：
- 接口数据不对，测试人员直接拿着手机找过来，说报错了，好一点的可能会自己会抓包查看网络接口；不好的就不说了。
- 一些日志崩溃也是如此，测着测着，突然闪退，接了日志系统还好，还能去查看到底什么原因导致的，如果没接入，则需要测试人员反复的去猜测复现，十分不便；
- 接口出了问题，后端找测试要参数，然后测试自己去抓包或者去找移动端开发；
- ....

> 真想给每个测试人员安装一个AS

整个就是基于这种小处境出发，解决测试人员查看日志的问题；

LogRocket基于此出发，将终端设备作为Server，直接将logcat的日志通过ws直接发送出来，开发者可在日志中加入网络接口打印，以及一些日常打印，测试人员可直接访问对应的ws地址就可直接阅读网络请求以及崩溃日志。

可第一时间发现问题并保留日志信息及时反馈，减少中间流转时间，提升效率。

### 对代码的影响
LogRocket建议仅仅在开发环境中使用，不建议上生产环境，以免引发不必要的错误；

为了实现这种解耦，于是将WebSocket得开启和关闭放在了Provider中，然后通过「debugImplementation」得方式进行依赖，这样在生产环境自动剔除，避免了对生产环境的干扰。

另外，终端既然作为Server端，那他就得提供一个ws地址，开发者可直接在wifi链接页面去查看自己手机的ip；通过LogRocket也提供了方法去获取对应终端的ip。

端口号是采用随机分配的空闲端口号，因为一个人可能会负责多个应用的开发，同一台设备，同一个网络下，不能同一个端口，故每次生成的ws链接端口号都不同。

这里LogRocket也提供的方法来获取整个ws的访问地址；

于是这里就会有一个问题，既然是采用「debugImplementation」得方式进行依赖，那肯定不能直接使用LogRocket的方法，不然在生产环境会因为没有依赖找不到对应的Class而报错，所以这里又提供了一个类，专门用于反射获取ws地址的。

对于知负责一个应用的开发者，如果会觉得每次更换端口显得很麻烦，想固定端口，这个也是提供了解决办法，可以在AndroidManifest.xml中配置；
在application节点中加入以下配置（默认值为false）：
```xml
<meta-data
    android:name="uniquePort"
    android:value="true" />
```

这里又有了一个问题，ws有了，难道还要找个ws测试网站去看日志？这个可以开发者自行实现，但是LogRocket也提供了一个默认h5页面，可以直接去查看，对应的文件为index.html。

这个index.html也是我去找ChatGpt生成的，还带了日志过滤，方便使用者过滤日志。如果您觉得UI不太好看，或者功能过于简陋，可以自行找ChatGpt去定制生成，或者让前端同学帮忙搞搞。

因为就只是临时起意的一个小工具，所以就不想去搭服务器放网页，所以直接把h5文件放到了github上，或者直接通过浏览器访问github上的这个index.html即可。

对于index.html，输入ws链接，回车即可；

![index_info.png](https://github.com/xieyang94/LogRocket/blob/dev/images/index_info.png)

### 注意点 
需要保持ws的Server和Client在同一网络

### 接入使用方式

#### 仓库引入
```gradle
repositories {
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
    }
```
#### 依赖引入
```gradle
debugImplementation 'com.logrocket:logrocket:lastVersion'
```
#### 固定端口配置
默认非固定端口(false)
```xml
<meta-data
    android:name="uniquePort"
    android:value="true" />
```

### 反射获取ws链接
```java
class Test {
    
    public String getWsAddress(Context context) {
        String result = null; // 默认值，如果反射调用失败则返回此值
        try {
            // 获取MetaUtil类的Class对象
            Class<?> metaUtilClass = Class.forName("cn.net.yto.logrocket.refect.ReflectLogRocket");
            // 获取getPort方法的Method对象
            Method getPortMethod = metaUtilClass.getMethod("wdAddress", Context.class);
            // 调用getPort方法
            // 注意：如果getPort是非公开方法，可能需要设置accessible为true
            getPortMethod.setAccessible(true);
            // 创建MetaUtil类的实例
            Object metaUtilInstance = metaUtilClass.newInstance();
            // 调用invoke方法执行getPort(context)
            result = (String) getPortMethod.invoke(metaUtilInstance, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
```
被反射的类：
```java
public class ReflectLogRocket {

    /**
     * 反射获取ws地址
     *
     * @param context
     * @return
     */
    public String wdAddress(Context context) {
        return LogRocket.getInstance().getWsAddress(context);
    }
}
```
您也可以不采用这种用法，可以直接把代码拷贝到您的项目中，然后直接调用。

### Demo
Demo样式
![app_cut.png](https://github.com/xieyang94/LogRocket/blob/dev/images/app_cut.jpg)

### 其他

有问题可以反馈，我会尽最大的努力解决或采纳。








