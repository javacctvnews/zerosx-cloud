<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <!-- <el-form :model="queryParams.t" label-position="left" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="服务商ID" prop="smsSupplierId">
        <el-input v-model="queryParams.t.smsSupplierId" placeholder="请输入服务商ID" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="短信业务编码" prop="businessCode">
        <el-input v-model="queryParams.t.businessCode" placeholder="请输入短信业务编码" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="模板CODE" prop="templateCode">
        <el-input v-model="queryParams.t.templateCode" placeholder="请输入模板CODE" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="模板内容" prop="templateContent">
        <el-input v-model="queryParams.t.templateContent" placeholder="请输入模板内容" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="模板签名" prop="signature">
        <el-input v-model="queryParams.t.signature" placeholder="请输入模板签名" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="状态，0：正常；1：停用" prop="status">
        <el-select v-model="queryParams.t.status" placeholder="状态，0：正常；1：停用" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.label"
                   :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remarks">
        <el-input v-model="queryParams.t.remarks" placeholder="请输入备注" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="租户公司" prop="operatorId">
        <el-select clearable v-model="queryParams.t.operatorId" placeholder="请选择租户公司" style="width: 220px;">
          <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form> -->

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPerms="['system:smsSupplierBusiness:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPerms="['system:smsSupplierBusiness:update']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['system:smsSupplierBusiness:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['system:smsSupplierBusiness:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
        @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div class="componentTable" style="height: calc(100vh - 260px)">
      <TablePlus ref="tables" :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleSelectionChange="handleSelectionChange" actionWidth="80px" :hideIndex="true"
        @sort-change="handleSortChange" :defaultSort="defaultSort">
        <template slot-scope="scope" slot="status">
          <dict-tag :options="dict.type.StatusEnum" :value="scope.row.status" />
        </template>
        <template slot-scope="scope" slot="businessCode">
          <dict-tag :options="dict.type.SmsBusinessCodeEnum" :value="scope.row.businessCode" />
        </template>
      </TablePlus>
    </div>

    <!-- 分页器 -->
    <!-- <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" /> -->

    <!-- 新增or删除 -->
    <el-dialog center :lock-scroll="true" :title="title" :visible.sync="open" width="800px" append-to-body
      :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="140px" size="small" :rules="rules">
        <!-- <el-form-item label="短信业务编码" prop="businessCode">
          <el-input v-model="form.businessCode" placeholder="请输入短信业务编码" maxlength="30" show-word-limit clearable/>
        </el-form-item> -->
        <el-form-item label="短信业务编码" prop="businessCode">
          <el-radio-group v-model="form.businessCode">
            <el-radio v-for="dict in dict.type.SmsBusinessCodeEnum" :key="dict.value" :label="dict.value">{{ dict.label
            }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.value">{{ dict.label
            }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="模板CODE" prop="templateCode">
          <el-input v-model="form.templateCode" placeholder="请输入模板CODE" maxlength="100" show-word-limit clearable />
        </el-form-item>
        <el-form-item label="模板内容" prop="templateContent">
          <el-input type="textarea" v-model="form.templateContent" placeholder="请输入模板内容" maxlength="200" show-word-limit
            clearable />
        </el-form-item>
        <el-form-item label="模板签名" prop="signature">
          <el-input v-model="form.signature" placeholder="请输入模板签名" maxlength="100" show-word-limit clearable />
        </el-form-item>
        <el-form-item label="备注(应用场景)" prop="remarks">
          <el-input type="textarea" v-model="form.remarks" placeholder="请输入备注(应用场景)" maxlength="100" show-word-limit
            clearable />
        </el-form-item>
        <!-- <el-form-item label="租户公司" prop="operatorId">
          <el-select clearable v-model="form.operatorId" placeholder="请选择租户公司" style="width: 100%;">
             <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
         </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="dialogLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { pageList, addSmsSupplierBusiness, queryById, updateSmsSupplierBusiness, deleteSmsSupplierBusiness } from '@/api/resource/smsSupplierBusiness.js';
import serviceConfig from '@/api/serviceConfig'
import { operators } from '@/api/common.js'

export default {
  name: 'SmsSupplierBusiness',
  dicts: ['StatusEnum', 'SmsBusinessCodeEnum'],
  props: {
    dataRow: {
      type: Object,
      required: false,
      default: {},
    },
  },
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
      defaultSort: {
        prop: 'createTime',
        order: 'descending'
      },
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 1000,
        sortList: [],
        t: {
          smsSupplierId: undefined,
          businessCode: undefined,
          templateCode: undefined,
          templateContent: undefined,
          signature: undefined,
          status: undefined,
          remarks: undefined,
          operatorId: undefined,
        }
      },
      form: {
        id: undefined,
        smsSupplierId: undefined,
        businessCode: undefined,
        templateCode: undefined,
        templateContent: undefined,
        signature: undefined,
        status: '0',
        remarks: undefined,
        operatorId: undefined,
      },

      rules: {
        smsSupplierId: [
          { required: true, message: "服务商ID不能为空", trigger: "blur" },
        ],
        businessCode: [
          { required: true, message: "短信业务编码不能为空", trigger: "blur" },
          { max: 30, message: '短信业务编码长度必须小于30个字符', trigger: 'blur' }
        ],
        templateCode: [
          { required: true, message: "模板CODE不能为空", trigger: "blur" },
          { max: 100, message: '模板CODE长度必须小于100个字符', trigger: 'blur' }
        ],
        templateContent: [
          { required: true, message: "模板内容不能为空", trigger: "blur" },
          { max: 200, message: '模板内容长度必须小于200个字符', trigger: 'blur' }
        ],
        // signature: [
        //   { required: true, message: "模板签名不能为空", trigger: "blur" },
        //   { max: 100, message: '模板签名长度必须小于100个字符', trigger: 'blur' }
        // ],
        status: [
          { required: true, message: "状态，0：正常；1：停用不能为空", trigger: "blur" },
          { max: 1, message: '状态，0：正常；1：停用长度必须小于1个字符', trigger: 'blur' }
        ],
        remarks: [
          { required: true, message: "备注(应用场景)不能为空", trigger: "blur" },
          { max: 100, message: '备注(应用场景)长度必须小于100个字符', trigger: 'blur' }
        ],
        operatorId: [
          { required: true, message: "租户公司不能为空", trigger: "blur" },
          { max: 100, message: '租户公司长度必须小于100个字符', trigger: 'blur' }
        ],
      },
      columns: [
        {
          slot: 'businessCode',
          attrs: {
            label: "短信业务",
            prop: "businessCode",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "模板CODE",
            prop: "templateCode",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "模板内容",
            prop: "templateContent",
            minWidth: "320",
            align: "center",
          },
        },
        {
          attrs: {
            label: "模板签名",
            prop: "signature",
            minWidth: "120",
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
            label: "备注",
            prop: "remarks",
            minWidth: "240",
            align: "center",
          },
        },
        {
          attrs: {
            label: "创建时间",
            prop: "createTime",
            minWidth: "140",
            align: "center",
            sortable: 'custom',
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
            sortable: 'custom',
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
      ],
    }
  },
  created() {
    //console.log('传递的数据：',this.dataRow);
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
      this.queryParams.t.smsSupplierId = this.dataRow.id;
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
      this.$refs.tables.$refs.tablePlus.sort(this.defaultSort.prop, this.defaultSort.order);
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = '新增短信业务模板'
    },
    handleUpdate(row) {
      this.reset();
      this.title = '编辑短信业务模板'
      this.open = true
      let id = row.id || this.ids;
      queryById(id).then((res) => {
        this.form = res.data;
      });
    },
    handleExport() {
      let name = '短信业务模板_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.system + '/sms_supplier_business/export', this.queryParams, name)
    },
    handleDelete(row) {
      const idList = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为[' + idList + ']的数据项？').then(function () {
        return deleteSmsSupplierBusiness(idList);
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
            updateSmsSupplierBusiness(this.form).then((res) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.dialogLoading = false
              this.getList();
            }).catch(err => {
              this.dialogLoading = false
            })
          } else {
            //新增
            this.form.smsSupplierId = this.dataRow.id;
            this.form.operatorId = this.dataRow.operatorId;
            addSmsSupplierBusiness(this.form).then((res) => {
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
        smsSupplierId: undefined,
        businessCode: undefined,
        templateCode: undefined,
        templateContent: undefined,
        signature: undefined,
        status: '0',
        remarks: undefined,
        operatorId: undefined,
      };
      this.resetForm("form");
    },
  },
}
</script>

<style lang="scss" scoped></style>