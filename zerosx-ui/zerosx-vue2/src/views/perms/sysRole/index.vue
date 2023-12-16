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
      <el-form-item label="关键字" prop="roleKeyword">
        <el-input v-model="queryParams.t.roleKeyword" placeholder="角色名称或权限字符串关键字" clearable style="width: 220px;" />
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
          v-hasPerms="['perms:sysrole:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPerms="['perms:sysrole:update']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['perms:sysrole:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['perms:sysrole:export']">导出</el-button>
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
      </TablePlus>
    </div>

    <!-- 分页器 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 新增or删除 -->
    <el-dialog center :lock-scroll="true" :title="title" :visible.sync="open" width="750px" append-to-body
      :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="110px" size="small" :rules="rules">
        <el-row>
          <el-col :span="24">
            <el-form-item label="租户公司" prop="operatorId">
              <el-select clearable v-model="form.operatorId" placeholder="请选择租户公司" style="width: 100%;">
                <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="角色名称" prop="roleName">
              <el-input v-model="form.roleName" placeholder="请输入角色名称" maxlength="30" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- <el-col :span="12">
            <el-form-item label="权限字符串" prop="roleKey">
              <el-input v-model="form.roleKey" placeholder="请输入角色权限字符串" maxlength="100" show-word-limit />
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="12">
            <el-form-item label="显示顺序" prop="roleSort">
              <el-input-number v-model="form.roleSort" controls-position="right" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col> -->
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="菜单权限" prop="rolesIds">
              <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event)">展开/折叠</el-checkbox>
              <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event)">全选/全不选</el-checkbox>
              <el-tree class="tree-border" :data="menuOptions" show-checkbox ref="menu" node-key="id"
                :check-strictly="false" empty-text="加载中，请稍候" :props="defaultProps"></el-tree>
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
import { pageList, addSysRole, queryById, updateSysRole, deleteSysRole, roleMenuTree } from '@/api/perms/sysRole.js';
import serviceConfig from '@/api/serviceConfig'
import { operators } from '@/api/common.js'
export default {
  name: 'SysRole',
  dicts: ['StatusEnum'],
  data() {
    return {
      searching: false,
      dialogLoading: false,
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
      ids: [],
      menuExpand: false,
      menuNodeAll: false,
      allMenus: [],
      menuOptions: [],
      checkedKeys: [],
      dateRange: [],
      operators: [],
      defaultSort: {
        prop: 'createTime',
        order: 'descending'
      },
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          roleKeyword: undefined,
          status: undefined,
          operatorId: undefined,
        }
      },
      form: {
        id: undefined,
        roleName: undefined,
        roleKey: undefined,
        roleSort: undefined,
        status: "0",
        remark: undefined,
        operatorId: undefined,
        menuCheckStrictly: true,
        menuIds: [],
      },

      rules: {
        roleName: [
          { required: true, message: "角色名称不能为空", trigger: "blur" },
          { max: 30, message: '角色名称长度必须小于30个字符', trigger: 'blur' }
        ],
        roleKey: [
          { required: true, message: "角色权限字符串不能为空", trigger: "blur" },
          { max: 100, message: '角色权限字符串长度必须小于100个字符', trigger: 'blur' }
        ],
        roleSort: [
          { required: true, message: "显示顺序不能为空", trigger: "blur" },
        ],
        status: [
          { required: true, message: "角色状态不能为空", trigger: "change" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" },
          { max: 500, message: '备注长度必须小于500个字符', trigger: 'blur' }
        ],
        operatorId: [
          { required: true, message: "租户公司不能为空", trigger: "blur" }
        ],
      },
      columns: [
        // {
        //   attrs: {
        //     label: "角色ID",
        //     prop: "id",
        //     minWidth: "90",
        //     sortable: 'custom',
        //     align: "center",
        //   },
        // },
        {
          attrs: {
            label: "角色名称",
            prop: "roleName",
            minWidth: "140",
            sortable: 'custom',
            align: "center",
          },
        },
        // {
        //   attrs: {
        //     label: "角色权限字符串",
        //     prop: "roleKey",
        //     minWidth: "140",
        //     align: "center",
        //   },
        // },
        // {
        //   attrs: {
        //     label: "显示顺序",
        //     prop: "roleSort",
        //     minWidth: "100",
        //     sortable: 'custom',
        //     align: "center",
        //   },
        // },
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
            label: "备注",
            prop: "remark",
            minWidth: "240",
            showOverflowTooltip: true,
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
      //添加默认排序
      if (this.queryParams.sortList.length == 0) {
        this.queryParams.sortList.push({ orderByColumn: this.defaultSort.prop, sortType: this.defaultSort.order });
      }
      pageList(this.queryParams).then((resp) => {
        this.list = resp.data.list;
        this.total = resp.data.total;
        this.loading = false;
      }).catch((err) => {
        this.loading = false;
      });;
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleRoleMenuTree() {
      roleMenuTree({}).then((res) => {
        this.menuOptions = res.data.menus;
      })
    },
    handleCheckedTreeExpand(value) {
      let treeList = this.menuOptions;
      for (let i = 0; i < treeList.length; i++) {
        this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
      }
    },
    handleCheckedTreeNodeAll(value) {
      this.$refs.menu.setCheckedNodes(value ? this.menuOptions : []);
      //this.checkedOrNotTreeNodeAll(value, this.menuOptions);
      //console.log(this.allMenus)
    },

    checkedOrNotTreeNodeAll(value, menuNodeData) {
      if (menuNodeData && menuNodeData.length > 0) {
        console.log('12345', menuNodeData)
        this.allMenus.push(...menuNodeData)
        //this.$refs.menu.setCheckedNodes(value ? menuNodeData : []);
        for (let i = 0; i < menuNodeData.length; i++) {
          //console.log('1234', menuNodeData[i])
          if (menuNodeData[i].children && menuNodeData[i].children.length > 0) {
            //console.log('123', menuNodeData[i].children)
            this.checkedOrNotTreeNodeAll(value, menuNodeData[i].children)
          }
        }
      }
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
      this.getOperators();
      this.handleRoleMenuTree();
      this.open = true;
      this.title = '新增用户角色'
    },
    handleUpdate(row) {
      this.getOperators();
      this.reset();
      this.title = '编辑用户角色'
      this.open = true
      let id = row.id || this.ids;
      queryById(id).then((res) => {
        this.form = res.data;
        this.menuOptions = res.data.sysRoleMenuTreeVO.menus;
        this.checkedKeys = res.data.sysRoleMenuTreeVO.checkedKeys;
        this.checkedKeys.forEach((v) => {
          this.$nextTick(() => {
            this.$refs.menu.setChecked(v, true, false);
          })
        })
      });
    },
    handleExport() {
      let name = '用户角色_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.system + '/sys_role/export', this.queryParams, name)
    },
    handleDelete(row) {
      const idList = row.id || this.ids;
      this.$modal.confirm('是否确认删除已选择的数据项？').then(function () {
        return deleteSysRole(idList);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },

    // 所有菜单节点数据
    getMenuAllCheckedKeys() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.menu.getCheckedKeys();
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },

    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.dialogLoading = true;
          this.form.menuIds = this.getMenuAllCheckedKeys();
          if (this.form.id != undefined) {
            //console.log('表单数据', this.form)
            //编辑
            updateSysRole(this.form).then((res) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).catch(err => {
              this.dialogLoading = false;
            })
          } else {
            //新增
            addSysRole(this.form).then((res) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).catch(err => {
              this.dialogLoading = false;
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
      this.menuExpand = false;
      this.menuNodeAll = false;
      this.form = {
        id: undefined,
        roleName: undefined,
        roleKey: undefined,
        roleSort: undefined,
        status: "0",
        remark: undefined,
        operatorId: undefined,
        menuIds: [],
      };
      this.resetForm("form");
    },
  },
}
</script>

<style lang="scss" scoped>
.el-dialog:not(.is-fullscreen) {
  margin-top: 10vh !important;
}
</style>
