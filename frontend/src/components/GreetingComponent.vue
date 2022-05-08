<!--<template>-->
<!--<div id="greeting">-->
<!--    <h3>Greeting component</h3>-->
<!--    <p>Username: {{ username }}</p>-->
<!--</div>-->
<!--</template>-->

<!--<template>-->
<!--    <div div="signin">-->
<!--        <div class="login-form">-->
<!--            <b-card-->
<!--                    title="Login"-->
<!--                    tag="article"-->
<!--                    style="max-width: 20rem;"-->
<!--                    class="mb-2"-->
<!--            >-->
<!--                <div>-->
<!--                    <b-alert-->
<!--                            :show="dismissCountDown"-->
<!--                            dismissible-->
<!--                            variant="danger"-->
<!--                            @dismissed="dismissCountDown=0"-->
<!--                            @dismiss-count-down="countDownChanged"-->
<!--                            > {{ alertMessage }}-->
<!--                    </b-alert>-->
<!--                </div>-->
<!--                <div>-->
<!--                    <b-form-input type="text" placeholder="Username" v-model="username" />-->
<!--                    <div class="mt-2"></div>-->

<!--                    <b-form-input type="password" placeholder="Password" v-model="password" />-->
<!--                    <div class="mt-2"></div>-->
<!--                </div>-->

<!--                <b-button v-on:click="login" variant="primary">Login</b-button>-->

<!--                <hr class="my-4" />-->

<!--                <b-button variant="link">Forget password?</b-button>-->
<!--            </b-card>-->
<!--        </div>-->
<!--    </div>-->
<!--</template>-->

<template>
    <el-card class="box-card">
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
        <el-button variant="primary"><a href="/login">Login</a></el-button>

    </el-card>
</template>


<!--<template>-->
<!--    <div class="bottom">-->
<!--        <el-card class="box-card">-->
<!--            <template #header>-->
<!--                <div class="card-header">-->
<!--                    <span>Sign In</span>-->
<!--                </div>-->
<!--            </template>-->
<!--            <div>-->
<!--                <el-input type="text" placeholder="Username" v-model="username" />-->
<!--                <div style="margin: 10px 0" />-->

<!--                <el-input type="password" placeholder="Password" v-model="password" />-->
<!--                <div style="margin: 10px 0" />-->
<!--            </div>-->

<!--            <el-button variant="primary" v-on:click="login">Login</el-button>-->

<!--        </el-card>-->
<!--    </div>-->
<!--</template>-->



<script>
    import axios from 'axios'

    axios.defaults.baseURL = 'http://localhost:8080/';

    export default {
        name: 'SignIn',
        data() {
            return {
                username: 'ann2',
                name: 'Mae',
                password: '1234',
                dismissSecs: 5,
                dismissCountDown: 0,
                alertMessage: 'Request error',
            }
        },
        methods: {
            login() {
                axios.post("/register/" + this.$data.username +"/", { 'name': this.$data.name, 'password': this.$data.password })
                .then(response => {
                    this.$data.username = "Yes";
                    console.log('kek')
                    this.$store.dispatch('login', {'token': response.data, 'username': this.$data.username});
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
            loadGreeting() {
                axios.get('/common/cards/all/', { params: {} })
                .then(response => {
                    this.$data.username = response.data;
                })
                .catch(error => {
                    console.log('ERROR: ' + error);
                })
            }
        },
        mounted() {
            this.loadGreeting();
        }
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


.bottom {
margin-top: 40px;
display: block;
left: 50%;
margin-right: -50%;
justify-content: center
}
</style>

<!--        this.$router.push('/home')-->
<!--change token to id-->