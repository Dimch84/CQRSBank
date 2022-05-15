import { createStore } from "vuex";

export let store = createStore({
    state: {
        login: localStorage.getItem('user-login') || '',
        username: localStorage.getItem('user-name') || '',
        authorities: localStorage.getItem('authorities') || '',
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
        getAuthorities: state => {
            return state.authorities;
        },
        getLogin: state => {
            return state.login
        }
    },
    mutations: {
        auth_login: (state, user) => {
            localStorage.setItem('user-login', user.login);
            localStorage.setItem('user-name', user.name);
            state.login = user.login
            state.username = user.username;
        },
        auth_logout: (state) => {
            state.login = '';
            state.username = '';
            state.authorities = [];
            localStorage.removeItem('user-name');
            localStorage.removeItem('user-login');
            localStorage.removeItem('user-authorities');
        }
    },
    actions: {
        login: (context, user) => {
            context.commit('auth_login', user)
        },
        logout: (context) => {
            context.commit('auth_logout');
        }
    }
});