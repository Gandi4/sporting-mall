<template>
  <div class="product-container">
    <h2 class="section-title">{{ title || '精选推荐' }}</h2>
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in products" :key="item.id" style="margin-bottom: 25px;">
        <el-card :body-style="{ padding: '0px' }" shadow="hover" class="product-card" @click="goDetail(item.id)">
          <div class="image-box">
            <el-image :src="formatImg(item.mainImage)" fit="cover" class="product-img">
              <template #error>
                <div class="image-error-slot">
                  <el-icon><Picture /></el-icon>
                  <p>SPORTING</p>
                </div>
              </template>
            </el-image>
          </div>
          <div class="card-info">
            <div class="product-name">{{ item.name }}</div>
            <div class="product-bottom">
              <span class="price">￥{{ (item.price || 0).toFixed(2) }}</span>
              <span class="sales">热销中</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Picture } from '@element-plus/icons-vue'

defineProps({ products: Array, title: String })
const router = useRouter()


const formatImg = (url) => {
  if (!url) return ''
  
  if (url.startsWith('http')) {
    return url
  }

  const path = url.startsWith('/') ? url : '/' + url
  return 'http://localhost:80' + path
}

const goDetail = (id) => router.push('/product/' + id)
</script>

<style scoped>
.section-title { font-size: 24px; font-weight: bold; margin-bottom: 30px; border-left: 6px solid #ff6b00; padding-left: 15px; color: #333; }
.product-card { cursor: pointer; transition: 0.3s; border-radius: 12px; border: 1px solid #f0f0f0; overflow: hidden; }
.product-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }
.image-box { width: 100%; height: 220px; background: #fafafa; }
.product-img { width: 100%; height: 100%; }
.image-error-slot { height: 100%; display: flex; flex-direction: column; align-items: center; justify-content: center; background: #eee; color: #bbb; }
.card-info { padding: 15px; }
.product-name { font-size: 15px; font-weight: 600; height: 44px; overflow: hidden; color: #333; line-height: 22px; }
.product-bottom { display: flex; justify-content: space-between; align-items: center; margin-top: 10px; }
.price { color: #f56c6c; font-size: 18px; font-weight: 800; }
.sales { color: #999; font-size: 12px; }
</style>