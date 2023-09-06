<template>
  <div class="table-plus">
    <el-table ref="tablePlus" :header-cell-style="{ padding: '0px' }" v-bind="$attrs" stripe height="100%" style="width: 100%"
      :border="border" v-on="$listeners" size="mini" 
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange" 
      :default-sort="defaultSort" >
      <el-table-column align="center" fixed type="selection" width="50"></el-table-column>
      <el-table-column align="center" v-if="!hideIndex" type="index" width="55" label="序号"></el-table-column>
      <template v-for="(column, index) in columns">
        <el-table-column align="center"
          v-if="column.type && column.type === 'action' && column.actions && column.actions.length > 0" :key="index"
          label="操作栏" fixed="right" :width="actionWidth">
          <template slot-scope="scope">
            <template v-for="(action, index2) in column.actions">
              <!--操作按钮-->
              <template v-if="!action.show || action.show(scope.row)">
                <template v-if="action.type === 'custom'">
                  <el-button type="text" v-if="!action.slot" :key="index2" v-bind="action.attrs || {}" size="mini"
                    @click="triggerAction(action, scope)">{{ action.name }}
                  </el-button>
                  <template v-else>
                    <slot :name="action.slot" v-bind="scope" />
                  </template>
                </template>
                <!--编辑-->
                <el-button v-if="action.type === 'edit'" :key="index2" size="mini" type="text" icon="el-icon-edit"
                  @click="triggerAction(action, scope)">{{ action.name }}
                </el-button>
                <!--详情-->
                <el-button type="text" v-if="action.type === 'detail'" :key="index2" size="mini" icon="el-icon-view"
                  style="color: #2e78ef" @click="triggerAction(action, scope)">{{ action.name }}
                </el-button>
                <!--删除-->
                <el-button v-if="action.type === 'delete'" :key="index2" size="mini" type="text" icon="el-icon-delete"
                  style="color:#ff4949" @click="triggerAction(action, scope)">{{ action.name }}
                </el-button>
              </template>
            </template>
          </template>
        </el-table-column>

        <template v-else-if="!column.attrs.hidden">
          <el-table-column v-if="!column.slot" :key="index" v-bind="column.attrs || {}" />

          <el-table-column v-else v-bind="column.attrs || {}">
            <template slot-scope="scope">
              <slot :name="column.slot" v-bind="scope" />
            </template>
          </el-table-column>
        </template>
      </template>
    </el-table>
  </div>
</template>
<script>
export default {
  name: "TablePlus",
  props: {
    border: {
      type: Boolean,
      default: true,
    },
    columns: {
      type: Array,
      required: true,
    },
    actionWidth: {
      type: String,
      required: false,
      default: "120px",
    },
    hideIndex: {
      type: Boolean,
      default: false,
    },
    defaultSort: {
      type: Object,
      required: false
    }
  },
  created() {
    //console.log(this.defaultSort)
  },
  methods: {
    // 触发action
    triggerAction(action, scope) {
      this.$emit(action.fcn, Object.assign({}, scope.row));
    },
    handleSelectionChange(val) {
      this.$emit("handleSelectionChange", val);
    },
    //排序
    handleSortChange(sortProp) {
      this.$emit("handleSortChange", sortProp);
    }
  },
};
</script>

<style lang="scss" scoped>
.table-plus {
  height: 100%;
}
</style>