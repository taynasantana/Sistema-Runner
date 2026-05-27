// tests/usecase/stop_simulator_usecase_test.go
package usecase_test

import (
	"os/exec"
	"testing"
)

func TestCasoDeUsoPararSimulador(t *testing.T) {

	cmd := exec.Command(
		"./simulador",
		"stop",
	)

	err := cmd.Run()

	if err != nil {
		t.Fatal("Falha no caso de uso de parar simulador")
	}
}
