<template>
    <div class="home">
        <img alt="Vue logo" src="../assets/logo.png">
        <HelloWorld :msg="msg"/>
    </div>
</template>

<script>
    // @ is an alias to /src
    import HelloWorld from '../components/HelloWorld.vue';
    import {hello} from '../api/hello';

    export default {
        name: 'home',
        components: {
            HelloWorld,
        },
        data() {
            return {
                msg: "Welcome to Your Vue.js App"
            }
        },
        mounted() {
            hello().then(res => {
                this.msg = "Welcome to Your Vue.js App, " + res.data;
            })
        }
    };
</script>
