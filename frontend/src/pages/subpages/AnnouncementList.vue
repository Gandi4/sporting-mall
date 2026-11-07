```vue
<template>
  <div class="page">
    <div class="page-header">
      <span class="title">公告管理</span>
      <el-button type="primary" @click="handleAdd">新增公告</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="list" border style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="公告标题" />
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>

          <el-button
            size="small"
            :type="scope.row.status === 1 ? 'warning' : 'success'"
            @click="handleToggle(scope.row)"
          >
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
          </el-button>

          <el-button size="small" type="danger" @click="handleDelete(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑公告' : '新增公告'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>

        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" rows="4" />
        </el-form-item>

        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

```vue
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import adminRequest from '@/utils/adminRequest'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)

const form = ref({
  id: null,
  title: '',
  content: '',
  status: 1
})

// 获取列表
const loadList = async () => {
  loading.value = true
  try {
    const res = await adminRequest.get('/announcement/list')

    console.log('公告返回：', res) 

    if (res && res.code === 200) {
      list.value = res.data || []
    } else {
      ElMessage.error(res?.msg || '加载失败')
    }

  } catch (err) {
    console.error('公告错误：', err)
    ElMessage.error('接口请求失败')
  } finally {
    loading.value = false
  }
}

// 新增
const handleAdd = () => {
  form.value = { id: null, title: '', content: '', status: 1 }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  form.value = { ...row }
  dialogVisible.value = true
}

// 提交
const submitForm = async () => {
  if (!form.value.title) {
    ElMessage.warning('请输入标题')
    return
  }

  let res
  if (form.value.id) {
    res = await adminRequest.put('/announcement/update', form.value)
  } else {
    res = await adminRequest.post('/announcement/add', form.value)
  }

  if (res.code === 200) {
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadList()
  } else {
    ElMessage.error(res.msg || '操作失败')
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该公告吗？', '提示', { type: 'warning' })
    .then(async () => {
      const res = await adminRequest.delete(`/announcement/delete/${row.id}`)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        loadList()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    })
}

onMounted(() => {
  loadList()
})
</script>
```


<style scoped>
.page {
  padding: 10px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.title {
  font-size: 16px;
  font-weight: bold;
}
</style>
```
