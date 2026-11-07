<template>
  <div class="admin-box">
    <div class="header">
      <el-button type="primary" @click="getList">刷新列表</el-button>
      <el-button type="success" @click="handleAdd">新增</el-button>
    </div>

    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" clearable />
        </el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form>
    </el-card>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />

      <el-table-column label="图片" width="100">
        <template #default="{row}">
          <el-image
            v-if="formatImg(row.mainImage)"
            :src="formatImg(row.mainImage)"
            style="width:50px;height:50px;border-radius:6px"
            fit="cover"
            :preview-src-list="[formatImg(row.mainImage)]"
          />
          <div v-else class="err">无图</div>
        </template>
      </el-table-column>

      <el-table-column prop="name" label="商品名称" />

      <el-table-column label="现价">
        <template #default="{row}">
          <span class="price">￥{{row.price}}</span>
        </template>
      </el-table-column>

      <el-table-column prop="stock" label="库存" />

      <el-table-column label="状态">
        <template #default="{row}">
          <el-tag :type="row.status==='on_sale'?'success':'danger'">
            {{row.status==='on_sale'?'上架':'下架'}}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="200">
        <template #default="{row}">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" @click="toggle(row)">
            {{row.status==='on_sale'?'下架':'上架'}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="page.current"
      v-model:page-size="page.size"
      :total="page.total"
      layout="total, prev, pager, next"
      @current-change="getList"
    />

    <el-dialog v-model="dialogVisible" :title="form.id?'编辑':'新增'" width="500px" append-to-body>
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="form.price"/>
        </el-form-item>
        <el-form-item label="图片">
          <el-input v-model="form.main_image"/>
        </el-form-item>
        <el-form-item label="上架">
          <el-switch v-model="isOnSale"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, computed, onMounted} from 'vue'
import adminRequest from '@/utils/adminRequest'
import {ElMessage} from 'element-plus'

const tableData = ref([])
const loading = ref(false)

const page = reactive({current:1,size:10,total:0})
const searchForm = ref({name:''})

const dialogVisible = ref(false)
const formRef = ref()

const form = reactive({
  id:null,
  name:'',
  price:0,
  main_image:'',
  status:'off_sale'
})

const formatImg = (url)=>{
  if(!url) return ''
  if(url.startsWith('http')) return url
  return 'http://localhost:80' + (url.startsWith('/')?'':'/') + url
}

const getList = async ()=>{
  loading.value=true
  const res = await adminRequest.get('/product/list',{
    params:{page:page.current,size:page.size,name:searchForm.value.name}
  })
  tableData.value = res.data.list || res.data || []
  page.total = res.data.total || 0
  loading.value=false
}

const handleSearch = ()=>{page.current=1;getList()}
const resetSearch = ()=>{searchForm.value.name='';getList()}

const handleAdd = ()=>{
  Object.assign(form,{id:null,name:'',price:0,main_image:'',status:'off_sale'})
  dialogVisible.value=true
}

const handleEdit = (row)=>{
  Object.assign(form,row)
  dialogVisible.value=true
}

const submit = async ()=>{
  const url = form.id?'/product/update':'/product/add'
  const res = await adminRequest.post(url,form)
  if(res.code===200){
    ElMessage.success('成功')
    dialogVisible.value=false
    getList()
  }
}

const toggle = async (row)=>{
  const status = row.status==='on_sale'?'off_sale':'on_sale'
  await adminRequest.post('/product/toggleStatus',{id:row.id,status})
  getList()
}

const isOnSale = computed({
  get:()=>form.status==='on_sale',
  set:v=>form.status=v?'on_sale':'off_sale'
})

onMounted(getList)
</script>

<style scoped>
.admin-box { padding:20px; background:#fff }
.header { margin-bottom:20px; display:flex; gap:10px }
.search-card { margin-bottom:20px }
.price { color:#F56C6C; font-weight:bold }
.err,.img-err{
  width:50px;height:50px;background:#eee;
  display:flex;align-items:center;justify-content:center;
  font-size:12px;color:#999
}
</style>