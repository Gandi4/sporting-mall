<template>
  <div class="dashboard-container">
    <h1 class="title">欢迎进入运动用品后台管理系统</h1>
    <p class="subtitle">请选择常用功能快速进入</p>

    <div class="entry-grid">
      <!-- 商品管理 -->
      <div class="entry-card" @click="go('/admin/product')">
        <el-icon size="48" color="#409EFF"><Goods /></el-icon>
        <h3>商品管理</h3>
        <p>商品新增 / 编辑 / 上下架</p>
      </div>

      <!-- 订单管理 -->
      <div class="entry-card" @click="go('/admin/order')">
        <el-icon size="48" color="#67C23A"><Document /></el-icon>
        <h3>订单管理</h3>
        <p>订单查看 / 发货 / 售后</p>
      </div>

      <!-- 用户管理 -->
      <div class="entry-card" @click="go('/admin/user')">
        <el-icon size="48" color="#E6A23C"><User /></el-icon>
        <h3>用户管理</h3>
        <p>用户信息 / 状态管理</p>
      </div>

      <!-- 分类管理 -->
      <div class="entry-card" @click="go('/admin/category')">
        <el-icon size="48" color="#F56C6C"><Menu /></el-icon>
        <h3>分类管理</h3>
        <p>商品分类新增与编辑</p>
      </div>

      <!-- 轮播图管理 -->
      <div class="entry-card" @click="go('/admin/banner')">
        <el-icon size="48" color="#909399"><Picture /></el-icon>
        <h3>轮播图管理</h3>
        <p>首页轮播图上传与排序</p>
      </div>

      <!-- 公告管理 -->
      <div class="entry-card" @click="go('/admin/announcement')">
        <el-icon size="48" color="#67C23A"><Notification /></el-icon>
        <h3>公告管理</h3>
        <p>系统公告发布与编辑</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Goods, Document, User, Menu, Picture, Notification } from '@element-plus/icons-vue'

const router = useRouter()

const go = (path) => {
  router.push(path)
}
</script>

<style scoped>
.dashboard-container {
  min-height: calc(100vh - 60px);
  padding: 40px 20px;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 14px;
  color: #909399;
  margin-bottom: 40px;
}

/* 网格布局：每行3个，居中 */
.entry-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  max-width: 1200px;
  width: 100%;
}

/* 卡片样式优化 */
.entry-card {
  height: 180px;
  padding: 30px 20px;
  background: #ffffff;
  border-radius: 12px;
  border: 1px solid #ebeef5;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  cursor: pointer;
  text-align: center;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.entry-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
  border-color: #409eff;
}

.entry-card .el-icon {
  margin-bottom: 20px;
}

.entry-card h3 {
  margin: 0 0 8px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.entry-card p {
  margin: 0;
  font-size: 14px;
  color: #909399;
  line-height: 1.5;
}

/* 响应式：小屏幕每行2个 */
@media (max-width: 1200px) {
  .entry-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .entry-grid {
    grid-template-columns: 1fr;
  }
  .dashboard-container {
    padding: 30px 15px;
  }
}
</style>