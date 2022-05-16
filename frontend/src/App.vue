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


<script>
  import NotFound from '@/components/NotFound'
  import HomePage from '@/components/HomePage'
  import UserPage from '@/components/UserPage'
  import SignIn from '@/components/SignIn'
  import SignUp from '@/components/SignUp'
  import AccountsPage from '@/components/AccountsPage'
  import HistoryPage from '@/components/HistoryPage'
  import OperationsPage from '@/components/OperationsPage'


  const routes = {
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


<style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
</style>


