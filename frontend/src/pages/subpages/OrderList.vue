<template>
  <div class="page-container">
    <div class="header"><h2>订单管理中心</h2></div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="orderNo" label="订单号" width="180" align="center" />
      <el-table-column prop="userId" label="用户ID" width="100" align="center" />
      <el-table-column label="支付总额" width="120" align="center">
        <template #default="{row}">￥{{ row.payAmount }}</template>
      </el-table-column>
      <el-table-column label="状态" width="120" align="center">
        <template #default="{row}"><el-tag type="success">{{ row.status }}</el-tag></template>
      </el-table-column>
      <el-table-column label="创建时间" min-width="180" align="center">
        <template #default="{row}">{{ new Date(row.createdAt).toLocaleString() }}</template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template #default="scope">
          <el-button size="small" type="danger" link @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination v-model:current-page="page" :total="total" layout="total, prev, pager, next" @current-change="loadData" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import adminRequest from '@/utils/adminRequest'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([]); const total = ref(0); const page = ref(1); const loading = ref(false)

const loadData = async () => {
  loading.value = true
  // 关键修复：确保路径与 Controller 一致
  const res = await adminRequest.get('/order/admin/list', { params: { page: page.value, size: 10 } })
  
  // 核心兼容逻辑
  const result = res.data || res
  tableData.value = result.list || []
  total.value = result.total || 0
  loading.value = false
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除此订单？').then(async () => {
    await adminRequest.delete(`/order/admin/${id}`)
    loadData()
  })
}

onMounted(loadData)
</script>

<style scoped>
.page-container { padding: 25px; background: #fff; min-height: 100%; border-radius: 12px; }
.header { margin-bottom: 20px; border-bottom: 1px solid #f0f0f0; padding-bottom: 15px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>