<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="title">评价管理</h2>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input 
        v-model="keyword" 
        placeholder="搜索商品名或用户名" 
        clearable 
        style="width: 300px; margin-right: 10px"
      />
      <el-button type="primary" @click="loadData">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="productId" label="商品ID" width="100" align="center" />
      <el-table-column prop="userId" label="用户ID" width="100" align="center" />
      
      <el-table-column label="评分" width="150" align="center">
        <template #default="{ row }">
          <el-rate v-model="row.rating" disabled />
        </template>
      </el-table-column>

      <el-table-column prop="content" label="评价内容" show-overflow-tooltip />

      <el-table-column label="操作" width="150" align="center">
        <template #default="scope">
          <el-button 
            size="small" 
            type="danger" 
            link 
            @click="handleDelete(scope.row.id)"
          >删除</el-button>
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

const tableData = ref([])
const page = ref(1); const size = ref(10); const total = ref(0)
const keyword = ref(''); const loading = ref(false)

const loadData = async () => {
  loading.value = true
  try {
    // 匹配 ReviewController: GET /api/review/list
    const res = await adminRequest.get('/review/list', {
      params: { page: page.value, size: size.value, keyword: keyword.value }
    })
    // 兼容拦截器返回 res.data 的情况
    const data = res.data || res
    tableData.value = data.list || []
    total.value = data.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  keyword.value = ''
  page.value = 1
  loadData()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这条评价吗？').then(async () => {
    const res = await adminRequest.post('/review/delete', { id })
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    }
  })
}

onMounted(loadData)
</script>

<style scoped>
.page-container { padding: 20px; background: #fff; border-radius: 8px; }
.page-header { margin-bottom: 20px; border-bottom: 1px solid #f0f0f0; padding-bottom: 15px; }
.title { font-size: 18px; font-weight: bold; border-left: 4px solid #409eff; padding-left: 10px; }
.search-bar { margin-bottom: 20px; background: #fdfdfd; padding: 15px; border: 1px solid #f0f0f0; border-radius: 4px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>