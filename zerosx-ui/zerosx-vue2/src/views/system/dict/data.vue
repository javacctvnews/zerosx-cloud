<template>
  <div class="app-container">
    <el-form label-position="right" :model="queryParams.t" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="字典类型名称" prop="dictType">
        <el-select v-model="queryParams.t.dictType" style="width: 220px;">
          <el-option v-for="item in typeOptions" :key="item.id" :label="item.dictName" :value="item.dictType" />
        </el-select>
      </el-form-item>
      <el-form-item label="数据标签" prop="dictLabel">
        <el-input v-model="queryParams.t.dictLabel" placeholder="请输入数据标签" clearable @keyup.enter.native="handleQuery"
          style="width: 220px;" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.t.status" placeholder="数据状态" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="searching" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-close" size="mini" @click="handleClose">关闭</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <div class="componentTable" style="height: calc(100vh - 245px);">
      <TablePlus :data="dataList" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleSelectionChange="handleSelectionChange" actionWidth="80px">
        <template slot-scope="scope" slot="status">
          <dict-tag :options="dict.type.StatusEnum" :value="scope.row.status" />
        </template>
        <template slot-scope="scope" slot="dictLabel">
          <span v-if="scope.row.listClass == '' || scope.row.listClass == 'default'">{{ scope.row.dictLabel }}</span>
          <el-tag v-else :type="scope.row.listClass == 'primary' ? '' : scope.row.listClass">{{ scope.row.dictLabel
          }}</el-tag>
        </template>
      </TablePlus>
    </div>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典类型">
          <el-input v-model="form.dictType" :disabled="true" />
        </el-form-item>
        <el-form-item label="数据标签" prop="dictLabel">
          <el-input v-model="form.dictLabel" placeholder="请输入数据标签" />
        </el-form-item>
        <el-form-item label="数据键值" prop="dictValue">
          <el-input v-model="form.dictValue" placeholder="请输入数据键值" />
        </el-form-item>
        <el-form-item label="样式属性" prop="cssClass">
          <el-input v-model="form.cssClass" placeholder="请输入样式属性" />
        </el-form-item>
        <el-form-item label="显示排序" prop="dictSort">
          <el-input-number v-model="form.dictSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="回显样式" prop="listClass">
          <el-select v-model="form.listClass">
            <el-option v-for="item in listClassOptions" :key="item.value" :label="item.label + '(' + item.value + ')'"
              :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
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
  </div>
</template>

<script>
import { listData, getData, delData, addData, updateData } from "@/api/system/dict/data";
import { optionselect as getDictOptionselect, getType } from "@/api/system/dict/type";
import serviceConfig from '@/api/serviceConfig'
export default {
  name: "Data",
  dicts: ['StatusEnum'],
  data() {
    return {
      searching:false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 字典表格数据
      dataList: [],
      // 默认字典类型
      defaultDictType: "",
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据标签回显样式
      listClassOptions: [
        {
          value: "default",
          label: "默认"
        },
        {
          value: "primary",
          label: "主要"
        },
        {
          value: "success",
          label: "成功"
        },
        {
          value: "info",
          label: "信息"
        },
        {
          value: "warning",
          label: "警告"
        },
        {
          value: "danger",
          label: "危险"
        }
      ],
      // 类型数据字典
      typeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        t: {
          dictLabel: undefined,
          dictType: undefined,
          status: undefined,
        }
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        dictLabel: [
          { required: true, message: "数据标签不能为空", trigger: "blur" }
        ],
        dictValue: [
          { required: true, message: "数据键值不能为空", trigger: "blur" }
        ],
        dictSort: [
          { required: true, message: "数据顺序不能为空", trigger: "blur" }
        ]
      },
      columns: [
        // {
        //   attrs: {
        //     label: '记录ID',
        //     prop: 'id',
        //     minWidth: '70'
        //   },
        // },
        {
          attrs: {
            label: '字典类型编码',
            prop: 'dictType',
            minWidth: '160'
          },
        },
        {
          slot: 'dictLabel',
          attrs: {
            label: '数据标签',
            prop: 'dictLabel',
            minWidth: '120'
          },
        },
        {
          attrs: {
            label: '数据键值',
            prop: 'dictValue',
            minWidth: '100'
          },
        },
        {
          slot: 'status',
          attrs: {
            label: '状态',
            prop: 'status',
            minWidth: '80'
          },
        },
        {
          attrs: {
            label: '显示排序',
            prop: 'dictSort',
            minWidth: '80'
          },
        },
        {
          attrs: {
            label: '备注',
            prop: 'remarks',
            minWidth: '180'
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
            minWidth: '140'
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
      ]
    };
  },
  created() {
    const dictId = this.$route.params && this.$route.params.dictId;
    this.getType(dictId);
    this.getTypeList();
  },
  methods: {
    /** 查询字典类型详细 */
    getType(dictId) {
      getType(dictId).then(response => {
        this.queryParams.t.dictType = response.data.dictType;
        this.defaultDictType = response.data.dictType;
        this.getList();
      })
    },
    /** 查询字典类型列表 */
    getTypeList() {
      getDictOptionselect({}).then(response => {
        this.typeOptions = response.data;
      });
    },
    /** 查询字典数据列表 */
    getList() {
      this.loading = true;
      this.searching = true;
      listData(this.queryParams).then(response => {
        this.dataList = response.data.list;
        this.total = response.data.total;
        this.loading = false;
        this.searching = false;
      }).catch(err=>{
        this.loading = false;
        this.searching = false;
      });;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        dictType: undefined,
        dictLabel: undefined,
        dictValue: undefined,
        cssClass: undefined,
        listClass: 'default',
        dictSort: 0,
        status: "0",
        remarks: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 返回按钮操作 */
    handleClose() {
      const obj = { path: "/system/dict" };
      this.$tab.closeOpenPage(obj);
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.dictType = this.defaultDictType;
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加字典数据";
      this.form.dictType = this.queryParams.t.dictType;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const dictCode = row.id || this.ids
      getData(dictCode).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改字典数据";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log(this.form.id)
          if (this.form.id != undefined) {
            updateData(this.form).then(response => {
              this.$store.dispatch('dict/removeDict', this.queryParams.dictType);
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addData(this.form).then(response => {
              this.$store.dispatch('dict/removeDict', this.queryParams.dictType);
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
      const dictCodes = row.id || this.ids;
      this.$modal.confirm('是否确认删除字典编码为"' + dictCodes + '"的数据项？').then(function () {
        return delData(dictCodes);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
        this.$store.dispatch('dict/removeDict', this.queryParams.dictType);
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      let name = '字典数据_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.resource + '/sysDictData/export', {
        ...this.queryParams.t
      }, name)
    }
  }
};
</script>