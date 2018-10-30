module.exports = {
    baseUrl: '/',
    assetsDir: 'static',
    outputDir: './target/dist/',
    lintOnSave: true,
    productionSourceMap: true,
    devServer: {
        port: 8080,
        proxy: {
            '/api': {
                'target': "http://localhost:9090",
                'changeOrigin': true
            }
        }
    }
}
