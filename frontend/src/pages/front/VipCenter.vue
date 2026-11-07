<template>
  <div class="vip-center-root">
    <!-- 极光装饰背景层 -->
    <div class="night-sky-bg">
      <div class="radial-glow"></div>
      <div class="dot-grid"></div>
    </div>

    <div class="center-container">
      <div class="page-header">
        <h1 class="title-text">SPORTING 证道中心</h1>
        <p class="subtitle-text">等级越高 · 权益越多</p>
      </div>

      <!-- 1. 三大核心境界卡片 -->
      <div class="realm-flex">
        <div class="realm-card card-orange">
          <div class="tag">人体开发 (VIP)</div>
          <div class="realms">灵胎 · 五曜 · 六合 · 七星 · 天人 · 生死 · 神桥</div>
          <div class="bottom-desc">探索肉身极限，开启神藏之门</div>
        </div>
        
        <div class="realm-card card-gold active-god">
          <div class="tag gold-text">天宫神境 (SVIP)</div>
          <div class="realms realms-dark">尊神 · 真神 · 瑶池 · 斩神台 · 玉京 · 凌霄 · 帝座</div>
          <div class="bottom-desc">登临九天之巅，俯瞰芸芸众生</div>
        </div>

        <div class="realm-card card-red">
          <div class="tag">至高领域 (GOD)</div>
          <div class="realms god-title">天 庭</div>
          <div class="bottom-desc">自定世界规则</div>
        </div>
      </div>

      <!-- 2. 15 阶详细对照表 -->
      <div class="table-wrap">
        <h3 class="table-caption">15 阶修炼阶位及特权全览</h3>
        <el-table :data="tableData" class="merged-table">
          <el-table-column prop="level" label="阶位" width="80" align="center" />
          <el-table-column prop="name" label="境界" width="150" align="center">
             <template #default="{row}"><b :class="row.level > 7 ? 'highlight-gold' : 'highlight-dark'">{{ row.name }}</b></template>
          </el-table-column>
          <el-table-column prop="exp" label="所需修为" />
          <el-table-column prop="discount" label="购物折扣" align="center">
            <template #default="{row}"><span class="price-red">{{ row.discount }}</span></template>
          </el-table-column>
          <el-table-column prop="perks" label="每日经验增益" />
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// ✅ 关键修改：初始化 user 变量，从 sessionStorage 获取用户信息
const user = ref(JSON.parse(sessionStorage.getItem('user') || '{}'))

// 表格数据保持不变
const tableData = [
  { level: 1, name: '灵胎', exp: '0', discount: '10折', perks: '初窥门径', stage: 'mortal' },
  { level: 2, name: '五曜', exp: '100', discount: '9.8折', perks: '+15 Exp/日', stage: 'mortal' },
  { level: 3, name: '六合', exp: '1000', discount: '9.6折', perks: '+20 Exp/日', stage: 'mortal' },
  { level: 4, name: '七星', exp: '5000', discount: '9.4折', perks: '+25 Exp/日', stage: 'mortal' },
  { level: 5, name: '天人', exp: '1万', discount: '9.2折', perks: '+30 Exp/日', stage: 'mortal' },
  { level: 6, name: '生死', exp: '2万', discount: '9.0折', perks: '+40 Exp/日', stage: 'mortal' },
  { level: 7, name: '神桥', exp: '5万', discount: '8.8折', perks: '+60 Exp/日', stage: 'mortal' },

  { level: 8, name: '尊神', exp: '10万', discount: '8.5折', perks: '+100 Exp/日', stage: 'god' },
  { level: 9, name: '真神', exp: '30万', discount: '8.0折', perks: '+150 Exp/日', stage: 'god' },
  { level: 10, name: '瑶池', exp: '50万', discount: '7.5折', perks: '+200 Exp/日', stage: 'god' },
  { level: 11, name: '斩神台', exp: '80万', discount: '7.0折', perks: '+300 Exp/日', stage: 'god' },
  { level: 12, name: '玉京', exp: '100万', discount: '6.5折', perks: '+400 Exp/日', stage: 'god' },
  { level: 13, name: '凌霄', exp: '200万', discount: '6.0折', perks: '+600 Exp/日', stage: 'god' },
  { level: 14, name: '帝座', exp: '300万', discount: '5.5折', perks: '+800 Exp/日', stage: 'god' },

  { level: 15, name: '天庭', exp: '500万', discount: '5.0折', perks: '+1200 Exp/日', stage: 'world' }
]
</script>

<style scoped>
/* 强制背景一致性 */
:global(body) {
  margin: 0;
  background-color: #fff;
}

.vip-center-root {
  min-height: 100vh;
  width: 100%;
  position: relative;
  /* 核心修改：从深色到白色的渐变 */
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 35%, #f8fafc 70%, #ffffff 100%);
  color: #1e293b;
  padding-bottom: 100px;
}

/* 顶部装饰，只保留在上方 */
.night-sky-bg { 
  position: absolute; 
  top: 0; left: 0; right: 0; 
  height: 600px; 
  pointer-events: none; 
  overflow: hidden;
}
.radial-glow {
  position: absolute;
  top: -150px; left: 50%;
  transform: translateX(-50%);
  width: 100%; height: 500px;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.2) 0%, transparent 70%);
}
.dot-grid {
  position: absolute; inset: 0;
  background-image: radial-gradient(rgba(255,255,255,0.05) 1px, transparent 1px);
  background-size: 40px 40px;
}

.center-container { position: relative; z-index: 1; max-width: 1200px; margin: 0 auto; padding: 80px 20px 0; }

.page-header { text-align: center; margin-bottom: 60px; }
/* 标题颜色调亮，以在深色顶部显示 */
.title-text { font-size: 52px; font-weight: 900; letter-spacing: 8px; color: #ffffff; text-shadow: 0 4px 12px rgba(0,0,0,0.3); margin: 0; }
.subtitle-text { font-size: 18px; color: rgba(255,255,255,0.6); letter-spacing: 4px; margin-top: 15px; }

/* --- 卡片部分 --- */
.realm-flex { display: flex; gap: 25px; margin-bottom: 80px; }
.realm-card {
  flex: 1;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 24px;
  padding: 50px 30px;
  text-align: center;
  box-shadow: 0 10px 25px rgba(0,0,0,0.05);
  transition: 0.3s;
}
.realm-card:hover { transform: translateY(-8px); box-shadow: 0 20px 40px rgba(0,0,0,0.1); background: #fff; }

.tag { font-size: 13px; font-weight: bold; margin-bottom: 25px; color: #3b82f6; }
.realms { font-size: 15px; line-height: 2.2; color: #475569; margin-bottom: 20px; }
.realms-dark { color: #1e293b; font-weight: 500; }
.bottom-desc { font-size: 12px; color: #94a3b8; }

.card-gold { background: linear-gradient(135deg, #fffcf0 0%, #fff9e6 100%); border: 1px solid #fde68a; }
.gold-text { color: #b45309 !important; }
.god-title { font-size: 36px !important; font-weight: 900; color: #1e293b; margin: 10px 0; }

/* --- 表格部分 --- */
.table-wrap {
  padding: 40px;
  border-radius: 24px;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 20px rgba(0,0,0,0.03);
}
.table-caption { font-size: 20px; margin-bottom: 30px; font-weight: 600; color: #0f172a; text-align: center; }

:deep(.merged-table) {
  background-color: transparent !important;
  --el-table-header-bg-color: #f8fafc;
  --el-table-border-color: #f1f5f9;
  --el-table-text-color: #475569;
}
:deep(.el-table th.el-table__cell) { color: #0f172a; font-weight: 700; }

.highlight-gold { color: #d97706; }
.highlight-dark { color: #1e293b; font-weight: 600; }
.price-red { color: #ef4444; font-weight: bold; }
</style>