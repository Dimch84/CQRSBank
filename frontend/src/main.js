import { createApp } from 'vue'
import App from './App.vue'
import { store } from './store/index.js';
//import BootstrapVue3 from 'bootstrap-vue-3'
//import BootstrapVue from 'bootstrap-vue'
//import 'bootstrap/dist/css/bootstrap.css'
//import 'bootstrap-vue/dist/bootstrap-vue.css'

//
//import "bootstrap/dist/css/bootstrap.min.css"
//import "bootstrap"


//import 'bootstrap'
//
//import 'bootstrap/dist/css/bootstrap.css'
//import 'bootstrap-vue-3/dist/bootstrap-vue-3.css'
// npm install element-plus --save
//

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router'

createApp(App).use(store).use(router).use(ElementPlus).mount('#app')
//.use(BootstrapVue3)

//…
//
//Vue.use(BootstrapVue)

//
//...
//
//new Vue({
//     router,
//     store,
//     render: h => h(App)
//}).$mount('#app')

//
////import Vue from 'vue'
//import App from './App.vue'
//import router from './router'
//
//
//
//
//createApp({
//    components: {
//          router,
//          render: h => h(App),
//    }
//}).mount('#app')

//new Vue({
//}).$mount('#app')
