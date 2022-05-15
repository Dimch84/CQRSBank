<template>
    <el-col :offset="9">

        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <el-dropdown :hide-on-click="false">
                        <span class="el-dropdown-link">
                            Select Account<el-icon class="el-icon--right"><arrow-down /></el-icon>
                        </span>
                        <template #dropdown>
                            <el-dropdown-menu>
                            </el-dropdown-menu>
                            </template>
                    </el-dropdown>
                    <span> {{ account }} </span>
                </div>
            </template>

        </el-card>
    </el-col>
</template>

<!--<div v-for="item in cards" :key="item" class="text item">{{ 'Account with ' + item.message + '$'}}</div>-->
<!--<el-dropdown-item v-for="item in accounts" :key="item" v-on:click="accountChange(item.name)">{{ item.name }}</el-dropdown-item>-->


<script setup>
    import { ArrowDown } from '@element-plus/icons-vue'
</script>


<script>
    import axios from 'axios'
    axios.defaults.baseURL = 'http://localhost:8080/';
    import { ref } from 'vue'


    export default {
        name: 'UserSpace',
        data() {
            return {
                account: "",
                accounts: ref([{ name: 'first' }, { name: 'second' }]),
                cards: ref([{ name: 'first', message: '5' }, { name: 'second', message: '55' }]),
                username: '',
                name: '',
                password: '',
            }
        },
        methods: {
            login() {
                axios.get("cqrs/register/" + this.$data.username +"/", {})
                .then(response => {
                    this.$data.name = response.data.name
                    console.log(this.$data.name)
                    this.$store.dispatch('login', { 'login': this.$data.username, 'username': this.$data.username});
                }, error => {
                    this.$data.alertMessage = (error.length < 150) ? error.message : 'Request error';
                    console.log(error)
                })
                .catch(e => {
                    console.log(e);
                    this.showAlert();
                })
            },
            logout() {
                this.$store.dispatch('logout');
            },
            accountChange(name) {
                this.$data.account = name
            },
            loadAccounts() {
                console.log(this.$store.getters.getLogin)
                axios.get("cqrs/user/" + this.$store.getters.getLogin + "/", {})
                .then(response => {
                    console.log(response)
                }, error => {
                    this.$data.alertMessage = (error.length < 150) ? error.message : 'Request error';
                    console.log(error)
                })
                .catch(e => {
                    console.log(e);
                    this.showAlert();
                })

            }
        },
        beforeMount(){
            this.loadAccounts()
        },
    }
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

.example-showcase .el-dropdown + .el-dropdown {
    margin-left: 15px;
}
.example-showcase .el-dropdown-link {
    cursor: pointer;
    color: var(--el-color-primary);
    display: flex;
    align-items: center;
}

</style>
