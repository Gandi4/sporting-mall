<template>
  <div class="admin-box">
    <div class="header">
      <h2>售后申请处理中心</h2>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      
      <!-- 申请人 -->
      <el-table-column prop="username" label="申请人" width="100" />

      <el-table-column prop="orderId" label="订单ID" width="100" />
      
      <el-table-column label="售后" min-width="220">
        <template #default="{row}">
          <div style="display:flex; align-items:center; gap:10px">
            <el-image 
              :src="formatImg(row.productImage)" 
              style="width:45px;height:45px;border-radius:4px;flex-shrink:0" 
              fit="cover"
            >
              <template #error><div class="img-err">无图</div></template>
            </el-image>
            <span style="line-height: 1.4">{{ row.productName }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="reason" label="申请原因" show-overflow-tooltip />
      
      <el-table-column label="状态" width="100" align="center">
        <template #default="{row}">
          <el-tag :type="['warning','success','danger','info'][row.status]">
            {{ ['待审核','已通过','被拒绝','已完成'][row.status] || '未知' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="160" align="center">
        <template #default="{row}">
          <!-- 只有待审核(0)状态才显示操作按钮 -->
          <div v-if="row.status === 0" style="display:flex; gap:5px; justify-content:center">
            <el-button type="success" size="small" @click="handleApprove(row.id)">通过</el-button>
            <el-button type="danger" size="small" @click="handleReject(row.id)">拒绝</el-button>
          </div>
          <span v-else style="color:#999; font-size:12px">流程已结束</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination 
        v-model:current-page="page" 
        :page-size="size" 
        :total="total" 
        layout="total, prev, pager, next" 
        @current-change="loadData" 
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import adminRequest from '@/utils/adminRequest'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([]); const total = ref(0); const page = ref(1); const size = ref(10); const loading = ref(false)

/**
 * 智能处理图片路径 (解决 CORB 404 问题)
 */
const formatImg = (url) => {
  if (!url) return ''
  // 1. 如果是网络图，直接返回
  if (url.startsWith('http')) return url
  // 2. 如果是本地上传图，拼后端地址
  const path = url.startsWith('/') ? url : '/' + url
  return 'http://localhost:80' + path
}

/**
 * 加载数据
 */
const loadData = async () => {
  loading.value = true
  try {
    // 路径修正为 /return/list
    const res = await adminRequest.get('/return/list', { 
      params: { page: page.value, size: size.value } 
    })
    const data = res.data || res
    tableData.value = data.list || []
    total.value = data.total || 0
  } catch (e) {
    ElMessage.error('加载售后列表失败')
  } finally {
    loading.value = false
  }
}

/** 1. 通过审核 **/
const handleApprove = async (id) => {
  ElMessageBox.confirm('确定通过此项售后申请吗？', '审核确认', { type: 'success' })
    .then(async () => {
      const res = await adminRequest.post(`/return/approve/${id}`)
      if (res.code === 200) {
        ElMessage.success('已审核通过')
        loadData()
      }
    })
}

/** 2. 拒绝申请：弹窗获取理由并发送 **/
const handleReject = (id) => {
  ElMessageBox.prompt('请输入拒绝理由', '驳回申请', {
    confirmButtonText: '确定驳回',
    cancelButtonText: '取消',
    inputPlaceholder: '请填写具体理由'
  }).then(async ({ value }) => {
    // 发送 POST 请求，Body 带上理由
    const res = await adminRequest.post(`/return/reject/${id}`, { reason: value })
    if (res.code === 200) {
      ElMessage.warning('已驳回该申请')
      loadData()
    }
  })
}

onMounted(loadData)
</script>

<style scoped>
.admin-box { padding: 25px; background: #fff; min-height: 100%; border-radius: 12px; }
.header { margin-bottom: 20px; border-bottom: 1px solid #f0f0f0; padding-bottom: 15px; }
.header h2 { font-size: 20px; color: #333; margin: 0; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
.img-err { background: #f5f7fa; width: 45px; height: 45px; display: flex; align-items: center; justify-content: center; font-size: 11px; color: #999; border-radius: 4px; border: 1px solid #eee; }


:deep(.el-table__header) {
  background-color: #fafafa;
}
:deep(.el-button--success) {
  background-color: #67c23a;
}
</style>