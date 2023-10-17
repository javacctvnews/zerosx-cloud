<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form label-position="left" :model="queryParams.t" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="系统模块" prop="title">
        <el-input v-model="queryParams.t.title" placeholder="请输入系统模块" clearable style="width: 220px;"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="操作人员" prop="operatorName">
        <el-input v-model="queryParams.t.operatorName" placeholder="请输入操作人员" clearable style="width: 220px;"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="类型" prop="businessType">
        <el-select v-model="queryParams.t.businessType" placeholder="操作类型" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.OpTypeEnum" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.t.status" placeholder="操作状态" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.OperatorResEnum" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="操作时间">
        <el-date-picker v-model="dateRange" style="width: 220px;" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']"></el-date-picker>
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['system:loginlog:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleClean"
          v-hasPerms="['system:loginlog:clean']">清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['system:loginin:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
        @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div style="height: calc(100vh - 265px);">
      <TablePlus ref="tables" :data="list" :columns="columns" v-loading="loading" @handleView="handleView"
        @handleDelete="handleDelete" @handleSelectionChange="handleSelectionChange" @sort-change="handleSortChange"
        :defaultSort="defaultSort" actionWidth="120px">
        <template slot-scope="scope" slot="businessType">
          <dict-tag :options="dict.type.OpTypeEnum" :value="scope.row.businessType" />
        </template>
        <template slot-scope="scope" slot="status">
          <dict-tag :options="dict.type.OperatorResEnum" :value="scope.row.status" />
        </template>
        <template slot-scope="scope" slot="costTime">
          <span>{{ scope.row.costTime }} 毫秒</span>
        </template>
      </TablePlus>
    </div>

    <!-- 分页器 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 操作日志详细（dialog） -->
    <el-dialog title="操作日志详细" :visible.sync="open" width="1000px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="操作模块：">{{ form.title }} / {{ typeFormat(form) }}</el-form-item>
            <el-form-item label="用户信息：">{{ form.operatorName }} / {{ form.operatorIdName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求地址：">{{ form.operatorUrl }}</el-form-item>
            <el-form-item label="请求方式：">{{ form.requestMethod }} / {{ form.operatorIp }}</el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="操作状态：">
              <div v-if="form.status === 0">正常</div>
              <div v-else-if="form.status === 1">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="消耗时间：">{{ form.costTime }}毫秒</el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="操作时间：">{{ parseTime(form.operatorTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="操作方法：">{{ form.methodName }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="请求参数：">{{ form.operatorParam }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="返回参数：">{{ form.jsonResult }}</el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="异常信息：" v-if="form.status === 1">{{ form.errorMsg }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, delOperlog, cleanOperlog, queryById } from "@/api/system/systemOperatorLog";
import serviceConfig from '@/api/serviceConfig'
export default {
  name: "SystemOperatorLog",
  dicts: ['OpTypeEnum', 'OperatorResEnum'],
  data() {
    return {
      searching: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: ['', ''],
      defaultSort: {
        prop: 'operatorTime',
        order: 'descending'
      },
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          title: undefined,
          operatorName: undefined,
          businessType: undefined,
          status: undefined
        }
      },
      columns: [
        {
          attrs: {
            label: '记录ID',
            prop: 'id',
            minWidth: '100',
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: '系统模块',
            prop: 'title',
            minWidth: '140',
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: '操作按钮',
            prop: 'btnName',
            minWidth: '140',
            sortable: 'custom',
            align: "center",
          },
        }, {
          slot: 'businessType',
          attrs: {
            label: '操作类型',
            prop: 'businessType',
            minWidth: '120',
            align: "center",
          },
        },
        {
          slot: 'status',
          attrs: {
            label: '操作状态',
            prop: 'status',
            minWidth: '120',
            align: "center",
          },
        },
        {
          slot: 'costTime',
          attrs: {
            label: '消耗时间',
            prop: 'costTime',
            minWidth: '120',
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: '请求方式',
            prop: 'requestMethod',
            minWidth: '120',
            align: "center",
          },
        },
        {
          attrs: {
            label: '操作人员',
            prop: 'operatorName',
            minWidth: '110',
            align: "center",
          },
        },
        {
          attrs: {
            label: '主机归属',
            prop: 'ipLocation',
            minWidth: '130',
            align: "center",
          },
        },
        {
          attrs: {
            label: '主机',
            prop: 'operatorIp',
            minWidth: '130',
            align: "center",
          },
        },
        {
          attrs: {
            label: '操作日期',
            prop: 'operatorTime',
            minWidth: '140',
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: '请求URL',
            prop: 'operatorUrl',
            minWidth: '280',
            align: "center",
          },
        },
        {
          attrs: {
            label: '方法名称',
            prop: 'methodName',
            minWidth: '280',
            align: "center",
          },
        },
        {
          attrs: {
            label: '租户公司',
            prop: 'operatorIdName',
            minWidth: '140',
            showOverflowTooltip: true,
            align: "center",
          },
        },
        {
          type: 'action',
          actions: [
            {
              type: 'detail',
              name: '详情',
              fcn: 'handleView'
            },
            {
              type: 'delete',
              name: '删除',
              fcn: 'handleDelete',
              show: () => this.checkPermi(['system:loginlog:delete'])
            }
          ]
        }
      ]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleSortChange(sortProp) {
      this.queryParams.sortList = [];
      this.queryParams.sortList.push({ orderByColumn: sortProp.prop, sortType: sortProp.order });
      this.getList();
    },
    /** 查询登录日志 */
    getList() {
      this.loading = true;
      this.searching = true;
      //添加默认排序
      if (this.queryParams.sortList.length == 0) {
        this.queryParams.sortList.push({ orderByColumn: this.defaultSort.prop, sortType: this.defaultSort.order });
      }
      this.dateRange = Array.isArray(this.dateRange) ? this.dateRange : [];
      this.queryParams.t.beginOperatorTime = this.dateRange[0]
      this.queryParams.t.endOperatorTime = this.dateRange[1]
      list(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
        this.searching = false;
      }).catch(() => {
        this.loading = false
        this.searching = false;
      });
    },
    // 操作日志类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.dict.type.OpTypeEnum, row.businessType);
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
      this.$refs.tables.$refs.tablePlus.sort(this.defaultSort.prop, this.defaultSort.order);
      //this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length
    },
    /** 详细按钮操作 */
    handleView(row) {
      queryById(row.id).then((res) => {
        this.form = res.data;
      });
      this.open = true;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const operIds = row.id || this.ids;
      this.$modal.confirm('是否确认删除日志编号为"' + operIds + '"的数据项？').then(function () {
        return delOperlog(operIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal.confirm('是否确认清空所有操作日志数据项？').then(function () {
        return cleanOperlog();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("清空成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      let name = '操作日志_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.system + '/system_operator_log/export', this.queryParams, name)
    },
       /** 导出按钮操作 */
    handleExportV2() {
      let name = '操作日志_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.system + '/system_operator_log/export_v2', this.queryParams, name)
    }
  }
};
</script>

