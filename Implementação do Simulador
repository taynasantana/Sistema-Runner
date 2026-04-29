


# IMPLEMENTAÇÃO DO SIMULADOR 

## Visão Geral

O módulo **Simulador** é responsável por gerenciar o ciclo de vida do serviço de assinatura digital executado via Java no Sistema Runner. Sua função é permitir que o usuário inicie, interrompa e consulte o estado do serviço de forma simples, utilizando comandos em linha de comando.

A implementação foi desenvolvida em **Go**, seguindo a mesma arquitetura modular adotada no CLI principal (`assinatura`), com separação por responsabilidades e uso da biblioteca **Cobra** para definição dos comandos.

---


## Arquivo Principal

### `cmd/simulador/main.go`

```go
package main

import "simulador/internal/cli"

func main() {
	cli.Execute()
}
```

Este arquivo é o ponto de entrada da aplicação. Sua única responsabilidade é inicializar a execução do CLI.

---

## Comando Raiz

### `internal/cli/root.go`

```go
package cli

import (
	"fmt"
	"os"

	"github.com/spf13/cobra"
)

var rootCmd = &cobra.Command{
	Use:   "simulador",
	Short: "Gerencia o ciclo de vida do simulador",
}

func Execute() {
	if err := rootCmd.Execute(); err != nil {
		fmt.Println(err)
		os.Exit(1)
	}
}

func init() {
	rootCmd.AddCommand(startCmd)
	rootCmd.AddCommand(stopCmd)
	rootCmd.AddCommand(statusCmd)
}
```

O comando raiz define o comportamento principal da aplicação e registra os subcomandos disponíveis:

* `start`
* `stop`
* `status`

---

## Comando de Inicialização

### `internal/cli/start.go`

```go
package cli

import (
	"fmt"
	"os"
	"os/exec"

	"github.com/spf13/cobra"
)

var startCmd = &cobra.Command{
	Use:   "start",
	Short: "Inicia o simulador",

	Run: func(cmd *cobra.Command, args []string) {
		command := exec.Command(
			"java",
			"-jar",
			"assinador.jar",
			"server",
			"8080",
		)

		err := command.Start()
		if err != nil {
			fmt.Println("Erro ao iniciar simulador:", err)
			return
		}

		_ = os.WriteFile(".simulador.pid", []byte(fmt.Sprint(command.Process.Pid)), 0644)

		fmt.Println("Simulador iniciado com sucesso (porta 8080)")
	},
}
```

O comando `start` inicia o processo Java em segundo plano e armazena o identificador do processo (PID) em um arquivo local chamado `.simulador.pid`.

Esse arquivo é utilizado posteriormente para:

* verificar se o simulador está ativo
* encerrar o processo corretamente

---

## Comando de Encerramento

### `internal/cli/stop.go`

```go
package cli

import (
	"fmt"
	"os"
	"strconv"
	"syscall"

	"github.com/spf13/cobra"
)

var stopCmd = &cobra.Command{
	Use:   "stop",
	Short: "Encerra o simulador",

	Run: func(cmd *cobra.Command, args []string) {
		data, err := os.ReadFile(".simulador.pid")
		if err != nil {
			fmt.Println("Simulador não está em execução")
			return
		}

		pid, err := strconv.Atoi(string(data))
		if err != nil {
			fmt.Println("PID inválido")
			return
		}

		process, err := os.FindProcess(pid)
		if err != nil {
			fmt.Println("Processo não encontrado")
			return
		}

		_ = process.Signal(syscall.SIGKILL)
		_ = os.Remove(".simulador.pid")

		fmt.Println("Simulador encerrado")
	},
}
```

O comando `stop` realiza a leitura do PID salvo anteriormente, localiza o processo correspondente e encerra sua execução.

Após o encerramento, o arquivo `.simulador.pid` é removido para manter o controle de estado consistente.

---

## Comando de Status

### `internal/cli/status.go`

```go
package cli

import (
	"fmt"
	"os"

	"github.com/spf13/cobra"
)

var statusCmd = &cobra.Command{
	Use:   "status",
	Short: "Exibe status do simulador",

	Run: func(cmd *cobra.Command, args []string) {
		_, err := os.Stat(".simulador.pid")
		if err != nil {
			fmt.Println("Simulador parado")
			return
		}

		fmt.Println("Simulador em execução")
	},
}
```

O comando `status` consulta a existência do arquivo `.simulador.pid`.

* Se o arquivo existir, o simulador é considerado em execução
* Se não existir, o simulador é considerado parado

---

## Execução

Após compilar o projeto, os comandos disponíveis são:

```bash
go build -o simulador ./cmd/simulador

./simulador start
./simulador status
./simulador stop
```

---

## Considerações Técnicas

As principais decisões de projeto foram:

* utilização de **Go** pela facilidade de manipulação de processos
* uso de **Cobra** para padronização dos comandos
* persistência do PID em arquivo local para controle de estado
* separação modular para facilitar manutenção e testes

Essa abordagem permite integração direta com o restante do Sistema Runner sem acoplamento excessivo.

---

## Conclusão

O módulo Simulador cumpre o papel de gerenciar o ciclo de vida do serviço de assinatura digital de forma prática e organizada.

Sua implementação fornece:

* inicialização do serviço
* interrupção controlada
* verificação de estado
* integração com o `assinador.jar`

Com isso, o Sistema Runner passa a contar com um mecanismo de controle operacional completo, reforçando sua arquitetura modular e aderência aos requisitos do projeto.

```
```
