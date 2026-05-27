// tests/usecase/start_simulator_usecase_test.go
package usecase_test

import (
	"os/exec"
	"testing"
)

func TestCasoDeUsoIniciarSimulador(t *testing.T) {

	cmd := exec.Command(
		"./simulador",
		"start",
	)

	err := cmd.Run()

	if err != nil {
		t.Fatal("Falha no caso de uso de iniciar simulador")
	}
}
