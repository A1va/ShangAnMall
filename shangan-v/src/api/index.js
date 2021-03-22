import http from './public'
// 上岸商城登陆
export const userLogin = (params) => {
  return http.fetchPost('/api/v1/user/login', params)
}
// 上岸商城退出登陆
export const loginOut = (params) => {
  return http.fetchPost('http://127.0.0.1:8888/api/private/v1/admin/logout', params)
}
// 用户信息
export const userInfo = (params) => {
  return http.fetchGet('/member/checkLogin', params)
}
// 上岸商城用户信息
export const adminInfo = (params) => {
  return http.fetchGet('http://127.0.0.1:8888/api/private/v1/admin/info', params)
}
// 上岸商城注册账号
export const register = (params) => {
  return http.fetchPost('http://127.0.0.1:4523/mock2/373361/4225826', params)
}
// 上传图片
export const upload = (params) => {
  return http.fetchPost('/member/imgaeUpload', params)
}
// 修改头像
export const updateheadimage = (params) => {
  return http.fetchPost('/member/updateheadimage', params)
}
// 捐赠列表
export const thanksList = (params) => {
  return http.fetchGet('/member/thanks', params)
}
// 首页接口
// export const productHome = (params) => {
//   // return http.fetchGet('/goods/home', params)
//   return http.fetchGet('http://127.0.0.1:4523/mock2/373361/4220251', params)
// }
// 上岸商城首页
export const shanganIndex = (params) => {
  return http.fetchGet('http://localhost:15335/api/v1/index-infos', params)
}
// 上岸商城首页分类
export const shanganCate = (params) => {
  return http.fetchGet('http://127.0.0.1:15335/api/v1/categories', params)
}
// 首页接口
export const navList = (params) => {
  return http.fetchGet('/goods/navList', params)
}
// 推荐板块
export const recommend = (params) => {
  return http.fetchGet('/goods/recommend', params)
}
// 捐赠板块
export const thank = (params) => {
  return http.fetchGet('/goods/thank', params)
}
// 极验验证码
export const geetest = (params) => {
  return http.fetchGet('/member/geetestInit?t=' + (new Date()).getTime(), params)
}
