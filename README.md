# SETUP ENVIRONMENT

## Database

Init server itself:

_It's needed to change persistent volume directory._

`docker-compose  -f ./mysql-docker-compose.yml up -d`

Setup database:
`create database wagawin;`

Sequentially execute tests to fill database:

`com.wagawin.testapp.TestAppApplicationTests.createHouses`\
`com.wagawin.testapp.TestAppApplicationTests.createMeals`\
`com.wagawin.testapp.TestAppApplicationTests.createChildren`

## Redis

Init server itself:

`docker-compose  -f ./redis-docker-compose.yml up -d`

# TEST

## REST API:


Create new house:

```http request
POST http://localhost:8080/house
Content-Type: application/json

{
  "address": "Some address 1",
  "zipCode": "123456",
  "type": "FLAT"
}
```

Response:

```json
{
  "id": 10302,
  "address": "Some address 1",
  "zipCode": "123456",
  "type": "FLAT"
}
```

Get created house:

```http request
GET http://localhost:8080/house?id=10302
```

Response:

```json
{
  "id": 10302,
  "address": "Some address 1",
  "zipCode": "123456",
  "type": "FLAT"
}
```

Get created house from cache:

```http request
GET http://localhost:8080/cache/house?id=10302
```

Response:

```json
{
  "id": 10302,
  "address": "Some address 1",
  "zipCode": "123456",
  "type": "FLAT"
}
```

Create person with same house id:

```http request
POST http://localhost:8080/person
Content-Type: application/json

{
  "name": "Some Name",
  "age": 30,
  "house": {
    "id": 10302
  }
}
```

Response:

```json
{
  "id": 10202,
  "name": "Some Name",
  "age": 30,
  "house": {
    "id": 10302,
    "address": "Some address 1",
    "zipCode": "123456",
    "type": "FLAT"
  }
}
```

Request person:

```http request
GET http://localhost:8080/person?id=10202
```

Response:

```json
{
  "id": 10202,
  "name": "Some Name",
  "age": 30,
  "house": {
    "id": 10302,
    "address": "Some address 1",
    "zipCode": "123456",
    "type": "FLAT"
  }
}
```

Request person from cache:

```http request
GET http://localhost:8080/cache/person?id=10202
```

Response:

```json
{
  "id": 10202,
  "name": "Some Name",
  "age": 30,
  "house": {
    "id": 10302,
    "address": "Some address 1",
    "zipCode": "123456",
    "type": "FLAT"
  }
}
```

Create child with created parent:

```http request
POST http://localhost:8080/child
Content-Type: application/json

{
  "name": "Some Name",
  "type: "Son",
  "age": 10,
  "parent": {
    "id": 10202
  }
}
```

Response:

```json
{
  "id": 186615,
  "name": "Some Name",
  "age": 10,
  "type": "Son",
  "parent": {
    "id": 10202,
    "name": "Some Name",
    "age": 30
  },
  "favouriteMeal": null
}
```

Add meal:

```http request
POST http://localhost:8080/child/meal?childId=186615
Content-Type: application/json

{
  "id": 1,
  "favIndex": 1
}
```

Response:

```Saved```

```http request
GET http://localhost:8080/child/info?id=186615
```

Response:

```json
{
  "id": 186615,
  "name": "Some Name",
  "age": 10,
  "type": "Son",
  "parent": {
    "id": 10202,
    "name": "Some Name",
    "age": 30
  },
  "favouriteMeal": {
    "id": 1,
    "name": "Meal 0",
    "invented": "2011-10-11"
  }
}
```

Set color for child:
```http request
POST http://localhost:8080/color?childId=186615&color=white
```

Response

```json
{
  "bicycleColor": "white"
}
```

Request color:

```http request
GET http://localhost:8080/color?id=186615
```

Response:

```json
{
  "bicycleColor": "white"
}
```

Get child data from cache:

```http request
GET http://localhost:8080/cache/child/info?id=186615
```

Response:

```json
{
  "id": 186615,
  "name": "Some Name",
  "age": 10,
  "type": "Son",
  "parent": {
    "id": 10202,
    "name": "Some Name",
    "age": 30
  },
  "favouriteMeal": {
    "id": 1,
    "name": "Meal 0",
    "invented": "2011-10-11"
  }
}
```

Get color data from cache:

```http request
GET http://localhost:8080/cache/color?id=186615
```

Response:

```json
{
  "bicycleColor": "white"
}
```

Get parents summary:

```http request
GET http://localhost:8080/persons/children
```

Response:

```json
{
  "parentSummary": [
    9,
    31,
    74,
    219,
    331,
    669,
    888,
    1140,
    1187,
    1264,
    1101,
    957,
    738,
    539,
    376,
    205,
    145,
    69,
    29,
    17,
    8,
    3,
    1
  ]
}
```

## LOAD TEST

Test machine: 
* Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz 1.99 GHz
* 16,0 Gb mem

Use jMeter file `Wagawin Thread Group.jmx`

Run independent test for /house\
Throughput: ~350-400 ops

Run independent test for /cache/house\
Throughput started at same: ~400-500 ops, but then increased to ~1000-1500 ops 

Run independent test for /child/info\
Throughput: ~200-250 ops

Run independent test for /cache/child/info\
Throughput started at same: ~200-250 ops, but then increased to ~1000-1200 ops\
It takes much more time to get to the highest throughput values because of the bigger cache warmup time.