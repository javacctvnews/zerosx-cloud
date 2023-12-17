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
                  <li>租户管理员账号：zuo123/Zuo123456</li>
                </ul>
              </div>
            </div>
          </div>
          <div class="login-form-right">
            <!-- 账号密码登录 -->
            <el-form v-if="accountLogin" ref="loginForm" :model="loginForm" :rules="loginRules"
              class="login-form-right-c1">
              <h3 class="title">登录</h3>
              <el-form-item prop="username">
                <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="请输入用户名" clearable>
                  <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
                </el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="请输入密码"
                  show-password clearable @keyup.enter.native="handleLogin">
                  <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
                </el-input>
              </el-form-item>
              <el-form-item prop="code" v-if="captchaEnabled">
                <el-input clearable v-model="loginForm.code" auto-complete="off" placeholder="验证码(纯数字)" style="width: 60%"
                  @keyup.enter.native="handleLogin">
                  <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
                </el-input>
                <div class="login-code">
                  <img :src="codeUrl" @click="getCode" class="login-code-img" />
                </div>
              </el-form-item>
              <div>
                <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住我</el-checkbox>
                <div class="smsLogin">
                  <a href="#" @click="switchSmsLogin">短信验证登录</a>
                </div>
              </div>
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
            <el-form v-if="!accountLogin" ref="smsLoginForm" :model="smsLoginForm" :rules="smsLoginRules"
              class="login-form-right-c1">
              <h3 class="title">登录</h3>
              <el-form-item prop="mobilePhone">
                <el-input v-model="smsLoginForm.mobilePhone" type="text" auto-complete="off" placeholder="手机号码" clearable>
                  <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
                </el-input>
              </el-form-item>
              <el-form-item prop="code2" v-if="captchaEnabled">
                <el-input clearable v-model="smsLoginForm.code2" auto-complete="off" placeholder="验证码(纯数字)" style="width: 60%">
                  <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
                </el-input>
                <div class="login-code">
                  <img :src="codeUrl" @click="getCode" class="login-code-img" />
                </div>
              </el-form-item>
              <el-form-item prop="smsCode">
                <el-input clearable v-model="smsLoginForm.smsCode" auto-complete="off" placeholder="短信验证码"
                  style="width: 60%">
                  <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
                </el-input>
                <el-button v-if="smsShow" style="margin-left: 10px;height: 40px;float: right;width:145px" plain
                  @click="getSmsCode">发送验证码</el-button>
                <el-button v-else style="margin-left: 10px;height: 40px;width:145px" plain disabled>{{ countdown
                }}后秒重新发送</el-button>
                <!-- <div class="sms-code" @click="getSmsCode">
                  <span v-if="smsShow">发送短信验证码</span>
                  <span v-else>{{ countdown }}后重新发送</span>
                </div> -->
              </el-form-item>
              <div>
                <el-checkbox v-model="smsLoginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住我</el-checkbox>
                <div class="smsLogin">
                  <a href="#" @click="handleAccountLogin">用户名密码登录</a>
                </div>
              </div>
              <el-form-item style="width:100%;">
                <el-button :loading="loading" type="primary" plain style="width:100%;height: 45px;font-size: 18px;"
                  @click.native.prevent="handleSmsLogin">
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
        <div style="color:#fff;text-align: center;margin-bottom: 15px;font-size: 12px;">
          Copyright © 2018-2023 {{ author }} All Rights Reserved.
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getCodeImg, smsCode } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'
import { Notification, MessageBox, Message, Loading } from 'element-ui'
import serviceConfig from '@/api/serviceConfig'

export default {
  name: "Login",
  data() {
    return {
      accountLogin: true,
      title: process.env.VUE_APP_TITLE,
      author: process.env.VUE_APP_AUTHOR,
      codeUrl: "",
      countdown: 0,
      smsShow: true,
      timer: null,
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: "",
        mobilePhone: '',
        smsCode: '',
        grant_type: '',
      },
      smsLoginForm: {
        rememberMe: false,
        code2: "",
        mobilePhone: '',
        smsCode: '',
        grant_type: '',
        uuid: "",
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }],
      },
      smsLoginRules: {
        code2: [{ required: true, trigger: "blur", message: "请输入验证码" }],
        mobilePhone: [
          { required: true, trigger: "blur", message: "请输入您的手机号码" }
        ],
        smsCode: [
          { required: true, trigger: "blur", message: "请输入手机验证码" }
        ]
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
    this.loginForm.grant_type = serviceConfig.captcha;
  },
  methods: {
    getSmsCode() {
      this.$refs.smsLoginForm.validateField(['mobilePhone'], (err) => {
        if (!err) {
          this.$refs.smsLoginForm.validateField(['code2'], (err) => {
            if (!err) {
              this.querySmsCode();
            }
          })
        }
      })
    },
    querySmsCode() {
      this.countdown = 60;
      if (this.timer) {
        return;
      }
      this.timer = setInterval(() => {
        if (this.countdown > 0) {
          this.smsShow = false;
          this.countdown--;
        } else {
          this.smsShow = true;
          clearInterval(this.timer);
          this.timer = null;
        }
      }, 1000);
      smsCode(this.smsLoginForm).then((res) => {
        //consolethis.smsL .log(res)smsAuthCode;
        Message({ message: '验证码已发送，请注意查收', type: 'success', duration: 2 * 1000 })
        if(res.data.imitate){
          this.smsLoginForm.smsCode = res.data.smsAuthCode;
        }
      }).catch(err => {
        //this.smsLoginForm.code2 = undefined
        this.smsShow = true;
        clearInterval(this.timer);
        this.timer = null;
      });
    },

    handleAccountLogin() {
      this.$refs.smsLoginForm.clearValidate();
      this.accountLogin = true;
      this.loginForm.grant_type = serviceConfig.captcha;
      this.getCode();
      this.getCookie();
    },
    switchSmsLogin() {
      this.$refs.loginForm.clearValidate();
      this.accountLogin = false;
      this.smsLoginForm.grant_type = serviceConfig.mobileSms;
      this.getCode();
      this.getCookie();
    },
    getCode() {
      this.smsLoginForm.code2 = ''
      //this.loginForm.code = ''
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = res.data.img;
          this.loginForm.uuid = res.data.uuid;
          this.smsLoginForm.uuid = res.data.uuid;
        }
      });
    },
    getCookie() {
      if (this.smsLoginForm.grant_type === serviceConfig.mobileSms) {
        const mobilePhone = Cookies.get("mobilePhone");
        const rememberMe = Cookies.get('rememberMe')
        this.smsLoginForm.mobilePhone = mobilePhone === undefined ? this.smsLoginForm.mobilePhone : mobilePhone,
          this.smsLoginForm.rememberMe = undefined ? false : Boolean(rememberMe);
      } else {
        const username = Cookies.get("username");
        const password = Cookies.get("password");
        const rememberMe = Cookies.get('rememberMe')
        this.loginForm = {
          username: username === undefined ? this.loginForm.username : username,
          password: password === undefined ? this.loginForm.password : decrypt(password),
          rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
        };
      }
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
    },
    handleSmsLogin() {
      this.$refs.smsLoginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.smsLoginForm.rememberMe) {
            Cookies.set("mobilePhone", this.smsLoginForm.mobilePhone, { expires: 30 });
            Cookies.set('rememberMe', this.smsLoginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("mobilePhone");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.smsLoginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(() => { });
          }).catch((err) => {
            //console.log('出现异常:', err)
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
  //background-color: #d7e6f4;
  background: linear-gradient(0deg, #3a485a 0%, #607089 100%);
  width: 50%;
  float: left;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  box-sizing: border-box;

  .login-form-left-c1 {
    width: 100%;
    color: #fff;

    .platform-name {
      width: 100%;
      padding: 10px;
      font-size: 28px;
      text-align: center;

      span {
        display: block;
      }

      span:last-child {
        color: rgba(255, 255, 255, 0.8);
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
  padding: 0 20px 0;

  .title {
    color: #666;
    text-align: center;
    font-size: 24px;
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
  width: 37%;
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

.othersBtn {
  color: #999;

}

.othersBtn>a {
  color: #999;
  font-size: 12px;
  margin: 0 5px;

  &:hover {
    color: #1890ff;
  }
}

.smsLogin {
  color: #005980;
  float: right;
  font-size: 14px;

  & a:hover {
    color: #009fda
  }
}

.sms-code {
  width: 35%;
  height: 38px;
  float: right;
  color: #1890ff;
  text-align: center;
  border: 1px solid #1890ff;
  border-radius: 4px;

  &:hover {
    cursor: pointer;
    color: #009fda;
    border: 1px solid #009fda;
  }

  .footer_container {
    height: 20px;
    line-height: 20px;
    font-size: 12px;
    letter-spacing: 1px;
    text-align: center;
    color: #FFF;
  }
}
</style>
