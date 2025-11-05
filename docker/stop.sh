project_name=$1

if [ -z "${project_name}" ]; then
  echo "Parameter 1 Error : Please Provide a valid project name used to stop selenium grid"
  exit 1
fi

docker compose -p "${project_name}"  down
