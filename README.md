# 概念

把每天的团购报表和非团购报表订单总数集中在一个文件中方便财务查看.

# 问题

日报表看上去很小,但是集中在一起后会很大,很容易超过虚拟机内存上限, 但月销量很好时, 报表大小很容易突破1G.

# 目标
解决的是读取日报太大导致Java虚拟机内存溢出的问题.

# 功能概述
主要功能有
1. ZIP报表包解包
2. 解包报表文件分类存储到指定临时目录
3. 按天读取临时目录的表报文件
4. 按商户分类创建文件夹存储单个商户的报表文件
5. 商户存储报表文件按天创建目录进行存储
6. 合并商户按天报表文件到商户目录下
7. 合并所有商户文件到临时目录下
8. 存储到指定位置
9. 

注意点
1,2 为第一步
3,4,5 为第二步
6,7 为第三步
8 为第四步

- 每一步都需要记录文件去记录完成情况.
- 每一步跳转下一步前都需要读取记录文件确认无误后方可进入下一步,若有误,应该重新读入有误文件直到无误为止.
    - 若单个有误情况超过阈值,应该删除全部临时文件,并通知监控平台
- 因注意效率和空间的使用.

**因为公司JDK版本比较低,这次实现主要采用JDK1.6设计实现.**