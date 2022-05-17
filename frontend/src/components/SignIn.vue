<template>
    <el-col :offset="9">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                <span>Sign In</span>
                </div>
            </template>

            <div>
                <el-input type="text" placeholder="Username" v-model="username" />
                <div style="margin: 10px 0" />

                <el-input type="password" placeholder="Password" v-model="password" />
                <div style="margin: 10px 0" />
            </div>

            <el-button variant="primary" v-on:click="login">Login</el-button>

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
                axios.get("/register/" + this.$data.username +"/", {})
                .then(response => {
                    console.log(response)
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
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },
            showAlert() {
                this.dismissCountDown = this.dismissSecs
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
