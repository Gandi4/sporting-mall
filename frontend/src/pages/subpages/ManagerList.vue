<!-- src/pages/subpages/ManagerList.vue -->
<template>
  <div class="manager-list">
    <h2>管理员管理</h2>

    <!-- 操作栏 -->
    <div class="page-header">
      <el-button type="primary" @click="handleAdd">新增管理员</el-button>
    </div>

    <!-- 管理员表格 -->
    <el-table :data="tableData" v-loading="loading" border height="600">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="manager" label="用户名" min-width="150" />
      <el-table-column prop="nickname" label="昵称" min-width="150" />
      <el-table-column prop="role" label="角色" width="120" />
      <el-table-column prop="created_at" label="创建时间" width="180">
        <template #default="scope">
          {{ formatDate(scope.row.created_at) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="page.current"
        v-model:page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      :title="form.id ? '编辑管理员' : '新增管理员'"
      v-model="dialogVisible"
      width="500px"
      destroy-on-close
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="用户名" prop="manager">
          <el-input v-model="form.manager" placeholder="登录用户名" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="显示昵称" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-input v-model="form.role" placeholder="如：超级管理员" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="留空则不修改密码"
            show-password
          />
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

// 时间格式化
const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN')
}

const tableData = ref([])
const loading = ref(false)
const page = reactive({ current: 1, size: 10, total: 0 })
const dialogVisible = ref(false)
const formRef = ref()
const form = reactive({
  id: null,
  manager: '',
  nickname: '',
  role: '',
  password: ''
})

const rules = {
  manager: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }]
}

const getList = async () => {
  loading.value = true
  try {
    const res = await adminRequest.get('/manager/list', {
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
    ElMessage.error('加载管理员列表失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  form.id = null
  form.manager = ''
  form.nickname = ''
  form.role = ''
  form.password = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    manager: row.manager,
    nickname: row.nickname,
    role: row.role,
    password: '' // 编辑时密码留空
  })
  dialogVisible.value = true
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return

    const submitData = { ...form }
    if (!submitData.password) {
      delete submitData.password
    }

    const url = form.id ? '/manager/update' : '/manager/add'
    const res = await adminRequest.post(url, submitData)

    if (res.code === 200) {
      ElMessage.success(form.id ? '修改成功' : '添加成功')
      dialogVisible.value = false
      getList()
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定删除该管理员吗？删除后不可恢复！',
    '提示',
    { type: 'warning' }
  ).then(async () => {
    const res = await adminRequest.post('/manager/delete', { id: row.id })
    if (res.code === 200) {
      ElMessage.success('删除成功')
      getList()
    } else {
      ElMessage.error(res.msg || '删除失败')
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
.manager-list {
  padding: 20px;
  background: #f9fafc;
}

.page-header {
  margin-bottom: 20px;
  text-align: right;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>