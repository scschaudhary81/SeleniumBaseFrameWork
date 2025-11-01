project_name=$1

if [ -z "${project_name}" ]; then
  echo "Please Provide a valid project name used to start selenium grid"
  exit 1
fi

docker compose -p "${project_name}"  down
