<template>
  <div class="layout">
    <!-- 1. 顶部导航 -->
    <FrontHeader />

    <!-- 2. 主体内容区 -->
    <main class="main-content">
      <router-view />
    </main>

    <!-- 3. 全局页脚 -->
    <footer class="footer">
      <div class="footer-container">
        <div class="footer-links">
          <div class="link-group">
            <h4>用户指南</h4>
            <p @click="$router.push('/login')">注册登录</p>
            <p @click="$router.push('/')">购物流程</p>
            <p @click="$router.push('/order')">订单查询</p>
          </div>
          <div class="link-group">
            <h4>商家服务</h4>
            <p @click="$router.push('/admin/login')">后台管理</p>
            <p @click="handleInfo">商家入驻</p>
            <p @click="handleInfo">物流合作</p>
          </div>
          <div class="link-group">
            <h4>联系我</h4>
            <p>邮箱：contact@example.com</p>
            <p>电话：400-000-0000</p>
            <p>地址：校园实训项目</p>
          </div>
        </div>
        <div class="footer-bottom">
          <p>© 2026 SPORTING 体育电商系统 | 毕业设计（已脱敏）</p>
        </div>
      </div>
    </footer>

    <!-- 4. AI 智能客服挂载 -->
    <AIChat />
  </div>
</template>

<script setup>
import FrontHeader from '@/components/FrontHeader.vue'
import AIChat from '@/components/AIChat.vue'
import { ElMessage } from 'element-plus'

const handleInfo = () => {
  ElMessage.info('功能演示中，暂未对外开放')
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  background-color: #fcfcfc;
}

/* 页脚样式 */
.footer {
  background: #1a1a1a;
  color: #fff;
  padding: 60px 0 30px;
  margin-top: 50px;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-links {
  display: flex;
  justify-content: space-between;
  margin-bottom: 40px;
}

.link-group h4 {
  font-size: 16px;
  margin-bottom: 20px;
  color: #ff6b00;
  font-weight: bold;
}

.link-group p {
  color: #999;
  font-size: 14px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: 0.3s;
}

.link-group p:hover {
  color: #ff6b00;
  padding-left: 5px;
}

.footer-bottom {
  text-align: center;
  border-top: 1px solid #333;
  padding-top: 30px;
  color: #666;
  font-size: 13px;
}
</style>