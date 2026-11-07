import { createRouter, createWebHistory } from 'vue-router'

/* ========= 布局 (Layouts) ========= */
const Layout = () => import('@/layouts/Layout.vue')
const AdminLayout = () => import('@/pages/admin/Index.vue')

/* ========= 前台页面 (Front-end) ========= */
const FrontHome         = () => import('@/pages/front/Home.vue')
const FrontLogin        = () => import('@/pages/front/Login.vue')
const FrontRegister     = () => import('@/pages/front/Register.vue')
const FrontProfile      = () => import('@/pages/front/Profile.vue')
const FrontCart         = () => import('@/pages/front/Cart.vue')
const FrontOrder        = () => import('@/pages/front/Order.vue')
const FrontProduct      = () => import('@/pages/front/Product.vue')
const FrontCheckout     = () => import('@/pages/front/Checkout.vue')
const FrontAnnouncement = () => import('@/pages/front/Announcement.vue')
const FrontSearch       = () => import('@/pages/front/Search.vue')
const FrontPay          = () => import('@/pages/front/Pay.vue')
const FrontVipCenter    = () => import('@/pages/front/VipCenter.vue')
const OrderDetailReturn = () => import('@/pages/front/OrderDetailReturn.vue')

/* ========= 后台管理 (Admin-end) ========= */
const AdminLogin  = () => import('@/pages/admin/Login.vue')
const Dashboard   = () => import('@/pages/admin/Dashboard.vue')

/* ========= 后台子页面 (Admin Sub-pages) ========= */
const ProductList        = () => import('@/pages/subpages/ProductList.vue')
const CategoryList       = () => import('@/pages/subpages/CategoryList.vue')
const OrderList          = () => import('@/pages/subpages/OrderList.vue')
const UserList           = () => import('@/pages/subpages/UserList.vue')
const BannerList         = () => import('@/pages/subpages/BannerList.vue')
const AnnouncementList   = () => import('@/pages/subpages/AnnouncementList.vue')
const ManagerList        = () => import('@/pages/subpages/ManagerList.vue')
const ReviewList         = () => import('@/pages/subpages/ReviewList.vue')
const ReturnRequestList  = () => import('@/pages/subpages/ReturnRequestList.vue')

const routes = [
  /* ========= 前台路由 (带统一页头页脚布局) ========= */
  {
    path: '/',
    component: Layout,
    children: [
      { path: '', name: 'Home', component: FrontHome },
      { path: 'login', name: 'Login', component: FrontLogin },
      { path: 'register', name: 'Register', component: FrontRegister },
      { path: 'profile', name: 'Profile', component: FrontProfile },
      { path: 'cart', name: 'Cart', component: FrontCart },
      { path: 'order', name: 'Order', component: FrontOrder },
      { path: 'order/detail/:id', name: 'OrderDetail', component: OrderDetailReturn },
      { path: 'product/:id', name: 'ProductDetail', component: FrontProduct },
      { path: 'checkout', name: 'Checkout', component: FrontCheckout },
      { path: 'announcement', name: 'Announcement', component: FrontAnnouncement },
      { path: 'search', name: 'Search', component: FrontSearch },
      { path: 'vip-center', name: 'VipCenter', component: FrontVipCenter },
      { path: 'pay/:orderId', name: 'Pay', component: FrontPay }
    ]
  },

  /* ========= 后台登录 (独立页面) ========= */
  { path: '/admin/login', name: 'AdminLogin', component: AdminLogin },

  /* ========= 后台管理核心 (带侧边栏布局) ========= */
  {
    path: '/admin',
    component: AdminLayout,
    redirect: '/admin/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: Dashboard },
      { path: 'product', name: 'AdminProduct', component: ProductList },
      { path: 'category', name: 'AdminCategory', component: CategoryList },
      { path: 'order', name: 'AdminOrder', component: OrderList },
      { path: 'user', name: 'AdminUser', component: UserList },
      { path: 'manager', name: 'AdminManager', component: ManagerList },
      { path: 'banner', name: 'AdminBanner', component: BannerList },
      { path: 'announcement', name: 'AdminAnnouncement', component: AnnouncementList },
      { path: 'review', name: 'AdminReview', component: ReviewList },
      { path: 'return-request', name: 'AdminReturn', component: ReturnRequestList }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// src/router/index.js
router.beforeEach((to, from, next) => {
  // ✅ 核心修复：全部改为 sessionStorage
  const user = JSON.parse(sessionStorage.getItem('user') || '{}')
  const admin = JSON.parse(sessionStorage.getItem('adminUser') || '{}')

  // 如果要去后台管理页，但没有管理员信息
  if (to.path.startsWith('/admin') && to.path !== '/admin/login') {
    if (!admin.id) {
      return next('/admin/login')
    }
  }

  // 如果要去个人中心、结算页等，但没有用户信息
  const authRoutes = ['/profile', '/checkout', '/order']
  if (authRoutes.includes(to.path)) {
    if (!user.id) {
      return next('/login')
    }
  }

  next()
})
export default router