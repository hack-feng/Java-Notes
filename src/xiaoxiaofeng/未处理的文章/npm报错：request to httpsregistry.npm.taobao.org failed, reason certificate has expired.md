## 1. 问题

`npm i` 报错：`request to https://registry.npm.taobao.org/@npmcli%2fmove-file failed, reason: certificate has expired`

![image-20240202145619910](https://image.xiaoxiaofeng.site/blog/2024/02/02/xxf-20240202145619.png?xxfjava)

## 2. 原因

错误提示已经告诉原因是淘宝镜像过期了！

其实，早在 2021 年，淘宝就发文称，npm 淘宝镜像已经从 registry.npm.taobao.org 切换到了 registry.npmmirror.com。旧域名也将于 2022 年 5 月 31 日停止服务（不过，直到今天 HTTPS 证书到期才真正不能用了）

![image-20240202145712350](https://image.xiaoxiaofeng.site/blog/2024/02/02/xxf-20240202145712.png?xxfjava)

## 3. 解决方案

3.1 查看当前的npm镜像设置：`npm config list`

![image-20240202145803510](https://image.xiaoxiaofeng.site/blog/2024/02/02/xxf-20240202145803.png?xxfjava)

3.2 清空缓存：`npm cache clean --force`

3.3 然后修改镜像即可：`npm config set registry https://registry.npmjs.org/` （或`npm config delete registry`）

> ！！！注意：此处修改的镜像用的是npm本身，一般国内用户还是建议使用淘宝镜像，所以推荐还是设置成用淘宝镜像，执行：`npm config set registry https://registry.npmmirror.com` 【推荐】

3.4 再次运行： `npm config list`，查看 registry 已经被更改为默认的 npm 公共镜像地址。

![image-20240202145907976](https://image.xiaoxiaofeng.site/blog/2024/02/02/xxf-20240202145908.png?xxfjava)

然后就可以正常使用npm啦～