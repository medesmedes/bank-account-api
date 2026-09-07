# bank-account-api

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Endpoints

Account numbers are assigned by the server (`DKBD` + a sequence), so create an account
first and reuse the number it returns.

Create an account:

```shell script
curl -X 'POST' \
  'http://localhost:8080/accounts' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "firstName": "Harald",
  "lastName": "Blaatand"
}'
```

Read an account and its balance:

```shell script
curl -X 'GET' \
  'http://localhost:8080/accounts/DKBD00000001' \
  -H 'accept: application/json'
```

Deposit money:

```shell script
curl -X 'POST' \
  'http://localhost:8080/accounts/DKBD00000001/deposit' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "amount": 1.00
}'
```

Transfer money to another account (source in the path, target in the body):

```shell script
curl -X 'POST' \
  'http://localhost:8080/accounts/DKBD00000001/transfer' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "toAccountNumber": "DKBD00000002",
  "amount": 25.00
}'
```

Convert 100 DKK to USD (Task 2):

```shell script
curl -X 'GET' \
  'http://localhost:8080/exchange' \
  -H 'accept: application/json'
  ```


## Trying it without curl

- Swagger UI: <http://localhost:8080/q/swagger-ui/>
- IntelliJ HTTP client: the `.http` files under `src/test/http` — run each request from the gutter.