<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams.t" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto"
      label-position="left">
      <el-form-item label="租户公司" prop="operatorId">
        <el-select clearable v-model="queryParams.t.operatorId" placeholder="请选择租户公司" style="width: 220px;">
          <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="关键字" prop="userKeyword">
        <el-input v-model="queryParams.t.userKeyword" placeholder="用户账号或昵称关键字" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="手机号码" prop="phoneNumber">
        <el-input v-model="queryParams.t.phoneNumber" placeholder="请输入手机号码" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="帐号状态" prop="status">
        <el-select v-model="queryParams.t.status" placeholder="帐号状态" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <div class="leftBtn">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
            v-hasPerms="['perms:sysuser:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
            v-hasPerms="['perms:sysuser:update']">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
            v-hasPerms="['perms:sysuser:delete']">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
            v-hasPerms="['perms:sysuser:export']">导出</el-button>
        </el-col>
      </div>
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
        <template slot-scope="scope" slot="sex">
          <dict-tag :options="dict.type.SexEnum" :value="scope.row.sex" />
        </template>
        <template slot-scope="scope" slot="userType">
          <dict-tag :options="dict.type.sys_user_type" :value="scope.row.userType" />
        </template>
      </TablePlus>
    </div>

    <!-- 分页器 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 新增or删除 -->
    <el-dialog center :lock-scroll="true" :title="title" :visible.sync="open" width="800px" append-to-body
      :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="110px" size="small" :rules="rules">
        <el-divider content-position="left" style="font-size: 15px;font-weight: 500;">基础信息</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="租户公司" prop="operatorId">
              <el-select @change="changeOpeator" clearable v-model="form.operatorId" placeholder="请选择租户公司"
                style="width: 100%;">
                <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户账号" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户账号" maxlength="30" show-word-limit
                autocomplete="new-password" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="帐号状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="showPass">
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="form.password" placeholder="请输入密码" show-password
                autocomplete="new-password" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="checkPassword">
              <el-input type="password" v-model="form.checkPassword" placeholder="请重新输入密码" maxlength="100" show-password
                clearable autocomplete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left">详细信息</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入用户昵称" maxlength="30" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phoneNumber">
              <el-input v-model="form.phoneNumber" placeholder="请输入手机号码" maxlength="11" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入用户邮箱" maxlength="50" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户性别" prop="sex">
              <el-radio-group v-model="form.sex">
                <el-radio v-for="dict in dict.type.SexEnum" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left">职责信息</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属部门" prop="deptId">
              <treeselect v-model="form.deptId" :options="deptTreeData" :show-count="true" placeholder="请选择归属部门"
                noOptionsText="暂无数据" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="角色权限" prop="roleIds">
              <el-select multiple clearable v-model="form.roleIds" placeholder="请选择权限角色" style="width: 100%;">
                <el-option v-for="item in roleSelectData" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属岗位" prop="postIds">
              <el-select multiple clearable v-model="form.postIds" placeholder="请选择岗位" style="width: 100%;">
                <el-option v-for="item in postSelectData" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import store from "@/store";
import { pageList, addSysUser, queryById, updateSysUser, deleteSysUser } from '@/api/perms/sysUser.js';
import { roleSelectList } from '@/api/perms/sysRole.js';
import { detpTreeSelect } from '@/api/perms/sysDept.js'
import { postSelectList } from '@/api/perms/sysPost.js'
import serviceConfig from '@/api/serviceConfig'
import { operators } from '@/api/common.js'
import { validUsername, patternConsts } from '@/utils/validate'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: 'SysUser',
  dicts: ['StatusEnum', 'SexEnum', 'sys_user_type'],
  components: { Treeselect },
  data() {
    var checkPasswordRule = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致'));
      } else {
        callback();
      }
    };
    return {
      searching: true,
      showPass: true,
      submitLoading: false,
      uploading: false,
      loading: false,
      total: 0,
      showSearch: true,
      open: false,
      multiple: true,
      single: true,
      list: [],
      deptTreeData: [],
      title: '',
      operators: [],
      ids: [],
      dateRange: [],
      defaultSort: {
        prop: 'createTime',
        order: 'descending'
      },
      roleSelectData: [],
      postSelectData: [],
      selectParam: {

      },
      deptProps: {
        checkStrictly: true,
        expandTrigger: 'hover',
        label: 'label',
        children: 'children',
        value: 'id'
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          deptId: undefined,
          userName: undefined,
          userCode: undefined,
          nickName: undefined,
          userType: undefined,
          email: undefined,
          phoneNumber: undefined,
          sex: undefined,
          avatar: undefined,
          password: undefined,
          status: undefined,
          loginIp: undefined,
          loginDate: undefined,
          remark: undefined,
          operatorId: undefined,
          checkPassword: undefined,
        }
      },
      form: {
        id: undefined,
        deptId: undefined,
        userName: undefined,
        userCode: undefined,
        nickName: undefined,
        userType: undefined,
        email: undefined,
        phoneNumber: undefined,
        sex: undefined,
        avatar: undefined,
        password: undefined,
        status: undefined,
        loginIp: undefined,
        loginDate: undefined,
        remark: undefined,
        operatorId: undefined,
        checkPassword: undefined,
        roleIds: [],
        postIds: []
      },

      rules: {
        operatorId: [
          { required: true, message: "租户公司不能为空", trigger: "blur" },
          { max: 20, message: '租户公司长度必须小于20个字符', trigger: 'blur' }
        ],
        userName: [
          { required: true, message: "用户账号不能为空", trigger: "blur" },
          { min: 4, max: 30, message: '用户账号长度必须介于4~30个字符', trigger: 'blur' },
          { validator: validUsername, trigger: 'blur' }
        ],
        // userCode: [
        //   { required: true, message: "用户内部编码不能为空", trigger: "blur" },
        //   { max: 20, message: '用户内部编码长度必须小于20个字符', trigger: 'blur' }
        // ],
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" },
          { min: 4, max: 30, message: '用户昵称长度必须介于4~30个字符', trigger: 'blur' },
          { validator: validUsername, trigger: 'blur' }
        ],
        userType: [
          { required: true, message: "用户类型不能为空", trigger: "blur" },
          { max: 20, message: '用户类型长度必须小于20个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: "用户邮箱不能为空", trigger: "blur" },
          { pattern: patternConsts.email, message: "请输入正确的邮箱", trigger: "blur" }
        ],
        phoneNumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          { pattern: patternConsts.phone, message: "请输入正确的手机号码", trigger: "blur" }
        ],
        sex: [
          { required: true, message: "用户性别不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          { min: 8, max: 20, message: '密码长度必须介于8~20个字符', trigger: 'blur' },
          { pattern: patternConsts.pwd, message: "密码必须包含大小写字母和数字的组合，不能使用特殊字符", trigger: "blur" }
        ],
        checkPassword: [
          { required: true, message: "请再次输入密码", trigger: "blur" },
          { min: 8, max: 20, message: '密码长度必须介于8~20个字符', trigger: 'blur' },
          { pattern: patternConsts.pwd, message: "密码必须包含大小写字母和数字的组合，不能使用特殊字符", trigger: "blur" },
          { validator: checkPasswordRule, trigger: 'blur' }
        ],
        status: [
          { required: true, message: "帐号状态不能为空", trigger: "blur" },
        ],
        // remark: [
        //   { required: true, message: "备注不能为空", trigger: "blur" },
        //   { max: 500, message: '备注长度必须小于500个字符', trigger: 'blur' }
        // ],
        // deptId: [
        //   { required: true, message: "所属部门不能为空", trigger: "blur" },
        // ],
      },
      columns: [
        {
          attrs: {
            label: "用户ID",
            prop: "id",
            minWidth: "90",
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: "用户账号",
            prop: "userName",
            minWidth: "140",
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: "用户昵称",
            prop: "nickName",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "所属部门",
            prop: "deptName",
            minWidth: "140",
            align: "center",
          },
        },
        {
          slot: 'userType',
          attrs: {
            label: "用户类型",
            prop: "userType",
            minWidth: "140",
            align: "center",
          },
        },
        {
          slot: 'status',
          attrs: {
            label: "帐号状态",
            prop: "status",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "用户邮箱",
            prop: "email",
            minWidth: "160",
            align: "center",
          },
        },
        {
          attrs: {
            label: "手机号码",
            prop: "phoneNumber",
            minWidth: "140",
            align: "center",
          },
        },
        {
          slot: 'sex',
          attrs: {
            label: "用户性别",
            prop: "sex",
            minWidth: "100",
            align: "center",
          },
        },
        {
          attrs: {
            label: "最后登录IP",
            prop: "loginIp",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "最后登录时间",
            prop: "loginDate",
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
            sortable: 'custom',
            align: "center",
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
            sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: "备注",
            prop: "remark",
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
    this.selectParam.operatorId = store.getters.operatorId;
  },
  methods: {
    changeOpeator(val) {
      this.selectParam = {
        operatorId: val
      }
      this.form.deptId = undefined;
      this.getDeptTreeSelect();
      this.getRoleSelectList();
      this.getPostSelectList();
    },
    handleSortChange(sortProp) {
      this.queryParams.sortList = [];
      this.queryParams.sortList.push({ orderByColumn: sortProp.prop, sortType: sortProp.order });
      this.getList();
    },
    handleChange() {

    },
    getRoleSelectList() {
      roleSelectList(this.selectParam).then((res) => {
        this.roleSelectData = res.data;
      })
    },
    getPostSelectList() {
      postSelectList(this.selectParam).then((res) => {
        this.postSelectData = res.data;
      })
    },
    getDeptTreeSelect() {
      detpTreeSelect(this.selectParam).then((res) => {
        this.deptTreeData = res.data;
      })
    },
    getOperators() {
      operators().then((res) => {
        this.operators = res.data
      })
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
      this.form.operatorId = store.getters.operatorId;
      this.getDeptTreeSelect();
      this.getRoleSelectList();
      this.getPostSelectList();
      this.showPass = true;
      this.open = true;
      this.title = '新增系统用户'
    },
    handleUpdate(row) {
      this.showPass = false;
      this.reset();
      this.title = '编辑系统用户'

      let id = row.id || this.ids;
      queryById(id).then((res) => {
        this.form = res.data;
        this.selectParam.operatorId = res.data.operatorId;
        console.log(this.selectParam)
        this.getDeptTreeSelect();
        this.getRoleSelectList();
        this.getPostSelectList();
      });
      this.open = true
    },
    handleExport() {
      let name = '系统用户_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.system + '/sys_user/export', this.queryParams, name)
    },
    handleDelete(row) {
      const idList = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为[' + idList + ']的数据项？').then(function () {
        return deleteSysUser(idList);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    submitForm() {
      console.log(this.form)
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.submitLoading = true;
          if (this.form.id != undefined) {
            //console.log('表单数据', this.form)
            //编辑
            updateSysUser(this.form).then((res) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).catch((err) => {
              this.submitLoading = false;
            })
          } else {
            //新增
            addSysUser(this.form).then((res) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).catch((err) => {
              this.submitLoading = false;
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
      this.submitLoading = false;
      this.form = {
        id: undefined,
        deptId: undefined,
        userName: undefined,
        userCode: undefined,
        nickName: undefined,
        userType: undefined,
        email: undefined,
        phoneNumber: undefined,
        sex: "3",
        avatar: undefined,
        password: undefined,
        status: "0",
        loginIp: undefined,
        loginDate: undefined,
        remark: undefined,
        operatorId: undefined,
        checkPassword: undefined,
        roleIds: [],
        postIds: []
      };
      this.resetForm("form");
    },
  },
}
</script>

<style lang="scss" scoped></style>
