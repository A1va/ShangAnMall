import http from './public'
// 上岸商城商品列表
export const getSearch = (params) => {
  return http.fetchGet('http://localhost:15335/api/v1/search', params)
}
// 上岸商城购物车
export const getCartList = (params) => {
  return http.fetchGet('http://localhost:15335/api/v1/shop-cart/page', params)
}
// 上岸商城加入购物车
export const addCart = (params) => {
  return http.fetchPost('http://127.0.0.1:15335/api/v1/shop-cart', params)
}
// 删除购物车
export const delCart = (params) => {
  return http.fetchPost('http://127.0.0.1:15335/api/v1/shop-cart', params)
}
// 删除购物车勾选商品
export const delCartChecked = (params) => {
  return http.fetchPost('/member/delCartChecked', params)
}
// 编辑购物车
export const cartEdit = (params) => {
  return http.fetchPost('/member/cartEdit', params)
}
// 全选
export const editCheckAll = (params) => {
  return http.fetchPost('/member/editCheckAll', params)
}
// 上岸商城删除整条购物车
export const cartDel = (params) => {
  return http.fetchPost('http://127.0.0.1:15335/api/v1/shop-cart/update', params)
}
// 上岸商城获取用户地址
export const addressList = (params) => {
  return http.fetchGet('http://127.0.0.1:15335/api/v1/address', params)
}
// 通过id获取地址
export const getAddress = (params) => {
  return http.fetchPost('/member/address', params)
}
// 上岸商城修改收货地址
export const addressUpdate = (params) => {
  return http.fetchPost('http://127.0.0.1:15335/api/v1/address/update', params)
}
// 上岸商城添加收货地址
export const addressAdd = (params) => {
  return http.fetchPost('http://127.0.0.1:15335/api/v1/address', params)
}
// 上岸商城删除收货地址
export const addressDel = (params) => {
  return http.fetchGet('http://127.0.0.1:15335/api/v1/address/delete', params)
}
// 生成订单
export const submitOrder = (params) => {
  return http.fetchPost('http://127.0.0.1:15335/api/v1/saveOrder', params)
}
// 支付
export const payMent = (params) => {
  return http.fetchPost('/member/payOrder', params)
}
// 上岸商城获取用户订单
export const orderList = (params) => {
  return http.fetchGet('http://127.0.0.1:15335/api/v1/order', params)
}
// 上岸商城获取单个订单详情
export const getOrderDet = (params) => {
  return http.fetchGet('http://127.0.0.1:15335/api/v1/order/detail', params)
}
// 取消订单
export const cancelOrder = (params) => {
  return http.fetchPost('/member/cancelOrder', params)
}
// 上岸商城商品详情
export const productDet = (params) => {
  return http.fetchGet('http://127.0.0.1:15335/api/v1/goods/detail', params)
}
// 删除订单
export const delOrder = (params) => {
  return http.fetchGet('/member/delOrder', params)
}

// 快速搜索
export const getQuickSearch = (params) => {
  return http.fetchGet('/goods/quickSearch', params)
}
