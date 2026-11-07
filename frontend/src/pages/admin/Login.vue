<template>
  <div class="login-container">
    <!-- 返回前台按钮 -->
    <div class="back-to-front">
      <el-button type="info" plain size="small" @click="goToFront">
        <el-icon><House /></el-icon>
        <span>返回前台</span>
      </el-button>
    </div>

    <div class="login-box">
      <h2>运动用品后台管理系统</h2>
      <el-form ref="ruleFormRef" :model="form" :rules="rules" status-icon label-width="0">
        <el-form-item prop="name">
          <el-input
            v-model="form.name"
            placeholder="用户名"
            size="large"
            prefix-icon="User"
            clearable
          />
        </el-form-item>

        <el-form-item prop="pwd">
          <el-input
            v-model="form.pwd"
            type="password"
            placeholder="密码"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-checkbox v-model="remember">记住用户名</el-checkbox>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import adminRequest from '@/utils/adminRequest'
import { ElMessage } from 'element-plus'
import { House } from '@element-plus/icons-vue'

const router = useRouter()

// 表单数据
const form = reactive({
  name: '',
  pwd: ''
})

// 记住用户名
const remember = ref(false)

// 登录加载状态
const loading = ref(false)

// 表单引用
const ruleFormRef = ref()

// 表单校验规则
const rules = reactive({
  name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }]
})

// 返回前台首页
const goToFront = () => {
  window.open('/', '_blank')
}

// 登录方法
// src/pages/admin/Login.vue
const handleLogin = async () => {
  if (!ruleFormRef.value) return

  await ruleFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await adminRequest.post('/login', {
      name: form.name,
      pwd: form.pwd
        })

        if (res.code === 200) {
          const managerInfo = res.data.managerInfo || res.data
          sessionStorage.setItem('adminUser', JSON.stringify(managerInfo))
          localStorage.setItem('token', managerInfo.id || 'temp_token') // 可选：保留 token
          localStorage.setItem('admin-token', 'admin')

          ElMessage.success('登录成功')
          router.push('/admin/dashboard')
        } else {
          ElMessage.error(res.msg || '登录失败')
        }
      } catch (err) {
        ElMessage.error('网络错误')
      } finally {
        loading.value = false
      }
    }
  })
}

// 页面加载时恢复记住的用户名
onMounted(() => {
  const rememberedName = localStorage.getItem('rememberedName')
  if (rememberedName) {
    form.name = rememberedName
    remember.value = true
  }
})
</script>

<style scoped>

.login-container {
  height: 100vh;

  /* 后台渐变 */
  background: linear-gradient(
    135deg,
    #1e293b 0%,
    #0f172a 40%,
    #1e3a8a 100%
  );

  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
}

/* 提高可读性 overlay */
.login-container::before {
  content: '';
  position: absolute;
  inset: 0;
  background: rgba(255,255,255,0.08);
}

/* 返回前台按钮 */
.back-to-front {
  position: absolute;
  top: 28px;
  right: 38px;
  z-index: 5;
}

.back-to-front .el-button {
  border-radius: 18px;
  padding: 9px 18px;
  font-weight: 500;
}

/* 登录卡片 */
.login-box {

  width: 440px;
  padding: 46px 42px;

  background: #ffffff;

  border-radius: 16px;

  border: 1px solid #e5e7eb;

  box-shadow:
    0 25px 60px rgba(0,0,0,0.35),
    0 3px 8px rgba(0,0,0,0.12);

  text-align: center;
  position: relative;
  z-index: 2;
}

/* 标题 */
.login-box h2 {

  margin-bottom: 36px;

  color: #111827;

  font-size: 26px;

  font-weight: 700;

  letter-spacing: 0.5px;
}

/* 输入框 */
:deep(.el-input__wrapper) {

  border-radius: 10px;

  background: #f5f7fa;

  border: 1px solid #e5e7eb;

  transition: all 0.25s ease;
}

:deep(.el-input__wrapper:hover) {

  border-color: #94a3b8;
}

:deep(.el-input__wrapper.is-focus) {

  border-color: #2563eb;

  box-shadow: 0 0 0 2px rgba(37,99,235,0.18);
}

/* checkbox */
:deep(.el-checkbox) {

  font-size: 13px;

  color: #555;
}

/* 登录按钮 */

.login-btn {

  width: 100%;

  height: 48px;

  font-size: 16px;

  border-radius: 10px;

  font-weight: 600;

  letter-spacing: 1px;

  background: linear-gradient(
    135deg,
    #2563eb,
    #1e40af
  );

  border: none;

  transition: all 0.25s ease;
}

.login-btn:hover {

  transform: translateY(-1px);

  box-shadow: 0 10px 22px rgba(37,99,235,0.35);
}

/* 表单间距 */
.el-form-item {

  margin-bottom: 22px;
}

/* 登录框底部信任提示 */

.login-box::after {

  content: "管理系统 · 安全认证 · 内部访问";

  display: block;

  margin-top: 18px;

  font-size: 12px;

  color: #9ca3af;
}

</style>