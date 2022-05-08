<template>
    <h1>Make Operation</h1>
    <div style="margin: 50px 0" />
    <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="From">
            <el-input v-model="from " placeholder="Card" />
        </el-form-item>
        <el-form-item label="To">
            <el-input v-model="to" placeholder="Card" />
        </el-form-item>
        <el-form-item label="Money">
            <el-input v-model="money" placeholder="Money ($)" />
        </el-form-item>
        <el-form-item>
            <el-button type="primary"  v-on:click="transfer">Transfer Money</el-button>
        </el-form-item>
    </el-form>

    <div style="margin: 50px 0" />
    <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="Card">
            <el-input v-model="card" placeholder="Card" />
        </el-form-item>
        <el-form-item label="Money">
            <el-input v-model="on_card" placeholder="Money ($)" />
        </el-form-item>
        <el-form-item>
            <el-button type="primary"  v-on:click="show">Show Money</el-button>
        </el-form-item>
    </el-form>

</template>

<!--<el-select v-model="from" placeholder="Card">-->
<!--<el-option label="Zone one" value="shanghai" />-->
<!--<el-option label="Zone two" value="beijing" />-->
<!--</el-select>-->

<script>
    import axios from 'axios'

    export default {
        name: 'OperationsScript',
        data() {
            return {
                from: '',
                to: '',
                money: 0,
                card: '',
                on_card: '',
            }
        },
        methods: {
            transfer() {
                axios.post("/cards/" + this.$data.from +"/payment/", {"money": this.$data.money})
                .then(response => {
                    console.log(response)
                    this.$data.from = ''
                    this.$data.to = ''
                    this.$data.money = 0


                    axios.post("/cards/" + this.$data.to +"/receipt/", {"money": this.$data.money})
                    .then(error => { console.log(error) })
                    .catch(e => { console.log(e); })
                }, error => {
                    console.log(error)
                })
                .catch(e => {
                    console.log(e);
                })

            },
            show() {
            axios.get("/cards/" + this.$data.card, {})
                .then(response => {
                    console.log(response)

                    axios.get("/accounts/" + response.data.account_id, {})
                    .then(r => {
                        console.log(r)
                        this.$data.on_card = r.data.money
                    },
                    error => { console.log(error) })
                    .catch(e => { console.log(e); })
                }, error => {
                    console.log(error)
                })
                .catch(e => {
                    console.log(e);
            })

            }
        }
    }
</script>

<style>
</style>
