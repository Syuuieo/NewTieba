# NewTieba

一个现代化的 Android 第三方百度贴吧客户端。

## 特性

- 🎨 HyperOS 风格 UI（基于 Miuix）
- 📱 Compose 原生开发
- 🏗️ Clean Architecture + MVVM + Repository Pattern
- 🚀 高性能（Paging3、Coil、Room）
- 🌙 深色模式支持
- 🎯 动态取色
- 📱 平板/折叠屏适配

## 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose + Miuix
- **架构**: Clean Architecture + MVVM + MVI
- **网络**: Retrofit + OkHttp + Protobuf
- **数据库**: Room
- **依赖注入**: Hilt
- **图片加载**: Coil
- **分页**: Paging3
- **协程**: Kotlin Coroutines + Flow

## 项目结构

```
NewTieba/
├── app/                    # 应用入口
├── core/
│   ├── common/             # 通用工具和模型
│   ├── ui/                 # UI 组件库
│   └── testing/            # 测试工具
├── protocol/               # 贴吧协议层
├── network/                # 网络层封装
├── database/               # 本地数据库
├── domain/                 # 业务逻辑层
├── data/                   # 数据层实现
└── feature/
    ├── home/               # 首页
    ├── forum/              # 吧详情
    ├── thread/             # 帖子详情
    ├── search/             # 搜索
    ├── profile/            # 用户主页
    ├── message/            # 消息
    ├── login/              # 登录
    └── settings/           # 设置
```

## 构建要求

- Android Studio Hedgehog | 2023.1.1 或更高版本
- JDK 17
- Android SDK 35
- Kotlin 2.1.0

## 构建步骤

1. 克隆项目
```bash
git clone https://github.com/yourusername/NewTieba.git
```

2. 使用 Android Studio 打开项目

3. 同步 Gradle 文件

4. 运行项目

## 许可证

MIT License

## 致谢

- [aiotieba](https://github.com/lumina37/aiotieba) - 贴吧协议参考
- [TiebaLite](https://github.com/zzc10086/TiebaLite) - 业务逻辑参考
- [Miuix](https://github.com/compose-miuix-ui/miuix) - UI 框架
