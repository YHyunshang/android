
### 依赖的第三方库
```
dependencies {
  implementation 'com.github.bumptech.glide:glide:4.12.0'
  annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'

  implementation 'jp.wasabeef:glide-transformations:4.3.0'
  // If you want to use the GPU Filters
  implementation 'jp.co.cyberagent.android:gpuimage:2.1.0'
}

```

### 使用方法
```
        YHImageLoader.INSTANCE.with(this)
                .asFile()
                .load("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2826242742,3026899353&fm=26&gp=0.jpg")
//                .load(R.mipmap.ic_launcher)
                .centerInside()
                .isBlur(true)
                .isRoundedCorners(true)
                .roundRadius(200)
                .addListeners(new YHImageLoaderListener<File>() {
                    @Override
                    public void onLoadStarted() {

                    }

                    @Override
                    public boolean onResourceReady(@Nullable File resource, boolean isFirstResource) {
                        System.out.println("zengbobo onResourceReady resource:" + resource + ",isFirstResource:" + isFirstResource);
                        return false;
                    }

                    @Override
                    public boolean onLoadFailed(@Nullable Exception e, boolean isFirstResource) {
                        System.out.println("zengbobo onLoadFailed Exception:" + e + ",isFirstResource:" + isFirstResource);
                        return false;
                    }
                })
                .preload();
//                .into(mViewBinding.img);

```

### YHImageLoader   应用层通过该类来使用图片加载

### YHImageRequestManager   图片加载发起类

### YHImageRequestBuilder  图片加载库的配置，封装原始加载配置属性，进行转换

### YHImageLoaderListener   监听类