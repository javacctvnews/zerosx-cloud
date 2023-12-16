<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams.t" label-position="left" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="业务标识" prop="bizTag">
        <el-input v-model="queryParams.t.bizTag" placeholder="请输入业务标识" clearable style="width: 220px;" />
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPerms="['leaf:leafAlloc:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPerms="['leaf:leafAlloc:update']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['leaf:leafAlloc:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['leaf:leafAlloc:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
        @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div class="componentTable" style="height: calc(100vh - 220px)">
      <TablePlus ref="tables" :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleSelectionChange="handleSelectionChange" actionWidth="80px" @sort-change="handleSortChange"
        :defaultSort="defaultSort">
        <template slot-scope="scope" slot="status">
          <dict-tag :options="dict.type.StatusEnum" :value="scope.row.status" />
        </template>
      </TablePlus>
    </div>

    <!-- 分页器 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 新增or删除 -->
    <el-dialog center :lock-scroll="true" :title="title" :visible.sync="open" width="700px" append-to-body
      :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="120px" size="small" :rules="rules">
        <el-form-item label="业务标识" prop="bizTag">
          <el-input :disabled="edit" v-model="form.bizTag" placeholder="请输入业务标识" maxlength="128" show-word-limit clearable />
        </el-form-item>
        <el-form-item label="号段当前最大ID" prop="maxId">
          <el-input-number :disabled="edit" style="width: 50%;" v-model="form.maxId" placeholder="请输入号段当前最大ID" :min="1" show-word-limit
            clearable />
        </el-form-item>
        <el-form-item label="号段步长" prop="step">
          <el-input-number style="width: 50%;" v-model="form.step" placeholder="请输入号段的步长" :min="1" :step="1000"
            :max="1000000" show-word-limit clearable />
        </el-form-item>
        <el-form-item label="业务描述" prop="description">
          <el-input type="textarea" v-model="form.description" placeholder="请输入业务描述" maxlength="256" show-word-limit
            clearable />
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
import { pageList, addLeafAlloc, queryById, updateLeafAlloc, deleteLeafAlloc } from '@/api/system/leafAlloc.js';
import serviceConfig from '@/api/serviceConfig'

export default {
  name: 'LeafAlloc',
  dicts: ['StatusEnum'],
  data() {
    return {
      searching: false,
      edit: false,
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
      ids: [],
      defaultSort: {
        prop: 'maxId',
        order: 'descending'
      },
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          bizTag: undefined,
          maxId: undefined,
          step: undefined,
          description: undefined,
        }
      },
      form: {
        bizTag: undefined,
        maxId: undefined,
        step: undefined,
        description: undefined,
      },

      rules: {
        bizTag: [
          { required: true, message: "业务标识不能为空", trigger: "blur" },
          { max: 128, message: '业务标识长度必须小于128个字符', trigger: 'blur' }
        ],
        maxId: [
          { required: true, message: "号段最大ID不能为空", trigger: "blur" },
        ],
        step: [
          { required: true, message: "号段步长不能为空", trigger: "blur" },
        ],
        description: [
          { required: true, message: "业务描述不能为空", trigger: "blur" },
          { max: 256, message: '业务描述长度必须小于256个字符', trigger: 'blur' }
        ],
      },
      columns: [
        {
          attrs: {
            label: "业务标识",
            prop: "bizTag",
            minWidth: "140",
            align: "center",
          },
        },
        {
          attrs: {
            label: "号段当前最大ID",
            prop: "maxId",
            minWidth: "180",
            align: "center",
            sortable: 'custom',
          },
        },
        {
          attrs: {
            label: "号段步长",
            prop: "step",
            minWidth: "100",
            align: "center",
          },
        },
        {
          attrs: {
            label: "最新更新时间",
            prop: "updateTime",
            minWidth: "140",
            align: "center",
            sortable: 'custom',
          },
        },
        {
          attrs: {
            label: "业务描述",
            prop: "description",
            minWidth: "240",
            align: "center",
          },
        },

      ],
    }
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
      this.ids = selection.map(item => item.bizTag)
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
      this.edit = false;
      this.title = '新增美团分布式ID'
    },
    handleUpdate(row) {
      this.reset();
      this.title = '编辑美团分布式ID'
      this.open = true
      this.edit = true;
      let id = row.bizTag || this.ids;
      queryById(id).then((res) => {
        this.form = res.data;
      });
    },
    handleExport() {
      let name = '美团分布式ID_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.leaf + '/leaf_alloc/export', this.queryParams, name)
    },
    handleDelete(row) {
      const idList = row.id || this.ids;
      this.$modal.confirm('是否确认删除已选择的数据项？').then(function () {
        return deleteLeafAlloc(idList);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.dialogLoading = true;
          if (this.edit) {
            //console.log('表单数据', this.form)
            //编辑
            updateLeafAlloc(this.form).then((res) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.dialogLoading = false
              this.getList();
            }).catch(err => {
              this.dialogLoading = false
            })
          } else {
            //新增
            addLeafAlloc(this.form).then((res) => {
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
        bizTag: undefined,
        maxId: undefined,
        step: undefined,
        description: undefined,
      };
      this.resetForm("form");
    },
  },
}
</script>

<style lang="scss" scoped></style>
