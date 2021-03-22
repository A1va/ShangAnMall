<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>管理员权限列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图 -->
    <el-card>
      <el-table :data="rightsList" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="管理员Id" prop="adminUserId"></el-table-column>
        <el-table-column label="用户管理权限">
          <template slot-scope="scope">
            <el-switch
                v-model="scope.row.userManagement"
                @change="userManagementChanged(scope.row)"
                style="display: block"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="正常"
                inactive-text="锁定">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="订单管理权限">
          <template slot-scope="scope">
            <el-switch
                v-model="scope.row.orderManagement"
                @change="orderManagementChanged(scope.row)"
                style="display: block"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="正常"
                inactive-text="锁定">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="商品管理权限">
          <template slot-scope="scope">
            <el-switch
                v-model="scope.row.goodsManagement"
                @change="goodsManagementChanged(scope.row)"
                style="display: block"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="正常"
                inactive-text="锁定">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="日志管理权限">
          <template slot-scope="scope">
            <el-switch
                v-model="scope.row.logManagement"
                @change="logManagementChanged(scope.row)"
                style="display: block"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="正常"
                inactive-text="锁定">
            </el-switch>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页区域 -->
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryInfo.pagenum" :page-sizes="[1, 2, 5, 10]" :page-size="queryInfo.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      queryInfo: {
        // 当前的页数
        pagenum: 1,
        // 当前每页显示多少条数据
        pagesize: 5
      },
      total: 0,
      // 权限列表
      rightsList: []
    }
  },
  created() {
    // 获取所有的权限
    this.getRightsList()
  },
  methods: {
    // 获取权限列表
    async getRightsList() {
      const { data: res } = await this.$http.get('rights', {
        params: this.queryInfo
      })
      if (res.resultCode !== 200) {
        return this.$message.error('获取权限列表失败！')
      }

      this.rightsList = res.data.list
      this.total = res.data.totalCount
      console.log(this.rightsList)
    },
    // 监听 pagesize 改变的事件
    handleSizeChange(newSize) {
      // console.log(newSize)
      this.queryInfo.pagesize = newSize
      this.getUserList()
    },
    // 监听 页码值 改变的事件
    handleCurrentChange(newPage) {
      console.log(newPage)
      this.queryInfo.pagenum = newPage
      this.getUserList()
    },
    async userManagementChanged(rightsInfo) {
      const { data: res } = await this.$http.put(
            `rights/${rightsInfo.adminUserId}/management/${rightsInfo.userManagement}/1`
      )
      if (res.resultCode !== 200) {
        rightsInfo.userManagement = !rightsInfo.userManagement
        return this.$message.error('修改用户管理权限状态失败！')
      }
      this.$message.success('更新用户状态成功！')
    },
    async orderManagementChanged(rightsInfo) {
      const { data: res } = await this.$http.put(
          `rights/${rightsInfo.adminUserId}/management/${rightsInfo.orderManagement}/2`
      )
      if (res.resultCode !== 200) {
        rightsInfo.orderManagement = !rightsInfo.orderManagement
        return this.$message.error('修改订单管理权限状态失败！')
      }
      this.$message.success('更新用户状态成功！')
    },
    async goodsManagementChanged(rightsInfo) {
      const { data: res } = await this.$http.put(
          `rights/${rightsInfo.adminUserId}/management/${rightsInfo.goodsManagement}/3`
      )
      if (res.resultCode !== 200) {
        rightsInfo.goodsManagement = !rightsInfo.goodsManagement
        return this.$message.error('修改商品管理权限状态失败！')
      }
      this.$message.success('更新用户状态成功！')
    },
    async logManagementChanged(rightsInfo) {
      const { data: res } = await this.$http.put(
          `rights/${rightsInfo.adminUserId}/management/${rightsInfo.logManagement}/4`
      )
      if (res.resultCode !== 200) {
        rightsInfo.logManagement = !rightsInfo.logManagement
        return this.$message.error('修改日志管理权限状态失败！')
      }
      this.$message.success('更新用户状态成功！')
    }
    // async userStateChanged(userinfo) {
    //   console.log(userinfo)
    //   const { data: res } = await this.$http.put(
    //       `users/${userinfo.userId}/state/${userinfo.status}`
    //   )
    //   if (res.resultCode !== 200) {
    //     userinfo.status = !userinfo.status
    //     return this.$message.error('更新用户状态失败！')
    //   }
    //   this.$message.success('更新用户状态成功！')
    // },
  }
}
</script>

<style lang="less" scoped>
</style>
