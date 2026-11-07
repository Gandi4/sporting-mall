<template>
  <el-container class="layout">
    <!-- 顶部 -->
    <el-header class="layout-header">
      <Header />
    </el-header>

    <!-- 主体 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px" class="layout-aside">
        <Aside />
      </el-aside>

      <!-- 内容区 -->
      <el-main class="layout-main">
        <el-card class="content-card">
          <router-view />
        </el-card>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { onMounted } from 'vue'
import Header from '@/components/Header.vue'
import Aside from '@/components/Aside.vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// ✅ 关键修改：在 onMounted 中检查 sessionStorage 的 adminUser
onMounted(() => {
  const admin = JSON.parse(sessionStorage.getItem('adminUser') || '{}')
  if (!admin.id) {
    router.push('/admin/login')
  }
})
</script>

<style scoped>
.layout {
  height: 100vh;
}

.layout-header {
  padding: 0;
}

.layout-aside {
  background-color: #001529;
}

.layout-main {
  background: #f5f7fa;
  padding: 16px;
}

.content-card {
  min-height: calc(100vh - 92px);
  border-radius: 6px;
}
</style>