<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="title">轮播图管理</h2>
      <!-- 点击按钮打开弹窗 -->
      <el-button type="primary" icon="Plus" @click="openAddDialog">新增轮播图</el-button>
    </div>

    <el-table :data="list" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="title" label="标题" />
      <el-table-column label="预览图" align="center" width="300">
        <template #default="scope">
          <el-image 
            :src="formatImg(scope.row.imageUrl)" 
            style="width:240px; height:90px; border-radius:4px" 
            fit="cover"
          >
            <template #error><div class="err-box">链接失效</div></template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template #default="scope">
          <!-- 新增编辑按钮 -->
          <el-button size="small" type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
          <!-- 删除按钮保持不变 -->
          <el-button size="small" type="danger" link @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 复用弹窗：新增/编辑 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑轮播图' : '新增轮播图'"
      width="500px"
      @close="resetForm"
    >
      <el-form :model="form" label-width="80px" ref="formRef" :rules="rules">
        <el-form-item label="自定义标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入轮播图标题" />
        </el-form-item>

        <el-form-item label="图片来源">
          <el-radio-group v-model="imageSource">
            <el-radio label="local">本地文件</el-radio>
            <el-radio label="network">网络地址</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 本地上传 -->
        <el-form-item v-if="imageSource === 'local'" label="上传图片">
          <el-upload
            class="banner-uploader"
            action="http://localhost:80/api/banner/upload"
            name="file"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <img v-if="form.imageUrl" :src="formatImg(form.imageUrl)" class="preview-img" />
            <el-icon v-else class="uploader-icon"><Plus /></el-icon>
          </el-upload>
          <div class="tip">支持 jpg/png，不超过 2MB</div>
        </el-form-item>

        <!-- 网络地址 -->
        <el-form-item v-else label="链接地址" prop="imageUrl">
          <el-input v-model="form.imageUrl" placeholder="请粘贴图片URL（如 https://...）" />
          <div class="net-preview" v-if="form.imageUrl">
            <p>预览：</p>
            <img :src="form.imageUrl" class="preview-img" />
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确定保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import adminRequest from '@/utils/adminRequest'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

// 数据与状态
const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const isEdit = ref(false) // 控制是新增还是编辑
const imageSource = ref('local')
const formRef = ref(null)

// 表单数据（必须包含 id 字段用于更新）
const form = reactive({
  id: null, // 必须增加此字段，后端才能知道更新哪一条
  title: '',
  imageUrl: '',
  sortOrder: 1,
  status: 1
})

// 校验规则
const rules = {
  title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请提供图片', trigger: 'change' }]
}

// 图片处理函数
const formatImg = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return 'http://localhost:80' + (url.startsWith('/') ? '' : '/') + url
}

// 加载列表
const loadList = async () => {
  loading.value = true
  try {
    const res = await adminRequest.get('/banner/list')
    list.value = res.data || res || []
  } finally {
    loading.value = false
  }
}

// 打开新增弹窗
const openAddDialog = () => {
  resetForm()
  dialogVisible.value = true
}

// 打开编辑弹窗
const handleEdit = (row) => {
  isEdit.value = true
  dialogVisible.value = true
  Object.assign(form, row) // 深拷贝当前行数据到表单
  // 判断图片来源
  imageSource.value = row.imageUrl.startsWith('http') ? 'network' : 'local'
}

// 删除操作
const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该轮播图吗？', '警告', { type: 'warning' }).then(async () => {
    const res = await adminRequest.delete(`/banner/delete/${id}`)
    if (res.code === 200) {
      ElMessage.success('已删除')
      loadList()
    }
  })
}

// 重置表单（清空或初始化）
const resetForm = () => {
  isEdit.value = false
  form.id = null
  form.title = ''
  form.imageUrl = ''
  imageSource.value = 'local'
  if (formRef.value) formRef.value.resetFields()
}

// 上传前校验
const beforeUpload = (file) => {
  const isType = ['image/jpeg', 'image/png', 'image/webp'].includes(file.type)
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isType) ElMessage.error('图片只能是 JPG/PNG/WebP 格式!')
  if (!isLt2M) ElMessage.error('图片大小不能超过 2MB!')
  return isType && isLt2M
}

// 上传成功回调
const handleUploadSuccess = (res) => {
  if (res.code === 200) {
    form.imageUrl = res.data // 后端返回的是生成的 URL
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(res.msg || '上传失败')
  }
}

// 提交表单（新增或编辑）
const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        // 根据 isEdit 决定调用 add 还是 update 接口
        const url = isEdit.value ? '/banner/update' : '/banner/add'
        const res = await adminRequest.post(url, form)

        if (res.code === 200) {
          ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
          dialogVisible.value = false
          loadList()
        }
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 页面加载时获取数据
onMounted(loadList)
</script>

<style scoped>
.page-container { padding: 20px; background: #fff; min-height: 400px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.title { font-size: 18px; font-weight: bold; border-left: 4px solid #409eff; padding-left: 10px; margin: 0; }

/* 上传组件样式 */
.banner-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 240px;
  height: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #fafafa;
}
.banner-uploader:hover { border-color: #409eff; }
.uploader-icon { font-size: 28px; color: #8c939d; }
.preview-img { width: 100%; height: 100%; object-fit: cover; }
.tip { font-size: 12px; color: #999; margin-top: 5px; }

.net-preview { margin-top: 10px; border-top: 1px solid #eee; padding-top: 10px; }
.net-preview p { margin: 0 0 5px 0; font-size: 12px; color: #666; }

.err-box { background: #f5f5f5; color: #999; height: 100%; display: flex; align-items: center; justify-content: center; font-size: 12px; }
</style>