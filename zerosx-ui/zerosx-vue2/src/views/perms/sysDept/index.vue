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
      <el-form-item label="部门名称" prop="deptName">
        <el-input v-model="queryParams.t.deptName" placeholder="请输入部门名称" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="部门状态" prop="status">
        <el-select v-model="queryParams.t.status" placeholder="部门状态" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPerms="['perms:sysdept:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPerms="['perms:sysdept:update']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['perms:sysdept:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['perms:sysdept:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-sort" size="mini" @click="toggleExpandAll">展开/折叠</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
        @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div class="componentTable" style="height: calc(100vh - 190px)">
      <TablePlus ref="tables" :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        row-key="id" :hideIndex="true" :border="true" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        v-if="refreshTable" :default-expand-all="isExpandAll" @handleSelectionChange="handleSelectionChange"
        @sort-change="handleSortChange" :defaultSort="defaultSort" actionWidth="80px">
        <template slot-scope="scope" slot="status">
          <dict-tag :options="dict.type.StatusEnum" :value="scope.row.status" />
        </template>
      </TablePlus>
    </div>

    <!-- 分页器 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 新增or删除 -->
    <el-dialog center :lock-scroll="true" :title="title" :visible.sync="open" width="750px" append-to-body
      :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="110px" size="small" :rules="rules">
        <el-divider content-position="left">基础信息</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="租户公司" prop="operatorId">
              <el-select clearable v-model="form.operatorId" placeholder="请选择租户公司" style="width: 100%;">
                <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上级部门" prop="parentId">
              <treeselect v-model="form.parentId" :options="deptTreeData" :show-count="true" placeholder="请选择上级部门" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入部门名称" maxlength="30" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input-number v-model="form.orderNum" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left">部门负责人信息</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入负责人" maxlength="20" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" maxlength="11" show-word-limit />
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left">职责信息</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="角色权限" prop="roleIds">
              <el-select multiple clearable v-model="form.roleIds" placeholder="请选择权限角色" style="width: 100%;">
                <el-option v-for="item in roleSelectData" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="dialogLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import store from "@/store";
import { addSysDept, queryById, updateSysDept, deleteSysDept, tableTree, detpTreeSelect } from '@/api/perms/sysDept.js';
import { roleSelectList } from '@/api/perms/sysRole.js';
import serviceConfig from '@/api/serviceConfig'
import { operators } from '@/api/common.js'
import { patternConsts } from '@/utils/validate'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: 'SysDept',
  components: { Treeselect },
  dicts: ['StatusEnum'],
  data() {
    return {
      // 重新渲染表格状态
      refreshTable: true,
      isExpandAll: false,
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
      deptTreeData: [],
      roleSelectData: [],
      deptProps: {
        checkStrictly: true,
        expandTrigger: 'hover',
        label: 'deptName',
        children: 'children',
        value: 'id'
      },
      selectParam: {

      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          deptName: undefined,
          deptCode: undefined,
          parentId: undefined,
          ancestors: undefined,
          orderNum: undefined,
          leader: undefined,
          phone: undefined,
          email: undefined,
          status: undefined,
          operatorId: undefined,
        }
      },
      form: {
        id: undefined,
        deptName: undefined,
        deptCode: undefined,
        parentId: undefined,
        ancestors: undefined,
        orderNum: undefined,
        leader: undefined,
        phone: undefined,
        email: undefined,
        status: undefined,
        operatorId: undefined,
        roleIds: []
      },

      rules: {
        deptName: [
          { required: true, message: "部门名称不能为空", trigger: "blur" },
          { max: 30, message: '部门名称长度必须小于30个字符', trigger: 'blur' }
        ],
        deptCode: [
          { required: true, message: "部门编码不能为空", trigger: "blur" },
          { max: 20, message: '部门编码长度必须小于20个字符', trigger: 'blur' }
        ],
        // parentId: [
        //   { required: true, message: "父部门id不能为空", trigger: "blur" },
        // ],
        ancestors: [
          { required: true, message: "祖级列表不能为空", trigger: "blur" },
          { max: 500, message: '祖级列表长度必须小于500个字符', trigger: 'blur' }
        ],
        orderNum: [
          { required: true, message: "显示顺序不能为空", trigger: "blur" },
        ],
        leader: [
          { required: true, message: "负责人不能为空", trigger: "blur" },
          { max: 20, message: '负责人长度必须小于20个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" },
          { pattern: patternConsts.phone, message: "请输入正确的手机号码", trigger: "blur" }
        ],
        email: [
          { required: true, message: "邮箱不能为空", trigger: "blur" },
          { pattern: patternConsts.email, message: "请输入正确的邮箱", trigger: "blur" }
        ],
        status: [
          { required: true, message: "部门状态不能为空", trigger: "blur" },
          { max: 1, message: '部门状态长度必须小于1个字符', trigger: 'blur' }
        ],
        operatorId: [
          { required: true, message: "租户公司不能为空", trigger: "blur" },
          { max: 20, message: '租户公司长度必须小于20个字符', trigger: 'blur' }
        ],
      },
      columns: [
        {
          attrs: {
            label: "部门名称",
            prop: "deptName",
            minWidth: "200",
            fixed: true,
            //sortable: 'custom',
          },
        },
        // {
        //   attrs: {
        //     label: "部门id",
        //     prop: "id",
        //     minWidth: "140"
        //   },
        // },
        // {
        //   attrs: {
        //     label: "部门编码",
        //     prop: "deptCode",
        //     minWidth: "140",
        //   },
        // },
        {
          attrs: {
            label: "父部门id",
            prop: "parentId",
            minWidth: "100",
            align: "center",
            //sortable: 'custom',
          },
        },
        // {
        //   attrs: {
        //     label: "祖级列表",
        //     prop: "ancestors",
        //     minWidth: "140",
        //   },
        // },
        {
          attrs: {
            label: "显示顺序",
            prop: "orderNum",
            minWidth: "140",
            //sortable: 'custom',
            align: "center",
          },
        },
        {
          attrs: {
            label: "负责人",
            prop: "leader",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "联系电话",
            prop: "phone",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "邮箱",
            prop: "email",
            minWidth: "140",
            align: "center",
          },
        },
        {
          slot: 'status',
          attrs: {
            label: "部门状态",
            prop: "status",
            minWidth: "100",
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
    this.form.operatorId = store.getters.operatorId;
  },
  methods: {
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    getRoleSelectList() {
      roleSelectList(this.selectParam).then((res) => {
        this.roleSelectData = res.data;
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
      tableTree(this.queryParams.t).then((resp) => {
        this.list = resp.data;
        //this.total = resp.data.total;
        this.loading = false;
        this.searching = false;
      }).catch((err) => {
        this.loading = false;
        this.searching = false;
      });
    },
    handleChange() {
      let checkedNode = this.$refs.dept.getCheckedNodes();
      console.log('12121', checkedNode)
      if (checkedNode && checkedNode.length > 0) {
        this.form.parentId = checkedNode[0].value
      } else {
        this.form.parentId = "";
      }
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
      this.selectParam.operatorId = store.getters.operatorId;
      this.getDeptTreeSelect();
      this.getRoleSelectList();
      this.open = true;
      this.title = '新增部门表'
    },
    handleUpdate(row) {
      this.reset();
      this.title = '编辑部门表'
      this.open = true
      let id = row.id || this.ids;
      queryById(id).then((res) => {
        this.form = res.data;
        this.selectParam.operatorId = res.data.operatorId;
        this.getRoleSelectList();
        this.getDeptTreeSelect();
      });
    },
    handleExport() {
      let name = '部门表_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.system + '/sys_dept/export', this.queryParams, name)
    },
    handleDelete(row) {
      const idList = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为[' + idList + ']的数据项？').then(function () {
        return deleteSysDept(idList);
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
            updateSysDept(this.form).then((res) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.dialogLoading = false
              this.getList();
            }).catch(err => {
              this.dialogLoading = false
            })
          } else {
            //新增
            addSysDept(this.form).then((res) => {
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
        deptName: undefined,
        deptCode: undefined,
        parentId: undefined,
        ancestors: undefined,
        orderNum: undefined,
        leader: undefined,
        phone: undefined,
        email: undefined,
        status: "0",
        operatorId: undefined,
        roleIds: []
      };
      this.resetForm("form");
    },
  },
}
</script>

<style lang="scss" scoped></style>
