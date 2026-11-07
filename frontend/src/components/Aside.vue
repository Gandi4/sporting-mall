<template>
  <el-menu
    class="el-menu-vertical"
    background-color="#001529"
    text-color="#fff"
    active-text-color="#409EFF"
    router
  >
    <!-- 主页 (仪表盘)-->
    <el-menu-item index="/admin/dashboard">
      <el-icon><House /></el-icon>
      <span>主页</span>
    </el-menu-item>

    <!-- 商品管理 -->
    <el-sub-menu index="product">
      <template #title>
        <el-icon><Goods /></el-icon>
        <span>商品管理</span>
      </template>
      <el-menu-item index="/admin/category">商品分类</el-menu-item>
      <el-menu-item index="/admin/product">商品列表</el-menu-item>
    </el-sub-menu>

    <!-- 订单管理 -->
    <el-sub-menu index="order">
      <template #title>
        <el-icon><ShoppingCart /></el-icon>
        <span>订单管理</span>
      </template>
      <el-menu-item index="/admin/order">订单列表</el-menu-item>
      <el-menu-item index="/admin/return-request">退货售后</el-menu-item>
    </el-sub-menu>

    <!-- 用户管理 -->
    <el-sub-menu index="user">
      <template #title>
        <el-icon><User /></el-icon>
        <span>用户管理</span>
      </template>
      <el-menu-item index="/admin/user">用户列表</el-menu-item>
      <el-menu-item index="/admin/review">评价管理</el-menu-item>
    </el-sub-menu>

    <!-- 营销管理 -->
    <el-sub-menu index="marketing">
      <template #title>
        <el-icon><Promotion /></el-icon>
        <span>营销管理</span>
      </template>
      <el-menu-item index="/admin/banner">轮播图管理</el-menu-item>
      <el-menu-item index="/admin/announcement">公告管理</el-menu-item>
    </el-sub-menu>

    <!-- 系统管理 -->
    <el-sub-menu index="system">
      <template #title>
        <el-icon><Setting /></el-icon>
        <span>系统管理</span>
      </template>
      <el-menu-item index="/admin/manager">管理员管理</el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>


<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  House,
  Goods,
  ShoppingCart,
  User,
  Promotion,
  Setting
} from '@element-plus/icons-vue'

const route = useRoute()

// 当前高亮菜单
const activeMenu = computed(() => route.path)

// 默认展开的子菜单
const openMenus = computed(() => {
  const path = route.path
  if (path.startsWith('/product') || path.startsWith('/category')) return ['/product']
  if (path.startsWith('/order') || path.startsWith('/return')) return ['/order']
  if (path.startsWith('/user') || path.startsWith('/review')) return ['/user']
  if (path.startsWith('/banner') || path.startsWith('/announcement')) return ['/banner']
  if (path.startsWith('/manager')) return ['/manager']
  return []
})
</script>

<style scoped>
.el-menu-vertical {
  height: 100vh;
  border-right: none;
}
</style>
