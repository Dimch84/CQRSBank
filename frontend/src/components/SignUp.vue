<template>
    <el-col :offset="9">
        <el-card class="box-card" v-if="!this.$store.getters.isAuthenticated">
        <template #header>
            <div class="card-header">
                <span>Sign Up</span>
            </div>
        </template>

        <div>
            <el-input type="text" placeholder="Name" v-model="name"/>
            <div style="margin: 10px 0" />

            <el-input type="text" placeholder="Username" v-model="username"/>
            <div style="margin: 10px 0" />

            <el-input type="password" placeholder="Password" v-model="password"/>
            <div style="margin: 10px 0" />
        </div>

        <el-button variant="primary" v-on:click="login">Register</el-button>
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
                username: '',
                name: '',
                password: '',
                dismissSecs: 5,
                dismissCountDown: 0,
                alertMessage: 'Request error',
            }
        },
        methods: {
            login() {
                axios.post("/cqrs/register/" + this.$data.username +"/", { 'name': this.$data.name, 'password': this.$data.password })
                .then(response => {
                    console.log(response)
                    this.$store.dispatch('login', {'login': this.$data.username, 'username': this.$data.username, 'id':response.data});
                }, error => {
                    this.$data.alertMessage = (error.length < 150) ? error.message : 'Request error';
                    console.log(error)
                })
                .catch(e => {
                    console.log(e);
                    this.showAlert();
                })
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

<!--        this.$router.push('/home')-->
<!--change token to id-->