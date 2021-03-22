<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>添加商品</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图 -->
    <el-card>
      <!-- 提示区域 -->
      <el-alert title="添加商品信息" type="info" center show-icon :closable="false">
      </el-alert>
      <!-- 步骤条区域 -->
      <el-steps :active="activeIndex - 0" finish-status="success" align-center>
        <el-step title="基本信息"></el-step>
<!--        <el-step title="商品参数"></el-step>-->
<!--        <el-step title="商品属性"></el-step>-->
        <el-step title="商品图片"></el-step>
        <el-step title="商品内容"></el-step>
        <el-step title="完成"></el-step>
      </el-steps>

      <!-- tab栏区域 -->
<!--      goodsName: '',-->
<!--      originalPrice: 0,-->
<!--      profit: 0,-->
<!--      stockNum: 0,-->
<!--      // 商品所属的分类数组-->
<!--      goodsCategoryId: '',-->
<!--      // 图片的数组-->
<!--      goodsCoverImg: '',-->
<!--      // 商品的详情描述-->
<!--      goodsDetailContent: '',-->
<!--      goodsIntro: ''-->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px" label-position="top">
        <el-tabs v-model="activeIndex" :tab-position="'left'" :before-leave="beforeTabLeave">
          <el-tab-pane label="基本信息" name="0">
            <el-form-item label="商品名称" prop="goodsName">
              <el-input v-model="addForm.goodsName"></el-input>
            </el-form-item>
            <el-form-item label="商品价格" prop="originalPrice">
              <el-input v-model="addForm.originalPrice" type="number"></el-input>
            </el-form-item>
            <el-form-item label="期望利润" prop="">
              <el-input v-model="addForm.profit" type="number"></el-input>
            </el-form-item>
            <el-form-item label="商品数量" prop="stockNum">
              <el-input v-model="addForm.stockNum" type="number"></el-input>
            </el-form-item>
            <el-form-item label="商品参数" prop="goodsIntro">
              <el-input v-model="addForm.goodsIntro"></el-input>
            </el-form-item>
            <el-form-item label="商品标签" prop="tag">
              <el-input v-model="addForm.tag"></el-input>
            </el-form-item>
            <el-form-item label="商品分类" prop="goodsCategoryId">
              <el-cascader expand-trigger="hover" :options="catelist" :props="cateProps" v-model="goods_cat" @change="handleChange">
              </el-cascader>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="商品图片" name="1">
            <!-- action 表示图片要上传到的后台API地址 -->
            <el-upload :action="uploadURL" :on-preview="handlePreview" :on-remove="handleRemove" list-type="picture" :headers="headerObj" :on-success="handleSuccess">
              <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
          </el-tab-pane>
          <el-tab-pane label="商品内容" name="2">
            <!-- 富文本编辑器组件 -->
            <quill-editor v-model="addForm.goodsDetailContent"></quill-editor>
            <!-- 添加商品的按钮 -->
            <el-button type="primary" class="btnAdd" @click="add">添加商品</el-button>
          </el-tab-pane>
        </el-tabs>
      </el-form>

    </el-card>

    <!-- 图片预览 -->
    <el-dialog title="图片预览" :visible.sync="previewVisible" width="50%">
      <img :src="previewPath" alt="" class="previewImg">
    </el-dialog>
  </div>
</template>

<script>
import _ from 'lodash'

export default {
  data() {
    return {
      activeIndex: '0',
      goods_cat: [],
      pics: [],
      // 添加商品的表单数据对象
      addForm: {
        goodsName: '',
        originalPrice: 0,
        profit: 0,
        stockNum: 0,
        tag: '',
        // 商品所属的分类数组
        goodsCategoryId: 0,
        // 图片的数组
        goodsCoverImg: '',
        // 商品的详情描述
        goodsDetailContent: '',
        goodsIntro: ''
      },
      addFormRules: {
        goodsName: [
          { required: true, message: '请输入商品名称', trigger: 'blur' }
        ],
        originalPrice: [
          { required: true, message: '请输入商品价格', trigger: 'blur' }
        ],
        profit: [
          { required: true, message: '请输入商品利润', trigger: 'blur' }
        ],
        stockNum: [
          { required: true, message: '请输入商品数量', trigger: 'blur' }
        ],
        goodsCategoryId: [
          { required: true, message: '请选择商品分类', trigger: 'blur' }
        ]
      },
      // 商品分类列表
      catelist: [],
      cateProps: {
        value: 'categoryId',
        label: 'categoryName',
        children: 'children'
      },
      // // 动态参数列表数据
      // manyTableData: [],
      // // 静态属性列表数据
      // onlyTableData: [],
      // 上传图片的URL地址
      uploadURL: 'http://127.0.0.1:8888/api/private/v1/upload',
      // 图片上传组件的headers请求头对象
      headerObj: {
        Authorization: window.sessionStorage.getItem('token')
      },
      previewPath: '',
      previewVisible: false
    }
  },
  created() {
    this.getCateList()
  },
  methods: {
    // 获取所有商品分类数据
    async getCateList() {
      const { data: res } = await this.$http.get('categories', {
        params: { type: 1 }
      })

      if (res.resultCode !== 200) {
        return this.$message.error('获取商品分类数据失败！')
      }

      this.catelist = res.data
      console.log(this.catelist)
    },
    // 级联选择器选中项变化，会触发这个函数
    handleChange() {
      console.log(this.goods_cat)
      if (this.goods_cat.length !== 3) {
        this.addForm.goodsCategoryId = 0
      } else {
        this.addForm.goodsCategoryId = this.goods_cat[this.goods_cat.length - 1]
      }
      console.log(this.addForm.goodsCategoryId)
    },
    beforeTabLeave(activeName, oldActiveName) {
      // console.log('即将离开的标签页名字是：' + oldActiveName)
      // console.log('即将进入的标签页名字是：' + activeName)
      // return false
      if (oldActiveName === '0' && this.goods_cat.length !== 3) {
        this.$message.error('请先选择商品分类！')
        return false
      }
    },
    // async tabClicked() {
    //   // console.log(this.activeIndex)
    //   // 证明访问的是动态参数面板
    //   if (this.activeIndex === '1') {
    //     const { data: res } = await this.$http.get(
    //       `categories/${this.cateId}/attributes`,
    //       {
    //         params: { sel: 'many' }
    //       }
    //     )
    //
    //     if (res.meta.status !== 200) {
    //       return this.$message.error('获取动态参数列表失败！')
    //     }
    //
    //     console.log(res.data)
    //     res.data.forEach(item => {
    //       item.attr_vals =
    //         item.attr_vals.length === 0 ? [] : item.attr_vals.split(' ')
    //     })
    //     this.manyTableData = res.data
    //   } else if (this.activeIndex === '2') {
    //     const { data: res } = await this.$http.get(
    //       `categories/${this.cateId}/attributes`,
    //       {
    //         params: { sel: 'only' }
    //       }
    //     )
    //
    //     if (res.meta.status !== 200) {
    //       return this.$message.error('获取静态属性失败！')
    //     }
    //
    //     console.log(res.data)
    //     this.onlyTableData = res.data
    //   }
    // },
    // 处理图片预览效果
    handlePreview(file) {
      console.log(111)
      this.previewPath = 'http://localhost:8888' + file.response.data
      console.log(this.previewPath)
      // http://localhost:15335//goods-img/f87bdee1-ed48-4b49-b701-cc44f26a2699.jpg
      this.previewVisible = true
    },
    // 处理移除图片的操作
    handleRemove(file) {
      // console.log(file)
      // 1. 获取将要删除的图片的临时路径
      const filePath = file.response.data.tmp_path
      // 2. 从 goodsCoverImg 数组中，找到这个图片对应的索引值
      const i = this.addForm.goodsCoverImg.findIndex(x => x.goodsCoverImg === filePath)
      // 3. 调用数组的 splice 方法，把图片信息对象，从 goodsCoverImg 数组中移除
      this.addForm.goodsCoverImg.splice(i, 1)
      console.log(this.addForm)
    },
    // 监听图片上传成功的事件
    handleSuccess(response) {
      // 1. 拼接得到一个图片信息对象
      const picInfo = { pic: response.data }
      // 2. 将图片信息对象，push 到pics数组中
      this.pics.push(picInfo)
      this.addForm.goodsCoverImg = picInfo.pic
      console.log(this.pics)
      console.log(this.addForm.goodsCoverImg)
    },
    // 添加商品
    add() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) {
          return this.$message.error('请填写必要的表单项！')
        }
        // 执行添加的业务逻辑
        // lodash   cloneDeep(obj)
        // this.removeHtml()
        console.log(this.addForm.goodsDetailContent)
        const form = _.cloneDeep(this.addForm)
        // form.goods_cat = form.goods_cat.join(',')
        // 处理动态参数
        // this.manyTableData.forEach(item => {
        //   const newInfo = {
        //     attr_id: item.attr_id,
        //     attr_value: item.attr_vals.join(' ')
        //   }
        //   this.addForm.attrs.push(newInfo)
        // })
        // 处理静态属性
        // this.onlyTableData.forEach(item => {
        //   const newInfo = { attr_id: item.attr_id, attr_value: item.attr_vals }
        //   this.addForm.attrs.push(newInfo)
        // })
        form.goodsIntro = this.addForm.goodsIntro
        console.log(form)

        // 发起请求添加商品
        // 商品的名称，必须是唯一的
        const { data: res } = await this.$http.post('goods', form)

        console.log(res)
        if (res.resultCode !== 200) {
          return this.$message.error('添加商品失败！')
        }

        this.$message.success('添加商品成功！')
        this.$router.push('/goods')
      })
    },
    output() {
      console.log(this.catelist)
    }
  },
  computed: {
    // cateId() {
    //   if (this.addForm.goods_cat.length === 3) {
    //     return this.addForm.goods_cat[2]
    //   }
    //   return null
    // }
  }
}
</script>

<style lang="less" scoped>
.el-checkbox {
  margin: 0 10px 0 0 !important;
}

.previewImg {
  width: 100%;
}

.btnAdd {
  margin-top: 15px;
}
</style>
