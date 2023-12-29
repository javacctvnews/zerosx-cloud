<template>
  <div class="app-container">
    <basic-container style="height: calc(100 - 70px);">
      <el-row class="vp-features">
        <div class="title">可视化监控</div>
        <el-col :sm="12" :lg="6">
          <div class="vp-feature-item" @click="getSrc('grafana-url')">
            <el-result title="Grafana" subTitle="一种流行的开源数据可视化和监控平台">
              <template slot="icon">
                <img src="../../../assets/images/grafana.jpg" />
              </template>
              <!-- <template slot="extra">
                <el-button type="primary" size="medium" @click="getSrc('grafana-url')">打开</el-button>
              </template> -->
            </el-result>
          </div>
        </el-col>
        <el-col :sm="12" :lg="6">
          <div class="vp-feature-item" @click="getSrc('prometheus-url')">
            <el-result title="Prometheus" subTitle="一个开源的服务监控系统和时间序列数据库">
              <template slot="icon">
                <img src="../../../assets/images/prometheus.png" />
              </template>
              <!-- <template slot="extra">
                <el-button type="primary" size="medium" @click="getSrc('prometheus-url')">打开</el-button>
              </template> -->
            </el-result>
          </div>
        </el-col>
        <el-col :sm="12" :lg="6">
          <div class="vp-feature-item" @click="getSrc('monitor-url')">
            <el-result title="SpringBootAdmin" subTitle="一个管理和监控Spring Boot应用程序的开源软件">
              <template slot="icon">
                <img src="../../../assets/images/icon-spring-boot-admin.svg" />
              </template>
              <!-- <template slot="extra">
                <el-button type="primary" size="medium" @click="getSrc('monitor-url')">打开</el-button>
              </template> -->
            </el-result>
          </div>
        </el-col>
      </el-row>
      <el-row class="vp-features">
        <div class="title">其他</div>
        <el-col :sm="12" :lg="6">
          <div class="vp-feature-item" @click="getSrc('job-url')">
            <el-result title="任务调度中心" subTitle="XXL-JOB是一个轻量级的分布式任务调度平台">
              <template slot="icon">
                <img style="width: 80px;height: 50px;" src="../../../assets/images/xxl-logo.jpg" />
              </template>
              <!-- <template slot="extra">
                <el-button type="primary" size="medium" @click="getSrc('job-url')">打开</el-button>
              </template> -->
            </el-result>
          </div>
        </el-col>
        <el-col :sm="12" :lg="6">
          <div class="vp-feature-item" @click="getSrc('openapi3-url')">
            <el-result title="文档中心" subTitle="Knife4j是一个集Swagger2和OpenAPI3为一体的增强解决方案">
              <template slot="icon">
                <img style="width: 90px;height: 50px;" src="../../../assets/images/knife4j.png" />
              </template>
              <!-- <template slot="extra">
                <el-button type="primary" size="medium" @click="getSrc('openapi3-url')">打开</el-button>
              </template> -->
            </el-result>
          </div>
        </el-col>
      </el-row>
    </basic-container>

  </div>
</template>

<script>
import { queryByKey } from '@/api/system/sysParam.js'

export default {
  data() {
    return {
      
    }
  },

  methods: {
    getSrc(paramKey) {
      let params = {
        paramKey: paramKey,
        paramScope: '0'
      }
      queryByKey(params).then((res) => {
        if (res.data) {
          let url = res.data.paramValue;
          window.open(url, '_blank');
        }
      }).catch(err => {

      })
    }
  }
}
</script>

<style scoped lang="scss">
.title {
  border-bottom: 1px solid #eceef1;
  padding-bottom: 5px;
  font-weight: 600;
}

img {
  width: 60px;
  height: 40px;
}

.vp-features {
  .vp-feature-item {
    margin: 15px;
    transition: background var(--color-transition), box-shadow var(--color-transition), transform var(--transform-transition);
  }

  .vp-feature-item:hover {
    margin: 15px;
    background-color: var(--bg-color-secondary);
    box-shadow: 0 2px 12px 0 var(--card-shadow);
    transform: translate(-2px, -2px);
    transform: scale(1.05);
    background-color: #F8F8FF;
    color: green;
    cursor: pointer;
  }

}
</style>