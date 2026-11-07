<template>
  <div class="page-wrapper">
    <div class="cart-container">
      <h2 class="title">我的购物车</h2>
      <el-table :data="cartList" v-if="cartList.length > 0">
        <el-table-column label="商品">
          <template #default="{ row }">
            <div class="p-cell">
              <img :src="row.productImage" class="p-img" />
              <span>{{ row.productName }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="单价" prop="price" width="120" />
        <el-table-column label="合计" width="120">
          <template #default="{ row }">￥{{ row.subtotal }}</template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="danger" link @click="del(row.cartId)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="footer" v-if="cartList.length > 0">
        <span class="total">合计：<b>￥{{ total }}</b></span>
        <el-button class="buy-btn" @click="$router.push('/checkout')">去结算</el-button>
      </div>
      
      <el-empty v-else description="购物车是空的" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import frontRequest from '@/utils/frontRequest'
import { ElMessage } from 'element-plus'

const cartList = ref([])

/**
 * 加载购物车列表
 */
const load = async () => {
  const userStr = sessionStorage.getItem('user')
  if (!userStr) {
    ElMessage.warning('请先登录以查看购物车')
    return
  }
  
  const user = JSON.parse(userStr)
  try {
    // 调用后端接口获取指定用户的购物车
    const res = await frontRequest.get(`/cart/list/${user.id}`)
    if (res.code === 200) {
      cartList.value = res.data || []
    }
  } catch (e) {
    console.error('Fetch Cart Error:', e)
    ElMessage.error('获取购物车列表失败')
  }
}

/**
 * 删除购物车项
 * @param cartId 对应数据库的主键ID
 */
const del = async (cartId) => {
  const userStr = sessionStorage.getItem('user')
  if (!userStr) {
    ElMessage.warning('请先登录')
    return
  }
  const user = JSON.parse(userStr)
  
  try {
    // 这里补全了后端接口要求的 userId 参数
    const res = await frontRequest.delete(`/cart/delete/${cartId}?userId=${user.id}`)
    if (res.code === 200) {
      ElMessage.success('移除成功')
      load() // 重新刷新列表
    } else {
      ElMessage.error(res.msg || '移除失败')
    }
  } catch (e) {
    console.error('Delete Cart Error:', e)
    ElMessage.error('操作异常')
  }
}

const total = computed(() => {
  return cartList.value.reduce((s, i) => s + (Number(i.subtotal) || 0), 0).toFixed(2)
})

onMounted(load)
</script>

<style scoped>
.page-wrapper { background: #f5f5f5; min-height: 100vh; padding: 40px; }
.cart-container { max-width: 1000px; margin: 0 auto; background: #fff; padding: 30px; border-radius: 16px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
.title { font-size: 24px; margin-bottom: 20px; color: #ff6b00; font-weight: bold; }
.p-cell { display: flex; align-items: center; gap: 15px; }
.p-img { width: 60px; height: 60px; object-fit: cover; border-radius: 8px; border: 1px solid #eee; }
.footer { display: flex; justify-content: flex-end; align-items: center; margin-top: 30px; gap: 20px; border-top: 1px solid #eee; padding-top: 20px; }
.total b { color: #f56c6c; font-size: 26px; margin-left: 10px; }
.buy-btn { background: #ff6b00; color: #fff; border: none; padding: 12px 40px; border-radius: 25px; font-weight: bold; font-size: 16px; transition: all 0.3s; }
.buy-btn:hover { background: #e66000; transform: scale(1.05); }
</style>