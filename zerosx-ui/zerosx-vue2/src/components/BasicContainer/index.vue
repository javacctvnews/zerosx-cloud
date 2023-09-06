<template>
  <div class="basic-container" :style="styleName" :class="{ 'basic-container--block': block }">
    <el-card class="basic-container__card">
      <slot></slot>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "BasicContainer",
  props: {
    radius: {
      type: [String, Number],
      default: 10
    },
    background: {
      type: String
    },
    block: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    styleName() {
      return {
        borderRadius: `${this.radius}px`,
        background: this.background,
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.basic-container {
  padding: 5px 5px;
  box-sizing: border-box;

  
 .el-card__body {
 	padding: 10px !important;
 }

  &--block {
    height: 100%;

    .basic-container__card {
      height: 100%;
    }
  }

  &__card {
    width: 100%;
  }

  &:first-child {
    padding-top: 0;
  }
}
</style>