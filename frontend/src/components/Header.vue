<template>
  <div class="header">
    <div class="left">
      <span class="title">运动用品后台管理系统</span>
    </div>

    <div class="right">
      <!-- 返回前台按钮 -->
      <el-button
        type="success"
        size="small"
        plain
        @click="goToFront"
        style="margin-right: 16px;"
      >
        <el-icon><House /></el-icon>
        <span>返回前台</span>
      </el-button>

      <span class="username">欢迎，{{ managerName || '管理员' }}</span>
      <el-button
        type="primary"
        size="small"
        plain
        @click="logout"
      >
        退出登录
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { House } from '@element-plus/icons-vue'

const router = useRouter()

// ✅ 关键修改：从 sessionStorage 获取管理员名字
const managerName = sessionStorage.getItem('managerName') || '管理员'

// 返回前台首页
const goToFront = () => {
  window.open('/', '_blank') // 新标签打开前台
}

// ✅ 关键修改：退出登录时只清除 adminUser，不全清 localStorage
const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '退出确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      // ✅ 只移除 adminUser，保留其他可能的本地数据
      sessionStorage.removeItem('adminUser')
      ElMessage.success('已成功退出登录')
      router.replace('/admin/login')
    })
    .catch(() => {
      // 用户点击了取消，不做任何操作
    })
}
</script>

<style scoped>
.header {
  height: 60px;
  background: #fff;
  padding: 0 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  position: sticky;
  top: 0;
}

.left .title {
  font-size: 20px;
  font-weight: 700;
  color: #303133;
}

.right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.username {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

.el-button {
  border-radius: 6px;
  padding: 8px 16px;
}

/* 返回前台按钮图标和文字间距 */
.el-button .el-icon {
  margin-right: 6px;
}
</style>