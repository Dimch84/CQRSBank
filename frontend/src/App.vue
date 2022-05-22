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
    <el-sub-menu  index="accounts" style='margin-left: auto;' v-if="this.$store.getters.isAuthenticated">
      <template #title>Account: {{ this.$store.getters.getAccountName }}</template>
      <el-menu-item v-for="item in accounts" :key="item" index="item" v-on:click="changeAccount(item.name, item.id)">{{ "Account: " + item.name }}</el-menu-item>
      <el-menu-item index="add account" @click="dialogVisible = true">+ Add Account</el-menu-item>
    </el-sub-menu>
  </el-menu>

  <el-dialog v-model="dialogVisible" title="Create Account" width="40%" draggable>
    <el-input type="text" placeholder="Account Name" v-model="curAccountName" />
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" v-on:click="addAccount()" @click="dialogVisible = false">Create</el-button>
      </span>
    </template>
  </el-dialog>

  <component :is="currentView" />
</template>

<!--TODO: проблемы с навигацией в menu-->

<script setup>
  const dialogVisible = ref(false)
</script>

<script>
  import NotFound from '@/components/NotFound'
  import HomePage from '@/components/HomePage'
  import UserPage from '@/components/UserPage'
  import SignIn from '@/components/SignIn'
  import SignUp from '@/components/SignUp'
  import AccountsPage from '@/components/AccountsPage'
  import HistoryPage from '@/components/HistoryPage'
  import OperationsPage from '@/components/OperationsPage'

  import axios from 'axios'
  import { ref } from 'vue'
  axios.defaults.baseURL = 'http://localhost:8080/';



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
        currentPath: window.location.hash,
        accounts: ref([{ name: 'first', id: 0 }, { name: 'second', id: 1 }]),
        curAccountName: ''
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
      },
      changeAccount(accountName, accountId) {
        this.$store.dispatch('selectAccount', { 'name': accountName, 'id': accountId });
        console.log("Change Account:", accountName, accountId)
        // TODO: Add page reload this.$router.go() maybe
      },
      addAccount() {
        console.log("Add Account", this.$data.curAccountName)

        axios.post("/cqrs/accounts/", { 'name': this.$data.curAccountName, 'money': 0, 'planId': 0, 'userId': this.$store.getters.getUserId })
        .then(response => {
          console.log(response)
          this.changeAccount(this.$data.curAccountName, response.data)

          this.$data.curAccountName = ''
          this.loadAccounts()          // TODO: Add page reload this.$router.go() maybe, not loadAccounts
        }, error => {
          this.$data.alertMessage = (error.length < 150) ? error.message : 'Request error';
          console.log(error)
        })
        .catch(e => {
          console.log(e);
          this.showAlert();
        })

      },
      loadAccounts() {
        console.log("Load Accounts")
        axios.get("/cqrs/user/" + this.$store.getters.getLogin + "/accounts/", {})
        .then(response => {
          console.log(response.data)
          this.$data.accounts = response.data
          console.log(this.$data.accounts)
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
    }
  }
</script>

<!--TODO: support accountId-->


<style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }

</style>


