<template>
  <div class="profile-page">
    <div class="container">
      <!-- 1. 顶部 Hero 卡片 (与个人中心 Profile 1:1 保持一致) -->
      <div class="hero-card" :class="{ 'is-god': user.isVip === 1 }">
        <div class="hero-left">
          <el-avatar :size="90" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" class="avatar-shadow" />
          <div class="user-text">
            <h2>{{ user.username }} 
              <el-tag size="small" :type="user.isVip===1?'warning':'primary'" effect="dark">{{ user.currentLevel || '灵胎' }}</el-tag>
            </h2>
            <div class="exp-box">
              <span>修为: {{ user.experience || 0 }} Exp</span>
              <el-progress :percentage="expPercentage" color="#ff6b00" :show-text="false" style="width:200px"/>
            </div>
          </div>
        </div>
        <div class="hero-right" v-if="user.isVip === 1">
          <div class="vip-info">天宫圣境 · 尊享特权中</div>
        </div>
      </div>

      <div class="profile-body">
        <!-- 2. 侧边导航  -->
        <el-menu :default-active="'order'" class="side-nav" @select="handleMenuSelect">
          <el-menu-item index="info">个人信息</el-menu-item>
          <el-menu-item index="address">地址管理</el-menu-item>
          <el-menu-item index="order">我的订单</el-menu-item>
          <el-menu-item index="fav">我的收藏</el-menu-item>
          <el-menu-item index="vip">会员中心</el-menu-item>
        </el-menu>

        <!-- 3. 内容区 -->
        <div class="content-area">
          <div class="section-header">
            <h3 class="view-title">订单中心</h3>
            
            <!-- 搜索工具条 -->
            <div class="search-bar-inline">
              <el-input 
                v-model="searchForm.orderNo" 
                placeholder="订单号" 
                size="default" 
                clearable
                style="width: 180px" 
              />
              <el-input 
                v-model="searchForm.productName" 
                placeholder="商品名" 
                size="default" 
                clearable
                style="width: 180px; margin: 0 12px" 
              />
              <el-button type="primary" @click="loadOrders">查询</el-button>
            </div>
          </div>

          <!-- 订单表格 -->
          <el-table :data="orderList" border stripe v-loading="loading" class="custom-table">
            <el-table-column prop="orderNo" label="订单编号" min-width="160" />
            <el-table-column label="支付金额" width="130">
              <template #default="{row}">
                <span class="price-text">￥{{ row.payAmount }}</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="110" align="center">
              <template #default="{row}">
                <el-tag :type="row.status === 'paid' ? 'success' : 'warning'" size="small" effect="light">
                  {{ row.status === 'paid' ? '已支付' : '待处理' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="下单时间" width="170">
              <template #default="{row}">{{ formatDate(row.createdAt) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="100" align="center">
              <template #default="{row}">
                <el-button type="primary" link @click="$router.push('/order/detail/' + row.id)">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <el-empty v-if="orderList.length === 0" description="暂无相关订单记录" :image-size="120" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import frontRequest from '@/utils/frontRequest'
import { ElMessage } from 'element-plus'

const router = useRouter()
const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const orderList = ref([])
const loading = ref(false)
const searchForm = ref({ orderNo: '', productName: '' })

// 修为百分比计算
const expPercentage = computed(() => {
  const exp = user.value.experience || 0
  return Math.min((exp % 5000) / 50, 100)
})


const handleMenuSelect = (index) => {
  if (index !== 'order') {
    router.push({ path: '/profile', query: { tab: index } })
  }
}

/**
 * 加载订单列表
 */
const loadOrders = async () => {
  if (!user.value.id) return
  loading.value = true
  try {
    const res = await frontRequest.get('/order/list', {
      params: { 
        userId: user.value.id,
        orderNo: searchForm.value.orderNo,
        productName: searchForm.value.productName
      }
    })
    if (res.code === 200) {
      orderList.value = res.data || []
    }
  } catch (e) {
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

const formatDate = (d) => {
  if (!d) return '-'
  return new Date(d).toLocaleString()
}

onMounted(() => {
  if (!user.value.id) {
    router.push('/login')
  } else {
    loadOrders()
  }
})
</script>

<style scoped>

.profile-page { background: #f8f9fa; min-height: 100vh; padding: 40px 0; }
.container { max-width: 1200px; margin: 0 auto; padding: 0 20px; }

/* Hero 卡片样式 */
.hero-card { 
  background: #fff; 
  padding: 35px; 
  border-radius: 16px; 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 25px; 
  border: 1px solid #eee; 
  box-shadow: 0 4px 15px rgba(0,0,0,0.05); 
}
.is-god { background: linear-gradient(135deg, #1a1a1a, #333); color: #ffd700; border-color: #ffd700; }
.hero-left { display: flex; align-items: center; gap: 20px; }
.user-text h2 { font-size: 24px; margin: 0; font-weight: bold; }
.exp-box { margin-top: 10px; display: flex; align-items: center; gap: 15px; font-size: 13px; color: #666; }
.is-god .exp-box { color: #ffd700; }

/* 布局结构 */
.profile-body { display: flex; gap: 25px; }
.side-nav { width: 240px; border-radius: 12px; border: none; box-shadow: 0 4px 15px rgba(0,0,0,0.05); height: fit-content; }
.content-area { flex: 1; background: #fff; border-radius: 12px; padding: 35px; min-height: 550px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }

/* 头部样式 */
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
.view-title { border-left: 5px solid #ff6b00; padding-left: 15px; font-size: 18px; color: #333; font-weight: 600; }

/* 搜索栏 */
.search-bar-inline { display: flex; align-items: center; }

/* 价格文字 */
.price-text { color: #f56c6c; font-weight: 700; font-size: 15px; }

/* 调整表格圆角 */
.custom-table { border-radius: 8px; overflow: hidden; }

/* 会员专享信息文字 */
.vip-info { font-size: 14px; font-weight: bold; letter-spacing: 1px; border: 1px solid #ffd700; padding: 4px 12px; border-radius: 20px; }
</style>