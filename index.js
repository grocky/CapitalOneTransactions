const _ = require('lodash');
const client = require('axios');
const Promise = require('bluebird');

const HOST = "https://2016.api.levelmoney.com";
const BASE_URI = "/api/v2/core";

class LevelLabsClient {

  constructor(uid, apiToken, email, password) {
    this.uid = uid;
    this.apiToken = apiToken;
    this.email = email;
    this.password = password;
    this.token = null;
  }

  buildUrl(uri) {
    return HOST + BASE_URI + uri;
  }

  getAllTransactions() {
    return (this.token ? Promise.resolve() : this.refreshToken())
      .then(() => {
        const body = {
          args: {
            uid: this.uid,
            token: this.token,
            'api-token': this.apiToken
          }
        };

        console.log('fetching data');
        return client.post(this.buildUrl('/get-all-transactions'), body)
      })
      .then(res => res.data.transactions)
      .catch(console.error)
  }

  refreshToken() {
    const body = {
      email: this.email,
      password: this.password,
      args: {"api-token": this.apiToken}
    };

    console.log('refreshing token...');

    return client.post(this.buildUrl('/login'), body)
      .then(res => res.data)
      .then(data => {
        console.log('new token', data.token);
        this.token = data.token
      })
      .catch(console.error)
  }
}

function buildMonthTransactionMap() {
  const apiClient = new LevelLabsClient(1110590645, 'AppTokenForInterview', 'interview@levelmoney.com', 'password2');
  return apiClient.getAllTransactions()
    .then(transactions => {
      console.log('building map');

      return _.reduce(transactions, (map, transaction) => {
        const transactionTime = new Date(transaction['transaction-time']);

        const key = transactionTime.getFullYear() + '-' + transactionTime.getMonth();

        if (!map.has(key)) {
          map.set(key, []);
        }

        map.get(key).push({amount: (transaction.amount / 10000)});

        return map;
      }, new Map);
    })
    .catch(console.error);
}

function buildSummary(dateKey, transactions) {
  const credits = _.reduce(transactions, (acc, transaction) => acc + (transaction.amount > 0 ? transaction.amount : 0), 0);
  const debits = _.reduce(transactions, (acc, transaction) => acc + (transaction.amount < 0 ? transaction.amount : 0), 0);

  let summary = {};
  summary[dateKey] = {spent: formatCurrency(debits), income: formatCurrency(credits)};

  return summary
}

function formatCurrency(amount) {
  return "$" + Math.abs(amount).toFixed(2);
}

buildMonthTransactionMap()
  .then(map => Promise.map(map.entries(), entry => buildSummary(entry[0], entry[1])))
  .then(console.log)
  .catch(console.error);
