import { createStore } from "vuex";

export let store = createStore({
    state: {
        userId: localStorage.getItem('user-id') || '',
        login: localStorage.getItem('user-login') || '',
        password: localStorage.getItem('user-password') || '',
        username: localStorage.getItem('user-name') || '',
        accountName: localStorage.getItem('account-name') || '',
        accountId: localStorage.getItem('account-id') || ''
    },
    getters: {
        isAuthenticated: state => {
            if (state.login != null && state.login != '') {
                return true;
            } else {
                return false;
            }
        },
        getUsername: state => {
            return state.username;
        },
        getLogin: state => {
            console.log("getlogin: ", state.login)
            return state.login
        },
        getUserId: state => {
            return state.userId
        },
        getAccountId: state => {
            return state.accountId
        },
        getPassword: state => {
            console.log("getpassword: ", state.password)
            return state.password
        },
        getAccountName: state => {
            return state.accountName
        },
    },
    mutations: {
        auth_login: (state, user) => {
            localStorage.setItem('user-login', user.login);
            localStorage.setItem('user-password', user.password);
            localStorage.setItem('user-name', user.name);
            localStorage.setItem('user-id', user.id);
            state.login = user.login;
            state.username = user.username;
            state.userId = user.id;
            state.password = user.password;
            console.log(user.password)
        },
        auth_logout: (state) => {
            state.login = '';
            state.username = '';
            state.userId = '';
            state.password = '';
            state.accountId = '';
            state.accountName = '';
            localStorage.removeItem('user-name');
            localStorage.removeItem('user-login');
            localStorage.removeItem('user-id');
            localStorage.removeItem('user-password');
            localStorage.removeItem('account-id');
            localStorage.removeItem('account-name');
        },
        set_account: (state, account) => {
            localStorage.setItem('account-id', account.id);
            localStorage.setItem('account-name', account.name);
            state.accountId = account.id;
            state.accountName = account.name;
        }
    },
    actions: {
        login: (context, user) => {
            context.commit('auth_login', user)
        },
        logout: (context) => {
            context.commit('auth_logout');
        },
        selectAccount: (context, account) => {
            context.commit('set_account', account)
        },
    }
});