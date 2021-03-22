<template>
  <div style="position: absolute;
            top: 50%;
            left: 50%;
            -webkit-transform: translate(-50%, -50%);
            -moz-transform: translate(-50%, -50%);
            -ms-transform: translate(-50%, -50%);
            -o-transform: translate(-50%, -50%);
            transform: translate(-50%, -50%);">
      <el-sl-panel style="color: #606266 ;font-family: Helvetica Neue; font-size: 60px;" v-if="grt=(hour<9?'早上好':
                    hour<12?'上午好':
                    hour==12?'中午好':
                    hour<18?'下午好':
                    '晚上好'
                    )">{{grt}}</el-sl-panel>
  </div>
</template>
<script>
export default {
  data () {
    return {
      hour: new Date().getHours()
    }
  }
}
</script>
