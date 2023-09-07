<template>
  <div class="app-container iframe-class">
    <iframe :src="iframeUrl" width="100%" height="100%" frameborder="0"></iframe>
  </div>
</template>

<script>
import { queryByKey } from '@/api/system/sysParam.js'

export default {
  data() {
    return {
      iframeUrl: ''
    }
  },
  created() {
    this.getSrc();
  },
  methods: {

    getSrc() {
      let params = {
        paramKey: 'monitor-url',
        paramScope: '0'
      }
      queryByKey(params).then((res) => {
        if (res.data) {
          this.iframeUrl = res.data.paramValue
        }
      }).catch(err => {

      })
    }
  }
}
</script>

<style scoped lang="scss">
.iframe-class {
  height: calc(100vh - 84px);
  padding: 0;
  box-shadow: -4px 5px 10px rgba(0, 0, 0, .4);
}

</style>