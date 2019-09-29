# CameraView

分别使用camera api1、api2和google的CameraView来实现照相机功能


## 如何引用

### 添加Gradle依赖

1.先在项目根目录的 build.gradle 的 repositories 添加:
```
allprojects {
     repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.然后在dependencies添加:

```
dependencies {
  ...
  implementation 'com.github.xuexiangjys:CameraView:1.0.0'
}
```

