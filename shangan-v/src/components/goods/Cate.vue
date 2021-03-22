<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>商品分类</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <el-row>
        <el-col>
          <el-button type="primary" @click="showAddCateDialog">添加分类</el-button>
        </el-col>
      </el-row>

      <!-- 表格 -->
      <tree-table class="treeTable" :data="catelist" :columns="columns" :selection-type="false" :expand-type="false" show-index index-text="#" border :show-row-hover="false">
        <!-- 是否有效 -->
        <template slot="isok" slot-scope="scope">
          <i class="el-icon-success" v-if="scope.row.isDeleted === 0" style="color: lightgreen;"></i>
          <i class="el-icon-error" v-else style="color: #ff0000;"></i>
        </template>
        <!-- 排序 -->
        <template slot="order" slot-scope="scope">
          <el-tag size="mini" v-if="scope.row.categoryLevel===1">一级</el-tag>
          <el-tag type="success" size="mini" v-else-if="scope.row.categoryLevel===2">二级</el-tag>
          <el-tag type="warning" size="mini" v-else>三级</el-tag>
        </template>
        <!-- 操作 -->
        <template slot="opt" slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="openEditDialog(scope.row.categoryId)">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeById(scope.row.categoryId)">删除</el-button>
        </template>
      </tree-table>

      <!-- 分页区域 -->
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryInfo.pagenum" :page-sizes="[3, 5, 10, 15]" :page-size="queryInfo.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </el-card>

    <!-- 添加分类的对话框 -->
    <el-dialog title="添加分类" :visible.sync="addCateDialogVisible" width="50%" @close="addCateDialogClosed">
      <!-- 添加分类的表单 -->
      <el-form :model="addCateForm" :rules="addCateFormRules" ref="addCateFormRef" label-width="100px">
        <el-form-item label="分类名称：" prop="categoryName">
          <el-input v-model="addCateForm.categoryName"></el-input>
        </el-form-item>
        <el-form-item label="父级分类：">
          <!-- options 用来指定数据源 -->
          <!-- props 用来指定配置对象 -->
          <el-cascader :options="parentCateList" :change-on-select="true" :props="cascaderProps" v-model="selectedKeys" @change="parentCateChanged" clearable>
          </el-cascader>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addCateDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editCate">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="修改分类信息" :visible.sync="editCateDialogVisible" width="50%" @close="editCateDialogClosed">
      <!-- 添加分类的表单 -->
      <el-form :model="editCateFrom" ref="editCateFormRef" label-width="100px">
        <el-form-item label="分类ID：" prop="categoryId">
          <el-input v-model="editCateFrom.categoryId" disabled></el-input>
        </el-form-item>
        <el-form-item label="分类名称：" prop="categoryName">
          <el-input v-model="editCateFrom.categoryName"></el-input>
        </el-form-item>
        <el-form-item label="父级分类：">
          <el-cascader :options="parentCateList" :change-on-select="true" :props="cascaderProps" v-model="selectedCate" @change="editParentCateChanged" clearable>
          </el-cascader>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editCateDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editCate">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>

export default {
  data() {
    return {
      // 查询条件
      queryInfo: {
        type: 3,
        pagenum: 1,
        pagesize: 5
      },
      // 商品分类的数据列表，默认为空
      catelist: [],
      // 总数据条数
      total: 0,
      // 为table指定列的定义
      columns: [
        {
          label: '分类名称',
          prop: 'categoryName'
        },
        {
          label: '是否有效',
          // 表示，将当前列定义为模板列
          type: 'template',
          // 表示当前这一列使用模板名称
          template: 'isok'
        },
        {
          label: '排序',
          // 表示，将当前列定义为模板列
          type: 'template',
          // 表示当前这一列使用模板名称
          template: 'order'
        },
        {
          label: '操作',
          // 表示，将当前列定义为模板列
          type: 'template',
          // 表示当前这一列使用模板名称
          template: 'opt'
        }
      ],
      // 控制添加分类对话框的显示与隐藏
      addCateDialogVisible: false,
      // 添加分类的表单数据对象
      addCateForm: {
        // 将要添加的分类的名称
        categoryName: '',
        // 父级分类的Id
        parentId: 0,
        // 分类的等级，默认要添加的是1级分类
        categoryLevel: 1
      },
      // 添加分类表单的验证规则对象
      addCateFormRules: {
        categoryName: [
          { required: true, message: '请输入分类名称', trigger: 'blur' }
        ]
      },
      // 父级分类的列表
      parentCateList: [],
      // 指定级联选择器的配置对象
      cascaderProps: {
        value: 'categoryId',
        label: 'categoryName',
        children: 'children',
        expandTrigger: 'hover'
      },
      // 选中的父级分类的Id数组
      selectedKeys: [],
      selectedCate: [],
      editCateDialogVisible: false,
      editCateFrom: {
        categoryId: 0,
        parentId: 0,
        categoryLevel: 0,
        categoryName: ''
      }
    }
  },
  created() {
    this.getCateList()
  },
  methods: {
    // 获取商品分类数据
    async getCateList() {
      const { data: res } = await this.$http.get('categories', {
        params: this.queryInfo
      })

      if (res.resultCode !== 200) {
        return this.$message.error('获取商品分类失败！')
      }

      console.log(res.data)
      // 把数据列表，赋值给 catelist
      this.catelist = res.data.list
      // 为总数据条数赋值
      this.total = res.data.totalCount
    },
    // 监听 pagesize 改变
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize
      this.getCateList()
    },
    // 监听 pagenum 改变
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage
      this.getCateList()
    },
    // 点击按钮，展示添加分类的对话框
    showAddCateDialog() {
      // 先获取父级分类的数据列表
      this.getParentCateList()
      // 再展示出对话框
      this.addCateDialogVisible = true
    },
    // 获取父级分类的数据列表
    async getParentCateList() {
      const { data: res } = await this.$http.get('categories', {
        params: { type: 2 }
      })

      if (res.resultCode !== 200) {
        return this.$message.error('获取父级分类数据失败！')
      }

      this.parentCateList = res.data
      console.log(this.parentCateList)
    },
    // 选择项发生变化触发这个函数
    parentCateChanged() {
      console.log(this.selectedKeys)
      console.log(this.selectedKeys.length)
      // 如果 selectedKeys 数组中的 length 大于0，证明选中的父级分类
      // 反之，就说明没有选中任何父级分类
      if (this.selectedKeys.length > 0) {
        // 父级分类的Id
        this.addCateForm.parentId = this.selectedKeys[
          this.selectedKeys.length - 1
        ]
        // 为当前分类的等级赋值
        this.addCateForm.categoryLevel = this.selectedKeys.length + 1
        console.log(this.addCateForm)
        return true
      } else {
        // 父级分类的Id
        this.addCateForm.parentId = 0
        // 为当前分类的等级赋值
        this.addCateForm.categoryLevel = 0
      }
    },
    editParentCateChanged() {
      if (this.selectedCate.length > 0) {
        // 父级分类的Id
        this.editCateFrom.parentId = this.selectedCate[
          this.selectedCate.length - 1
        ]
        // 为当前分类的等级赋值
        this.editCateFrom.categoryLevel = this.selectedCate.length + 1
        console.log(this.editCateFrom)
        return true
      } else {
        // 父级分类的Id
        this.editCateFrom.parentId = 0
        // 为当前分类的等级赋值
        this.editCateFrom.categoryLevel = 0
      }
    },
    // 点击按钮，添加新的分类
    addCate() {
      this.$refs.addCateFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          'categories',
          this.addCateForm
        )

        if (res.resultCode !== 200) {
          return this.$message.error('添加分类失败！')
        }

        this.$message.success('添加分类成功！')
        await this.getCateList()
        this.addCateDialogVisible = false
      })
    },
    // 监听对话框的关闭事件，重置表单数据
    addCateDialogClosed() {
      this.$refs.addCateFormRef.resetFields()
      this.selectedKeys = []
      this.addCateForm.categoryLevel = 0
      this.addCateForm.parentId = 0
    },
    editCateDialogClosed() {
      this.$refs.editCateFormRef.resetFields()
      this.selectedCate = []
      this.editCateFrom.categoryLevel = 0
      this.editCateFrom.parentId = 0
    },
    async openEditDialog(id) {
      await this.getParentCateList()
      // 再展示出对话框
      this.editCateDialogVisible = true
      this.selectedCate = []
      for (const i in this.catelist) {
        if (this.catelist[i].categoryId === id) {
          this.editCateFrom.categoryId = this.catelist[i].categoryId
          this.editCateFrom.categoryName = this.catelist[i].categoryName
          if (this.catelist[i].categoryLevel === 1) {
            // this.selectedCate = [this.editCateFrom.categoryId]
            // console.log(this.selectedCate)
          } else if (this.catelist[i].categoryLevel === 2) {
            const { data: res } = await this.$http.get(`categories/${id}`, {
              params: { type: 2 }
            })
            if (res.resultCode === 200) {
              this.selectedCate = res.data
              console.log(res.data)
              this.editCateFrom.parentId = this.selectedCate[this.selectedCate.length - 1]
              this.editCateFrom.categoryLevel = this.selectedCate.length + 1
            }
          } else {
            const { data: res } = await this.$http.get(`categories/${id}`, {
              params: { type: 3 }
            })
            if (res.resultCode === 200) {
              this.selectedCate = res.data
              this.editCateFrom.parentId = this.selectedCate[this.selectedCate.length - 1]
              this.editCateFrom.categoryLevel = this.selectedCate.length + 1
              console.log(this.selectedCate)
            }
          }
        }
      }
    },
    async removeById(id) {
      const confirmResult = await this.$confirm(
        '此操作将永久删除该商品分类, 是否继续?',
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

      const { data: res } = await this.$http.delete(`categories/${id}`)

      if (res.resultCode !== 200) {
        return this.$message.error('删除分类失败！')
      }

      this.$message.success('删除分类成功！')
      this.queryInfo.pagenum = 1
      await this.getCateList()
    },
    editCate() {
      this.$refs.editCateFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          'categories/edit',
          this.editCateFrom
        )

        if (res.resultCode !== 200) {
          return this.$message.error('修改分类信息失败！')
        }

        this.$message.success('修改分类信息成功！')
        await this.getCateList()
        this.editCateDialogVisible = false
      })
    }
  }
}
</script>

<style lang="less" scoped>
.treeTable {
  margin-top: 15px;
}

.el-cascader {
  width: 100%;
}
</style>
