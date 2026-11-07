<template>
  <div class="login-container">
    <el-card class="login-card">
      <!-- 标题 -->
      <div class="title">SPORTING</div>
      <p class="subtitle">欢迎来到专业运动装备商城</p>

      <!-- 登录/注册切换 -->
      <div class="tab-switch">
        <span
          class="tab-item"
          :class="{ active: currentTab === 'login' }"
          @click="currentTab = 'login'"
        >
          登录
        </span>
        <span
          class="tab-item"
          :class="{ active: currentTab === 'register' }"
          @click="currentTab = 'register'"
        >
          注册
        </span>
      </div>

      <!-- 登录表单 -->
      <el-form
        v-if="currentTab === 'login'"
        :model="loginForm"
        :rules="rules"
        ref="loginFormRef"
        label-width="0"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            style="width: 100%"
            @click="handleLogin"
          >
            立即登录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 注册表单 -->
      <el-form
        v-else
        :model="registerForm"
        :rules="registerRules"
        ref="registerFormRef"
        label-width="0"
      >
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            size="large"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>
        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="请输入邮箱"
            size="large"
            :prefix-icon="'Message'"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            style="width: 100%"
            @click="handleRegister"
          >
            立即注册
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 忘记密码 -->
      <div class="forgot-password" v-if="currentTab === 'login'">
        <el-link type="primary" @click="$router.push('/reset-password')">忘记密码？</el-link>
      </div>

      <!-- 管理员入口 -->
      <div class="admin-login">
        <p class="admin-text">管理员专用</p>
        <el-button
          type="info"
          plain
          size="small"
          @click="$router.push('/admin/login')"
        >
          进入后台管理
        </el-button>
      </div>

      <!-- 安全提示 -->
      <div class="security-tip">
        安全登录 · 隐私保护 · SSL 加密
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import frontRequest from '@/utils/frontRequest'
import { useRouter } from 'vue-router'

const router = useRouter()

// 当前标签页：login / register
const currentTab = ref('login')

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 注册表单数据
const registerForm = reactive({
  username: '',
  email: '', // 
  password: '',
  confirmPassword: ''
})

// 表单引用
const loginFormRef = ref(null)
const registerFormRef = ref(null)

// 登录校验规则
const rules = reactive({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20位', trigger: 'blur' }
  ]
})

// 注册校验规则
const registerRules = reactive({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value) => {
        if (value !== registerForm.password) {
          return Promise.reject('两次输入的密码不一致')
        }
        return Promise.resolve()
      },
      trigger: 'blur'
    }
  ]
})

/**
 * 用户登录
 */
const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await frontRequest.post('/user/login', loginForm)
        if (res.code === 200) {
          ElMessage.success('登录成功')

          // 存入 sessionStorage
          sessionStorage.setItem('user', JSON.stringify(res.data))

          // 推荐使用 router.push 刷新路由（可选）
          router.push('/')
        } else {
          ElMessage.error(res.msg || '登录失败')
        }
      } catch (e) {
        ElMessage.error('服务器连接异常')
      }
    }
  })
}

/**
 * 用户注册
 */
const handleRegister = async () => {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await frontRequest.post('/user/register', registerForm)
        if (res.code === 200) {
          ElMessage.success('注册成功，请登录')
          currentTab.value = 'login'
          // 清空表单
          registerForm.username = ''
          registerForm.email = ''
          registerForm.password = ''
          registerForm.confirmPassword = ''
        } else {
          ElMessage.error(res.msg || '注册失败')
        }
      } catch (e) {
        ElMessage.error('服务器连接异常，请检查后端是否启动')
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #3b82f6 0%, #1e40af 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.login-container::before {
  content: "";
  position: absolute;
  inset: 0;
  background: rgba(255,255,255,0.1);
  backdrop-filter: blur(2px);
}

.login-card {
  width: 400px;
  padding: 40px 30px;
  border-radius: 16px;
  background: #ffffff;
  box-shadow:
    0 20px 45px rgba(0,0,0,0.15),
    0 2px 4px rgba(0,0,0,0.06);
  border: 1px solid #f1f1f1;
  position: relative;
  z-index: 1;
}

.title {
  font-size: 28px;
  font-weight: 700;
  text-align: center;
  margin-bottom: 12px;
  color: #111;
}

.subtitle {
  text-align: center;
  color: #555;
  margin-bottom: 24px;
  font-size: 14px;
}

.tab-switch {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
  gap: 20px;
}

.tab-item {
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: all 0.2s ease;
}

.tab-item.active {
  color: #3b82f6;
  border-bottom-color: #3b82f6;
}

.forgot-password {
  text-align: center;
  margin-top: 16px;
}

.admin-login {
  margin-top: 24px;
  text-align: center;
}

.admin-text {
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
}

.security-tip {
  margin-top: 20px;
  text-align: center;
  font-size: 12px;
  color: #999;
}

/* 输入框样式 */
:deep(.el-input__wrapper) {
  border-radius: 10px;
  background: #f5f7fa;
  border: 1px solid #e5e7eb;
  transition: all 0.25s ease;
}

:deep(.el-input__wrapper:hover) {
  border-color: #c7d2fe;
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59,130,246,0.15);
}

.el-button--primary {
  border-radius: 10px;
  font-weight: 600;
  letter-spacing: 1px;
  background: linear-gradient(135deg, #3b82f6, #1e40af);
  border: none;
  transition: all 0.25s ease;
}

.el-button--primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 18px rgba(59,130,246,0.35);
}

.el-button--info {
  border-radius: 8px;
  font-size: 12px;
  padding: 6px 12px;
}
</style>