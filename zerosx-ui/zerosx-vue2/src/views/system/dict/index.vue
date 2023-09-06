<template>
  <div class="app-container">
    <el-form label-position="left" :model="queryParams.t" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="字典名称" prop="dictName">
        <el-input v-model="queryParams.t.dictName" placeholder="请输入字典名称" clearable style="width: 220px"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="字典类型" prop="dictType">
        <el-input v-model="queryParams.t.dictType" placeholder="请输入字典类型" clearable style="width: 220px"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="dictStatus">
        <el-select v-model="queryParams.t.dictStatus" placeholder="字典状态" clearable style="width: 220px">
          <el-option v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPerms="['system:dict:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPerms="['system:dict:update']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['system:dict:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['system:dict:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-refresh" size="mini" @click="handleRefreshCache"
          v-hasPerms="['system:dict:initCache']">刷新缓存</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
        @resetQuery="resetQuery" />
    </el-row>

    <div class="componentTable" style="height: calc(100vh - 220px);">
      <TablePlus ref="tables" :data="typeList" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleDictData="handleDictData" actionWidth="90px" @handleSelectionChange="handleSelectionChange"
        @sort-change="handleSortChange" :defaultSort="defaultSort">
        <template slot-scope="scope" slot="dictType">
          <!-- <router-link :to="'/system/dict-data/index/' + scope.row.id" class="link-type">
            <span>{{ scope.row.dictType }}</span>
          </router-link> -->
          <span>{{ scope.row.dictType }}</span>
        </template>
        <template slot-scope="scope" slot="dictStatus">
          <dict-tag :options="dict.type.StatusEnum" :value="scope.row.dictStatus" />
        </template>
      </TablePlus>
    </div>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="form.dictType" placeholder="请输入字典类型" :disabled="editDisabled" />
        </el-form-item>
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="form.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="状态" prop="dictStatus">
          <el-radio-group v-model="form.dictStatus">
            <el-radio v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.value">{{ dict.label
            }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="dictDataTitle" :visible.sync="dataOpen" width="1100px" :lock-scroll="true" center append-to-body
      :close-on-click-modal="false">
      <DataDialog v-if="dataOpen" :dictId="rowDictType"></DataDialog>
    </el-dialog>
  </div>
</template>

<script>
import DataDialog from './dataDialog.vue';
import { listType, getType, delType, addType, updateType, refreshCache } from "@/api/system/dict/type";
import serviceConfig from '@/api/serviceConfig'
export default {
  name: "Dict",
  components: { DataDialog },
  dicts: ['StatusEnum'],
  data() {
    return {
      dictDataTitle: '字典类型数据',
      dataOpen: false,
      rowDictType: '',
      // 遮罩层
      loading: true,
      searching: true,
      // 选中数组
      dictIds: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 字典表格数据
      typeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      defaultSort: {
        prop: 'createTime',
        order: 'descending'
      },
      // 编辑时disable
      editDisabled: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          dictName: undefined,
          dictType: undefined,
          dictStatus: undefined,
        }
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        dictName: [
          { required: true, message: "字典名称不能为空", trigger: "blur" }
        ],
        dictType: [
          { required: true, message: "字典类型不能为空", trigger: "blur" }
        ]
      },
      columns: [
        {
          attrs: {
            label: '记录ID',
            prop: 'id',
            minWidth: '100',
            sortable: 'custom',
          },
        },
        {
          attrs: {
            label: '字典类型名称',
            prop: 'dictName',
            minWidth: '140',
            sortable: 'custom',
          },
        },
        {
          slot: 'dictType',
          attrs: {
            label: '字典类型编码',
            prop: 'dictType',
            minWidth: '160',
            sortable: 'custom',
          },
        },
        {
          slot: 'dictStatus',
          attrs: {
            label: '状态',
            prop: 'dictStatus',
            minWidth: '80'
          },
        },
        {
          attrs: {
            label: '备注',
            prop: 'remarks',
            minWidth: '240'
          },
        },
        {
          attrs: {
            label: '创建人',
            prop: 'createBy',
            minWidth: '120'
          },
        },
        {
          attrs: {
            label: '创建时间',
            prop: 'createTime',
            minWidth: '140',
            sortable: 'custom',
          },
        },
        {
          attrs: {
            label: '更新人',
            prop: 'updateBy',
            minWidth: '120'
          },
        },
        {
          attrs: {
            label: '更新时间',
            prop: 'updateTime',
            minWidth: '140'
          },
        },
        {
          type: 'action',
          actions: [
            {
              type: 'edit',
              name: '数据',
              fcn: 'handleDictData',
              show: () => this.checkPermi(['system:dict:dictdata'])
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
    handleDictData(row) {
      //console.log(row)
      this.rowDictType = row.id
      this.dictDataTitle = '字典类型【' + row.dictType + '】数据'
      this.dataOpen = true
    },

    /** 查询字典类型列表 */
    getList() {
      this.loading = true;
      this.searching = true;
      //添加默认排序
      if (this.queryParams.sortList.length == 0) {
        this.queryParams.sortList.push({ orderByColumn: this.defaultSort.prop, sortType: this.defaultSort.order });
      }
      listType(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.typeList = response.data.list;
        this.total = response.data.total;
        this.loading = false;
        this.searching = false;
      }).catch((err) => {
        this.loading = false;
        this.searching = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.editDisabled = false;
      this.form = {
        id: undefined,
        dictName: undefined,
        dictType: undefined,
        dictStatus: "0",
        remarks: undefined
      };
      this.resetForm("form");
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
      this.$refs.tables.$refs.tablePlus.sort(this.defaultSort.prop, this.defaultSort.order)
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加字典类型";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.dictIds = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.dictIds
      getType(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改字典类型";
        this.editDisabled = true;
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateType(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.editDisabled = false;
              this.getList();
            });
          } else {
            addType(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const dictIds = row.id || this.dictIds;
      this.$modal.confirm('是否确认删除字典编号为"' + dictIds + '"的数据项？').then(function () {
        return delType(dictIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      let name = '字典类型_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.system + '/sysDictType/export', this.queryParams, name)
    },
    /** 刷新缓存按钮操作 */
    handleRefreshCache() {
      this.loading = true;
      refreshCache().then(() => {
        this.getList();
        this.$modal.msgSuccess("刷新成功");
        this.$store.dispatch('dict/cleanDict');
        this.loading = flase;
      }).catch(err => {
        this.loading = false;
      });
    }
  }
};
</script>