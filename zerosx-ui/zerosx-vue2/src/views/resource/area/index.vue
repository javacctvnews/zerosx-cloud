<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 树形结构 -->
      <el-col :span="8" :xs="24" style="padding-left:0;padding-right: 0;">
        <BasicContainer style="">
          <div class="head-container">
            <el-input v-model="areaName" placeholder="请输入行政区域名称" clearable size="small" prefix-icon="el-icon-search"
              style="margin-bottom: 20px" />
          </div>
          <div class="head-container" style="height: calc(100vh - 200px);overflow-y: scroll;">
            <el-tree :data="areaOptions" :props="defaultProps" :expand-on-click-node="false" lazy :load="handleLoadNode"
              :filter-node-method="filterNode" ref="tree" node-key="id" highlight-current @node-click="handleNodeClick" />
          </div>
        </BasicContainer>
      </el-col>

      <el-col :span="16" :xs="24">
        <!-- <el-row :gutter="10" class="mb5">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增下级</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
              @click="handleUpdate">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-edit" size="mini" :disabled="single"
              @click="handleDelete">删除</el-button>
          </el-col>
        </el-row> -->

        <el-form ref="form" style="margin-top: 10px;" label-position="right" label-width="100px" :model="form"
          size="small">
          <el-form-item label="父区域编号">
            <el-input v-model="form.parentAreaCode" disabled />
          </el-form-item>
          <el-form-item label="父区域名称">
            <el-input v-model="form.parentAreaName" disabled />
          </el-form-item>
          <el-form-item label="区域等级" prop="deep">
            <el-radio-group v-model="form.deep">
              <el-radio v-for="dict in dict.type.area_deep" :key="dict.value" :label="dict.value">{{ dict.label
              }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="区域编号">
            <el-input v-model="form.areaCode" />
          </el-form-item>
          <el-form-item label="区域名称">
            <el-input v-model="form.areaName" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增父区域下级</el-button>
            <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
              @click="handleUpdate">修改</el-button>
            <el-button type="danger" plain icon="el-icon-edit" size="mini" :disabled="single"
              @click="handleDelete">删除</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetForm">清空</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { areaTree, addArea, updateArea, deleteArea } from '@/api/system/area.js'
import BasicContainer from '@/components/BasicContainer/index'
export default {
  dicts: ['area_deep'],
  components: {
    BasicContainer
  },
  data() {
    return {
      single: false,
      areaName: '',
      areaOptions: [],
      defaultProps: {
        children: "children",
        label: "title",
        isLeaf: function (data) {
          return !data.hasChildren;
        }
      },
      form: {
        id: '',
        parentAreaCode: '',
        parentAreaName: '',
        areaCode: '',
        areaName: '',
        deep: ''
      },
    }
  },
  watch: {
    areaName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getAreaTree()
  },
  methods: {
    handleLoadNode(node, resolve) {
      let data = node.data;
      if (!data.key) {
        return;
      }
      areaTree(data.key).then((res) => {
        resolve(res.data)
      })
    },
    handleNodeClick(data) {
      //父节点
      this.form.id = data.id;
      this.form.parentAreaCode = data.parentId;
      this.form.parentAreaName = data.parentTitle
      //当前节点信息
      this.form.areaName = data.title;
      this.form.areaCode = data.key;
      this.form.deep = data.deep + '';
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.title.indexOf(value) !== -1;
    },
    getAreaTree() {
      let searchNode = this.areaName || '000000';
      areaTree(searchNode).then((res) => {
        this.areaOptions = res.data
      })
    },
    checkSelect() {
      if (!this.form.areaName) {
        this.$message({
          message: '请选择一个区域节点',
          type: 'warning'
        });
        return false;
      }
      return true;
    },
    handleSubmit() { },
    resetForm() {
      this.reset()
    },
    handleDelete() {
      if (this.checkSelect()) {
        this.$message({
          message: '敬请期待',
          type: 'warning'
        });
        // this.$modal.confirm('是否确认删除区域名称为"【' + this.form.areaName + '】"的数据项？').then(function () {
        //   console.log("执行删除")
        //   return deleteArea(this.form.id);
        // }).then(() => {
        //   this.$modal.msgSuccess("删除成功");
        //   this.getAreaTree()
        // }).catch(() => { });
      }
    },
    //新增下级
    handleAdd() {
      if (this.checkSelect()) {
        this.$message({
          message: '敬请期待',
          type: 'warning'
        });
        // if (this.form.deep === '3') {
        //   this.$message({
        //     message: '无法增加【镇】的区域数据',
        //     type: 'warning'
        //   });
        //   return;
        // }
        // //提交表单
        // addArea(this.form).then((res) => {
        //   this.$modal.msgSuccess("新增成功");
        //   this.getAreaTree()
        // })
      }
    },
    handleUpdate() {
      if (this.checkSelect()) {
        this.$message({
          message: '敬请期待',
          type: 'warning'
        });
        // if (this.checkSelect) {
        //   //提交表单
        //   updateArea(this.form).then((res) => {
        //     this.$modal.msgSuccess("编辑成功");
        //     this.getAreaTree()
        //   })
        // }
      }
    },
    reset() {
      this.form = {
        parentAreaCode: '',
        parentAreaName: '',
        areaCode: '',
        areaName: '',
        deep: '',
        id: ''
      }
    }
  }
}
</script>

<style scoped lang="scss">
.textMarks {
  padding: 8px 16px;
  background-color: #ecf8ff;
  border-radius: 4px;
  border-left: 5px solid #50bfff;
  margin-bottom: 5px;
}
</style>