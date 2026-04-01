
 Requisito principal do sistema

O sistema deve facilitar a execução de aplicações Java via linha de comando, escondendo a complexidade de configuração do Java para o usuário.
Ou seja, o usuário consegue executar funcionalidades do assinador.jar através de um CLI simples.

Requisitos Funcionais
Os requisitos funcionais estão organizados em histórias de usuário (US-01 a US-05).

US-01 — Invocar o assinador.jar via CLI

O sistema deve permitir executar comandos que chamam o assinador.jar.
O CLI deve permitir:
Criar assinatura digital
Validar assinatura digital
Executar assinador localmente
Executar assinador como servidor HTTP
O CLI também deve:
Iniciar o assinador.jar
Detectar se já existe instância rodando
Reutilizar servidor existente
Permitir parar o assinador
Permitir encerramento automático por timeout

Exemplo de comandos:
assinatura sign
assinatura validate
assinatura start
assinatura stop

US-02 — Simular assinatura digital com validação de parâmetros

O assinador.jar deve:

✔ validar todos os parâmetros recebidos
✔ retornar erro quando parâmetros estiverem inválidos
✔ simular criação de assinatura
✔ simular validação de assinatura

Importante:

❗ não é assinatura criptográfica real.

É apenas simulação.

Exemplo de resposta simulada
{
 "signatureId": "abc123",
 "algorithm": "SHA256withRSA",
 "status": "SIGNED"
}
US-03 — Gerenciar o Simulador do HubSaúde

O CLI deve permitir controlar o simulador.jar.

Funções obrigatórias:

✔ iniciar simulador
✔ parar simulador
✔ verificar status do simulador

Exemplo:

simulador start
simulador stop
simulador status

O CLI também deve:

✔ baixar automaticamente o simulador.jar do GitHub
✔ não baixar novamente se já existir localmente

US-04 — Provisionar JDK automaticamente

O sistema deve verificar se existe Java instalado.

Se não existir:

✔ baixar automaticamente o JDK compatível
✔ armazenar localmente
✔ reutilizar em execuções futuras

Local sugerido:

~/.hubsaude/jdk/
US-05 — Distribuição multiplataforma

O projeto deve fornecer executáveis prontos.

Plataformas obrigatórias:

✔ Windows amd64
✔ Linux amd64
✔ macOS amd64

Exemplo de arquivos:

assinatura-1.0.0-windows-amd64.exe
assinatura-1.0.0-linux-amd64.AppImage
assinatura-1.0.0-macos-amd64.dmg

Esses arquivos devem ser publicados no:
