<!-- src/pages/subpages/CategoryList.vue -->
<template>
  <div class="category-list">
    <h2>商品分类</h2>

    <div class="page-header">
      <el-button type="primary" @click="handleAdd">新增分类</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" min-width="200" />
      <el-table-column prop="sort" label="排序" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="page.current"
        v-model:page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      :title="form.id ? '编辑分类' : '新增分类'"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import adminRequest from '@/utils/adminRequest'

const tableData = ref([])
const loading = ref(false)
const page = reactive({ current: 1, size: 10, total: 0 })
const dialogVisible = ref(false)
const formRef = ref()
const form = reactive({ id: null, name: '', sort: 0, status: 1 })

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
}

const getList = async () => {
  loading.value = true
  try {
    const res = await adminRequest.get('/category/list', {
      params: {
        page: page.current,
        size: page.size
      }
    })


    if (res.code === 200) {
      tableData.value = res.data?.list || []
      page.total = res.data?.total || 0
    }
  } catch (err) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  form.id = null
  form.name = ''
  form.sort = 0
  form.status = 1
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return

    const url = form.id ? '/category/update' : '/category/add'
    const res = await adminRequest.post(url, form)

    if (res.code === 200) {
      ElMessage.success(form.id ? '修改成功' : '添加成功')
      dialogVisible.value = false
      getList()
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该分类吗？', '提示', { type: 'warning' })
    .then(async () => {
      const res = await adminRequest.post('/category/delete', { id: row.id })
      if (res.code === 200) {
        ElMessage.success('删除成功')
        getList()
      }
    })
}

const handleSizeChange = (val) => {
  page.size = val
  getList()
}

const handleCurrentChange = (val) => {
  page.current = val
  getList()
}

onMounted(getList)
</script>


<style scoped>
.category-list { padding: 20px; }
.page-header { margin-bottom: 20px; text-align: right; }
.pagination { margin-top: 20px; text-align: right; }
</style>