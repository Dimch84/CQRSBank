<template>
    <el-col :offset="9">
    <el-card class="box-card" v-if="!this.$store.getters.isAuthenticated">
            <template #header>
                <div class="card-header">
                <span>Sign In</span>
                </div>
            </template>

            <div>
                <el-input type="text" placeholder="Login" v-model="user_login" />
                <div style="margin: 10px 0" />

                <el-input type="password" placeholder="Password" v-model="user_password" />
                <div style="margin: 10px 0" />
            </div>

            <el-button variant="primary" v-on:click="login">Login</el-button>

        </el-card>
    <el-card class="box-card" v-if="this.$store.getters.isAuthenticated">
        <el-button v-on:click="logout"><a href="/#/login">Logout</a></el-button>
    </el-card>
    </el-col>
</template>




<script>
    import axios from 'axios'
    axios.defaults.baseURL = 'http://localhost:8080/';

    export default {
        name: 'SignIn',
        data() {
            return {
                user_login: '',
                user_password: '',
            }
        },
        methods: {
            login() {
                const instance = axios.create({
                    baseURL: 'http://localhost:8080/',
                    auth: { username: this.$data.user_login, password: this.$data.user_password },
                });

                instance.get("cqrs/user/",  {})
                .then(response => {
                    console.log(response)
                    this.$store.dispatch('login', { 'login': this.$data.user_login, 'username': response.data.name, 'password': this.$data.user_password, 'id':response.data.id });
                }, error => { console.log(error) })
                .catch(e => { console.log(e); })
            },
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },
            logout() {
                this.$store.dispatch('logout');
            }
        },
    }

</script>
        <!--                    this.$data.name = response.data.name-->
        <!--                    console.log(this.$data.name)-->

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
    margin-top: 40px;
}

</style>
