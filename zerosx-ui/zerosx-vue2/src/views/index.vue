
<template>
  <div class="mainContainer">
    <basic-container class="topContainer">
      <p class="carouselCss">{{ currentLyric }}</p>
    </basic-container>
    <div>
      <el-row :span="24">
        <el-col :span="16">
          <basic-container>
            <el-collapse v-model="activeNames" @change="handleChange">
              <el-collapse-item title="欢迎来到Zerox-Cloud多租户开发平台" name="1">
                <div class="imgs">
                  <img style="display: inline-block;" src="https://img.shields.io/badge/Zerosx_Cloud-0.0.3-critical"
                    alt="">
                  <img style="display: inline-block;"
                    src="https://img.shields.io/badge/copyright-javacctvnews@163.com-blue" alt="">
                </div>
                <div class="imgs">
                  <img src="https://img.shields.io/badge/JDK-1.8+-blue?logo=openjdk" alt="">
                  <img src="https://img.shields.io/badge/Spring_Boot-2.7.16-blue?logo=spring-boot" alt="">
                  <img src="https://img.shields.io/badge/Spring_Cloud-2021.0.5-blue?logo=spring-boot" alt="">
                  <img src="https://img.shields.io/badge/Spring_Cloud_Alibaba-2021.0.5.0-blue?logo=alibabadotcom" alt="">
                  <img src="https://img.shields.io/badge/Nacos-2.2.0-critical?logo=alibabadotcom" alt="">
                </div>
                <div class="imgs">
                  <img src="https://img.shields.io/badge/Vue-2.x-green" alt="">
                  <img src="https://img.shields.io/badge/Vuex-3.x-blue" alt="">
                  <img src="https://img.shields.io/badge/Vue_Router-3.x-blue" alt="">
                  <img src="https://img.shields.io/badge/Element_UI-2.x-green" alt="">
                </div>
              </el-collapse-item>
              <el-collapse-item title="平台简介" name="2">
                <div>
                  <div>此平台是由作者个人兴趣下搭建及开发，开源，使用，仅供学习参考；</div>
                  <div>1、基于JDK8、SpringBoot2.7.16、SpringCloud2021.0.5和SpringCloudAlibaba2021.0.5.0且采用前后端分离的微服务多租户体系架构；
                  </div>
                  <div>2、采用自定义starter组件化的思想，结构解耦，易于扩展；</div>
                  <div>3、实现了SpringCloudGateway集成OAuth2.0统一认证授权及URL级功能权限校验（适用于网络隔离场景，即网关是统一入口），且可自定义扩展OAuth2.0授权模式；</div>
                  <div>4、集成Knife4j的OpenAPI3的接口文档，方便前后端对接；</div>
                  <div>5、代码生成器一键生成基础CRUD功能，节约开发时间，更多注重业务开发；</div>
                  <div>6、提供一套基于Vue2.x的前端项目[zerosx-vue2]，此项目是参考【若依】前端开源改造适配，感谢若依（我是Vue2.x初学者，还不具备从零搭建一个完整的Vue项目，所以目前只能借鉴改造，勿喷^_^）。</div>
                </div>
              </el-collapse-item>

              <el-collapse-item title="主要功能简介" name="3">
                <div>1、网关统一权限控制：URL级的功能按钮的权限控制，支持配置不同客户端及白名单URL；</div>
                <div>2、网关统一鉴权：OAuth2令牌Token的有效性校验，支持配置忽略认证的URL；</div>
                <div>3、OAuth2.0认证授权：
                  <div style="margin-left: 18px;">1）支持OAuth2.0默认的4种鉴权方式；</div>
                  <div style="margin-left: 18px;">2）可扩展自定义授权模式，已实现用户名+验证码+密码、手机号码验证码两种授权模式；</div>
                  <div style="margin-left: 18px;">3）支持多账户类型授权，应用场景是同一授权模式下需要对不同的账户进行授权（不同数据库表）；</div>
                </div>
                <div>4、数据权限：多租户的数据隔离，采用的是同一数据库表的逻辑隔离（table添加租户标识的列来区分数据）；</div>
                <div>5、数据安全：敏感数据的加密存储及解密，如手机号码、身份证号码等，不同加密字段可配置不同的加解密算法；</div>
                <div>6、数据导出：支持大量数据快速导出，支持配置每个sheet页的数据大小及每批次查询数据库数据条数大小；</div>
                <div>7、数据翻译：字段标识ID翻译成名称，已支持数据字典、枚举、行政区域、OSS访问链接、租户名称翻译，可扩展；</div>
                <div>8、链路追踪：记录整个调用链路的traceId和spanId，支持多线程；</div>
                <div>9、OSS存储：支持多家OSS厂商且参数配置界面化，方便管理及文件管理，已实现阿里云、七牛云、腾讯云厂商，可扩展；</div>
                <div>10、SMS短信：支持多家SMS厂商且参数配置界面化，方便管理，已实现阿里云、聚合短信厂商，可扩展；</div>
                <div>11、分布式任务调用：集成XXL-JOB分布式任务调度；</div>
                <div>12、分布式事务：集成Seata分布式事务；</div>
                <div>13、分布式锁：集成Redisson分布式锁，有测试案例；</div>
                <div>14、系统监控：集成SpringBootAdmin应用监控；</div>
                <div>15、代码生成器：高度符合阿里巴巴开发手册规范的前后端代码一键生成，减少CRUD基础开发，更专注业务开发；</div>
              </el-collapse-item>
              <el-collapse-item title="软件定制开发合作或其他咨询" name="4">
                <ul>
                  <li>请邮件联系：javacctvnews@163.com，谢谢！</li>
                </ul>
              </el-collapse-item>
            </el-collapse>

          </basic-container>
          <!-- <basic-container>
            <div style="height: 315px;text-align:center;line-height: 300px;">内容区域3</div>
          </basic-container> -->
        </el-col>
        <el-col :span="8">
          <el-col :span="24">
            <basic-container>
              <div style="height: 1000px;">
                <!-- <div class="block">
                  <el-timeline>
                    <el-timeline-item timestamp="2023/10/17" placement="top">
                      <el-card>
                        <h3>V0.0.2</h3>
                        <ol>
                          <li>分离zerosx-resource资源微服务</li>
                          <li>规范部分代码命名</li>
                          <li>修复encrypt组件验证BUG（丢失自增ID等一些字段的值）</li>
                          <li>导出功能增加nacos配置项</li>
                          <li>seata分布式事务案例优化</li>
                          <li>多租户前后逻辑优化（针对一个账户管理一个租户和多个租户情况）</li>
                          <li>个人中心模块优化（头像上传、基本信息展示更多信息等）</li>
                          <li>图片上传组件的删除按钮在删除时会删除服务器oss源文件</li>
                          <li>修复数据字典翻译feign接口BUG</li>
                          <li>补全更新md文档、数据库及YAML配置脚本</li>
                        </ol>
                      </el-card>
                    </el-timeline-item>
                    <el-timeline-item timestamp="2023/9/20" placement="top">
                      <el-card>
                        <h3>V0.0.1</h3>
                        <p>初始版本，包括非业务功能及内置业务功能</p>
                      </el-card>
                    </el-timeline-item>
                  </el-timeline>
                </div> -->
                <el-collapse title="版本更新日志" v-model="activeLogs">
                  <el-collapse-item title="V0.0.3" name="3">
                    <ul>
                      <li>1.JDK版本更新到JDK17</li>
                      <li>2.SpringBoot版本更新到3.x</li>
                      <li>3.SpringCloud&Alibaba版本更新到2022.x及更新对应的组件(Nacos等)</li>
                      <li>4.引入Spring Authorization Server认证授权</li>
                      <li>5.SpringCloudGateway集成Spring Authorization Server</li>
                      <li>6.[适配]用户名密码+验证码授权模式</li>
                      <li>7.[登录、登出、令牌管理、登录日志、客户端管理]等功能重新适配Spring Authorization Server</li>
                      <li>8.更新md文档、数据库及YAML配置脚本</li>
                    </ul>
                  </el-collapse-item>
                  <el-collapse-item title="V0.0.2" name="2">
                    <ul>
                      <li>1.分离zerosx-resource资源微服务</li>
                      <li>2.规范部分代码命名</li>
                      <li>3.修复encrypt组件验证BUG（丢失自增ID等一些字段的值）</li>
                      <li>4.导出功能增加nacos配置项</li>
                      <li>5.seata分布式事务案例优化</li>
                      <li>6.多租户前后逻辑优化（针对一个账户管理一个租户和多个租户情况）</li>
                      <li>7.个人中心模块优化（头像上传、基本信息展示更多信息等）</li>
                      <li>8.图片上传组件的删除按钮在删除时会删除服务器oss源文件</li>
                      <li>9.修复数据字典翻译feign接口BUG</li>
                      <li>10.补全更新md文档、数据库及YAML配置脚本</li>
                    </ul>
                  </el-collapse-item>
                  <el-collapse-item title="V0.0.1" name="1">
                    <ul>
                      <li>1.初始版本，包括非业务功能及内置业务功能</li>
                    </ul>
                  </el-collapse-item>
                </el-collapse>
              </div>
            </basic-container>
          </el-col>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import BasicContainer from '@/components/BasicContainer/index.vue'

export default {
  data() {
    return {
      activeNames: ['1', '2', '3', '4'],
      logActiveNames: ['32'],
      activeLogs: ['3'],
      lyrics: [
        '天青色等烟雨，而我在等你，炊烟袅袅升起，隔江千万里',
        '翻着我们的照片，想念若隐若现，去年的冬天，我们笑得很甜',
        '如果让我遇见你而你正当年轻，用最真的心换你最深的情',
        '如果一切可以重新来过，我希望我不曾出现在你的生命里',
        '苦海翻起爱恨，在世间难逃命运  一生所爱',
        '像我这样碌碌无为的人，你还见过多少人，像我这样不甘平凡的人，世界上有多少人'
      ]
    }

  },
  computed: {
    currentLyric() {
      return this.switchLyric();
    }
  },
  methods: {
    handleChange() {
    },
    switchLyric() {
      return this.lyrics[Math.floor(Math.random() * this.lyrics.length)];
    }
  }
}
</script>

<style lang="scss" scoped>
ul li {
  margin: 0;
  padding: 0;
}

.el-divider--horizontal {
  margin: 12px 0 !important;
}

.el-font-size {
  font-size: 14px;
}

.mainContainer {
  padding-right: 5px;
  padding-left: 3px;

  .topContainer {
    margin-top: 10px;
  }

  .imgs {
    margin-top: 8px;
    display: flex;
    // justify-content: center;

    img {
      margin-right: 8px;
      // margin: 0 auto;
    }
  }

  .carouselCss {
    background: linear-gradient(to right, red, orange, yellow, green, cyan, blue, purple);
    background-clip: text;
    -webkit-text-fill-color: transparent;
    animation: scroll 15s linear infinite;
    font-size: 25px;
  }

  @keyframes scroll {
    0% {
      transform: translateX(100%);
    }

    100% {
      transform: translateX(-100%);
    }
  }

  @keyframes hue {
    0% {
      filter: hue-rotate(0deg);
    }

    100% {
      filter: hue-rotate(360deg);
    }
  }
}

ul {
  padding-inline-start: 5px;
}
</style>
