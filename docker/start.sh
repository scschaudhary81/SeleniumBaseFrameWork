PROJECT_NAME=$1
NODE_COUNT=$2
FILE_NAME=$3

if [ -z "$PROJECT_NAME" ]; then
  echo "Parameter 1 Error: Project name is required."
  exit 1
fi

if [ -z "$NODE_COUNT" ]; then
  echo "Parameter 2 Error: No of nodes required for selenium node."
  exit 1
fi

if [ -z "$FILE_NAME" ]; then
  echo "Parameter 3 Error: Please Provide Docker file path."
  exit 1
fi


echo "Starting Selenium Grid with project name: $PROJECT_NAME"

# Start docker compose with custom project name
docker compose -f "$FILE_NAME" -p "$PROJECT_NAME" up --scale selenium-node="$NODE_COUNT" -d
sleep 10


DOCKER_PORT=$(docker port "${PROJECT_NAME}-selenium-hub-1" 4444/tcp | awk -F':' '{print $2}')

echo "$DOCKER_PORT"
ENV_NAME="SEL_HUB_PORT"
export "$ENV_NAME"="$DOCKER_PORT"


echo "Port Configured for selenium HUB"
echo "0.0.0.0:${!ENV_NAME}"


