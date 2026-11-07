<template>
  <div class="order-detail-return-container">
    <!-- 1. 顶部导航/面包屑 -->
    <div class="breadcrumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/profile' }">个人中心</el-breadcrumb-item>
        <el-breadcrumb-item>订单详情与售后</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="content-wrapper" v-loading="loading">
      <!-- 2. 订单基本信息卡片 -->
      <el-card class="box-card order-info-card" v-if="orderInfo">
        <template #header>
          <div class="card-header">
            <span>订单编号：{{ orderInfo.orderNo }}</span>
            <el-tag :type="getStatusType(orderInfo.status)" size="large">
              {{ getStatusText(orderInfo.status) }}
            </el-tag>
          </div>
        </template>
        
        <div class="order-meta">
          <div class="meta-item">
            <span class="label">下单时间：</span>
            <span class="value">{{ formatTime(orderInfo.createTime) }}</span>
          </div>
          <div class="meta-item">
            <span class="label">收货人：</span>
            <span class="value">{{ orderInfo.receiverName }} {{ orderInfo.receiverPhone }}</span>
          </div>
          <div class="meta-item">
            <span class="label">收货地址：</span>
            <span class="value">{{ orderInfo.receiverAddress }}</span>
          </div>
        </div>
      </el-card>

      <!-- 3. 商品列表 -->
      <el-card class="box-card product-list-card" v-if="orderInfo && orderInfo.items">
        <div class="product-item" v-for="item in orderInfo.items" :key="item.id">
          <div class="product-img">
            <el-image 
              :src="formatImg(item.productImage)" 
              fit="cover" 
              style="width: 80px; height: 80px; border-radius: 4px;"
            />
          </div>
          <div class="product-info">
            <div class="product-name">{{ item.productName }}</div>
            <div class="product-spec" v-if="item.specs">{{ item.specs }}</div>
            <div class="product-price">
              <span class="price">¥{{ item.price }}</span>
              <span class="count">x{{ item.quantity }}</span>
            </div>
          </div>
          <div class="product-action">
            <el-button 
              type="primary" 
              plain 
              size="small"
              @click="openReturnDialog(item)"
              :disabled="orderInfo.status !== 3" 
            >
              申请售后
            </el-button>
          </div>
        </div>
        
        <div class="total-section">
          <span class="total-label">订单总计：</span>
          <span class="total-price">¥{{ orderInfo.totalAmount }}</span>
        </div>
      </el-card>

      <!-- 4. 售后记录列表 (如果已有申请) -->
      <el-card class="box-card return-history-card" v-if="returnList.length > 0">
        <template #header>
          <div class="card-header">
            <span>售后记录</span>
          </div>
        </template>
        <el-table :data="returnList" style="width: 100%">
          <el-table-column prop="createTime" label="申请时间" width="180">
            <template #default="scope">
              {{ formatTime(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="reason" label="售后原因" />
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <el-tag :type="getReturnStatusType(scope.row.status)">
                {{ getReturnStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button size="small" @click="viewReturnDetail(scope.row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 5. 申请售后弹窗 -->
    <el-dialog 
      v-model="returnDialogVisible" 
      title="申请售后" 
      width="500px"
      @close="resetReturnForm"
    >
      <el-form :model="returnForm" label-width="100px" ref="returnFormRef">
        <el-form-item label="商品名称">
          <span>{{ currentProduct?.productName }}</span>
        </el-form-item>
        <el-form-item label="售后类型">
          <el-radio-group v-model="returnForm.type">
            <el-radio :label="1">仅退款</el-radio>
            <el-radio :label="2">退货退款</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="申请原因" required>
          <el-select v-model="returnForm.reason" placeholder="请选择原因" style="width: 100%">
            <el-option label="拍错/多拍/不喜欢" value="拍错/多拍/不喜欢" />
            <el-option label="商品质量问题" value="商品质量问题" />
            <el-option label="商品破损/污渍" value="商品破损/污渍" />
            <el-option label="发货太慢" value="发货太慢" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="问题描述">
          <el-input 
            v-model="returnForm.description" 
            type="textarea" 
            rows="3" 
            placeholder="请详细描述您遇到的问题..."
          />
        </el-form-item>
        <el-form-item label="上传凭证">
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="fileList"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">最多上传3张图片</div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="returnDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReturn" :loading="submitLoading">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import frontRequest from '@/utils/frontRequest'

const route = useRoute()
const loading = ref(false)
const orderInfo = ref(null)
const returnList = ref([])

// 售后弹窗相关
const returnDialogVisible = ref(false)
const submitLoading = ref(false)
const currentProduct = ref(null)
const fileList = ref([])
const returnFormRef = ref(null)

const returnForm = ref({
  orderId: null,
  productId: null,
  type: 1, // 1: 仅退款, 2: 退货退款
  reason: '',
  description: '',
  images: []
})

// 格式化图片路径
const formatImg = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return 'http://localhost:80' + (url.startsWith('/') ? '' : '/') + url
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '-'
  const date = new Date(time)
  return date.toLocaleString('zh-CN', { hour12: false })
}

// 获取订单状态文本
const getStatusText = (status) => {
  const map = {
    0: '待付款',
    1: '待发货',
    2: '待收货',
    3: '已完成',
    4: '已取消'
  }
  return map[status] || '未知状态'
}

// 获取订单状态标签类型
const getStatusType = (status) => {
  const map = {
    0: 'warning',
    1: 'primary',
    2: 'success',
    3: 'success',
    4: 'info'
  }
  return map[status] || ''
}

// 获取售后状态文本
const getReturnStatusText = (status) => {
  const map = {
    0: '待审核',
    1: '同意',
    2: '拒绝',
    3: '已完成'
  }
  return map[status] || '未知'
}

// 获取售后状态标签类型
const getReturnStatusType = (status) => {
  const map = {
    0: 'warning',
    1: 'success',
    2: 'danger',
    3: 'info'
  }
  return map[status] || ''
}

// 加载订单详情
const loadOrderDetail = async () => {
  const orderId = route.query.orderId
  if (!orderId) {
    ElMessage.error('缺少订单ID')
    return
  }

  loading.value = true
  try {
    // 假设后端接口为 /front/order/detail/{id}
    const res = await frontRequest.get(`/order/detail/${orderId}`)
    if (res.code === 200) {
      orderInfo.value = res.data
      loadReturnHistory(orderId)
    } else {
      ElMessage.error(res.msg || '加载订单失败')
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('网络请求失败')
  } finally {
    loading.value = false
  }
}

// 加载售后历史
const loadReturnHistory = async (orderId) => {
  try {
    // 假设后端接口为 /front/return/list?orderId=xxx
    const res = await frontRequest.get('/return/list', { params: { orderId } })
    if (res.code === 200) {
      returnList.value = res.data || []
    }
  } catch (e) {
    console.error('加载售后记录失败', e)
  }
}

// 打开售后弹窗
const openReturnDialog = (product) => {
  currentProduct.value = product
  returnForm.value.orderId = orderInfo.value.id
  returnForm.value.productId = product.productId
  returnDialogVisible.value = true
}

// 重置表单
const resetReturnForm = () => {
  returnForm.value = {
    orderId: null,
    productId: null,
    type: 1,
    reason: '',
    description: '',
    images: []
  }
  fileList.value = []
  if (returnFormRef.value) returnFormRef.value.resetFields()
}

// 处理文件选择
const handleFileChange = (file, files) => {
  fileList.value = files.slice(0, 3) // 限制3张
}

// 提交售后申请
const submitReturn = async () => {
  if (!returnForm.value.reason) {
    ElMessage.warning('请选择售后原因')
    return
  }

  submitLoading.value = true
  try {
    // 这里通常需要先将图片上传到服务器获取URL，再提交表单
    // 简化处理：假设直接提交，实际项目中需先调用上传接口
    const formData = new FormData()
    formData.append('orderId', returnForm.value.orderId)
    formData.append('productId', returnForm.value.productId)
    formData.append('type', returnForm.value.type)
    formData.append('reason', returnForm.value.reason)
    formData.append('description', returnForm.value.description || '')
    
    // 如果有图片，添加到 formData
    fileList.value.forEach(file => {
      formData.append('files', file.raw)
    })

    const res = await frontRequest.post('/return/apply', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })

    if (res.code === 200) {
      ElMessage.success('申请提交成功')
      returnDialogVisible.value = false
      loadReturnHistory(orderInfo.value.id) // 刷新售后列表
    } else {
      ElMessage.error(res.msg || '申请失败')
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('提交失败，请稍后重试')
  } finally {
    submitLoading.value = false
  }
}

// 查看售后详情（可以跳转到专门页面或弹窗）
const viewReturnDetail = (row) => {
  ElMessage.info(`查看售后单 ${row.id} 的详情`)
  // 实际项目中可以 router.push 到详情页
}

onMounted(() => {
  loadOrderDetail()
})
</script>

<style scoped>
.order-detail-return-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.breadcrumb {
  margin-bottom: 20px;
}

.content-wrapper {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  font-size: 16px;
}

.order-meta {
  display: flex;
  flex-direction: column;
  gap: 10px;
  font-size: 14px;
  color: #606266;
}

.meta-item .label {
  color: #909399;
  width: 80px;
  display: inline-block;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #ebeef5;
}

.product-item:last-child {
  border-bottom: none;
}

.product-info {
  flex: 1;
  margin-left: 15px;
}

.product-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.product-spec {
  font-size: 12px;
  color: #909399;
  margin-bottom: 5px;
}

.product-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 150px;
}

.price {
  color: #ff6b00;
  font-weight: bold;
  font-size: 16px;
}

.count {
  color: #909399;
  font-size: 12px;
}

.total-section {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding-top: 15px;
  font-size: 14px;
}

.total-label {
  margin-right: 10px;
  color: #606266;
}

.total-price {
  color: #ff6b00;
  font-size: 20px;
  font-weight: bold;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
</style>