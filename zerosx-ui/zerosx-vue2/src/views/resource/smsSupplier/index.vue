<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams.t" label-position="left" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="租户公司" prop="operatorId">
        <el-select clearable v-model="queryParams.t.operatorId" placeholder="请选择租户公司" style="width: 220px;">
          <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="服务商名称" prop="supplierName">
        <el-input v-model="queryParams.t.supplierName" placeholder="请输入服务商名称" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.t.status" placeholder="状态" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPerms="['system:smsSupplier:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPerms="['system:smsSupplier:update']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['system:smsSupplier:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['system:smsSupplier:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
        @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div class="componentTable" style="height: calc(100vh - 220px)">
      <TablePlus :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleSelectionChange="handleSelectionChange" @handleTemplate="handleTemplate" actionWidth="120px">
        <template slot-scope="scope" slot="status">
          <dict-tag :options="dict.type.StatusEnum" :value="scope.row.status" />
        </template>
        <template slot-scope="scope" slot="supplierType">
          <dict-tag :options="dict.type.SupplierTypeEnum" :value="scope.row.supplierType" />
        </template>
      </TablePlus>
    </div>

    <!-- 分页器 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 新增or删除 -->
    <el-dialog center :lock-scroll="true" :title="title" :visible.sync="open" width="700px" append-to-body
      :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="140px" size="small" :rules="rules">
        <el-form-item label="租户公司" prop="operatorId">
          <el-select clearable v-model="form.operatorId" placeholder="请选择租户公司" style="width: 100%;">
            <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="服务商" prop="supplierType">
          <el-radio-group v-model="form.supplierType" @input="radioChange">
            <el-radio v-for="dict in dict.type.SupplierTypeEnum" :key="dict.value" :label="dict.value">{{ dict.label
            }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item label="服务商名称" prop="supplierName">
          <el-input v-model="form.supplierName" placeholder="请输入服务商名称" maxlength="30" show-word-limit clearable />
        </el-form-item> -->
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.value">{{ dict.label
            }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <div v-if="showJuhe">
          <el-form-item label="服务器URL" prop="domainAddress">
            <el-input v-model="form.domainAddress" placeholder="请输入服务器URL" maxlength="100" show-word-limit clearable />
          </el-form-item>
          <el-form-item label="key值" prop="keyValue">
            <el-input v-model="form.keyValue" placeholder="请输入key值" maxlength="100" show-word-limit clearable />
          </el-form-item>
        </div>
        <div v-if="showAccess">
          <el-form-item label="Access Key" prop="accessKeyId">
            <el-input v-model="form.accessKeyId" placeholder="请输入Access Key" maxlength="100" show-word-limit clearable />
          </el-form-item>
          <el-form-item label="AccessKeySecret" prop="accessKeySecret">
            <el-input v-model="form.accessKeySecret" placeholder="请输入AccessKeySecret" maxlength="100" show-word-limit
              clearable />
          </el-form-item>
          <el-form-item label="短信签名" prop="signature">
            <el-input v-model="form.signature" placeholder="请输入短信签名" maxlength="100" show-word-limit clearable />
          </el-form-item>
          <el-form-item label="regionId" prop="regionId">
            <el-input v-model="form.regionId" placeholder="请输入regionId" maxlength="100" show-word-limit clearable />
          </el-form-item>
        </div>

        <el-form-item label="备注" prop="remarks">
          <el-input type="textarea" v-model="form.remarks" placeholder="请输入备注" maxlength="200" show-word-limit
            clearable />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="dialogLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog center :title="smsBusinessTitle" :lock-scroll="true" :visible.sync="openTemplate" width="1200px"
      append-to-body :close-on-click-modal="false">
      <SmsSupplierBusiness v-if="openTemplate" :dataRow="dataRow"></SmsSupplierBusiness>
    </el-dialog>
  </div>
</template>

<script>
import { pageList, addSmsSupplier, queryById, updateSmsSupplier, deleteSmsSupplier } from '@/api/resource/smsSupplier.js';
import serviceConfig from '@/api/serviceConfig'
import { operators } from '@/api/common.js'
import SmsSupplierBusiness from './businessDialog.vue';

export default {
  components: { SmsSupplierBusiness },
  name: 'SmsSupplier',
  dicts: ['StatusEnum', 'SupplierTypeEnum'],
  data() {
    return {
      //表单显隐
      showJuhe: false,
      showAccess: false,
      //短信业务模板
      smsBusinessTitle: '短信业务模板',
      openTemplate: false,
      dataRow: {},
      //短信配置
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
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        t: {
          supplierType: undefined,
          supplierName: undefined,
          status: undefined,
          accessKeyId: undefined,
          accessKeySecret: undefined,
          signature: undefined,
          regionId: undefined,
          operatorId: undefined,
          remarks: undefined,
        }
      },
      form: {
        id: undefined,
        supplierType: undefined,
        supplierName: undefined,
        status: '0',
        accessKeyId: undefined,
        accessKeySecret: undefined,
        signature: undefined,
        regionId: undefined,
        operatorId: undefined,
        remarks: undefined,
        domainAddress: undefined,
        keyValue: undefined
      },

      rules: {
        supplierType: [
          { required: true, message: "服务商编码不能为空", trigger: "blur" },
          { max: 30, message: '服务商编码长度必须小于30个字符', trigger: 'blur' }
        ],
        supplierName: [
          { required: true, message: "服务商名称不能为空", trigger: "blur" },
          { max: 30, message: '服务商名称长度必须小于30个字符', trigger: 'blur' }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" },
        ],
        accessKeyId: [
          { required: true, message: "Access Key不能为空", trigger: "blur" },
          { max: 100, message: 'Access Key长度必须小于100个字符', trigger: 'blur' }
        ],
        accessKeySecret: [
          { required: true, message: "AccessKeySecret不能为空", trigger: "blur" },
          { max: 100, message: 'AccessKeySecret长度必须小于100个字符', trigger: 'blur' }
        ],
        domainAddress: [
          { required: true, message: "服务URL不能为空", trigger: "blur" },
          { max: 100, message: '服务URL长度必须小于100个字符', trigger: 'blur' }
        ],
        keyValue: [
          { required: true, message: "key值不能为空", trigger: "blur" },
          { max: 100, message: 'key值长度必须小于100个字符', trigger: 'blur' }
        ],
        signature: [
          { required: true, message: "短信签名不能为空", trigger: "blur" },
          { max: 100, message: '短信签名长度必须小于100个字符', trigger: 'blur' }
        ],
        regionId: [
          { required: true, message: "regionId不能为空", trigger: "blur" },
          { max: 100, message: 'regionId长度必须小于100个字符', trigger: 'blur' }
        ],
        operatorId: [
          { required: true, message: "租户公司不能为空", trigger: "blur" },
          { max: 30, message: '租户公司长度必须小于30个字符', trigger: 'blur' }
        ],
        remarks: [
          { required: true, message: "备注不能为空", trigger: "blur" },
          { max: 200, message: '备注长度必须小于200个字符', trigger: 'blur' }
        ],
      },
      columns: [
        {
          attrs: {
            label: "记录ID",
            prop: "id",
            minWidth: "100",
            align: "center"
          },
        },
        {
          attrs: {
            label: "租户公司",
            prop: "operatorName",
            minWidth: "180",
            align: "center"
          },
        },
        {
          slot: 'supplierType',
          attrs: {
            label: "服务商",
            prop: "supplierType",
            minWidth: "140",
            align: "center"
          },
        },
        {
          slot: 'status',
          attrs: {
            label: "状态",
            prop: "status",
            minWidth: "120",
            align: "center"
          },
        },
        // {
        //   attrs: {
        //     label: "服务商名称",
        //     prop: "supplierName",
        //     minWidth: "140",
        //     align: "center"
        //   },
        // },
        {
          attrs: {
            label: "短信签名",
            prop: "signature",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "Access Key",
            prop: "accessKeyId",
            minWidth: "180",
            align: "center"
          },
        },
        {
          attrs: {
            label: "AccessKeySecret",
            prop: "accessKeySecret",
            minWidth: "280",
            align: "center"
          },
        },
        {
          attrs: {
            label: "regionId",
            prop: "regionId",
            minWidth: "280",
            align: "center"
          },
        },
        {
          attrs: {
            label: "服务URL",
            prop: "domainAddress",
            minWidth: "240",
            align: "center"
          },
        },
        {
          attrs: {
            label: "key值",
            prop: "keyValue",
            minWidth: "240",
            align: "center"
          },
        },
        
        {
          attrs: {
            label: "创建者",
            prop: "createBy",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "创建时间",
            prop: "createTime",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "更新者",
            prop: "updateBy",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "更新时间",
            prop: "updateTime",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "备注",
            prop: "remarks",
            minWidth: "340",
            align: "center"
          },
        },
        {
          type: 'action',
          actions: [
            {
              type: 'edit',
              name: '短信模板',
              fcn: 'handleTemplate',
              //show: () => this.checkPermi(['system:dict:dictdata'])
            }
          ]
        }
      ],
    }
  },
  created() {
    this.getList();
    this.getOperators();
  },
  watch: {

  },
  methods: {
    radioChange(val) {
      this.$refs.form.clearValidate();
      if (val == 'alibaba' || val == 'jdcloud') {
        this.showAccess = true;
        this.showJuhe = false;
      } else if (val == 'juhe') {
        this.showJuhe = true;
        this.showAccess = false;
      }
    },
    handleTemplate(row) {
      //console.log(row);
      this.dataRow = row;
      this.smsBusinessTitle = '【' + row.operatorName + '】的短信配置【' + row.id + '】的模板，服务商【' + row.supplierName + '】'
      this.openTemplate = true;
    },

    getOperators() {
      operators().then((res) => {
        this.operators = res.data
      })
    },
    getList() {
      this.loading = true;
      this.searching = true;
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
      //this.handleQuery()
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = '新增SMS配置'
    },
    handleUpdate(row) {
      this.reset();
      this.title = '编辑SMS配置'
      this.open = true
      let id = row.id || this.ids;
      queryById(id).then((res) => {
        this.form = res.data;
        this.radioChange(this.form.supplierType);
      });
    },
    handleExport() {
      let name = 'SMS配置_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.system + '/sms_supplier/export', {
        ...this.queryParams.t
      }, name)
    },
    handleDelete(row) {
      const idList = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为[' + idList + ']的数据项？').then(function () {
        return deleteSmsSupplier(idList);
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
            updateSmsSupplier(this.form).then((res) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.dialogLoading = false
              this.getList();
            }).catch(err => {
              this.dialogLoading = false
            })
          } else {
            //新增
            addSmsSupplier(this.form).then((res) => {
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
        supplierType: undefined,
        supplierName: undefined,
        status: '0',
        accessKeyId: undefined,
        accessKeySecret: undefined,
        signature: undefined,
        regionId: undefined,
        operatorId: undefined,
        remarks: undefined,
        domainAddress: undefined,
        keyValue: undefined
      };
      this.resetForm("form");
    },
  },
}
</script>

<style lang="scss" scoped></style>
