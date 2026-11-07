<template>
  <div class="announcement-page">
   

    <div class="announcement-container">
      <h2 class="page-title">平台公告</h2>

      <div v-if="loading" class="loading-tip">
        加载中...
      </div>

      <div v-else-if="announcements.length === 0" class="empty-tip">
        暂无公告
      </div>

      <div v-else class="announcement-list">
        <div v-for="item in announcements" :key="item.id" class="announcement-card">
          <div class="announcement-header">
            <span class="label">公告</span>
           <span class="time">{{ formatDate(item.publishAt) }}</span>
          </div>
          <h3 class="title">{{ item.title }}</h3>
          <div class="content" v-html="item.content || '暂无详细内容'"></div>
        </div>
      </div>
    </div>

    <div class="footer-placeholder"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import FrontHeader from '@/components/FrontHeader.vue'
import frontRequest from '@/utils/frontRequest'

const announcements = ref([])
const loading = ref(true)

const loadAnnouncements = async () => {
  try {
    // 前后端匹配的接口路径：/front/announcement/list
    const res = await frontRequest.get('/announcement/list')// console.log('公告接口完整响应：', res)          
console.log('拿到的 data：', res?.data)           


announcements.value = Array.isArray(res?.data) ? res.data : []  } catch (err) {
    console.error('公告加载失败', err)
  } finally {
    loading.value = false
  }
}



// 日期格式化（数据库 publish_at 字段）
const formatDate = (dateStr) => {
  if (!dateStr) return '未知时间';
  const date = new Date(dateStr);
  if (isNaN(date.getTime())) return '格式错误';

  const options = {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit' // 显示秒
  };

  return date.toLocaleString('zh-CN', options)
             .replace(/\//g, '-')  // 替换斜杠
             .replace(/年|月/g, '-') // 年月替换为 -
             .replace(/日/g, '');   // 去掉日
};

onMounted(() => {
  loadAnnouncements()
})
</script>

<style scoped>
.announcement-page {
  min-height: 100vh;
  background: #f8f8f8;
}

.announcement-container {
  width: 1200px;
  margin: 0 auto;
  padding: 20px 20px 40px;
}

.page-title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 32px;
  text-align: center;
}

.loading-tip,
.empty-tip {
  text-align: center;
  color: #999;
  font-size: 16px;
  padding: 120px 0;
}

.announcement-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.announcement-card {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  transition: all 0.2s;
}

.announcement-card:hover {
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  transform: translateY(-2px);
}

.announcement-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.label {
  color: #ff5000;
  font-weight: bold;
  font-size: 15px;
  background: #fff5f0;
  padding: 4px 10px;
  border-radius: 4px;
}

.time {
  color: #999;
  font-size: 14px;
}

.title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}

.content {
  font-size: 15px;
  line-height: 1.8;
  color: #555;
  white-space: pre-wrap; /* 保留换行和空格 */
}

.footer-placeholder {
  height: 80px;
}
</style>