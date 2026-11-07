<template>
  <header class="header">
    <!-- 1. Logo -->
    <div class="logo" @click="$router.push('/')">SPORTING</div>

    <!-- 2. 分类导航 -->
    <div class="nav-categories">
      <div class="nav-item" @click="$router.push('/')">首页</div>
      <div 
        v-for="cat in categories.slice(0, 5)" 
        :key="cat.id" 
        class="nav-item"
        @click="selectCategory(cat.id, cat.name)"
      >
        {{ cat.name }}
      </div>
      
      <el-dropdown v-if="categories.length > 5" trigger="hover">
        <div class="nav-item more-trigger">
          更多 <el-icon><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item 
              v-for="cat in categories.slice(5)" 
              :key="cat.id"
              @click="selectCategory(cat.id, cat.name)"
            >
              {{ cat.name }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <!-- 3. 右侧操作区 -->
    <div class="actions">
      
      <!-- 境界标签 -->
      <div 
        v-if="user && user.id" 
        class="realm-tag-btn" 
        :class="{ 'is-svip': user.isVip === 1 }" 
        @click="$router.push('/vip-center')"
      >
        <el-icon><StarFilled /></el-icon>
        <span>{{ user.currentLevel || '灵胎' }}</span>
      </div>

      <!-- 搜索框 -->
      <div class="search-area">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索装备..."
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch" class="search-confirm-btn">
              <el-icon color="#fff" :size="18"><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>
      
      <div class="nav-btn-link" @click="$router.push('/announcement')">
        <el-icon><Notification /></el-icon>
        <span>公告</span>
      </div>

      <el-icon size="22" class="icon-btn" @click="$router.push('/cart')">
        <ShoppingCart />
      </el-icon>

      <div v-if="!user || !user.id" class="login-entry">
        <el-button 
          type="primary" 
          class="pure-orange-btn" 
          @click.stop="handleLoginClick"
        >
          登录
        </el-button>
      </div>

      <!-- 已登录状态 -->
      <el-dropdown v-else placement="bottom-end" trigger="hover">
        <div class="user-entry" @click="$router.push('/profile')">
          <el-avatar :size="32" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
          <span class="uname">{{ user.username }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/profile')">个人中心</el-dropdown-item>
            <el-dropdown-item divided @click="handleLogout" style="color: #f56c6c">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, ShoppingCart, StarFilled, Notification, ArrowDown } from '@element-plus/icons-vue'
import frontRequest from '@/utils/frontRequest'

const router = useRouter()
const searchKeyword = ref('')
const categories = ref([])
const user = ref({})

// 解析函数封装，防止 JSON 报错导致 script 挂掉
const syncUser = () => {
  try {
    const userData = sessionStorage.getItem('user')
    user.value = userData ? JSON.parse(userData) : {}
  } catch (e) {
    user.value = {}
  }
}



const loadCategories = async () => {
  try {
    const res = await frontRequest.get('/category/list') 
    const list = res.data || res || []
    categories.value = list.filter(c => c.parentId === null)
  } catch (e) {
    console.error('导航分类加载失败')
  }
}

const handleSearch = () => {
  if (!searchKeyword.value.trim()) return
  router.push({ path: '/search', query: { keyword: searchKeyword.value } })
}

const selectCategory = (id, name) => {
  router.push({ path: '/', query: { categoryId: id, categoryName: name } })
}

/**
 * 明确跳转逻辑
 */
const handleLoginClick = () => {
  console.log("执行登录跳转")
  router.push('/login').catch(err => {
    if (err.name !== 'NavigationDuplicated') console.error(err)
  })
}

const handleLogout = () => {
  sessionStorage.removeItem('user')
  user.value = {}
  location.href = '/'
}

onMounted(() => {
  syncUser()
  loadCategories()
  window.addEventListener('storage', syncUser)
})

onUnmounted(() => {
  window.removeEventListener('storage', syncUser)
})
</script>

<style scoped>
.header {
  height: 70px;
  display: flex;
  align-items: center;
  padding: 0 40px;
  background: #fff;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 2000; /* 调高 z-index */
  box-shadow: 0 2px 10px rgba(0,0,0,0.03);
}

.logo {
  font-size: 26px;
  font-weight: 900;
  cursor: pointer;
  color: #1a1a1a;
  margin-right: 40px;
}

.nav-categories {
  flex: 1;
  display: flex;
  gap: 22px;
  align-items: center;
}

.nav-item {
  cursor: pointer;
  font-size: 15px;
  font-weight: 500;
  color: #555;
  white-space: nowrap;
}
.nav-item:hover { color: #ff6b00; }

.actions {
  display: flex;
  align-items: center;
  gap: 20px;
  pointer-events: auto; 
}

/* 境界标签 */
.realm-tag-btn {
  padding: 5px 15px;
  border-radius: 20px;
  background: #1a1a1a;
  color: #ffd700;
  font-size: 12px;
  font-weight: bold;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.search-area { width: 220px; }
:deep(.el-input-group__append) {
  background-color: #ff6b00 !important;
  border: none;
  cursor: pointer;
  border-radius: 0 20px 20px 0;
  padding: 0 15px;
}
:deep(.el-input__wrapper) {
  border-radius: 20px 0 0 20px;
  background: #f5f5f5;
  box-shadow: none !important;
}

.nav-btn-link { color: #ff6b00; font-weight: bold; cursor: pointer; font-size: 14px; display: flex; align-items: center; gap: 4px;}
.icon-btn { cursor: pointer; color: #333; }

.user-entry { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.uname { font-size: 14px; font-weight: 500; color: #333; }

/* 登录按钮样式优化：确保 cursor 属性 */
.pure-orange-btn {
  background: #ff6b00 !important;
  border: none !important;
  border-radius: 20px;
  padding: 8px 22px;
  font-weight: bold;
  cursor: pointer !important;
}
.pure-orange-btn:hover {
  background: #ff8533 !important;
  transform: translateY(-1px);
}
</style>