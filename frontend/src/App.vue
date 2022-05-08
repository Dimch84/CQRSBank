<!--<template>-->
<!--  <img alt="Vue logo" src="./assets/logo.png">-->
<!--  <HelloWorld msg="Welcome to Your Vue.js App"/>-->
<!--</template>-->
        <!--<div id="app">-->
        <!--    <router-view></router-view>-->
        <!--</div>-->


<template>
  <el-menu
          class="menu"
          mode="horizontal"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#fff"
  >
    <el-menu-item index="/"><a href="/#/home">CQRS</a></el-menu-item>
    <el-menu-item index="/user" v-if="this.$store.getters.isAuthenticated"><a href="/#/user">User Page</a></el-menu-item>
    <el-menu-item index="/operaions" v-if="this.$store.getters.isAuthenticated"><a href="/#/operations">Operations</a></el-menu-item>
    <el-menu-item index="/login" v-if="!this.$store.getters.isAuthenticated"><a href="/#/login">Sign In</a></el-menu-item>
    <el-menu-item index="/register" v-if="!this.$store.getters.isAuthenticated"><a href="/#/register">Sign Up</a></el-menu-item>
    <el-menu-item index="/login" style='margin-left: auto;' v-if="this.$store.getters.isAuthenticated" v-on:click="logout"><a href="/#/login">Logout</a></el-menu-item>
  </el-menu>
    <component :is="currentView" />

</template>
<!--&lt;!&ndash;        v-if="this.$store.getters.isAuthenticated"&ndash;&gt; add this for all-->

<!--<el-menu-item index='/' router="true">Home</el-menu-item>-->
<!--<el-menu-item index='/history' router="true">History</el-menu-item>-->

<!--<el-sub-menu index="2">-->
<!--<template #title>Workspace</template>-->
<!--<el-menu-item index="2-1">item one</el-menu-item>-->
<!--<el-menu-item index="2-2">item two</el-menu-item>-->
<!--<el-menu-item index="2-3">item three</el-menu-item>-->
<!--<el-sub-menu index="2-4">-->
<!--<template #title>item four</template>-->
<!--<el-menu-item index="2-4-1">item one</el-menu-item>-->
<!--<el-menu-item index="2-4-2">item two</el-menu-item>-->
<!--<el-menu-item index="2-4-3">item three</el-menu-item>-->
<!--        </el-sub-menu>-->
<!--        </el-sub-menu>-->

<!--<div id="app">-->
<!--<b-navbar style="width: 100%" type="dark" variant="dark">-->
<!--  <b-navbar-brand id="nav-brand" href="#">Kotlin+Spring+Vue</b-navbar-brand>-->
<!--</b-navbar>-->
<!--</div>-->
<!--<router-view></router-view>-->
<!--<router-link to="/"><img height="30px" src="./assets/logo.png" alt="Kotlin+Spring+Vue"/></router-link>-->
<!--<router-link to="/"><img height="30px" src="./assets/logo.png" alt="Kotlin+Spring+Vue"/></router-link>-->
<!--<router-link to="/"><img height="30px" src="./assets/logo.png" alt="Kotlin+Spring+Vue"/></router-link>-->
<!--<router-link to="/user" class="nav-link text-light" v-if="this.$store.getters.isAuthenticated">User</router-link>-->
<!--<router-link to="/register" class="nav-link text-light" v-if="!this.$store.getters.isAuthenticated">Register</router-link>-->
<!--<router-link to="/login" class="nav-link text-light" v-if="!this.$store.getters.isAuthenticated">Login</router-link>-->
<!--<a href="#" class="nav-link text-light" v-if="this.$store.getters.isAuthenticated" v-on:click="logout">Logout </a>-->


        <!--<div id="app">-->
<!--<router-view></router-view>-->
<!--</div>-->



<script>
import HelloWorld from '@/components/HelloWorld'
import NotFound from '@/components/NotFound'
import GreetingComponent from '@/components/GreetingComponent'
import HomePage from '@/components/HomePage'
import UserPage from '@/components/UserPage'
import SignIn from '@/components/SignIn'
import SignUp from '@/components/SignUp'
import AccountsPage from '@/components/AccountsPage'
import HistoryPage from '@/components/HistoryPage'
import OperationsPage from '@/components/OperationsPage'


const routes = {
  '/hello-world' : HelloWorld,
  '/greeting' : GreetingComponent,
  '/' : HomePage,
  '/home' : HomePage,
  '/login' : SignIn,
  '/register' : SignUp,
  '/user' : UserPage,
  '/accounts' : AccountsPage,
  '/history' : HistoryPage,
  '/operations' : OperationsPage
};

export default {
  data() {
    return {
      currentPath: window.location.hash
    }
  },
  computed: {
    currentView() {
      return routes[this.currentPath.slice(1) || '/'] || NotFound
    }
  },
  mounted() {
    window.addEventListener('hashchange', () => {
      this.currentPath = window.location.hash
    })
  },
  methods: {
    logout() {
      this.$store.dispatch('logout');
    }
  }
}
</script>


<!--        export default {-->
<!--        name: 'app',-->
<!--        }-->

        <!--        import HelloWorld from './components/HelloWorld.vue'-->
        <!--  components: {-->
        <!--    HelloWorld-->
        <!--  }-->
        <!--export default {-->
        <!--name: 'app'-->
        <!--}-->



<style>
        #app {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
        }
</style>
        <!--        margin-top: 60px;-->


