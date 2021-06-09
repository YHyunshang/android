# 开发说明文档


## 摘要

* [1 模块架构（MVVM+组件化）](#1-模块架构（MVVM+组件化）)
* [2 组件介绍](#2-组件介绍)
* [3 android 设计图要求](#5-android-设计图要求)
* [4 as 规范](#6-as-规范)
* [5 as 常用插件](#7-as-常用插件)
* [6 开发规范](#8-开发规范)
* [7 ui库说明](#9-ui库说明)
* [8 引用的类库说明](#10-引用的类库说明)
* [9 其他参考](#8-其他参考)
* [10 版本更新说明](#9-版本更新说明)


##### 1 模块架构（MVVM+组件化）
```
├── base                            【基础SDK】
├        ├── activity               // MVVM Activity封装
├        ├── fragment               // MVVM Fragment封装
├        └── dialog                 // MVVM Dialog封装
├        └── liveData               // MVVM LiveData封装
├        └── viewModel              // MVVM ViewModel封装
├
├── base_library                    【基础组件】
├        ├── http-core              // 网络核心库
├        ├── http-cache             // 全局网络缓存（自动缓存，内存+磁盘）
├        ├── RecyclerView           // RecyclerView封装
├        ├── lib_utils              // 常用工具类库
├        ├── lib_log                // Log控制
├        ├── lib_storage            // 存储封装（MMKV）
├        ├── lib_toast              // Toast展示
├        ├── lib_imageLoader        // 图片加载库（基于Glide）
├        └── lib_widget             // 各种ui组件 Imageview RefreshActivity等
├
├── base-ui                         【基础业务模块】
├        ├── activity               // 业务层 Activity封装
├        ├── fragment               // 业务层 Fragment封装
├        └── dialog                 // 业务层 Dialog封装
├
├── library                        【常用业务组件】
├        ├── lib_video              // 视频组件封装
├        ├── lib_download           // 下载组件封装
├        ├── lib_share              // 分享组件封装
├        ├── lib_pay                // 支付组件封装
├        └── lib_baiduMap           // 百度地图定位
├
├── demo                           【使用demo】
├
├── app                             //应用层（直接引用基础业务模块 + 业务组件）
├        ├── SplashActivity         // 闪屏
├        └──  MainActivity          // 主activity
├
├
├── build.gradle                    // 项目依赖gradle
├
├── config.gradle                   // 配置gradle文件
├
├── local.properties
└── settings.gradle
```

### 1 核心框架
-- http-core         网络核心库（封装okttp、retrofit、RxJava2）
-- http-cache        全局网络缓存核心库（自动缓存，内存+磁盘）
-- RecyclerView      基于RecyclerView进一步封装
    -- RecyclerView-Adapter                 RecyclerView 适配器封装
    -- RecyclerView-ItemDecoration          RecyclerView ItemDecoration
    -- RecyclerView-Ext                     RecyclerView扩展方法

### 2 基础模块
-- base  基础SDK模块（Activity/Fragment/Dialog、liveData、ViewBinding、ARouter、EventBus）
-- lib_utils 基础工具类
-- lib_log log模块
-- lib_toast toast模块
-- lib_imageLoader 图片加载模块（基于Glide）
-- lib_widget  各种ui组件 Imageview RefreshActivity等
-- lib_debug 调试工具

### 3 基础业务模块
-- base_ui  基础业务模块（业务BaseUIActivity/BaseUIFragment/BaseUIDialog）
-- EmptyLayer/BlankLayer 统一的覆盖层模板（展示空数据、网络异常等）

### 4 基础业务组件
-- lib_video  视频播放组件
-- lib_download 下载组件
-- lib_share 分享组件
-- lib_pay  支付组件
-- lib_baiduMap  百度地图定位组件

### 5 android 设计图要求

1. 设计图单位为dp，以宽360dp为基准
2. 用xxhdpi、xxxhdpi两套图（对应mipmap-xxhdpi、mipmap-xxxhdpi文件夹）

### 6 as 规范

1. 使用最新稳定版的 IDE 进行开发  **[Android官方网站][Android官方网站]** 下载IDE、tools
2. 编码格式统一为 UTF-8
3. 代码格式化，统一使用 AndroidStudio 默认模板
4. 推荐 Genymotion 模拟器，请移步 **[Genymotion官方网站][Genymotion官方网站]**

### 7 as 常用插件

在IDE内搜索、下载安装（仅常用，其他根据需求扩展）

| 插件名称                               | 说明                                       |
| :--------------------------           | ---------------------------------------- |
| `Alibaba Java Coding Guidelines`      | Alibaba编码规约插件，检查编码是否规范 |
| `Gsonformat`                          | 根据json数据快速生成java实体类      |
| `Android Parcelable code generator`   | JavaBean序列化，快速实现Parcelable接口  |
| `CodeGlance`                          | 可用于快速定位代码                                   |
| `Native Terminal Plugin`              | 能够打开系统的Terminal，并且快速定位到当前项目                               |
| `Exynap`                              | 一个帮助开发者自动生成样板代码的 AndroidStudio 插件         |
| `Biu`                                 | 压缩PNG                                     |
| `Android Resource Usage Count`        | 显示每个资源的使用次数                                  |
| `Android Methods Count`               | 获取Android 框架上方法数量                                     |
| `ECTranslation`                       | 翻译神器                                     |
| `JSONOnlineViewer`                    | 在Android Studio中请求、调试接口                  |
| `gradle packer plugin`                | Android渠道打包工具                  |
| `lint cleaner plugin`                 | 用于删除Android项目中未使用的资源                  |
| `idea markdown`                       | 针对 idea 的Markdown语言支持                  |
| `JRebel for android`                  | 高效调试神器                  |
| `AndroidWiFiADB`                      | 通过WiFi连接您的Android设备                  |
| `Permission Dispatcher`               | 权限管理的插件                  |


### 8 开发规范

1. Java部分参考： **[阿里巴巴 Java 开发手册][阿里巴巴 Java 开发手册]**
2. Android部分参考： **[阿里巴巴 Android 开发手册][阿里巴巴 Android 开发手册]**

### 5 UI库说明
其他根据项目具体需求扩展

1. Android原生UI组件，参考：**[Android官方网站][Android官方网站]**
2. QMUI快速开发库，参考：**[QMUI官方网站][QMUI官方网站]**

### 10 引用的类库说明

##### 6.1 图片处理
| 类库名称                               | 说明                                       |
| :--------------------------           | ---------------------------------------- |
| [Luban][Luban]                        | 图片压缩 |
| [glide][glide]                        | 图片加载及缓存      |
| [Matisse][Matisse]                    | 图片选择 |
| [uCrop][uCrop]                        | 图片裁剪                                   |

##### 6.2 屏幕适配
| 类库名称                               | 说明                                       |
| :--------------------------           | ---------------------------------------- |
| [ScreenAdaptation][ScreenAdaptation]  | smallestWidth限定符适配 |
| [AndroidAutoSize][AndroidAutoSize]    | 今日头条屏幕适配方案，修改Density|

##### 6.3 基础测试
| 类库名称                               | 说明                                       |
| :--------------------------           | ---------------------------------------- |
| [leakcanary][leakcanary]              | 内存泄漏检测 |
| [AndroidPerformanceMonitor][AndroidPerformanceMonitor]    | UI卡顿检测|

##### 6.4 Rx系列
| 类库名称                               | 说明                                       |
| :--------------------------           | ---------------------------------------- |
| [RxJava][RxJava]                      |   异步   |
| [RxAndroid][RxAndroid]                |   异步  |
| [RxLifecycle][RxLifecycle]            |生命周期|
| [RxPermissions][RxPermissions]        |权限管理|
| [RxCache][RxCache]                    | 缓存|

##### 6.5 网络
| 类库名称                               | 说明                                       |
| :--------------------------           | ---------------------------------------- |
| [okhttp][okhttp]                      |   网络库   |
| [okio][okio]                          |   okhttp依赖的io库  |
| [retrofit][retrofit]                  |   网络封装，注解  |

##### 6.6 UI方面
| 类库名称                               | 说明                                       |
| :--------------------------           | ---------------------------------------- |
| [QMUI_Android][QMUI_Android]          |   QMUI库   |
| [SmartRefreshLayout][SmartRefreshLayout] | 下拉刷新，上拉加载  |
| [DToast][DToast]                          | Toast兼容库  |

##### 6.7 缓存
| 类库名称                               | 说明                                       |
| :--------------------------           | ---------------------------------------- |
| [greenDAO][greenDAO]          |   greenDao数据库   |
| [GreenDaoUpgradeHelper][GreenDaoUpgradeHelper] | greenDao数据库升级辅助  |
| [DiskLruCache][DiskLruCache]                          | Lru磁盘缓存  |
| [DiskLruCacheUtils][DiskLruCacheUtils]                         | Lru磁盘缓存工具类  |

##### 6.8 事件总线
| 类库名称                               | 说明                                       |
| :--------------------------           | ---------------------------------------- |
| [AndroidEventBus][AndroidEventBus]    |   AndroidEventBus   |
| [EventBus][EventBus] | EventBus  |
| [otto][otto]                          | otto  |

##### 6.9 路由、组件化、注解
| 类库名称                               | 说明                                       |
| :--------------------------           | ---------------------------------------- |
| [ARouter][ARouter]                    |   路由   |
| [WMRouter][WMRouter]                  |   路由  |
| [JIMU][JIMU]                          | 组建化（参考）  |
| [butterknife][butterknife]            |  视图注解 |
| [dagger2][dagger2]                    |   注解|


### 9 其他参考
    github
1. [知乎][zhihu]
2. [腾讯][Tencent]
3. [美团][meituan]
4. [阿里巴巴][alibaba]
5. [square][square]
6. [百度][baidu]
7. [饿了么][eleme]

### 10 版本更新说明

##### V1.0.0更新说明

1. 确定常用的技术选择，确定基础架构；
2. 约束项目目录结构；
3. 代码规范
















[阿里巴巴 Java 开发手册]: https://github.com/alibaba/p3c/blob/master/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C%EF%BC%88%E8%AF%A6%E5%B0%BD%E7%89%88%EF%BC%89.pdf
[阿里巴巴 Android 开发手册]:https://102.alibaba.com/downloadFile.do?file=1519806643286/Android1_0_0.pdf
[Android官方网站]: https://developer.android.com
[Genymotion官方网站]: https://www.genymotion.com/
[QMUI官方网站]: https://qmuiteam.com/android

[Luban]: https://github.com/Curzibn/Luban
[glide]: https://github.com/bumptech/glide
[Matisse]: https://github.com/zhihu/Matisse
[uCrop]: https://github.com/Yalantis/uCrop

[ScreenAdaptation]: https://github.com/wildma/ScreenAdaptation
[AndroidAutoSize]: https://github.com/JessYanCoding/AndroidAutoSize

[leakcanary]: https://github.com/square/leakcanary
[AndroidPerformanceMonitor]: https://github.com/markzhai/AndroidPerformanceMonitor

[RxJava]: https://github.com/ReactiveX/RxJava
[RxAndroid]: https://github.com/ReactiveX/RxAndroid
[RxLifecycle]: https://github.com/trello/RxLifecycle
[RxPermissions]: https://github.com/tbruyelle/RxPermissions
[RxCache]: https://github.com/VictorAlbertos/RxCache

[okhttp]: https://github.com/square/okhttp
[okio]: https://github.com/square/okio
[retrofit]: https://github.com/square/retrofit

[QMUI_Android]: https://github.com/Tencent/QMUI_Android
[SmartRefreshLayout]: https://github.com/scwang90/SmartRefreshLayout
[DToast]: https://github.com/Dovar66/DToast

[greenDAO]: https://github.com/greenrobot/greenDAO
[GreenDaoUpgradeHelper]: https://github.com/yuweiguocn/GreenDaoUpgradeHelper
[DiskLruCache]: https://github.com/JakeWharton/DiskLruCache
[DiskLruCacheUtils]: https://github.com/Mr-wangyong/DiskLruCacheUtils

[AndroidEventBus]: https://github.com/hehonghui/AndroidEventBus
[EventBus]: https://github.com/greenrobot/EventBus
[otto]: https://github.com/square/otto

[ARouter]: https://github.com/alibaba/ARouter
[WMRouter]: https://github.com/meituan/WMRouter
[JIMU]: https://github.com/mqzhangw/JIMU

[butterknife]: https://github.com/JakeWharton/butterknife
[dagger2]: https://github.com/google/dagger

[zhihu]: https://github.com/zhihu
[Tencent]: https://github.com/Tencent
[meituan]: https://github.com/meituan
[alibaba]: https://github.com/alibaba
[square]: https://github.com/square
[baidu]: https://github.com/baidu
[eleme]: https://github.com/eleme







