
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
                  <img style="display: inline-block;"
                    src="https://img.shields.io/badge/Zerosx_Cloud-0.0.1.Developer.Beta-critical" alt="">
                  <img style="display: inline-block;"
                    src="https://img.shields.io/badge/copyright-javacctvnews@163.com-blue" alt="">
                </div>
                <div class="imgs">
                  <img src="https://img.shields.io/badge/JDK-1.8+-blue?logo=openjdk" alt="">
                  <img src="https://img.shields.io/badge/Spring_Boot-2.7.14-blue?logo=spring-boot" alt="">
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
                  <div>1、基于JDK8、SpringBoot2.7.14、SpringCloud2021.0.5和SpringCloudAlibaba2021.0.5.0且采用前后端分离的微服务多租户体系架构；</div>
                  <div>2、采用自定义starter组件化的思想，结构解耦，易于扩展；</div>
                  <div>3、实现了SpringCloudGateway集成OAuth2.0统一认证授权及URL级功能权限校验（适用于网络隔离场景，即网关是统一入口），且可自定义扩展OAuth2.0授权模式；</div>
                  <div>4、集成Knife4j的OpenAPI3的接口文档，方便前后端对接，基础功能和文档注释完全由代码生成器一键生成（前后端代码）；</div>
                  <div>5、代码生成器一键生成基础CRUD功能，节约开发时间，更多注重业务开发；</div>
                  <div>6、提供一套基于Vue2.x的前端项目[zerosx-vue2]，此项目是参考【若依】前端开源改造，感谢若依，作者前端刚开始学习，勿喷.</div>
                </div>
              </el-collapse-item>

              <el-collapse-item title="功能简介" name="3">
                <ul>
                  <li>敬请期待</li>
                </ul>
              </el-collapse-item>
              <el-collapse-item title="软件定制开发合作" name="4">
                <ul>
                  <li>敬请期待</li>
                </ul>
              </el-collapse-item>
            </el-collapse>

          </basic-container>
          <basic-container>
            <div style="height: 315px;text-align:center;line-height: 300px;">内容区域3</div>
          </basic-container>
        </el-col>
        <el-col :span="8">
          <el-col :span="24">
            <basic-container>
              <div style="height: 1000px;text-align:center;">
                
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
</style>