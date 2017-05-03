# VideoList
这是一个简单的本地视频播放器，实现了按文件后缀名过滤扫描出大部分格式的视频文件，可以设置过滤的文件夹。<br>
数据存储用到了GreenDao数据库框架，播放器简单的用到了[Rukey7/IjkPlayerView](https://github.com/Rukey7/IjkPlayerView)集成的IJKPlayer播放器。
## 截图
![](https://github.com/Wantrer/VideoList/raw/master/screenshot/1.png)![](https://github.com/Wantrer/VideoList/raw/master/screenshot/2.png)![](https://github.com/Wantrer/VideoList/raw/master/screenshot/3.png)
![](https://github.com/Wantrer/VideoList/raw/master/screenshot/4.png)![](https://github.com/Wantrer/VideoList/raw/master/screenshot/5.png)![](https://github.com/Wantrer/VideoList/raw/master/screenshot/6.png)
### 数据传递
用到eventbus在fragment和fragment、fragment和activity间传递数据消息<br>
org.greenrobot:eventbus:3.0.0<br>
点击视频文件跳转另一个activity需要用到gson传递该文件的信息以初始化播放器<br>
com.google.code.gson:gson:2.2.4
### 数据库存储
扫描出的结果即视频文件的信息存储到videoinfo.db，设置要过滤的文件夹存储到filefolder.db，这些数据库的存储用到了greendao数据库框架<br>
org.greenrobot:greendao:2.2.1<br>
org.greenrobot:greendao-generator:2.2.0
### 播放器
[Rukey7/IjkPlayerView](https://github.com/Rukey7/IjkPlayerView)集成的IJKPlayer播放器需要用到glide加载初始化播放器时的视频区域显示的图片<br>
com.github.bumptech.glide:glide:3.7.0<br>
以Moudle的方式添加[Rukey7/IjkPlayerView](https://github.com/Rukey7/IjkPlayerView)中的playerview文件夹<br>
compile project(':playerview')
### 过滤设置
设置要过滤扫描的文件夹的添加的界面用到了pagerslidingtabstrip实现tab的切换<br>
com.astuetz:pagerslidingtabstrip:1.0.1

# LICENSE
```
Copyright [2017] [Wantrer]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

