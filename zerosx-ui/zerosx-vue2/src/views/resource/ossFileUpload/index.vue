<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams.t" label-position="left" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="租户公司" prop="operatorId">
        <el-select clearable v-model="queryParams.t.operatorId" placeholder="请选择租户公司" style="width: 210px;">
          <el-option v-for="item in operators" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="OSS类型" prop="ossType">
        <el-input v-model="queryParams.t.ossType" placeholder="请输入OSS类型" clearable @keyup.enter.native="handleQuery"
          style="width: 210px;" />
      </el-form-item>
      <el-form-item label="文件名" prop="fileName">
        <el-input v-model="queryParams.t.fileName" placeholder="请输入文件名关键字" clearable @keyup.enter.native="handleQuery"
          style="width: 210px;" />
      </el-form-item>
    </el-form>
    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['resource:ossfile:delete']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
        @resetQuery="resetQuery" />
    </el-row>
    <!-- 表格 -->
    <div class="componentTable" style="height: calc(100vh - 220px)">
      <TablePlus ref="tables" :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleView="handleView" @handleSelectionChange="handleSelectionChange" @sort-change="handleSortChange"
        :defaultSort="defaultSort" actionWidth="80px">
      </TablePlus>
    </div>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog title="文件预览" :visible.sync="open" width="900px" append-to-body :close-on-click-modal="false">
      <el-form label-position="left" ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="24" style="display: flex;justify-content: center;">
            <el-image style="height: 360px;" fit="scale-down" :src="form.objectViewUrl"
              :preview-src-list="prewList"></el-image>
          </el-col>
          <el-col :span="24">
            <el-form-item label="源文件名：">{{ form.originalFileName }}</el-form-item>
            <el-form-item label="文件标识名：">{{ form.objectName }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="访问URL:">{{ form.objectViewUrl }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间：">{{ form.createTime }}</el-form-item>
            <el-form-item label="过期时间：">{{ form.expirationTime }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { pageList, fullDelete } from '@/api/resource/ossFileUpload.js'
import { operators } from '@/api/common.js'

export default {
  name: 'OssFileUpload',
  dicts: ['tenant_valid_status', 'StatusEnum'],
  data() {
    return {
      searching: false,
      loading: false,
      total: 0,
      list: [],
      single: true,
      multiple: true,
      showSearch: true,
      operators: [],
      open: false,
      form: {},
      ids: [],
      prewList: [],
      defaultSort: {
        prop: 'createTime',
        order: 'descending'
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          fileName: '',
          ossType: ''
        }
      },
      columns: [
        {
          attrs: {
            label: '记录ID',
            prop: 'id',
            minWidth: '100',
            align: 'center',
            sortable: 'custom',
          },
        },
        {
          attrs: {
            label: 'OSS类型',
            prop: 'ossType',
            minWidth: '100',
            align: 'center'
          },
        },
        {
          attrs: {
            label: '源文件名',
            prop: 'originalFileName',
            minWidth: '240',
            align: 'center',
            sortable: 'custom',
          },
        },
        {
          attrs: {
            label: '存储标识',
            prop: 'objectName',
            minWidth: '320',
            align: 'center'
          },
        },
        {
          attrs: {
            label: '存储链接',
            prop: 'objectUrl',
            minWidth: '240',
            showOverflowTooltip: true,
            align: 'center'
          },
        },
        {
          attrs: {
            label: '访问链接',
            prop: 'objectViewUrl',
            minWidth: '240',
            showOverflowTooltip: true,
            align: 'center'
          },
        },
        {
          attrs: {
            label: '创建时间',
            prop: 'createTime',
            minWidth: '140',
            align: 'center',
            sortable: 'custom',
          },
        },
        {
          attrs: {
            label: '失效日期',
            prop: 'expirationTime',
            minWidth: '140',
            align: 'center',
            sortable: 'custom',
          },
        },
        {
          attrs: {
            label: '创建人',
            prop: 'createBy',
            minWidth: '140',
            align: 'center'
          },
        },
        {
          attrs: {
            label: '备注',
            prop: 'remarks',
            minWidth: '140',
            align: 'center'
          },
        },
        {
          attrs: {
            label: '租户公司',
            prop: 'operatorName',
            minWidth: '140',
            showOverflowTooltip: true,
          },
        },
        {
          type: 'action',
          actions: [
            {
              type: 'detail',
              name: '详情',
              fcn: 'handleView',
              show: () => this.checkPermi(['resource:ossfile:detail'])
            },
          ]
        }
      ]
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
      pageList(this.queryParams).then((res) => {
        this.total = res.data.total;
        this.list = res.data.list;
        this.loading = false;
        this.searching = false;
      }).catch((err) => {
        this.loading = false;
        this.searching = false;
        console.log(err);
      })
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm");
      //清除排序（默认自动排序的列）
      this.$refs.tables.$refs.tablePlus.sort(this.defaultSort.prop, this.defaultSort.order)
    },
    handleUpload() { },
    handleDelete(row) {
      let ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的数据项？').then(function () {
        return fullDelete(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch((error) => { });
    },
    handleView(row) {
      this.open = true
      this.form = row;
      this.prewList = [row.objectViewUrl];
    }
  },
}
</script>
