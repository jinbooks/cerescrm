<template>
  <div class="register">
    <el-form ref="licenseRef" :model="licenseForm" :rules="registerRules" class="register-form">
      <h3 class="title">产品授权查询</h3>
      <el-form-item prop="license">
        <el-input
            v-model="licenseForm.license"
            type="text"
            size="large"
            auto-complete="off"
            placeholder="授权码"
        >
          <template #prefix>
            <svg-icon icon-class="user" class="el-input__icon input-icon"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
            :loading="loading"
            size="large"
            type="primary"
            style="width:100%;"
            @click.prevent="handleRegister"
        >
          <span v-if="!loading">查询</span>
          <span v-else>查 询 中...</span>
        </el-button>
      </el-form-item>
      <el-form-item style="width:100%;" v-show="licenseForm.show ==true && licenseForm.loaded">
        <el-alert title="" type="success" :closable="false" show-icon >
          <font color='green'>授权码 {{licenseForm.license}} </font><br>
          <font color='green'>授权客户 {{licenseForm.customerName}}</font><br>
          <font color='green'>产品名称 {{licenseForm.productName}} </font><br>
          <font color='green'>版本 {{licenseForm.productVersion}}</font><br>
          <font color='green'>授权日期 {{licenseForm.grantDate}}</font><br>
          </el-alert>
        
      </el-form-item>
       <el-form-item style="width:100%;" v-show="licenseForm.show !=true && licenseForm.loaded">
        <el-alert title="" type="error" :closable="false" show-icon >
          未查到授权码 [{{licenseForm.license}}] 的授权信息！<br>
        </el-alert>
        
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-register-footer">
      <span>Copyright © 2018-2024 ruoyi.vip All Rights Reserved.</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import {ElMessageBox} from 'element-plus';
import {useRouter} from 'vue-router';
import * as productGrantService  from '@/api/product-grant/product-grant'

const router: any = useRouter();
const {proxy} = getCurrentInstance()!;

interface LicenseForm {
  show: boolean,
  loaded:boolean,
  license: string,
  customerName?: string,
  productName?: string,
  productVersion?: string,
  grantDate?: string,
}

const licenseForm: any = reactive<LicenseForm>({
  show:false,
  loaded:false,
  license: ''
});



const registerRules: any = {
  license: [
    {required: true, trigger: 'blur', message: '请输入您的授权码'},
    {min: 2, max: 20, message: '授权码长度必须介于 2 和 200 之间', trigger: 'blur'}
  ]
};

const codeUrl: any = ref('');
const loading: any = ref(false);
const captchaEnabled: any = ref(true);

const handleRegister: any = () => {
  const formRef: any = proxy.$refs.licenseRef as any;
  formRef.validate((valid: boolean) => {
    if (valid) {
      loading.value = true;
      productGrantService.license(licenseForm.license).then((res: any) => {
        loading.value = false;
        console.log(res.data)
        if(res.data){
          licenseForm.show =true
          licenseForm.customerName = res.data.customerName
          licenseForm.productName =res.data.productName
          licenseForm.productVersion =res.data.productVersion
          licenseForm.grantDate =res.data.grantDate
        }else{
          licenseForm.show =false
        }
        licenseForm.loaded =true;
      }).catch(() => {
        loading.value = false;
      });
    }
  });
};


</script>

<style lang='scss' scoped>
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/backstage2.jpg");
  background-size: cover;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.register-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;

  .el-input {
    height: 40px;

    input {
      height: 40px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}

.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.register-code {
  width: 33%;
  height: 40px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-register-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.register-code-img {
  height: 40px;
  padding-left: 12px;
}
</style>
