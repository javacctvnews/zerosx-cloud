<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form label-position="left" :model="queryParams.t" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="登录地址" prop="sourceLocation">
        <el-input v-model="queryParams.t.sourceLocation" placeholder="请输入登录地址" clearable style="width: 220px;"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="用户名称" prop="username">
        <el-input v-model="queryParams.t.username" placeholder="请输入用户名称" clearable style="width: 220px;"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="登录结果" prop="status">
        <el-select v-model="queryParams.t.status" placeholder="登录结果" clearable style="width: 220px">
          <el-option v-for="dict in dict.type.OperatorResEnum" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="登录时间">
        <el-date-picker v-model="dateRange" style="width: 220px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']"></el-date-picker>
      </el-form-item>
    </el-form>
    <!-- 操作按钮栏 -->
    <el-row ref="operations" :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['system:operatorlog:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleClean"
          v-hasPerms="['system:operatorlog:clean']">清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['system:operatorlog:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="handleSortChange"
        @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div style="height: calc(100vh - 220px);">
      <TablePlus ref="tables" :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleSelectionChange="handleSelectionChange" @sort-change="handleSortChange" :defaultSort="defaultSort"
        actionWidth="80px">
        <template slot-scope="scope" slot="oauthResult">
          <dict-tag :options="dict.type.OperatorResEnum" :value="scope.row.oauthResult" />
        </template>
      </TablePlus>
    </div>

    <!-- 分页器 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>

<script>
import { list, delLogininfor, cleanLoginLog } from "@/api/system/oauthTokenRecord";
import serviceConfig from '@/api/serviceConfig';

export default {
  name: "OauthTokenRecord",
  dicts: ['OperatorResEnum'],
  data() {
    return {
      // 遮罩层
      searching: false,
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 选择用户名
      selectName: "",
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 日期范围
      dateRange: ['', ''],
      // 默认排序
      defaultSort: { prop: 'applyOauthTime', order: 'descending' },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          sourceLocation: undefined,
          username: undefined,
          status: undefined
        }
      },
      columns: [
        // {
        //   attrs: {
        //     label: '记录ID',
        //     prop: 'id',
        //     minWidth: '100',
        //     sortable: 'custom',
        //     align: "center",
        //   },
        // },
        {
          attrs: {
            label: '访问编号',
            prop: 'requestId',
            minWidth: '160',
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: '用户名',
            prop: 'username',
            minWidth: '140',
            align: "center",
          },
        },
        {
          slot: 'oauthResult',
          attrs: {
            label: '登录结果',
            prop: 'oauthResult',
            minWidth: '120',
            align: "center",
          },
        },
        {
          attrs: {
            label: '登录结果描述',
            prop: 'oauthMsg',
            minWidth: '280',
            align: "center",
          },
        },
        {
          attrs: {
            label: 'IP归属',
            prop: 'sourceLocation',
            minWidth: '140',
            align: "center",
          },
        },
        {
          attrs: {
            label: 'IP',
            prop: 'sourceIp',
            minWidth: '140',
            align: "center",
          }
        },
        {
          attrs: {
            label: '登录时间',
            prop: 'applyOauthTime',
            minWidth: '140',
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: '授权ClientId',
            prop: 'clientId',
            minWidth: '140',
            align: "center",
          },
        },
        {
          attrs: {
            label: '授权类型',
            prop: 'grantType',
            minWidth: '160',
            align: "center",
          },
        },
        {
          attrs: {
            label: 'Token值',
            prop: 'tokenValue',
            minWidth: '280',
            showOverflowTooltip: true,
            align: "center",
          },
        },
        {
          attrs: {
            label: '操作系统',
            prop: 'osType',
            minWidth: '180',
            align: "center",
          },
        },
        {
          attrs: {
            label: '浏览器',
            prop: 'browserType',
            minWidth: '180',
            align: "center",
          },
        },
        {
          attrs: {
            label: '租户公司',
            prop: 'operatorName',
            minWidth: '140',
            showOverflowTooltip: true,
            align: "center",
          },
        },
        {
          type: 'action',
          actions: [
            {
              type: 'delete',
              name: '删除',
              fcn: 'handleDelete',
              show: () => this.checkPermi(['system:operatorlog:delete'])
            }
          ]
        }
      ]
    };
  },

  created() {
    this.handleSortChange();
    //console.log(this)
  },

  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      this.searching = true;
      this.dateRange = Array.isArray(this.dateRange) ? this.dateRange : [];
      this.queryParams.t.beginOauthApplyTime = this.dateRange[0]
      this.queryParams.t.endOauthApplyTime = this.dateRange[1]
      list(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
        this.searching = false;
      }).catch(err => {
        this.loading = false;
        this.searching = false;
      });
    },
    //排序参数
    getSort(sortProp) {
      this.queryParams.sortList = [];
      this.queryParams.sortList.push({ orderByColumn: sortProp.prop, sortType: sortProp.order });
    },
    // 处理排序
    handleSortChange(sortProp) {
      this.getSort(sortProp || this.defaultSort);
      this.getList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      //清除排序（默认自动排序的列）
      this.$refs.tables.$refs.tablePlus.sort(this.defaultSort.prop, this.defaultSort.order)
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
      this.selectName = selection.map(item => item.userName);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const infoIds = row.id || this.ids;
      this.$modal.confirm('是否确认删除已选择的数据项？').then(function () {
        return delLogininfor(infoIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal.confirm('是否确认清空所有登录日志数据项？').then(function () {
        return cleanLoginLog();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("清空成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      let name = '登录日志_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.auth + '/oauth_token_record/export', this.queryParams, name)
    }
  }
};
</script>

<style lang="scss" scoped></style>
