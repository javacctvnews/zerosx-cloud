<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams.t" label-position="left" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="服务商编码" prop="supplierTypeEnum">
        <el-input v-model="queryParams.t.supplierTypeEnum" placeholder="请输入服务商编码" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="服务商名称" prop="supplierName">
        <el-input v-model="queryParams.t.supplierName" placeholder="请输入服务商名称" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="状态，0：正常；1：停用" prop="status">
        <el-select v-model="queryParams.t.status" placeholder="状态，0：正常；1：停用" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
                   :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="Access Key" prop="accessKeyId">
        <el-input v-model="queryParams.t.accessKeyId" placeholder="请输入Access Key" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="accessKeySecret" prop="accessKeySecret">
        <el-input v-model="queryParams.t.accessKeySecret" placeholder="请输入accessKeySecret" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="短信签名" prop="signature">
        <el-input v-model="queryParams.t.signature" placeholder="请输入短信签名" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="regionId" prop="regionId">
        <el-input v-model="queryParams.t.regionId" placeholder="请输入regionId" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="租户公司" prop="operatorId">
        <el-select clearable v-model="queryParams.t.operatorId" placeholder="请选择租户公司" style="width: 220px;">
          <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remarks">
        <el-input v-model="queryParams.t.remarks" placeholder="请输入备注" clearable style="width: 220px;" />
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerms="['system:smsSupplier:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="handleUpdate"  v-hasPerms="['system:smsSupplier:update']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete" v-hasPerms="['system:smsSupplier:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPerms="['system:smsSupplier:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
                     @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div class="componentTable" style="height: calc(100vh - 235px)">
      <TablePlus :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleSelectionChange="handleSelectionChange" actionWidth="80px">
        <template slot-scope="scope" slot="status">
            <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
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
        <el-form-item label="服务商编码" prop="supplierTypeEnum">
          <el-input v-model="form.supplierTypeEnum" placeholder="请输入服务商编码" maxlength="30" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="服务商名称" prop="supplierName">
          <el-input v-model="form.supplierName" placeholder="请输入服务商名称" maxlength="30" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="状态，0：正常；1：停用" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label
              }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Access Key" prop="accessKeyId">
          <el-input v-model="form.accessKeyId" placeholder="请输入Access Key" maxlength="100" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="accessKeySecret" prop="accessKeySecret">
          <el-input v-model="form.accessKeySecret" placeholder="请输入accessKeySecret" maxlength="100" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="短信签名" prop="signature">
          <el-input v-model="form.signature" placeholder="请输入短信签名" maxlength="100" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="regionId" prop="regionId">
          <el-input v-model="form.regionId" placeholder="请输入regionId" maxlength="100" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="租户公司" prop="operatorId">
          <el-select clearable v-model="form.operatorId" placeholder="请选择租户公司" style="width: 100%;">
             <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
         </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入备注" maxlength="200" show-word-limit clearable/>
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
import { pageList, addSmsSupplier, queryById, updateSmsSupplier, deleteSmsSupplier } from '@/api/smsSupplier/smsSupplier.js';
import serviceConfig from '@/api/serviceConfig'
import { operators } from '@/api/common.js'

export default {
  name: 'SmsSupplier',
  dicts: ['sys_normal_disable'],
  data() {
    return {
      searching: false,
      uploading: false,
      dialogLoading:false,
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
          supplierTypeEnum: undefined,
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
        supplierTypeEnum: undefined,
        supplierName: undefined,
        status: undefined,
        accessKeyId: undefined,
        accessKeySecret: undefined,
        signature: undefined,
        regionId: undefined,
        operatorId: undefined,
        remarks: undefined,
      },

      rules: {
        supplierTypeEnum: [
          { required: true, message: "服务商编码不能为空", trigger: "blur" },
          { max: 30, message: '服务商编码长度必须小于30个字符', trigger: 'blur' }
        ],
        supplierName: [
          { required: true, message: "服务商名称不能为空", trigger: "blur" },
          { max: 30, message: '服务商名称长度必须小于30个字符', trigger: 'blur' }
        ],
        status: [
          { required: true, message: "状态，0：正常；1：停用不能为空", trigger: "blur" },
          { max: 1, message: '状态，0：正常；1：停用长度必须小于1个字符', trigger: 'blur' }
        ],
        accessKeyId: [
          { required: true, message: "Access Key不能为空", trigger: "blur" },
          { max: 100, message: 'Access Key长度必须小于100个字符', trigger: 'blur' }
        ],
        accessKeySecret: [
          { required: true, message: "accessKeySecret不能为空", trigger: "blur" },
          { max: 100, message: 'accessKeySecret长度必须小于100个字符', trigger: 'blur' }
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
          { required: true, message: "租户标识不能为空", trigger: "blur" },
          { max: 30, message: '租户标识长度必须小于30个字符', trigger: 'blur' }
        ],
        remarks: [
          { required: true, message: "备注不能为空", trigger: "blur" },
          { max: 200, message: '备注长度必须小于200个字符', trigger: 'blur' }
        ],
      },
      columns: [
        {
          attrs: {
            label: "id",
            prop: "id",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "服务商编码",
            prop: "supplierTypeEnum",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "服务商名称",
            prop: "supplierName",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "状态，0：正常；1：停用",
            prop: "status",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "Access Key",
            prop: "accessKeyId",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "accessKeySecret",
            prop: "accessKeySecret",
            minWidth: "140",
            align: "center"
          },
        },
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
            label: "regionId",
            prop: "regionId",
            minWidth: "140",
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
            label: "租户标识",
            prop: "operatorId",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "备注",
            prop: "remarks",
            minWidth: "140",
            align: "center"
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
            }).catch(err=>{
              this.dialogLoading = false
            })
          } else {
            //新增
            addSmsSupplier(this.form).then((res) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.dialogLoading = false
              this.getList();
            }).catch(err=>{
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
        supplierTypeEnum: undefined,
        supplierName: undefined,
        status: undefined,
        accessKeyId: undefined,
        accessKeySecret: undefined,
        signature: undefined,
        regionId: undefined,
        operatorId: undefined,
        remarks: undefined,
      };
      this.resetForm("form");
    },
  },
}
</script>

<style lang="scss" scoped></style>