
# steedFramework4web by 战马

# 超快速开发大中小型网站的全能型web框架(大型网站部分正在逐步完善,敬请期待)

## 看了部署说明还不会部署的同学请看下面的部署教程

 **[部署教程](http://v.youku.com/v_show/id_XMTcxNjg1Njc0NA==.html?beta&)** 

##之前项目推荐一直在审核....终于审核通过了,有动力更新了,文档我会慢慢完善的(其实根本不需要什么文档,对着例子,看看注释基本都会用了的),麻烦大家耐心等待.
## 特色:

* 1.还在烦mybatis的mapper文件?还在为hibernate的hql头痛?该框架全自动生成hql,不用手写任何hql或sql,彻底解决您的烦恼.

* 2.通过反射将查询条件封装到model,然后根据model自动生成hql,所以普通的增删查改只需一行调用父类相关增删查改方法的代码!!

* 3.约定高于配置,0配置文件,都是按一个规定写的,所以不用担心团队开发代码乱.

* 4.全自动生成hql,没有sql,所以该框架兼容hibernate方言所兼容的所有数据库.

* 5.添加功能只需编写实体类,并在action中调用已经写好的父类方法即可,开发效率非常高.

* 6.全局过滤,转义危险字符,避免sql注入,jsoup+正则表达式校验富文本内容避免xss攻击.

* 7.只需添加一个注解即可实现权限管理,其它什么都不用做.

* 8.基于注解的字段校验,校验数据合法性只需添一个注解即可.

* 9.摒弃传统action,service,dao层模式(你他妈在逗我,就一个小型网站还分三层,还要抽接口),数据库操作只需一个DaoUtil即可,不用另外编写dao层.

* 10.首创(不知道是不是,反正没看到网上有相关文章)实体类编程,去掉service层,实体类继承父类即可拥有增删查改能力,可以通过重写相关方法实现假删除,级联删除等等.

* 11.全局异常捕捉,不会向用户展示任何出错信息,保证系统安全,另外也可以在代码的任何地方通过抛消息提示专用异常来返回提示给前端.返回的提示内容可以是一个html页面,也可以是json数据,默认html页面,如果想要返回json格式的数据,在请求的时候加上&ajax=1这个参数即可.

* 12.多数据库支持,支持操作多个不同的数据库,可以在mysql,oracle等等之间进行切换,也就是说你的数据可以保存在多个不同的数据库.

* 13.编写了非常多的工具类http请求,二维码生成,Excel解析,加密解密等,拿来就可以用.

* 14.封装了微信接口调用模块,只要steed.filter.WechatLoginFilter过滤到的地址均会调微信微信oauth登陆接口静默登陆,微信js相关的接口已经封装成标签,一个标签即可实现微信支付,获取共享地址等,微信http调用接口通过steed.util.wechat.WechatInterfaceInvokeUtil调用,只需一行代码接口即可调用发红包接口,模板消息推送接口等,JsapiTicket,AccessToken等由框架来管理,要用的时候直接拿来用,微信解密解密工具也封装有,直接拿来用即可.

* 15.封装了支付宝批量支付(目前只封装了这个,其它功能敬请期待).

* 16.集成log4j2,自动分类并压缩日志.

* 17.编写了专门用来写接口与其他系统对接的接口模块(包括android,ios等客户的接口,android配套的接口调用模块会适时和android框架一起放出)

* 18.集成了ueditor,不用看官网文档,框架自动配置好,include相应jsp就可以用了,只需一行代码即可.

## 环境要求:

jdk1.8(要用1.7的改一下hibernate版本,最新hibernate不支持1.7,那些用jdk1.6的我就不吐槽了,什么年代了,还用这么古老的东西),mysql 5.5及以上(或者其他数据库,这里不提供其他数据库的初始数据文件,请自己生成)

## 部署:

* -1.请自己手动添加servlet容器jar包和junit jar包.

* 0.把项目编码改为UTF-8.

* 1.把/src/main/webapp/WEB-INF/doc/steedframework4web.sql导进数据库.

* 2.修改/src/main/resources/pool-one.properties数据库连接相关信息(也支持c3p0连接池,把/src/main/resources/hibernate.cfg.xml c3p0注释去掉,然后注释掉proxool即可)

* 3.修改/src/main/resources/properties/config.properties 中的serverEncoding和site.rootURL和aesKey(可以不修改,建议修改).
 
* 4.如果修改了aeskey(test包下面的config.properties也要改,因为是在test目录下面运行的)请运行steed.test.DatabaseTest.aesInit()(运行之前请手动添加junit jar包).

* 5.部署运行,默认账号:admin,密码:123456

## 使用说明:


自己看代码注释,或者参考steed.ext包下面的代码,steed.ext下面的就是例子兼基本功能实现

## 捐赠名单

- **疯狂的蛋鸡**      _6.66大洋_ 

