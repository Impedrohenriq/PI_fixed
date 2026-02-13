# 🎯 Hunter - Guia de Instalação

Este guia irá te ajudar a configurar o projeto Hunter do zero após formatar o computador.

---

## 📥 1. Softwares Necessários

### 1.1 Python (3.11 ou superior)
- Download: https://www.python.org/downloads/
- **IMPORTANTE:** Marque a opção "Add Python to PATH" durante a instalação

### 1.2 PostgreSQL 18
- Download: https://www.postgresql.org/download/windows/
- Durante a instalação:
  - Defina a senha do usuário postgres como: `0608`
  - Mantenha a porta padrão: `5432`

### 1.3 Google Chrome
- Download: https://www.google.com/chrome/
- Necessário para o scraping funcionar

### 1.4 Git
- Download: https://git-scm.com/download/win
- Instale com as opções padrão

### 1.5 VS Code
- Download: https://code.visualstudio.com/
- Extensões recomendadas:
  - Python
  - GitHub Copilot

---

## 🚀 2. Configuração do Projeto

### 2.1 Clonar o repositório
```powershell
cd C:\Users\SEU_USUARIO\Downloads
git clone https://github.com/Impedrohenriq/PI_fixed.git
cd PI_fixed
```

### 2.2 Criar ambiente virtual Python
```powershell
python -m venv .venv
.\.venv\Scripts\Activate
pip install -r requirements.txt
```

### 2.3 Criar o banco de dados
```powershell
$env:PGPASSWORD = "0608"
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -c "CREATE DATABASE hunter_db;"
```

### 2.4 Restaurar os dados do banco
```powershell
$env:PGPASSWORD = "0608"
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -d hunter_db -f database_dump.sql
```

---

## ▶️ 3. Executar o Projeto

### 3.1 Iniciar a API
```powershell
cd C:\Users\SEU_USUARIO\Downloads\PI_fixed
.\.venv\Scripts\Activate
python -m uvicorn app:app --reload --host 0.0.0.0 --port 8000
```

### 3.2 Acessar o site
- API: http://localhost:8000
- Documentação: http://localhost:8000/docs
- Frontend: Abra o arquivo `index.html` ou `buscar.html` no navegador

---

## 🔄 4. Rodar o Scraping (Opcional)

Para coletar novos produtos da Kabum:
```powershell
cd C:\Users\SEU_USUARIO\Downloads\PI_fixed\scraping
python buscar_produtoskabum.py
```

---

## ⚠️ Observações

- Se a senha do PostgreSQL for diferente de `0608`, edite o arquivo `app.py` (linha 20)
- O Chrome precisa estar instalado em `C:\Program Files\Google\Chrome\Application\chrome.exe`
- Para o app Android, instale o Android Studio

---

## 📞 Suporte

Se tiver problemas, peça ajuda ao Copilot seguindo este arquivo passo a passo!
