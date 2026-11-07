<template>
  <div class="profile-page">
    <div class="container">
     
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
        <!-- 2. 左侧侧边导航 -->
        <el-menu :default-active="activeTab" class="side-nav" @select="handleMenuSelect">
          <el-menu-item index="info">个人信息</el-menu-item>
          <el-menu-item index="address">地址管理</el-menu-item>
          <el-menu-item index="order">我的订单</el-menu-item>
          <el-menu-item index="fav">我的收藏</el-menu-item>
          <el-menu-item index="return">售后记录</el-menu-item>
          <el-menu-item index="vip">会员中心</el-menu-item>
        </el-menu>

        <!-- 3. 右侧内容区 -->
        <div class="content-area" v-loading="loading">
          
          <!-- A. 个人信息 -->
          <div v-if="activeTab === 'info'">
            <h3 class="view-title">个人资料</h3>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="道号">{{ user.username }}</el-descriptions-item>
              <el-descriptions-item label="当前境界">{{ user.currentLevel || '灵胎' }}</el-descriptions-item>
              <el-descriptions-item label="累积经验">{{ user.experience || 0 }} Exp</el-descriptions-item>
              <el-descriptions-item label="注册日期">{{ formatDate(user.createdAt) }}</el-descriptions-item>
            </el-descriptions>
            <div class="hint">
              <el-icon><InfoFilled /></el-icon> 境界越高，折扣越多。
            </div>
          </div>

          <!-- B. 地址管理  -->
          <div v-if="activeTab === 'address'">
            <div class="view-header">
              <h3 class="view-title">地址符传</h3>
              <el-button type="primary" size="small" @click="showAddr=true">+ 新增地址</el-button>
            </div>
            <div class="address-list">
              <div v-for="item in addressList" :key="item.id" class="addr-card" :class="{ active: item.isDefault === 1 }">
                <div class="addr-top">
                  <strong>{{ item.receiverName }}</strong> <span class="phone">{{ item.phone }}</span>
                  <el-tag v-if="item.isDefault === 1" size="small" type="warning" effect="dark" style="margin-left:10px">默认</el-tag>
                </div>
                <p class="addr-detail">{{ item.province }} {{ item.city }} {{ item.district }} {{ item.detailAddress }}</p>
                <div class="addr-ops">
                  <el-button v-if="item.isDefault !== 1" type="warning" link @click="setDefault(item.id)">设为默认</el-button>
                  <el-button type="danger" link @click="deleteAddr(item.id)">删除</el-button>
                </div>
              </div>
              <el-empty v-if="addressList.length === 0" />
            </div>
          </div>

          <!-- C. 我的订单 -->
          <div v-if="activeTab === 'order'">
            <h3 class="view-title">交易订单</h3>
            <el-table :data="orderList" border stripe>
              <el-table-column prop="orderNo" label="订单编号" min-width="150" />
              <el-table-column label="支付金额">
                <template #default="{row}">￥{{ (row.payAmount || 0).toFixed(2) }}</template>
              </el-table-column>
              <el-table-column label="下单时间" width="160">
                <template #default="{row}">{{ formatDate(row.createdAt) }}</template>
              </el-table-column>
              <el-table-column label="操作" width="180" align="center">
                <template #default="{row}">
                  <el-button type="primary" link @click="openOrderDetail(row.id)">详情 / 评价 / 售后</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-if="orderList.length === 0" />
          </div>

          <!-- D. 售后记录  -->
          <div v-if="activeTab === 'return'">
            <h3 class="view-title">售后进度记录</h3>
            <el-table :data="returnList" border stripe>
              <el-table-column label="影像" width="80" align="center">
                <template #default="{row}">
                  <el-image :src="formatImg(row.productImage)" style="width:40px;height:40px;border-radius:4px" />
                </template>
              </el-table-column>
              <el-table-column prop="productName" label="商品名称" />
              <el-table-column label="申请类型" width="100">
                <template #default="{row}">{{ ['退款','换货','退货退款'][row.type] || '其他' }}</template>
              </el-table-column>
              <el-table-column label="处理状态" width="120" align="center">
                <template #default="{row}">
                  <el-tag :type="['info','success','danger','warning'][row.status]">
                    {{ ['待审核','已通过','被拒绝','完成'][row.status] }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="reason" label="申请理由" show-overflow-tooltip />
            </el-table>
            <el-empty v-if="returnList.length === 0" />
          </div>

          <!-- E. 我的收藏 -->
          <div v-if="activeTab === 'fav'">
            <h3 class="view-title">收藏</h3>
            <el-row :gutter="20">
              <el-col :span="8" v-for="p in favList" :key="p.id">
                <el-card shadow="hover" class="fav-card" @click="$router.push('/product/'+p.id)">
                  <img :src="formatImg(p.mainImage)" class="fav-img" />
                  <p class="p-name">{{ p.name }}</p>
                </el-card>
              </el-col>
            </el-row>
            <el-empty v-if="favList.length === 0" />
          </div>

          <!-- F. 会员中心  -->
          <div v-if="activeTab === 'vip'">
             <div class="vip-buy-card">
              <h3>突破境界中心</h3>
              <p>购买灵丹直接冲击更高阶位，解锁全场专属折扣</p>
              <div class="price">￥19 / 30天</div>
              <el-button type="warning" size="large" @click="handleBuyVip" class="buy-btn">立即突破</el-button>
             </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 弹窗 1：订单清单 (带申请售后) -->
    <el-dialog v-model="orderDetailVisible" title="订单法宝清单" width="780px">
      <el-table :data="currentOrderItems" border stripe>
        <el-table-column label="影像" width="80" align="center">
          <template #default="{row}">
            <el-image :src="formatImg(row.productImage)" style="width:50px;height:50px;border-radius:4px" fit="cover"/>
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column label="价格x数量" width="150">
          <template #default="{row}">￥{{ (row.price || 0).toFixed(2) }} x {{ row.quantity }}</template>
        </el-table-column>
        <el-table-column label="因果操作" width="220" align="center">
          <template #default="{row}">
            <el-button type="warning" plain size="small" @click="openReview(row)">去评价</el-button>
            <el-button type="danger" size="small" @click="openReturn(row)">申请售后</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 弹窗 2：评价 -->
    <el-dialog v-model="reviewVisible" title="发表评价" width="400px" center>
       <div style="text-align:center">
          <p>评价：<b>{{ targetItem?.productName }}</b></p>
          <el-rate v-model="reviewForm.rating" show-text style="margin:20px 0" />
          <el-input v-model="reviewForm.content" type="textarea" :rows="4" placeholder="分享您的使用体验..." />
       </div>
       <template #footer>
         <el-button @click="reviewVisible = false">取消</el-button>
         <el-button type="primary" class="orange-btn" @click="submitReview">发布评价</el-button>
       </template>
    </el-dialog>

    <!-- 弹窗 3：申请售后 -->
    <el-dialog v-model="returnVisible" title="申请售后重塑" width="450px">
      <el-form :model="returnForm" label-width="90px">
        <el-form-item label="对应商品">
          <el-input :model-value="targetItem?.productName" disabled />
        </el-form-item>
        <el-form-item label="申请类型">
          <el-select v-model="returnForm.type" style="width:100%">
            <el-option :value="0" label="退款 (不退货)" />
            <el-option :value="1" label="换货 (同款换新)" />
            <el-option :value="2" label="退货退款" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请原因">
          <el-input v-model="returnForm.reason" type="textarea" :rows="3" placeholder="请详细描述理由..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="returnVisible = false">取消</el-button>
        <el-button type="primary" class="orange-btn" @click="submitReturn">提交申请</el-button>
      </template>
    </el-dialog>

    <!-- 弹窗 4：新增地址 (省市区) -->
    <el-dialog v-model="showAddr" title="新增收货地址" width="500px">
      <el-form :model="addrForm" label-width="80px">
        <el-form-item label="收件人"><el-input v-model="addrForm.receiverName" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="addrForm.phone" /></el-form-item>
        <el-form-item label="所在地">
          <div class="addr-select-group">
            <el-input v-model="addrForm.province" placeholder="省" style="width:30%"/>
            <el-input v-model="addrForm.city" placeholder="市" style="width:30%"/>
            <el-input v-model="addrForm.district" placeholder="区" style="width:35%"/>
          </div>
        </el-form-item>
        <el-form-item label="详细地址"><el-input v-model="addrForm.detailAddress" placeholder="街道门牌号" /></el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="saveAddr">确认保存</el-button></template>
    </el-dialog>
  </div>
</template>

// ... existing code ...
<script setup>
import { ref, onMounted, computed } from 'vue'
import frontRequest from '@/utils/frontRequest'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { InfoFilled } from '@element-plus/icons-vue'

const router = useRouter()
const activeTab = ref('info')
const loading = ref(false)

// 初始化用户信息
const user = ref(JSON.parse(sessionStorage.getItem('user') || '{}'))

// 列表数据
const addressList = ref([])
const orderList = ref([])
const favList = ref([])
const returnList = ref([])

// 弹窗与交互状态
const orderDetailVisible = ref(false)
const currentOrderItems = ref([])
const currentOrderId = ref(null)
const reviewVisible = ref(false)
const returnVisible = ref(false)
const targetItem = ref(null)
const showAddr = ref(false)

// 表单数据
const addrForm = ref({ receiverName: '', phone: '', province: '', city: '', district: '', detailAddress: '', isDefault: 0 })
const reviewForm = ref({ rating: 5, content: '' })
const returnForm = ref({ type: 0, reason: '' })

// 修为百分比计算
const expPercentage = computed(() => {
  const exp = user.value.experience || 0
  return Math.min((exp % 5000) / 50, 100)
})

// 辅助方法：图片处理
const formatImg = (url) => {
  if (!url) return ''
  return url.startsWith('http') ? url : 'http://localhost:80' + url
}
const formatDate = (d) => d ? new Date(d).toLocaleString() : '-'

// 菜单切换逻辑
const handleMenuSelect = (idx) => {
  activeTab.value = idx
  if (idx === 'address') loadAddress()
  if (idx === 'order') loadOrders()
  if (idx === 'fav') loadFavs()
  if (idx === 'return') loadReturns()
}

/** 1. 业务逻辑：评价与售后 **/
const openOrderDetail = async (id) => {
  currentOrderId.value = id
  loading.value = true
  try {
    
    const res = await frontRequest.get(`/order/detail/${id}`)
    if (res.code === 200) {
      currentOrderItems.value = res.data.items || []
      orderDetailVisible.value = true
    }
  } finally {
    loading.value = false
  }
}

const openReview = (row) => {
  targetItem.value = row
  reviewVisible.value = true
}

const submitReview = async () => {
  if (!reviewForm.value.content) return ElMessage.warning('请填写评价内容')
 
  const res = await frontRequest.post('/review/add', {
    ...reviewForm.value,
    userId: user.value.id,
    productId: targetItem.value.productId,
    orderItemId: targetItem.value.id
  })
  if (res.code === 200) {
    ElMessage.success('评价已发布')
    reviewVisible.value = false
    syncUserStatus()
  }
}

const openReturn = (row) => {
  targetItem.value = row
  returnForm.value = { type: 0, reason: '' }
  returnVisible.value = true
}

const submitReturn = async () => {
  if (!returnForm.value.reason) return ElMessage.warning('请描述理由')
 
  const res = await frontRequest.post('/return/apply', {
    ...returnForm.value,
    userId: user.value.id,
    orderId: currentOrderId.value,
    orderItemId: targetItem.value.id,
    productId: targetItem.value.productId,
    productName: targetItem.value.productName,
    productImage: targetItem.value.productImage
  })
  if (res.code === 200) {
    ElMessage.success('申请提交成功')
    returnVisible.value = false
    orderDetailVisible.value = false
    loadReturns()
  }
}

/** 2. 基础加载逻辑  **/
const loadOrders = async () => {
  const res = await frontRequest.get('/order/list', { params: { userId: user.value.id } })
  if (res.code === 200) orderList.value = res.data
}

const loadReturns = async () => {
  const res = await frontRequest.get('/return/list', { params: { userId: user.value.id } })
  if (res.code === 200) returnList.value = res.data
}

const loadAddress = async () => {
  // FrontAddressController: @RequestMapping("/front/address")
  const res = await frontRequest.get('/address/list', { params: { userId: user.value.id } })
  if (res.code === 200) addressList.value = res.data
}

const loadFavs = async () => {
  const res = await frontRequest.get('/favorite/list', { params: { userId: user.value.id } })
  if (res.code === 200) favList.value = res.data
}

/** 3. 地址与会员操作 **/
const setDefault = async (id) => {
  await frontRequest.post('/address/set-default', { id, userId: user.value.id })
  loadAddress()
}

const deleteAddr = async (id) => {
  await frontRequest.post('/address/delete', { id, userId: user.value.id })
  loadAddress()
}

const saveAddr = async () => {
  await frontRequest.post('/address/add', { ...addrForm.value, userId: user.value.id })
  showAddr.value = false
  loadAddress()
}

/** 购买VIP **/
const handleBuyVip = async () => {
  // 匹配 VipController @RequestMapping("/vip") 的 /buy 接口
  const res = await frontRequest.post('/vip/buy', { userId: user.value.id, months: 1 })
  if (res.code === 200) {
    user.value = res.data
    sessionStorage.setItem('user', JSON.stringify(res.data))
    ElMessage.success('境界突破成功')
    window.dispatchEvent(new Event('storage'))
  }
}

/** 同步用户信息 **/
const syncUserStatus = async () => {
  if (!user.value.id) return
  // 匹配 FrontLoginController
  const res = await frontRequest.get('/user/info', { params: { userId: user.value.id } })
  if (res.code === 200) {
    user.value = res.data
    sessionStorage.setItem('user', JSON.stringify(res.data))
    window.dispatchEvent(new Event('storage'))
  }
}

onMounted(() => {
  if (!sessionStorage.getItem('user')) {
    router.push('/login')
  } else {
    syncUserStatus()
  }
})
</script>


<style scoped>
.profile-page { background: #f8f9fa; min-height: 100vh; padding: 40px 0; }
.container { max-width: 1200px; margin: 0 auto; padding: 0 20px; }

/* Hero 卡片样式 */
.hero-card { background: #fff; padding: 35px; border-radius: 16px; display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; border: 1px solid #eee; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
.is-god { background: linear-gradient(135deg, #1a1a1a, #333); color: #ffd700; border-color: #ffd700; }
.user-text h2 { font-size: 24px; margin: 0; font-weight: bold; }
.exp-box { margin-top: 10px; display: flex; align-items: center; gap: 15px; font-size: 13px; color: #666; }
.is-god .exp-box { color: #ffd700; }
.vip-info { font-size: 14px; color: #ffd700; border: 1px solid #ffd700; padding: 4px 12px; border-radius: 20px; }

.profile-body { display: flex; gap: 25px; }
.side-nav { width: 240px; border-radius: 12px; border: none; box-shadow: 0 4px 15px rgba(0,0,0,0.05); height: fit-content; }
.content-area { flex: 1; background: #fff; border-radius: 12px; padding: 35px; min-height: 550px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }

.view-title { border-left: 5px solid #ff6b00; padding-left: 15px; margin-bottom: 25px; font-size: 18px; color: #333; font-weight: 600; }
.view-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }

/* 地址与卡片 */
.addr-card { border: 1px solid #eee; padding: 20px; border-radius: 12px; margin-bottom: 15px; background: #fafafa; }
.addr-card.active { border-color: #ff6b00; background: #fffaf5; }
.phone { margin-left: 12px; color: #888; font-size: 14px; }
.addr-ops { margin-top: 10px; text-align: right; }
.addr-select-group { display: flex; gap: 5px; }

.fav-img { width: 100%; height: 160px; object-fit: cover; border-radius: 8px; }
.p-name { font-size: 14px; margin-top: 10px; text-align: center; }

/* 会员中心按钮样式 */
.vip-buy-card { background: #1a1a1a; color: #fff; padding: 60px; border-radius: 20px; text-align: center; }
.price { font-size: 36px; color: #ff6b00; margin: 25px 0; font-weight: bold; }
.buy-btn { background: #ff6b00; border: none; color: #fff; width: 240px; height: 50px; border-radius: 25px; font-weight: bold; }

.orange-btn { background: #ff6b00 !important; border: none !important; color: #fff !important; }
.hint { margin-top: 20px; color: #ff6b00; font-size: 13px; display: flex; align-items: center; gap: 5px; }
</style>