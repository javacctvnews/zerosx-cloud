<template>
  <div>
    <svg-icon icon-class="gitee" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'ZerosxGitee',
  data() {
    return {
      url: 'https://gitee.com/javacctvnews/zerosx-cloud'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
