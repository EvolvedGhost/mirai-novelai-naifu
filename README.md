# mirai-novelai-naifu

一个以 [Mirai-Console](https://github.com/mamoe/mirai) 为基础开发，对接NovelAi（NAIFU）的机器人

## 开始使用

插件需要以 [Mirai-Console](https://github.com/mamoe/mirai)

为基础，你可以下载 [MCL](https://github.com/iTXTech/mirai-console-loader/releases) 作为你的Mirai插件载入器

与此同时，本插件需要 Mirai 官方插件 [chat-command](https://github.com/project-mirai/chat-command)

使用之前请先用参考[权限节点](#权限节点)来开启插件权限

**请注意：当用户权限大于等于机器人所在权限时（如机器人是管理员用户为群主），机器人不会对游戏请求做任何回复**

## 权限节点

```
 |- com.evolvedghost.mirai-novelai-naifu:*            The base permission
 |  |- com.evolvedghost.mirai-novelai-naifu:command.ai
 |  |  |  `- MiraiNAIFU指令
```

插件调用的是Mirai自带的权限管理系统，你可以在控制台输入`?`来获取帮助，通常权限的添加方式为：

`/permission add <被许可人 ID> <权限 ID>    # 授权一个权限`

<被许可人 ID> 可以为QQ号或者通配符`*`来代表所有用户

<权限 ID> 即上述权限名称，可按照需求添加

一个例子为：`/permission add * com.evolvedghost.mirai-novelai-naifu:*`，即为所有用户添加本插件所有权限

## 插件命令

| 指令                         | 默认别名                    | 功能                    |
|----------------------------|-------------------------|-----------------------|
| `/ai image <tags> <image>` | `/ai 图片 <tags> <image>` | 以图生图                  |
| `/ai text <tags>`          | `/ai 文本 <tags>`         | 以文本生图                 |
| `/ai tag <tags>`           | `/ai 标签 <tags>`         | 搜索Tag名称               |
| `/ai custom <key> <value>` | `/ai 自设 <key> <value>`  | 自定义设置ai绘图参数           |
| `/ai switch`               | `/ai 开关`                | 开关本群ai绘图功能            |
| `/ai reload`               | `/ai 重载`                | 快速重载插件配置项（不需要重启Mirai） |

## 插件配置

插件的配置项储存在`config\com.evolvedghost.mirai-novelai-naifu`目录下

可通过配置来实现部分Tag禁用、群白名单等功能

## 编译

如果您需要自行编译，使用在文件目录使用以下命令即可

Windows：`./gradlew.bat buildPlugin`

Linux：`./gradlew buildPlugin`

编译好的jar文件可以在`/build/mirai`下找到
