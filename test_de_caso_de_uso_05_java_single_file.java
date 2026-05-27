// tests/usecase/status_simulator_usecase_test.go
package usecase_test

import (
	"os/exec"
	"testing"
)

func TestCasoDeUsoStatusSimulador(t *testing.T) {

	cmd := exec.Command(
		"./simulador",
		"status",
	)

	err := cmd.Run()

	if err != nil {
		t.Fatal("Falha no caso de uso de status do simulador")
	}
}
