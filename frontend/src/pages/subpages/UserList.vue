<template>
  <div class="page-container">
    <div class="header">
      <h2>用户及会员管理中心</h2>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column prop="phone" label="手机号" width="140" align="center" />
      
      <el-table-column label="修为等级" width="160" align="center">
        <template #default="{row}">
          <el-tag effect="dark" :type="row.isVip === 1 ? 'warning' : 'info'">
            {{ row.currentLevel || '灵胎' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="管理操作" width="220" align="center">
        <template #default="{row}">
          <el-button 
            size="small" 
            :type="row.isVip === 1 ? 'danger' : 'warning'" 
            plain
            @click="handleVipToggle(row)"
          >
            {{ row.isVip === 1 ? '削减修为' : '手动提拔' }}
          </el-button>
          <el-button size="small" type="danger" link @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
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
    const res = await adminRequest.get('/user/list', {
      params: { page: page.value, size: size.value }
    })
    const result = res.data || res
    tableData.value = result.list || []
    total.value = result.total || 0
  } catch (e) {
    console.error('加载失败', e)
  } finally {
    loading.value = false
  }
}

// 核心修复点：加入 .catch(() => {}) 捕获用户点击“取消”的操作
const handleVipToggle = (user) => {
  const target = user.isVip === 1 ? 0 : 1
  const text = target === 1 ? '提拔为会员' : '取消会员身份'
  
  ElMessageBox.confirm(
    `操作提醒：确认要为用户 "${user.username}" ${text} 吗？`,
    '系统提示',
    { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => {
    // 点击确定执行这里的逻辑
    const res = await adminRequest.post('/vip/admin/toggle', { userId: user.id, status: target })
    if (res.code === 200) {
      ElMessage.success('操作成功')
      loadData()
    }
  }).catch(() => {
    // 关键修复：点击取消或关闭弹窗会进入这里，不再报错
    console.log('用户取消了操作')
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm(
    '极其严肃的操作：确认彻底删除此用户账号？此操作不可逆！',
    '危险警告',
    { confirmButtonText: '狠心删除', cancelButtonText: '再想想', type: 'error' }
  ).then(async () => {
    const res = await adminRequest.delete(`/user/${id}`)
    if (res.code === 200) {
      ElMessage.success('已彻底删除该账号')
      loadData()
    }
  }).catch(() => {
    // 处理取消删除的情况
  })
}

onMounted(loadData)
</script>

<style scoped>
.page-container { padding: 25px; background: #fff; border-radius: 12px; }
.header { margin-bottom: 25px; border-bottom: 2px solid #f2f2f2; padding-bottom: 15px; }
.pagination { margin-top: 25px; display: flex; justify-content: flex-end; }
</style>