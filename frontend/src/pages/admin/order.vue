<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="title">订单管理</h2>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="orderNo" label="订单号" width="180" align="center" />
      <el-table-column prop="userId" label="用户ID" width="100" align="center" />
      <el-table-column label="总金额" width="120" align="center">
        <template #default="{ row }"><span style="color:#f56c6c; font-weight:bold">￥{{ row.totalAmount }}</span></template>
      </el-table-column>
      <el-table-column label="订单状态" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 'paid' ? 'success' : 'warning'">
            {{ row.status === 'paid' ? '已支付' : '待处理' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" width="180" align="center">
        <template #default="{ row }">{{ new Date(row.createdAt).toLocaleString() }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center">
        <template #default="scope">
          <el-button size="small" type="primary" link @click="viewDetail(scope.row.id)">详情</el-button>
          <el-button size="small" type="danger" link @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

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

const tableData = ref([])
const page = ref(1); const size = ref(10); const total = ref(0); const loading = ref(false)

const loadData = async () => {
  loading.value = true
  try {
    // 路径：/order/admin/list
    const res = await adminRequest.get('/order/admin/list', { 
      params: { page: page.value, size: size.value } 
    })
    
    // 兼容逻辑：如果拦截器没剥离 data，则取 res.data；否则取 res 
    const result = res.data || res
    
    if (result && result.list) {
      tableData.value = result.list
      total.value = result.total
    } else if (Array.isArray(result)) {
      // 容错：如果后端直接返回数组
      tableData.value = result
      total.value = result.length
    }
  } catch (e) {
    console.error('订单加载失败', e)
  } finally {
    loading.value = false
  }
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该订单吗？').then(async () => {
    const res = await adminRequest.delete(`/order/admin/${id}`)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    }
  })
}

onMounted(loadData)
</script>

<style scoped>
.page-container { padding: 20px; background: #fff; min-height: 100%; border-radius: 8px; }
.page-header { margin-bottom: 25px; border-bottom: 1px solid #eee; padding-bottom: 15px; }
.title { font-size: 18px; font-weight: bold; border-left: 4px solid #ff6b00; padding-left: 12px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>