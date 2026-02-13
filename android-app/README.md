# HunterMobile (Android)

Aplicativo Android nativo (Kotlin + XML) conectado ao Firebase Firestore para consumir os dados migrados do Hunter.

## Pré-requisitos

- Android Studio Iguana ou mais recente
- JDK 17 (Android Studio já inclui)
- Projeto Firebase com Firestore habilitado
- Arquivo `google-services.json` gerado pelo console Firebase (Configurações do projeto > Seus apps > Android)

## Configuração

1. Copie `google-services.json` para `android-app/app/`.
2. Opcional: ajuste o `applicationId` em `app/build.gradle` e no console Firebase se desejar outro pacote.
3. Sincronize o Gradle no Android Studio (`File > Sync Project with Gradle Files`).
4. Execute em um dispositivo físico ou emulador Android 7.0 (API 24) ou superior.

## Funcionalidades

- Lista unificada de produtos das coleções `produtos_kabum` e `produtos_mercadolivre`.
- Atualização por swipe-to-refresh.
- Estados de carregamento, vazio e erros amigáveis.
- Abertura do link do produto no navegador nativo.

## Estrutura principal

- `HunterApp.kt`: inicializa o Firebase.
- `FirestoreRepository.kt`: encapsula leituras do Firestore.
- `MainViewModel.kt`: coordena carregamento, estados e exposição dos dados.
- `MainActivity.kt` + layouts XML: UI responsiva pronta para celulares.

## Próximos passos sugeridos

- Integrar Firebase Authentication para filtrar dados por usuário, se necessário.
- Criar telas adicionais para feedbacks e alertas utilizando as subcoleções por usuário.
- Configurar Crashlytics / Performance Monitoring conforme a necessidade do produto.
