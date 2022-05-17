import { createStore } from "vuex";

export let store = createStore({
    state: {
        userId: localStorage.getItem('user-id') || '',
        login: localStorage.getItem('user-login') || '',
        username: localStorage.getItem('user-name') || '',
        accountName: localStorage.getItem('account-name') || '',
        accountId: localStorage.getItem('account-id') || ''
    },
    getters: {
        isAuthenticated: state => {
            if (state.token != null && state.token != '') {
                return true;
            } else {
                return false;
            }
        },
        getUsername: state => {
            return state.username;
        },
        getLogin: state => {
            return state.login
        },
        getUserId: state => {
            return state.userId
        },
        getAccountId: state => {
            return state.accountId
        },
        getAccountName: state => {
            return state.accountName
        },
    },
    mutations: {
        auth_login: (state, user) => {
            localStorage.setItem('user-token', user.token);
            localStorage.setItem('user-name', user.name);
            localStorage.setItem('user-id', user.id);
            state.login = user.login;
            state.username = user.username;
            state.userId = user.id;
        },
        auth_logout: (state) => {
            state.token = '';
            state.username = '';
            state.userId = '';
            localStorage.removeItem('user-name');
            localStorage.removeItem('user-login');
            localStorage.removeItem('user-id');
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