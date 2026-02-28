grid:
	docker compose -f docker-compose.yml -f docker-compose.grid.yml up --build --scale chrome=3

down:
	docker compose down -v

logs:
	docker compose logs -f