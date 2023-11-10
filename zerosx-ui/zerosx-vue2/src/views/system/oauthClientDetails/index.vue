<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams.t" label-position="left" ref="queryForm" size="small" :inline="true" v-show="showSearch"
      label-width="auto">
      <el-form-item label="应用标识" prop="clientId">
        <el-input v-model="queryParams.t.clientId" placeholder="请输入应用标识" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="应用名称" prop="clientName">
        <el-input v-model="queryParams.t.clientName" placeholder="请输入应用名称" clearable style="width: 220px;" />
      </el-form-item>
      <el-form-item label="授权方式" prop="authorizedGrantTypes">
        <el-select v-model="queryParams.t.authorizedGrantTypes" placeholder="授权方式" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.GranterTypeEnum" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.t.status" placeholder="状态" clearable style="width: 220px;">
          <el-option v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item> -->
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb5">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPerms="['auth:oauthClientDetails:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPerms="['auth:oauthClientDetails:update']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPerms="['auth:oauthClientDetails:delete']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPerms="['auth:oauthClientDetails:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdatePwd"
          v-hasPerms="['auth:oauthClientDetails:update']">修改密码</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :searching="searching" @handleQuery="getList"
        @resetQuery="resetQuery" />
    </el-row>

    <!-- 表格 -->
    <div class="componentTable" style="height: calc(100vh - 220px)">
      <TablePlus :data="list" :columns="columns" v-loading="loading" @handleDelete="handleDelete"
        @handleUpdatePwd="handleUpdatePwd" @handleSelectionChange="handleSelectionChange" @sort-change="handleSortChange"
        actionWidth="80px">
        <template slot-scope="scope" slot="status">
          <dict-tag :options="dict.type.StatusEnum" :value="scope.row.status" />
        </template>
        <template slot-scope="scope" slot="accessTokenTimeToLive">
          <span>{{ scope.row.accessTokenTimeToLive }} 秒</span>
        </template>
        <template slot-scope="scope" slot="refreshTokenTimeToLive">
          <span>{{ scope.row.refreshTokenTimeToLive }} 秒</span>
        </template>
      </TablePlus>
    </div>

    <!-- 分页器 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 修改密码 -->
    <el-dialog center :lock-scroll="true" :title="title" :visible.sync="openPwd" width="850px" append-to-body
      :close-on-click-modal="false">
      <el-form ref="form2" :model="form2" label-width="140px" size="small" :rules="rules2">
        <el-row>
          <el-col :span="12">
            <el-form-item label="应用标识" prop="clientId">
              <el-input disabled v-model="form2.clientId" placeholder="请输入应用标识" maxlength="32" show-word-limit
                clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用名称" prop="clientName">
              <el-input disabled v-model="form2.clientName" placeholder="请输入应用名称" maxlength="128" show-word-limit
                clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="应用密钥" prop="clientSecret">
              <el-input v-model="form2.clientSecret" placeholder="请输入应用密钥" maxlength="256" show-word-limit clearable />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="pwdDialogLoading" @click="submitFormPwd">确 定</el-button>
        <el-button @click="cancelPwd">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 新增or删除 -->
    <el-dialog center :lock-scroll="true" :title="title" :visible.sync="open" width="850px" append-to-body
      :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="140px" size="small" :rules="rules">
        <el-divider content-position="left" style="font-size: 15px;font-weight: 500;">基础配置</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="应用标识" prop="clientId">
              <el-input v-model="form.clientId" placeholder="请输入应用标识" maxlength="32" show-word-limit clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用名称" prop="clientName">
              <el-input v-model="form.clientName" placeholder="请输入应用名称" maxlength="128" show-word-limit clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="showPwd">
          <el-col :span="12">
            <el-form-item label="应用密钥" prop="clientSecret">
              <el-input v-model="form.clientSecret" placeholder="请输入应用密钥" maxlength="256" show-word-limit clearable />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.StatusEnum" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col> -->
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="密钥有效截止时间" prop="clientSecretExpiresAt">
              <el-date-picker type="datetime" format="" placeholder="选择日期" v-model="form.clientSecretExpiresAt"
                style="width: 100%;" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-row>
          <el-col :span="24">
            <el-form-item label="资源权限" prop="resourceIdList">
              <el-checkbox-group v-model="form.resourceIdList">
                <el-checkbox :key="dict.value" v-for="dict in dict.type.resource_ids" :label="dict.value"
                  @change="resourceIdChangeSelect">{{ dict.label }}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row>
          <el-col :span="24">
            <el-form-item label="授权方式" prop="authorizationGrantTypes">
              <el-checkbox-group v-model="form.authorizationGrantTypes">
                <el-checkbox :key="dict.value" v-for="dict in dict.type.GranterTypeEnum" :label="dict.value"
                  @change="grantTypeChangeSelect">{{ dict.label }}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="回调地址 " prop="redirectUris">
              <el-input type="textarea" v-model="form.redirectUris" placeholder="请输入回调地址 " maxlength="256" show-word-limit
                clearable />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left" style="font-size: 15px;font-weight: 500;">ClientSettings</el-divider>
        <el-row>
          <el-col :span="24">
            <el-form-item label="RequireProofKey" prop="requireProofKey">
              <el-radio-group v-model="form.requireProofKey">
                <el-radio v-for="dict in dict.type.EnableEnum" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left" style="font-size: 15px;font-weight: 500;">TokenSettings</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="令牌有效期" prop="accessTokenTimeToLive">
              <el-input-number v-model="form.accessTokenTimeToLive" :min="0" :step="3600" clearable style="width: 90%;" />
              秒
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="刷新令牌有效期" prop="refreshTokenTimeToLive">
              <el-input-number v-model="form.refreshTokenTimeToLive" :min="0" :step="3600" clearable
                style="width: 90%;" />
              秒
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="授权码有效期" prop="authorizationCodeTimeToLive">
              <el-input-number v-model="form.authorizationCodeTimeToLive" :min="0" :step="3600" clearable
                style="width: 90%;" /> 秒
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备授权码有效期" prop="deviceCodeTimeToLive">
              <el-input-number v-model="form.deviceCodeTimeToLive" :min="0" :step="3600" clearable style="width: 90%;" />
              秒
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="启用刷新令牌" prop="reuseRefreshTokens">
              <el-radio-group v-model="form.reuseRefreshTokens">
                <el-radio v-for="dict in dict.type.EnableEnum" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="ID令牌签名算法" prop="idTokenSignatureAlgorithm">
              <el-radio-group v-model="form.idTokenSignatureAlgorithm">
                <el-radio v-for="dict in dict.type.SignatureAlgorithmEnum" :key="dict.value" :label="dict.value">{{
                  dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-row>
          <el-col :span="24">
            <el-form-item label="附加信息" prop="additionalInformation">
              <el-input type="textarea" v-model="form.additionalInformation" placeholder="请输入附加信息" maxlength="4096"
                show-word-limit clearable />
            </el-form-item>
          </el-col>
        </el-row> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="dialogLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { pageList, addOauthClientDetails, queryById, updatePwd, updateOauthClientDetails, deleteOauthClientDetails } from '@/api/system/oauthClientDetails.js';
import serviceConfig from '@/api/serviceConfig'

export default {
  name: 'OauthClientDetails',
  dicts: ['StatusEnum', 'GranterTypeEnum', 'resource_ids', 'EnableEnum', 'SignatureAlgorithmEnum'],
  data() {
    return {
      openPwd: false,
      showPwd: true,
      searching: false,
      uploading: false,
      dialogLoading: false,
      pwdDialogLoading: false,
      loading: false,
      total: 0,
      showSearch: true,
      open: false,
      multiple: true,
      single: true,
      list: [],
      title: '',
      ids: [],
      row: undefined,
      dateRange: [],
      defaultSort: {
        prop: 'clientIdIssuedAt',
        order: 'descending'
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        sortList: [],
        t: {
          clientId: undefined,
          clientName: undefined,
          authorizedGrantTypes: undefined,
          status: undefined,
        }
      },
      form: {
        id: undefined,
        clientId: undefined,
        clientName: undefined,
        clientSecret: undefined,
        clientSecretExpiresAt: undefined,
        status: "0",
        resourceIdList: [],
        authorizationGrantTypes: [],
        redirectUris: undefined,
        requireProofKey: 'false',
        accessTokenTimeToLive: 7200,
        refreshTokenTimeToLive: 14400,
        authorizationCodeTimeToLive: 7200,
        deviceCodeTimeToLive: 7200,
        reuseRefreshTokens: 'true',
        idTokenSignatureAlgorithm: 'RS256'
      },
      form2: {
        id: undefined,
        clientId: undefined,
        clientName: undefined,
        clientSecret: undefined,
      },
      rules2: {
        clientSecret: [
          { required: true, message: "应用密钥不能为空", trigger: "blur" },
          { max: 256, message: '应用密钥长度必须小于256个字符', trigger: 'blur' }
        ],
      },
      rules: {
        clientId: [
          { required: true, message: "应用标识不能为空", trigger: "blur" },
          { max: 32, message: '应用标识长度必须小于32个字符', trigger: 'blur' }
        ],
        clientName: [
          { required: true, message: "应用名称不能为空", trigger: "blur" },
          { max: 128, message: '应用名称长度必须小于128个字符', trigger: 'blur' }
        ],
        resourceIdList: [
          { required: true, message: "资源限定不能为空", trigger: "blur" }
        ],
        clientSecret: [
          { required: true, message: "应用密钥不能为空", trigger: "blur" },
          { max: 256, message: '应用密钥长度必须小于256个字符', trigger: 'blur' }
        ],
        authorizationGrantTypes: [
          { required: true, message: "授权方式不能为空", trigger: "blur" }
        ],
        accessTokenValidity: [
          { required: true, message: "access_token有效期不能为空", trigger: "blur" }
        ],
        refreshTokenValidity: [
          { required: true, message: "refresh_token有效期不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ],
      },
      columns: [
        // {
        //   attrs: {
        //     label: "记录ID",
        //     prop: "id",
        //     minWidth: "90",
        //     align: "center"
        //   },
        // },
        {
          attrs: {
            label: "应用标识",
            prop: "clientId",
            minWidth: "140",
            align: "center"
          },
        },
        {
          attrs: {
            label: "应用名称",
            prop: "clientName",
            minWidth: "140",
            align: "center"
          },
        },
        // {
        //   slot: 'status',
        //   attrs: {
        //     label: "状态",
        //     prop: "status",
        //     minWidth: "100",
        //     align: "center"
        //   },
        // },
        // {
        //   attrs: {
        //     label: "资源限定串",
        //     prop: "resourceIds",
        //     minWidth: "140",
        //     align: "center"
        //   },
        // },
        // {
        //   attrs: {
        //     label: "应用密钥",
        //     prop: "clientSecretStr",
        //     minWidth: "140",
        //     align: "center"
        //   },
        // },
        // {
        //   attrs: {
        //     label: "范围",
        //     prop: "scope",
        //     minWidth: "140",
        //   },
        // },
        {
          attrs: {
            label: "授权方式",
            prop: "authorizedGrantTypes",
            minWidth: "340",
            align: "center"
          },
        },
        {
          attrs: {
            label: "回调地址 ",
            prop: "redirectUris",
            minWidth: "140",
            align: "center"
          },
        },
        // {
        //   attrs: {
        //     label: "权限",
        //     prop: "authorities",
        //     minWidth: "140",
        //   },
        // },
        {
          slot: 'accessTokenTimeToLive',
          attrs: {
            label: "access_token有效期",
            prop: "accessTokenTimeToLive",
            minWidth: "140",
            align: "center"
          },
        },
        {
          slot: 'refreshTokenTimeToLive',
          attrs: {
            label: "refresh_token有效期",
            prop: "refreshTokenTimeToLive",
            minWidth: "140",
            align: "center"
          },
        },
        // {
        //   attrs: {
        //     label: "附加信息",
        //     prop: "additionalInformation",
        //     minWidth: "140",
        //   },
        // },
        // {
        //   attrs: {
        //     label: "是否自动授权",
        //     prop: "autoapprove",
        //     minWidth: "140",
        //   },
        // },
        {
          attrs: {
            label: "密钥有效截止时间",
            prop: "clientSecretExpiresAt",
            minWidth: "160",
            align: "center",
            sortable: 'custom',
          },
        },
        {
          attrs: {
            label: "创建时间",
            prop: "clientIdIssuedAt",
            minWidth: "140",
            sortable: 'custom',
            align: "center"
          },
        },

        // {
        //   attrs: {
        //     label: "创建人",
        //     prop: "createBy",
        //     minWidth: "140",
        //     align: "center"
        //   },
        // },
        // {
        //   attrs: {
        //     label: "更新时间",
        //     prop: "updateTime",
        //     minWidth: "140",
        //     align: "center"
        //   },
        // },
        // {
        //   attrs: {
        //     label: "更新人",
        //     prop: "updateBy",
        //     minWidth: "140",
        //     align: "center"
        //   },
        // },

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
    resourceIdChangeSelect() {
      this.form.resourceIds = this.form.resourceIdList.toString();
      console.log(this.form.resourceIds)

    },
    grantTypeChangeSelect() {
      this.form.authorizedGrantTypes = this.form.authorizedGrantTypeList.toString();
      console.log(this.form.authorizedGrantTypes)
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
      this.row = selection[0]
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      //this.handleQuery()
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.showPwd = true;
      this.title = '新增客户端管理'
    },
    handleUpdate(row) {
      this.showPwd = false;
      this.reset();
      this.title = '编辑客户端管理'
      this.open = true
      let id = row.id || this.ids;
      queryById(id).then((res) => {
        this.form = res.data;
        this.form.authorizedGrantTypeList = res.data.authorizedGrantTypes === null ? [] : res.data.authorizedGrantTypes.split(',');
        this.form.resourceIdList = res.data.resourceIds === null ? [] : res.data.resourceIds.split(',');
      });
    },
    handleUpdatePwd() {
      this.reset2();
      this.title = '修改客户端密钥'
      this.form2.clientId = this.row.clientId;
      this.form2.clientName = this.row.clientName;
      this.openPwd = true;
    },
    handleExport() {
      let name = '客户端管理_' + this.parseTime(new Date(), '{y}{m}{d}{h}{i}{s}') + '.xlsx';
      this.download(serviceConfig.auth + '/oauth_client_details/export', this.queryParams, name)
    },
    handleDelete(row) {
      const idList = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为[' + idList + ']的数据项？').then(function () {
        return deleteOauthClientDetails(idList);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    submitFormPwd() {
      this.$refs["form2"].validate((valid) => {
        if (valid) {
          this.dialogLoading = true;
          //修改密码
          updatePwd(this.form2).then((res) => {
            this.$modal.msgSuccess("修改成功");
            this.openPwd = false;
            this.pwdDialogLoading = false
            this.getList();
          }).catch(err => {
            this.pwdDialogLoading = false
          })
        }
      })
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.dialogLoading = true;
          this.form.id = this.form.clientId;
          if (this.form.id != undefined) {
            //console.log('表单数据', this.form)
            //编辑
            updateOauthClientDetails(this.form).then((res) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.dialogLoading = false
              this.getList();
            }).catch(err => {
              this.dialogLoading = false
            })
          } else {
            //新增
            addOauthClientDetails(this.form).then((res) => {
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
    cancelPwd() {
      this.openPwd = false;
      this.reset2();
    },
    reset2() {
      this.pwdDialogLoading = false;
      this.form2 = {
        id: undefined,
        clientId: undefined,
        clientName: undefined,
        clientSecret: undefined,
      };
      this.resetForm("form2");
    },
    // 表单重置
    reset() {
      this.dialogLoading = false;
      this.form = {
        id: undefined,
        clientId: undefined,
        clientName: undefined,
        clientSecret: undefined,
        clientSecretExpiresAt: undefined,
        status: "0",
        resourceIdList: [],
        authorizationGrantTypes: [],
        redirectUris: undefined,
        requireProofKey: 'false',
        accessTokenTimeToLive: 7200,
        refreshTokenTimeToLive: 14400,
        authorizationCodeTimeToLive: 7200,
        deviceCodeTimeToLive: 7200,
        reuseRefreshTokens: 'true',
        idTokenSignatureAlgorithm: 'RS256'
      };
      this.resetForm("form");
    },
  },
}
</script>

<style lang="scss" scoped></style>
