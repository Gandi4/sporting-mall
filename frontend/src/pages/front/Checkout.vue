<template>
  <div class="page-wrapper">
    <div class="checkout-container">
      <h1 class="page-title">确认订单</h1>

      <!-- 1. 地址选择区 (新版网格逻辑 + 旧版视觉) -->
      <div class="section">
        <h3 class="section-title">选择收货地址</h3>
        <div class="address-selection-grid">
          <!-- 地址卡片 -->
          <div 
            v-for="item in addressList" 
            :key="item.id" 
            class="addr-sel-card" 
            :class="{ active: selectedAddrId === item.id }"
            @click="selectedAddrId = item.id"
          >
            <div class="check-icon" v-if="selectedAddrId === item.id">
              <el-icon><CircleCheckFilled /></el-icon>
            </div>
            <div class="addr-content">
              <p class="user-line">
                <strong>{{ item.receiverName }}</strong> 
                <span class="phone">{{ item.phone }}</span>
              </p>
              <p class="text-detail">{{ item.province }} {{ item.city }} {{ item.district }} {{ item.detailAddress }}</p>
            </div>
            <el-tag v-if="item.isDefault === 1" size="small" type="info" class="def-tag">默认</el-tag>
          </div>
          
          <!-- 管理地址按钮 -->
          <div class="addr-sel-card add-btn" @click="$router.push('/profile')">
            <el-icon :size="24"><Plus /></el-icon>
            <p>管理/新增地址</p>
          </div>
        </div>
        <el-alert v-if="addressList.length === 0" title="请先前往个人中心设置收货地址" type="warning" show-icon :closable="false" style="margin-top:15px" />
      </div>

      <!-- 2. 商品清单 (整合旧版图片处理) -->
      <div class="section">
        <h3 class="section-title">商品清单</h3>
        <div v-for="item in items" :key="item.cartId" class="p-row">
          <!-- 使用 formatImg 修复图片显示 -->
          <el-image :src="formatImg(item.productImage)" class="p-img" fit="cover">
            <template #error>
              <div class="img-error"><span>图损</span></div>
            </template>
          </el-image>
          <div class="p-info">
            <p class="name">{{ item.productName }}</p>
            <p class="price">￥{{ (item.price || 0).toFixed(2) }} x {{ item.quantity }}</p>
          </div>
          <div class="p-subtotal">￥{{ (item.subtotal || 0).toFixed(2) }}</div>
        </div>
        <el-empty v-if="items.length === 0" description="结算清单为空" :image-size="80" />
      </div>

      <!-- 3. 底部结算 (新版逻辑 + 旧版样式) -->
      <div class="checkout-footer">
        <div class="summary">
          <span class="label">应付总额：</span>
          <span class="total-price">￥{{ totalAmount }}</span>
        </div>
        <el-button 
          type="warning" 
          size="large" 
          class="submit-btn" 
          :disabled="!selectedAddrId || items.length === 0" 
          @click="handleSubmit"
        >
          提交订单并支付
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import frontRequest from '@/utils/frontRequest'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, CircleCheckFilled } from '@element-plus/icons-vue'

const router = useRouter()
const items = ref([])
const addressList = ref([])
const totalAmount = ref('0.00')
const selectedAddrId = ref(null)

// 1. 图片路径处理
const formatImg = (url) => {
  if (!url) return ''
  return url.startsWith('http') ? url : 'http://localhost:80' + url
}

// 2. 加载数据
const loadData = async () => {
  const userStr = sessionStorage.getItem('user')
  if (!userStr) {
    ElMessage.warning('您的会话已过期，请重新登录后再进行结算')
    return router.push('/login')
  }
  const user = JSON.parse(userStr)

  try {
    // A. 加载订单预览（获取商品和总价）
    const res = await frontRequest.get('/order/preview', { params: { userId: user.id } })
    if (res.code === 200) {
      items.value = res.data.items || []
      totalAmount.value = (res.data.totalAmount || 0).toFixed(2)
    }

    // B. 加载地址列表供用户选择
    const addrRes = await frontRequest.get('/address/list', { params: { userId: user.id } })
    if (addrRes.code === 200) {
      addressList.value = addrRes.data
      // 策略：优先选中标记为 isDefault 的地址，若无则选第一个
      const def = addrRes.data.find(a => a.isDefault === 1)
      if (def) {
        selectedAddrId.value = def.id
      } else if (addrRes.data.length > 0) {
        selectedAddrId.value = addrRes.data[0].id
      }
    }
  } catch (e) {
    console.error('Checkout Data Load Error:', e)
    ElMessage.error('结算数据加载失败，请检查后端服务')
  }
}

// 3. 提交订单 (跳转至支付页)
const handleSubmit = async () => {
  if (!selectedAddrId.value) {
    return ElMessage.warning('请选择一个收货地址')
  }

  //从 localStorage 切换到 sessionStorage
  const user = JSON.parse(sessionStorage.getItem('user') || '{}')
  
  try {
    const res = await frontRequest.post('/order/submit', { 
      userId: user.id, 
      addressId: selectedAddrId.value // 发送用户实时选中的地址ID
    })
    
    if (res.code === 200) {
      ElMessage.success('订单已成功创建')
      // 跳转到收银台支付页，携带订单 ID
      router.push(`/pay/${res.data.orderId}`)
    }
  } catch (e) { 
    console.error('Order Submit Error:', e)
    ElMessage.error('创建订单失败，请稍后重试') 
  }
}

onMounted(loadData)
</script>

<style scoped>
.page-wrapper { background: #f8f9fa; min-height: 100vh; padding: 40px; }
.checkout-container { max-width: 1000px; margin: 0 auto; background: #fff; padding: 40px; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.05); }

.page-title { font-size: 28px; font-weight: bold; margin-bottom: 40px; border-bottom: 2px solid #f5f5f5; padding-bottom: 20px; color: #1a1a1a; }

.section { margin-bottom: 45px; }
.section-title { border-left: 5px solid #ff6b00; padding-left: 15px; margin-bottom: 20px; font-size: 18px; color: #333; font-weight: 600; }

/* 地址选择网格样式 */
.address-selection-grid { 
  display: grid; 
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); 
  gap: 20px; 
}
.addr-sel-card { 
  border: 2px solid #f0f0f0; 
  padding: 20px; 
  border-radius: 12px; 
  cursor: pointer; 
  position: relative; 
  transition: all 0.3s ease; 
  background: #fff;
  min-height: 100px;
}
.addr-sel-card:hover { border-color: #ff6b00; }
.addr-sel-card.active { border-color: #ff6b00; background: #fffaf5; box-shadow: 0 4px 12px rgba(255, 107, 0, 0.1); }

.user-line { margin-bottom: 8px; font-size: 15px; }
.phone { color: #888; font-size: 13px; margin-left: 8px; }
.text-detail { font-size: 13px; color: #666; line-height: 1.5; }

.check-icon { position: absolute; top: -10px; right: -10px; color: #ff6b00; font-size: 24px; background: #fff; border-radius: 50%; }
.def-tag { position: absolute; bottom: 10px; right: 10px; font-size: 11px; }

.add-btn { 
  display: flex; 
  flex-direction: column; 
  align-items: center; 
  justify-content: center; 
  color: #999; 
  border-style: dashed; 
  background: #fafafa;
}
.add-btn:hover { color: #ff6b00; background: #fffaf5; }

/* 商品清单样式 */
.p-row { display: flex; align-items: center; padding: 20px 0; border-bottom: 1px solid #f2f2f2; }
.p-img { width: 80px; height: 80px; border-radius: 8px; margin-right: 20px; background: #f5f5f5; flex-shrink: 0; }
.img-error { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; background: #eee; color: #bbb; font-size: 12px; }

.p-info { flex: 1; }
.p-info .name { font-weight: 600; font-size: 16px; margin-bottom: 8px; color: #333; }
.p-info .price { color: #999; font-size: 14px; }
.p-subtotal { font-size: 20px; font-weight: bold; color: #f56c6c; width: 140px; text-align: right; }

/* 底部结算样式 */
.checkout-footer { text-align: right; margin-top: 50px; border-top: 1px solid #eee; padding-top: 30px; }
.summary { margin-bottom: 25px; }
.summary .label { font-size: 18px; color: #666; }
.total-price { font-size: 36px; font-weight: 800; color: #f56c6c; margin-left: 15px; }

.submit-btn { 
  width: 280px; 
  height: 56px; 
  border-radius: 28px; 
  font-size: 20px; 
  font-weight: bold; 
  background: #ff6b00 !important; 
  border: none !important; 
  box-shadow: 0 8px 15px rgba(255, 107, 0, 0.2);
}
.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 12px 20px rgba(255, 107, 0, 0.3);
}
</style>