#!/bin/bash

# Example UUIDs
CLIENT1="11111111-1111-1111-1111-111111111111"
CLIENT2="22222222-2222-2222-2222-222222222222"
BOOKING1="BK-001"
BOOKING2="BK-002"
BOOKING3="BK-003"
CONTAINER1="CONT-001"
CONTAINER2="CONT-002"
ORDER1="PUR-001"
ORDER2="PUR-002"
ORDER3="PUR-003"
INVOICE1="INV-001"
INVOICE2="INV-002"
INVOICE3="INV-003"

# 1. /api/email for CLIENT1 with containers (BOOKING1, CONTAINER1)
echo "[POST] /api/email for CLIENT1 with containers (BOOKING1, CONTAINER1)"
echo "Request: booking=$BOOKING1, containers=[$CONTAINER1]"
echo "Response:"
curl -s -X POST http://localhost:8080/api/email \
  -H "Content-Type: application/json" \
  -H "X-Client-ID: $CLIENT1" \
  -d '{
    "booking": "'$BOOKING1'",
    "containers": [
      { "container": "'$CONTAINER1'" }
    ]
  }'
echo -e "\n---\n"

# 2. /api/email for CLIENT1 with orders (BOOKING1, ORDER1, INVOICE1)
echo "[POST] /api/email for CLIENT1 with orders (BOOKING1, ORDER1, INVOICE1)"
echo "Request: booking=$BOOKING1, orders=[$ORDER1], invoices=[$INVOICE1]"
echo "Response:"
curl -s -X POST http://localhost:8080/api/email \
  -H "Content-Type: application/json" \
  -H "X-Client-ID: $CLIENT1" \
  -d '{
    "booking": "'$BOOKING1'",
    "orders": [
      { "purchase": "'$ORDER1'", "invoices": [ { "invoice": "'$INVOICE1'" } ] }
    ]
  }'
echo -e "\n---\n"

# 3. /api/email for CLIENT2 with containers and orders (BOOKING2, CONTAINER2, CONTAINER1, ORDER2, INVOICE2)
echo "[POST] /api/email for CLIENT2 with containers and orders (BOOKING2, CONTAINER2, CONTAINER1, ORDER2, INVOICE2)"
echo "Request: booking=$BOOKING2, containers=[$CONTAINER2,$CONTAINER1], orders=[$ORDER2], invoices=[$INVOICE2]"
echo "Response:"
curl -s -X POST http://localhost:8080/api/email \
  -H "Content-Type: application/json" \
  -H "X-Client-ID: $CLIENT2" \
  -d '{
    "booking": "'$BOOKING2'",
    "containers": [
      { "container": "'$CONTAINER2'" },
      { "container": "'$CONTAINER1'" }
    ],
    "orders": [
      { "purchase": "'$ORDER2'", "invoices": [ { "invoice": "'$INVOICE2'" } ] }
    ]
  }'
echo -e "\n---\n"

# 4. /api/email for CLIENT2 with containers and orders (BOOKING3, CONTAINER2, ORDER3, INVOICE3)
echo "[POST] /api/email for CLIENT2 with containers and orders (BOOKING3, CONTAINER2, ORDER3, INVOICE3)"
echo "Request: booking=$BOOKING3, containers=[$CONTAINER2], orders=[$ORDER3], invoices=[$INVOICE3]"
echo "Response:"
curl -s -X POST http://localhost:8080/api/email \
  -H "Content-Type: application/json" \
  -H "X-Client-ID: $CLIENT2" \
  -d '{
    "booking": "'$BOOKING3'",
    "containers": [
      { "container": "'$CONTAINER2'" }
    ],
    "orders": [
      { "purchase": "'$ORDER3'", "invoices": [ { "invoice": "'$INVOICE3'" } ] }
    ]
  }'
echo -e "\n---\n"

# GET requests for loaded data
echo "Get booking for $BOOKING1"
curl -X GET "http://localhost:8080/api/bookings/$BOOKING1" -H "X-Client-ID: $CLIENT1"
echo -e "\n---\n"

echo "Get booking for $BOOKING2"
curl -X GET "http://localhost:8080/api/bookings/$BOOKING2" -H "X-Client-ID: $CLIENT2"
echo -e "\n---\n"

echo "Get booking for $BOOKING2"
curl -X GET "http://localhost:8080/api/bookings/$BOOKING3" -H "X-Client-ID: $CLIENT2"
echo -e "\n---\n"

# Get containers for CLIENT1
echo "Get containers for CLIENT1"
curl -X GET "http://localhost:8080/api/containers" -H "X-Client-ID: $CLIENT1"
echo -e "\n---\n"

# Get containers for CLIENT2
echo "Get containers for CLIENT2"
curl -X GET "http://localhost:8080/api/containers" -H "X-Client-ID: $CLIENT2"
echo -e "\n---\n"

# Get orders for CLIENT1
echo "Get orders for CLIENT1"
curl -X GET "http://localhost:8080/api/orders" -H "X-Client-ID: $CLIENT1"
echo -e "\n---\n"

# Get orders for CLIENT2
echo "Get orders for CLIENT2"
curl -X GET "http://localhost:8080/api/orders" -H "X-Client-ID: $CLIENT2"
echo -e "\n---\n"

# Get orders for a container for CLIENT1 (CONTAINER1)
echo "Get orders for a container for CLIENT1 (CONTAINER1)"
curl -X GET "http://localhost:8080/api/containers/$CONTAINER1/orders" -H "X-Client-ID: $CLIENT1"
echo -e "\n---\n"

# Get orders for a container for CLIENT2 (CONTAINER2)
echo "Get orders for a container for CLIENT2 (CONTAINER2)"
curl -X GET "http://localhost:8080/api/containers/$CONTAINER2/orders" -H "X-Client-ID: $CLIENT2"
echo -e "\n---\n"

# Get containers for an order for CLIENT1 (ORDER1)
echo "Get containers for an order for CLIENT1 (ORDER1)"
curl -X GET "http://localhost:8080/api/orders/$ORDER1/containers" -H "X-Client-ID: $CLIENT1"
echo -e "\n---\n"

# Get containers for an order for CLIENT2 (ORDER2)
echo "Get containers for an order for CLIENT2 (ORDER2)"
curl -X GET "http://localhost:8080/api/orders/$ORDER2/containers" -H "X-Client-ID: $CLIENT2"
echo -e "\n---\n"
