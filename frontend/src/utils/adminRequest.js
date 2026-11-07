// src/utils/adminRequest.js
import axios from 'axios'

const adminRequest = axios.create({
  baseURL: '/api', // 相对路径， Vite 代理生效
  timeout: 15000,
})

// 添加请求拦截器，自动携带 token
adminRequest.interceptors.request.use(
  config => {
    // 从 sessionStorage 中获取 adminUser
    const admin = JSON.parse(sessionStorage.getItem('adminUser') || '{}')

    // 如果有 token，则添加到请求头
    if (admin.token) {
      config.headers['Authorization'] = `Bearer ${admin.token}`
    }

    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器保持不变
adminRequest.interceptors.response.use(
  res => res.data,
  err => Promise.reject(err)
)

export default adminRequest