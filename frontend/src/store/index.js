import { createStore } from "vuex";

export let store = createStore({
    state: {
        token: localStorage.getItem('user-token') || '',
        username: localStorage.getItem('user-name') || '',
        authorities: localStorage.getItem('authorities') || '',
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
        getAuthorities: state => {
            return state.authorities;
        },
        getToken: state => {
            return state.token;
        }
    },
    mutations: {
        auth_login: (state, user) => {
            localStorage.setItem('user-token', user.token);
            localStorage.setItem('user-name', user.name);
            state.token = user.token;
            state.username = user.username;
        },
        auth_logout: (state) => {
            state.token = '';
            state.username = '';
            state.authorities = [];
            localStorage.removeItem('user-token');
            localStorage.removeItem('user-name');
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