<template>
    <h1>User Page</h1>
    <el-col :offset="1">
    <div style="margin: 30px" />
    <el-form :model="form" label-width="50px" width="40%">
        <el-form-item label="Name">
            <el-input v-model="form.name" width="40%"/>
        </el-form-item>
        <el-form-item label="Login">
            <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Phone">
            <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Email">
            <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="onSubmit">Change</el-button>
        </el-form-item>
    </el-form>
    </el-col>

</template>

        <!-- TODO: change <el-col :offset="9"> to something -->


<script setup>
import { reactive } from 'vue'

// do not use same name with ref
const form = reactive({
name: '',
region: '',
date1: '',
date2: '',
delivery: false,
type: [],
resource: '',
desc: '',
})

const onSubmit = () => {
console.log('submit!')
}
</script>


<script>
    import axios from 'axios'
    axios.defaults.baseURL = 'http://localhost:8080/';
    import { ref } from 'vue'

    const items = ref([{ message: '5' }, { message: '55' }])
</script>

<style>
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.text {
    font-size: 14px;
}

.item {
    margin-bottom: 18px;
    marign-top: 30%
}

.box-card {
    width: 480px;
}

</style>
