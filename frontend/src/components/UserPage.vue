<template>
    <el-col :offset="9">

        <el-card class="box-card">
            <template #header>User Page</template>

        </el-card>
    </el-col>
</template>

        <!-- TODO: change <el-col :offset="9"> to something -->

<script setup>
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
}

.box-card {
    width: 480px;
}

</style>
