<template>
  <div class="app-container iframe-class">
    <iframe :src="iframeUrl" width="100%" height="100%" frameborder="0" :loading="loading"></iframe>
  </div>
</template>

<script>
import { queryByKey } from '@/api/system/sysParam.js'

export default {
  data() {
    return {
      loading: false,
      iframeUrl: ''
    }
  },
  created() {
    this.getSrc();
    this.loading = true;
  },
  methods: {

    getSrc() {
      let params = {
        paramKey: 'openapi3-url',
        paramScope: '0'
      }
      this.loading = true;
      queryByKey(params).then((res) => {
        if (res.data) {
          this.iframeUrl = res.data.paramValue
        }
        this.loading = false;
      }).catch(err => {
        this.loading = false;
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