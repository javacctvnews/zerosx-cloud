<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams.t" label-position="left" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="服务商编码" prop="supplierType">
        <el-input v-model="queryParams.t.supplierType" placeholder="请输入服务商编码" clearable style="width: 220px;" />
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
      <el-form-item label="AccessKey" prop="accessKeyId">
        <el-input v-model="queryParams.t.accessKeyId" placeholder="请输入AccessKey" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="AccessSecret" prop="accessKeySecret">
        <el-input v-model="queryParams.t.accessKeySecret" placeholder="请输入AccessSecret" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="存储桶名称" prop="bucketName">
        <el-input v-model="queryParams.t.bucketName" placeholder="请输入存储桶名称" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="所属地域" prop="regionId">
        <el-input v-model="queryParams.t.regionId" placeholder="请输入所属地域" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="endpoint" prop="endpoint">
        <el-input v-model="queryParams.t.endpoint" placeholder="请输入endpoint" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="域名" prop="domainAddress">
        <el-input v-model="queryParams.t.domainAddress" placeholder="请输入域名" clearable style="width: 220px;" />
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
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerms="['system:ossSupplier:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="handleUpdate"  v-hasPerms="['system:ossSupplier:update']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete" v-hasPerms="['system:ossSupplier:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPerms="['system:ossSupplier:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
                     @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div class="componentTable" style="height: calc(100vh - 220px)">
      <TablePlus ref="tables" :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleSelectionChange="handleSelectionChange" actionWidth="80px" @sort-change="handleSortChange"
        :defaultSort="defaultSort" >
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
        <el-form-item label="服务商编码" prop="supplierType">
          <el-input v-model="form.supplierType" placeholder="请输入服务商编码" maxlength="30" show-word-limit clearable/>
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
        <el-form-item label="AccessKey" prop="accessKeyId">
          <el-input v-model="form.accessKeyId" placeholder="请输入AccessKey" maxlength="100" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="AccessSecret" prop="accessKeySecret">
          <el-input v-model="form.accessKeySecret" placeholder="请输入AccessSecret" maxlength="100" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="存储桶名称" prop="bucketName">
          <el-input v-model="form.bucketName" placeholder="请输入存储桶名称" maxlength="100" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="所属地域" prop="regionId">
          <el-input v-model="form.regionId" placeholder="请输入所属地域" maxlength="100" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="endpoint" prop="endpoint">
          <el-input v-model="form.endpoint" placeholder="请输入endpoint" maxlength="100" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="域名" prop="domainAddress">
          <el-input v-model="form.domainAddress" placeholder="请输入域名" maxlength="100" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入备注" maxlength="200" show-word-limit clearable/>
        </el-form-item>
        <el-form-item label="租户公司" prop="operatorId">
          <el-select clearable v-model="form.operatorId" placeholder="请选择租户公司" style="width: 100%;">
             <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
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
import { pageList, addOssSupplier, queryById, updateOssSupplier, deleteOssSupplier } from '@/api/ossSupplier/ossSupplier.js';
import serviceConfig from '@/api/serviceConfig'
import { operators } from '@/api/common.js'

export default {
  name: 'OssSupplier',
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
      defaultSort: {
        prop: 'createTime',
        order: 'descending'
      },
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          supplierType: undefined,
          supplierName: undefined,
          status: undefined,
          accessKeyId: undefined,
          accessKeySecret: undefined,
          bucketName: undefined,
          regionId: undefined,
          endpoint: undefined,
          domainAddress: undefined,
          remarks: undefined,
          operatorId: undefined,
        }
      },
      form: {
        id: undefined,
        supplierType: undefined,
        supplierName: undefined,
        status: undefined,
        accessKeyId: undefined,
        accessKeySecret: undefined,
        bucketName: undefined,
        regionId: undefined,
        endpoint: undefined,
        domainAddress: undefined,
        remarks: undefined,
        operatorId: undefined,
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
          { required: true, message: "状态，0：正常；1：停用不能为空", trigger: "blur" },
          { max: 1, message: '状态，0：正常；1：停用长度必须小于1个字符', trigger: 'blur' }
        ],
        accessKeyId: [
          { required: true, message: "AccessKey不能为空", trigger: "blur" },
          { max: 100, message: 'AccessKey长度必须小于100个字符', trigger: 'blur' }
        ],
        accessKeySecret: [
          { required: true, message: "AccessSecret不能为空", trigger: "blur" },
          { max: 100, message: 'AccessSecret长度必须小于100个字符', trigger: 'blur' }
        ],
        bucketName: [
          { required: true, message: "存储桶名称不能为空", trigger: "blur" },
          { max: 100, message: '存储桶名称长度必须小于100个字符', trigger: 'blur' }
        ],
        regionId: [
          { required: true, message: "所属地域不能为空", trigger: "blur" },
          { max: 100, message: '所属地域长度必须小于100个字符', trigger: 'blur' }
        ],
        endpoint: [
          { required: true, message: "endpoint不能为空", trigger: "blur" },
          { max: 100, message: 'endpoint长度必须小于100个字符', trigger: 'blur' }
        ],
        domainAddress: [
          { required: true, message: "域名不能为空", trigger: "blur" },
          { max: 100, message: '域名长度必须小于100个字符', trigger: 'blur' }
        ],
        remarks: [
          { required: true, message: "备注不能为空", trigger: "blur" },
          { max: 200, message: '备注长度必须小于200个字符', trigger: 'blur' }
        ],
        operatorId: [
          { required: true, message: "租户标识不能为空", trigger: "blur" },
          { max: 30, message: '租户标识长度必须小于30个字符', trigger: 'blur' }
        ],
      },
      columns: [
        {
          attrs: {
            label: "id",
            prop: "id",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "服务商编码",
            prop: "supplierType",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "服务商名称",
            prop: "supplierName",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "状态，0：正常；1：停用",
            prop: "status",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "AccessKey",
            prop: "accessKeyId",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "AccessSecret",
            prop: "accessKeySecret",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "存储桶名称",
            prop: "bucketName",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "所属地域",
            prop: "regionId",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "endpoint",
            prop: "endpoint",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "域名",
            prop: "domainAddress",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "创建者",
            prop: "createBy",
            minWidth: "140",
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
            label: "更新者",
            prop: "updateBy",
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
            label: "备注",
            prop: "remarks",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "租户标识",
            prop: "operatorId",
            minWidth: "140",
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
   this.$refs.tables.$refs.tablePlus.sort(this.defaultSort.prop, this.defaultSort.order);
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = '新增OSS配置'
    },
    handleUpdate(row) {
      this.reset();
      this.title = '编辑OSS配置'
      this.open = true
      let id = row.id || this.ids;
      queryById(id).then((res) => {
        this.form = res.data;
      });
    },
    handleExport() {
      let name = 'OSS配置_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.system + '/oss_supplier/export', this.queryParams, name)
    },
    handleDelete(row) {
      const idList = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为[' + idList + ']的数据项？').then(function () {
        return deleteOssSupplier(idList);
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
            updateOssSupplier(this.form).then((res) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.dialogLoading = false
              this.getList();
            }).catch(err=>{
              this.dialogLoading = false
            })
          } else {
            //新增
            addOssSupplier(this.form).then((res) => {
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
        supplierType: undefined,
        supplierName: undefined,
        status: undefined,
        accessKeyId: undefined,
        accessKeySecret: undefined,
        bucketName: undefined,
        regionId: undefined,
        endpoint: undefined,
        domainAddress: undefined,
        remarks: undefined,
        operatorId: undefined,
      };
      this.resetForm("form");
    },
  },
}
</script>

<style lang="scss" scoped></style>