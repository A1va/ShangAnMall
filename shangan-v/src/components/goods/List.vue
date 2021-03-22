<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>商品列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input placeholder="请输入内容" v-model="queryInfo.query" clearable @clear="getGoodsList">
            <el-button slot="append" icon="el-icon-search" @click="getGoodsList"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="goAddpage">添加商品</el-button>
        </el-col>
      </el-row>

      <!-- table表格区域 -->
      <el-table :data="goodslist" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="商品名称" prop="goodsName"></el-table-column>
        <el-table-column label="商品参数" prop="goodsIntro" width=""></el-table-column>
        <el-table-column label="商品成本价(元)" prop="originalPrice" width=""></el-table-column>
        <el-table-column label="商品售价(元)" prop="sellingPrice" width=""></el-table-column>
        <el-table-column label="商品库存" prop="stockNum" width=""></el-table-column>
        <el-table-column label="商品上架状态" prop="goodsSellStatus">
          <template slot-scope="scope">
            <i class="el-icon-success" v-if="scope.row.goodsSellStatus === 0" style="color: lightgreen;"></i>
            <i class="el-icon-error" v-else style="color: #ff0000;"></i>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="">
          <template slot-scope="scope">
            {{scope.row.createTime | dateFormat}}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130px">
          <template slot-scope="scope">
<!--            <el-button type="primary" size="mini"></el-button>-->
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="openEditDialog(scope.row.goodsId)"></el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeById(scope.row.goodsId)"></el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryInfo.pagenum" :page-sizes="[5, 10, 15, 20]" :page-size="queryInfo.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="total" background>
      </el-pagination>
    </el-card>
    <!-- 修改对话框 -->
    <el-dialog title="修改商品信息" :visible.sync="editGoodsDialogVisible" width="50%" @close="editGoodsDialogClosed">
      <!-- 添加分类的表单 -->
      <el-form :model="editGoodsForm" ref="editGoodsFormRef" label-width="100px">
        <el-form-item label="商品ID：" prop="goodsId">
          <el-input v-model="editGoodsForm.goodsId" disabled></el-input>
        </el-form-item>
        <el-form-item label="商品名称：" prop="goodsName">
          <el-input v-model="editGoodsForm.goodsName"></el-input>
        </el-form-item>
        <el-form-item label="商品成本价" prop="originalPrice">
          <el-input v-model="editGoodsForm.originalPrice" type="number"></el-input>
        </el-form-item>
        <el-form-item label="商品利润" prop="profit">
          <el-input v-model="editGoodsForm.profit" type="number"></el-input>
        </el-form-item>
        <el-form-item label="商品库存" prop="stockNum">
          <el-input v-model="editGoodsForm.stockNum" type="number"></el-input>
        </el-form-item>
        <el-form-item label="商品标签" prop="tag">
          <el-input v-model="editGoodsForm.tag"></el-input>
        </el-form-item>
        <el-form-item label="商品上架状态" prop="goodsSellStatus">
<!--          <el-cascader v-model="editGoodsForm.goodsSellStatus"></el-cascader>-->
          <el-select v-model="editGoodsForm.goodsSellStatus" placeholder="请选择">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品详情" prop="goodsDetailContent">
          <el-input v-model="editGoodsForm.goodsDetailContent"></el-input>
        </el-form-item>
      </el-form>
<!--      editGoodsForm: {-->
<!--      goodsId: 100,-->
<!--      goodsName: '',-->
<!--      goodsCategoryId: 0,-->
<!--      originalPrice: 0,-->
<!--      profit: 0,-->
<!--      stockNum: 0,-->
<!--      tag: '',-->
<!--      goodsSellStatus: false,-->
<!--      goodsDetailContent: ''-->
<!--        <el-form-item label="父级分类：">-->
<!--          &lt;!&ndash; options 用来指定数据源 &ndash;&gt;-->
<!--          &lt;!&ndash; props 用来指定配置对象 &ndash;&gt;-->
<!--          <el-cascader :options="parentCateList" :change-on-select="true" :props="cascaderProps" v-model="selectedKeys" @change="parentCateChanged" clearable>-->
<!--          </el-cascader>-->
<!--        </el-form-item>-->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editGoodsDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editGoods">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import _ from 'lodash'

export default {
  data() {
    return {
      // 查询参数对象
      queryInfo: {
        query: '',
        pagenum: 1,
        pagesize: 10
      },
      // 商品列表
      goodslist: [],
      editGoodsForm: {
        goodsId: 0,
        goodsName: '',
        originalPrice: 0,
        profit: 0,
        stockNum: 0,
        tag: '',
        goodsSellStatus: 0,
        goodsDetailContent: ''
      },
      // 总数据条数
      total: 0,
      editGoodsDialogVisible: 0,
      options: [{
        value: 0,
        label: '上架'
      }, {
        value: 1,
        label: '下架'
      }],
      value: 0
    }
  },
  created() {
    this.getGoodsList()
  },
  methods: {
    // 根据分页获取对应的商品列表
    async getGoodsList() {
      const { data: res } = await this.$http.get('goods', {
        params: this.queryInfo
      })

      if (res.resultCode !== 200) {
        return this.$message.error('获取商品列表失败！')
      }

      this.$message.success('获取商品列表成功！')
      console.log(res.data)
      this.goodslist = res.data.list
      this.total = res.data.totalCount
    },
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize
      this.getGoodsList()
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage
      this.getGoodsList()
    },
    async removeById(id) {
      const confirmResult = await this.$confirm(
        '此操作将永久删除该商品, 是否继续?',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).catch(err => err)

      if (confirmResult !== 'confirm') {
        return this.$message.info('已经取消删除！')
      }

      const { data: res } = await this.$http.delete(`goods/${id}`)

      if (res.resultCode !== 200) {
        return this.$message.error('删除失败！')
      }

      this.$message.success('删除成功！')
      this.queryInfo.pagenum = 1
      await this.getGoodsList()
    },
    goAddpage() {
      this.$router.push('/goods/add')
    },
    editGoodsDialogClosed() {
      this.$refs.addCateFormRef.resetFields()
      // this.selectedKeys = []
      // this.addCateForm.categoryLevel = 0
      // this.addCateForm.parentId = 0
    },
    openEditDialog(id) {
      this.editGoodsDialogVisible = true
      console.log(id)
      console.log(this.goodslist)
      for (const i in this.goodslist) {
        if (this.goodslist[i].goodsId === id) {
          this.editGoodsForm.goodsId = this.goodslist[i].goodsId
          this.editGoodsForm.goodsName = this.goodslist[i].goodsName
          this.editGoodsForm.originalPrice = this.goodslist[i].originalPrice
          this.editGoodsForm.profit = this.goodslist[i].sellingPrice - this.goodslist[i].originalPrice
          this.editGoodsForm.stockNum = this.goodslist[i].stockNum
          this.editGoodsForm.tag = this.goodslist[i].tag
          this.editGoodsForm.goodsSellStatus = this.goodslist[i].goodsSellStatus
          this.editGoodsForm.goodsDetailContent = this.goodslist[i].goodsDetailContent
        }
      }
    },
    editGoods() {
      this.$refs.editGoodsFormRef.validate(async valid => {
        if (!valid) {
          return this.$message.error('请填写必要的表单项！')
        }
        // 执行添加的业务逻辑
        console.log(this.editGoodsForm.goodsDetailContent)
        const form = _.cloneDeep(this.editGoodsForm)
        console.log(form)

        // 发起请求添加商品
        // 商品的名称，必须是唯一的
        const { data: res } = await this.$http.post('goods/edit', form)
        console.log(res)
        if (res.resultCode !== 200) {
          return this.$message.error('修改商品信息失败！')
        }
        this.$message.success('修改商品信息成功！')
        this.editGoodsDialogVisible = false
        await this.getGoodsList()
      })
    }
  }
}
</script>

<style lang="less" scoped>
</style>
