<template>
    <el-col :offset="9">

        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <span>Accounts</span>
                </div>
            </template>
            <div v-for="item in items" :key="item" class="text item">{{ 'Account with ' + item.message + '$'}}</div>

        </el-card>
    </el-col>
</template>
<script setup>
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
