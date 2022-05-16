import { createApp } from 'vue'
import App from './App.vue'
import { store } from './store/index.js';


import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

createApp(App).use(store).use(ElementPlus).mount('#app')
