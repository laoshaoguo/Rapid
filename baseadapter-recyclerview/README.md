<<<<<<< HEAD
2017年1月18日13:40:10
# 基础列表Adapter

 基于[baseAdapter](https://github.com/hongyangAndroid/baseAdapter),


 ## 修改
1. `ViewHolder`,增加`INVISIABLE`
 ```
  public ViewHolder setVisible(int viewId, int visible)
     {
         View view = getView(viewId);
         view.setVisibility(visible);
         return this;
     }
 ```
 2. `MultiItemTypeAdapter`增加`getItem(int position)`方法

=======
2017年1月18日13:40:10
# 基础列表Adapter

 基于[baseAdapter](https://github.com/hongyangAndroid/baseAdapter),


 ## 修改
1. `ViewHolder`,增加`INVISIABLE`
 ```
  public ViewHolder setVisible(int viewId, int visible)
     {
         View view = getView(viewId);
         view.setVisibility(visible);
         return this;
     }
 ```
 2. `MultiItemTypeAdapter`增加`getItem(int position)`方法

>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
 3. `ItemViewDelegate` 添加返回了上一条消息