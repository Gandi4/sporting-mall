<template>
  <div class="search-page">
    <div class="search-container">
      <div class="search-header">
        <div class="result-info">
          找到关于 “<span class="keyword">{{ keyword }}</span>” 的结果 ({{ products.length }})
        </div>
        <div class="sort-bar">
          <span class="label">排序：</span>
       
          <el-radio-group v-model="sort" size="small" @change="loadProducts">
            <el-radio-button value="">默认</el-radio-button>
            <el-radio-button value="priceAsc">价格低到高</el-radio-button>
            <el-radio-button value="priceDesc">价格高到低</el-radio-button>
          </el-radio-group>
        </div>
      </div>

      <div class="result-body">
        <ProductGrid :products="products" :title="'搜索结果'" />
        <el-empty v-if="products.length === 0" description="可莉不知道哦" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import frontRequest from '@/utils/frontRequest'
import ProductGrid from '@/components/ProductGrid.vue'

const route = useRoute()
const products = ref([])
const keyword = ref('')
const sort = ref('')

const loadProducts = async () => {
  keyword.value = route.query.keyword || ''
  try {
    const res = await frontRequest.get('/product/list', {
      params: { keyword: keyword.value, sort: sort.value }
    })
    if (res.code === 200) products.value = res.data || []
  } catch (e) { }
}

watch(() => route.query.keyword, () => loadProducts())
onMounted(loadProducts)
</script>

<style scoped>
.search-page { background: #f8f9fa; min-height: 100vh; padding: 30px 0; }
.search-container { max-width: 1200px; margin: 0 auto; padding: 0 20px; }
.search-header { 
  display: flex; justify-content: space-between; align-items: center; 
  background: #fff; padding: 15px 25px; border-radius: 8px; margin-bottom: 25px;
}
.keyword { color: #ff6b00; font-weight: bold; }
:deep(.el-radio-button__orig-radio:checked + .el-radio-button__inner) {
  background-color: #ff6b00; border-color: #ff6b00; box-shadow: -1px 0 0 0 #ff6b00;
}
</style>