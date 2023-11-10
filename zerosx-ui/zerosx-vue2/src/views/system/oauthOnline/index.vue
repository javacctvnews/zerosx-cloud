<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams.t" label-position="left" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="客户端" prop="clientId">
        <el-select clearable v-model="queryParams.t.clientId" placeholder="请选择客户端" style="width: 220px;">
          <el-option v-for="item in clients" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="用户名" prop="username">
        <el-input v-model="queryParams.t.username" placeholder="请输入用户名称" clearable style="width: 220px;" />
      </el-form-item>-->
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['auth:oauthClientDetails:delete']">强退</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDeleteAll"
          v-hasPerms="['auth:oauthClientDetails:deleteAll']">强退所有</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-delete" size="mini" @click="handleDeleteExpire"
          v-hasPerms="['auth:oauthClientDetails:deleteExpire']">清空过期TOKEN</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['auth:oauthClientDetails:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="handleQuery"
        @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div class="componentTable" style="height: calc(100vh - 220px)">
      <TablePlus :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleSelectionChange="handleSelectionChange" actionWidth="80px">
        <template slot-scope="scope" slot="status">
          <dict-tag :options="dict.type.StatusEnum" :value="scope.row.status" />
        </template>
        <template slot-scope="scope" slot="accessTokenValidity">
          <span>{{ scope.row.accessTokenValidity }} 秒</span>
        </template>
        <template slot-scope="scope" slot="refreshTokenValidity">
          <span>{{ scope.row.refreshTokenValidity }} 秒</span>
        </template>
      </TablePlus>
    </div>
    <!-- 分页器 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>

<script>
import { pageList, getClients, handleDeleteAll, forceLogout } from '@/api/system/oauthOnline.js';
import serviceConfig from '@/api/serviceConfig'
import { Message } from 'element-ui'

export default {
  name: 'OauthOnline',
  dicts: ['StatusEnum'],
  data() {
    return {
      searching: false,
      uploading: false,
      dialogLoading: false,
      loading: false,
      total: 0,
      showSearch: true,
      multiple: true,
      single: true,
      list: [],
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        t: {
          clientId: 'saas',
          username: undefined
        }
      },
      clients: [],
      columns: [
        {
          attrs: {
            label: "客户端",
            prop: "clientId",
            minWidth: "120",
            align: "center"
          },
        },
        {
          attrs: {
            label: "用户名",
            prop: "username",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "鉴权用户类型",
            prop: "authUserType",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "授权类型",
            prop: "grantType",
            minWidth: "100",
            align: "center"
          },
        },
        {
          attrs: {
            label: "TOKEN值",
            prop: "tokenValue",
            minWidth: "240",
            align: "center"
          },
        },
        {
          attrs: {
            label: "登录时间",
            prop: "loginExpiration",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "有效时长(秒)",
            prop: "expirationLength",
            minWidth: "120",
            align: "center"
          },
        },
        {
          attrs: {
            label: "过期时间",
            prop: "expiration",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "租户名称",
            prop: "operatorName",
            minWidth: "140",
            align: "center"
          },
        },
      ],
    }
  },
  created() {
    // this.queryParams.t.clientId = 'saas';
    this.getList();
    this.queryClients();
  },
  methods: {
    queryClients() {
      getClients().then((res) => {
        this.clients = res.data
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
      this.ids = selection.map(item => item.tokenValue)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    hasClientId() {
      if (!this.queryParams.t.clientId) {
        console.log('1233');
        Message({ message: "请选择客户端", type: 'error', duration: 3 * 1000 });
        return false;
      }
      console.log('1233');
      return true;
    },
    handleQuery() {
      if (!this.hasClientId()) {
        return;
      }
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.$refs["queryForm"].resetFields();
      this.handleQuery();
    },

    handleExport() {
      if (!this.hasClientId()) {
        return;
      }
      let name = '在线用户_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.auth + '/token2/export', this.queryParams, name)
    },
    handleDeleteAll() {
      let param = {
        clientId: this.queryParams.t.clientId,
        opType: '0'
      }
      this.$modal.confirm('是否确认强退所有TOKEN？').then(function () {
        return handleDeleteAll(param);
      }).then(() => {
        this.queryParams.pageNum = 1;
        this.getList();
        this.$modal.msgSuccess("清空成功");
      }).catch(() => { });
    },
    handleDeleteExpire() {
      let param = {
        clientId: this.queryParams.t.clientId,
        opType: '1'
      }
      this.$modal.confirm('是否确认清空过期TOKEN？').then(function () {
        return handleDeleteAll(param);
      }).then(() => {
        this.queryParams.pageNum = 1;
        this.getList();
        this.$modal.msgSuccess("清空成功");
      }).catch(() => { });
    },
    handleDelete(row) {
      //const idList = row.id || this.ids;
      let param = {
        tokens: this.ids
      }
      this.$modal.confirm('是否确认强退选择的数据项？').then(function () {
        return forceLogout(param);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },

  },
}
</script>
