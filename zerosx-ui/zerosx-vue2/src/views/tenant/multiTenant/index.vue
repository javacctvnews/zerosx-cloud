<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams.t" label-position="left" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="租户公司" prop="tenantGroupName">
        <el-input v-model="queryParams.t.tenantGroupName" placeholder="请输入租户公司名称关键字" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="联系人姓名" prop="contactName">
        <el-input v-model="queryParams.t.contactName" placeholder="请输入联系人姓名" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPerms="['tenant:tenant:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
          v-hasPerms="['tenant:tenant:update']" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          v-hasPerms="['tenant:tenant:deleted']" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['tenant:tenant:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
        @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div class="componentTable" style="height: calc(100vh - 220px)">
      <TablePlus ref="tables" :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleSelectionChange="handleSelectionChange" @sort-change="handleSortChange" :defaultSort="defaultSort"
        actionWidth="80px">
        <template slot-scope="scope" slot="validStatus">
          <dict-tag :options="dict.type.StatusEnum" :value="scope.row.validStatus" />
        </template>
      </TablePlus>
    </div>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 新增or删除 -->
    <el-dialog center :lock-scroll="true" :title="title" v-if="open" :visible.sync="open" width="900px" append-to-body
      :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="110px" size="small" :rules="rules">
        <el-row>
          <el-col :span="24">
            <el-form-item label="公司全称" prop="tenantGroupName">
              <el-input v-model="form.tenantGroupName" placeholder="请输入公司全称" maxlength="30" auto-complete="off"
                show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="公司简称" prop="tenantShortName">
              <el-input v-model="form.tenantShortName" placeholder="请输入公司简称" maxlength="10" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="社会信用代码" prop="socialCreditCode">
              <el-input v-model="form.socialCreditCode" placeholder="请输入公司社会信用代码" maxlength="18" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态" prop="validStatus">
              <el-radio-group v-model="form.validStatus">
                <el-radio v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在省市区" prop="areaList">
              <el-cascader :options="areas" v-model="form.areaList" :props="areaProps" clearable
                :style="{ width: '100%' }"></el-cascader>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="详细地址" prop="street">
              <el-input type="textarea" v-model="form.street" placeholder="请输入详细地址" maxlength="100" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系人名称" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入联系人名称" maxlength="30" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人号码" prop="contactMobilePhone">
              <el-input v-model="form.contactMobilePhone" placeholder="请输入联系人号码" maxlength="11" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="公司电话" prop="telephone">
              <el-input v-model="form.telephone" placeholder="请输入公司电话" maxlength="12" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="公司Logo" prop="logPicture">
              <ImageUpload :limit="1" v-model="form.logPicture" ref="imageUpload" :up-loading.sync="uploading"
                :fileListBack="logoFileList" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="营业执照" prop="businessLicensePicture">
              <ImageUpload :limit="1" v-model="form.businessLicensePicture" ref="imageUpload1"
                :up-loading.sync="uploading" :fileListBack="businessLicenseFileList" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input type="textarea" v-model="form.remarks" placeholder="请输入备注" maxlength="230" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { pageList, addTenant, getById, updateTenant, deleteTenant } from '@/api/tenant/multiTenant.js';
import { areas } from '@/api/common.js'
import serviceConfig from '@/api/serviceConfig'
export default {
  name: 'MultiTenant',
  dicts: ['StatusEnum'],
  data() {
    return {
      searching: false,
      uploading: false,
      loading: false,
      total: 0,
      showSearch: true,
      open: false,
      multiple: true,
      single: true,
      list: [],
      title: '',
      areas: [],
      tenantIds: [],
      dateRange: [],
      logoUrlStr: '',
      businessLicensePictureStr: '',
      logoFileList: [],
      businessLicenseFileList: [],
      defaultSort: {
        prop: 'createTime',
        order: 'descending'
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          tenantGroupName: undefined,
          contactName: undefined
        }
      },
      form: {
        tenantGroupName: '',
        tenantShortName: '',
        socialCreditCode: '',
        businessLicensePicture: '',
        street: '',
        contactName: '',
        validStatus: '',
        contactMobilePhone: '',
        telephone: '',
        logPicture: '',
        areaList: '',
        province: '',
        city: '',
        area: '',
        remarks: ''
      },
      areaProps: {
        expandTrigger: 'hover',
        children: "children",
        value: 'areaCode',
        label: "areaName"
      },
      rules: {
        tenantGroupName: [
          { required: true, message: "公司全称不能为空", trigger: "blur" },
          { min: 4, max: 20, message: '公司全称长度必须介于 4 和 20 之间', trigger: 'blur' }
        ],
        tenantShortName: [
          { required: true, message: "公司简称不能为空", trigger: "blur" },
          { min: 2, max: 10, message: '公司全称长度必须介于 2 和 10 之间', trigger: 'blur' }
        ],
        socialCreditCode: [
          { required: true, message: "社会信用代码不能为空", trigger: "blur" },
          { min: 18, max: 18, message: '社会信用代码长度必须是18位的字符串', trigger: 'blur' }
        ],
        areaList: [
          { required: true, message: "请选择公司所在省市区", trigger: ["blur", "change"] }
        ],
        validStatus: [
          { required: true, message: "状态不能为空", trigger: "blur" },
        ],
        street: [
          { required: true, message: "详细街道地址不能为空", trigger: "blur" },
          { max: 100, message: '详细街道地址长度不能超过100', trigger: 'blur' }
        ],
        contactName: [
          { required: true, message: "联系人姓名不能为空", trigger: "blur" },
          { min: 2, max: 12, message: '联系人姓名长度必须介于2和12之间', trigger: 'blur' }
        ],
        contactMobilePhone: [
          { required: true, message: "联系号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        telephone: [
          { required: true, message: "公司电话不能为空", trigger: "blur" },
          {
            pattern: /^400(-\d{3,4}){2}$/,
            message: "请输入正确的400号码",
            trigger: "blur"
          }
        ],
        logPicture: [
          { required: true, message: "公司logo不能为空", trigger: "blur" },
        ],
        businessLicensePicture: [
          { required: true, message: "公司营业执照不能为空", trigger: "blur" },
        ],
        remarks: [
          { max: 100, message: '备注长度不能超过100', trigger: 'blur' }
        ],
      },
      columns: [
        // {
        //   attrs: {
        //     label: '记录ID',
        //     prop: 'id',
        //     minWidth: '90',
        //     sortable: 'custom',
        //     align: "center",
        //   },
        // },
        {
          attrs: {
            label: '租户标识',
            prop: 'operatorId',
            minWidth: '100',
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: '公司全称',
            prop: 'tenantGroupName',
            minWidth: '180',
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: '公司简称',
            prop: 'tenantShortName',
            minWidth: '140',
            sortable: 'custom',
            align: "center",
          },
        },
        {
          slot: 'validStatus',
          attrs: {
            label: '状态',
            prop: 'validStatus',
            minWidth: '100',
            align: "center",
          },
        },
        {
          attrs: {
            label: '联系人姓名',
            prop: 'contactName',
            minWidth: '140',
            align: "center",
          },
        },
        {
          attrs: {
            label: '联系人电话',
            prop: 'contactMobilePhone',
            minWidth: '140',
            align: "center",
          },
        },
        {
          attrs: {
            label: '公司电话',
            prop: 'telephone',
            minWidth: '140',
            align: "center",
          },
        },
        {
          attrs: {
            label: '创建时间',
            prop: 'createTime',
            minWidth: '150',
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: '所在省',
            prop: 'provinceName',
            minWidth: '120',
            align: "center",
          },
        },
        {
          attrs: {
            label: '所在市',
            prop: 'cityName',
            minWidth: '120',
            align: "center",
          },
        },
        {
          attrs: {
            label: '所在区',
            prop: 'areaName',
            minWidth: '120',
            align: "center",
          },
        },
        {
          attrs: {
            label: '详细地址',
            prop: 'street',
            minWidth: '180',
            align: "center",
            showOverflowTooltip: true
          },
        },
        {
          attrs: {
            label: '备注',
            prop: 'remarks',
            minWidth: '240',
            showOverflowTooltip: true,
            align: "center",
          },
        },
      ],
    }
  },
  created() {
    this.getList();
    this.getAreas();
  },
  methods: {
    getAreas() {
      areas().then((res) => {
        this.areas = res.data
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
      });;
    },
    handleSelectionChange(selection) {
      this.tenantIds = selection.map(item => item.id)
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
      this.title = '新增租户公司'
    },
    handleUpdate(row) {
      this.reset();
      this.title = '编辑租户公司'
      this.open = true
      let id = row.id || this.tenantIds;
      getById(id).then((res) => {
        this.form = res.data;
        const cities = [];
        cities.unshift(this.form.province, this.form.city, this.form.area)
        this.form.areaList = cities;
        let logImg = this.buildImage(res.data.logPicture, res.data.logPicture,res.data.logPictureUrl);
        let licenseImg = this.buildImage(res.data.businessLicensePicture, res.data.businessLicensePicture,res.data.businessLicensePictureUrl);
        this.logoFileList.push(logImg);
        this.businessLicenseFileList.push(licenseImg);
        this.form.businessLicensePicture = res.data.businessLicensePicture;
        this.form.logPicture = res.data.logPicture;
      });
    },
    buildImage(name, objectName, url) {
      let imageObj = {
        name: name,
        objectName: objectName,
        url: url
      }
      console.log(imageObj);
      return imageObj;
    },
    handleExport() {
      let name = '租户管理_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.system + '/muti_tenancy/export', this.queryParams, name)
    },
    handleDelete(row) {
      const idList = row.id || this.tenantIds;
      this.$modal.confirm('是否确认删除已选择的数据项？').then(function () {
        return deleteTenant(idList);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    submitForm() {
      this.form.province = this.form.areaList[0];
      this.form.city = this.form.areaList[1];
      this.form.area = this.form.areaList[2];
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != undefined) {
            console.log('表单数据', this.form)
            //编辑
            updateTenant(this.form).then((res) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
              this.reset();
            })
          } else {
            //新增
            addTenant(this.form).then((res) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              this.reset();
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
      this.form = {
        tenantGroupName: '',
        tenantShortName: '',
        socialCreditCode: '',
        businessLicensePicture: '',
        validStatus: '0',
        street: '',
        contactName: '',
        contactMobilePhone: '',
        telephone: '',
        logPicture: '',
        areaList: [],
        province: '',
        city: '',
        area: '',
        remarks: '',

      };
      this.businessLicenseFileList = [];
      this.logoFileList = [];
      this.resetForm("form");
    },
  },
}
</script>


<style lang="scss" scoped></style>
