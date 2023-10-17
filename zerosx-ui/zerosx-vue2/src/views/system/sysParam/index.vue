<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams.t" label-position="left" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="参数名" prop="paramName">
        <el-input v-model="queryParams.t.paramName" placeholder="请输入参数名或编码关键字" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="参数范围" prop="paramScope">
        <el-select v-model="queryParams.t.paramScope" placeholder="参数范围" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.ParamScopeEnum" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.t.status" placeholder="状态" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="租户公司" prop="operatorId">
        <el-select clearable v-model="queryParams.t.operatorId" placeholder="请选择租户公司" style="width: 220px;">
          <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPerms="['system:param:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPerms="['system:param:update']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['system:param:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['system:param:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
        @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div class="componentTable" style="height: calc(100vh - 220px)">
      <TablePlus ref="tables" :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleSelectionChange="handleSelectionChange" @sort-change="handleSortChange" :defaultSort="defaultSort"
        actionWidth="80px">
        <template slot-scope="scope" slot="status">
          <dict-tag :options="dict.type.StatusEnum" :value="scope.row.status" />
        </template>
        <template slot-scope="scope" slot="paramScope">
          <dict-tag :options="dict.type.ParamScopeEnum" :value="scope.row.paramScope" />
        </template>
      </TablePlus>
    </div>

    <!-- 分页器 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 新增or删除 -->
    <el-dialog center :lock-scroll="true" :title="title" :visible.sync="open" width="700px" append-to-body
      :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="110px" size="small" :rules="rules">
        <el-form-item label="参数范围" prop="paramScope">
          <el-radio-group v-model="form.paramScope">
            <el-radio v-for="dict in dict.type.ParamScopeEnum" :key="dict.value" :label="dict.value">{{ dict.label
            }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="form.paramScope === '1'" label="租户公司" prop="operatorId">
          <el-select clearable v-model="form.operatorId" placeholder="请选择租户公司" style="width: 100%;">
            <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="参数名" prop="paramName">
          <el-input v-model="form.paramName" placeholder="请输入参数名" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="参数编码" prop="paramKey">
          <el-input v-model="form.paramKey" placeholder="请输入参数编码" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="参数值" prop="paramValue">
          <el-input v-model="form.paramValue" placeholder="请输入参数值" maxlength="100" show-word-limit />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.value">{{ dict.label
            }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注说明" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注说明" maxlength="100" show-word-limit />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="dialogLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { pageList, addSysParam, queryById, updateSysParam, deleteSysParam } from '@/api/system/sysParam.js';
import serviceConfig from '@/api/serviceConfig'
import { operators } from '@/api/common.js'

export default {
  name: 'SysParam',
  dicts: ['StatusEnum', 'ParamScopeEnum'],
  data() {
    return {
      searching: false,
      uploading: false,
      dialogLoading: false,
      loading: false,
      total: 0,
      showSearch: true,
      open: false,
      multiple: true,
      single: true,
      list: [],
      title: '',
      operators: [],
      ids: [],
      dateRange: [],
      defaultSort: {
        prop: 'createTime',
        order: 'descending'
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          paramName: undefined,
          paramKey: undefined,
          paramValue: undefined,
          paramScope: undefined,
          status: undefined,
          remark: undefined,
          operatorId: undefined,
        }
      },
      form: {
        id: undefined,
        paramName: undefined,
        paramKey: undefined,
        paramValue: undefined,
        paramScope: '0',
        status: '0',
        remark: undefined,
        operatorId: undefined,
      },

      rules: {
        paramName: [
          { required: true, message: "参数名不能为空", trigger: "blur" },
          { max: 50, message: '参数名长度必须小于50个字符', trigger: 'blur' }
        ],
        paramKey: [
          { required: true, message: "参数编码不能为空", trigger: "blur" },
          { max: 50, message: '参数编码长度必须小于50个字符', trigger: 'blur' }
        ],
        paramValue: [
          { required: true, message: "参数值不能为空", trigger: "blur" },
          { max: 100, message: '参数值长度必须小于100个字符', trigger: 'blur' }
        ],
        paramScope: [
          { required: true, message: "参数范围不能为空", trigger: "blur" },
          { max: 10, message: '参数范围长度必须小于10个字符', trigger: 'blur' }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" },
          { max: 1, message: '状态长度必须小于1个字符', trigger: 'blur' }
        ],
        remark: [
          { required: true, message: "备注说明不能为空", trigger: "blur" },
          { max: 100, message: '备注说明长度必须小于100个字符', trigger: 'blur' }
        ],
        operatorId: [
          { required: true, message: "租户公司不能为空", trigger: "blur" }
        ],
      },
      columns: [
        {
          attrs: {
            label: "记录ID",
            prop: "id",
            minWidth: "100",
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: "参数名",
            prop: "paramName",
            minWidth: "140",
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: "参数编码",
            prop: "paramKey",
            minWidth: "140",
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: "参数值",
            prop: "paramValue",
            minWidth: "140",
            align: "center",
          },
        },
        {
          slot: 'paramScope',
          attrs: {
            label: "参数范围",
            prop: "paramScope",
            minWidth: "100",
            align: "center",
          },
        },
        {
          slot: 'status',
          attrs: {
            label: "状态",
            prop: "status",
            minWidth: "100",
            align: "center",
          },
        },
        {
          attrs: {
            label: "备注说明",
            prop: "remark",
            minWidth: "240",
            align: "center",
          },
        },

        {
          attrs: {
            label: "创建时间",
            prop: "createTime",
            minWidth: "140",
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: "创建人",
            prop: "createBy",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "更新时间",
            prop: "updateTime",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "更新人",
            prop: "updateBy",
            minWidth: "140",
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
      ],
    }
  },
  created() {
    this.getList();
    this.getOperators();
  },
  methods: {
    getOperators() {
      operators().then((res) => {
        this.operators = res.data
      })
    },
    handleSortChange(sortProp) {
      this.queryParams.sortList = [];
      this.queryParams.sortList.push({ orderByColumn: sortProp.prop, sortType: sortProp.order });
      this.getList();
    },
    getList() {
      this.loading = true;
      this.searching = true;
      //添加默认排序
      if (this.queryParams.sortList.length == 0) {
        this.queryParams.sortList.push({ orderByColumn: this.defaultSort.prop, sortType: this.defaultSort.order });
      }
      pageList(this.queryParams).then((resp) => {
        this.list = resp.data.list;
        this.total = resp.data.total;
        this.loading = false;
        this.searching = false;
      }).catch((err) => {
        this.loading = false;
        this.searching = false;
      });
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      //清除排序（默认自动排序的列）
      this.$refs.tables.$refs.tablePlus.sort(this.defaultSort.prop, this.defaultSort.order)
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = '新增系统参数'
    },
    handleUpdate(row) {
      this.reset();
      this.title = '编辑系统参数'
      this.open = true
      let id = row.id || this.ids;
      queryById(id).then((res) => {
        this.form = res.data;
      });
    },
    handleExport() {
      let name = '系统参数_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.resource + '/sys_param/export', this.queryParams, name)
    },
    handleDelete(row) {
      const idList = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为[' + idList + ']的数据项？').then(function () {
        return deleteSysParam(idList);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.dialogLoading = true;
          if (this.form.id != undefined) {
            //console.log('表单数据', this.form)
            //编辑
            updateSysParam(this.form).then((res) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.dialogLoading = false
              this.getList();
            }).catch(err => {
              this.dialogLoading = false
            })
          } else {
            //新增
            addSysParam(this.form).then((res) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.dialogLoading = false
              this.getList();
            }).catch(err => {
              this.dialogLoading = false
            })
          }
        }
      })
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.dialogLoading = false;
      this.form = {
        id: undefined,
        paramName: undefined,
        paramKey: undefined,
        paramValue: undefined,
        paramScope: '0',
        status: '0',
        remark: undefined,
        operatorId: undefined,
      };
      this.resetForm("form");
    },
  },
}
</script>

<style lang="scss" scoped></style>
