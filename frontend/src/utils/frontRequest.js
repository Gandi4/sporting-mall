// src/utils/frontRequest.js
import axios from 'axios'
import { ElMessage } from 'element-plus'

const frontRequest = axios.create({
  baseURL: '/front', // 触发 vite.config.js 代理
  timeout: 30000,    //  30 秒超时时间
  withCredentials: true // 跨域支持
})

// 添加请求拦截器，自动携带 userId
frontRequest.interceptors.request.use(
  config => {
    const user = JSON.parse(sessionStorage.getItem('user') || '{}')
    if (user.userId) {
      config.headers['X-User-ID'] = user.userId
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器保持不变
frontRequest.interceptors.response.use(
  response => {
    const res = response.data

    // 如果返回 code 为 200，说明业务处理成功，直接返回数据
    if (res.code === 200) return res

    // 如果返回 code 为 401，说明登录失效
    if (res.code === 401) {
    
      sessionStorage.removeItem('user')
      window.location.href = '/login' 
      return Promise.reject(res)
    }

    // 其他业务错误弹出后端给的错误提示
    ElMessage.error(res.msg || '服务器接口异常')
    return Promise.reject(res)
  },
  error => {
    // 处理 HTTP 状态码错误
    if (error.response && error.response.status === 404) {
      ElMessage.error('找不到该资源(404)')
    } else if (error.code === 'ECONNABORTED') {
      // 针对 AI 客服特别优化的提示
      ElMessage.error('AI 响应太慢，请求已超时，请稍后再试')
    } else {
      ElMessage.error('网络连接失败，请检查后端服务是否正常运行')
    }
    return Promise.reject(error)
  }
)

export default frontRequest