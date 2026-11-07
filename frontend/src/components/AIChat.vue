<template>
  <div class="ai-chat-fixed">
    <div class="chat-fab" @click="toggleChat">
      <el-icon :size="30"><ChatDotRound /></el-icon>
    </div>

    <transition name="el-zoom-in-bottom">
      <el-card v-if="visible" class="chat-card" :body-style="{ height: '100%', display: 'flex', flexDirection: 'column', padding: '0' }">
        <template #header>
          <div class="card-header">
            <span class="header-title">SPORTING 定制版伊涅夫</span>
            <el-icon class="close-btn" @click="visible = false"><Close /></el-icon>
          </div>
        </template>

        <div class="quick-actions">
          <el-button size="small" round @click="sendQuick('帮我查询最近的订单')">查询订单</el-button>
          <el-button size="small" round @click="sendQuick('有什么适合新手的体育用品？')">新手推荐</el-button>
          <el-button size="small" round @click="sendQuick('讲个关于体育的冷笑话')">讲个冷笑话</el-button>
        </div>

        <div class="chat-body" ref="bodyRef">
          <div v-for="(msg, i) in history" :key="i" :class="['msg-row', msg.role]">
            <el-avatar v-if="msg.role === 'ai'" :size="35" :src="ineffaImg" class="ai-avatar" />
            <div class="bubble">{{ msg.text }}</div>
          </div>
        </div>

        <div class="card-footer">
          <el-input v-model="input" placeholder="有什么可以为你做的吗..." @keyup.enter="handleSend" :disabled="loading" />
          <el-button class="send-btn" @click="handleSend" :loading="loading">发送</el-button>
        </div>
      </el-card>
    </transition>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { ChatDotRound, Close } from '@element-plus/icons-vue'
import frontRequest from '@/utils/frontRequest'
import ineffaImg from '@/assets/ineffa.png'

const visible = ref(false)
const input = ref('')
const loading = ref(false)
const bodyRef = ref(null)
const history = ref([{ role: 'ai', text: '有什么可以为你做的吗？' }])

const toggleChat = () => visible.value = !visible.value
const sendQuick = (msg) => { input.value = msg; handleSend() }

const handleSend = async () => {
  const message = input.value.trim()
  if (!message || loading.value) return
  history.value.push({ role: 'user', text: message })
  input.value = ''
  loading.value = true
  
  try {
    const user = JSON.parse(sessionStorage.getItem('user') || '{}')
    const res = await frontRequest.post('/ai/chat', { message, userId: user.id || null, mode: 'ineffa' })
    // 逻辑：优先取 data，没有取 msg
    const answer = res.data || res.msg || "伊涅夫正在执行任务，但未获取到有效返回。"
    history.value.push({ role: 'ai', text: answer })
  } catch (e) {
    history.value.push({ role: 'ai', text: '通信模块波动，伊涅夫正在重连中...' })
  } finally {
    loading.value = false
    nextTick(() => { if (bodyRef.value) bodyRef.value.scrollTop = bodyRef.value.scrollHeight })
  }
}
</script>

<style scoped>
.ai-chat-fixed { position: fixed; right: 30px; bottom: 30px; z-index: 9999; }
.chat-fab { width: 65px; height: 65px; background: rgba(255, 255, 255, 0.4); backdrop-filter: blur(20px); border-radius: 20px; display: flex; align-items: center; justify-content: center; cursor: pointer; box-shadow: 0 8px 20px rgba(0,0,0,0.1); }

.chat-card { width: 400px; height: 580px; position: absolute; bottom: 80px; right: 0; border-radius: 30px; background: rgba(255, 255, 255, 0.7); backdrop-filter: blur(40px); border: 1px solid rgba(255, 255, 255, 0.5); display: flex; flex-direction: column; overflow: hidden; }

.card-header { padding: 20px; display: flex; justify-content: space-between; align-items: center; border-bottom: none; }
.header-title { font-weight: bold; color: #333; }
.quick-actions { padding: 0 20px 10px; display: flex; gap: 8px; flex-shrink: 0; }

.chat-body { flex: 1; overflow-y: auto; padding: 20px; }
.msg-row { display: flex; gap: 10px; margin-bottom: 15px; }
.msg-row.user { flex-direction: row-reverse; }

/* 修正头像大小，防止变形 */
.ai-avatar { flex-shrink: 0; background: transparent; }

.bubble { padding: 12px 16px; border-radius: 18px; background: rgba(255, 255, 255, 0.6); color: #333; font-size: 14px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); }
.user .bubble { background: #007AFF; color: white; }

.card-footer { height: 75px; padding: 0 20px 20px 20px; flex-shrink: 0; display: flex; align-items: center; gap: 10px; }

:deep(.el-input__wrapper) { height: 44px; border-radius: 22px; background: rgba(255, 255, 255, 0.8) !important; box-shadow: none !important; border: 1px solid rgba(0,0,0,0.08); }
.send-btn { height: 44px; border-radius: 22px; padding: 0 20px !important; background: #fff !important; border: 1px solid rgba(0,0,0,0.08) !important; color: #333 !important; }

/* 毛玻璃滚动条 */
.chat-body::-webkit-scrollbar { width: 6px; }
.chat-body::-webkit-scrollbar-thumb { background: rgba(0,0,0,0.1); border-radius: 10px; backdrop-filter: blur(5px); }
</style>