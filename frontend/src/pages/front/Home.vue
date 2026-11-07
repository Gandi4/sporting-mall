<template>
  <div class="home-pages">
   <!-- 轮播图 -->
<section class="hero-section">
  <el-carousel height="500px" indicator-position="outside" v-if="carouselItems.length > 0">
    <el-carousel-item v-for="item in carouselItems" :key="item.id">
      <div 
        class="carousel-bg" 
        :style="{ backgroundImage: `url('${formatImg(item.imageUrl)}')` }"
      >
        <div class="hero-overlay">
          <div class="overlay-text">
            <h1>{{ item.title }}</h1>
            <p>为运动而生 · 专业装备精选</p>
            <el-button type="primary" class="hero-btn" @click="$router.push('/')">立即探索</el-button>
          </div>
        </div>
      </div>
    </el-carousel-item>
  </el-carousel>
  <div v-else class="hero-placeholder">精彩内容加载中...</div>
</section>

    <!-- 欢迎语区域 -->
    <section class="welcome-section">
      <div class="welcome-container">
        <h2 v-if="user.level === 1">欢迎你，新手玩家！</h2>
        <h2 v-else-if="user.level >= 2 && user.level <= 7">欢迎你，修炼者 {{ user.name }}！</h2>
        <h2 v-else-if="user.level >= 8 && user.level <= 14">欢迎你，尊贵的神境之主 {{ user.name }}！</h2>
        <h2 v-else-if="user.level === 15">欢迎你，天庭主宰 {{ user.name }}！</h2>
        <h2 v-else>欢迎来到 SPORTING 购物！</h2>
      </div>
    </section>

    <!-- 商品列表 -->
    <section class="product-section">
      <ProductGrid :products="products" :title="title" />
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import frontRequest from '@/utils/frontRequest'
import ProductGrid from '@/components/ProductGrid.vue'

const route = useRoute()
const products = ref([])
const carouselItems = ref([])
const title = ref('精选推荐')

// 初始化 user 变量，从 sessionStorage 获取用户信息
const user = ref(JSON.parse(sessionStorage.getItem('user') || '{}'))

const loadBanners = async () => {
  try {
    const res = await frontRequest.get('/banner/list')
    if (res.code === 200) carouselItems.value = res.data || []
  } catch (err) { console.error('Banner Load Failed') }
}

/**
 * 加载商品列表
 */
/**
 * 加载商品列表
 */
const loadProducts = async () => {
  try {
    const params = {}
    if (route.query.categoryId) params.categoryId = Number(route.query.categoryId)
    const res = await frontRequest.get('/product/list', { params })

    if (res.code === 200) {
      // 判断 res.data 是否本身就是数组
      if (Array.isArray(res.data)) {
        products.value = res.data
      } else {
        // 兼容分页情况
        products.value = res.data.list || res.data.records || []
      }
    }

    title.value = route.query.categoryName || '精选推荐'
  } catch (err) {
    console.error('商品加载失败', err)
  }
}

const formatImg = (url) => {
  if (!url) return ''
  
  if (url.startsWith('http')) {
    return url
  }

  const path = url.startsWith('/') ? url : '/' + url
  return 'http://localhost:80' + path
}

watch(() => route.query, () => loadProducts(), { deep: true, immediate: true })
onMounted(() => { loadBanners(); loadProducts() })
</script>

<style scoped>
.home-pages { min-height: 100vh; background: #fff; }

.hero-section { background: #f5f5f5; min-height: 500px; }
.carousel-bg { width: 100%; height: 100%; background-size: cover; background-position: center; position: relative; }
.hero-overlay { position: absolute; inset: 0; display: flex; align-items: center; justify-content: center; background: rgba(0,0,0,0.2); }
.overlay-text { text-align: center; color: #fff; text-shadow: 0 4px 10px rgba(0,0,0,0.3); }
.overlay-text h1 { font-size: 56px; font-weight: 900; margin-bottom: 10px; }
.hero-btn {
    background: linear-gradient(135deg, #ff8c00, #ff4500);
    border: none;
    padding: 12px 30px;
    color: white;
    font-weight: bold;
    border-radius: 25px;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 4px 15px rgba(255, 69, 0, 0.3);
}

/* 鼠标悬停效果 */
.hero-btn:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 20px rgba(255, 69, 0, 0.5);
    background: linear-gradient(135deg, #ff4500, #ff8c00);
}


.welcome-section {
    max-width: 1240px;
    margin: 40px auto;
    padding: 0 20px;
    text-align: center;
}
.welcome-container {
    padding: 40px;
    background: linear-gradient(135deg, #1e293b 0%, #3b82f6 100%);
    border-radius: 16px;
    color: white;
    box-shadow: 0 10px 25px rgba(0,0,0,0.1);
}
.welcome-container h2 {
    font-size: 28px;
    font-weight: 700;
    margin: 0;
}

.product-section { max-width: 1240px; margin: 40px auto; padding: 0 20px; }
</style>