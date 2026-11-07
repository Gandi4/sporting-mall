<template>
  <div class="product-detail-page">
    <div class="detail-container" v-if="product">
      <!-- 1. 商品上半部分：核心展示与购买 -->
      <el-row :gutter="60">
        <el-col :span="12">
          <!-- 仅保留主图展示区 -->
          <div class="main-image-box">
            <el-image 
              :src="formatImg(product.mainImage)" 
              fit="contain" 
              class="main-img" 
              :preview-src-list="[formatImg(product.mainImage)]"
            >
              <template #error><div class="img-err">暂无图片</div></template>
            </el-image>
          </div>
        </el-col>

        <el-col :span="12">
          <div class="purchase-panel">
            <h1 class="title">{{ product.name }}</h1>
            
            <div class="price-box">
              <span class="symbol">￥</span>
              <span class="val">{{ (product.price || 0).toFixed(2) }}</span>
            </div>

            <div class="buy-section">
              <div class="num-row">
                <span class="label">购买数量</span>
                <el-input-number v-model="quantity" :min="1" :max="product.stock" />
                <span class="stock-tip">库存 {{ product.stock }} 件</span>
              </div>
              
              <div class="btn-group">
                <el-button type="primary" size="large" class="orange-btn" @click="handleCart">加入购物车</el-button>
                
                <!-- 收藏按钮 -->
                <el-button 
                  size="large" 
                  :type="isFavorited ? 'warning' : ''" 
                  :plain="!isFavorited"
                  @click="handleToggleFavorite"
                >
                  <el-icon style="margin-right: 5px;">
                    <StarFilled v-if="isFavorited" />
                    <Star v-else />
                  </el-icon>
                  {{ isFavorited ? '已入收藏夹' : '收藏商品' }}
                </el-button>
              </div>
            </div>

            <div class="intro-box">
              <h3 class="sub-title">商品详述</h3>
              <p class="description">{{ product.description }}</p>
              <div class="service-tags">
                <span><el-icon><CircleCheck /></el-icon> 100%正品</span>
                <span><el-icon><CircleCheck /></el-icon> 瞬息发货</span>
                <span><el-icon><CircleCheck /></el-icon> 售后无忧</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 2. 下半部分：用户评价流 -->
      <div class="review-area">
        <div class="review-header-flex">
          <div class="review-title">用户评价 <span class="count">({{ reviewList.length }})</span></div>
        </div>
        
        <div class="review-list">
          <div 
            v-for="item in reviewList" 
            :key="item.id" 
            class="review-card" 
            :class="{ 'svip-card': item.isVip === 1 }"
          >
            <div class="review-header">
              <el-avatar :size="48" :src="item.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
              <div class="user-info">
                <div class="user-top">
                  <span class="name">{{ item.username }}</span>
                  <span class="realm-badge" :class="{ 'gold-badge': item.isVip === 1 }">
                    {{ item.currentLevel || '灵胎' }}
                  </span>
                  <el-rate v-model="item.rating" disabled size="small" />
                </div>
                <div class="date">{{ formatDate(item.createdAt) }}</div>
              </div>
            </div>
            <div class="review-content">{{ item.content }}</div>
          </div>
        </div>
        
        <el-empty v-if="reviewList.length === 0" description="暂无评论" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import frontRequest from '@/utils/frontRequest'
import { ElMessage } from 'element-plus'
import { CircleCheck, Star, StarFilled } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const product = ref(null)
const quantity = ref(1)
const reviewList = ref([])
const isFavorited = ref(false)

const user = ref(JSON.parse(sessionStorage.getItem('user') || '{}'))

// 图片路径修复逻辑
const formatImg = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return 'http://localhost:80' + url
}

// 1. 加载核心数据
const loadData = async () => {
  const id = route.params.id
  try {
    const res = await frontRequest.get(`/product/${id}`)
    if (res.code === 200) {
      product.value = res.data
      // 并发请求评价、收藏和用户信息，不干扰主图展示
      fetchReviews(id)
      checkFavoriteStatus(id)
      syncUserStatus() 
    }
  } catch (e) { 
    console.error('数据加载失败')
    ElMessage.error('无法连接服务器，请检查后端状态')
  }
}

// 2. 加载评价列表
const fetchReviews = async (productId) => {
  try {
    const rev = await frontRequest.get(`/review/list/${productId}`)
    if (rev.code === 200) reviewList.value = rev.data || []
  } catch(e) {}
}

// 3. 检查收藏状态
const checkFavoriteStatus = async (productId) => {
  if (!user.value.id) return
  try {
    const res = await frontRequest.get('/favorite/check', { params: { userId: user.value.id, productId } })
    if (res.code === 200) isFavorited.value = res.data
  } catch(e) {}
}

// 4. 同步境界信息
const syncUserStatus = async () => {
  if (!user.value.id) return
  try {
    const res = await frontRequest.get('/user/info', { params: { userId: user.value.id } })
    if (res.code === 200) {
      const newUser = res.data
      sessionStorage.setItem('user', JSON.stringify(newUser))
      user.value = newUser
      window.dispatchEvent(new Event('storage'))
    }
  } catch (e) {}
}

// 5. 加入购物车
const handleCart = async () => {
  const userStr = sessionStorage.getItem('user')
  if(!userStr) {
    ElMessage.warning('请登录后再加购')
    return router.push('/login')
  }
  const currentUser = JSON.parse(userStr)
  
  const res = await frontRequest.post('/cart/add', { 
    userId: currentUser.id, 
    productId: product.value.id, 
    quantity: quantity.value 
  })
  if (res.code === 200) ElMessage.success('成功加入购物车')
}

// 6. 切换收藏
const handleToggleFavorite = async () => {
  if (!user.value.id) return router.push('/login')
  if (isFavorited.value) {
    await frontRequest.delete('/favorite/remove', { params: { userId: user.value.id, productId: product.value.id } })
    isFavorited.value = false
    ElMessage.success('已移出收藏夹')
  } else {
    await frontRequest.post('/favorite/add', { userId: user.value.id, productId: product.value.id })
    isFavorited.value = true
    ElMessage.success('已加入收藏夹')
  }
}

const formatDate = d => d ? new Date(d).toLocaleDateString() : ''

onMounted(loadData)
</script>

<style scoped>
.product-detail-page { padding: 60px 0; background: #fff; min-height: 100vh; }
.detail-container { max-width: 1200px; margin: 0 auto; padding: 0 20px; }

/* 主图样式：固定高度，保持整洁 */
.main-image-box { width: 100%; height: 500px; background: #fdfdfd; border-radius: 16px; display: flex; align-items: center; justify-content: center; border: 1px solid #f0f0f0; overflow: hidden; }
.main-img { width: 100%; height: 100%; }

/* 购买面板 */
.purchase-panel { padding-left: 20px; }
.title { font-size: 32px; font-weight: 800; color: #1a1a1a; margin-bottom: 20px; }
.price-box { background: #fff8f5; padding: 25px; border-radius: 12px; margin: 30px 0; border: 1px solid #ffe8df; }
.price-box .val { color: #f56c6c; font-size: 44px; font-weight: 900; }

.num-row { margin-bottom: 30px; display: flex; align-items: center; gap: 20px; }
.stock-tip { color: #999; font-size: 13px; }

.btn-group { display: flex; gap: 15px; }
.orange-btn { background: #ff6b00 !important; border: none !important; width: 220px; height: 52px; font-size: 18px; font-weight: bold; transition: 0.3s; }
.orange-btn:hover { background: #ff8533 !important; transform: translateY(-2px); }

.sub-title { font-size: 18px; font-weight: bold; margin: 40px 0 15px; color: #333; }
.description { color: #666; line-height: 1.8; margin-bottom: 25px; }
.service-tags { display: flex; gap: 20px; color: #ff6b00; font-size: 13px; font-weight: bold; }
.service-tags span { display: flex; align-items: center; gap: 5px; }

/* 评价区 */
.review-area { margin-top: 100px; border-top: 1px solid #eee; padding-top: 50px; }
.review-title { font-size: 26px; font-weight: 900; color: #1a1a1a; }
.review-title .count { color: #ff6b00; margin-left: 10px; }
.review-card { padding: 30px; border: 1px solid #f5f5f5; border-radius: 16px; margin-bottom: 25px; transition: 0.3s; }
.review-card:hover { border-color: #ff6b00; box-shadow: 0 5px 15px rgba(0,0,0,0.02); }
.svip-card { background: linear-gradient(90deg, #fffcf0 0%, #fff 100%); border: 1px solid #ffe58f; }

.user-top { display: flex; align-items: center; gap: 12px; margin-bottom: 5px; }
.realm-badge { font-size: 11px; padding: 2px 8px; border-radius: 4px; background: #f1f5f9; color: #64748b; font-weight: bold; }
.gold-badge { background: #1a1a1a; color: #ffd700; border: 1px solid #ffd700; }
.review-content { padding-left: 68px; font-size: 15px; color: #444; line-height: 1.8; }
.img-err { color: #999; font-size: 14px; }
</style>