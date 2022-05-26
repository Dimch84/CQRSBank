<template>
    <h1>User Page</h1>
    <el-col :offset="9">
    <el-card class="box-card" v-if="this.$store.getters.isAuthenticated">
    <el-form :inline=true label-width="70px">
        <el-form-item label="Name">
            <el-input v-model="user.name"/>
        </el-form-item>
        <el-form-item label="Login">
            <el-input v-model="user.login" disabled/>
        </el-form-item>
        <el-form-item label="Phone">
            <el-input v-model="user.phone" />
        </el-form-item>
        <el-form-item label="Email">
            <el-input v-model="user.email" />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" v-on:click="changeUserInfo()">Change</el-button>
        </el-form-item>
        <el-form-item>
            <el-button v-on:click="logout"><a href="/#/login">Logout</a></el-button>
        </el-form-item>
    </el-form>
    </el-card>
    </el-col>

</template>

<script>
    import axios from 'axios'

    export default {
        name: 'UserSpace',
        data() {
            return {
                user : {},
                accountMoney : 0
            }
        },
        methods: {
            login() {
            const instance = axios.create({
                baseURL: 'http://localhost:8080/',
                auth: { username: this.$store.getters.getLogin, password: this.$store.getters.getPassword },
            });

            instance.get("cqrs/register/" + this.$data.username +"/", {})
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
            loadUser() {
                const instance = axios.create({
                    baseURL: 'http://localhost:8080/',
                    auth: { username: this.$store.getters.getLogin, password: this.$store.getters.getPassword },
                });

                instance.get("cqrs/user/", {})
                .then(response => {
                    console.log(response.data)
                    this.$data.user = response.data
                }, error => { console.log(error) })
                .catch(e => { console.log(e); })
            },
            loadAccount() {

            },
            changeUserInfo() {
                const instance = axios.create({
                baseURL: 'http://localhost:8080/',
                auth: { username: this.$store.getters.getLogin, password: this.$store.getters.getPassword },
                });

                instance.post("cqrs/user/"  , this.$data.user)
                .then(response => {
                    console.log(response.data)
                }, error => { console.log(error) })
                .catch(e => { console.log(e); })

            }
        },
        beforeMount(){
            this.loadUser()
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
    marign-top: 30%
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
