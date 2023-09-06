<template>
  <div class="main">
    <div class="login_container">
      <div class="main_container">
        <div class="login-form">
          <div class="login-form-left">
            <div class="login-form-left-c1">
              <div class="platform-name">
                <span>{{ title }}</span>
                <span>演示系统</span>
              </div>
              <div class="platform-users">
                <h3>体验账号</h3>
                <ul>
                  <li>超级管理员账号：admin123/Admin123</li>
                  <li>租户管理员账号：zeros/Zeros1234</li>
                  <li>租户普通账号：z001/Zeros1234</li>
                </ul>
              </div>
            </div>

          </div>
          <div class="login-form-right">
            <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form-right-c1">
              <h3 class="title">账户密码登录</h3>
              <el-form-item prop="username">
                <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号" clearable>
                  <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
                </el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码" show-password
                  clearable @keyup.enter.native="handleLogin">
                  <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
                </el-input>
              </el-form-item>
              <el-form-item prop="code" v-if="captchaEnabled">
                <el-input clearable v-model="loginForm.code" auto-complete="off" placeholder="验证码" style="width: 63%"
                  @keyup.enter.native="handleLogin">
                  <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
                </el-input>
                <div class="login-code">
                  <img :src="codeUrl" @click="getCode" class="login-code-img" />
                </div>
              </el-form-item>
              <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
              <el-form-item style="width:100%;">
                <el-button :loading="loading" type="primary" plain style="width:100%;height: 45px;font-size: 18px;"
                  @click.native.prevent="handleLogin">
                  <span v-if="!loading">登 录</span>
                  <span v-else>登 录 中...</span>
                </el-button>
                <div style="float: right;" v-if="register">
                  <router-link class="link-type" :to="'/register'">立即注册</router-link>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>
    <div class="footer_container">
      <div class="main_container">
        Copyright © 2018-2023 {{ author }} All Rights Reserved.
      </div>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: "Login",
  data() {
    return {
      title: process.env.VUE_APP_TITLE,
      author: process.env.VUE_APP_AUTHOR,
      codeUrl: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.getCode();
    this.getCookie();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = res.data.img;
          this.loginForm.uuid = res.data.uuid;
        }
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(() => { });
          }).catch((err) => {
            console.log('出现异常:', err)
            this.loading = false;
            if (this.captchaEnabled) {
              this.getCode();
            }
          });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.main {
  width: 100%;
  height: 100%;
  background-image: url(../assets/images/bg2.png);
  background-size: cover;
}

.main_container {
  width: 1190px;
  margin: 0 auto;
}

.top_bar {
  height: 80px;
  line-height: 80px;

  .logo {
    display: flex;
    height: 80px;
    line-height: 80px;
    font-size: 24px;

    img {
      width: 160px;
      height: 80px;
    }
  }
}

.login_container {
  height: calc(100vh - 20px);
}

.login-form {
  height: calc(100vh - 20px);
  display: flex;
  width: 80%;
  margin: 0 auto;
}

.login-form-left {
  position: relative;
  min-height: 450px;
  margin: auto;
  // background-color: #8b9aac;
  background-color: #d7e6f4;
  width: 50%;
  float: left;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  box-sizing: border-box;

  .login-form-left-c1 {
    width: 100%;
    color: #1890ff;

    .platform-name {
      width: 100%;
      padding: 10px;
      font-size: 28px;
      text-align: center;

      span {
        display: block;
      }

      span:last-child {
        color: red;
        font-size: 18px;
        padding: 10px;
      }
    }

    .platform-users {
      width: 100%;
      padding: 10px;

      ul li {
        margin-bottom: 10px;
      }
    }
  }
}

.login-form-right {
  position: relative;
  min-height: 450px;
  align-items: center;
  display: flex;
  margin: auto;
  background-color: #FFF;
  width: 50%;
  float: left;
  border-left: none;
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  color: #fff;
  box-sizing: border-box;
  justify-content: center;
  flex-direction: column;
}

.login-form-right-c1 {
  width: 90%;
  border-radius: 6px;
  background: #ffffff;
  padding: 20px;

  .title {
    color: #666;
    text-align: center;
  }

  .el-input {
    height: 40px;

    input {
      height: 40px;
    }
  }

  .input-icon {
    height: 40px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-code {
  width: 35%;
  height: 40px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }

  .login-code-img {
    height: 40px;
    border: 1px solid #f0f0f0;
    width: 100%;
  }
}

.footer_container {
  height: 20px;
  line-height: 20px;
  font-size: 12px;
  letter-spacing: 1px;
  text-align: center;
  color: #FFF;
}
</style>
