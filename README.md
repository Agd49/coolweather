# coolweather
### 《第一行代码》开发酷欧天气
#### 为什么使用pull而不是sax以及dom
因为pull允许你只对其中的一部分进行解析处理，而另外两种都需要对整个进行处理
sax需要你对整个文档进行处理，事件处理机制
dom也需要进行处理，而且之前两者都是事件处理机制，而这个方法是生成一棵树，所以不适合应用于大量的数据中。
###XmlPullParserFactory factory = XmlPullParserFactory.newInstance();这里会遇到rourse not found的问题
###finish()之后，如果接下来的方法不想执行了，就要return
###inflating liearLayout，这是XML文件编写错误导致的，这里应该是LinearLayout
###使用MyApplication.getContext()在getDefaultSharedPreference()的时候，这里的语句会出现错误，NullPointException
##为了在多线程中使用context得到sharedPreference,使用了太多的Context传递，所以这里应该使用WeatherActivity
##的单例模式
####simpleDateFormat有问题
