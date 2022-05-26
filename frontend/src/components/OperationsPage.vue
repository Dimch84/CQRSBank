<template>
    <div style="margin: 20px" />
    <el-row class="block-col-2">
        <el-col :span="3">
            <el-dropdown>
                <span class="el-dropdown-link">
                    Card: {{ card.name }} <el-icon class="el-icon--right"><arrow-down /></el-icon>
                </span>
                <template #dropdown>
                <el-dropdown-menu>
                    <el-dropdown-item v-for="item in cards" :key="item.id" index="item.id" v-on:click="changeCard(item.id)">{{ "Card: " + item.name }}</el-dropdown-item>
                    <el-dropdown-item @click="dialogVisible = true">+ Add Card</el-dropdown-item>
                </el-dropdown-menu>
                </template>
            </el-dropdown>
        </el-col>
        <el-col :span="1">
        <el-button type="primary" @click="operationDialogVisible = true">Pay</el-button>
        </el-col>
        <el-col :span="1">
        <el-button type="primary" @click="infoDialogVisible = true">Info</el-button>
        </el-col>
        <el-col :span="11">
        </el-col>
        <el-col :span="8">
        <div> Balance: {{ cur_balance }} </div>
        </el-col>
    </el-row>
    <el-divider />

    <h1>History</h1>

    <el-scrollbar max-height="400px" width="40%">
    <div v-for="elem in history" :key="elem.login" class="scrollbar-demo-item">
    <el-col :span="6" >
        <span> From : {{ elem.from }} </span>
    </el-col>
    <el-col :span="6" >
        <span> To : {{ elem.to }}</span>
    </el-col>
    <el-col :span="6" >
        <span> Money : {{ elem.money }}</span>
    </el-col>
    </div>
    </el-scrollbar>

    <el-dialog v-model="dialogVisible" title="Create Card" width="40%" draggable>
    <el-form :inline="true" class="demo-form-inline">
    <el-input type="text" placeholder="Card Name" v-model="add_name" />
    <el-input type="text" placeholder="Card Number" v-model="add_number" />
    <el-select v-model="add_type" class="m-2" placeholder="Select" size="large">
        <el-option
                v-for="item in cardTypes"
                :key="item"
                :label="item"
                :value="item"
        />
    </el-select>
    </el-form>
    <template #footer>
        <span class="dialog-footer">
            <el-button @click="dialogVisible = false">Cancel</el-button>
            <el-button type="primary" v-on:click="addCard()" @click="dialogVisible = false">Create</el-button>
        </span>
    </template>
    </el-dialog>


    <el-dialog v-model="operationDialogVisible" title="Pay" width="40%" draggable>

    <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="From">
            <el-input v-model="card.cardNumber" disabled placeholder="Card" />
        </el-form-item>
        <el-form-item label="To">
            <el-input v-model="to" placeholder="Card" />
        </el-form-item>
        <el-form-item label="Money">
            <el-input v-model="money" placeholder="Money ($)" />
        </el-form-item>
    </el-form>

    <template #footer>
    <span class="dialog-footer">
        <el-button @click="operationDialogVisible = false">Cancel</el-button>
        <el-button type="primary" v-on:click="transfer()" @click="operationDialogVisible = false">Pay</el-button>
    </span>
    </template>
    </el-dialog>


    <el-dialog v-model="infoDialogVisible" title="Info" width="30%" draggable>
    <el-descriptions column=1>
        <el-descriptions-item label="Name">{{ card.name }}</el-descriptions-item>
        <el-descriptions-item label="Type">{{ card.type }}</el-descriptions-item>
        <el-descriptions-item label="Card Number">{{ card.cardNumber }}</el-descriptions-item>
        <el-descriptions-item label="CVV">{{ card.cvv }}</el-descriptions-item>
        <el-descriptions-item label="Exp. Date">{{ card.expDate }}</el-descriptions-item>
    </el-descriptions>

    <template #footer>
    <span class="dialog-footer">
        <el-button type="primary" @click="infoDialogVisible = false">Ok</el-button>
    </span>
    </template>
    </el-dialog>


</template>


<script setup>
    import { ArrowDown } from '@element-plus/icons-vue'
    import { ref } from 'vue'

    const dialogVisible = ref(false)
    const operationDialogVisible = ref(false)
    const infoDialogVisible = ref(false)

    const cardTypes = ['Visa', 'Mastercard', 'МИР']
</script>

<script>
    import axios from 'axios'

    export default {
        name: 'OperationsScript',
        data() {
            return {
                to: '',
                money: 0,

                add_name: '',
                add_type: '',
                add_number: '', // TODO: put it work on server

                history : [],
                card: {},
                cardId: -1,
                cur_balance: '',

                cards: [],
            }
        },
        methods: {
            loadCards() {
                const instance = axios.create({
                    baseURL: 'http://localhost:8080/',
                    auth: { username: this.$store.getters.getLogin, password: this.$store.getters.getPassword },
                });

                instance.get("/cqrs/accounts/" + this.$store.getters.getAccountId + "/cards/", {})
                .then(response => {
                    this.$data.cards = response.data
                    if (this.$data.cards.length != 0 && this.$data.cardId === -1) {
                        this.changeCard(this.$data.cards[0].id)
                    }
                }, error => { console.log(error) })
                .catch(e => { console.log(e); })
            },
            transformHistory() {
                const instance = axios.create({
                    baseURL: 'http://localhost:8080/',
                    auth: { username: this.$store.getters.getLogin, password: this.$store.getters.getPassword },
                });
                this.$data.history.map((elem) => {
                    if (elem.idTo == this.$data.cardId) {
                        elem.to = this.$data.card.cardNumber

                        instance.get("/cqrs/cards/" + elem.idFrom, {})
                        .then(response => {
                            elem.from = response.data.cardNumber
                        }, error => { console.log(error) })
                        .catch(e => { console.log(e); })
                    } else {
                        elem.from = this.$data.card.cardNumber

                        instance.get("/cqrs/cards/" + elem.idTo, {})
                        .then(response => {
                            elem.to = response.data.cardNumber
                        }, error => { console.log(error) })
                        .catch(e => { console.log(e); })
                    }
                    return elem
                })
            },
            loadCurrentCard() {
                const instance = axios.create({
                    baseURL: 'http://localhost:8080/',
                    auth: { username: this.$store.getters.getLogin, password: this.$store.getters.getPassword },
                });

                instance.get("/cqrs/cards/" + this.$data.cardId, {})
                .then(response => {
                    this.$data.card = response.data
                }, error => { console.log(error) })
                .catch(e => { console.log(e); })

                instance.get("/cqrs/cards/" + this.$data.cardId + "/money/", {})
                .then(response => {
                    console.log("Money : " , response.data)
                    this.$data.cur_balance = response.data
                    console.log("Cur_balance: ", this.$data.cur_balance)
                }, error => {
                    console.log(error)
                })
                .catch(e => {
                    console.log(e);
                })

                instance.get("/cqrs/cards/" + this.$data.cardId + "/history/pays", {})
                .then(response => {
                    console.log("Card History: ", response.data)
                    this.$data.history = response.data
                    this.transformHistory()
                    console.log("History: ", this.$data.history)
                }, error => {
                    console.log(error)
                })
                .catch(e => {
                    console.log(e);
                })

            },
            addCard() {
                const instance = axios.create({
                    baseURL: 'http://localhost:8080/',
                    auth: { username: this.$store.getters.getLogin, password: this.$store.getters.getPassword },
                });

                instance.post(
                    "/cqrs/cards/", {
                        "accountId": this.$store.getters.getAccountId,
                        "cardNumber": this.$data.add_number,
                        "cvv": "600",
                        "expDate": "12/44",
                        "id": 0,
                        "name": this.$data.add_name,
                        "type": this.$data.add_type
                    })
                .then(response => {
                    console.log(response)
                    this.loadCards()          // TODO: Add page reload this.$router.go() maybe, not loadAccounts
                    location.reload();
                    this.changeCard(response.data)
                    this.$data.add_name = ''
                    this.$data.add_type = ''
                    this.$data.add_number = ''
                }, error => {
                    console.log(error)
                })
                .catch(e => {
                    console.log(e);
                })

            },
            changeCard(cardId) {
                console.log("Change Card:", cardId)
                this.$data.cardId = cardId
                this.loadCurrentCard()
            },
            transfer() {
                const instance = axios.create({
                    baseURL: 'http://localhost:8080/',
                    auth: { username: this.$store.getters.getLogin, password: this.$store.getters.getPassword },
                });

                instance.get("/cqrs/cards/byNumber/" + this.$data.to, {})
                .then(response => {
                    console.log(response)
                    if (response == "List is empty.") {
                        instance.post("/cqrs/cards/" + this.$data.cardId + "/payment/", {"money": this.$data.money})
                        .then(response => {
                            console.log(response)
                            this.$data.to = ''
                            this.$data.money = 0
                            location.reload();
                    }, error => { console.log(error) })
                        .catch(e => { console.log(e); })
                    } else {
                        instance.post("/cqrs/cards/" + this.$data.cardId +"/localTransfer/", {"money": this.$data.money, "idTo": response.data.id})
                        .then(response => {
                            console.log(response)
                            this.$data.to = ''
                            this.$data.money = 0
                            location.reload();
                        }, error => { console.log(error) })
                        .catch(e => { console.log(e); })
                    }
                }, error => { console.log(error) })
                .catch(e => { console.log(e); })

            },
            show() {
                axios.get("/cqrs/cards/" + this.$data.card, {})
                .then(response => {
                    console.log(response)

                    axios.get("/cqrs/accounts/" + response.data.account_id, {})
                    .then(r => {
                        console.log(r)
                        this.$data.cur_balance = r.data.money
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
        },
        beforeMount() {
            this.loadCards()
        }

    }
</script>

<style scoped>
.example-showcase .el-dropdown-link {
cursor: pointer;
color: var(--el-color-primary);
display: flex;
align-items: center;
}

.infinite-list {
height: 600px;
padding: 0;
margin: 80px;
list-style: none;
}
.infinite-list .infinite-list-item {
display: flex;
align-items: center;
justify-content: center;
height: 50px;
background: var(--el-color-primary-light-9);
margin: 10px;
color: var(--el-color-primary);
}
.infinite-list .infinite-list-item + .list-item {
margin-top: 10px;
}
.scrollbar-demo-item {
display: flex;
align-items: center;
justify-content: center;
height: 50px;
margin: 10px;
text-align: center;
border-radius: 4px;
background: var(--el-color-primary-light-9);
color: var(--el-color-primary);
}

</style>
