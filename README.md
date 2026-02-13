# PI_fixed
hunter app

## Banco de Dados

1. Crie o banco `hunter_db` no PostgreSQL: `createdb hunter_db`.
2. Execute o script `database_setup.sql` com o `psql`: `psql -d hunter_db -f database_setup.sql`.
3. As credenciais padrão são usuário `postgres` e senha `0608`. Ajuste `app.py` e os scrapers caso use outros valores.
