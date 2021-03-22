<template>
  <el-container class="home-container">
    <!-- 头部区域 -->
    <el-header>
      <div>
        <img src="../assets/heima.png" alt />
        <span>电商后台管理系统</span>
      </div>
      <div style="float: right">
        <span style="font-size: 16px; margin-right: 10px">你好, 管理员{{this.adminInfo.nickName}}</span>
<!--        <el-button type="info" @click="logout" round>退出</el-button>-->
        <el-button type="info" @click="open" round>退出登录</el-button>
      </div>
    </el-header>
    <!-- 页面主体区域 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <div class="toggle-button" @click="toggleCollapse">{{flag}}</div>
        <!-- 侧边栏菜单区域 -->
        <el-menu
          background-color="#333744"
          text-color="#fff"
          active-text-color="#409EFF"
          unique-opened
          :collapse="isCollapse"
          :collapse-transition="false"
          router
          :default-active="activePath"
        >
          <!-- 一级菜单 -->
          <el-submenu :index="item.id + ''" v-for="item in menulist" :key="item.id">
            <!-- 一级菜单的模板区域 -->
            <template slot="title">
              <!-- 图标 -->
              <i :class="iconsObj[item.id]" ></i>
              <!-- 文本 -->
              <span>{{item.name}}</span>
            </template>

            <!-- 二级菜单 -->
            <el-menu-item
              :index="'/' + subItem.path"
              v-for="subItem in item.children"
              :key="subItem.id"
              @click="saveNavState('/' + subItem.path)"
            >
              <template slot="title">
                <!-- 图标 -->
                <i class="el-icon-menu"></i>
                <!-- 文本 -->
                <span>{{subItem.authName}}</span>
              </template>
            </el-menu-item>
          </el-submenu>

        </el-menu>
      </el-aside>
      <!-- 右侧内容主体 -->
      <el-main>
        <!-- 路由占位符 -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { adminInfo, loginOut } from '../api/index.js'
// import { removeStore } from '../utils/storage.js'
export default {
  data() {
    return {
      // 左侧菜单数据
      menulist: [],
      iconsObj: {
        125: 'iconfont icon-user',
        103: 'iconfont icon-tijikongjian',
        101: 'iconfont icon-shangpin',
        102: 'iconfont icon-danju',
        145: 'iconfont icon-baobiao'
      },
      // 是否折叠
      isCollapse: false,
      // 被激活的链接地址
      adminInfo: {
        loginUserName: '',
        nickName: ''
      },
      token: '',
      activePath: '',
      flag: '|||'
    }
  },
  created() {
    this.token = window.sessionStorage.getItem('token')
    this.activePath = window.sessionStorage.getItem('activePath')
  },
  methods: {
    async getMenuList() {
      const { data: res } = await this.$http.get('menu')
      if (res.resultCode !== 200) {
        return this.$message.error('获取导航栏失败！')
      }
      this.menulist = res.data
    },
    logout() {
      loginOut().then(res => {
        if (res.resultCode !== 200) {
          this.error = true
          return
        }
        this.token = ''
        this.activePath = ''
        window.sessionStorage.clear()
      })
      this.$router.push('/login')
    },
    open() {
      this.$confirm('此操作将退出登录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then((action) => {
        this.logout()
      }).then(() => {
        this.$message({
          type: 'success',
          message: '退出登录成功!'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出登录'
        })
      })
    },
    // // 获取所有的菜单
    // async getMenuList() {
    //   const { data: res } = await this.$http.get('')
    //   if (res.meta.status !== 200) return this.$message.error(res.meta.msg)
    //   this.menulist = res.data
    //   // console.log(res)
    // },
    // 点击按钮，切换菜单的折叠与展开
    toggleCollapse() {
      if (this.flag === '...') {
        this.flag = '|||'
        this.isCollapse = !this.isCollapse
      } else {
        this.flag = '...'
        this.isCollapse = !this.isCollapse
      }
    },
    // 保存链接的激活状态
    saveNavState(activePath) {
      window.sessionStorage.setItem('activePath', activePath)
      this.activePath = activePath
    }
  },
  mounted () {
    if (this.token !== null) {
      // -----暂时停用
      adminInfo().then(res => {
        if (res.resultCode !== 200) {
          this.error = true
          return
        }
        this.adminInfo = res.data
        console.log(this.adminInfo)
      })
      this.getMenuList()
    }
  }
}
</script>

<style lang="less" scoped>
.home-container {
  height: 100%;
}
.el-header {
  background-color: #373d41;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  align-items: center;
  color: #fff;
  font-size: 20px;
  > div {
    display: flex;
    align-items: center;
    span {
      margin-left: 15px;
    }
  }
}

.el-aside {
  background-color: #333744;
  .el-menu {
    border-right: none;
  }
}

.el-main {
  background-color: #eaedf1;
}

.iconfont {
  margin-right: 10px;
}

.toggle-button {
  background-color: #4a5064;
  font-size: 10px;
  line-height: 24px;
  color: #fff;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer;
}
</style>
// 侧边栏高亮有bug
