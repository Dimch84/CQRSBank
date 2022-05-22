<template>
    <div style="margin: 20px" />
    <el-row class="block-col-2">
        <el-col :span="8">
            <el-dropdown>
                <span class="el-dropdown-link">
                    Dropdown List<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </span>
                <template #dropdown>
                <el-dropdown-menu>
                    <el-dropdown-item :icon="Plus">Action 1</el-dropdown-item>
                    <el-dropdown-item :icon="CirclePlusFilled">
                        Action 2
                    </el-dropdown-item>
                    <el-dropdown-item :icon="CirclePlus">Action 3</el-dropdown-item>
                    <el-dropdown-item :icon="Check">Action 4</el-dropdown-item>
                    <el-dropdown-item :icon="CircleCheck">Action 5</el-dropdown-item>
                </el-dropdown-menu>
                </template>
            </el-dropdown>
        </el-col>
        <el-col :span="8">
        </el-col>
        <el-col :span="8">
        <div> Balance: </div>
        </el-col>
    </el-row>
    <el-divider />

    <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
    <li v-for="i in count" :key="i" class="infinite-list-item">{{ i }}</li>
    </ul>

    <el-divider />

    <h1>Make Operation</h1>

    <div style="margin: 50px 0" />
    <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="From">
            <el-input v-model="from " placeholder="Card" />
        </el-form-item>
        <el-form-item label="To">
            <el-input v-model="to" placeholder="Card" />
        </el-form-item>
        <el-form-item label="Money">
            <el-input v-model="money" placeholder="Money ($)" />
        </el-form-item>
        <el-form-item>
            <el-button type="primary"  v-on:click="transfer">Transfer Money</el-button>
        </el-form-item>
    </el-form>

</template>

<!--<div style="margin: 50px 0" />-->
<!--<el-form :inline="true" class="demo-form-inline">-->
<!--<el-form-item label="Card">-->
<!--    <el-input v-model="card" placeholder="Card" />-->
<!--</el-form-item>-->
<!--<el-form-item label="Money">-->
<!--    <el-input v-model="on_card" placeholder="Money ($)" />-->
<!--</el-form-item>-->
<!--<el-form-item>-->
<!--    <el-button type="primary"  v-on:click="show">Show Money</el-button>-->
<!--</el-form-item>-->
<!--</el-form>-->

<script setup>
    import { ArrowDown } from '@element-plus/icons-vue'
    import { ref } from 'vue'

    const count = ref(10)
    const load = () => {
        count.value += 2
    }

</script>

<script>
    import axios from 'axios'

    export default {
        name: 'OperationsScript',
        data() {
            return {
                from: '',
                to: '',
                money: 0,
                card: '',
                on_card: '',
            }
        },
        methods: {
            transfer() {
                axios.post("/cqrs/cards/" + this.$data.from +"/payment/", {"money": this.$data.money})
                .then(response => {
                    console.log(response)
                    this.$data.from = ''
                    this.$data.to = ''
                    this.$data.money = 0


                    axios.post("/cqrs/cards/" + this.$data.to +"/receipt/", {"money": this.$data.money})
                    .then(error => { console.log(error) })
                    .catch(e => { console.log(e); })
                }, error => {
                    console.log(error)
                })
                .catch(e => {
                    console.log(e);
                })

            },
            show() {
            axios.get("/cqrs/cards/" + this.$data.card, {})
                .then(response => {
                    console.log(response)

                    axios.get("/cqrs/accounts/" + response.data.account_id, {})
                    .then(r => {
                        console.log(r)
                        this.$data.on_card = r.data.money
                    },
                    error => { console.log(error) })
                    .catch(e => { console.log(e); })
                }, error => {
                    console.log(error)
                })
                .catch(e => {
                    console.log(e);
            })

            }
        }
    }
</script>

<style scoped>
.example-showcase .el-dropdown-link {
cursor: pointer;
color: var(--el-color-primary);
display: flex;
align-items: center;
}

.infinite-list {
height: 600px;
padding: 0;
margin: 80px;
list-style: none;
}
.infinite-list .infinite-list-item {
display: flex;
align-items: center;
justify-content: center;
height: 50px;
background: var(--el-color-primary-light-9);
margin: 10px;
color: var(--el-color-primary);
}
.infinite-list .infinite-list-item + .list-item {
margin-top: 10px;
}
</style>
