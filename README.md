# SurfaceView,TextureView + MedaiPlayer分别实现播放本地视频，网络视频，M3U8视频
# SurfaceView, TextureView + MedaiPlayer respectively play local video, network video, M3U8 video

这是一个 利用MediaPlayer+SurfaceView/TextureView+MediaPlayer播放本地视频，网络视频，M3U8格式的网络直播视频流的小例子

SurfaceView和VideoView都是软解码，即利用gpu解码播放，几乎不利用cpu资源。
TextureView是硬解码，即利用cpu景星街吗播放，占用大量cpu资源，开始播放会出现短暂资源加载的卡顿
