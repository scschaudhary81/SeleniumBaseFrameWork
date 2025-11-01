docker compose -p custom_name -f selenium_grid.yaml  down
docker compose -p custom_name -f selenium_grid.yaml up --scale selenium-node=2 -d
