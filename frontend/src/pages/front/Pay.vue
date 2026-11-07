<template>
  <div class="pay-page">
    <div class="pay-container">
      <el-card class="pay-card">
        <div class="pay-header">
          <el-result icon="warning" title="订单提交成功，请尽快支付！">
            <template #sub-title>
              <p class="order-info">订单号：{{ orderId }} | 应付金额：<span class="price">￥{{ amount }}</span></p>
              <p class="timer">请在 <span class="time">14:59</span> 内完成支付，超时订单将自动取消</p>
            </template>
          </el-result>
        </div>

        <div class="payment-methods">
          <h3 class="method-title">选择支付方式</h3>
          <div class="method-list">
            <!-- 支付宝：纯 SVG 实现 -->
            <div 
              class="method-item" 
              :class="{ active: payMethod === 'alipay' }" 
              @click="payMethod = 'alipay'"
            >
              <div class="method-content">
                <svg class="pay-svg-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg">
                  <path d="M512 1024c282.752 0 512-229.248 512-512S794.752 0 512 0 0 229.248 0 512s229.248 512 512 512z" fill="#1677FF"></path>
                  <path d="M710.272 546.944a101.888 101.888 0 0 1-62.848-26.624 186.24 186.24 0 0 0-21.76-46.08c17.152-3.712 34.176-7.808 50.432-13.44 26.368-8.96 46.592-23.936 46.592-63.104 0-25.088-12.8-40.448-35.968-45.184-21.888-4.48-47.616-2.56-74.88-2.56-43.264 0-87.936 0-112.512 2.56a120.32 120.32 0 0 0-38.656 8.704c-11.392 5.376-17.792 14.976-17.792 31.488 0 19.328 10.112 32.128 29.824 37.12 10.624 2.816 23.936 3.072 38.656 3.072 6.4 0 12.8 0 19.2 0 1.92 5.12 3.84 10.368 5.888 15.616-43.264 12.544-84.352 32.768-109.184 80.512-14.848 28.544-21.504 59.904-12.544 94.72 9.216 35.712 38.016 57.6 74.368 57.6 36.48 0 68.608-20.992 90.112-51.584a179.968 179.968 0 0 0 24.32-47.744c20.352 9.088 43.136 18.048 68.096 18.048 40.192 0 76.544-24.32 89.216-52.992 4.48-10.112 5.248-18.432 2.304-26.624h10.496a30.848 30.848 0 0 0 32.768-30.976 29.824 29.824 0 0 0-26.112-30.72z m-357.632-15.616a22.528 22.528 0 0 1-22.528-22.528 22.528 22.528 0 0 1 22.528-22.528h331.008l41.6 45.056H352.64z" fill="#FFFFFF"></path>
                </svg>
                <span class="pay-name">支付宝</span>
              </div>
              <div v-if="payMethod === 'alipay'" class="check-icon">
                <el-icon><CircleCheckFilled /></el-icon>
              </div>
            </div>

            <!-- 微信支付：纯 SVG 实现 -->
            <div 
              class="method-item" 
              :class="{ active: payMethod === 'wechat' }" 
              @click="payMethod = 'wechat'"
            >
              <div class="method-content">
                <svg class="pay-svg-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg">
                  <path d="M512 1024c282.752 0 512-229.248 512-512S794.752 0 512 0 0 229.248 0 512s229.248 512 512 512z" fill="#07C160"></path>
                  <path d="M638.208 300.032c-115.712 0-209.92 84.48-209.92 188.416 0 56.576 28.16 107.008 72.448 142.08l-18.432 55.04 63.744-31.744c29.184 8.704 60.16 13.568 92.16 13.568 115.712 0 209.92-84.48 209.92-188.416s-93.952-188.928-210.176-188.928zM578.56 442.112c-12.8 0-23.04-10.24-23.04-23.04s10.24-23.04 23.04-23.04 23.04 10.24 23.04 23.04-10.24 23.04-23.04 23.04z m119.296 0c-12.8 0-23.04-10.24-23.04-23.04s10.24-23.04 23.04-23.04 23.04 10.24 23.04 23.04-10.24 23.04-23.04 23.04z" fill="#FFFFFF"></path>
                  <path d="M380.16 414.72c-96.512 0-174.592 70.4-174.592 157.184 0 47.104 23.296 89.088 60.416 118.528l-15.36 45.824 53.248-26.368c24.32 7.168 50.176 11.264 76.8 11.264 12.032 0 24.064-0.768 35.584-2.56-4.608-11.776-7.168-24.576-7.168-37.888 0-66.56 60.16-120.32 134.4-120.32 10.24 0 20.224 1.024 29.696 2.816-1.536-105.472-88.832-188.48-193.024-188.48zM315.648 533.248c-10.752 0-19.2-8.448-19.2-19.2s8.448-19.2 19.2-19.2 19.2 8.448 19.2 19.2-8.448 19.2-19.2 19.2z m104.96 0c-10.752 0-19.2-8.448-19.2-19.2s8.448-19.2 19.2-19.2 19.2 8.448 19.2 19.2-8.448 19.2-19.2 19.2z" fill="#FFFFFF"></path>
                </svg>
                <span class="pay-name">微信支付</span>
              </div>
              <div v-if="payMethod === 'wechat'" class="check-icon">
                <el-icon><CircleCheckFilled /></el-icon>
              </div>
            </div>
          </div>
        </div>

        <div class="qr-area" v-if="payMethod">
          <div class="qr-box">
            <!-- 模拟二维码生成：如果这里也不稳定，可以用 Canvas 生成 -->
            <img :src="`https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=${payMethod === 'alipay' ? 'ALIPAY_ORDER_' + orderId : 'WECHAT_ORDER_' + orderId}`" />
            <p>使用 {{ payMethod === 'alipay' ? '支付宝' : '微信' }} 扫码支付</p>
          </div>
          <div class="op-btns">
            <el-button type="primary" size="large" class="confirm-btn" @click="handlePaySuccess">我已完成支付</el-button>
            <p class="back-link" @click="$router.push('/order')">返回修改订单</p>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { CircleCheckFilled } from '@element-plus/icons-vue'
import frontRequest from '@/utils/frontRequest'

const route = useRoute()
const router = useRouter()
const orderId = route.params.orderId
const amount = ref('0.00')
const payMethod = ref('alipay')

const loadOrder = async () => {
  try {
    const res = await frontRequest.get(`/order/detail/${orderId}`)
    if (res.code === 200) {
      amount.value = (res.data.order.payAmount || 0).toFixed(2)
    }
  } catch (e) {
    ElMessage.error('获取订单详情失败')
  }
}

const handlePaySuccess = async () => {
  ElMessage.success('支付确认成功！境界提升中...')
  setTimeout(() => {
    router.push('/order') 
  }, 1200)
}

onMounted(loadOrder)
</script>

<style scoped>
.pay-page { background: #f4f7f9; min-height: 100vh; padding: 60px 0; }
.pay-container { max-width: 700px; margin: 0 auto; }
.pay-card { border-radius: 20px; padding: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.05); }

.price { color: #f56c6c; font-size: 26px; font-weight: 800; }
.time { color: #ff6b00; font-weight: bold; }

.payment-methods { margin-top: 30px; padding: 0 20px; }
.method-title { font-size: 18px; font-weight: 600; margin-bottom: 20px; color: #333; }
.method-list { display: flex; gap: 20px; }

.method-item { 
  flex: 1; border: 2px solid #f0f0f0; border-radius: 16px; padding: 22px; 
  cursor: pointer; position: relative; transition: all 0.3s ease;
  background: #fff;
}
.method-item:hover { border-color: #ff6b00; }
.method-item.active { border-color: #ff6b00; background: #fffaf5; }

.method-content { display: flex; align-items: center; justify-content: center; gap: 12px; }

/* SVG 图标尺寸控制 */
.pay-svg-icon { width: 34px; height: 34px; }

.pay-name { font-size: 17px; font-weight: 600; color: #444; }

.check-icon { 
  position: absolute; top: -10px; right: -10px; 
  color: #ff6b00; font-size: 26px; 
  background: #fff; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
}

.qr-area { margin-top: 40px; text-align: center; border-top: 1px solid #f2f2f2; padding-top: 40px; }
.qr-box { 
  background: #fff; padding: 20px; display: inline-block; 
  border: 1px solid #eee; border-radius: 12px; margin-bottom: 25px; 
}
.qr-box img { width: 160px; height: 160px; }

.op-btns { display: flex; flex-direction: column; align-items: center; gap: 15px; }
.confirm-btn { 
  width: 320px; height: 54px; border-radius: 27px; 
  background: #ff6b00; border: none; font-size: 18px; 
  font-weight: bold; box-shadow: 0 6px 15px rgba(255, 107, 0, 0.2);
}
.back-link { color: #999; font-size: 14px; cursor: pointer; text-decoration: underline; }
</style>