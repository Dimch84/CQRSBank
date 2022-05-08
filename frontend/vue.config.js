const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
    transpileDependencies: true
})
resolve: {
    devServer: {
        proxy: 'http://localhost:8080/'
    }
    alias: {
        vue: 'vue/dist/vue.js'
    }
}
