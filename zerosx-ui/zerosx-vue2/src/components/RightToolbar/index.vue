<template>
  <div class="rightBtn">
    <el-col :span="1.5">
      <el-button type="primary" icon="el-icon-search" :loading="searching" size="mini" @click="handleQuery">搜索</el-button>
    </el-col>
    <el-col :span="1.5">
      <el-button icon="el-icon-refresh" :loading="searching" size="mini" @click="resetQuery">重置</el-button>
    </el-col>
  </div>
</template>
<script>
export default {
  name: "RightToolbar",
  data() {
    return {

    };
  },
  props: {
    searching: {
      type: Boolean,
      default: false
    }
  },
  computed: {

  },
  created() {

  },
  methods: {
    //搜索
    handleQuery() {
      this.$emit("handleQuery");
    },
    //重置
    resetQuery() {
      this.$emit("resetQuery");
    },
  },
};
</script>
<style lang="scss" scoped></style>
